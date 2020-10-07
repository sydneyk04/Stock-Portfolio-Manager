<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>notfound</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
		<link href="https://fonts.googleapis.com/css2?family=Raleway:wght@300&display=swap" rel="stylesheet">
		<link rel="stylesheet" href="notfound.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
		
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
  		<script src="//code.jquery.com/jquery-1.12.4.js"></script>
  		<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>


	</head>
	<body>
		<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-center">
			<div id="banner-content" class="navbar-brand">
				<a href="home.jsp" style="color:white; text-decoration:none">
				  USC 310 Stock Portfolio Management
				</a>
	    	</div>
		</nav>
		
		<div class="container-fluid mt-3">
			<div class="row justify-content-center">
				
					<div id="datepicker"></div>
					
					<div class="ml-3">
						<span style="display:inline-block; vertical-align:middle">
							<h2>Your Portfolio Value:</h2>
							<h3 id="portfolioValue">	 
							</h3>
						</span>
					</div>
			</div>
		</div>

		<script type="text/javascript">
			var dateToday = new Date();
			$('#datepicker').datepicker({
  			language: "en", 
			  todayHighlight: true,
			  minDate: dateToday,
			  onSelect: function() {
				  $('#portfolioValue').text("1000");
			  }
			});
		</script>
	</body>

</html>