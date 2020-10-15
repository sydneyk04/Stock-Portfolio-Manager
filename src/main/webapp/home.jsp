<%@ page import="csci310.*" %>
<!DOCTYPE html>
<%
	//Disable Caching
	response.setHeader("Cache-Control", "no-cache, no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);

	Portfolio portfolio = (Portfolio) session.getAttribute("portfolio");
	Double portfolio_value = 0.00;
	if (portfolio != null) {
		portfolio_value = portfolio.getValue();
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
	<body style="font-family: 'Raleway', sans-serif;">
		<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-left">
	      	<div id="banner-content" class="navbar-brand" style="color:white;font-size:45px">
	      		<a href="home.jsp" style="text-decoration: none; color:white;" >
			    USC 310 Stock Portfolio Management 
			</a>
	      	</div>
	    </nav>
	    <nav id="navbar" class="navbar navbar-dark bg-light navbar-static-top">
			<div class="d-flex align-items-start">
				<form name="formname" action="/home" method="POST">
					<input type="hidden" name="action" value="logout">
					<button id="logout-button" type="submit" class="btn btn-outline-primary btn-md justify-content-start">
						Logout <i class="icon-signout"></i>
					</button>	       
				</form>
			</div>
		  	<div id="search-container" class="align-self-end">
		  		<form id="search-form" action="/search" method="post">
					<div id="search-bar" class="p-1 bg-white rounded rounded-pill shadow-sm">
						<div class="input-group">
							<input id="search-input" type="search" name="stockName" placeholder="Search for stocks..." class="form-control border-0 bg-white">
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
	     	<h2 id="portfolio-value"><strong>Portfolio Value:</strong> <%= portfolio_value%></h2>
	     	<div id="portfolio-buttons">
	     		<a id="portfolio-performance" class="btn btn-primary" href="/portfolio" role="button">Performance</a>
 	     		<a id="portfolio-prediction" class="btn btn-primary" href="/predict.jsp" role="button">Predict</a>
	     	</div>
	     	<div id="portfolio" style="margin-top:24px">
	     		<h3 id="portfolio-header"><strong>Portfolio</strong></h3>	     		
     			<%if (portfolio == null || portfolio.getStocks().isEmpty()) { %>
			    	<p id="empty-portfolio-mssg">Your portfolio is empty.</p>
			    <%} else {%>
				    <table id="portfolio-table">
				    	<tr>
					        <th style="background-color: #007bff;color: white;">Stock</th>
					        <th style="background-color: #007bff;color: white;">Price</th>
					        <th style="background-color: #007bff;color: white;">Shares</th>
					        <th style="background-color: #007bff;color: white;">Total</th>
					    </tr>
					    <%for (PortfolioStock stock : portfolio.getStocks()) { %>
					    	<tr>
					            <td><%=stock.getSymbol() %></td>
				                <td><%=stock.getPrice() %></td>
				                <td><%=stock.getShares() %></td>
				                <td><%=String.format("%.2f", stock.getTotalValue()) %></td>
					        </tr>
					    <%} %>
				    </table>
			    <%} %>
	     	</div>
	    </div>
	</body>
</html>
