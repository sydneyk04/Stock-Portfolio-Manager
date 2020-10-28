package csci310.servlets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.firebase.FirebaseApp;

public class LoginServletTest extends Mockito {
	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	HttpSession session;

	@Mock
	RequestDispatcher rd;

	LoginServlet servlet;

	@Before
	public void setUp() throws Exception {
    	request = Mockito.mock(HttpServletRequest.class);
    	response = Mockito.mock(HttpServletResponse.class);
    	session = Mockito.mock(HttpSession.class);
    	rd = Mockito.mock(RequestDispatcher.class);
    	servlet = new LoginServlet();
     
    	when(request.getSession()).thenReturn(session);        
	}
	
	@Test
	public void testHashPassword() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String pw = "randompassword";
		String hashedPw = LoginServlet.hashPassword(pw);

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

		Assert.assertTrue(hashedPw.contentEquals(hexString));
	}
    
    @Test
    public void testInitializeFirebaseNotNull() throws IOException {
    	for (FirebaseApp app : FirebaseApp.getApps()) {
			app.delete();
		}
		assertNotNull(servlet.initializeFirebase("stock16-service-account.json"));
   	 }
 
	@Test
	public void testInitializeFirebaseNull() throws IOException { 
		assertNull(servlet.initializeFirebase("stock16-service-account.json"));
	}
	 
    @Test
	public void testInitializeFirebaseThrowIOException() throws IOException { 
		for (FirebaseApp app : FirebaseApp.getApps()) {
			app.delete();
		}
		servlet.initializeFirebase("stock16-service-account.json");
	}
	 
	@Test
	public void testDoPostSuccess() throws Exception {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
  
		when(response.getWriter()).thenReturn(out);
		when(request.getParameter("username")).thenReturn("johnDoe");
		when(request.getParameter("password")).thenReturn("test123");   
		

		servlet.doPost(request, response);  
		String result = writer.getBuffer().toString();
		
        
		Assert.assertEquals("login success", result);
		
		doThrow(IOException.class)
			.when(response)
			.sendRedirect(anyString());
     
		servlet.doPost(request, response);  
	}
    
	@Test
	public void testDoPostFail() throws Exception {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
        
		when(response.getWriter()).thenReturn(out);
		when(request.getParameter("username")).thenReturn("johnDoe");
		when(request.getParameter("password")).thenReturn("pw");   

		servlet.doPost(request, response);
		String result = writer.getBuffer().toString();
        
		Assert.assertEquals("login fail", result);
     
		when(request.getParameter("username")).thenReturn("fake");
		servlet.doPost(request, response);
		result = writer.getBuffer().toString();
     
		Assert.assertEquals("login fail", result);
		
		LoginServlet spyServlet = spy(servlet);
		doNothing().when(spyServlet).authenticate(anyString(), anyString());
		when(request.getParameter("username")).thenReturn(".");
		spyServlet.doPost(request, response);
		result = writer.getBuffer().toString();
		Assert.assertEquals("login fail", result);
		
		when(request.getParameter("username")).thenReturn("$");
		spyServlet.doPost(request, response);
		result = writer.getBuffer().toString();
		Assert.assertEquals("login fail", result);
		
		when(request.getParameter("username")).thenReturn("[");
		spyServlet.doPost(request, response);
		result = writer.getBuffer().toString();
		Assert.assertEquals("login fail", result);
		
		when(request.getParameter("username")).thenReturn("]");
		spyServlet.doPost(request, response);
		result = writer.getBuffer().toString();
		Assert.assertEquals("login fail", result);
		
		when(request.getParameter("username")).thenReturn("#");
		spyServlet.doPost(request, response);
		result = writer.getBuffer().toString();
		Assert.assertEquals("login fail", result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAuthenticateThrowInterruptedException() throws Exception {
		when(response.getWriter()).thenThrow(InterruptedException.class);
		servlet.doPost(request, response);
	}
}
