<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="ISO-8859-1">
    <title>Login</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
  </head>
  <body>
    <div class="container h-100 d-flex justify-content-center align-items-center">
      <div class="row">
        <div class="col-md-12">
          <h2>Login</h2>
          <div id="login-error-msg" div class="alert alert-danger collapse" role="alert">Invalid username and/or password</div>
          <form id="login-form" action="/login" method="post">
            <div class="form-group">
              <label>Username</label>
              <input type="text" name="username" class="form-control" required>
            </div>
            <div class="form-group">
              <label>Password</label>
              <input type="password" name="password" class="form-control" required>
            </div>
            <div class="form-group">
              <input type="submit" class="btn btn-primary" value="Login" id="login-form-submit" onclick="authenticate()">
            </div>
            <p>Don't have an account? <a href="signup.html">Signup here</a>.</p>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>
