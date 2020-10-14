package csci310.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import java.text.DateFormatSymbols;
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


@WebServlet("/stockperformance")
public class StockPerformanceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String jsp= "stockPerformance.jsp";
	static PrintWriter out;
	private HttpSession session = null;
	String timePeriod = "1M";
	Boolean check = false;
	List<String> tickers;
	List<String> jsons;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		session = request.getSession();
		response.setContentType("text/plain");
		out = response.getWriter();
		
		System.out.println("Hello from doGet");
		//START JACKSON CODE//
		
			//Jackon, the code that should go here is a function that gets this info from the database and stores it locally:
			//we can chat about best way to do this since we dont want to have to re call this function everytime the user 
		    //adds or removes a stock- or maybe we do lol??
			
			//The return types i need:
			//A list of the stocks in user portfolio of type List<String> with the ticker symbols, i made it a variable at the top
			//A String that has the portfolio json in it - we are going to need to leverage nandas code for this
		
			//method stub you can use that i committed test file for TDD
			buildPortfolioJSON();

		
		//END JACKSON CODE//
		
		//START KENDALL CODE//
			//My code is going to take in the data from above and format it to be displayed on the front end
			
			//for testing im making some tickers
			tickers = new ArrayList<String>();
			tickers.add("TSLA");
			tickers.add("GOOGL");
			
			//this is to format all the string jsons from the list of tickers
			jsons = new ArrayList<String>();
			buildStockJSONS();
				
			//build the graph using the list of stocks
			buildGraph();
		
			response.sendRedirect(jsp);
		//END KENDALL CODE//

			
	}
	
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		session = request.getSession();
		
		//also im not sure if our initial code should go in dopost or doget so we can figure that out once we are 
		//connecting everything to the dashboard
		//for now i am typing '/stockperformance' in the url and having it display on stockPerformance.jsp
		
		
		System.out.println("Hello from doPost");
		//this code runs when time period is changed
		timePeriod = request.getParameter("timePeriod");
		
		
		buildStockJSONS();
		buildGraph();
		
	}
	
	void getCalendarDate() {
		//nanda built this somewhere we just need to add it in and convert to our graph
	}
	
	void buildPortfolioJSON() {
		//nanda built this somewhere we just need to add it in and convert to our graph
	}
	
	void buildStockJSONS() throws IOException {
		//Below is the code to build/format json for graph
		
		//for loop to run through list of users stocks
		for(int s=0; s<tickers.size(); s++) {
			Stock stock = getStock(tickers.get(s));
			
			//this is where we need to deal with time period
			List<HistoricalQuote> history = stock.getHistory();
			
			//this is for formatting it for the graph that i am using
			Map<Object,Object> map = null;
			List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
		
			for(int i=0; i<history.size(); i++) {
				Calendar date = history.get(i).getDate();
				int year = date.get(Calendar.YEAR);
				int month = date.get(Calendar.MONTH);
				if(month == 0) {
					month = 12;
				}
				DateFormatSymbols symbols = new DateFormatSymbols();
				String label = symbols.getShortMonths()[month] + " " + year;
				BigDecimal close = history.get(i).getClose();
			
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
				"			window.onload = function() { \n" + 
				"				var chart = new CanvasJS.Chart(\"chartContainer\", {\n" + 
				"					theme: \"light2\",\n" + 
				"					title: {\n" + 
				"						text: \"\"\n" + 
				"					},\n" + 
				"					axisX: {\n" + 
				"						title: \"Time\"\n" + 
				"					},\n" + 
				"					axisY: {\n" + 
				"						title: \"Closing Price\",\n" + 
				"						includeZero: true\n" + 
				"					},\n" + 
				"					data: [\n";
		
		//add all of the stock jsons to the data for the graph
		for(int i=0; i<jsons.size(); i++) {
			
			theChart += "{\n" +
							"type: \"line\",\n" + 
							"yValueFormatString: \"#,$##0\",\n" + 
							"dataPoints :" + jsons.get(i) +
						"},\n";	
		}
		
		//add the end code
		theChart +=
				"					]\n" + 
				"				});\n" + 
				"				chart.render();\n" + 
				"			}\n" + 
				"		</script>";
		
		session.setAttribute("chart", theChart);
		
	}
	
	public Stock getStock(String symbol) throws IOException {
		return YahooFinance.get(symbol);
	}
}