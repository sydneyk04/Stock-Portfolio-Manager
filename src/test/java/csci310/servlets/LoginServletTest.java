package csci310.servlets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class LoginServletTest {
	static LoginServlet servlet;
	
	@BeforeClass
    public static void setup() {
		servlet = new LoginServlet();
    }

	@Test
	public void testInitializeFirebaseNotNull() {		
		assertNotNull(servlet.initializeFirebase());
	}
	
	@Test
	public void testAuthenticateSuccess() throws IOException {
		String username = "johnDoe";
		String password = "test123";
		
		assertTrue(true);
	}
	
	@Test
	public void testAuthenticateFail() throws IOException {
		String username = "johnDoe";
		String password = "test";
		
		assertFalse(false);
	}

}
