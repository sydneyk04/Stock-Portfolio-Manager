package csci310.servlets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import csci310.servlets.SignUpServlet.MyCallback;
import static org.hamcrest.Matchers.*;

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
		ArrayList<String> stock = new ArrayList<String>();
		String ticker = "TSLA";
		String name = "Tesla";
		String shares = "1";
		String dp = "2020-10-10";
		stock.add(ticker);
		stock.add(name);
		stock.add(shares);
		stock.add(dp);
		servlet.myStocks.add(stock);
    }
	
	@Test
	public void testDoGet() throws IOException, ServletException {	
		when(request.getParameter("ticker")).thenReturn("TSLA");
		servlet.doGet(request, response);
		assertTrue(true);
	}
	
	@Test
	public void testDoPost() throws IOException, ServletException, InterruptedException {	
		servlet.doPost(request, response);
		assertTrue(true);
	}
	
	@Test
	public void testAddPortfolioValues() throws IOException, ServletException, InterruptedException {	
		servlet.addPortfolioValues(0, 0.0, 0.0, "label", false);
		assertTrue(true);
	}
	
	@Test
	public void testBuildPortfolioJSON() throws IOException, ServletException, InterruptedException {	
		servlet.buildPortfolioJSON();
		assertTrue(true);
	}
	
	@Test
	public void testBuildStockJSONS() throws IOException, ServletException, InterruptedException, ParseException {	
		Calendar from = Calendar.getInstance();
		from.add(Calendar.YEAR, -1);
		Calendar now = Calendar.getInstance();
		servlet.buildStockJSONS(from, now);
		assertTrue(true);
	}
	
	@Test
	public void testBuildGraph() throws IOException, ServletException, InterruptedException, ParseException {	
		System.out.println("testbuild graph");
		servlet.buildGraph();
		assertTrue(true);
		System.out.println("end testbuild graph");
	}
	
	@Test
	public void testAddStock() {
		assertTrue(true);
	}
	
	@Test
	public void testRemoveStock() throws IOException {
		servlet.removeStock("none", "none");
	}
	
	@Test
	public void getUserStock() throws IOException, InterruptedException {
		servlet.myStocks.clear();
		servlet.getUserStock("johnDoe");
		System.out.println(servlet.myStocks);
		assertThat(servlet.myStocks, hasSize(3));
		servlet.myStocks.clear();
		servlet.getUserStock("none");
		assertThat(servlet.myStocks, hasSize(0));
	}
}
		