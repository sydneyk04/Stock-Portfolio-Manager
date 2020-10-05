package csci310;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PortfolioStockTest {
	PortfolioStock stock;

	@Before
	public void setUp() throws Exception {
		stock = new PortfolioStock("t", "test", 10.00);
	}
	
	@Test
	public void testGetTotalValue() {
		assertTrue(stock.getTotalValue() == 10.00);
	}

	@Test
	public void testSetPrice() {
		stock.setPrice();
		assertTrue(stock.getPrice() == 2);
	}

}
