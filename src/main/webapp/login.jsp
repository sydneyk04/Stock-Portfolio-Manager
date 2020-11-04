<%@ page import="csci310.*" %>
<!DOCTYPE html>
<%
	//Disable Caching
	response.setHeader("Cache-Control", "no-cache, no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	
	session.setAttribute("username", null);

	String error_message = (String) session.getAttribute("login_error_message");
%>
<html>
  <head>
    <meta charset="ISO-8859-1">
    <title>Login</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  	<!-- Firebase Auth Testing -->
	  <script src="https://cdn.firebase.com/libs/firebaseui/3.5.2/firebaseui.js"></script>
	  <link type="text/css" rel="stylesheet" href="https://cdn.firebase.com/libs/firebaseui/3.5.2/firebaseui.css" />
    <link rel="stylesheet" href="styles/login.css">
    <link href="https://fonts.googleapis.com/css2?family=Raleway:wght@300&display=swap" rel="stylesheet">  </head>
  
  	<script>
		// Back button pressed
		if (window.performance && window.performance.navigation.type == window.performance.navigation.TYPE_BACK_FORWARD) {
			session.setAttribute("username", null);
			session.setAttribute("login_error_message", null);
			session.setAttribute("myStocks", null);
		}
	</script>
  
  <body>
    <header style="height:70px; background:#787878;">
  			<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-left">
		      	<div id="banner-content" class="navbar-brand" style="color:white;font-size:45px;font-family: 'Raleway', sans-serif;">
		      		<a href="index.jsp" style="text-decoration: none; color:white;" >
				   		USC CS 310 Stock Portfolio Management 
					</a>
		      	</div>
	   		</nav>
	 	 </header>
      
    <div class="container h-100 justify-content-center align-items-center">
          
          
          <form id="login-form" action="/login" method="post">
            <h1>Login</h1>

            
            <div class="form-group">
              <label>Username</label>
              <input id="usrname" type="text" name="username" class="form-control" placeholder="username" required>
            </div>

            <div class="form-group">
              <label>Password</label>
              <input id="password" type="password" name="password" class="form-control" placeholder="password" required>
            </div>

            <div id="login-error-container">
				      <strong id="login_error" style="color:red"><%if(error_message != null){ %> <%= error_message%> <% } %></strong>
            </div>
            
            <div class="form-group">
              <input type="submit" class="btn btn-lg btn-block btn-primary" value="Login" id="login-form-submit" onclick="authenticate()">
            </div>

            <p>Don't have an account? <a href="signup.jsp">Signup here</a>.</p>
          </form>
    </div>
  </body>
</html>
