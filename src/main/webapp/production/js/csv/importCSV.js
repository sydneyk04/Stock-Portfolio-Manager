console.log("init_importCSV");

$(document).ready(function() {

    // The event listener for the file upload
    document.getElementById('csvAddButton').addEventListener('click', upload, false);

    // Method that checks that the browser supports the HTML5 File API
    function browserSupportFileUpload() {
        var isCompatible = false;
        if (window.File && window.FileReader && window.FileList && window.Blob) {
        isCompatible = true;
        }
        return isCompatible;
    }

    // Method that reads and processes the selected file
    function upload(evt) {
      console.log("called");
    if (!browserSupportFileUpload()) {
        alert('The File APIs are not fully supported in this browser!');
        } else {
            var data = null;
            var file = document.getElementById('txtFileUpload').files[0];
            var reader = new FileReader();
            reader.readAsText(file);
            reader.onload = function(event) {
                var lines = event.target.result.split('\r\n');
                for(i = 1; i < lines.length; ++i)
                {
                  var lineElements = lines[i].split(",");
                  var ticker = lineElements[1];
                  var newData = {
                    name: lineElements[2],
                    from: lineElements[3],
                    to: lineElements[4],
                    shares: lineElements[5]
                  }
                  var username = lineElements[0];
                  var buyDate = newData["from"];
                  var sellDate = newData["to"];
                  console.log(username);
                  // Update existing node if ticker/from/to match, else add new one
                  var flagToAppendHTML = true;
                  var ref = firebase.database().ref().child('users').child(username).child('portfolio').child(ticker);
                  ref.once("value")
                    .then(function(snapshot) {
                    if(snapshot.val() == null) {
                    } else if(snapshot.val()["from"] == newData["from"] && snapshot.val()["to"] == newData["to"]) {
                      newData["shares"] = (parseFloat(newData["shares"]) + parseFloat(snapshot.val()["shares"])).toString();
                      document.querySelector("#li-"+ticker).getElementsByTagName('p')[3].innerHTML = newData["shares"];
                      flagToAppendHTML = false;
                    } else {
                    }
                  }).then(() => {
                    if(flagToAppendHTML) {
                      var appendingHTML = `
                      <li id="li-`+ ticker +`" class="d-flex">
                          <div style="display:inline; float: left; width: 15%;">
                            <button type="button" style="background:lightgrey; border:none; border-radius:5px; color:white;" class="flat" data-toggle="modal" data-target="#removeStockModal-`+ticker+`">X</button><br>
                            <!-- Modal for Remove Stock -->
                            <div class="modal fade" id="removeStockModal-`+ticker+`" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
                                    <form class="" action="/dashboard" method="post">
                                      <input type="hidden" name="action" value="removeStock">
                                      <input type="hidden" name="ticker" value="<%=myStocks.get(i).get(0) %>">
                                      <button type="submit" class="btn btn-primary deletestock" data-dismiss="modal" id="stockremovebutton">Remove Stock</button>
                                    </form>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                          <div style="float: left; width: 85%;">
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   Ticker: </p><p style="text-align:left; display:inline; font-weight:bold;">`+ ticker + `</p>
                              <br>
                            </div>
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   # Shares: </p><p style="text-align:left; display:inline; font-weight:bold;">`+ lineElements[5] + `</p>
                              <br>
                            </div>
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   Purchase Date: </p><p style="text-align:left; display:inline; font-weight:bold;">${newData["from"]}</p>
                              <br>
                            </div>
                            <div style="display:inline;">
                              <p style="text-align:left;display:inline;">   Sell Date: </p><p style="text-align:left; display:inline; font-weight:bold;">${newData["to"]}</p>
                              <br>
                            </div>
                            <div style="display:inline;">
                             <p style="text-align:left;display:inline;">   Calculate in Portfolio: </p><p style="text-align:left; display:inline; font-weight:bold;">
                               <form name="formname" action="/dashboard" method="POST">
                                 <input type="hidden" name="action" value="portfolioState">
                                 <input type="hidden" name="ticker" value="${ticker}">
                                 <button style="text-align:left;display:inline;" type="submit"class="btn btn-light btn-sm">Yes</button>
                               </form>
                             </p>
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

                      els[els.length-1].addEventListener('click', function (e) {
                          $('#removeStockModal').modal('hide');
                          console.log("delete stock")
                          e.preventDefault();
                          var username = '<%=username%>';
                          var ref = firebase.database().ref().child('users').child(username).child('portfolio').child(ticker);
                          ref.remove();
                          console.log(e.target);
                          e.target.closest('li').remove();
                      });
                }
            });
            reader.onerror = function() {
                alert('Unable to read ' + file.fileName);
            };
        }
    }
}}});
