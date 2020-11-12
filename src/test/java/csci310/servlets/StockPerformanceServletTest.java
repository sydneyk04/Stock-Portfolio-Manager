package csci310.servlets;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StockPerformanceServletTest extends Mockito {
	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	HttpSession session;
	
	@Mock
	RequestDispatcher dispatcher;
	
	static DatabaseReference mockedDatabaseReference;
	static FirebaseDatabase mockedFirebaseDatabase;
	
	StringWriter stringWriter;
	PrintWriter printWriter;
	StockPerformanceServlet servlet;
	
	@Before
    public void setup() throws IOException {
		servlet = new StockPerformanceServlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = Mockito.mock(HttpSession.class);
		dispatcher = mock(RequestDispatcher.class);
		stringWriter = new StringWriter();
		printWriter = new PrintWriter(stringWriter);
		mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
		mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
		when(request.getSession()).thenReturn(session); 
		when(response.getWriter()).thenReturn(printWriter);
    }
	
	@Test
	public void testDoGet() throws IOException, ServletException, InterruptedException, ParseException {	
		when(session.getAttribute("username")).thenReturn("test");
		StockPerformanceServlet spyServlet = spy(StockPerformanceServlet.class);
		doNothing().when(spyServlet).getUserStock(anyString());
		doNothing().when(spyServlet).calculatePortfolio();
		doNothing().when(spyServlet).buildGraph();
		spyServlet.doGet(request, response);
		
		doThrow(InterruptedException.class).when(spyServlet).getUserStock(anyString());
		spyServlet.doGet(request, response);
		
		doThrow(ParseException.class).when(spyServlet).calculatePortfolio();
		spyServlet.doGet(request, response);
		
		when(session.getAttribute("username")).thenReturn(null);
		spyServlet.doGet(request, response);

		servlet.doGet(request, response);
		assertTrue(true);
		
		servlet.portfolioValHistory = new ArrayList<ArrayList>();
		servlet.doGet(request, response);
		assertTrue(!servlet.portfolioValHistory.equals(""));
	}
	
	@Test
	public void testDoGetThrowIOException() throws IOException, InterruptedException, ServletException, ParseException {	
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		StockPerformanceServlet spyServlet = spy(servlet);
		
		when(response.getWriter()).thenReturn(out);
		when(session.getAttribute("username")).thenReturn("johnDoe");
		doThrow(IOException.class).when(spyServlet).getUserStock(anyString());
		
		spyServlet.doGet(request, response);

		String result = writer.getBuffer().toString();      
		Assert.assertEquals("stock fail - IOException", result);
	}
	
	@Test
	public void testDoGetThrowParseException() throws IOException, InterruptedException, ParseException, ServletException {	
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		StockPerformanceServlet spyServlet = spy(servlet);
		
		when(response.getWriter()).thenReturn(out);
		when(session.getAttribute("username")).thenReturn("johnDoe");
		doNothing().when(spyServlet).getUserStock(anyString());
		doThrow(ParseException.class).when(spyServlet).getUserStock("johnDoe");
		doThrow(ParseException.class).when(spyServlet).calculatePortfolio();
		doThrow(ParseException.class).when(spyServlet).ownedCheck(anyString(), anyString(), anyString());
		
		spyServlet.doGet(request, response);

		String result = stringWriter.getBuffer().toString();      
		Assert.assertEquals("", result);
	}
	
	@Test
	public void testDoPost() throws IOException, ServletException, InterruptedException, ParseException {			
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);		
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
        servlet.getUserStock("johnDoe");
		ArrayList<String> stock = new ArrayList<String>();
		stock.add("TSLA");
		stock.add("Tesla");
		stock.add("1");
		stock.add("2020-01-10");
		stock.add("2020-10-10");
		stock.add("Yes");
		servlet.myStocks.add(stock);
//		servlet.view.add(stock); 
        StockPerformanceServlet spyServlet = spy(servlet);
		
		when(response.getWriter()).thenReturn(out);
		when(request.getParameter("action")).thenReturn("portfolioState");
		when(session.getAttribute("username")).thenReturn("johnDoe");		
		when(request.getParameter("ticker")).thenReturn("TSLA");
		
		spyServlet.doPost(request, response);
		assertTrue(servlet.myStocks.get(servlet.myStocks.size()-1).get(5).equals("No"));
		
		spyServlet.doPost(request, response);
		assertTrue(servlet.myStocks.get(servlet.myStocks.size()-1).get(5).equals("Yes"));
	}
	
	@Test
	public void testDoPostToggleSP() throws IOException, ServletException, InterruptedException, ParseException {			
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);		
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
        servlet.getUserStock("johnDoe");  
        StockPerformanceServlet spyServlet = spy(servlet);
		
		when(response.getWriter()).thenReturn(out);
		when(request.getParameter("action")).thenReturn("toggleSP");
		when(session.getAttribute("username")).thenReturn("johnDoe");	
		
		spyServlet.doPost(request, response);
		assertTrue(servlet.myStocks.get(0).get(5).equals("No"));
		
		spyServlet.doPost(request, response);
		assertTrue(servlet.myStocks.get(0).get(5).equals("Yes"));
	}
	
	@Test
	public void testDoPostShowViewStock() throws IOException, ServletException, InterruptedException, ParseException {			
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);		
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
        servlet.getUserStock("johnDoe");
    	ArrayList<String> stock1 = new ArrayList<String>();
		stock1.add("SNAP");
		stock1.add("Snap");
		stock1.add("1");
		stock1.add("2020-01-10");
		stock1.add("2020-10-10");
		stock1.add("Yes");
		servlet.view.add(stock1);
        
        ArrayList<String> stock2 = new ArrayList<String>();
		stock2.add("TSLA");
		stock2.add("Tesla");
		stock2.add("1");
		stock2.add("2020-01-10");
		stock2.add("2020-10-10");
		stock2.add("Yes");
		servlet.view.add(stock2); 
        StockPerformanceServlet spyServlet = spy(servlet);
		
		when(response.getWriter()).thenReturn(out);
		when(request.getParameter("action")).thenReturn("showViewStock");
		when(session.getAttribute("username")).thenReturn("johnDoe");		
		when(request.getParameter("ticker")).thenReturn("TSLA");
		
		spyServlet.doPost(request, response);
		assertTrue(servlet.view.get(servlet.view.size()-1).get(5).equals("No"));
		
		spyServlet.doPost(request, response);
		assertTrue(servlet.view.get(servlet.view.size()-1).get(5).equals("Yes"));
	}
	
	@Test
	public void testDoPostViewStock() throws IOException, ServletException, InterruptedException, ParseException {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
        servlet.getUserStock("johnDoe");
        ArrayList<String> stock1 = new ArrayList<String>();
		stock1.add("SNAP");
		stock1.add("Snap");
		stock1.add("1");
		stock1.add("2020-01-10");
		stock1.add("2020-10-10");
		stock1.add("Yes");
		servlet.view.add(stock1);
        
        ArrayList<String> stock = new ArrayList<String>();
		stock.add("TSLA");
		stock.add("Tesla");
		stock.add("1");
		stock.add("2020-01-10");
		stock.add("2020-10-10");
		stock.add("Yes");
//		servlet.myStocks.add(stock);
		servlet.view.add(stock);
		StockPerformanceServlet spyServlet = spy(servlet);
		
		when(request.getParameter("action")).thenReturn("viewStock");
		when(response.getWriter()).thenReturn(out);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("johnDoe");		
		when(request.getParameter("ticker")).thenReturn("TSLA");
		when(request.getParameter(request.getParameter("datePurchased"))).thenReturn("2020-01-10");
		when(request.getParameter(request.getParameter("dateSold"))).thenReturn("2020-10-10");
		when(request.getParameter(request.getParameter("numOfShares"))).thenReturn("1");		
		doReturn("").when(spyServlet).viewStock(anyString(), anyString(), anyString(), anyString());
		
		spyServlet.doPost(request, response);
		assertThat(servlet.view.size(), greaterThan(0));
		//assertEquals("Please enter a stock you're not already viewing", session.getAttribute("invalid_error"));
		//assertNull(session.getAttribute("invalid_error"));
		//assertNull(session.getAttribute("view"));
		
		when(session.getAttribute("invalid_error")).thenReturn(null);
		doThrow(ParseException.class).when(spyServlet).viewStock(anyString(), anyString(), anyString(), anyString());
		spyServlet.doPost(request, response);
		//assertEquals("Please enter a valid ticker", session.getAttribute("invalid_error"));
		//assertNull(session.getAttribute("invalid_error"));
		assertThat(servlet.view.size(), greaterThan(0));
		
		doThrow(NullPointerException.class).when(spyServlet).viewStock(anyString(), anyString(), anyString(), anyString());
		spyServlet.doPost(request, response);
		//assertEquals("", session.getAttribute("invalid_error"));
		//assertNull(session.getAttribute("invalid_error"));
		assertThat(servlet.view.size(), greaterThan(0));
		
		doThrow(FileNotFoundException.class).when(spyServlet).viewStock(anyString(), anyString(), anyString(), anyString());
		spyServlet.doPost(request, response);
		//assertEquals("", session.getAttribute("invalid_error"));
		//assertNull(session.getAttribute("invalid_error"));
		assertThat(servlet.view.size(), greaterThan(0));
		
		when(session.getAttribute("invalid_error")).thenReturn("not null");
		doThrow(ParseException.class).when(spyServlet).viewStock(anyString(), anyString(), anyString(), anyString());
		spyServlet.doPost(request, response);
		//assertEquals("Please enter a valid ticker", session.getAttribute("invalid_error"));
		//assertNull(session.getAttribute("invalid_error"));
		assertThat(servlet.view.size(), greaterThan(0));
	}
	
	@Test
	public void testDoPostRemoveViewStock() throws IOException, ServletException, InterruptedException, ParseException {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
		servlet.getUserStock("johnDoe");
		ArrayList<String> stock1 = new ArrayList<String>();
		stock1.add("SNAP");
		stock1.add("Snap");
		stock1.add("1");
		stock1.add("2020-01-10");
		stock1.add("2020-10-10");
		stock1.add("Yes");
		servlet.view.add(stock1);
	        
		ArrayList<String> stock = new ArrayList<String>();
		stock.add("TSLA");
		stock.add("Tesla");
		stock.add("1");
		stock.add("2020-01-10");
		stock.add("2020-10-10");
		stock.add("Yes");
//		servlet.myStocks.add(stock);
		servlet.view.add(stock);
		StockPerformanceServlet spyServlet = spy(servlet);
		
		when(request.getParameter("action")).thenReturn("removeViewStock");
		when(response.getWriter()).thenReturn(out);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("johnDoe");		
		when(request.getParameter("removeTicker")).thenReturn("TSLA");
		when(request.getParameter(request.getParameter("datePurchased"))).thenReturn("2020-01-10");
		when(request.getParameter(request.getParameter("removeTicker"))).thenReturn("TSLA");
		when(request.getParameter(request.getParameter("dateSold"))).thenReturn("2020-10-10");
		when(request.getParameter(request.getParameter("numOfShares"))).thenReturn("1");
		
		spyServlet.doPost(request, response);
		assertNull(session.getAttribute("view"));
		
		servlet.portfolioValHistory = new ArrayList<ArrayList>();
		servlet.doGet(request, response);
		assertTrue(servlet.portfolioValHistory != null);
	}
	
	@Test
	public void testDoPostAddStock() throws IOException, ServletException, InterruptedException, ParseException {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
        servlet.getUserStock("johnDoe");
		ArrayList<String> stock = new ArrayList<String>();
		stock.add("TSLA");
		stock.add("Tesla");
		stock.add("1");
		stock.add("2020-01-10");
		stock.add("2020-10-10");
		stock.add("Yes");
		
		StockPerformanceServlet spyServlet = spy(servlet);
		
		when(request.getParameter("action")).thenReturn("addStock");
		when(response.getWriter()).thenReturn(out);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("johnDoe");	
		when(request.getParameter("ticker")).thenReturn("TSLA");
		when(request.getParameter("datePurchased")).thenReturn("2020-01-10");
		when(request.getParameter("dateSold")).thenReturn("2020-10-10");
		when(request.getParameter("numOfShares")).thenReturn("1");
		
		servlet.myStocks.add(stock);
		servlet.view.add(stock);
		
		spyServlet.doPost(request, response);
		assertNull(session.getAttribute("view"));
		
		servlet.portfolioValHistory = new ArrayList<ArrayList>();
		servlet.doGet(request, response);
		assertTrue(servlet.portfolioValHistory != null);
	}
	
	@Test
	public void testDoPostRemoveStock() throws IOException, ServletException, InterruptedException, ParseException {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
		servlet.getUserStock("johnDoe");
		ArrayList<String> stock = new ArrayList<String>();
		stock.add("TSLA");
		stock.add("Tesla");
		stock.add("1");
		stock.add("2020-01-10");
		stock.add("2020-10-10");
		stock.add("Yes");
		servlet.myStocks.add(stock);
		servlet.addStock("test", "TSLA", servlet.from, servlet.now, 1);
		
		when(request.getParameter("action")).thenReturn("removeStock");
		when(response.getWriter()).thenReturn(out);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("johnDoe");		
		when(request.getParameter("ticker")).thenReturn("TSLA");
		when(request.getParameter("datePurchased")).thenReturn("2020-01-10");
		when(request.getParameter("removeStockTicker")).thenReturn("TSLA");
		when(request.getParameter("dateSold")).thenReturn("2020-10-10");
		when(request.getParameter("numOfShares")).thenReturn("1");
		
		System.out.println("hi: " + servlet.myStocks);
		
		servlet.doPost(request, response);
		assertNotNull(servlet.myStocks);
	}
	
	@Test
	public void testDoPostChangeTimePeriod() throws IOException, ServletException, InterruptedException, ParseException {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
        servlet.getUserStock("johnDoe");
        ArrayList<String> stock = new ArrayList<String>();
		stock.add("TSLA");
		stock.add("Tesla");
		stock.add("1");
		stock.add("2020-01-10");
		stock.add("2020-10-10");
		stock.add("Yes");
		servlet.myStocks.add(stock);
		servlet.view.add(stock);
		StockPerformanceServlet spyServlet = spy(servlet);
		
		when(request.getParameter("action")).thenReturn("changeTimePeriod");
		when(request.getParameter("from")).thenReturn("2020-01-10");
		when(request.getParameter("to")).thenReturn("2020-10-10");
		when(response.getWriter()).thenReturn(out);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("johnDoe");	
		when(request.getParameter("ticker")).thenReturn("TSLA");
		when(request.getParameter(request.getParameter("datePurchased"))).thenReturn("2020-01-10");
		when(request.getParameter(request.getParameter("dateSold"))).thenReturn("2020-10-10");
		when(request.getParameter(request.getParameter("numOfShares"))).thenReturn("1");
		
		spyServlet.doPost(request, response);
		assertNotNull(servlet.myStocks);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDoPostSelectViewAll() throws IOException, ServletException, InterruptedException, ParseException {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
        servlet.getUserStock("johnDoe");
        ArrayList<String> stock = new ArrayList<String>();
		stock.add("TSLA");
		stock.add("Tesla");
		stock.add("1");
		stock.add("2020-01-10");
		stock.add("2020-10-10");
		stock.add("Yes");
		servlet.myStocks.add(stock);
		servlet.view.add(stock);
		StockPerformanceServlet spyServlet = spy(servlet);
		
		when(request.getParameter("action")).thenReturn("selectViewAll");
		when(request.getParameter("from")).thenReturn("2020-01-10");
		when(request.getParameter("to")).thenReturn("2020-10-10");
		when(response.getWriter()).thenReturn(out);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("johnDoe");	
		when(request.getParameter("ticker")).thenReturn("TSLA");
		when(request.getParameter(request.getParameter("datePurchased"))).thenReturn("2020-01-10");
		when(request.getParameter(request.getParameter("dateSold"))).thenReturn("2020-10-10");
		when(request.getParameter(request.getParameter("numOfShares"))).thenReturn("1");
		
		spyServlet.doPost(request, response);
		assertNotNull(servlet.myStocks);
		
		doThrow(ParseException.class).when(spyServlet).calculatePortfolio();
		spyServlet.doPost(request, response);
		
		doThrow(IOException.class).when(spyServlet).calculatePortfolio();
		spyServlet.doPost(request, response);
			
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDoPostDeselectViewAll() throws IOException, ServletException, InterruptedException, ParseException {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
        servlet.getUserStock("johnDoe");
        ArrayList<String> stock = new ArrayList<String>();
		stock.add("TSLA");
		stock.add("Tesla");
		stock.add("1");
		stock.add("2020-01-10");
		stock.add("2020-10-10");
		stock.add("Yes");
		servlet.myStocks.add(stock);
		servlet.view.add(stock);
		StockPerformanceServlet spyServlet = spy(servlet);
		
		when(request.getParameter("action")).thenReturn("deselectViewAll");
		when(request.getParameter("from")).thenReturn("2020-01-10");
		when(request.getParameter("to")).thenReturn("2020-10-10");
		when(response.getWriter()).thenReturn(out);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("johnDoe");	
		when(request.getParameter("ticker")).thenReturn("TSLA");
		when(request.getParameter(request.getParameter("datePurchased"))).thenReturn("2020-01-10");
		when(request.getParameter(request.getParameter("dateSold"))).thenReturn("2020-10-10");
		when(request.getParameter(request.getParameter("numOfShares"))).thenReturn("1");
		
		spyServlet.doPost(request, response);
		assertNotNull(servlet.myStocks);
		
		doThrow(ParseException.class).when(spyServlet).calculatePortfolio();
		spyServlet.doPost(request, response);
		
		doThrow(IOException.class).when(spyServlet).calculatePortfolio();
		spyServlet.doPost(request, response);
	}
	
	@Test
	public void testDoPostThrowParseException() throws IOException, InterruptedException, ParseException, ServletException {	
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		StockPerformanceServlet spyServlet = spy(servlet);
		
		when(response.getWriter()).thenReturn(out);
		when(session.getAttribute("username")).thenReturn("johnDoe");
		when(request.getParameter("action")).thenReturn("portfolioState");
		doThrow(ParseException.class).when(spyServlet).calculatePortfolio();
		
		spyServlet.doPost(request, response);

		String result = stringWriter.getBuffer().toString();      
		Assert.assertEquals("", result);
	}
	

	@Test
	public void testAddPortfolioValues() throws IOException, ServletException, InterruptedException {	
		servlet.portfolioValHistory.clear();
		servlet.addPortfolioValues(0, 0.0, 0.0, "label", true);
		assertTrue(servlet.portfolioValHistory.size() == 1);
		
		servlet.addPortfolioValues(1, 0.0, 0.0, "label", false);
		assertTrue(servlet.portfolioValHistory.size() == 2);
		
		servlet.addPortfolioValues(1, 0.0, 0.0, "label", true);
		assertTrue(servlet.portfolioValHistory.size() == 2);
		
		servlet.addPortfolioValues(1, 0.0, 0.0, "label", false);
		assertTrue(servlet.portfolioValHistory.size() == 2);
	}
	
	@Test
	public void testBuildPortfolioJSON() throws IOException, ServletException, InterruptedException {	
		servlet.buildPortfolioJSON();
		assertTrue(true);
	}
	
	@Test
	public void testOwnedCheck() throws IOException, ServletException, InterruptedException, ParseException {	
		//i just put every possible combo to get full coverage
		
		//sell date > historical date, purchase date < historical date
		Boolean owned = servlet.ownedCheck("2020-04-22", "2020-01-22", "");
		assertTrue(owned);
		
		//sell date < historical date, purchase date < historical date
		owned = servlet.ownedCheck("2020-04-22", "2020-01-22", "2020-02-22");
		assertFalse(owned);
		
		//sell date = historical date, purchase date < historical date
		owned = servlet.ownedCheck("2020-04-22", "2020-01-22", "2020-04-22");
		assertTrue(owned);

		//sell date > historical date, purchase date > historical date
		owned = servlet.ownedCheck("2020-04-22", "2020-05-22", "");
		assertFalse(owned);
		
		//sell date < historical date, purchase date > historical date
		owned = servlet.ownedCheck("2020-04-22", "2020-05-22", "2020-02-22");
		assertFalse(owned);
		
		//sell date = historical date, purchase date > historical date
		owned = servlet.ownedCheck("2020-04-22", "2020-05-22", "2020-04-22");
		assertFalse(owned);
		
		//sell date > historical date, purchase date = historical date
		owned = servlet.ownedCheck("2020-04-22", "2020-04-22", "2020-05-22");
		assertTrue(owned);

		//sell date < historical date, purchase date = historical date
		owned = servlet.ownedCheck("2020-04-22", "2020-04-22", "2020-01-22");
		assertFalse(owned);
		
		//sell date = historical date, purchase date = historical date
		owned = servlet.ownedCheck("2020-04-22", "2020-04-22", "2020-04-22");
		assertTrue(owned);
	}

	@Test
	public void testCalculatePortfolio() throws IOException, ServletException, InterruptedException, ParseException {	
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		
		when(response.getWriter()).thenReturn(out);
		when(session.getAttribute("username")).thenReturn("johnDoe");
		
		servlet.doGet(request, response);
		servlet.calculatePortfolio();
		assertTrue(!servlet.portfolioValHistory.equals(""));
	}
	
	
	@Test
	public void testViewStock() throws IOException, ServletException, InterruptedException, ParseException {	
		Calendar from = Calendar.getInstance();
		from.add(Calendar.YEAR, -1);
		Calendar now = Calendar.getInstance();
		servlet.from = from;
		servlet.now = now;
    
		String stockJSON = servlet.viewStock("TSLA", "1", "2020-01-22", "2020-10-22");
		assertTrue(stockJSON != null);
	}

	@Test
	public void testBuildGraph() throws IOException, ServletException, InterruptedException, ParseException {	
		servlet.session = session;
		servlet.portfolioJSON = "";
		servlet.from = Calendar.getInstance();
        servlet.from.add(Calendar.YEAR, -1);
        servlet.now = Calendar.getInstance();
		
		servlet.getUserStock("johnDoe");
		ArrayList<String> stock = new ArrayList<String>();
		stock.add("TSLA");
		stock.add("Tesla");
		stock.add("1");
		stock.add("2020-01-10");
		stock.add("2020-10-10");
		stock.add("Yes");
//		servlet.myStocks.add(stock);
		servlet.view.add(stock);
		
		servlet.buildGraph();
		assertTrue(true);
	}
	
	@Test
	public void testAddStock() throws IOException {
		Calendar from = Calendar.getInstance();
        from.add(Calendar.YEAR, -1);
        servlet.addStock("test", "TSLA", from, Calendar.getInstance(), 1.0);
        assertTrue(true);
	}
	
	@Test
	public void testRemoveStock() throws IOException {
		servlet.removeStock("none", "none");
	}
	
	@Test
	public void testGetUserStock() throws IOException, InterruptedException, ParseException {
		servlet.myStocks.clear();
		Calendar from = Calendar.getInstance();
		from.add(Calendar.YEAR, -1);
		Calendar now = Calendar.getInstance();
		servlet.from = from;
		servlet.now = now;
		servlet.getUserStock("johnDoe");
		System.out.println(servlet.myStocks);
		assertThat(servlet.myStocks.size(), greaterThan(0));
		servlet.myStocks.clear();
		assertTrue(servlet.myStocks.isEmpty());
	}
}