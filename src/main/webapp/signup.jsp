<html>
	<head>
		<link rel="stylesheet" href="signup.css">
		<title>Sign Up</title>
	</head>
	<body>
		<h1>create an account now!</h1>
		<form class = "container" action="/signup" method="post">
			<input id = "email" name = "email" class = "input" type="text" placeholder="email">
			<input id = "password" name = "password" class = "input" type="password" placeholder="password">
			<input id = "password2" name = "password2" class = "input" type="password" placeholder="password">
			<div id="error" name = "error"></div>
			<button class = "signup">sign up</button>
		</form>

		<!-- The core Firebase JS SDK is always required and must be listed first -->
		<!-- <script src="https://www.gstatic.com/firebasejs/7.21.0/firebase-app.js"></script>

		TODO: Add SDKs for Firebase products that you want to use
		     https://firebase.google.com/docs/web/setup#available-libraries
		<script src="https://www.gstatic.com/firebasejs/7.21.0/firebase-analytics.js"></script>

		<script>
		  // Your web app's Firebase configuration
		  // For Firebase JS SDK v7.20.0 and later, measurementId is optional
		  var firebaseConfig = {
		    apiKey: "AIzaSyD2UNIj11jvzjzhxZE_q_-J6sghVpqKc14",
		    authDomain: "stock16-e451e.firebaseapp.com",
		    databaseURL: "https://stock16-e451e.firebaseio.com",
		    projectId: "stock16-e451e",
		    storageBucket: "stock16-e451e.appspot.com",
		    messagingSenderId: "326942561287",
		    appId: "1:326942561287:web:c74346b57d023ab4b086a2",
		    measurementId: "G-FZ5DY7SBDT"
		  };
		  // Initialize Firebase
		  firebase.initializeApp(firebaseConfig);
		  firebase.analytics();
		</script> -->
	</body>
</html>
