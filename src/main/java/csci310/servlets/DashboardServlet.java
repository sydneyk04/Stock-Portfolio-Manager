package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Main servlet for dashboard page that contains instances of other servlets
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INDEXPG= "production/index.html"; // do i redirect to index.html or index.jsp?
	
	private HttpSession session = null;
	private HttpServletResponse response = null;
	private PrintWriter out = null;
	
	// Servlets
	private HomeServlet homeServlet = new HomeServlet();	// grabs user's portfolio from firebase
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		homeServlet.doGet(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		homeServlet.doPost(request, response);
	}
	
}
