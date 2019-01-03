function ready(callback) {
    // in case the document is already rendered
    if (document.readyState != 'loading') callback();
    // modern browsers
    else if (document.addEventListener) document.addEventListener('DOMContentLoaded', callback);
    // IE <= 8
    else document.attachEvent('onreadystatechange', function () {
            if (document.readyState == 'complete') callback();
        });
}

ready(function () {
    (function () {
        var saveAction = 'save';
        var stopUploadAndDeleteAction = 'stopUploadAndDelete.action';
        var uploadId = '068fe811-05d4-479e-9e54-44ad06da0beb';
        var fileInput = document.getElementById('fileUpload_0');

        fileInput.addEventListener('change', processFile, false);

        function processFile(e) {
            var file = fileInput.files[0];
            var size = file.size;
            var sliceSize = 1048576;
            var start = 0;
            setTimeout(loop, 1);

            function loop() {
                var end = start + sliceSize;
                var isLastSlice = false;
                if (size - end < 0) {
                    end = size;
                    isLastSlice = true;
                }
                var s = slice(file, start, end);
                send(s, start, end, size, fileInput.files[0].name, isLastSlice);
                if (end < size) {
                    start += sliceSize;
                    setTimeout(loop, 200);
                }
            }
        }

        function send(piece, start, end, size, fileName, isLastSlice) {
            var formdata = new FormData();
            var xhr = new XMLHttpRequest();
            xhr.open('POST', saveAction, true);
            formdata.append('start', start);
            formdata.append('end', end);
            formdata.append('fileUpload', piece);
            formdata.append('descr', 'Test description for chunks upload');
            formdata.append('uploadId', uploadId);
            formdata.append('fileSize', size);
            formdata.append('fileName', fileName);

            if (isLastSlice) {
                xhr.onreadystatechange = function () {
                    // Call a function when the state changes.
                    if (this.readyState === XMLHttpRequest.DONE) {
                        // Request finished. Do processing here.
                        if (this.status === 200) {
                            alert("Upload finished!");
                            document.getElementById('fileUploadName_0').value = fileInput.files[0].name;
                            document.getElementById('file_upload_content_type_0').value = fileInput.files[0].type;
                            fileInput.value = "";
                        }

                    }
                };
            }

            xhr.send(formdata);

        }

        /**
         * Formalize file.slice
         */
        function slice(file, start, end) {
            var slice = file.mozSlice ? file.mozSlice :
                file.webkitSlice ? file.webkitSlice :
                    file.slice ? file.slice : noop;
            return slice.bind(file)(start, end);
        }

        function noop() {
        }

        var deleteButton = document.getElementById('delete_file');
        deleteButton.addEventListener('click', deleteFile, false);

        function deleteFile(e) {
            e.preventDefault();
            var fileId = '068fe811-05d4-479e-9e54-44ad06da0beb';
            var formdata = new FormData();
            var xhr = new XMLHttpRequest();
            xhr.open('POST', stopUploadAndDeleteAction, true);
            formdata.append('fileID', fileId);
            xhr.send(formdata);
        }

        /*
         * Generating unique IDs for each upload
         * This function is an attempt to implement RFC-4222 https://www.ietf.org/rfc/rfc4122.txt
         * It was found in this SO answer: https://stackoverflow.com/a/2117523/2267244
         *
         */
        function uuidv4() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        }
    })();
});

