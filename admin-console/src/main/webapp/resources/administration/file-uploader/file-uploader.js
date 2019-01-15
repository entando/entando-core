var FileUploadManager = function (config) {
    this.config = config;
    this.files = [];
    this.UPLOAD_STATUSES = {
        PENDING: 'pending',
        IN_PROGRESS: 'in-progress',
        DONE: 'done',
        FAILED: 'failed',
        CANCELED: 'canceled'
    };

    /*
     * Generating unique IDs for each upload
     * This function is an attempt to implement RFC-4222 https://www.ietf.org/rfc/rfc4122.txt
     * It was found in this SO answer: https://stackoverflow.com/a/2117523/2267244
     */
    this.generateUploadId = function () {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    };

    this.prepareFile = function (fileInput) {
        return {
            uploadId: this.generateUploadId(),
            name: fileInput.name,
            numberOfPieces: Math.ceil(fileInput.size / this.config.sliceSize),
            numberOfUploadedPieces: 0,
            uploadStatus: this.UPLOAD_STATUSES.PENDING,
            description: fileInput.name,
            fileInput: fileInput,
            size: fileInput.size
        };
    };

    this.prepareFiles = function (fileInputs) {
        var preparedFiles = [];
        for (var i = 0; i < fileInputs.length; i++) {
            preparedFiles.push(this.prepareFile(fileInputs[i]));
        }

        return preparedFiles;
    };

    this.insertFile = function (file) {
        this.files.push(file);
    };

    this.insertFiles = function (files) {
        for (var i = 0; i < files.length; i++) {
            this.insertFile(files[i]);
        }
    };

    this.slice = function (file, start, end) {
        var slice = file.mozSlice ? file.mozSlice :
            file.webkitSlice ? file.webkitSlice :
                file.slice ? file.slice : function () {
                };
        return slice.bind(file)(start, end);
    };

    this.getNextPendingFileIndex = function () {
        for (var i = 0; i < this.files.length; i++) {
            if (this.files[i].uploadStatus === this.UPLOAD_STATUSES.PENDING || this.files[i].uploadStatus === this.UPLOAD_STATUSES.IN_PROGRESS) {
                return i;
            }
        }

        return false;
    };

    this.getNextPiece = function (fileIndex) {
        var file = this.files[fileIndex];
        var numberOfUploadedPieces = file.numberOfUploadedPieces;

        if (numberOfUploadedPieces === file.numberOfPieces) {
            return false;
        }

        var sliceSize = this.config.sliceSize;
        var fileSize = file.size;

        var start = numberOfUploadedPieces * sliceSize;
        var end = start + sliceSize;
        if (fileSize - end < 0) {
            end = fileSize;
        }

        return {
            slice: this.slice(file.fileInput, start, end),
            start: start,
            end: end
        };
    };



    this.prepareFormData = function(piece, file){
        var formData = new FormData();
        formData.append('fileUpload', piece.slice);
        formData.append('start', piece.start);
        formData.append('end', piece.end);
        formData.append('uploadId', file.uploadId);
        formData.append('fileSize', file.size);
        formData.append('fileName', file.name);
        formData.append('descr', file.description);
        formData.append('resourceTypeCode', document.getElementById('resourceTypeCode').value);

        return formData;
    };

    this.uploadPiece = function (piece, file, nextPendingFileIndex) {
        var formData = this.prepareFormData(piece, file);
        var xhr = new XMLHttpRequest();
        xhr.open('POST', this.config.saveAction, true);

        xhr.onreadystatechange = function () {
            // Call a function when the state changes.
            if (this.readyState === XMLHttpRequest.DONE) {
                // Request finished. Do processing here.
                if (this.status === 200) {
                    fileUploadManager.files[nextPendingFileIndex].numberOfUploadedPieces++;
                    fileUploadManager.pieceUploadDone(nextPendingFileIndex);
                    console.log("DONE PIECE UPLOAD");
                    fileUploadManager.processNextFileR()

                } else {
                    // Retry failed upload
                    fileUploadManager.uploadPiece(piece, file, nextPendingFileIndex);
                }

            }
        };

        xhr.send(formData);
    };

    this.pieceUploadDone = function(fileIndex){
        this.updateProgressBar(fileIndex);
    };

    this.updateProgressBar = function (fileIndex) {
        var $progress = $('#progress_' + fileIndex);
        var progressValue = Math.ceil(this.files[fileIndex].numberOfUploadedPieces / this.files[fileIndex].numberOfPieces * 100);
        var $progressBar = $progress.find('.progress-bar');
        $progressBar.attr('aria-valuenow', progressValue);
        $progressBar.css("width", progressValue + "%");
        $progressBar.find('span').text(progressValue + "%");
    };
    this.processNextFileR = function () {
        var nextPendingFileIndex = this.getNextPendingFileIndex();
        if (nextPendingFileIndex !== false) {
            var file = this.files[nextPendingFileIndex];
            this.files[nextPendingFileIndex].uploadStatus = this.UPLOAD_STATUSES.IN_PROGRESS;
            var piece = this.getNextPiece(nextPendingFileIndex);

            if (piece) {
                this.uploadPiece(piece, file, nextPendingFileIndex);
            } else {
                this.files[nextPendingFileIndex].uploadStatus = this.UPLOAD_STATUSES.DONE;
                console.log('DONE UPLOADING FILE: ' + nextPendingFileIndex);
                this.processNextFileR();
            }
        } else {
            console.log("FINISHED FILES UPLOAD PROCESS");
        }
    }


};

var fileUploadManager;

jQuery(document).ready(function ($) {
    fileUploadManager = new FileUploadManager({
        sliceSize: 1048576,
        saveAction: 'upload',
        stopAction: 'stopUploadAndDelete.action'
    });

    $('#save').on('change', 'input', function (e) {
        if ('files' in e.target) {
            fileUploadManager.insertFiles(fileUploadManager.prepareFiles(e.target.files));
            // fileUploadManager.processNextFileR();
        }
    });
});


// function deleteFile(fileId, stopUploadAndDeleteAction) {
//     var formdata = new FormData();
//     var xhr = new XMLHttpRequest();
//     xhr.open('POST', stopUploadAndDeleteAction, true);
//     formdata.append('fileID', fileId);
//     xhr.send(formdata);
// }
