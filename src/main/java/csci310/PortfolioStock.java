package csci310;

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
	
	public Double getPrice() {
		return price;
	}
	
	public Double getTotal() {
		return price * shares;
	}
	
	public void setPrice() {
		price++;
	}
	
}
