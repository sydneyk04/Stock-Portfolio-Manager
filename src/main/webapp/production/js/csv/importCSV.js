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
            var content;
            var file = document.getElementById('txtFileUpload').files[0];
            var reader = new FileReader();
            reader.readAsText(file);
            reader.onload = function(event) {
                var lines = event.target.result.split('\r\n');
                for(i = 1; i < lines.length; ++i)
                {
                  content += lines[i] + ",";
                }
                content = content.substring(0, content.length-1);
                var form = document.getElementById("csvAddForm");
                document.getElementById("csvContent").value = content;
                form.submit();
                console.log("submitted");
            };
            reader.onerror = function() {
                alert('Unable to read ' + file.fileName);
            };
        }
    }
});
