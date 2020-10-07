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

import csci310.Portfolio;

public class HomeServletTest extends Mockito {
	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	HttpSession session;

	@Mock
	RequestDispatcher rd;

	HomeServlet servlet;

	@Before
	public void setUp() throws Exception {
		request = Mockito.mock(HttpServletRequest.class);
    	response = Mockito.mock(HttpServletResponse.class);
    	session = Mockito.mock(HttpSession.class);
    	rd = Mockito.mock(RequestDispatcher.class);
		servlet = new HomeServlet();
		
		session.setAttribute("username", "johnDoe");
		
		when(request.getSession()).thenReturn(session);
		when(request.getSession().getAttribute("username")).thenReturn("johnDoe");
		when(request.getParameter("username")).thenReturn("johnDoe");
	}
	
	@Test
	public void testDoGet() throws IOException, ServletException {
		servlet.doGet(request, response);
		assertTrue(true);
	}

	@Test
	public void testDoPost() throws IOException, ServletException {
		servlet.doPost(request, response);
		assertTrue(true);
	}

	@Test
	public void testGetPortfolio() {
		Portfolio portfolio = servlet.getPortfolio("johnDoe");
		assertTrue(portfolio.getStocks().size() == 2);
	}
	
	@Test
	public void testDisplayPortfolio() throws IOException {
		servlet.doPost(request, response);
		
		Portfolio portfolio = servlet.getPortfolio("johnDoe");
		servlet.displayPortfolio(portfolio);
		
		verify(session).setAttribute("portfolio", portfolio);
	}

	@Test
	public void testLogout() throws IOException {		
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
  
		when(request.getParameter("action")).thenReturn("logout");
		when(response.getWriter()).thenReturn(out);
		
		servlet.doPost(request, response);
		String result = writer.getBuffer().toString();
        
		Assert.assertEquals("logout success", result);
	}

}
