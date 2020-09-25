<html>
	<head>
		<link rel="stylesheet" href="signup.css">
		<title>Sign Up</title>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

	</head>
	<body>
		<div class="container">

		
		<h1 class="text-center">Create an account now!</h1>
		<form class = "form-signin" action="/signup" method="post">
			
			
			<div class="form-group">
				<label for="username">Username:</label>
				<input id = "username" name = "username" class = "form-control" type="text" placeholder="username" required>
			</div>

			<div class="form-group">
				<label for="password">Password:</label>
				<input id = "password" name = "password" class = "form-control" type="password" placeholder="password" required>
			</div>

			<div class="form-group">
				<label for="confirmPassword">Confirm Password:</label>
				<input id = "password2" name = "password2" class = "form-control" type="password" placeholder="password" required>
			</div>

			<div id="error" name ="error"></div>
			<button type="submit" class="btn btn-lg btn-primary btn-block">sign up</button>
		</form>
		</div>
	</body>
</html>
