<%@ page import="csci310.*" %>
<!DOCTYPE html>
<%
	//Disable Caching
	response.setHeader("Cache-Control", "no-cache, no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);

	String portfolio_value = (String) session.getAttribute("portfolio_value");
	if (portfolio_value == null) {
		portfolio_value = "0";
	}
%>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Home</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		<link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  		<link rel="stylesheet" href="styles/home.css">
  		<link href="https://fonts.googleapis.com/css2?family=Raleway:wght@300&display=swap" rel="stylesheet">
	</head>
	<body>
		<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-center">
	      	<div id="banner-content" class="navbar-brand" style="color:white">
	      		USC 310 Stock Portfolio Management
	      	</div>
	    </nav>
	    <nav id="navbar" class="navbar navbar-dark bg-light navbar-static-top">
			<div class="d-flex align-items-start">
				<a id="logout-button" class="btn btn-outline-primary btn-md justify-content-start" href="login.jsp" role="button">
					Log out
				</a>
			</div>
		  	<div id="search-container" class="align-self-end">
		  		<form id="search-form" action="/search" method="post">
					<div id="search-bar" class="p-1 bg-white rounded rounded-pill shadow-sm">
						<div class="input-group">
							<input id="search-input" type="search" placeholder="Search for stocks..." class="form-control border-0 bg-white">
							<div class="input-group-append">
	                			<button id="search-button" type="submit" class="btn btn-link text-primary">
	                				<i class="icon-search"></i>
	                			</button>
	              			</div>
              			</div>
					</div>
				</form>
			</div>
	    </nav>
	    <div id="main-content" class="mainContent" style="padding:14px">
	     	<h2><strong>Portfolio Value:</strong> <%= portfolio_value%></h2>
	     	<div id="portfolio-buttons">
	     		<a id="portfolio-performance" class="btn btn-primary" href="/portfolio" role="button">Performance</a>
	     		<a id="portfolio-prediction" class="btn btn-primary" href="/predict" role="button">Predict</a>
	     	</div>
	     	<div id="portfolio" style="margin-top:24px">
	     		<h3>Portfolio</h3>
	     		<table id="portfolio-table" width="100%" border="1">
				    <tr>
				        <th>Stock</th>
				        <th>Price</th>
				        <th>Shares</th>
				        <th>Total</th>
				    </tr>
				    <tr>
				    	<td>${stock.symbol}</td>
			            <td>${stock.price}</td>
			            <td>${stock.shares}</td>
			            <td>${stock.total}</td>
				    </tr>
<!--  				    <c:forEach items="${stockList}" var="stock" varStatus="status">
				        <tr>
				            <td>${stock.symbol}</td>
				            <td>${stock.price}</td>
				            <td>${stock.shares}</td>
				            <td>${stock.total}</td>
				        </tr>
				    </c:forEach>
-->
				</table>
	     	</div>
	    </div>
	</body>
</html>
