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
			out.println("<script>document.getElementById('error').innerHTML='" + check + "'; </script>");
		} else {
			//Create User
			createUser(username, password, new MyCallback() {
				@Override
			    public void accountExists() {
					createdUser = false;
			     }
			    
			    public void accountCreated() {
			    	System.out.println("abt to make true");
			    	createdUser = true;
			    }
			    
			});
			//make sleep while checks Firebase for account
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (createdUser) {
				System.out.println("account created!");
				response.sendRedirect("/home.jsp");
			} else {
				System.out.println("account alrdy exists!");
				request.getRequestDispatcher("signup.jsp").include(request, response);
				out.println("<script>document.getElementById('error').innerHTML='That account already exists! Please try again.'; </script>");
				   
			}
			
		}
		
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
		
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		final DatabaseReference ref = database.getReference().child("users");
		
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
            
			@Override
            public void onDataChange(DataSnapshot snapshot) {
				//if username already exists
                if (snapshot.child(username).exists()) {
                	myCallback.accountExists();
                	return;
                } else {
                	ref.child(username).push();
                	ref.child(username).child("password").setValueAsync(password);
                	System.out.println("calling account Created");
                	myCallback.accountCreated();
                }
            }

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public interface MyCallback {
	    void accountExists();
	    void accountCreated();
	}

	public FirebaseApp initializeFireBase() throws IOException {
		//FireBase Initialization
		//we need to figure out where we initialize it, since we should probably only do so once
		if (FirebaseApp.getApps().isEmpty()) {
			
			FileInputStream serviceAccount;
//			try {
				serviceAccount = new FileInputStream("stock16-serviceaccount.json");
				@SuppressWarnings("deprecation")
				FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://stock16-e451e.firebaseio.com").build();
				return FirebaseApp.initializeApp(options);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		return null;
	}
	
}