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
	Stock stock;
	String timePeriod = "1M";
	Boolean check = false;
	List<String> jsons;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		session = request.getSession();
		response.setContentType("text/plain");
		out = response.getWriter();

//		//pass stock symbol through URL,
//		String symbol = request.getQueryString();
//		symbol = symbol.substring(symbol.lastIndexOf("=") + 1);
//		
//		//grab stock and set all the variables based on what stock we have
//		stock = getStock(symbol);
//		
//		buildGraph(timePeriod);
//		
//		request.setAttribute("stockName", stock.getName());
//		request.setAttribute("stockCode", stock.getSymbol());
//		request.setAttribute("stockPrice", stock.getQuote().getPrice());
//		
//		response.sendRedirect(jsp);
//		check = true;
	}
	
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		session = request.getSession();
		
		//when user selects new time period
		//timePeriod = request.getParameter(“timePeriod”);
		String symbol = request.getParameter("stockName");
		
		//grab stock and set all the variables based on what stock we have
		System.out.println("stock symbol is: " + symbol);
		stock = getStock(symbol);
		System.out.println(stock);
		
		//this is to get all the formatted jsons that will need to be displayed
		buildPortfolioJSONS();
		
		buildStockJSONS();
				
		
		//build the graph using the list of stocks
		buildGraph();
		
		session.setAttribute("stockName", stock.getName());
		session.setAttribute("stockCode", stock.getSymbol());
		session.setAttribute("stockPrice", stock.getQuote().getPrice());
		
		response.sendRedirect(jsp);

	}
	
	public Stock getStock(String symbol) throws IOException {
		return YahooFinance.get(symbol);
	}
	
	void buildPortfolioJSONS() {
		//nanda built this somewhere we just need to add it in and convert to our graph
	}
	
	void buildStockJSONS() {
		
	}
	
	void buildGraph() throws IOException {
		
		
	}
}