/**
 * 
 */
package csci310.servlets;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import yahoofinance.Stock;


public class SearchServletTest extends Mockito {

	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	RequestDispatcher rs;
	
	SearchServlet ss;
	
	@Before
	public void setup() {
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
		rs = Mockito.mock(RequestDispatcher.class);
		ss = new SearchServlet();
	}
	
	/**
	 * Test method for {@link csci310.servlets.SearchServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws IOException, ServletException {
		when(request.getParameter("stockName")).thenReturn("GOOG");
		when(request.getRequestDispatcher(anyString())).thenReturn(rs);
		doNothing().when(rs).forward(request, response);
		ss.doPost(request, response);
		verify(request, times(1)).getRequestDispatcher("/stockperformance");
		
		when(request.getParameter("stockName")).thenReturn("1234");
		ss.doPost(request, response);
		verify(response, times(1)).sendRedirect("notfound.jsp");
	}

	/**
	 * Test method for {@link csci310.servlets.SearchServlet#getStock(java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testGetStock() throws IOException {
		Stock s = ss.getStock("GOOG");
		assertNotNull(s);
	}

}
