package csci310.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
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
	private static String HOMESERVLET= "/home";
	private static String DASHBOARDSERVLET= "/dashboard";
	
	private HttpSession session = null;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private boolean dataFetched = false;
	private PrintWriter out = null;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			session = request.getSession();
			this.request = request;
			this.response = response;
			out = response.getWriter();
			dataFetched = false;
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String hashPw = hashPassword(password);
			
			if(username != null) {
				if(username.contains(".") || username.contains("$") || 
						username.contains("[") || username.contains("]") || username.contains("#")) {
					onAuthenticate(null);
				}
			}
			authenticate(username, hashPw);
					
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static String hashPassword(String pw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] hash = messageDigest.digest(pw.getBytes("UTF-8"));
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return (hexString.toString());
	}
	
	public void authenticate(final String username, final String password) throws InterruptedException, IOException {
		initializeFirebase("stock16-serviceaccount.json");
		final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);
		
		userRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				if (snapshot.exists() && snapshot.child("password").getValue(String.class).equals(password)) {
					//authentic log in
					if(!lockedout(snapshot)) {
						//not locked out
						onAuthenticate(username);
					}
					else {
						//locked out
						onAuthenticate(null);
					}
				}
				else if(!snapshot.exists()) {
					//no matching username
					onAuthenticate(null);
				}
				else {
					//invalid password 
					addLockout(snapshot);
					onAuthenticate(null);
				}
			}
	
			private void addLockout(DataSnapshot snapshot) {
				// TODO Auto-generated method stub                  
				
				
				
			}

			private boolean lockedout(DataSnapshot snapshot) {
				// TODO Auto-generated method stub
				
				return false;
			}

			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println(error.getMessage());
				out.print("server error");
				session.setAttribute("login_error_message", "Unable to access server");
				
				try {
					response.sendRedirect(LOGINPG);
				} catch (Exception e) {
					//e.printStackTrace();
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
	public FirebaseApp initializeFirebase(String filename) throws IOException {
		if (FirebaseApp.getApps().isEmpty()) {
			FileInputStream serviceAccount;
			
			serviceAccount = new FileInputStream(filename);
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://stock16-e451e.firebaseio.com").build();
			return FirebaseApp.initializeApp(options);
		}
		return null;
	}
	
	private void onAuthenticate(String username) {
		try {
			dataFetched = true;
			out = response.getWriter();
			
			if (username != null) {
				out.print("login success");
				session.setAttribute("username", username);
				//response.sendRedirect(HOMESERVLET);
				response.sendRedirect(DASHBOARDSERVLET);
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
