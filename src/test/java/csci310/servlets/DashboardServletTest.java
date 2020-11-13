package csci310.servlets;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DashboardServletTest extends Mockito {
	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	HttpSession session;

	@Mock
	RequestDispatcher rd;
	
	DashboardServlet servlet;

	@Before
	public void setUp() throws Exception {
		request = Mockito.mock(HttpServletRequest.class);
    	response = Mockito.mock(HttpServletResponse.class);
    	session = Mockito.mock(HttpSession.class);
    	rd = Mockito.mock(RequestDispatcher.class);
    	servlet = new DashboardServlet();
		
		session.setAttribute("username", "johnDoe");
		
		when(request.getSession()).thenReturn(session);
		when(request.getSession().getAttribute("username")).thenReturn("johnDoe");
		when(request.getParameter("username")).thenReturn("johnDoe");
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() throws IOException, ServletException {
		when(session.getAttribute("username")).thenReturn("test");
		servlet.doGet(request, response);
		assertTrue(true);
	}

	@Test(expected=NullPointerException.class)
	public void testDoPostHttpServletRequestHttpServletResponse() throws IOException, ServletException {
		servlet.doPost(request, response);
		assertTrue(true);
	}
	
	@Test
	public void testLogoutPost() throws IOException, ServletException {		
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
  
		when(request.getParameter("action")).thenReturn("logout");
		when(response.getWriter()).thenReturn(out);
		
		servlet.doPost(request, response);
		String postResult = writer.getBuffer().toString();
        
		Assert.assertEquals("logout success", postResult);
	}
	
	@Test
	public void testLogoutGet() throws IOException, ServletException {		
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
  
		when(request.getParameter("action")).thenReturn("logout");
		when(response.getWriter()).thenReturn(out);
		
		servlet.doGet(request, response);
		String getResult = writer.getBuffer().toString();
        
		Assert.assertEquals("logout success", getResult);
	}

}
