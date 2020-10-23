package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class, FirebaseOptions.class} )
@PowerMockIgnore("jdk.internal.reflect.*")
public class SignUpServletStaticTest extends Mockito{
 
		@Mock
    	HttpServletRequest request;

    	@Mock
    	HttpServletResponse response;
    
    	@Mock
    	HttpSession session;
    
    	@Mock
    	RequestDispatcher rd;
    
    	@Mock
    	DatabaseReference mockedDatabaseReference;
    
    	SignUpServlet servlet;
    
    	@Before
    	public void setUp() throws Exception {
        	request = mock(HttpServletRequest.class);
        	response = mock(HttpServletResponse.class);
        	session = mock(HttpSession.class);
        	rd = mock(RequestDispatcher.class);
        	servlet = new SignUpServlet();
         
        	when(request.getSession()).thenReturn(session); 
        	doNothing().when(response).setContentType(anyString());
        	doNothing().when(response).setStatus(anyInt());
        	mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
        
        	FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        	when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);
        
        	PowerMockito.mockStatic(FirebaseDatabase.class);
   
        	when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
    	}  
    
    	@Test
    	public void testCreateUserOnCancelled() throws IOException, ServletException {
			when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);
			 
			StringWriter writer = new StringWriter();
			PrintWriter out = new PrintWriter(writer);
			 
			//doNothing().when(mockedDatabaseReference).push();
			
			 
			doAnswer(new Answer<Void>() {
				public Void answer(InvocationOnMock invocation) {
					ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
					DatabaseError error = mock(DatabaseError.class);
					valueEventListener.onCancelled(error);
					return null;     
				}
			}).when(mockedDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));
			      
			servlet.createUser("username", "password", null);
    	}   
    
}
