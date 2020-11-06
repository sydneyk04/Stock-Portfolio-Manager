package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.RequestDispatcher;
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
public class StockPerformanceServletStaticTest extends Mockito{
 
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
    
    	StockPerformanceServlet servlet;
    
    	@Before
    	public void setUp() throws Exception {
        	request = mock(HttpServletRequest.class);
        	response = mock(HttpServletResponse.class);
        	session = mock(HttpSession.class);
        	rd = mock(RequestDispatcher.class);
        	servlet = new StockPerformanceServlet();
         
        	when(request.getSession()).thenReturn(session); 
        
        	mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
        
        	FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        	when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);
        
        	PowerMockito.mockStatic(FirebaseDatabase.class);
   
        	when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
    	}  
    
    	@Test
    	public void testGetUserStock() throws IOException, InterruptedException, ParseException {
    		when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);
    		servlet.from = Calendar.getInstance();
            servlet.from.add(Calendar.YEAR, -1);
            servlet.now = Calendar.getInstance();
    		
    		doAnswer(new Answer<Void>() {
				public Void answer(InvocationOnMock invocation) {
					ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
					DatabaseError error = mock(DatabaseError.class);
					valueEventListener.onCancelled(error);
					return null;     
				}
			}).when(mockedDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));
			      
			servlet.getUserStock("johnDoe");
    	}   
    	
    	@SuppressWarnings("unchecked")
		@Test
    	public void testAddStock() throws IOException {
    		Calendar c = Calendar.getInstance();
    		when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);
    		when(mockedDatabaseReference.updateChildrenAsync(anyMap())).thenReturn(null);
    		servlet.addStock("johnDoe", "GOOG", c, c, 1);
    	}
    
}
