<%@ page import="csci310.*" %>
<%@ page import ="java.util.ArrayList"%>
<%@ page import ="java.util.List"%>
<%@ page import ="java.util.Calendar"%>
<%@ page import ="java.util.GregorianCalendar"%>

<!DOCTYPE html>
<%
	// Disable Caching
	response.setHeader("Cache-Control", "no-cache, no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);

	if (session.getAttribute("username") == null) {
		session.setAttribute("from", null);
		session.setAttribute("now", null);
		session.setAttribute("chart", null);
		session.setAttribute("invalid_error", null);
		session.setAttribute("failedAdd", null);
		session.setAttribute("portfolioVal", null);
		session.setAttribute("portfolioPercentage", null);
	
		response.sendRedirect("../login.jsp");		
	}

	Calendar from = (Calendar) session.getAttribute("from");
	Calendar now = (Calendar) session.getAttribute("now");
	String zoomInFrom = (String) session.getAttribute("zoomInFrom");
	String zoomInNow = (String) session.getAttribute("zoomInNow");
	String zoomOutFrom = (String) session.getAttribute("zoomOutFrom");
	String zoomOutNow = (String) session.getAttribute("zoomOutNow");
	String zoomError = (String) session.getAttribute("zoomError");
	String chart = (String) session.getAttribute("chart");
	String username = (String) session.getAttribute("username");
	String invalid_error = (String) session.getAttribute("invalid_error");
	String failedAdd = (String) session.getAttribute("failedAdd");
	String portfolioVal = (String) session.getAttribute("portfolioVal");
	String portfolioPercentage = (String) session.getAttribute("portfolioPercentage");
	List<ArrayList> view = (List<ArrayList>) session.getAttribute("view");
	List<ArrayList> myStocks = (List<ArrayList>) session.getAttribute("myStocks");

%>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="icon" href=https://www.daytrading.com/favicon32x32.png type="image/ico" />

    <title>Dashboard</title>

	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	<link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="../styles/home.css">

	<link href="https://fonts.googleapis.com/css2?family=Raleway:wght@300&display=swap" rel="stylesheet">
    <!-- Bootstrap -->
    <link href="../vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="../vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- NProgress -->
    <link href="../vendors/nprogress/nprogress.css" rel="stylesheet">
    <!-- iCheck -->
    <link href="../vendors/iCheck/skins/flat/green.css" rel="stylesheet">

    <!-- bootstrap-progressbar -->
    <link href="../vendors/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet">
    <!-- JQVMap -->
    <link href="../vendors/jqvmap/dist/jqvmap.min.css" rel="stylesheet"/>
    <!-- bootstrap-daterangepicker -->
    <link href="../vendors/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="../build/css/custom.min.css" rel="stylesheet">
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>

  	<!-- Insert these scripts at the bottom of the HTML, but before you use any Firebase services -->

  	<!-- Firebase App (the core Firebase SDK) is always required and must be listed first -->
  	<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-app.js"></script>

  	<!-- If you enabled Analytics in your project, add the Firebase SDK for Analytics -->
  	<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-analytics.js"></script>

  	<!-- Add Firebase products that you want to use -->
  	<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-auth.js"></script>
  	<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-firestore.js"></script>
		<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-database.js"></script>

		<!-- bootstrap-daterangepicker -->
    <script src="../vendors/moment/min/moment.min.js"></script>
    <script src="../vendors/bootstrap-daterangepicker/daterangepicker.js"></script>

	<script>
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
  	</script>

	<script>
		/*
	 	 * App security: Back button pressed - prevent user from going back to dashboard afterwards
	 	 */
		if (window.performance && window.performance.navigation.type == window.performance.navigation.TYPE_BACK_FORWARD) {
	    	window.location.replace("../login.jsp");
		}

		$(document).ready(function() {
			/*
			 * App security: Auto-logout after 2 min of inactivity
			 */
			$('body').bind('click mousemove keypress scroll resize', function() {
           		lastActiveTime = new Date().getTime();
           	});

           	setInterval(checkIdleTime, 1000); // 1 sec

           	function checkIdleTime() {
                var diff = new Date().getTime() - lastActiveTime;
                if (diff > 120000) {
                 window.location.href ="../login.jsp"
                }
                else {
                    $.ajax({url: 'index.jsp', error: function(data, status, xhr){
                        alert("Unable to refresh session on server: "+xhr);
                        window.location.reload();}
                    });
                }
           	}

			function resizeTopNav() {
				$('#top_nav').each(function(){
				    var inner = $(this).find('nav');
				    $(this).height(inner.outerHeight(true));
				});
			}

			/*
			 * Window resize: UI changes
			 */
			$(window).resize(function() {
			    if(this.resizeTO) clearTimeout(this.resizeTO);
			    this.resizeTO = setTimeout(function() {
			        $(this).trigger('resizeEnd');
			    }, 500);
			});

			$(window).bind('resizeEnd', function() {
				resizeTopNav();
			});

			/*
			 * App security: Back button pressed
			 */
			$(window).bind("pageshow", function(event) {
			    if (event.originalEvent.persisted) {
			        Alert("User clicked on back button!");
			    }
			});
		});

	</script>
		<script src="../production/js/csv/importCSV.js"></script>
	</head>

  <body class="nav-md">

    <div class="container body">
      <div class="main_container">
        <!-- top navigation -->
         <header id="top_nav" style="height:60px; background:#787878;">
  			<nav id="banner" class="navbar navbar-dark bg-secondary justify-content-left">
		      	<div id="banner-content" class="navbar-brand" style="color:white;font-size:45px;font-family: 'Raleway', sans-serif;">
		      		<a href="index.jsp" style="text-decoration: none; color:white;" >
				    	USC CS 310 Stock Portfolio Management
					</a>
		      	</div>
		      	<div>

					<form name="formname" action="/dashboard" method="POST">
						<input type="hidden" name="action" value="logout">
						<button id="logout-button" type="submit" class="btn btn-primary btn-md justify-content-start">
							Logout <i class="icon-signout"></i>
						</button>
					</form>
		      	</div>
	   		</nav>
	 	 </header>


          <div class="row">
          	<div class="col-md-12 col-sm-12 bg-white">
               	<div class="portfolio_value" id="portfolio-value-today">
               		<h3>Portfolio Value Today: 
               			<%if(portfolioVal != null) {%> 
               				<%if (portfolioVal.startsWith(".")) {
               					portfolioVal = "0." + portfolioVal.substring(1);
               				}%>
               				$<%=portfolioVal%>
               			<%} else {%>
               				$0.00
               			<%}%>
               		</h3>
                </div>
            </div>
            <div class="col-md-12 col-sm-12 bg-white">
                  <div class="x_title">
                    <div class="portfolio_percentage" id="portfolio-percentage-change">
	                    <%if (portfolioPercentage == null || portfolioPercentage == "0.00" || portfolioPercentage == "0") {%> 
	                    	<h4><i id="percentChangeArrow"></i>0.00%</h4>
	                    <%} else if (portfolioPercentage.contains("-")) {
	                    	if (portfolioPercentage.contains("-.")) { portfolioPercentage = "-0." + portfolioPercentage.substring(2); }%>
	                    	<h4 style="color: red;"><i id="percentChangeArrow" class="glyphicon glyphicon-arrow-down"></i> <%=portfolioPercentage%>%</h4>
	                    <%} else {
	                    	if (portfolioPercentage.charAt(0) == '.') { portfolioPercentage = "0." + portfolioPercentage.substring(1); }%>
	                    	<h4 style="color: green;"><i id="percentChangeArrow" class="glyphicon glyphicon-arrow-up"></i> <%=portfolioVal%>%</h4>
	                    <%}%>
                    </div>
                    <div style="margin: 0px; padding: 0px;">
	                    <form name="formname" id="toggleSP" style=" display:inline-block;"  action="/dashboard" method="POST">
							<input type="hidden" name="action" value="toggleSP">
							<button style="text-align:left;display:inline;" type="submit" id="displayButton" class="btn btn-info btn-md">Toggle S&P</button>
		  				</form>
		  				 <form name="formname"" id="zoomInBtn" style=" display:inline-block;" action="/dashboard" method="POST">
							<input type="hidden" name="action" value="zoom">
							<input type="hidden" id="rangeFrom" name="from" value="<%=zoomInFrom%>">
							<input type="hidden" id="rangeTo" name="to" value="<%=zoomInNow%>">
							<button style="text-align:left;display:inline;" type="submit" id="displayButton" class="btn btn-info btn-md">Zoom In</button>
		  				</form>
		  				<form name="formname"" id="zoomOutBtn" style=" display:inline-block;" action="/dashboard" method="POST">
							<input type="hidden" name="action" value="zoom">
							<input type="hidden" id="rangeFrom" name="from" value="<%=zoomOutFrom%>">
							<input type="hidden" id="rangeTo" name="to" value="<%=zoomOutNow%>">
							<button style="text-align:left;display:inline;" type="submit" id="displayButton" class="btn btn-info btn-md">Zoom Out</button>
		  				</form>
	  				</div>
	  				<strong id="zoom_error" style="color:red; margin-left: 20%;"><%if(zoomError != null){ %> <%= zoomError%> <% } %></strong>  
                    <div class="clearfix"></div>
                  </div>
            </div>
            <div class="col-md-12 col-sm-12 ">
              <div class="dashboard_graph">

                <div class="row x_title">
                  <div class="col-md-6">
                    <h3>Your Stock Portfolio Performance</h3>
                  </div>
                  <div class="col-md-6">
                  </div>
                </div>

                <div class="col-md-9 col-sm-9 ">
                	<!--   <div id="chart_plot_01" class="demo-placeholder"></div> -->
                  <!-- <canvas id="chartContainer" width="1000" height="400"></canvas> -->
                  <div id="chartContainer" style="width: 1000px; height: 400px" ></div>
                  <%-- <script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script> --%>
				  <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
                  <%= chart%>
              	
					<div class="col-md-3 col-sm-6" id="performanceRangePicker">
						<p>Choose a range for display</p>
						<form id="performanceRangeForm" action="/dashboard" method="post">
							<input type="hidden" name="action" value="changeTimePeriod">
							<input type="hidden" id="rangeFrom" name="from" value="">
							<input type="hidden" id="rangeTo" name="to" value="">

							<div class="input-datarange input-group date">
								<input type="text" class="input-sm form-control" id="datepicker" />
							</div>
							<button type="submit" class="btn btn-primary btn-md" name="button">Confirm</button>
						</form>
					</div>

					<script type="text/javascript">
						$('#datepicker').daterangepicker({
							startDate: moment().subtract(1, 'year'),
							endDate: moment(),
							maxDate: moment(),
							ranges: {
								'Last Week': [moment().subtract(6, 'days'), moment()],
								'Last Month': [moment().subtract(29, 'days'), moment()],
								'Last 3 Months': [moment().subtract(3, 'month'), moment()],
								'Last Year': [moment().subtract(1, 'year'), moment()]
							}
						});

						$('#datepicker').on('apply.daterangepicker', function(ev, picker) {
							$('input[name=from]').val(picker.startDate.format('YYYY-MM-DD'));
							$('input[name=to]').val(picker.endDate.format('YYYY-MM-DD'));
						})
				</script>

				</div>

                <div class="clearfix"></div>
              </div>
            </div>

          </div>
          <br />

          <div class="row">

		 	<!-- Start to do list -->
            <div class="col-md-4 col-sm-4 ">
              <div class="x_panel">
                <div class="x_title">
                  <h2>Manage Portfolio <small>+ / - stocks</small></h2>
                  <div class="clearfix"></div>
                </div>
                <div class="x_content">
                <!-- Button trigger modal -->
					<button type="button"  style="background:lightgrey; border:none; border-radius:5px; color:#73879C;" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">Add by CSV</button>

					<!-- Modal -->
					<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
					  <div class="modal-dialog" role="document">
					    <div class="modal-content">
					      <div class="modal-header">
					        <h5 class="modal-title" id="exampleModalLabel">Upload a CSV File</h5>
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
					          <span aria-hidden="true">&times;</span>
					        </button>
					      </div>
						  <form class="" id="csvAddForm" action="/dashboard" method="post">
							 <div class="modal-body">
					      	 <a href="exampleStockCSV.csv" download="example">
					     	 <button type="button" id="example-csv-button" style="background: darkgrey;" class="btn btn-primary">Download Example CSV</button>
					     	 </a>

						      <div id="dvImportSegments" class="fileupload">
								<fieldset>
									<legend>Upload your CSV file</legend>
									<input type="file" name="FileUpload" id="txtFileUpload" accept=".csv" />
								</fieldset>
							 </div>
					        </div>
					      <div class="modal-footer">
						     <button type="button" id="cancel-csv-button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
							 <input type="hidden" name="action" value="addCSV">
							 <button type="submit" id="upload-file-button" class="btn btn-primary" data-dismiss="modal" id="csvAddButton">Upload File</button>
						  </div>
						</form>
					    </div>
					  </div>
					</div>

					<%-- submit csv form --%>
					<script type="text/javascript">
						var removeForm = document.getElementById("csvAddForm");
						document.getElementById("csvAddButton").addEventListener("click", function() {
							removeForm.submit();
						});
					</script>

                  <div class="">
                    <ul id="stock_list" class="to_do">
                      <%if(myStocks!=null){for(int i=1; i<myStocks.size(); i++){ %>
	                      <li id="li-<%=myStocks.get(i).get(0) %>" class="d-flex">
	                          <div style="display:inline; float: left; width: 15%;">
	                            <button type="button" id="manage-portfolio-removeStockButton-<%=myStocks.get(i).get(0)%>" style="background:lightgrey; border:none; border-radius:5px; color:white;" class="flat" data-toggle="modal" data-target="#removeStockModal-<%=myStocks.get(i).get(0)%>">X</button>
	                            <!-- Modal for Remove Stock -->
	                            <div class="modal fade" id="removeStockModal-<%=myStocks.get(i).get(0)%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	                              <div class="modal-dialog" role="document">
	                                <div class="modal-content">
	                                  <div class="modal-header">
	                                    <h5 class="modal-title" id="exampleModalLabel">Are you sure you want to remove this stock?</h5>
	                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                                      <span aria-hidden="true">&times;</span>
	                                    </button>
	                                  </div>
	                                  <div class="modal-footer">
	                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>

										<form class="" id="removeStock-<%=myStocks.get(i).get(0)%>" action="/dashboard" method="POST">
											<input type="hidden" name="action" value="removeStock">
											<input type="hidden" name="removeStockTicker" value="<%=myStocks.get(i).get(0) %>">
											<button type="submit" class="btn btn-primary deletestock" data-dismiss="modal" id="stockremovebutton<%=myStocks.get(i).get(0)%>">Remove Stock</button>
										</form>

										<%-- remove stock form --%>
										<script type="text/javascript">

											var form = document.getElementById("removeStock-<%=myStocks.get(i).get(0)%>");
											console.log(form);
											document.getElementById("stockremovebutton<%=myStocks.get(i).get(0)%>").addEventListener("click", function() {
												form.submit();
												console.log("called remove");
											});
										</script>
	                                  </div>
	                                </div>
	                              </div>
	                            </div>
	                          </div>
	                          <div style="float: left; width: 85%;">
	                            <div style="display:inline;">
	                              <p style="text-align:left;display:inline;">   Ticker: </p><p style="text-align:left; display:inline; font-weight:bold;"><%=myStocks.get(i).get(0) %></p>
	                              <br>
	                            </div>
	                            <div style="display:inline;">
	                              <p style="text-align:left;display:inline;">   # Shares: </p><p style="text-align:left; display:inline; font-weight:bold;"><%=myStocks.get(i).get(2) %></p>
	                              <br>
	                            </div>
	                            <div style="display:inline;">
	                              <p style="text-align:left;display:inline;">   Purchase Date: </p><p style="text-align:left; display:inline; font-weight:bold;"><%=myStocks.get(i).get(3) %></p>
	                              <br>
	                            </div>
	                            <div style="display:inline;">
	                              <p style="text-align:left;display:inline;">   Sell Date: </p><p style="text-align:left; display:inline; font-weight:bold;"><%=myStocks.get(i).get(4) %></p>
	                              <br>
	                            </div>
	                             <div style="display:inline;">
	                              <p style="text-align:left;display:inline;">   Calculate in Portfolio: </p><p style="text-align:left; display:inline; font-weight:bold;">
									<form name="formname" id="stockDisplay-<%=myStocks.get(i).get(0)%>" action="/dashboard" method="POST">
										<input type="hidden" name="action" value="portfolioState">
										<input type="hidden" name="ticker" value="<%=myStocks.get(i).get(0) %>">
		 								<button style="text-align:left;display:inline;" type="submit" id="displayButton-<%=myStocks.get(i).get(0)%>" class="btn btn-light btn-sm"><%=myStocks.get(i).get(5) %></button>
       								</form>
									<%-- toggle stock display form --%>
									<script type="text/javascript">
										var toggleForm = document.getElementById("stockDisplay-<%=myStocks.get(i).get(0)%>");
										document.getElementById("displayButton-<%=myStocks.get(i).get(0)%>").addEventListener("click", function() {
											toggleForm.submit();
										});
									</script>
	                             	</p>
	                            </div>
	                        </div>


	                      </li>
					   <% } }%>
					<strong id="login_error" style="color:red; margin-left: 20%;"><%if(failedAdd != null){ %> <%= failedAdd%> <% } %></strong>
                    </ul>

										<%-- select/deselect all buttons --%>
										<div class="d-flex">
											<form class="" action="/dashboard" method="post">
												<input type="hidden" name="action" value="selectViewAll">
												<button class="btn btn-secondary" type="submit" name="">Select All</button>
											</form>
										</div>

										<div class="d-flex">
											<form class="d-flex" action="/dashboard" method="post">
												<input type="hidden" name="action" value="deselectViewAll">
												<button class="btn btn-secondary" type="submit" name="">Deselect All</button>
											</form>
										</div>

                    <!-- Button trigger modal --><br><br>
                    <div class="addstockbutton">
                    <strong id="login_error" style="color:red"><%if(failedAdd != null){ %> <%= failedAdd%> <% } %></strong>

                    <br><button id="manage-portfolio-add-stock-button" type="button" class="btn btn-primary" data-toggle="modal" data-target="#addStockModal">Add Stock</button>

                   


                    <!-- Modal For Add Stock-->
                    <div class="modal fade" id="addStockModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                      <div class="modal-dialog" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Add your new stock</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                          </div>
                          
                          
                           <form name="formname" action="/dashboard" method="POST">
                          	<div class="modal-body">
                              <div class="inputrow" style="width: 100%;">
	                            <input type="hidden" name="action" value="addStock">
	                              <div style="float: left; width: 45%; overflow: scroll; margin-right:2.5%; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;">Ticker*</p>
	                                  <input class="stockinput" type="text" id="ticker" name="ticker" required>
	                                </div>
	                              </div>
	                              <div style="float: left; width: 45%; overflow: scroll; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;"># Shares*</p>
	                                  <input class="stockinput" type="text" id="shares" name="numOfShares" required>
	                                </div>
	                              </div>
	                            </div>
	                            <br>
	                            <div class="inputrow" style="width: 100%;">
	                              <div style="float: left; width: 45%; overflow: scroll; margin-right:2.5%; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;">Date Purchased*</p>
	                                  <input class="stockinput" type="date" id="datePurchased" name="datePurchased" required>
	                                </div>
	                              </div>
	                              <div style="float: left; width: 45%; overflow: scroll; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;">Date Sold</p>
	                                  <input class="stockinput" type="date" id="dateSold" name="dateSold">
	                                </div>
	                              </div>
	                              <div style="float: left; width: 95%; overflow: scroll; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;">Note: date must be in mm/dd/YYYY</p>
	                                </div>
	                              </div>
	                            </div>
	                          </div>
	                          <div class="modal-footer">
	                          <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
							  <button type="submit" class="btn btn-primary">Add Stock</button>
							</div>
                          </form>
                          
							<%-- add stock form --%>
							<script type="text/javascript">
								var addForm = document.getElementById("addStockForm");
								document.getElementById("stockaddbutton").addEventListener("click", function() {
									addForm.submit();
								});
							</script>
                          </div>
                        </div>
                      </div>
                    </div>



					 <style>
                      .stockinput{
                        border: none;
                        background-color: Gainsboro;
                        padding-right:10px;
                        /* border-radius: 5px; */
                      }
                      .inputrow{
                        content: "";
                        display: table;
                        clear: both;
                      }
                      .addstockbutton{
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        border: none;
                      }
                    </style>
                 


                  <script>
                    // $(this).delay(3000).queue(function(){
                    //   $(function() {
                    //     $( "#stock_list" ).append("<li><p><div class=\"icheckbox_flat-green checked\" style=\"position: relative;\"><input type=\"checkbox\" class=\"flat\" style=\"position: absolute; opacity: 0;\"><ins class=\"iCheck-helper\" style=\"position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;\"></ins></div> Schedule meeting with new client </p></li>");
                    //     $( "#stock_list" ).append('<li><div style="display:inline;"><input type="checkbox" class="flat"><br></div><div style="display:inline;"><p style="text-align:left;display:inline;">   Exchange: </p><p style="text-align:left;display:inline;">Exchange</p><br> </div><div style="display:inline;"> <p style="text-align:left;display:inline;">   Ticker: </p><p style="text-align:left;display:inline;">Exchange</p><br> </div><div style="display:inline;"><p style="text-align:left;display:inline;">   # Shares: </p><p style="text-align:left;display:inline;">Exchange</p><br></div></li>');
                    //   });
                    // });
                    $(document).ready(function(){
                      function updateDoughnut(){
                        // if ("undefined" != typeof Chart && (console.log("init_chart_doughnut"),
                        // $(".canvasDoughnut"))) {
                        //     var a = {
                        //         type: "doughnut",
                        //         tooltipFillColor: "rgba(51, 51, 51, 0.55)",
                        //         data: {
                        //             labels: ["Symbian", "Blackberry", "Other", "Android", "IOS"],
                        //             datasets: [{
                        //                 data: [50, 10, 10, 10, 20],
                        //                 backgroundColor: ["#BDC3C7", "#9B59B6", "#E74C3C", "#26B99A", "#3498DB"],
                        //                 hoverBackgroundColor: ["#CFD4D8", "#B370CF", "#E95E4F", "#36CAAB", "#49A9EA"]
                        //             }]
                        //         },
                        //         options: {
                        //             legend: !1,
                        //             maintainAspectRatio: !0,
                        //             responsive: !1
                        //         }
                        //     };
                        //     $(".canvasDoughnut").each(function() {
                        //         var e = $(this);
                        //         new Chart(e,a)
                        //
                        //     })
                        //}
                        var els = document.getElementsByClassName("canvasDoughnut");
                        for (var i = 0; i < els.length; i++) {
                          els[i].style.width = "280";
                          els[i].style.height = "280";
                        }
                      }

                      function getCookie(name) {
                  		// Split cookie string and get all individual name=value pairs in an array
                   	    var cookieArr = document.cookie.split(";");

                   	    // Loop through the array elements
                   	    for(var i = 0; i < cookieArr.length; i++) {
                   	        var cookiePair = cookieArr[i].split("=");

                   	        /* Removing whitespace at the beginning of the cookie name
                   	        and compare it with the given string */
                   	        if(name == cookiePair[0].trim()) {
                   	            // Decode the cookie value and return
                   	            return decodeURIComponent(cookiePair[1]);
                   	        }
                   	    }

                   	    // Return null if not found
                   	    return null;
                   	  }

                      function getTotalPortfolioValue() {
                   	    // Get cookie using our custom function
                   	    var portfolioValue = getCookie("portfolioValue");
                   	 	console.log("Total portfolio value: " + portfolioValue);

                   	    if (portfolioValue == null) {
                   	    	document.getElementById("totalPortfolio").innerHTML = "$0.00";
                   	    } else {
                   	    	document.getElementById("totalPortfolio").innerHTML = "$" + portfolioValue;
                   	    }
                      }
                      function updatePorfolio() {
                        var apiLink = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=60096f09efmsh63c9a7515124324p1b4c57jsna3f56df4cc32";
                        console.log(apiLink)
                        axios.get(apiLink)
                          .then(function (response) {
                            var responseData = response.data;
                            // responseData = responseData.split("0").join("");
                            // console.log(responseData);
                            // var responseJSON = JSON.parse(responseData);
                            var responseJSON = JSON.parse(JSON.stringify(responseData))
                            var valueJson = responseJSON["Time Series (Daily)"];
                            console.log(valueJson);
                            valueJson = valueJson["2020-09-23"];
                            console.log(valueJson);
                            valueJson = valueJson["4. close"];
                            var dollarValue = valueJson;
                            //document.getElementById("totalPortfolio").innerHTML = "$" + dollarValue;
                            //getTotalPortfolioValue();
                          })
                          .catch(function (error) {
                            console.log(error);
                          });

                      }


                      //updatePorfolio();
                      setTimeout(
                        function()
                        {
                          updateDoughnut();
                        }, 5000);


                      // This deletes buttons when they click out of the stock
                      // var els = document.getElementsByClassName("deletestock");
                      // for (var i = 0; i < els.length; i++) {
                      //     els[i].addEventListener('click', function (e) {
                      //         $('#removeStockModal').modal('hide');
                      //         console.log("delete stock")
                      //         e.preventDefault();
                      //         e.target.closest('li').remove();
                      //     });
                      // }
                    });
                    // $( document ).ready(function() {
                    //   $( "#stock_list" ).append("");
                    //   console.log( "ready!" );
                    // });
                  </script>
                </div>
              </div>
            </div>
            </div>
            <!-- End to do list -->

			  <div class="col-md-5 col-sm-5s  bg-white" style="">
			  		<!-- <form name="formname" action="/dashboard" method="POST">
						<input type="hidden" name="action" value="viewstock">
						  <button type="button" class="btn btn-primary" id="spytoggle" style="color:#007bff;background:#fff;">
						  	Click this to go to stock performance graph
						  </button>
					</form> -->
					<h2>View stocks</h2>
					  <strong id="login_error" style="color:red"><%if(invalid_error != null){ %> <%= invalid_error%> <% } %></strong>
					<!-- Button trigger modal -->
                    <br><button type="button" data-toggle="modal" data-target="#viewStockModal">View Stock</button>
                    <br><br>

					<%if(view!=null){for(int i=0; i<view.size(); i++) {%>
						<div style="float: left; width: 85%;">
                           <div style="display:inline;">
                             <p style="text-align:left;display:inline;">   Ticker: </p><p style="text-align:left; display:inline; font-weight:bold;"><%=view.get(i).get(0) %></p>
                             <br><p style="text-align:left;display:inline;">   # Shares: </p><p style="text-align:left; display:inline; font-weight:bold;"><%=view.get(i).get(2) %></p>
                             <br><p style="text-align:left;display:inline;">   Purchase Date: </p><p style="text-align:left; display:inline; font-weight:bold;"><%=view.get(i).get(3) %></p>
                             <br><p style="text-align:left;display:inline;">   Sell Date: </p><p style="text-align:left; display:inline; font-weight:bold;"><%=view.get(i).get(4) %></p>
							 <form name="formname" action="/dashboard" method="POST">
	                            <input type="hidden" name="action" value="showViewStock">
	                            <input type="hidden" name="ticker" value="<%=view.get(i).get(0) %>">
	                            <button style="text-align:left; display:inline; font-weight:bold;">Toggle on Graph</button>
	                         </form>
                             <form name="formname" action="/dashboard" method="POST">
	                            <input type="hidden" name="action" value="removeViewStock">
	                            <input type="hidden" name="removeTicker" value="<%=view.get(i).get(0) %>">
	                            <button style="text-align:left; display:inline; font-weight:bold;">Remove</button>
	                         </form>
	                         <form name="formname" action="/dashboard" method="POST">
	                         	<input type="hidden" name="ticker" value=<%=view.get(i).get(0) %>>
	                         	<input type="hidden" name="numOfShares" value=<%=view.get(i).get(2) %>>
	                         	<input type="hidden" name="datePurchased" value=<%=view.get(i).get(3) %>>
	                         	<input type="hidden" name="dateSold" value=<%=view.get(i).get(4) %>>
	                            <input type="hidden" name="action" value="addStock">
	                       	 	<button type="submit" class="addstockbutton" style="text-align:left; display:inline; font-weight:bold;">Add to Portfolio</button>
                             </form>
                             <br>
                           </div>
	                    </div>
					<%}}%>

					 <!-- Modal To Add Stock to Graph but Not Portfolio-->
                    <div class="modal fade" id="viewStockModal" tabindex="-1" role="dialog"aria-hidden="true">
                      <div class="modal-dialog" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h5 class="modal-title">Add a Stock to your Graph</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                          </div>
                          <form name="formname" action="/dashboard" method="POST">
                          	<div class="modal-body">
                              <div class="inputrow" style="width: 100%;">
	                            <input type="hidden" name="action" value="viewStock">
	                              <div style="float: left; width: 45%; overflow: scroll; margin-right:2.5%; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;">Ticker*</p>
	                                  <input class="stockinput" type="text" id="ticker" name="ticker" required>
	                                </div>
	                              </div>
	                              <div style="float: left; width: 45%; overflow: scroll; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;"># Shares*</p>
	                                  <input class="stockinput" type="text" id="shares" name="numOfShares" required>
	                                </div>
	                              </div>
	                            </div>
	                            <br>
	                            <div class="inputrow" style="width: 100%;">
	                              <div style="float: left; width: 45%; overflow: scroll; margin-right:2.5%; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;">Date Purchased*</p>
	                                  <input class="stockinput" type="date" id="datePurchased" name="datePurchased" required>
	                                </div>
	                              </div>
	                              <div style="float: left; width: 45%; overflow: scroll; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;">Date Sold</p>
	                                  <input class="stockinput" type="date" id="dateSold" name="dateSold">
	                                </div>
	                              </div>
	                              <div style="float: left; width: 95%; overflow: scroll; margin-left:2.5%; display:table-cell;">
	                                <div style="margin-right:5px;">
	                                  <p style="text-align:center;">Note: date must be in mm/dd/YYYY</p>
	                                </div>
	                              </div>
	                            </div>
	                          </div>
	                          <div class="modal-footer">
	                          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
							  <button type="submit" class="btn btn-primary">View Stock</button>
							</div>
                          </form>
                        </div>
                      </div>
                    </div>

				</div>
					<!-- <form name="formname" action="/dashboard" method="POST">
						<input type="text" name="ticker" placeholder="Enter a stock you want to view">
						<input type="hidden" name="action" value="viewStock">
						<button type="submit" class="btn btn-primary btn-md justify-content-start">
							submit
						</button>
						<div id="login-error-container">

           				</div>
					</form> -->
                 </div>

                   <!--
                    <div class="modal fade" id="stockPerformanceModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                      <div class="modal-dialog" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Stock Performance Graph</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                          </div>
                          <div class="modal-body">

                          </div>
                           <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                          </div>
                      </div>
                    </div>

 -->
          </div>



        </div>

        <!-- /page content -->

        <!-- footer content -->
        <!-- <footer>
          <div class="pull-right">
            Gentelella - Bootstrap Admin Template by <a href="https://colorlib.com">Colorlib</a>
          </div>
          <div class="clearfix"></div>
        </footer> -->
        <!-- /footer content -->

    <!-- jQuery -->
    <script src="../vendors/jquery/dist/jquery.min.js"></script>
    <!-- Bootstrap -->
    <script src="../vendors/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
    <!-- FastClick -->
    <script src="../vendors/fastclick/lib/fastclick.js"></script>
    <!-- NProgress -->
    <script src="../vendors/nprogress/nprogress.js"></script>
    <!-- Chart.js -->
    <script src="../vendors/Chart.js/dist/Chart.min.js"></script>
    <!-- gauge.js -->
    <script src="../vendors/gauge.js/dist/gauge.min.js"></script>
    <!-- bootstrap-progressbar -->
    <script src="../vendors/bootstrap-progressbar/bootstrap-progressbar.min.js"></script>
    <!-- iCheck -->
    <script src="../vendors/iCheck/icheck.min.js"></script>
    <!-- Skycons -->
    <script src="../vendors/skycons/skycons.js"></script>
    <!-- Flot -->
    <script src="../vendors/Flot/jquery.flot.js"></script>
    <script src="../vendors/Flot/jquery.flot.pie.js"></script>
    <script src="../vendors/Flot/jquery.flot.time.js"></script>
    <script src="../vendors/Flot/jquery.flot.stack.js"></script>
    <script src="../vendors/Flot/jquery.flot.resize.js"></script>
    <!-- Flot plugins -->
    <script src="../vendors/flot.orderbars/js/jquery.flot.orderBars.js"></script>
    <script src="../vendors/flot-spline/js/jquery.flot.spline.min.js"></script>
    <script src="../vendors/flot.curvedlines/curvedLines.js"></script>
    <!-- DateJS -->
    <script src="../vendors/DateJS/build/date.js"></script>
    <!-- JQVMap -->
    <script src="../vendors/jqvmap/dist/jquery.vmap.js"></script>
    <script src="../vendors/jqvmap/dist/maps/jquery.vmap.world.js"></script>
    <script src="../vendors/jqvmap/examples/js/jquery.vmap.sampledata.js"></script>


    <!-- Custom Theme Scripts -->
    <script src="../build/js/custom.min.js"></script>

  </body>
</html>
