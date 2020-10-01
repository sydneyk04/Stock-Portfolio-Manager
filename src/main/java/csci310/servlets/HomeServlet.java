package csci310.servlets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	public TreeMap<String, ArrayList<Double>> fetchPortfolio() {
		/*
		 * TreeMap of alphabetically ordered stocks in the user's portfolio as the key
		 * ArrayList of stock info [value, number of shares, total value] as the value
		*/
		TreeMap<String, ArrayList<Double>> map = new TreeMap<>();
		String stock = "GOOGL";
		ArrayList<Double> stockInfo = new ArrayList<>(Arrays.asList(116.60, 10.00, 1165.60));
		map.put(stock, stockInfo);
		return map;
	}
	
	public String displayPortfolio(TreeMap<String, ArrayList<Double>> portfolio) {
		return "portfolio";
	}
	
	public double calculatePortfolio(TreeMap<String, ArrayList<Double>> portfolio) {
		return 10.00;
	}
	
	public double calculateStock(double value, double shares) {
		return value * shares;
	}
	
	public void logout() {
		
	}

}
