package csci310;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class PortfolioStock {
	private String symbol;
	private String name;
	private Double shares;
	private Double price;
	
	PortfolioStock(String symbol, String name, Double shares) {
		this.symbol = symbol;
		this.name = name;
		this.shares = shares;
		this.price = 0.00;
		
		setPrice();
	}
	
	public void setPrice() {
		String url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-summary?region=US&symbol=" + symbol;
		HttpResponse<JsonNode> response;
		try {			
//			response = Unirest.get(url)
//					.header("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
//					.header("x-rapidapi-key", "b649142cdemsh9271259a839a0e6p1038a7jsnd1899fcce2c0")
//					.asJson();
			
			// ^ accidentally exceeded monthly quota
			response = Unirest.get(url)
					.header("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
					.header("x-rapidapi-key", "b10d8afd88msh889e1e6cc42b810p1a2d5cjsn4b9330895aa2")
					.asJson();
			
			price = response.getBody().getObject().getJSONObject("financialData").getJSONObject("currentPrice").getDouble("raw");
		} catch (UnirestException e) {
			System.out.println("Failed to fetch stock data from the server.");
		}		
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

	public Double getShares() {
		return shares;
	}
	
	// Get the current stock price from Yahoo Finance API
	public Double getPrice() {
		return price;
	}
	
	public Double getTotalValue() {
		BigDecimal bd = new BigDecimal(price * shares).setScale(2, RoundingMode.HALF_EVEN);
		return bd.doubleValue();
	}
	
}
