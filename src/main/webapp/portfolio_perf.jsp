<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>	
<!DOCTYPE html>
<html>

<%-- graph from https://canvasjs.com/jsp-charts/json-data-api-ajax-chart/ --%>
<script type="text/javascript">
window.onload = function() { 
 
var dataPoints = [];
 
var chart = new CanvasJS.Chart("chartContainer", {
	animationEnabled: true,
	theme: "light2",
 	zoomEnabled: true,
	title: {
		text: "Portfolio Value"
	},
	axisY: {
		title: "Value in $",
		titleFontSize: 24
	},
	data: [{
		type: "line",
		yValueFormatString: "#.# $",
		xValueType: "dateTime",
		dataPoints: dataPoints
	}]
});
 
function addData(data) {
	for (var i = 0; i < data.length; i++) {
		dataPoints.push({
			x: data[i].timestamp,
			y: data[i].value
		});
	}
	chart.render();
}
 
$.getJSON("https://canvasjs.com/data/gallery/jsp/total-biomass-energy-consumption.json", addData);
 
}
</script>

	<head>
		<meta charset="ISO-8859-1">
		<title>Home</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	</head>
	<body>
		<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-center">
	      	<div id="banner-content" class="navbar-brand"  style="color:white">
	      		<a href="home.jsp" style="text-decoration: none" >	USC 310 Stock Portfolio Management </a>
	      	</div>
	    </nav>
	    <div id="main-content" class = "mainContent">
	     	<div id="chartContainer" style="height: 370px; width: 100%;"></div>
			<script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
			<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
	    </div>
	    <div id="buttons" class = "buttons">
	    	<button type="button" id="aweek" class="btn btn-outline-primary">1 Week</button>
	    	<button type="button" id="amonth" class="btn btn-outline-primary">1 Month</button>
	    	<button type="button" id="ayear" class="btn btn-outline-primary">1 Year</button>
	    </div>
	</body>
</html>