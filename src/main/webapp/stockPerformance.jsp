<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>	
<!DOCTYPE html>
<%
	//Disable Caching
	response.setHeader("Cache-Control", "no-cache, no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);

	String chart = (String) session.getAttribute("chart");
%>
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
	     	
	     	<h1 class="ml-5"></h1>
	     	<div class="text-center">
	     		<form name="getdata" action="/stockperformance" method="post">
	     		<div class="btn-group" role="group" aria-label="Basic example">
	     		<div class="input-group date" data-provide="datepicker">
				    <input type="text" class="form-control">
				    <div class="input-group-addon">
				        <span class="glyphicon glyphicon-th"></span>
				    </div>
				</div>
				  <input type="submit" id="1-day-btn" class="btn btn-secondary" name="timePeriod" value="1D"/>
				  <input type="submit" id="1-week-btn" class="btn btn-secondary" name="timePeriod" value="1W"/>
				  <input type="submit" id="1-month-btn" class="btn btn-secondary" name="timePeriod" value="1M"/>
				  <input type="submit" id="1-year-btn" class="btn btn-secondary" name="timePeriod" value="1Y"/>
				  <input type="submit" id="5-year-btn" class="btn btn-secondary" name="timePeriod" value="5Y"/>
				</div>
				</form>
	     		<div id="chartContainer">
	     			<%= chart%>
	     		</div>
	     	</div>
			<script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
			<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
	    </div>
	</body>
</html>
