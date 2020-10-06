package csci310.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


@WebServlet("/stockperformance")
public class StockPerformanceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static PrintWriter out;
	Boolean check = false;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/plain");
		request.setAttribute("stockName", "Kendall's Stock");
		request.setAttribute("stockCode", "KS");
		request.setAttribute("stockPrice", "15.33");
		check = true;
	}
	
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		out = response.getWriter();
		check = true;
	}
}