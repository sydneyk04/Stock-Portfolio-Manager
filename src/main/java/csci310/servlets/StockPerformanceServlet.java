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
	Stock stock;
	String timePeriod = "1M";
	Boolean check = false;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/plain");
		out = response.getWriter();

		//pass stock symbol through URL,
		String symbol = request.getQueryString();
		symbol = symbol.substring(symbol.lastIndexOf("=") + 1);
		
		//grab stock and set all the variables based on what stock we have
		stock = getStock(symbol);
		
		buildGraph(timePeriod);
		
		request.setAttribute("stockName", stock.getName());
		request.setAttribute("stockCode", stock.getSymbol());
		request.setAttribute("stockPrice", stock.getQuote().getPrice());
		
		response.sendRedirect(jsp);
		check = true;
	}
	
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		
		//when user selects new time period
//		timePeriod = request.getParameter(“timePeriod”);
		
		//rebuild the graph
		buildGraph(timePeriod);
		
		response.sendRedirect(jsp);
		check = true;
	}
	
	public Stock getStock(String symbol) throws IOException {
		return YahooFinance.get(symbol);
	}
	
	void buildGraph(String timePeriod) throws IOException {
		List<HistoricalQuote> history = stock.getHistory();

		//for making json based on time period
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
		
			map = new HashMap<Object,Object>(); map.put("label", label); map.put("y", close); list.add(map);
		}
		
		String stockHistory = new Gson().toJson(list);
		
		out.println("<script type=\"text/javascript\">\n" + 
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
				"					data: [{\n" + 
				"						type: \"line\",\n" + 
				"						yValueFormatString: \"#,$##0\",\n" + 
				"						dataPoints :" + stockHistory +
				"					}]\n" + 
				"				});\n" + 
				"				chart.render();\n" + 
				"			}\n" + 
				"		</script>"
				);
		
	}
}