package csci310.servlets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.firebase.FirebaseApp;

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
	public void testDoGet() throws IOException, ServletException {	
		servlet.doGet(request, response);
	}
	
	@Test
	public void testDoPost() throws IOException, ServletException {		
		when(request.getParameter("username")).thenReturn("hi");
		when(request.getParameter("password")).thenReturn("hi");
		when(request.getParameter("password2")).thenReturn("hi");
		servlet.doPost(request, response);	
	}
	
	
	@Test
	public void testInitializeFirebase() {	
		FirebaseApp fb = servlet.initializeFireBase();
		assertTrue(fb != null);
	}
	
	@Test
	public void testcheckUserInputs() throws ServletException, IOException {
//		missing field login
		String output = servlet.checkUserInputs("johnDoe", "", "");
		assertTrue(output.equals("Please fill out empty fields."));
		
		//passwords do not match
		String output1 = servlet.checkUserInputs("johnDoe", "test123", "test922929");
		assertTrue(output1.equals("Please ensure that your passwords match."));
		
		//correct login
		String output2 = servlet.checkUserInputs("johnDoe", "test123", "test123");
		assertTrue(output2 == null);
	}
	
	@Test
	public void testCreateUser() {
		//test user that already exists
		Boolean output = servlet.createUser("johnDoe", "test123");
		assertTrue(output == true);
		
		//test new user
		Random rand = new Random();
		char a = (char)(rand.nextInt(26) + 'a');
		Random rand1 = new Random();
		char b = (char)(rand1.nextInt(26) + 'a');
		String user = new StringBuilder().append(a).append(b).toString();
		Boolean output1 = servlet.createUser(user, "test123");
		assertTrue(output1 == true);
	}
	
	

	
	
}
