package csci310.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;


@WebServlet("/stockperformance")
public class StockPerformanceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static PrintWriter out;
	HttpSession session = null;
	Calendar from;
	Calendar now;
	Boolean check = false;
	List<ArrayList> myStocks = new ArrayList<ArrayList>();
	List<ArrayList> view = new ArrayList<ArrayList>();
	List<String> jsons = new ArrayList<String>();
	List<ArrayList> portfolioValHistory = new ArrayList<ArrayList>();
	String portfolioJSON;
	
	Boolean dataFetched = false;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		session = request.getSession();
		response.setContentType("text/plain");
		out = response.getWriter();
		System.out.println("Hello from doGet");
		
		myStocks.clear();
		jsons.clear();
		portfolioValHistory.clear();
		view.clear();
		if (session.getAttribute("username") != null) {
	        String username = session.getAttribute("username").toString();
	        //default time period is 1Y
	        from = Calendar.getInstance();
	        from.add(Calendar.YEAR, -1);
	        now = Calendar.getInstance();
  
	        try {
	        	getUserStock(username);
	        } catch (IOException e) {
	        	out.print("stock fail - IOException");
	        	return;
	        } catch (InterruptedException e) {
	        	out.print("stock fail - InterruptedException");
	        	return;
	        }
	
	        //set stocks as session variable for front end
	        session.setAttribute("myStocks", myStocks);
	        session.setAttribute("view", view);
	        session.setAttribute("invalid_error", null);
	
	        try {
	        	calculatePortfolio();
	        } catch (ParseException e) {
	        	out.print("stock fail - ParseException");
	        	return;
	        }
	
	        if (!portfolioValHistory.isEmpty()) {
	        	DecimalFormat f = new DecimalFormat("##.00");
	        	ArrayList<String> holder = portfolioValHistory.get(portfolioValHistory.size()-1);
	        	Double val = Double.parseDouble(holder.get(1));
	        	session.setAttribute("portfolioVal", f.format(val));	
	        }
	
	        //build the graph using the list of stocks
	        buildGraph();    
		}
	}
	
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		session = request.getSession();
		System.out.println("Hello from doPost");
		String action = request.getParameter("action");
		//if user wants to toggle hide/show on graph - DONE
		if(action.equals("portfolioState")) {
			System.out.println("Action is 'portfolioState'");
			String ticker = request.getParameter("ticker");
			for(int i=0; i<myStocks.size(); i++) {
				if(myStocks.get(i).get(0).equals(ticker)){
					if(myStocks.get(i).get(5).equals("Yes")) {
						myStocks.get(i).set(5, "No");
					}else {
						myStocks.get(i).set(5, "Yes");
					}
				}
			}

			try {
				calculatePortfolio();
			} catch (ParseException e) {
				out.print("stock fail - ParseException");
				return;
			}
			buildGraph();
		} 
		
		//add a random stock to graph but don't add it to your portfolio - DONE
		else if(action.equals("viewStock")) {
			System.out.println("Action is 'viewStock'");
			session.setAttribute("invalid_error", null);
			String ticker = request.getParameter("ticker");
			ticker = ticker.toUpperCase();
			String purchase = request.getParameter("datePurchased");
			String sell = request.getParameter("dateSold");
			String numOfShares = request.getParameter("numOfShares");
			
			//make sure you're not already looking at it
			for(int i=0; i<view.size(); i++) {
				if(view.get(i).get(0).equals(ticker)){
					session.setAttribute("invalid_error", "Please enter a stock you're not already viewing");
				}
			}
			
			if(session.getAttribute("invalid_error") == null) {
				System.out.println("invalid_error is null");
				try {
					ArrayList<String> holder = new ArrayList<String>();
					String json = viewStock(ticker, numOfShares, purchase, sell);
					holder.add(ticker);
					holder.add(json);
					holder.add(numOfShares);
					holder.add(purchase);
					holder.add(sell);
					view.add(holder);
					buildGraph();
				//if not valid stock name
				} catch(NullPointerException e) {
					session.setAttribute("invalid_error", "Please enter a valid ticker");
				} catch(FileNotFoundException f) {
					session.setAttribute("invalid_error", "Please enter a valid ticker");
				} catch (ParseException e) {
					session.setAttribute("invalid_error", "Please enter a valid ticker");
				}
			} 
			session.setAttribute("view", view);
		}
		
		//this is just case where user views stock and decides they dont want to see it on the graph anymore
		else if(action.equals("removeViewStock")) {
			System.out.println("Action is 'removeViewStock'");
			String ticker = request.getParameter("removeTicker");
			for(int i=0; i<view.size(); i++) {
				if(view.get(i).get(0).equals(ticker)){
					view.remove(i);
				}
			}
			session.setAttribute("view", view);
			buildGraph();
		} 
		
		//this is for adding stock to database
		else if(action.equals("addStock")) {
			System.out.println("Action is 'addStock'");
			//code to add a stock to your portfolio
			System.out.println("add stock function");
			
			//grab all the info from the frontend
			String username = session.getAttribute("username").toString();
			String ticker = request.getParameter("ticker");
			String purchase = request.getParameter("datePurchased");
			String sell = request.getParameter("dateSold");
			String numOfShare = request.getParameter("numOfShares"); // or "numOfShare"?
//			Calendar purchase = request.getParameter("datePurchased");
//			Calendar sell = request.getParameter("dateSold");
//			Double numOfShare = request.getParameter("numOfShare");
			
			System.out.println("ticker");
			//if stock is already in "view" array list and you are adding from there
			//remove it from view array
			for(int i=0; i<view.size(); i++) {
				if(view.get(i).get(0).equals(ticker)){
					view.remove(i);
				}
			}
			
			//add stock to firebase here
			//addStock(username, ticker, purchase, sell, numOfShare);
			
			//regenerate all the variables
			myStocks = new ArrayList<ArrayList>();
			jsons = new ArrayList<String>();
			portfolioValHistory = new ArrayList<ArrayList>();
			portfolioJSON = "";
			
			//recalculate the portfolio
			try {
				calculatePortfolio();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			//build the graph using the list of stocks
			buildGraph();
			
		}
		//change calendar time period
		else if(action.equals("changeTimePeriod")){
			System.out.println("Action is 'changeTimePeriod'");
			//these are of type "Calendar"
			String fromString = request.getParameter("from");
			String toString = request.getParameter("to");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				from.setTime(sdf.parse(fromString));
				now.setTime(sdf.parse(toString));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			jsons = new ArrayList<String>();
			portfolioValHistory = new ArrayList<ArrayList>();
			portfolioJSON = "";
			
			//pass the new dates into build stock jsons
			try {
				calculatePortfolio();
				for(int i=0; i<view.size(); i++) {
					ArrayList<String> holder = view.get(i);
					String json = viewStock(holder.get(0), holder.get(2), holder.get(3), holder.get(4));
					holder.set(1, json);
					view.set(i, holder);
				}
				session.setAttribute("view", view);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			buildGraph();
		} 
	}
	
	String viewStock(String ticker, String numOfShares, String purchase, String sell) throws IOException, ParseException {
		List<HistoricalQuote> history = YahooFinance.get(ticker, from, now, Interval.DAILY).getHistory();
		//this is for formatting it for the graph that i am using
		Map<Object,Object> map = null;
		List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
		for(int i=0; i<history.size(); i++) {
			Calendar date = history.get(i).getDate();
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH);
			int day = date.get(Calendar.DAY_OF_MONTH);
			DateFormatSymbols symbols = new DateFormatSymbols();
			String label = day + " " + symbols.getShortMonths()[month] + " " + year;
			Double close = history.get(i).getClose().doubleValue();
			Double shares = Double.parseDouble(numOfShares);
			close = close * shares;
			//check if user owned stock during this point in time before showing on the graph
			String holder = year + "-" + (month + 1) + "-" + day;
			boolean owned = ownedCheck(holder, purchase, sell);
			if(owned == true) {
				map = new HashMap<Object,Object>(); map.put("label", label); map.put("y", close); 
				list.add(map);
			}
			//otherwise add blank date
			else {
				map = new HashMap<Object,Object>(); map.put("label", label); map.put("y", 0.00); 
				list.add(map);
			}
		}
		String stockHistory = new Gson().toJson(list);
		System.out.println(stockHistory);
		return stockHistory;
	}
	
	Boolean ownedCheck(String holder, String dp, String sd) throws ParseException {
		boolean owned = false;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date historicalDate = (Date)formatter.parse(holder);
		Date datePurchased = (Date)formatter.parse(dp);
		Date sellDate;
		if(sd.equals("")) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			sellDate = calendar.getTime();
			
		} else {
			sellDate = (Date)formatter.parse(sd);
		}
		
		if(sellDate.after(historicalDate) && datePurchased.before(historicalDate)){
			owned = true;
		} else if(sellDate.equals(historicalDate) && datePurchased.before(historicalDate)) {
			owned = true;
		} else if(sellDate.after(historicalDate) && datePurchased.equals(historicalDate)) {
			owned = true;
		} else if(sellDate.equals(historicalDate) && datePurchased.equals(historicalDate)) {
			owned = true;
		}
		
		return owned;
	}
	
	int calculatePortfolio() throws IOException, ParseException {
		portfolioValHistory = new ArrayList<ArrayList>();
		//for loop to run through list of users stocks
		for(int s=0; s<myStocks.size(); s++) {
			String ticker = (String) myStocks.get(s).get(0);
			List<HistoricalQuote> history = YahooFinance.get(ticker, from, now, Interval.DAILY).getHistory();
			
			for(int i=0; i<history.size(); i++) {
				Calendar date = history.get(i).getDate();
				int year = date.get(Calendar.YEAR);
				int month = date.get(Calendar.MONTH);
				int day = date.get(Calendar.DAY_OF_MONTH);
				DateFormatSymbols symbols = new DateFormatSymbols();
				String label = day + " " + symbols.getShortMonths()[month] + " " + year;
				Double close = history.get(i).getClose().doubleValue();
				Double shares = Double.parseDouble((String) myStocks.get(s).get(2));
				
				//check if user owned stock during this point in time add to portfolio value
				String holder = year + "-" + (month + 1) + "-" + day;
				
				boolean owned = ownedCheck(holder, (String)myStocks.get(s).get(3), (String)myStocks.get(s).get(4));
					
				//create portfolio value at that index
				//if stock is supposed to be calculated in the portfolio
				if(myStocks.get(s).get(5).equals("Yes")) {
					addPortfolioValues(i, close, shares, label, owned);
				}
			}
		}
		
		buildPortfolioJSON();
		return portfolioValHistory.size();
	}
	
	void addPortfolioValues(Integer i, Double close, Double shares, String label, Boolean owned) throws IOException {
		Double portfolioValue = close * shares;
		ArrayList<String> val = new ArrayList<String>();
	
		//if there is already a value at that index, add to it
		try {
			portfolioValHistory.get(i);
			ArrayList<String> holder = portfolioValHistory.get(i);
			//only add if purchased before this date
			if(owned == true) {
				portfolioValue += Double.parseDouble(holder.get(1));
				val.add(label);
				val.add(String.valueOf(portfolioValue));
				portfolioValHistory.set(i, val);
			}
		} catch ( IndexOutOfBoundsException e ) {
			//otherwise create new value
			//if purchased before add value
			if(owned == true) {
				val.add(label);
				val.add(String.valueOf(portfolioValue));
				portfolioValHistory.add(i, val);
			} 
			//otherwise initialize the value at the index so it can be displayed on same graph
			else {
				val.add(label);
				val.add(String.valueOf(0));
				portfolioValHistory.add(i, val);
			}
			
		}
	}
	
	
	void buildPortfolioJSON() throws IOException {
		Map<Object,Object> map = null;
		List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
		//for loop to run through portfolio values
		for(int s=0; s<portfolioValHistory.size(); s++) {
			ArrayList<String> val = portfolioValHistory.get(s);
			String label = val.get(0);
			Double y = Double.parseDouble(val.get(1));
			map = new HashMap<Object,Object>(); map.put("label", label); map.put("y", y);
			list.add(map);
		}
		
		portfolioJSON = new Gson().toJson(list);
		System.out.println(portfolioJSON);
	}

	void buildStockJSONS(Calendar from, Calendar now) throws IOException, ParseException {
		//Below is the code to build/format json for graph
		//for loop to run through list of users stocks
		for(int s=0; s<myStocks.size(); s++) {
			
			//this is where we need to deal with time period (Done!)
			String ticker = (String) myStocks.get(s).get(0);
			List<HistoricalQuote> history = YahooFinance.get(ticker, from, now, Interval.DAILY).getHistory();
			
			//this is for formatting it for the graph that i am using
			Map<Object,Object> map = null;
			List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
			for(int i=0; i<history.size(); i++) {
				Calendar date = history.get(i).getDate();
				int year = date.get(Calendar.YEAR);
				int month = date.get(Calendar.MONTH);
				int day = date.get(Calendar.DAY_OF_MONTH);
				DateFormatSymbols symbols = new DateFormatSymbols();
				String label = day + " " + symbols.getShortMonths()[month] + " " + year;
				Double close = history.get(i).getClose().doubleValue();
				Double shares = Double.parseDouble((String) myStocks.get(s).get(2));
				
				//check if user owned stock during this point in time add to portfolio value
				String holder = year + "-" + (month + 1) + "-" + day;
				DateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
				Date datePurchased = null;
				Date sellDate = null;
				Date historicalDate = null;
				boolean owned = false;
				try {
					datePurchased = (Date)formatter.parse((String)myStocks.get(s).get(3));
					sellDate = (Date)formatter.parse((String)myStocks.get(s).get(4));
					historicalDate = (Date)formatter.parse(holder);
					
					owned = sellDate.after(historicalDate);
					owned = datePurchased.before(historicalDate);
				} catch (ParseException pe) {
					System.out.println("ERROR - Failed to format date: " + pe.getLocalizedMessage());
				}
	
				//create portfolio value at that index
				addPortfolioValues(i, close, shares, label, owned);
			
				map = new HashMap<Object,Object>(); 
				map.put("label", label); 
				map.put("y", close); 
				list.add(map);
			}
			String stockHistory = new Gson().toJson(list);
			System.out.println(stockHistory);
			
			//add to big list
			jsons.add(stockHistory);
		}
		
		buildPortfolioJSON();
	}
	
	void buildGraph() throws IOException {
		//chart to display different stocks
		String theChart =  "<script type=\"text/javascript\">\n" + 
				"				var chart = new CanvasJS.Chart(\"chartContainer\", {\n" + 
				"					zoomEnabled: true,\n" + 
				"					theme: \"light2\",\n" + 
				"					title: {\n" + 
				"						text: \"\"\n" + 
				"					},\n" + 
				"					axisX: {\n" + 
				"						title: \"\"\n" + 
				"					},\n" + 
				"					axisY: {\n" + 
				"						title: \"\",\n" + 
				"						includeZero: true\n" + 
				"					},\n" + 
				"					legend: {" +
				"						verticalAlign: \"top\",\n" + 
				"						horizontalAlign: \"center\",\n" + 
				"						dockInsidePlotArea: true,\n" + 
				"					},\n" + 
				"					data: [\n";
		
		//add the portfolio
		theChart += "{\n" +
						"type: \"line\",\n" + 
						"name: \"Portfolio\",\n" + 
						"showInLegend: true,\n" +
						"yValueFormatString: \"$##0.00\",\n" + 
						"dataPoints :" + portfolioJSON +
					"},\n";	
		
		//add any stocks you want to view
		for(int i=0; i<view.size(); i++) {
			theChart += "{\n" +
						"type: \"line\",\n" + 
						"name: \"" + view.get(i).get(0) + "\",\n" +
						"showInLegend: true,\n" +
						"yValueFormatString: \"$##0.00\",\n" + 
						"dataPoints :" + view.get(i).get(1) +
					"},\n";	
		}
		
		//add the end code
		theChart +=
				"					]\n" + 
				"				});\n" + 
				"				chart.render();\n" + 
//				"			}\n" + 
				"		</script>";
		session.setAttribute("chart", theChart);
		
	}
	
	public FirebaseApp initializeFireBase() throws IOException {
		if (FirebaseApp.getApps().isEmpty()) {
			FileInputStream serviceAccount;
			serviceAccount = new FileInputStream("stock16-serviceaccount.json");
			@SuppressWarnings("deprecation")
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://stock16-e451e.firebaseio.com").build();
			return FirebaseApp.initializeApp(options);
		}
		return null;
	}
	
	public Map<String, Object> addStock(String username, String symbol, Calendar purchase, Calendar sell, double numOfShare) throws IOException {
		initializeFireBase();
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("portfolio");
		Map<String, Object> updates = new HashMap<>();
		Map<String, String> content = new HashMap<>();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String fromTime = format1.format(purchase.getTime());
		String toTime = format1.format(sell.getTime());
		content.put("name", YahooFinance.get(symbol).getName());
		content.put("from", fromTime);
		content.put("to", toTime);
		content.put("shares", Double.toString(numOfShare));
		updates.put(symbol, content);
		
		ref.updateChildrenAsync(updates);
		return updates;
	}
	
	// retrieve stock symbols
	public void getUserStock(String username) throws IOException, InterruptedException {
		initializeFireBase();
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference ref = database.getReference().child("users").child(username).child("portfolio");
		
		System.out.println(ref.toString());
		
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				if(snapshot.exists()) {
					for(DataSnapshot child : snapshot.getChildren()) {
						//get all values of stock
						ArrayList<String> stock = new ArrayList<String>();
						//add ticker
						String ticker = child.getKey().toString();
						stock.add(ticker);
						System.out.println("INFO FOR: " + ticker);
						String name = child.child("name").getValue().toString();
						String shares = child.child("shares").getValue().toString();
						String from = child.child("from").getValue().toString();
						String to = child.child("to").getValue().toString();
						System.out.println("    " + name);
						System.out.println("    " + shares);
						System.out.println("    " + from);
						System.out.println("    " + to);
						System.out.println();
						stock.add(name);
						stock.add(shares);
						stock.add(from);
						stock.add(to);
						//whether or not it should be shown on graph
						stock.add("Yes");
						//add to big array
						myStocks.add(stock);
					}
					dataFetched = true;
				}
			}
	
			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println(error.getMessage());
				
			}
		});
		
		for (int i = 0; i < 5; ++i) {
			TimeUnit.SECONDS.sleep(1);
			if (dataFetched) {
				break;
			}
		}
	}
	
	public void removeStock(String username, String symbol) throws IOException {
		initializeFireBase();
		FirebaseDatabase.getInstance().getReference().child("users").child(username).child("portfolio").child(symbol).removeValueAsync();
	}

}