// TODO - Integrate new upload manager with crop-editor.


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

// Configure file uploader.
var sliceSize = 1048576;
var saveAction = 'upload';
var stopUploadAndDeleteAction = 'stopUploadAndDelete.action';

/*
 * File upload statuses: pending, in-progress, done, failed.
 */

var files = [];

var collectFiles = function (e) {
    console.log(e);
    var fileInputs = e.target;
    for (var i = 0; i < fileInputs.files.length; i++) {
        addFile(fileInputs.files[i]);
    }


    startNextFileUpload();
};

ready(function () {
    var fileInput = document.getElementById('fileUpload_0');
    fileInput.addEventListener('change', collectFiles, false);
});


var addFile = function (fileInput) {
    console.log(fileInput);
    var file = {
        numberOfPieces: Math.ceil(fileInput.size / sliceSize),
        numberOfUploadedPieces: 0,
        uploadStatus: 'pending',
        description: fileInput.name,
        fileInput: fileInput,
        size: fileInput.size,
        name: fileInput.name,
        uploadId: uuidv4()
    };
    files.push(file);
};


var uploadNextFilePiece = function (fileIndex) {
    var file = files[fileIndex];
    files[fileIndex].uploadStatus = 'in-progress';
    var piece = getNextPiece(fileIndex);

    if (piece) {
        var formdata = new FormData();
        var xhr = new XMLHttpRequest();
        xhr.open('POST', saveAction, true);
        formdata.append('fileUpload', piece.slice);
        formdata.append('start', piece.start);
        formdata.append('end', piece.end);
        formdata.append('uploadId', file.uploadId);
        formdata.append('fileSize', file.size);
        formdata.append('fileName', file.name);
        formdata.append('descr', file.description);

        xhr.onreadystatechange = function () {
            // Call a function when the state changes.
            if (this.readyState === XMLHttpRequest.DONE) {
                // Request finished. Do processing here.
                if (this.status === 200) {
                    files[fileIndex].numberOfUploadedPieces++;
                }

                // Retry failed upload
                uploadNextFilePiece(fileIndex);
            }
        };

        xhr.send(formdata);
    } else {
        files[fileIndex].uploadStatus = 'done';
        console.log('DONE UPLOADING: ' + fileIndex);
        startNextFileUpload();
    }
};

var getNextPiece = function (fileIndex) {
    var file = files[fileIndex];
    console.log(file);
    console.log(fileIndex);
    console.log("FILE");
    if (file.numberOfUploadedPieces === file.numberOfPieces) {
        return false;
    }



    var start = file.numberOfUploadedPieces * sliceSize;
    var end = start + sliceSize;
    if (file.size - end < 0) {
        end = file.size;
    }

    return {slice: slice(file.fileInput, start, end), start: start, end: end};
};

// Return next pending file or false if there is no more pending files.
var getNextPendingFileIndex = function () {
    for (var i = 0; i < files.length; i++) {
        if (files[i].uploadStatus === 'pending') {
            return i;
        }
    }

    return false;
};

var startNextFileUpload = function () {
    var nextPendingFileIndex = getNextPendingFileIndex();
    console.log(nextPendingFileIndex);
    if (nextPendingFileIndex !== false ) {
        uploadNextFilePiece(nextPendingFileIndex);
    }

    console.log('ALL FILES FINISHED!');
};


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

function deleteFile(fileId, stopUploadAndDeleteAction) {
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