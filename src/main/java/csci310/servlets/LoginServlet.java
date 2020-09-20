package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import csci310.Hello;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/plain");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (authenticate(username, password)) {
			//RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/home.jsp");
			rd.forward(request, response);
		}
		else {
			//$('#login-error-msg').show();
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Username and/or password incorrect');");
			out.println("$('#login-error-msg').show();");
			out.println("</script>");
		}
	}
	
	public Boolean authenticate(String username, String password) {
		if (username == "johnDoe" && password == "test123") {
			return true;
		}
		return false;
	}
}
