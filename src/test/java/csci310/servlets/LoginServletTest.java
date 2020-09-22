package csci310.servlets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.PrintWriter;
import java.io.StringWriter;

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

public class LoginServletTest extends Mockito{
	
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
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        rd = mock(RequestDispatcher.class);
        servlet = new LoginServlet();
         
        when(request.getSession()).thenReturn(session);        
    }
	
	@Test
	public void testInitializeFirebaseNotNull() {
		for (FirebaseApp app : FirebaseApp.getApps()) {
			app.delete();
		}
		assertNotNull(servlet.initializeFirebase());
	}
	
	@Test
	public void testInitializeFirebaseNull() {	
		assertNull(servlet.initializeFirebase());
	}
	
	@Test
    public void testLoginSuccess() throws Exception {
		StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
		
        when(response.getWriter()).thenReturn(out);
    	when(request.getParameter("username")).thenReturn("johnDoe");
    	when(request.getParameter("password")).thenReturn("test123");    	
        

    	servlet.doPost(request, response); 	
        String result = writer.getBuffer().toString();
        
    	Assert.assertEquals("login success", result);
    }
    
	@Test
    public void testLoginFail() throws Exception {
    	StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        
        when(response.getWriter()).thenReturn(out);
    	when(request.getParameter("username")).thenReturn("johnDoe");
    	when(request.getParameter("password")).thenReturn("pw");   

    	servlet.doPost(request, response);
    	String result = writer.getBuffer().toString();
        
    	Assert.assertEquals("login fail", result);
    }

}
