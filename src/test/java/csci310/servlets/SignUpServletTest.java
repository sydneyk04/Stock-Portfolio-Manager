package csci310.servlets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

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
	public void testDoGet() throws IOException, ServletException {	
		servlet.doGet(request, response);
	}
	
	@Test
	public void testDoPost() throws IOException, ServletException {		
		//check not null
		when(request.getRequestDispatcher("signup.jsp")).thenReturn(dispatcher);
		when(request.getParameter("username")).thenReturn("");
		when(request.getParameter("password")).thenReturn("");
		when(request.getParameter("password2")).thenReturn("");
		when(response.getWriter()).thenReturn(printWriter);
		servlet.doPost(request, response);	
		assertTrue(servlet.createdUser == false);
		
		//test user that already exists
		System.out.println();
		System.out.println("TEST USER THAT EXISTS:");
		when(request.getRequestDispatcher("signup.jsp")).thenReturn(dispatcher);
		when(request.getParameter("username")).thenReturn("johnDoe");
		when(request.getParameter("password")).thenReturn("test123");
		when(request.getParameter("password2")).thenReturn("test123");
		when(response.getWriter()).thenReturn(printWriter);
		servlet.doPost(request, response);	
		assertTrue(servlet.createdUser == false);
		
		//test new user
		//make new username
		Random rand = new Random();
		char a = (char)(rand.nextInt(26) + 'a');
		Random rand1 = new Random();
		char b = (char)(rand1.nextInt(26) + 'a');
		String user = new StringBuilder().append(a).append(b).toString();
		System.out.println();
		System.out.println("TEST NEW USER:");
		when(request.getRequestDispatcher("signup.jsp")).thenReturn(dispatcher);
		when(request.getParameter("username")).thenReturn(user);
		when(request.getParameter("password")).thenReturn("hi");
		when(request.getParameter("password2")).thenReturn("hi");
		when(response.getWriter()).thenReturn(printWriter);
		servlet.doPost(request, response);	
		System.out.println("created new user: " + servlet.createdUser);
		assertTrue(servlet.createdUser == true);
		
		//test thread.sleep interrupted exception
//		CountDownLatch mockLatch = mock(CountDownLatch.class);
//		when(mockLatch.await()).thenThrow(new InterruptedException());
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
	
//	@Test
//	public void testCreateUser() throws IOException {
//		//test user that already exists
//		final MyCallback callback = mock(MyCallback.class);
//		servlet.createUser("johnDoe", "test123", callback);
//		System.out.println("created user that alrdy exists: " + servlet.createdUser);
//		assertTrue(servlet.createdUser == false);
//		
//		//test new user
//		//make new username
//		Random rand = new Random();
//		char a = (char)(rand.nextInt(26) + 'a');
//		Random rand1 = new Random();
//		char b = (char)(rand1.nextInt(26) + 'a');
//		String user = new StringBuilder().append(a).append(b).toString();
//		//delete firebase
//		for (FirebaseApp app : FirebaseApp.getApps()) {
//			app.delete();
//		}
//		//generate callback
//		final MyCallback callback1 = mock(MyCallback.class);
//		servlet.createUser(user, "test123", callback1);
//		System.out.println("created new user: " + servlet.createdUser);
//		assertTrue(servlet.createdUser == true);
//	}
	
	

	
	
}
