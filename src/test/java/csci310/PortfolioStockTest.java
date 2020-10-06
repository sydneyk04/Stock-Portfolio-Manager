package csci310;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PortfolioStockTest {
	PortfolioStock stock;

	@Before
	public void setUp() throws Exception {
		stock = new PortfolioStock("GOOG", "GOOGLE", 10.00);
	}
	
	@Test
	public void testGetTotalValue() {
		double total = stock.getPrice() * stock.getShares();
		assertTrue(stock.getTotalValue() == total);
	}

	@Test
	public void testSetPrice() {
		stock.setPrice();
		assertTrue(stock.getPrice() > 0);
	}

}
