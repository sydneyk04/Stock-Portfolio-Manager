<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>	
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script type="text/javascript">
			window.onload = function() { 
			 
			var dataPoints = [];
			 
			var chart = new CanvasJS.Chart("chartContainer", {
				animationEnabled: true,
				theme: "light2",
			 	zoomEnabled: true,
				axisY: {
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
		
		<title>Home</title>
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
	     		<div id="chartContainer"></div>
	     	</div>
			<script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
			<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
	    </div>
	</body>
</html>


<!-- <html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Stock Performance</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	</head>
	<body>
		<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-center">
	      	<a id="banner-content" class="navbar-brand" style="color:white" href="/home.jsp">
	      		USC 310 Stock Portfolio Management
	      	</a>
	    </nav>
	    <div id="main-content" class = "mainContent">
	     	
	    </div>
	</body>
</html>
 -->