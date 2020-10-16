package csci310;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;

//@RunWith(PowerMockRunner.class)
//@PowerMockRunnerDelegate(JUnit4.class)
//@PrepareForTest(Unirest.class)
//@PowerMockIgnore("jdk.internal.reflect.*")
//public class PortfolioStockTest extends Mockito {
public class PortfolioStockTest {
	
	@Mock
	private Unirest unirest;

	@Mock
    private GetRequest getRequest;

    @Mock
    private HttpResponse<JsonNode> httpResponse;

    @Mock
    private HttpRequestWithBody requestWithBody;

    @Mock
    private RequestBodyEntity requestBodyEntity;

    @Mock
    JsonNode value;
	
	PortfolioStock stock;

	@Before
	public void setUp() throws Exception {
		//PowerMockito.mockStatic(Unirest.class);
		
		stock = new PortfolioStock("GOOG", "GOOGLE", 10.00);
	}
	
	@Test
	public void testGetStock() {
		assertNotNull(stock.getStock("GOOG"));
		assertNull(stock.getStock("FDAGRSRGSHSFGFG"));
	}
	
	@Test
	public void testGetTotalValue() {
		assertTrue("Actual value " + stock.getTotalValue() + " is not greater than 0", stock.getTotalValue() > 0);
	}
	
	@Test
	public void testGetSymbol() {
		assertTrue(stock.getSymbol().equals("GOOG"));
	}
	
	@Test
	public void testGetName() {
		assertTrue(stock.getName().equals("GOOGLE"));
	}

	@Test
	public void testSetPrice() {
		stock.setPrice();
		assertTrue(stock.getPrice() > 0);
		
		PortfolioStock nullStock = new PortfolioStock("FDAGRSRGSHSFGFG", "FDAGRSRGSHSFGFG", 10.00);
		nullStock.setPrice();
		assertTrue(nullStock.getPrice() == 0);
	}

}
