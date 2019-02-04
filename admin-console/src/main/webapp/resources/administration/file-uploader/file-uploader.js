var cropEditorEnabled = false;
console.log(cropEditorEnabled);
var attachmentUploadEnabled = false;

var FileUploadManager = function (config) {
    this.config = config;
    this.files = [];
    this.lastFileId = 0;
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
            domElements: {},
            imageData: '',
            cropper: false
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

        var $formGroup = this.prepareFormGroupForNewFiles();
        $formGroup = this.prepareFormGroupHiddenFields($formGroup, file);

        $formGroup.show();

        file.domElements.$formGroup = $formGroup;
        this.files.push(file);

        return this.files.length - 1;
    };

    this.prepareFormGroupForNewFiles = function () {
        var newId = this.files.length;
        var $formGroup;

        if ($('#formGroup-' + newId).length) {
            $formGroup = $('#formGroup-' + newId);
        } else {
            $formGroup = this.addFormGroupForNewFile();
        }

        return $formGroup;
    };

    this.addFormGroupForNewFile = function () {
        console.log("addFormGroupForNewFile");
        var newId = $('.form-group--file').length;
        var $formGroup;

        $formGroup = $(this.generateFormGroupById(newId));
        $formGroup.appendTo('#fields-container');

        return $formGroup;
    };

    this.insertFiles = function (files) {
        for (var i = 0; i < files.length; i++) {
            this.insertFile(files[i]);
        }
    };

    this.updateFile = function (fileIndex, fileInput, $formGroup) {
        console.log('updateFile');
        console.log(fileInput);
        var file = this.prepareFile(fileInput);
        console.log('FILE');
        console.log(file);

        if (typeof this.files[fileIndex] !== 'undefined') {
            file.domElements = this.files[fileIndex].domElements;
        }
        file.domElements.$formGroup = this.prepareFormGroupHiddenFields($formGroup, file)

        this.files[fileIndex] = file;
    };

    this.deleteFile = function (fileIndex) {
        this.files[fileIndex].uploadStatus = this.UPLOAD_STATUSES.CANCELED;
        this.files[fileIndex].domElements.$formGroup.hide();
    }


    this.prepareFormGroupHiddenFields = function ($formGroup, file) {
        $formGroup.find('.file-description').val(file.description);
        // Setup hidden fields
        $formGroup.find('.fileUploadName').val(file.name);
        $formGroup.find('.fileUploadId').val(file.uploadId);
        $formGroup.find('.fileUploadContentType').val(file.fileUploadContentType);

        return $formGroup;
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
        console.log("process");
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
                $('.form-group--file:hidden').remove();
                $('#submit').unbind('click');
                $('#submit').click();
            }, 1000);

        }
    };

    this.generateFormGroupById = function (id) {
        if ($('#formGroup-' + id).length) {
            return $('#formGroup-' + id);
        }

        return $.parseHTML('<div id="formGroup-' + id + '" class="form-group form-group--file" data-file-id="' + id + '">' +
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
            '                    Select a file' +
            '                </label>' +
            '' +
            '                <input type="file" name="fileUpload" id="fileUpload_' + id + '" class="input-file-button" >' +
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
    cropEditorEnabled = ($('.image_cropper_enabled').length === 1);
    console.log(cropEditorEnabled);
    attachmentUploadEnabled = ($('.attachment_upload_enabled').length === 1);

    fileUploadManager = new FileUploadManager({
        sliceSize: 1048576,
        saveAction: 'upload',
        stopAction: 'stopUploadAndDelete.action'
    });

    // Listen to add fields button click events and action on it.
    $('#add-fields').on('click', function (e) {
        e.preventDefault();
        fileUploadManager.addFormGroupForNewFile();
    });


    // Listen to delete fields kebab menu item click events
    // and delete related storeItem in modal.
    $('#save').on("click", ".delete-fields", function (e) {
        e.preventDefault();
        $formGroup = $(this).closest('.form-group');
        var fileId = parseInt($(this).closest('.form-group').data('fileId'));
        if (typeof fileUploadManager.files[fileId] === 'undefined') {
            $formGroup.hide();
        } else {
            fileUploadManager.deleteFile(fileId);
        }
    });

    // Listen to edit fields kebab menu item click events
    // and open related storeItem in modal.
    $('#save').on('click', '.edit-fields', function (e) {
        e.preventDefault();

        var fileId = parseInt($(this).closest('.form-group').data('fileId'));
        var file = fileUploadManager.files[fileId];

        // Remove previously active tab state.
        $('.bs-cropping-modal').find('.active').removeClass('active');

        // Add new active tab state;
        file.domElements.$tabPane.addClass('active');
        file.domElements.$tabNavigationItem.addClass('active');

        $('.bs-cropping-modal').modal('show');

    });

    $('#save').on('change', 'input', function (e) {
        if ('files' in e.target) {
            var files = [];
            for (var i = 0; i < e.target.files.length; i++) {
                files.push(e.target.files[i]);
            }

            if (files.length > 0) {
                // Change file input for currently selected file
                var $target = $(e.target);

                if ($target.attr('id') !== 'newFileUpload-multiple') {
                    $currentlyClickedFormGroup = $(e.target).closest('.form-group');
                    fileUploadManager.updateFile($currentlyClickedFormGroup.data('fileId'), files[0], $currentlyClickedFormGroup);
                    console.log(files);
                    files.splice(0, 1);
                }

                if (files.length > 0) {
                    console.log(files.length);
                    console.log("INSERT");
                    fileUploadManager.insertFiles(fileUploadManager.prepareFiles(files));

                    if (cropEditorEnabled) {
                        for (var i = 0; i < fileUploadManager.files.length; i++) {
                            var fileInput = fileUploadManager.files[i].fileInput;
                            readAsDataUrl(i, fileInput, function (fileIndex, imageData) {
                                fileUploadManager.files[fileIndex].imageData = imageData;
                                var tabResult = addTab(fileIndex);
                                fileUploadManager.files[fileIndex].domElements.$tabNavigationItem = tabResult.$tabNavigationItem;
                                fileUploadManager.files[fileIndex].domElements.$tabPane = tabResult.$tabPane;

                            })
                        }
                    }
                }


            }
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


    var $cropEditorModal = $('.bs-cropping-modal');

    $cropEditorModal.find('.nav-tabs').on('shown.bs.tab', '[data-toggle="tab"]', function () {
        var fileId = parseInt($(this).closest('.image-navigation-item').data('storeItemId'));
        var file = fileUploadManager.files[fileId];
        setupCropper(fileId);
    });

    $cropEditorModal.on('shown.bs.modal', function () {
        var fileId = parseInt($(this).find('.tab-pane.active').data('storeItemId'));
        var file = fileUploadManager.files[fileId];
        setupCropper(fileId);
    });


    // Listen on crop editor toolbar buttons clicks and action accordingly.
    $cropEditorModal.on('click', '.btn', function () {
        var $btn = $(this);
        var fileId = parseInt($(this).closest('.tab-pane').data('storeItemId'));
        var file = fileUploadManager.files[fileId];

        switch ($btn.data('method')) {
            case 'crop':

                if ($('.singleImageUpload').length === 1) {
                    var imageData = "";
                    if (file) {
                        imageData = file.cropper.getCroppedCanvas().toDataURL(file.type);
                        file.cropper.replace(imageData);
                        file.imageData = imageData;
                        fileUploadManager.files[fileId] = file;
                    }

                    // DOMToastSuccess("Image cropped!");
                } else {
                    var imageData = file.cropper.getCroppedCanvas().toDataURL(file.type);
                    var newFile = fileUploadManager.prepareFile(dataURLtoFile(imageData, name));
                    newFile.name = newFile.uploadId.substr(0, 4) + "_" + file.name;
                    newFile.description = newFile.uploadId.substr(0, 4) + "_" + file.description;

                    newFile.domElements.$formGroup = fileUploadManager.addFormGroupForNewFile();

                    newFile.imageData = imageData;


                    var newFileId = fileUploadManager.insertFile(newFile);
                    var tabResult = addTab(newFileId);
                    fileUploadManager.files[newFileId].domElements.$tabNavigationItem = tabResult.$tabNavigationItem;
                    fileUploadManager.files[newFileId].domElements.$tabPane = tabResult.$tabPane;

                }

                // DOMToastSuccess("Crop created!");

                break;
            case 'remove':
                // deleteStoreItem(fileId);
                break;
            case 'setDragMode':
                file.cropper.setDragMode($btn.data('option'));
                break;
            case 'zoom':
                file.cropper.zoom($btn.data('option'));
                break;
            case 'scaleX':
                var option = $btn.data('option');
                file.cropper.scaleX(option);
                $btn.data('option', -1 * option);
                break;
            case 'scaleY':
                var option = $btn.data('option');
                file.cropper.scaleY(option);
                $btn.data('option', -1 * option);
                break;
            case 'move':
                file.cropper.move($btn.data('option'), $btn.data('second-option'));
                break;
            case 'rotate':
                file.cropper.rotate($btn.data('option'));
                break;
            case 'setAspectRatio':
                file.cropper.setAspectRatio($btn.data('option'));
                break;

            default:
                return true;
        }
    });


    // Utils

    var addTab = function (fileIndex) {
        var file = fileUploadManager.files[fileIndex];
        var $imageNav = $('.image-navigation');
        var $tabContent = $('.bs-cropping-modal').find('.tab-content');

        // Copy image navigation item - tab navigation item
        var $newTabNavigationItem = $('#image-navigation-item-blueprint').clone();
        $newTabNavigationItem.addClass('image-navigation-item');
        $newTabNavigationItem.find('a').attr('href', '#store_item_' + fileIndex);
        $newTabNavigationItem.find('a').text(file.name);
        $newTabNavigationItem.removeClass('hidden');
        $newTabNavigationItem.attr('id', 'image-navigation-item_' + fileIndex);
        $newTabNavigationItem.attr('data-store-item-id', fileIndex);

        // Copy tab pane
        var $newTabPane = $('#tab-pane-blueprint').clone();
        $newTabPane.addClass('tab-pane');
        $newTabPane.removeClass('hidden');
        $newTabPane.attr('id', 'store_item_' + fileIndex);
        $newTabPane.attr('data-item-id', fileIndex);
        $newTabPane.attr('data-store-item-id', fileIndex);
        var $newImage = $newTabPane.find('.store_item_');
        $newImage.attr('src', file.imageData);
        $newImage.addClass('store_item_' + fileIndex);

        return {
            $tabNavigationItem: $newTabNavigationItem.appendTo($imageNav),
            $tabPane: $newTabPane.appendTo($tabContent)
        }
    };


    function toDataUrl(url, callback) {
        var xhr = new XMLHttpRequest();
        xhr.onload = function () {
            var reader = new FileReader();
            reader.onloadend = function () {
                callback(reader.result);
            };
            reader.readAsDataURL(xhr.response);
        };
        xhr.open('GET', url);
        xhr.responseType = 'blob';
        xhr.send();
    }

    function dataURLtoFile(dataurl, filename) {
        var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new File([u8arr], filename, {type: mime});
    }

    function readAsDataUrl(fileIndex, fileInput, callback) {
        var reader = new FileReader();

        reader.onload = function (e) {
            callback(fileIndex, e.target.result);
        };

        reader.readAsDataURL(fileInput);
    }


    var setupCropper = function (fileId) {
        var file = fileUploadManager.files[fileId];
        file.cropper = new Cropper(
            $('.store_item_' + fileId)[0],
            {
                maxWidth: 300,
                maxHeight: 300,
                aspectRatio: 16 / 9,
                preview: $('#store_item_' + fileId).find('.img-preview'),
                crop: function (e) {
                    var data = e.detail;

                    file.domElements.$tabPane.find('.dataX').val(Math.round(data.x));
                    file.domElements.$tabPane.find('.dataY').val(Math.round(data.y));
                    file.domElements.$tabPane.find('.dataHeight').val(Math.round(data.height));
                    file.domElements.$tabPane.find('.dataWidth').val(Math.round(data.width));
                    file.domElements.$tabPane.find('.dataRotate').val((typeof data.rotate !== 'undefined' ? data.rotate : ''));
                    file.domElements.$tabPane.find('.dataScaleX').val((typeof data.scaleX !== 'undefined' ? data.scaleX : ''));
                    file.domElements.$tabPane.find('.dataScaleY').val((typeof data.scaleY !== 'undefined' ? data.scaleY : ''));
                }
            });

        fileUploadManager.files[fileId] = file;
    };


    $(document).on({
        'dragover dragenter': function (e) {
            $('.file-upload-region').addClass('file-upload-region--dragging');
            e.preventDefault();
            e.stopPropagation();
        },
        'drop': function (e) {
            $('.file-upload-region').removeClass('file-upload-region--dragging');
            e.preventDefault();
            e.stopPropagation();
            var dataTransfer = e.originalEvent.dataTransfer;
            if (dataTransfer && dataTransfer.files.length) {
                e.preventDefault();
                e.stopPropagation();
                var files = [];
                for (var i = 0; i < dataTransfer.files.length; i++) {
                    files.push(dataTransfer.files[i]);
                }

                if (files.length > 0) {
                    if (files.length > 0) {
                        console.log(files.length);
                        console.log("INSERT");
                        fileUploadManager.insertFiles(fileUploadManager.prepareFiles(files));

                        if (cropEditorEnabled) {
                            for (var i = 0; i < fileUploadManager.files.length; i++) {
                                var fileInput = fileUploadManager.files[i].fileInput;
                                readAsDataUrl(i, fileInput, function (fileIndex, imageData) {
                                    fileUploadManager.files[fileIndex].imageData = imageData;
                                    var tabResult = addTab(fileIndex);
                                    fileUploadManager.files[fileIndex].domElements.$tabNavigationItem = tabResult.$tabNavigationItem;
                                    fileUploadManager.files[fileIndex].domElements.$tabPane = tabResult.$tabPane;
                                })
                            }
                        }
                    }

                }
            }
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
