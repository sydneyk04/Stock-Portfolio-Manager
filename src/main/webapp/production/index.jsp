<%@ page import="csci310.*" %>
<!DOCTYPE html>
<%
	// Disable Caching
	response.setHeader("Cache-Control", "no-cache, no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	
	if (session.getAttribute("username") == null) {
		response.sendRedirect("../login.jsp");
	}
	
	String chart = (String) session.getAttribute("chart");
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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

  	<!-- Insert these scripts at the bottom of the HTML, but before you use any Firebase services -->

  	<!-- Firebase App (the core Firebase SDK) is always required and must be listed first -->
  	<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-app.js"></script>

  	<!-- If you enabled Analytics in your project, add the Firebase SDK for Analytics -->
  	<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-analytics.js"></script>

  	<!-- Add Firebase products that you want to use -->
  	<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-auth.js"></script>
  	<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-firestore.js"></script>
	<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-database.js"></script>

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
    
		<script src="../production/js/csv/importCSV.js"></script>
	</head>

  <body class="nav-md">

    <div class="container body">
      <div class="main_container">
        <!-- top navigation -->
         <header id="top_nav" style="height:60px; background:#787878;">
  			<nav id="banner" class="navbar navbar-dark bg-secondary navbar-static-top justify-content-left">
		      	<div id="banner-content" class="navbar-brand" style="color:white;font-size:45px;font-family: 'Raleway', sans-serif;">
		      		<a href="index.jsp" style="text-decoration: none; color:white;" >
				    	USC CS 310 Stock Portfolio Management
					</a>
		      	</div>
		      	<div>
		      	<!--
		      	<a href="../login.jsp" style="text-decoration: none; color:white;" >
				    Logout
				 </a>
				-->
					<form name="formname" action="/dashboard" method="POST">
						<input type="hidden" name="action" value="logout">
						<button id="logout-button" type="submit" class="btn btn-primary btn-md justify-content-start">
							Logout <i class="icon-signout"></i>
						</button>
					</form>
		      	</div>
	   		</nav>
	 	 </header>
       <!--  <div class="top_nav">
          <div class="nav_menu">
              <nav class="nav navbar-nav" style="background:#2A3F54;">
              <ul class=" navbar-right">
                <li class="nav-item dropdown open" style="padding-left: 15px;">
                  <a href="javascript:;" class="user-profile dropdown-toggle" style="color:white !important;" aria-haspopup="true" id="navbarDropdown" data-toggle="dropdown" aria-expanded="false">
                    <img src="images/img.jpg" alt="">John Doe
                  </a>
                  <div class="dropdown-menu dropdown-usermenu pull-right" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item"  href="../login.jsp"><i class="fa fa-sign-out pull-right"></i> Log Out</a>
                  </div>
                </li>

             </ul>
            </nav>
          </div>
        </div> -->
        <!-- /top navigation -->

        <!-- page content -->
     <%--    <div class="right_col" role="main" style="margin-left:0px;padding-right:90px;padding-left:90px;">
          <!-- top tiles -->
          <div class="row">
            <div class="tile_count" style="width:inherit;">
            <div class="col-md-12 tile_stats_count">
              <span class="count_top"><i class="fa fa-user"></i> Total Portfolio Value</span>
              <div class="count" id="totalPortfolio">$<%= portfolio_value%></div>
              <!-- <span class="count_bottom"><i class="green">4% </i> From last Week</span> -->
            </div>
            </div>
          </div> --%>




          <div class="row">
            <div class="col-md-12 col-sm-12 ">
              <div class="dashboard_graph">

                <div class="row x_title">
                  <div class="col-md-6">
                    <h3>Your Stock Portfolio Performance</h3>
                  </div>
                  <div class="col-md-6">
                  <!--
                    <div id="reportrange" class="pull-right" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc">
                      <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
                      <span>December 30, 2014 - January 28, 2015</span> <b class="caret"></b>
                    </div>
                  -->
                  </div>
                </div>

                <div class="col-md-9 col-sm-9 ">
                	<!--   <div id="chart_plot_01" class="demo-placeholder"></div> -->
                  <!-- <canvas id="chartContainer" width="1000" height="400"></canvas> -->
                  <div id="chartContainer" style="width: 1000px; height: 400px" ></div>
                  <script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
				  <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
                  <%= chart%>
                  </script>
			</div>
                <div class="col-md-3 col-sm-3  bg-white">
                  <div class="x_title">
                    <h2>Toggle Graph</h2>
                    <div class="clearfix"></div>
                  </div>

                  <button type="button" class="btn btn-primary" id="spytoggle" style="color:#007bff;background:#fff;">Remove S&P</button>
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
					      <div class="modal-body">
					      	 <a href="exampleStockCSV.csv" download="example">
					     	 <button type="button" style="background: darkgrey;" class="btn btn-primary">Download Example CSV</button>
					     	 </a>
						      <div id="dvImportSegments" class="fileupload">
								<fieldset>
									<legend>Upload your CSV file</legend>
									<input type="file" name="File Upload" id="txtFileUpload" accept=".csv" />
								</fieldset>
							 </div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
					        <button type="button" class="btn btn-primary">Upload</button>
					      </div>
					    </div>
					  </div>
					</div>

                  <div class="">
                    <ul id="stock_list" class="to_do">
                      <li>
                          <div style="display:inline; float: left; width: 15%;">
                            <button type="button" style="background:lightgrey; border:none; border-radius:5px; color:white;" class="flat" data-toggle="modal" data-target="#removeStockModal">X</button><br>
                            <!-- Modal for Remove Stock -->
                            <div class="modal fade" id="removeStockModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
                                    <button type="button" class="btn btn-primary deletestock" id="stockremovebutton">Remove Stock</button>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                          <div style="float: left; width: 85%;">
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   Exchange: </p><p style="text-align:left; display:inline; font-weight:bold;">Exchange</p>
                              <br>
                            </div>
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   Ticker: </p><p style="text-align:left; display:inline; font-weight:bold;">Exchange</p>
                              <br>
                            </div>
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   # Shares: </p><p style="text-align:left; display:inline; font-weight:bold;">Exchange</p>
                              <br>
                            </div>
                        </div>
                        <br>
                        <br>
                        <br>


                      </li>

                      <li>
                          <div style="display:inline; float: left; width: 15%;">
                            <button type="button" style="background:lightgrey; border:none; border-radius:5px; color:white;" class="flat" data-toggle="modal" data-target="#removeStockModal">X</button><br>
                            <!-- Modal for Remove Stock -->
                            <div class="modal fade" id="removeStockModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
                                    <button type="button" class="btn btn-primary deletestock" id="stockremovebutton">Remove Stock</button>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                          <div style="float: left; width: 85%;">
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   Exchange: </p><p style="text-align:left; display:inline; font-weight:bold;">Exchange</p>
                              <br>
                            </div>
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   Ticker: </p><p style="text-align:left; display:inline; font-weight:bold;">Exchange</p>
                              <br>
                            </div>
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   # Shares: </p><p style="text-align:left; display:inline; font-weight:bold;">Exchange</p>
                              <br>
                            </div>
                        </div>
                        <br>
                        <br>
                        <br>


                      </li>
                    </ul>

                    <!-- Button trigger modal -->
                    <div class="addstockbutton">
                    <button type="button" class="addstockbutton" data-toggle="modal" data-target="#addStockModal">Add Stock</button>
                    </div>

										

					
					
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
                          <div class="modal-body">
                            <div class="inputrow">
                              <div style="float: left; width: 30%; overflow: scroll; margin-right:2.5%; display:table-cell;">
                                <div style="margin-right:5px;">
                                  <p style="text-align:center;">Exchange*</p>
                                  <input class="stockinput" type="text" id="exchange" name="fname" required>
                                </div>
                              </div>
                              <div style="float: left; width: 30%; overflow: scroll; margin-right:2.5%; margin-left:2.5%; display:table-cell;">
                                <div style="margin-right:5px;">
                                  <p style="text-align:center;">Ticker*</p>
                                  <input class="stockinput" type="text" id="ticker" name="fname" required>
                                </div>
                              </div>
                              <div style="float: left; width: 30%; overflow: scroll; margin-left:2.5%; display:table-cell;">
                                <div style="margin-right:5px;">
                                  <p style="text-align:center;"># Shares*</p>
                                  <input class="stockinput" type="text" id="shares" name="fname" required>
                                </div>
                              </div>
                            </div>
                            <br>
                            <div class="inputrow">
                              <div style="float: left; width: 30%; overflow: scroll; margin-right:2.5%; display:table-cell;">
                                <div style="margin-right:5px;">
                                  <p style="text-align:center;">Date Purchased*</p>
                                  <input class="stockinput" type="date" id="exchange" name="fname" required>
                                </div>
                              </div>
                              <div style="float: left; width: 30%; overflow: scroll; margin-right:2.5%; margin-left:2.5%; display:table-cell;">
                                <div style="margin-right:5px;">
                                  <p style="text-align:center;">Date Sold</p>
                                  <input class="stockinput" type="date" id="ticker" name="fname">
                                </div>
                              </div>
                              <div style="float: left; width: 30%; overflow: scroll; margin-left:2.5%; display:table-cell;">
                                <div style="margin-right:5px;">
                                  <p style="text-align:center;">Note: date must be in mm/dd/YYYY and only NYSE/NASDAQ supported</p>
                                </div>
                              </div>
                            </div>
                          </div>
                          <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" id="stockaddbutton">Add to my portfolio</button>
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
                  </div>


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



                      // Adding a stock JS
                      $("#stockaddbutton").click(function(){
                        console.log("stock add attempt")
                        var exchange = $("#exchange").val();
                        var ticker = $("#ticker").val();
                        var shares = $("#shares").val();
                        $("#exchange").val("");
                        $("#ticker").val("");
                        $("#shares").val("");
                        // var appendingHTML = '<li><div style="display:inline;"><button type="button" id="delete1" style="background:lightgrey; border:none; border-radius:5px;" class="flat">X</button><br></div><div style="display:inline;"><p style="text-align:left;display:inline;">   Exchange: </p><p style="text-align:left;display:inline;font-weight: bold;">' + exchange + '</p><br> </div><div style="display:inline;"> <p style="text-align:left;display:inline;">   Ticker: </p><p style="text-align:left;display:inline;font-weight: bold;">' + ticker + '</p><br> </div><div style="display:inline;"><p style="text-align:left;display:inline;">   # Shares: </p><p style="text-align:left;display:inline;font-weight: bold;">' + shares + '</p><br></div></li>';
                        var appendingHTML = `
                        <li>
                            <div style="display:inline; float: left; width: 15%;">
                              <button type="button" style="background:lightgrey; border:none; border-radius:5px; color:white;" class="flat" data-toggle="modal" data-target="#removeStockModal">X</button><br>
                              <!-- Modal for Remove Stock -->
                              <div class="modal fade" id="removeStockModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
                                      <button type="button" class="btn btn-primary deletestock" id="stockremovebutton">Remove Stock</button>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <div style="float: left; width: 85%;">
                              <div style="display:inline;">
                                <p style="text-align:left;display:inline;">   Exchange: </p><p style="text-align:left; display:inline; font-weight:bold;">`+ exchange + `</p>
                                <br>
                              </div>
                              <div style="display:inline;">
                                <p style="text-align:left;display:inline;">   Ticker: </p><p style="text-align:left; display:inline; font-weight:bold;">`+ ticker + `</p>
                                <br>
                              </div>
                              <div style="display:inline;">
                                <p style="text-align:left;display:inline;">   # Shares: </p><p style="text-align:left; display:inline; font-weight:bold;">`+ shares + `</p>
                                <br>
                              </div>
                          </div>
                          <br>
                          <br>
                          <br>


                        </li>

                        `;


                        $( "#stock_list" ).append(appendingHTML);

                        $('#addStockModal').modal('hide');

                        // This deletes buttons when they click out of the stock
                        var els = document.getElementsByClassName("deletestock");

                        for (var i = 0; i < els.length; i++) {
                            els[i].addEventListener('click', function (e) {
                              $('#removeStockModal').modal('hide');
                                console.log("delete stock")
                                e.preventDefault();
                                e.target.closest('li').remove();
                            });
                        }
                      });

                      // This deletes buttons when they click out of the stock
                      var els = document.getElementsByClassName("deletestock");

                      for (var i = 0; i < els.length; i++) {
                          els[i].addEventListener('click', function (e) {
                              $('#removeStockModal').modal('hide');
                              console.log("delete stock")
                              e.preventDefault();
                              e.target.closest('li').remove();
                          });
                      }




                    });
                    // $( document ).ready(function() {
                    //   $( "#stock_list" ).append("");
                    //   console.log( "ready!" );
                    // });
                  </script>
                </div>
              </div>
            </div>
            <!-- End to do list -->

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
      </div>
    </div>

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
    <!-- bootstrap-daterangepicker -->
    <script src="../vendors/moment/min/moment.min.js"></script>
    <script src="../vendors/bootstrap-daterangepicker/daterangepicker.js"></script>

    <!-- Custom Theme Scripts -->
    <script src="../build/js/custom.min.js"></script>

  </body>
</html>
