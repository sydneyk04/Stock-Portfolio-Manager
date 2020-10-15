<html>
	<head>
		<title>Sign Up</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="signup.css">
		<link href="https://fonts.googleapis.com/css2?family=Raleway:wght@300&display=swap" rel="stylesheet">
	</head>
	<body>
		<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-left">
	      	<div id="banner-content" class="navbar-brand" style="color:white;font-size:45px">
	      		USC 310 Stock Portfolio Management
	      	</div>
		</nav>
		
		<div class="container">

		
		<h1 class="text-center">Create an account now!</h1>
		<form class = "form-signin" action="/signup" method="post">
			
			
			<div class="form-group">
				<label for="username">Username</label>
				<input id = "username" name = "username" class = "form-control" type="text" placeholder="username" required>
			</div>

			<div class="form-group">
				<label for="password">Password</label>
				<input id = "password" name = "password" class = "form-control" type="password" placeholder="password" required>
			</div>

			<div class="form-group">
				<label for="confirmPassword">Confirm Password</label>
				<input id = "password2" name = "password2" class = "form-control" type="password" placeholder="password" required>
			</div>

			<div id="error" name ="error"></div>
			
			<button type="submit" class="btn btn-lg btn-primary btn-block">Create User</button>
			<button onclick="location.href='/login.jsp'" type="button" class="btn btn-lg btn-secondary btn-block">Cancel</button>
		</form>
		</div>
	</body>
</html>
