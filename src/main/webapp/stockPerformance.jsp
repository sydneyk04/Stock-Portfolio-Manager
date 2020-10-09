<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>	
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Stock Perfomance</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	</head>
	<body>
		<jsp:include page="/stockperformance" />
		<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-center">
	      	<a id="banner-content" class="navbar-brand" style="color:white" href="/home.jsp">
	      		USC 310 Stock Portfolio Management
	      	</a>
	    </nav>
	    <div id="main-content" class = "mainContent">
	     	
	     	<h1 class="ml-5"><%=request.getAttribute("stockName")%> (<%=request.getAttribute("stockCode")%>)</h1>
	     	<h2 class="ml-5">$<%=request.getAttribute("stockPrice")%></h2>	
	     	<div class="text-center">
	     		<form name="getdata" action="/stockperformance" method="post">
	     		<div class="btn-group" role="group" aria-label="Basic example">
				  <input type="submit" id="1day-button" class="btn btn-secondary" name="timePeriod" value="1D"/>
				  <input type="submit" id="1week-button" class="btn btn-secondary" name="timePeriod" value="1W"/>
				  <input type="submit" id="1month-button" class="btn btn-secondary" name="timePeriod" value="1M"/>
				  <input type="submit" id="1year-button" class="btn btn-secondary" name="timePeriod" value="1Y"/>
				  <input type="submit" id="5year-button" class="btn btn-secondary" name="timePeriod" value="5Y"/>
				</div>
				</form>
	     		<div id="chartContainer"></div>
	     	</div>
			<script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
			<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
	    </div>
	</body>
</html>
