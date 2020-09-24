<%@ page import="csci310.*" %>
<!DOCTYPE html>
<%
	//Disable Caching
	response.setHeader("Cache-Control", "no-cache, no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);

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
  </head>
  <body>
    <div class="container h-100 d-flex justify-content-center align-items-center">
      <div class="row">
        <div class="col-md-12">
          <h2>Login</h2>
          <form id="login-form" action="/login" method="post">
          	<div id="login-error-container">
				<strong id="login_error" style="color:red"><%if(error_message != null){ %> <%= error_message%> <% } %></strong>
			</div>
            <div class="form-group">
              <label>Username</label>
              <input id="usrname" type="text" name="username" class="form-control" required>
            </div>
            <div class="form-group">
              <label>Password</label>
              <input id="password" type="password" name="password" class="form-control" required>
            </div>
            <div class="form-group">
              <input type="submit" class="btn btn-primary" value="Login" id="login-form-submit" onclick="authenticate()">
            </div>
            <p>Don't have an account? <a href="signup.jsp">Signup here</a>.</p>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>
