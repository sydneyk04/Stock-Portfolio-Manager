package csci310;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Portfolio {
	private TreeMap<String, ArrayList<Double>> portfolio;
	private String username;
	private double totalValue;
	
	Portfolio (String username) {
		this.username = username;
		this.totalValue = 0;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void fetchData() {
		/*
		 * TreeMap of alphabetically ordered stocks in the user's portfolio as the key
		 * ArrayList of stock info [value, number of shares, total value] as the value
		*/
		TreeMap<String, ArrayList<Double>> map = new TreeMap<>();
		String stock = "GOOGL";
		ArrayList<Double> stockInfo = new ArrayList<>(Arrays.asList(116.60, 10.00, 1165.60));
		map.put(stock, stockInfo);
		portfolio = map;
	}
	
	public void calculateValue() {
		totalValue++;
	}
	
	public TreeMap<String, ArrayList<Double>> getData() {
		return portfolio;
	}
	
	public double getValue() {
		return totalValue;
	}
	
	public double calculateStockValue(String stockName, double price, double shares) {
		return price * shares;
	}
	
}
