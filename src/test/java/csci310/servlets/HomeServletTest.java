package csci310.servlets;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class HomeServletTest {
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
		servlet = new HomeServlet();
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() {
		servlet.doPost(request, response);
		assertTrue(true);
	}

	@Test
	public void testFetchPortfolio() {
		TreeMap<String, ArrayList<Double>> portfolio = servlet.fetchPortfolio();
		assertTrue(portfolio.size() == 1);
	}

	@Test
	public void testDisplayPortfolio() {
		TreeMap<String, ArrayList<Double>> portfolio = servlet.fetchPortfolio();
		String portfolioContents = servlet.displayPortfolio(portfolio);
		assertTrue(portfolioContents.equals("portfolio"));
	}

	@Test
	public void testCalculatePortfolio() {
		TreeMap<String, ArrayList<Double>> portfolio = servlet.fetchPortfolio();
		assertTrue(servlet.calculatePortfolio(portfolio) == 10.00);
	}

	@Test
	public void testCalculateStock() {
		assertTrue(servlet.calculateStock(10.0, 10.0) == 100.00);
	}

	@Test
	public void testLogout() {
		servlet.logout();
		assertTrue(true);
	}

}
