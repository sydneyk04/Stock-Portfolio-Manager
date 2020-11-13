package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
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
	private static String LOGINPG= "login.jsp";
	private static String INDEXPG= "production/index.jsp";
	private HttpSession session = null;
	private HttpServletResponse response = null;
	private PrintWriter out = null;
	
	// Servlets - initialize servlets here (e.g. HomeServlet homeServlet = new HomeServlet();)
	private StockPerformanceServlet stockperformanceServlet = new StockPerformanceServlet();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		session = request.getSession();
		this.response = response;
		out = response.getWriter();
		
		String action = request.getParameter("action");
		if (action != null && action.equals("logout")) {
			logout();
			return;
		} else if (action != null){
			// Call functions of servlet here (e.g. homeServlet.doGet(request, response);)
			stockperformanceServlet.doGet(request, response);
			response.sendRedirect(INDEXPG);
		} else {
			stockperformanceServlet.doGet(request, response);
			response.sendRedirect(INDEXPG);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		session = request.getSession();
		this.response = response;
		out = response.getWriter();
		
		String action = request.getParameter("action");
		if (action != null && action.equals("logout")) {
			logout();
			return;
		}
		else {
			// Call functions of servlet here (e.g. homeServlet.doPost(request, response);)
			stockperformanceServlet.doPost(request, response);
			response.sendRedirect(INDEXPG);
		}
	}
	
	public void logout() throws IOException {
		session.setAttribute("username", null);
		session.setAttribute("login_error_message", null);
		
		//reset variables in graph servlet
		session.setAttribute("myStocks", null);
		session.setAttribute("from", null);
		session.setAttribute("now", null);
		session.setAttribute("chart", null);
		session.setAttribute("invalid_error", null);
		session.setAttribute("failedAdd", null);
		session.setAttribute("portfolioVal", null);
		session.setAttribute("portfolioPercentage", null);

		stockperformanceServlet.myStocks = new ArrayList<ArrayList>();
		stockperformanceServlet.view = new ArrayList<ArrayList>();
		stockperformanceServlet.jsons = new ArrayList<String>();
		stockperformanceServlet.portfolioValHistory = new ArrayList<ArrayList>();
		stockperformanceServlet.portfolioJSON = "";
		
		out.print("logout success");	
		response.sendRedirect(LOGINPG);
		out.flush();
	}
	
}
