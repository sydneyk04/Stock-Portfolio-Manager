console.log("init importCSV");

$(document).ready(function() {

    // The event listener for the file upload
    document.getElementById('txtFileUpload').addEventListener('change', upload, false);

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
            var file = evt.target.files[0];
            var reader = new FileReader();
            reader.readAsText(file);
            reader.onload = function(event) {
                var lines = event.target.result.split('\r\n');
                for(i = 1; i < lines.length; ++i)
                {
                  var lineElements = lines[i].split(",");
                  var newData = {
                    symbol: lineElements[1],
                    from: lineElements[2],
                    to: lineElements[3],
                    shares: lineElements[4]
                  }
                  var ref = firebase.database().ref().child('users').child(lineElements[0]).child('portfolio');
                  ref.update(newData);
                  console.log("updated");
                }
            };
            reader.onerror = function() {
                alert('Unable to read ' + file.fileName);
            };
        }
    }
});
