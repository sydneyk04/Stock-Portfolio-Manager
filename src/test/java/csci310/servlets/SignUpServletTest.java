package csci310.servlets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class SignUpServletTest extends Mockito {
	static SignUpServlet servlet;
	static HttpServletRequest request;
	static HttpServletResponse response;
	static PrintWriter printWriter;
	
	@BeforeClass
    public static void setup() {
		servlet = new SignUpServlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		printWriter = new PrintWriter(new StringWriter());
    }
	
	@Test
	public void testInitializeFirebase() {	
		assertNotNull(servlet.initializeFireBase());
	}
	
	@Test
	public void testcheckUserInputs() throws ServletException, IOException {
		//missing field login
		Boolean output = servlet.checkUserInputs("johnDoe", "", "", request);
		assertTrue(output.equals(false));
		
		//passwords do not match
		Boolean output1 = servlet.checkUserInputs("johnDoe", "test123", "test922929", request);
		assertTrue(output1.equals(false));
		
		//correct login
		Boolean output2 = servlet.checkUserInputs("johnDoe", "test123", "test123", request);
		assertTrue(output2.equals(true));
	}
	
	@Test
	public void testCreateUser() {
		Boolean output = servlet.createUser("johnDoe", "test123");
		assertTrue(output == true);
	}
	
	@Test
	public void testDoPost() {		
		
		
	}

	
	
}
