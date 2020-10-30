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
	private HttpSession session = null;
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
		
		String username = session.getAttribute("username").toString();
		
		if(username != null) {
			//default time period is 1Y
			from = Calendar.getInstance();
			from.add(Calendar.YEAR, -1);
			now = Calendar.getInstance();
				
			try {
				getUserStock(username);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//set stocks as session variable for front end
			session.setAttribute("myStocks", myStocks);
			
			try {
				buildStockJSONS(from, now);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		//if user wants to toggle hide/show on graph
		if(action != null && action.equals("showOnGraph")) {
			String ticker = request.getParameter("ticker");
			for(int i=0; i<myStocks.size(); i++) {
				if(myStocks.get(i).get(0).equals(ticker)){
					if(myStocks.get(i).get(5).equals("Hidden")) {
						myStocks.get(i).set(5, "Visible");
					}else {
						myStocks.get(i).set(5, "Hidden");
					}
				}
			}
			buildGraph();
		} else if(action != null && action.equals("viewStock")) {
			//add a random stock to graph but don't add it to your portfolio
			
			
		} else if(action != null && action.equals("addStock")) {
			//add a stock to your portfolio
			
			
		} else if(action != null && action.equals("changeTimePeriod")){
			System.out.println("Change time period");
			//change calendar time period
			//there is no frontend for this yet....
			
			//these are of type "Calendar"
			//from = request.getParameter("from");
			//now = request.getParameter("now");
			
			//hardcode for testing, passes!
			from = Calendar.getInstance();
		    from.add(Calendar.MONTH, -2);
			now = Calendar.getInstance();
			
			jsons = new ArrayList<String>();
			portfolioValHistory = new ArrayList<ArrayList>();
			portfolioJSON = "";
			
			//pass the new dates into build stock jsons
			try {
				buildStockJSONS(from, now);
			} catch (ParseException e) {
				
			}
			
			buildGraph();
			
		} else {
			
			
		}
		
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
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date historicalDate = (Date)formatter.parse(holder);
				Date datePurchased = (Date)formatter.parse((String)myStocks.get(s).get(3));
				Date sellDate;
				if(myStocks.get(s).get(4).equals("")) {
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_YEAR, 1);
					sellDate = calendar.getTime();
					
				} else {
					sellDate = (Date)formatter.parse((String)myStocks.get(s).get(4));
				}
				boolean owned = false;
				if(sellDate.after(historicalDate) && datePurchased.before(historicalDate)){
					owned = true;
				}
				//create portfolio value at that index
				addPortfolioValues(i, close, shares, label, owned);
			
				map = new HashMap<Object,Object>(); map.put("label", label); map.put("y", close); 
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
		//i know this looks wacky but it will actually work hahah
		String theChart =  "<script type=\"text/javascript\">\n" + 
//				"			window.onload = function() { \n" + 
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
		
		
		for(int i=0; i<myStocks.size(); i++) {
			if(myStocks.get(i).get(5).equals("Visible")) {
				theChart += "{\n" +
								"type: \"line\",\n" + 
								"name: \"" + myStocks.get(i).get(0) + "\",\n" +
								"showInLegend: true,\n" +
								"yValueFormatString: \"$##0.00\",\n" + 
								"dataPoints :" + jsons.get(i) +
							"},\n";	
			}
		}
		
		//add the portfolio
		theChart += "{\n" +
						"type: \"line\",\n" + 
						"name: \"Portfolio\",\n" + 
						"showInLegend: true,\n" +
						"yValueFormatString: \"$##0.00\",\n" + 
						"dataPoints :" + portfolioJSON +
					"},\n";	
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
	
	public void addStock(String username, String symbol, Calendar from, Calendar to, double numOfShare) throws IOException {
		initializeFireBase();
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("portfolio");
		Map<String, Object> updates = new HashMap<>();
		Map<String, String> content = new HashMap<>();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String fromTime = format1.format(from.getTime());
		String toTime = format1.format(to.getTime());
		content.put("name", YahooFinance.get(symbol).getName());
		content.put("from", fromTime);
		content.put("to", toTime);
		content.put("shares", Double.toString(numOfShare));
		updates.put(symbol, content);
		
		ref.updateChildrenAsync(updates);
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
						stock.add("Hidden");
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