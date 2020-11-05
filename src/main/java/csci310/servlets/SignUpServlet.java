package csci310.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.regex.Pattern;
import java.time.LocalDateTime;

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


@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static PrintWriter out;
	Boolean createdUser = false;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/plain");
	}
	
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		out = response.getWriter();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		
		String check = checkUserInputs(username, password, password2);
		
		if(check != null) {
			request.getRequestDispatcher("signup.jsp").include(request, response);
			out.println("<script>document.getElementById('error').innerHTML='" + check + "'; "
					+ "document.getElementById('error').style.visibility='visible';</script>");
		} else {
			//Create User
			String hashPw = password;
			try{
				hashPw = hashPassword(password);
			} catch (NoSuchAlgorithmException e) {
				out.print("signup fail");
				return;
			}
			createUser(username, hashPw, new MyCallback() {
				@Override
			    public void accountCreated() {
			    	createdUser = true;
			    	return;
			    }
			});
			
			//wait for that check to happen
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (createdUser) {
				//response.sendRedirect("/production/index.jsp");
				response.sendRedirect("login.jsp");
				return;
			} else {
				request.getRequestDispatcher("signup.jsp").include(request, response);
				out.println("<script>document.getElementById('error').innerHTML='That account already exists! Please try again.'; "
						+ "document.getElementById('error').style.visibility='visible';</script>");
				   
			}
			
		}
		
	}
	
	public String hashPassword(String pw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
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
	
	public String checkUserInputs(String username, String password, String password2) {
		if(username.contentEquals("") || password.contentEquals("") || password2.contentEquals("")) {
			return "Please fill out empty fields.";
		} else if (!password.equals(password2)) {
			return "Please ensure that your passwords match.";
		}
		 
		return null;
	}
	
	public void createUser(final String username, final String password, final MyCallback myCallback) throws IOException {
		initializeFireBase();
		
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		final DatabaseReference ref = database.getReference().child("users");
		
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
            public void onDataChange(DataSnapshot snapshot) {
				Long systemTime = System.currentTimeMillis();
				
                if (snapshot.child(username).exists()) {
                	createdUser = false;
                	return;
                } else {
                	ref.child(username).push();
                	ref.child(username).child("password").setValueAsync(password);
                	ref.child(username).child("portfolio").setValueAsync("none");
                	ref.child(username).child("loginTime").setValueAsync(systemTime);
                	ref.child(username).child("loginAttempts").setValueAsync(0);
                	myCallback.accountCreated();
                	return;
                }
            }

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	public interface MyCallback {
	    void accountCreated();
	}

	public FirebaseApp initializeFireBase() throws IOException {
		//FireBase Initialization
		//we need to figure out where we initialize it, since we should probably only do so once
		if (FirebaseApp.getApps().isEmpty()) {
			FileInputStream serviceAccount;
			serviceAccount = new FileInputStream("stock16-serviceaccount.json");
			@SuppressWarnings("deprecation")
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://stock16-e451e.firebaseio.com").build();
			return FirebaseApp.initializeApp(options);
		}
		return null;
	}
	
}