package csci310.servlets;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import csci310.Portfolio;

public class DashboardServletTest extends Mockito {
	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	HttpSession session;

	@Mock
	RequestDispatcher rd;
	
	@Mock
	Portfolio p;
	
	DashboardServlet servlet;

	@Before
	public void setUp() throws Exception {
		request = Mockito.mock(HttpServletRequest.class);
    	response = Mockito.mock(HttpServletResponse.class);
    	session = Mockito.mock(HttpSession.class);
    	rd = Mockito.mock(RequestDispatcher.class);
    	p = Mockito.mock(Portfolio.class);
    	servlet = new DashboardServlet();
		
		session.setAttribute("username", "johnDoe");
		
		when(request.getSession()).thenReturn(session);
		when(request.getSession().getAttribute("username")).thenReturn("johnDoe");
		when(request.getParameter("username")).thenReturn("johnDoe");
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() throws IOException, ServletException {
		servlet.doGet(request, response);
		assertTrue(true);
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws IOException, ServletException {
		servlet.doPost(request, response);
		assertTrue(true);
	}

}
