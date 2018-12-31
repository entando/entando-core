// Configure file uploader.
var sliceSize = 1048576;
var saveAction = 'save';
var stopUploadAndDeleteAction = 'stopUploadAndDelete.action';

/*
 * File upload statuses: pending, in-progress, done, failed.
 */

var files = [];

var uploadNextFilePiece = function (fileIndex) {
    files[fileIndex].uploadStatus = 'in-progress';
    var piece = getNextPiece();

    if (piece) {
        var formdata = new FormData();
        var xhr = new XMLHttpRequest();
        xhr.open('POST', action, true);
        formdata.append('fileUpload', piece);
        formdata.append('start', start);
        formdata.append('end', end);
        formdata.append('uploadId', uploadId);
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

                uploadNextFilePiece(fileIndex);
            }
        };

        xhr.send(formdata);
    } else {
        files[fileIndex].uploadStatus = 'done';
        startNextFileUpload();
    }
};

var getNextPiece = function (fileIndex) {
    var file = files[fileIndex];
    if (file.numberOfUploadedPieces === file.numberOfPieces) {
        return false;
    }

    var start = file.numberOfUploadedPieces * sliceSize;
    var end = start + sliceSize;
    if (file.size - end < 0) {
        end = file.size;
    }

    return slice(file.fileInput, start, end);
};

// Return next pending file or false if there is no more pending files.
var getNextPendingFileIndex = function () {
    for (var i = 0; i < files.size; i++) {
        if (files[i].uploadStatus === 'pending') {
            return i;
        }
    }

    return false;
};

var startNextFileUpload = function (start) {
    var nextPendingFileIndex = getNextPendingFileIndex();
    if (nextPendingFileIndex) {
        uploadNextFilePiece(nextPendingFileIndex);
    }
};

var addFile = function (fileInput, fileDescription) {
    var file = {
        numberOfPieces: Math.ceil(fileInput.size / sliceSize),
        numberOfUploadedPieces: 0,
        uploadStatus: 'pending',
        description: fileDescription,
        fileInput: fileInput,
        size: fileInput.size,
        name: fileInput.name,
        uploadId: uuidv4()
    };
    files.push(file);
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