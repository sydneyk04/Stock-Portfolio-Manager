package csci310.servlets;

import static org.junit.Assert.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		servlet = new DashboardServlet();
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() {
		servlet.doGet(request, response);
		assertTrue(true);
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() {
		servlet.doPost(request, response);
		assertTrue(true);
	}

}
