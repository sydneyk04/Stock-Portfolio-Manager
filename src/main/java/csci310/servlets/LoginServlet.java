package csci310.servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String LOGINPG= "login.jsp";
	private static String HOMEPG= "home.jsp";
	private static HttpServletRequest postRequest;
	private static HttpServletResponse postResponse;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);
		RequestDispatcher dispatcher = request.getRequestDispatcher(LOGINPG);
		dispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		postRequest = request;
		postResponse = response;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		authenticate(username, password);
	}
	
	@SuppressWarnings("deprecation")
	public FirebaseApp initializeFirebase() {
		if (FirebaseApp.getApps().isEmpty()) {
			FileInputStream serviceAccount;
			try {
				serviceAccount = new FileInputStream("stock16-service-account.json");
				FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://stock16-e451e.firebaseio.com").build();
				return FirebaseApp.initializeApp(options);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void authenticate(final String username, final String password) {
		initializeFirebase();
		final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);
		
		userRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				if (snapshot.exists()) {
					if (snapshot.child("password").getValue(String.class).equals(password)); {
						onAuthenticate(true);
					}
				}
				else {
					onAuthenticate(false);
				}
			}
	
			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println(error.getMessage());
			}
		});
	}

	public void onAuthenticate(Boolean isAuthenticated) {
		try {
			if (isAuthenticated) {
				postResponse.sendRedirect(HOMEPG);
			} else {
				PrintWriter out = postResponse.getWriter();
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Username and/or password incorrect');");
				out.println("$('#login-error-msg').show();");
				out.println("</script>");
				postRequest.getRequestDispatcher(LOGINPG).forward(postRequest, postResponse);
			}
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}
}
