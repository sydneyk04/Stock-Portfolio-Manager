package csci310.servlets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import csci310.servlets.SignUpServlet.MyCallback;

public class StockPerformanceServletTest extends Mockito {
	static StockPerformanceServlet servlet;
	static HttpServletRequest request;
	static HttpServletResponse response;
	static PrintWriter printWriter;
	static RequestDispatcher dispatcher;
	static DatabaseReference mockedDatabaseReference;
	static FirebaseDatabase mockedFirebaseDatabase;
	
	@BeforeClass
    public static void setup() {
		servlet = new StockPerformanceServlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		dispatcher = mock(RequestDispatcher.class);
		printWriter = new PrintWriter(new StringWriter());
		mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
		mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
    }
	
	@Test
	public void testDoGet() throws IOException, ServletException {	
		when(request.getParameter("stockName")).thenReturn("Kendall's Stock");
		when(request.getParameter("stockCode")).thenReturn("KS");
		when(request.getParameter("stockPrice")).thenReturn("15.10");
		when(response.getWriter()).thenReturn(printWriter);
		servlet.doGet(request, response);
		assertTrue(servlet.check==true);
	}
	
	@Test
	public void testDoPost() throws IOException, ServletException, InterruptedException {	
		servlet.doPost(request, response);
		assertTrue(servlet.check==true);
	}
}
		