package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import csci310.Portfolio;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String HOMEPG= "home.jsp";
	
	private HttpSession session = null;
	private HttpServletResponse response = null;
	private PrintWriter out = null;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		session = request.getSession();
		this.response = response;
		out = response.getWriter();
		String username = (String) session.getAttribute("username");

		String action = request.getParameter("action");
		if (action != null && action.equals("logout")) {
			logout();
		}
		else {
			displayPortfolio(getPortfolio(username));
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		session = request.getSession();
		this.response = response;
		out = response.getWriter();
		String username = request.getParameter("username");
		
		String action = request.getParameter("action");
		if (action != null && action.equals("logout")) {
			logout();
		}
		else {
			displayPortfolio(getPortfolio(username));
		}
	}
	
	public Portfolio getPortfolio(String username) {
		Portfolio portfolio = null;
		
		if (username != null) {
			try {
				portfolio = new Portfolio(username);
			} catch (InterruptedException e) {
				System.out.println("Failed to create portfolio for " + username);
			}
		}
		
		return portfolio;
	}
	
	public void displayPortfolio(Portfolio portfolio) throws IOException {	
		// Wait for portfolio data to be updated before redirect
		for (int i = 0; i < 20; ++i) {
			try {
				TimeUnit.SECONDS.sleep(1);
				
				if (portfolio.getDataFetched()) {
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
				
		portfolio.calculateValue();
		session.setAttribute("portfolio", portfolio);
		response.sendRedirect(HOMEPG);
		return;
	}
	
	public void logout() throws IOException {
		session.setAttribute("username", null);
		session.setAttribute("login_error_message", null);
		out.print("logout success");	
		response.sendRedirect("login.jsp");
		out.flush();
	}

}
