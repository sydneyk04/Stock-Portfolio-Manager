package csci310.servlets;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

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
		
		myStocks.clear();
		jsons.clear();
		portfolioValHistory.clear();
		view.clear();
		if (session.getAttribute("username") != null) {
        String username = session.getAttribute("username").toString();
        //default time period is 1Y
        from = Calendar.getInstance();
        from.add(Calendar.MONTH, -3);
        now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        String graphRangeFrom = sdf.format(from.getTime());
        String graphRangeTo = sdf.format(now.getTime());
	        try {
	        	getUserStock(username);
	        } catch (IOException e) {
	        	out.print("stock fail - IOException");
	        	return;
	        } catch (InterruptedException e) {
	        	out.print("stock fail - InterruptedException");
	        	return;
	        } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	        //set stocks as session variable for front end
	        session.setAttribute("from", from);
	        session.setAttribute("now", now);
	        session.setAttribute("myStocks", myStocks);
	        session.setAttribute("view", view);
	        session.setAttribute("invalid_error", null);
	        session.setAttribute("uploadCSVError", null);
	        session.setAttribute("graphRangeFrom", graphRangeFrom);
	        session.setAttribute("graphRangeTo", graphRangeTo);
	        session.setAttribute("graphRangeError", null);
	        

	        try {
	        	calculatePortfolio();
	        } catch (ParseException e) {
	        	out.print("stock fail - ParseException");
	        	return;
	        }
	
	       
	        //build the graph using the list of stocks
	        buildGraph();    
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		
		response.setContentType("text/html;charset=UTF-8");
		out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		session = request.getSession();
		String action = request.getParameter("action");
		//if user wants to toggle hide/show on graph - DONE
		if(action.equals("portfolioState")) {
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
				buildGraph();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		} 
		
		else if(action.equals("showViewStock")) {
			
			String ticker = request.getParameter("ticker");
			for(int i=0; i<view.size(); i++) {
				if(view.get(i).get(0).equals(ticker)){
					if(view.get(i).get(5).equals("Yes")) {
						view.get(i).set(5, "No");
					}else {
						view.get(i).set(5, "Yes");
					}
				}
			}
			
			buildGraph();
		}
		
		else if(action.equals("toggleSP")) {
			
			if(myStocks.get(0).get(5).equals("Yes")) {
				myStocks.get(0).set(5, "No");
			}else {
				myStocks.get(0).set(5, "Yes");
			}
			buildGraph();
		} 
		
		//add a random stock to graph but don't add it to your portfolio - DONE
		else if(action.equals("viewStock")) {
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
				try {
					ArrayList<String> holder = new ArrayList<String>();
					String json = viewStock(ticker, numOfShares, purchase, sell);
					holder.add(ticker);
					holder.add(json);
					holder.add(numOfShares);
					holder.add(purchase);
					holder.add(sell);
					holder.add("Yes");
					view.add(holder);
					buildGraph();
				//if not valid stock name
				} catch(NullPointerException e) {
					session.setAttribute("invalid_error", "Please enter a valid ticker");
				} catch(FileNotFoundException f) {
					session.setAttribute("invalid_error", "Please enter a valid ticker");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} 
			session.setAttribute("view", view);
		}
		
		//this is just case where user views stock and decides they dont want to see it on the graph anymore
		else if(action.equals("removeViewStock")) {
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
			
			session.setAttribute("failedAdd", null);
			String username = session.getAttribute("username").toString();
			String ticker = request.getParameter("ticker");
			ticker = ticker.toUpperCase();
			String purchase = request.getParameter("datePurchased");
			String sell = request.getParameter("dateSold");
			String numOfShares = request.getParameter("numOfShares");
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Calendar datePurchased = Calendar.getInstance();
			Calendar sellDate = Calendar.getInstance();
			try {
				datePurchased.setTime(formatter.parse(purchase));
				if(sell.equals("")) {
					sellDate.add(Calendar.DAY_OF_YEAR, 1);		
				} else {
					sellDate.setTime(formatter.parse(sell));
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			// if invalid number of shares 
			if(Double.parseDouble(numOfShares) <= 0) {
				session.setAttribute("failedAdd", "Invalid number of shares");
			}
			
			//if invalid date
			if(datePurchased.after(Calendar.getInstance())) {
				session.setAttribute("failedAdd", "Please enter a valid date.");			
			}
			
			// if date sold before date purchased
			if(datePurchased.after(sellDate)) {
				session.setAttribute("failedAdd", "Date sold cannot be before date purchased.");
			}
			
			//if you already own the stock dont let user add it and remove from view?
			for(int i=0; i<myStocks.size(); i++) {
				if(myStocks.get(i).get(0).equals(ticker)){
					session.setAttribute("failedAdd", "Uh, oh! Looks like you already own this stock.");
				}
			}
			
			if(session.getAttribute("failedAdd") == null) {
				//if its in view remove it
				for(int i=0; i<view.size(); i++) {
					if(view.get(i).get(0).equals(ticker)){
						view.remove(i);
					}
				}
				session.setAttribute("view", view);
				
				try {
					addStock(username, ticker, datePurchased, sellDate, Double.parseDouble(numOfShares));
				
					ArrayList<String> stock = new ArrayList<String>();
					stock.add(ticker);
					stock.add(YahooFinance.get(ticker).getName());
					stock.add(numOfShares);
					stock.add(purchase);
					stock.add(sell);
					stock.add("Yes");
					myStocks.add(stock);
					session.setAttribute("myStocks", myStocks);
					calculatePortfolio();
					buildGraph();
				} catch(NullPointerException e) {
					session.setAttribute("failedAdd", "Unable to add this stock");
				} catch(FileNotFoundException f) {
					session.setAttribute("failedAdd", "Unable to add this stock");
				} catch (ParseException e) {
					session.setAttribute("failedAdd", "Unable to add this stock");
				}
			}
			
			if (!portfolioValHistory.isEmpty()) {
				DecimalFormat f = new DecimalFormat("##.00");
				ArrayList<String> holder = portfolioValHistory.get(portfolioValHistory.size()-1);
				Double val = Double.parseDouble(holder.get(1));
				session.setAttribute("portfolioVal", f.format(val));	

	        	if (portfolioValHistory.size() > 1) {
	        		// yesterday's portfolio value
		        	ArrayList<String> prevHolder = portfolioValHistory.get(portfolioValHistory.size()-2);
		        	Double prevVal = Double.parseDouble(prevHolder.get(1));
		        	Double percentChange = (val - prevVal) / 100;
		        	session.setAttribute("portfolioPercentage", f.format(percentChange));
		        	
	        	}        	
			}
			
		}
		
		else if(action.equals("removeStock")) {
			
			String username = session.getAttribute("username").toString();
			String ticker = request.getParameter("removeStockTicker");
			for(int i=0; i<myStocks.size(); i++) {
				if(myStocks.get(i).get(0).equals(ticker)){
					myStocks.remove(i);
				}
			}
			removeStock(username, ticker);
			session.setAttribute("myStocks", myStocks);

			
			//recalculate the portfolio
			try {
				calculatePortfolio();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			//build the graph using the list of stocks
			buildGraph();
			
			if (!portfolioValHistory.isEmpty()) {
				DecimalFormat f = new DecimalFormat("##.00");
				ArrayList<String> holder = portfolioValHistory.get(portfolioValHistory.size()-1);
				
				Double val = Double.parseDouble(holder.get(1));
				session.setAttribute("portfolioVal", f.format(val));	
	        	
	        	if (portfolioValHistory.size() > 1) {
	        		// yesterday's portfolio value
		        	ArrayList<String> prevHolder = portfolioValHistory.get(portfolioValHistory.size()-2);
		        	Double prevVal = Double.parseDouble(prevHolder.get(1));
		        	Double percentChange = (val - prevVal) / 100;
		        	session.setAttribute("portfolioPercentage", f.format(percentChange));
		        	
	        	}       
			} else {
				session.setAttribute("portfolioVal", "0.00");	
			}
		} 
		
		else if(action.equals("addCSV")) {
			
			String username = session.getAttribute("username").toString();
			String splitBy = ",", csv = request.getParameter("csvContent");
			String[] lines = csv.split("\n");
			ArrayList<ArrayList> newStocks = new ArrayList<ArrayList>();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			if(lines.length < 2) {
				session.setAttribute("uploadCSVError", "Empty CSV file!");
				return;
			}
			for(int i = 1; i < lines.length; i++) {
				
				String[] contents = lines[i].split(splitBy);
				if(lines[i].compareTo(",,,") == 0) {
					continue;
				}
				if(contents.length != 4) {
					session.setAttribute("uploadCSVError", "Malformed CSV file!");
					return;
				}
				String ticker = contents[0];
				ticker = ticker.toUpperCase();
				String purchase = contents[2];
				String sell = contents[3];
				String numOfShares = contents[1];
				
				String[] purchaseDates = purchase.split("/");
				String[] sellDates = sell.split("/");
				
				//malformed dates
				if(purchaseDates.length != 3 || sellDates.length != 3) {
					session.setAttribute("uploadCSVError", "Malformed dates");
					return;
				}
				purchase = "20" + purchaseDates[2] + "-" + purchaseDates[0] + "-" + purchaseDates[1];
				sell = "20" + sellDates[2] + "-" + sellDates[0] + "-" + sellDates[1];
				Calendar datePurchased = Calendar.getInstance();
				Calendar sellDate = Calendar.getInstance();
				try {
					datePurchased.setTime(formatter.parse(purchase));	
					sellDate.setTime(formatter.parse(sell));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				formatter = new SimpleDateFormat("yyyy-MM-dd");
				purchase = formatter.format(datePurchased.getTime());
				sell = formatter.format(sellDate.getTime());
				//invalid ticker
				if(YahooFinance.get(ticker) == null) {
					session.setAttribute("uploadCSVError", "Invalid ticker");
					return;
				}
				
				// if invalid number of shares 
				if(numOfShares.isEmpty() || Double.parseDouble(numOfShares) <= 0) {
					session.setAttribute("uploadCSVError", "Invalid number of shares");
					return;
				}
				
				//if invalid date
				if(datePurchased.after(Calendar.getInstance())) {
					session.setAttribute("uploadCSVError", "Please enter a valid date.");	
					return;
				}
				
				// if date sold before date purchased
				if(datePurchased.after(sellDate)) {
					session.setAttribute("uploadCSVError", "Date sold cannot be before date purchased.");
					return;
				}
				
				//if you already own the stock dont let user add it
				for(int j=0; j<myStocks.size(); j++) {
					if(myStocks.get(j).get(0).equals(ticker)){
						session.setAttribute("uploadCSVError", "You already own one or more of them in the file");
						return;
					}
				}
				session.setAttribute("uploadCSVError", null);
				
				ArrayList<String> stock = new ArrayList<String>();
				stock.add(ticker);
				stock.add(YahooFinance.get(ticker).getName());
				stock.add(numOfShares);
				stock.add(purchase);
				stock.add(sell);
				stock.add("Yes");
				newStocks.add(stock);
				
			}
			
			for(int i = 0; i < newStocks.size(); i++) {
				ArrayList<String> cur = newStocks.get(i);
				String ticker = cur.get(0);
				ticker = ticker.toUpperCase();
				String numOfShares = cur.get(2);	
				String purchase = cur.get(3);
				String sell = cur.get(4);
				
				Calendar datePurchased = Calendar.getInstance();
				Calendar sellDate = Calendar.getInstance();
				try {
					datePurchased.setTime(formatter.parse(purchase));	
					sellDate.setTime(formatter.parse(sell));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				addStock(username, ticker, datePurchased, sellDate, Double.parseDouble(numOfShares));
				myStocks.add(cur);
			}
			try {
				calculatePortfolio();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			buildGraph();
		}
		//change calendar time period
		else if(action.equals("changeTimePeriod")){
			//these are of type "Calendar"
			String fromString = request.getParameter("from");
			String toString = request.getParameter("to");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar newFrom = Calendar.getInstance();
			Calendar minFrom = Calendar.getInstance();
			minFrom.add(Calendar.YEAR, -1);
			minFrom.add(Calendar.DATE, -1);
			try {
				if(fromString.isEmpty()) {
					session.setAttribute("graphRangeError", "Invalid date");
					return;
				}
				newFrom.setTime(sdf.parse(fromString));
				if(newFrom.compareTo(minFrom) < 0) {
					session.setAttribute("graphRangeError", "Invalid date");
					return;
				}
				session.setAttribute("graphRangeError", "");
				from.setTime(sdf.parse(fromString));
				now.setTime(sdf.parse(toString));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			jsons = new ArrayList<String>();
			portfolioValHistory = new ArrayList<ArrayList>();
			portfolioJSON = "";
			
			//fix s and p
			myStocks.get(0).set(3,fromString);
			myStocks.get(0).set(4,toString);
			String temp = "";
			try {
				temp = viewStock("^GSPC", "1", fromString, toString);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			myStocks.get(0).set(6,temp);
			
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
			
			//set session from and to
			session.setAttribute("graphRangeFrom", fromString);
			session.setAttribute("graphRangeTo", toString);

		}
		//selectall, add alls stocks to view
		else if(action.equals("selectViewAll")){
			for(int i = 1; i < myStocks.size(); i++) {
				myStocks.get(i).set(5, "Yes");
			}
			session.setAttribute("myStocks", myStocks);
			try {
				calculatePortfolio();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			buildGraph();
		}
		//deselect all stocks
		else if(action.equals("deselectViewAll")) {
			for(int i = 1; i < myStocks.size(); i++) {
				myStocks.get(i).set(5, "No");
			}
			session.setAttribute("myStocks", myStocks);
			try {
				calculatePortfolio();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
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
	
	void calculatePortfolio() throws IOException, ParseException {
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
				String holder = year + "-" + (month + 1) + "-" + day;
				String label = day + " " + symbols.getShortMonths()[month] + " " + year;
				Double close = 0.00;
				Double shares = Double.parseDouble((String) myStocks.get(s).get(2));
				boolean owned = true;
				
				//if the stock is not the first one, calculate values, otherwise will initilize a 0 line
				if(s != 0) {
					//check if user owned stock during this point in time add to portfolio value
					close = history.get(i).getClose().doubleValue();
					owned = ownedCheck(holder, (String)myStocks.get(s).get(3), (String)myStocks.get(s).get(4));
				}
				
				//create portfolio value at that index
				//if stock is supposed to be calculated in the portfolio
				if(myStocks.get(s).get(5).equals("Yes")) {
					addPortfolioValues(i, close, shares, label, owned);
				}
				
			}
		}
		
		session.setAttribute("portfolioVal", 0.00);
		session.setAttribute("portfolioPercentage", 0);
		
		//set portfolio value
		if (!portfolioValHistory.isEmpty()) {
        	// today's portfolio value
        	DecimalFormat f = new DecimalFormat("##.00");
        	f.setRoundingMode(RoundingMode.HALF_EVEN);
        	ArrayList<String> holder = portfolioValHistory.get(portfolioValHistory.size()-1);
        	Double val = Double.parseDouble(holder.get(1));
        	
        	session.setAttribute("portfolioVal", f.format(val));
        	
        	if (portfolioValHistory.size() > 1) {
        		// yesterday's portfolio value
	        	ArrayList<String> prevHolder = portfolioValHistory.get(portfolioValHistory.size()-2);
	        	Double prevVal = Double.parseDouble(prevHolder.get(1));
	        	Double percentChange = (val - prevVal) / 100;
	        	session.setAttribute("portfolioPercentage", f.format(percentChange));
	        	
        	}        	
        }
	
		
		buildPortfolioJSON();
	
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
		
	}


	void buildGraph() throws IOException {
		//chart to display different stocks
		String theChart =  "<script type=\"text/javascript\">\n" + 
				"				function getSP() {\n" + 
				"					return \""+ myStocks.get(0).get(5) + "\";\n" + 
				"				}\n" +
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
		
		//add the sp
		if(myStocks.get(0).get(5).equals("Yes")) {
			theChart += "{\n" +
							"type: \"line\",\n" + 
							"name: \"S&P\",\n" + 
							"showInLegend: true,\n" +
							"yValueFormatString: \"$##0.00\",\n" + 
							"dataPoints :" + myStocks.get(0).get(6) +
						"},\n";	
		}

		//add any stocks you want to view
		for(int i=0; i<view.size(); i++) {
			if(view.get(i).get(5).equals("Yes")) {
				theChart += "{\n" +
							"type: \"line\",\n" + 
							"name: \"" + view.get(i).get(0) + "\",\n" +
							"showInLegend: true,\n" +
							"yValueFormatString: \"$##0.00\",\n" + 
							"dataPoints :" + view.get(i).get(1) +
						"},\n";	
			}
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
	
	public void addStock(String username, String symbol, Calendar purchase, Calendar sell, double numOfShare) throws IOException {
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
	}
	
	// retrieve stock symbols
	public void getUserStock(String username) throws IOException, InterruptedException, ParseException {
		initializeFireBase();
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference ref = database.getReference().child("users").child(username).child("portfolio");
		
		//add the sp in first
		ArrayList<String> stock = new ArrayList<String>();
		stock.add("^GSPC");
		stock.add(YahooFinance.get("^GSPC").getName());
		stock.add("1");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String fromTime = format1.format(from.getTime());
		String toTime = format1.format(now.getTime());
		stock.add(fromTime);
		stock.add(toTime);
		//whether or not it should be shown on graph
		stock.add("Yes");
		String json = viewStock("^GSPC", "1", fromTime, toTime);
		
		//add to big array
		stock.add(json);
		myStocks.add(stock);
		
		
		
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