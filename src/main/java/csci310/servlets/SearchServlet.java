package csci310.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@WebServlet("/search")
public class SearchServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private HttpServletResponse response = null;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.response = response;
		
		String stockName = request.getParameter("stockName");
		Stock s = getStock(stockName);
		
		if(s == null) {
			this.response.sendRedirect("notfound.jsp");
		}
		else {
			this.response.sendRedirect("/stock.jsp?stockName=" + stockName);
		}
	}
	
	public Stock getStock(String stockName) throws IOException {
		return YahooFinance.get(stockName);
	}
}
