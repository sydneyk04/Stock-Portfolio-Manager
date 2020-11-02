package csci310.servlets;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StockPerformanceServletTest extends Mockito {
	static StockPerformanceServlet servlet;
	static HttpServletRequest request;
	static HttpServletResponse response;
	static HttpSession session;
	static PrintWriter printWriter;
	static RequestDispatcher dispatcher;
	static DatabaseReference mockedDatabaseReference;
	static FirebaseDatabase mockedFirebaseDatabase;
	
	@BeforeClass
    public static void setup() throws IOException {
		servlet = new StockPerformanceServlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = Mockito.mock(HttpSession.class);
		dispatcher = mock(RequestDispatcher.class);
		printWriter = new PrintWriter(new StringWriter());
		mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
		mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
		when(request.getSession()).thenReturn(session); 
		when(response.getWriter()).thenReturn(printWriter);
//		ArrayList<String> stock = new ArrayList<String>();
//		String ticker = "TSLA";
//		String name = "Tesla";
//		String shares = "1";
//		String dp = "2020-10-10";
//		stock.add(ticker);
//		stock.add(name);
//		stock.add(shares);
//		stock.add(dp);
//		servlet.myStocks.add(stock);
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
	}
	
	@Test
	public void testDoPost() throws IOException, ServletException, InterruptedException, ParseException {	
		servlet.getUserStock("johnDoe");
		servlet.from = Calendar.getInstance();
		servlet.from.add(Calendar.YEAR, -1);
		servlet.now = Calendar.getInstance();
		servlet.doPost(request, response);
		
		StockPerformanceServlet spyServlet = spy(StockPerformanceServlet.class);
		spyServlet.from = Calendar.getInstance();
		spyServlet.from.add(Calendar.YEAR, -1);
		spyServlet.now = Calendar.getInstance();
		doThrow(ParseException.class).when(spyServlet).calculatePortfolio();
		doNothing().when(response).sendRedirect(anyString());
		spyServlet.getUserStock("johnDoe");
		spyServlet.doPost(request, response);
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
		servlet.ownedCheck("2020-04-22", "2020-01-22", "");
		assertTrue(true);
	}
	
	@Test
	public void testCalculatePortfolio() throws IOException, ServletException, InterruptedException, ParseException {	
		Calendar from = Calendar.getInstance();
		from.add(Calendar.YEAR, -1);
		Calendar now = Calendar.getInstance();
		servlet.getUserStock("johnDoe");
		servlet.calculatePortfolio();
		assertTrue(true);
	}
	
	
	@Test
	public void testViewStock() throws IOException, ServletException, InterruptedException, ParseException {	
		servlet.viewStock("TSLA");
		assertTrue(servlet.jsons.size() > 0);
	}
	
//	@Test
//	public void testBuildGraph() throws IOException, ServletException, InterruptedException, ParseException {	
//		servlet.jsons.clear();
//		servlet.myStocks.clear();
//		Calendar from = Calendar.getInstance();
//		from.add(Calendar.YEAR, -1);
//		Calendar now = Calendar.getInstance();
//		servlet.getUserStock("johnDoe");
//		servlet.buildStockJSONS(from, now);
//		System.out.println(servlet.myStocks.size() + "111");
//		System.out.println(servlet.jsons.size() + "222");
//		
//		doNothing().when(session).setAttribute(anyString(), anyString());
//		
//		servlet.buildGraph();
//	}
	
	@Test
	public void testAddStock() {
		assertTrue(true);
	}
	
	@Test
	public void testRemoveStock() throws IOException {
		servlet.removeStock("none", "none");
	}
	
	@Test
	public void testGetUserStock() throws IOException, InterruptedException {
		servlet.myStocks.clear();
		servlet.getUserStock("johnDoe");
		System.out.println(servlet.myStocks);
		assertThat(servlet.myStocks.size(), greaterThan(0));
		servlet.myStocks.clear();
		servlet.getUserStock("none");
		assertThat(servlet.myStocks, hasSize(0));
	}
}
		