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
	private static String LOGINPG= "login.jsp";
	private static String INDEXPG= "production/index.jsp";
	
	private HttpSession session = null;
	private HttpServletResponse response = null;
	private PrintWriter out = null;
	
	// Servlets - initialize servlets here (e.g. HomeServlet homeServlet = new HomeServlet();)
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		session = request.getSession();
		this.response = response;
		out = response.getWriter();
		
		// Call functions of servlet here (e.g. homeServlet.doGet(request, response);)
		
		
		String action = request.getParameter("action");
		if (action != null && action.equals("logout")) {
			logout();
			return;
		} 
		else {
			response.sendRedirect(INDEXPG);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		session = request.getSession();
		this.response = response;
		out = response.getWriter();
		
		// Call functions of servlet here (e.g. homeServlet.doPost(request, response);)
		
		
		String action = request.getParameter("action");
		if (action != null && action.equals("logout")) {
			logout();
			return;
		}
		else {
			response.sendRedirect(INDEXPG);
		}
	}
	
	public void logout() throws IOException {
		session.setAttribute("username", null);
		session.setAttribute("login_error_message", null);
		out.print("logout success");	
		response.sendRedirect(LOGINPG);
		out.flush();
	}
	
}
