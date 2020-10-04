package csci310;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class PortfolioTest {
	Portfolio portfolio;
	
	@Before
	public void setUp() throws Exception {
		portfolio = new Portfolio("johnDoe");
	}

	@Test
	public void testFetchPortfolio() {
		portfolio.fetchData();
		assertTrue(portfolio.getData().size() == 1);
	}

	@Test
	public void testCalculateValue() {
		portfolio.calculateValue();
		assertTrue(portfolio.getValue() == 1);
	}

	@Test
	public void testCalculateStockValue() {
		assertTrue(portfolio.calculateStockValue("stock", 10.0, 10.0) == 100.00);
	}

}
