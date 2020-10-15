package csci310;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class PortfolioStock {
	private String symbol;
	private String name;
	private Double shares;
	private Double price;
	private Stock stock;
	
	PortfolioStock(String symbol, String name, Double shares) {
		this.symbol = symbol;
		this.name = name;
		this.shares = shares;
		this.price = 0.00;
		
		setPrice();
	}
	
	public Stock getStock(String symbol) {
		try {
			return YahooFinance.get(symbol);
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}
	
	public void setPrice() {
		stock = getStock(symbol);
		
		if (stock != null) {
			price = stock.getQuote().getPrice().doubleValue();
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
