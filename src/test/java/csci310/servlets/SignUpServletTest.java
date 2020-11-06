package csci310.servlets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import csci310.servlets.SignUpServlet.MyCallback;

public class SignUpServletTest extends Mockito {
	static SignUpServlet servlet;
	static HttpServletRequest request;
	static HttpServletResponse response;
	static PrintWriter printWriter;
	static RequestDispatcher dispatcher;
	static DatabaseReference mockedDatabaseReference;
	static FirebaseDatabase mockedFirebaseDatabase;
	
	@BeforeClass
    public static void setup() {
		servlet = new SignUpServlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		dispatcher = mock(RequestDispatcher.class);
		printWriter = new PrintWriter(new StringWriter());
		mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
		mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
    }
	
	@Test
	public void testHashPassword() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String pw = "randompassword";
		String hashedPw = servlet.hashPassword(pw);

		// hashing method
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] hash = messageDigest.digest(pw.getBytes("UTF-8"));
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}

		assertTrue(hashedPw.contentEquals(hexString));
	}
	
	@Test
	public void testDoGet() throws IOException, ServletException {	
		servlet.doGet(request, response);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testDoPost() throws IOException, ServletException, InterruptedException, NoSuchAlgorithmException {		
		//check not null
		when(request.getRequestDispatcher("signup.jsp")).thenReturn(dispatcher);
		when(request.getParameter("username")).thenReturn("");
		when(request.getParameter("password")).thenReturn("");
		when(request.getParameter("password2")).thenReturn("");
		when(response.getWriter()).thenReturn(printWriter);
		servlet.doPost(request, response);	
		assertTrue(servlet.createdUser == false);
		
		//test user that already exists
		when(request.getRequestDispatcher("signup.jsp")).thenReturn(dispatcher);
		when(request.getParameter("username")).thenReturn("johnDoe");
		when(request.getParameter("password")).thenReturn("test123");
		when(request.getParameter("password2")).thenReturn("test123");
		when(response.getWriter()).thenReturn(printWriter);
		servlet.doPost(request, response);	
		assertTrue(servlet.createdUser == false);
		
		//test new user
		//make new username
		String user = getSaltString();
		when(request.getRequestDispatcher("signup.jsp")).thenReturn(dispatcher);
		when(request.getParameter("username")).thenReturn(user);
		when(request.getParameter("password")).thenReturn("hi");
		when(request.getParameter("password2")).thenReturn("hi");
		when(response.getWriter()).thenReturn(printWriter);
		servlet.doPost(request, response);	
		assertTrue(servlet.createdUser == true);
		
		//test no algorithm exception
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		SignUpServlet spyServlet = spy(servlet);
		
		doThrow(NoSuchAlgorithmException.class).when(spyServlet).hashPassword(anyString());
		when(response.getWriter()).thenReturn(out);
		when(request.getParameter("username")).thenReturn("johnDoe");
		when(request.getParameter("password")).thenReturn("test123");
		when(request.getParameter("password2")).thenReturn("test123");
		spyServlet.doPost(request, response);
		String result = writer.getBuffer().toString();
		Assert.assertEquals("signup fail", result);
		
		//test thread
		Thread.currentThread().interrupt();
		servlet.doPost(request, response);
		//since if we catch it, it will reset the flag
		assertFalse(Thread.currentThread().interrupted());
	}
	
	
	@Test
	public void testInitializeFirebase() throws IOException {	
		//delete firebase and test creation
		for (FirebaseApp app : FirebaseApp.getApps()) {
			app.delete();
		}
		assertNotNull(servlet.initializeFireBase());
		
		//test creation if already exists
		FirebaseApp fb = servlet.initializeFireBase();
		assertTrue(fb == null);
	}
	
	@Test
	public void testcheckUserInputs() throws ServletException, IOException {
		//missing field login 1
		String output1 = servlet.checkUserInputs("", "hi", "hi");
		assertTrue(output1.equals("Please fill out empty fields."));
		
		//missing field login 2
		String output2 = servlet.checkUserInputs("hi", "hi", "");
		assertTrue(output2.equals("Please fill out empty fields."));
		
		//missing field login 3
		String output3 = servlet.checkUserInputs("hi", "", "hi");
		assertTrue(output3.equals("Please fill out empty fields."));
	
		//passwords do not match
		String output4 = servlet.checkUserInputs("johnDoe", "test123", "test922929");
		assertTrue(output4.equals("Please ensure that your passwords match."));
		
		//correct login
		String output5 = servlet.checkUserInputs("johnDoe", "test123", "test123");
		assertTrue(output5 == null);
	}
	
	
	//https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
	protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
	

	
}
