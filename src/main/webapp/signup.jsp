<html>
	<head>
		<link rel="stylesheet" href="signup.css">
		<title>Sign Up</title>
	</head>
	<body>
		<h1>create an account now!</h1>
		<form class = "container" action="/signup" method="post">
			<input id = "username" name = "username" class = "input" type="text" placeholder="username">
			<input id = "password" name = "password" class = "input" type="password" placeholder="password">
			<input id = "password2" name = "password2" class = "input" type="password" placeholder="password">
			<div id="error" name = "error"></div>
			<button class = "signup">sign up</button>
		</form>
	</body>
</html>
