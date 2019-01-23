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
            fileUploadContentType: fileInput.type,
            name: fileInput.name,
            numberOfPieces: Math.ceil(fileInput.size / this.config.sliceSize),
            numberOfUploadedPieces: 0,
            uploadStatus: this.UPLOAD_STATUSES.PENDING,
            description: fileInput.name,
            fileInput: fileInput,
            size: fileInput.size,
            domElements: {}
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
        var newId = this.files.length;
        var $formGroup;

        if ($('#formGroup-' + newId).length) {
            $formGroup = $('#formGroup-' + newId);
        } else {
            $formGroup = $(this.generateFormGroupById(newId));
            $formGroup.appendTo('#fields-container');
        }

        $formGroup.find('.file-description').val(file.description);
        // Setup hidden fields
        $formGroup.find('.fileUploadName').val(file.name);
        $formGroup.find('.fileUploadId').val(file.uploadId);
        $formGroup.find('.fileUploadContentType').val(file.fileUploadContentType);
        console.log($formGroup);

        file.domElements.$formGroup = $formGroup;
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
            if (this.files[i].uploadStatus === this.UPLOAD_STATUSES.PENDING ||
                this.files[i].uploadStatus === this.UPLOAD_STATUSES.IN_PROGRESS) {
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

    this.prepareFormData = function (piece, file) {
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

    this.pieceUploadDone = function (fileIndex) {
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
            var $submitBtn = $('#submit');
            $submitBtn.removeAttr('disabled');
            setTimeout(function () {
                $('#submit').unbind('click');
                $('#submit').click();
            }, 1000);

        }
    };

    this.generateFormGroupById = function (id) {
        if ($('#formGroup-' + id).length) {
            return $('#formGroup-' + id);
        }

        return $.parseHTML('<div id="formGroup-' + id + '" class="form-group">' +
            '        <label class="col-sm-2 control-label" for="descr">' +
            '            Name' +
            '            <i class="fa fa-asterisk required-icon"></i>' +
            '        </label>' +
            '        <div class="col-sm-4 fileUpload-right">' +
            '            <input type="text" name="descr_' + id + '" maxlength="250" value="" id="descr_' + id + '" class="form-control file-description">' +
            '        <div class="progress" id="progress_' + id + '">' +
            '               <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">' +
            '                   <span>0%</span>' +
            '               </div>' +
            '           </div></div>' +
            '        <div id="fileUpload_box_' + id + '">' +
            '            <label class="col-sm-1 control-label" for="upload">' +
            '                File' +
            '                    <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-placement="top" data-content="Default image file formats are jpg,jpeg,png" data-original-title="" style="position: absolute; right: 8px;">' +
            '                        <span class="fa fa-info-circle"></span>' +
            '                    </a>' +
            '                ' +
            '            </label>' +
            '' +
            '            <div class="col-sm-4">' +
            '                <label id="fileUpload_label_' + id + '" for="fileUpload_' + id + '" class="btn btn-default">' +
            '                    Select one or more files' +
            '                </label>' +
            '' +
            '                <input type="file" name="fileUpload" id="fileUpload_' + id + '" class="input-file-button" multiple="true">' +
            '                <span id="fileUpload_' + id + '_selected" class="file-upload-selected-name">No file chosen</span>' +
            '            </div>' +
            '        <input type="hidden" name="fileUploadId_' + id + '" maxlength="500" value="" id="fileUploadId_' + id + '" class="form-control fileUploadId">' +
            '        <input type="hidden" name="fileUploadName_' + id + '" maxlength="500" value="" id="fileUploadName_' + id + '" class="form-control fileUploadName">' +
            '        <input type="hidden" name="fileUploadContentType_' + id + '" maxlength="500" value="" id="fileUploadContentType_' + id + '" class="form-control fileUploadContentType"></div>' +
            '' +
            '        ' +
            '' +
            '            <div class="col-sm-1">' +
            '                <div class="list-view-pf-actions">' +
            '                    <div class="dropdown pull-right dropdown-kebab-pf">' +
            '                        <button class="btn btn-menu-right dropdown-toggle" type="button" id="dropdownKebabRight' + id + '" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">' +
            '                            <span class="fa fa-ellipsis-v"></span>' +
            '                        </button>' +
            '                        <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight2">' +
            '                            <li>' +
            '                                <a href="#" class="edit-fields">Edit</a>' +
            '                            </li>' +
            '                            <li>' +
            '                                <a href="#" class="delete-fields">Delete</a>' +
            '                            </li>' +
            '                        </ul>' +
            '                    </div>' +
            '                </div>' +
            '            </div>' +
            '        ' +
            '    </div>');
    };


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
        }
    });


    $('#submit').on('click', function (e) {
        if ($(this).attr('in-progress') === undefined) {
            console.log("CLICK");
            e.preventDefault();
            $(this).attr("in-progress", true);
            $(this).attr("disabled", "disabled");
            $(this).prepend("<div class=\"spinner spinner-xs spinner-inline\">")
            fileUploadManager.processNextFileR();
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
