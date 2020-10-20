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
	List<String> portfolioJSON = new ArrayList<String>();
	
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//set stocks as session variable for front end
		session.setAttribute("myStocks", myStocks);
			
		buildStockJSONS(from, now);
				
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
		buildStockJSONS(from, now);
		buildGraph();
		
		response.sendRedirect("production/index.jsp");
		
	}
	
	void addPortfolioValues() throws IOException {
		
	}
	
	
	void buildPortfolioJSON() throws IOException {
		
		Double portfolioVal;
		
		//for loop to run through list of users stocks and create value of portfolio
//		for(int s=0; s<myStocks.size(); s++) {
//			Stock stock = YahooFinance.get((String)myStocks.get(s).get(0));
//			Double price = stock.getQuote().getPrice().doubleValue();
//			Double shares = myStocks.get(s).get(2);
//			BigDecimal value = new BigDecimal(price * shares).setScale(2, RoundingMode.HALF_EVEN);
//			portfolioVal += value.doubleValue();;
//		}
		
	}
	
	void buildStockJSONS(Calendar from, Calendar now) throws IOException {
		//Below is the code to build/format json for graph
		
		//for loop to run through list of users stocks
		for(int s=0; s<myStocks.size(); s++) {
			
			//this is where we need to deal with time period (Done!)
			String ticker = (String) myStocks.get(s).get(0);
			System.out.println("ticker in build json " + ticker);
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
				BigDecimal close = history.get(i).getClose();
			
				//
				buildPortfolioJSON();
				
				
				map = new HashMap<Object,Object>(); map.put("label", label); map.put("y", close); 
				list.add(map);
			}
			String stockHistory = new Gson().toJson(list);
			System.out.println(stockHistory);
			
			//add to big list
			jsons.add(stockHistory);
		}
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
		
		//add all of the stock jsons to the data for the graph
		for(int i=0; i<jsons.size(); i++) {
			System.out.println(myStocks.get(i).get(0));
		}
		
		for(int i=0; i<jsons.size(); i++) {
			
			theChart += "{\n" +
							"type: \"line\",\n" + 
							"name: \"" + myStocks.get(i).get(0) + "\",\n" +
							"showInLegend: true,\n" +
							"yValueFormatString: \"$##0.00\",\n" + 
							"dataPoints :" + jsons.get(i) +
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
	
	public Stock getStock(String symbol) throws IOException {
		return YahooFinance.get(symbol);
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
						for(DataSnapshot children : child.getChildren()) {
							String name = children.child("name").getValue().toString();
							String shares = children.child("shares").getValue().toString();
							String from = children.child("from").getValue().toString();
							stock.add(name);
							stock.add(shares);
							stock.add(from);
						}
						//add to big array
						System.out.println("adding stock info: " + stock);
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