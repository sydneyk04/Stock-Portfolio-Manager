package csci310.servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String LOGINPG= "login.jsp";
	private static String HOMEPG= "home.jsp";
	
	private HttpSession session = null;
	private HttpServletResponse response = null;
	private boolean dataFetched = false;
	private PrintWriter out = null;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			session = request.getSession();
			this.response = response;
			out = response.getWriter();
			dataFetched = false;
			String username = request.getParameter("username");
			String password = request.getParameter("password");
		
			authenticate(username, password);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void authenticate(final String username, final String password) throws InterruptedException {
		initializeFirebase();
		final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);
		
		userRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				if (snapshot.exists() && snapshot.child("password").getValue(String.class).equals(password)) {
					onAuthenticate(username);
				}
				else {
					onAuthenticate(null);
				}
			}
	
			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println(error.getMessage());
				out.print("server error");
				session.setAttribute("login_error_message", "Unable to access server");
				
				try {
					response.sendRedirect(LOGINPG);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		for (int i = 0; i < 30; ++i) {
			TimeUnit.SECONDS.sleep(1);
			if (dataFetched) {
				break;
			}
		}		
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
	
	private void onAuthenticate(String username) {
		try {
			dataFetched = true;
			out = response.getWriter();
			
			if (username != null) {
				out.print("login success");
				response.sendRedirect(HOMEPG);
			} else {
				out.print("login fail");
				session.setAttribute("login_error_message", "Invalid login and/or password");
				response.sendRedirect(LOGINPG);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
