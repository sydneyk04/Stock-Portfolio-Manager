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
		<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-center">
	      	<a id="banner-content" class="navbar-brand" style="color:white" href="/home.jsp">
	      		USC 310 Stock Portfolio Management
	      	</a>
	    </nav>
	    <div id="main-content" class = "mainContent">
	     	
	     	<h1 class="ml-5"><%=session.getAttribute("stockName")%> (<%=session.getAttribute("stockCode")%>)</h1>
	     	<h2 class="ml-5">$<%=session.getAttribute("stockPrice")%></h2>	
	     	<div class="text-center">
	     		<form name="getdata" action="/stockperformance" method="post">
	     		<div class="btn-group" role="group" aria-label="Basic example">
				  <input type="submit"class="btn btn-secondary" name="timePeriod" value="1D"/>
				  <input type="submit"class="btn btn-secondary" name="timePeriod" value="1W"/>
				  <input type="submit"class="btn btn-secondary" name="timePeriod" value="1M"/>
				  <input type="submit"class="btn btn-secondary" name="timePeriod" value="1Y"/>
				  <input type="submit"class="btn btn-secondary" name="timePeriod" value="5Y"/>
				</div>
				</form>
	     		<div id="chartContainer"></div>
	     	</div>
			<script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
			<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
	    </div>
	</body>
</html>