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
	Calendar from ;
	Calendar now;
	Boolean check = false;
	List<ArrayList> myStocks = new ArrayList<ArrayList>();
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
		buildPortfolioJSON();
		
		//default time period is 1Y
		from = Calendar.getInstance();
		from.add(Calendar.YEAR, -1);
		now = Calendar.getInstance();
			
		try {
			getUserStock(username);
		} 
		 catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//set stocks as session variable for front end
		session.setAttribute("myStocks", myStocks);
		
		
		try {
			buildStockJSONS(from, now);
		} catch (ParseException e) {
			
		}
				
		//build the graph using the list of stocks
		buildGraph();
		
		
	}
	
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		session = request.getSession();
		System.out.println("Hello from doPost");
		
		//code for when user changes time period
		//just need to grab the two dates from the frontend in calendar format
		//Calendar from = request.getParameter("from");
		//Calendar now = request.getParameter("now");
		
		//pass the new dates into build stock jsons
		try {
			buildStockJSONS(from, now);
		} catch (ParseException e) {
			
		}
		
		buildGraph();
		response.sendRedirect("production/index.jsp");
		
	}
	
	void addPortfolioValues(Integer i, Double close, Double shares, String label, Boolean after) throws IOException {
		Double portfolioValue = close * shares;
		ArrayList<String> val = new ArrayList<String>();
	
		//if there is already a value at that index, add to it
		try {
			portfolioValHistory.get(i);
			ArrayList<String> holder = portfolioValHistory.get(i);
			//only add if purchased before this date
			if(after == true) {
				portfolioValue += Double.parseDouble(holder.get(1));
				val.add(label);
				val.add(String.valueOf(portfolioValue));
				portfolioValHistory.set(i, val);
			}
		} catch ( IndexOutOfBoundsException e ) {
			//otherwise create new value
			//if purchased before add valye
			if(after == true) {
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
//				System.out.println(day);
				DateFormatSymbols symbols = new DateFormatSymbols();
				String label = day + " " + symbols.getShortMonths()[month] + " " + year;
				Double close = history.get(i).getClose().doubleValue();
				Double shares = Double.parseDouble((String) myStocks.get(s).get(2));
				
				String holder = year + "-" + (month + 1) + "-" + day;
				DateFormat formatter = new SimpleDateFormat("YYYY-MM-DD"); 
				Date datePurchased = (Date)formatter.parse((String)myStocks.get(s).get(3));
				Date sellDate = (Date)formatter.parse((String)myStocks.get(s).get(4));
				Date historicalDate = (Date)formatter.parse(holder);
				
				boolean owned = false;
				owned = sellDate.after(historicalDate);
				owned = datePurchased.before(historicalDate);
				
				//if user owned stock during this point in time add to portfolio value
//				Boolean owned = false;
//				String datePurchased = (String)myStocks.get(s).get(3);
//				String[] dpParts = datePurchased.split("\\-");
//				String dateSold = (String)myStocks.get(s).get(4);
//				String[] sellParts = datePurchased.split("\\-");
//				Integer dpYear = Integer.parseInt(dpParts[0]); 
//				Integer sellYear = Integer.parseInt(sellParts[0]); 
//				
//				if(dpYear < year) {
//					owned = true;
//				} else if(dpYear == year){
//					Integer dpMonth = Integer.parseInt(dpParts[1]);
//					if(dpMonth < month+1) {
//						owned = true;
//					} else if(dpMonth == month+1){
//						Integer dpDay = Integer.parseInt(dpParts[2]);
//						if(dpDay <= day) {
//							owned = true;
//						}
//					}
//				}

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
		System.out.println("hi1");
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
		
		
		for(int i=0; i<jsons.size(); i++) {
			System.out.println("hiiii");
			theChart += "{\n" +
							"type: \"line\",\n" + 
							"name: \"" + myStocks.get(i).get(0) + "\",\n" +
							"showInLegend: true,\n" +
							"yValueFormatString: \"$##0.00\",\n" + 
							"dataPoints :" + jsons.get(i) +
						"},\n";	
		}
		System.out.println("hi2");
		
		//add the portfolio
		theChart += "{\n" +
						"type: \"line\",\n" + 
						"name: \"Portfolio\",\n" + 
						"showInLegend: true,\n" +
						"yValueFormatString: \"$##0.00\",\n" + 
						"dataPoints :" + portfolioJSON +
					"},\n";	
		System.out.println("hi3");
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