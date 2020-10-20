package csci310;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.firebase.FirebaseApp;

public class PortfolioTest {
	Portfolio portfolio;
	
	@Before
	public void setUp() throws Exception {
		portfolio = new Portfolio("johnDoe");
	}
	
	@Test
	public void testGetUsername() {
		assertTrue(portfolio.getUsername().equals("johnDoe"));
	}
	
    @Test
    public void testInitializeFirebaseNotNull() throws IOException {
    	for (FirebaseApp app : FirebaseApp.getApps()) {
			app.delete();
		}
		assertNotNull(portfolio.initializeFirebase("stock16-service-account.json"));
   	 }
 
	@Test
	public void testInitializeFirebaseNull() { 
		assertNull(portfolio.initializeFirebase("stock16-service-account.json"));
	}


}
