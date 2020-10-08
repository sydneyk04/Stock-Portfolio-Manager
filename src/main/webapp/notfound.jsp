<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>notfound</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
		<link href="https://fonts.googleapis.com/css2?family=Raleway:wght@300&display=swap" rel="stylesheet">
		<link rel="stylesheet" href="notfound.css">
	</head>
	<body>
		<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-center">
			<div id="banner-content" class="navbar-brand">
				<a href="home.jsp" style="color:white; text-decoration:none">
				  USC 310 Stock Portfolio Management
				</a>
	    	</div>
		</nav>
		
		<div class="container mt-3">
			<div class="jumbotrum text-center">
				<h1>
					Sorry, we didn't found any result.
				</h1>
			</div>
			<form class="w-50 mt-3 mx-auto" action="/search" method="post">
				<div class="form-group w-100">
					<div class="active-cyan-4 mb-4">
						<input type="text" class="form-control" name="stockName" placeholder="Search" aria-label="Search">
					</div>
				</div>
			</form>
		
		</div>
	</body>

</html>