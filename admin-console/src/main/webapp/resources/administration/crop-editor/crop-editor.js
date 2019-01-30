var Store;

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

$(document).ready(function () {
    var cropEditorEnabled = ($('.image_cropper_enabled').length === 1);
    var attachmentUploadEnabled = ($('.attachment_upload_enabled').length === 1);

    /**
     * Checking if either crop editor or at least attachment upload form related functionality should be enabled.
     */
    if (cropEditorEnabled || attachmentUploadEnabled) {

        /**
         * Store - object which stores all store items.
         * storeItem - object representing currently available store items in Store.
         *
         * Store/storeItem structure.
         * {
         *     items: {
         *       item_1: {
         *         id: 1,
         *         type: "file type",
         *         name: "store item name",
         *         imageData: "base64string",
         *         active: Bool -
         *         $fieldGroup: jQuery reference to store item field group in DOM,
         *         $tabPane: jQuery reference to store item tab pane in modal window,
         *         $tabNavigationItem: jQuery reference to tab navigation item in modal window,
         *       }
         *     }
         *     lastStoreItemId: 0,
         *
         * }
         *
         *
         */
        Store = {
            items: {},
            lastStoreItemId: false
        };

        var createStoreItem = function(element) {
            var newStoreItem = {
                id: getNewStoreItemId(),
                $fieldGroup: $(element)
            };

            saveNewStoreItem(newStoreItem);
        };

        /**
         * Store management related utility functions.
         */

        /** jQuery reference to fields-container. */
        var $fieldsContainer = $('#fields-container');

        // Setting up initial Store items according to file upload input fields rendered initially.
        var setupInitialStoreItems = function () {
            $fieldsContainer.find('.form-group ').each(function (i, element) {
                createStoreItem(element);
            });
        };

        // Retrieve new store item by incrementing to last one.
        var getNewStoreItemId = function () {
            var newStoreItemId = 0;
            if (Store.lastStoreItemId !== false) {
                newStoreItemId = Store.lastStoreItemId + 1;
            }

            return newStoreItemId;
        };

        // Retrieve store item using storeItemId.
        var getStoreItem = function (storeItemId) {
            return Store.items["item_" + storeItemId];
        };

        // Mutate Store objec to add newStoreItem.
        var saveNewStoreItem = function (newStoreItem) {
            // Update last storeItemId value.
            Store.lastStoreItemId = newStoreItem.id;
            Store.items["item_" + newStoreItem.id] = newStoreItem;
            newStoreItem.$fieldGroup.attr('data-store-item-id', newStoreItem.id);
        };

        // Update storeItem with new one and perform related DOM manipulations.
        var updateStoreItem = function (storeItem) {
            Store.items["item_" + storeItem.id] = storeItem;

            // Update file description input fields with file name.
            if (storeItem.name) {
                storeItem.$fieldGroup.find('.file-description').val(storeItem.name);
                storeItem.$fieldGroup.find('.file-upload-selected-name').text(storeItem.name);
            }

            if (cropEditorEnabled) {
                if (!storeItem.$tabPane && !storeItem.$tabNavigationItem) {
                    var newTab = addTab(storeItem);
                    storeItem.$tabPane = newTab.$newTabPane;
                    storeItem.$tabNavigationItem = newTab.$newTabNavigationItem;
                }
            }

            Store.items["item_" + storeItem.id] = setupHiddenFields(storeItem);

        };

        // Setup file for storeItem
        var setupStoreItemFile = function (storeItemId, file) {
            var storeItem = getStoreItem(storeItemId);
            storeItem.name = file.name;
            storeItem.type = file.type;
            updateStoreItem(storeItem);


            var reader = new FileReader();

            reader.onload = function (e) {
                storeItem.name = file.name;
                storeItem.type = file.type;
                storeItem.imageData = e.target.result;

                updateStoreItem(storeItem);
            };

            reader.readAsDataURL(file);
        };

        // Add fileUpload form field group to the form.
        var addFields = function () {
            var newStoreItem = {
                id: getNewStoreItemId()
            };

            var $template = $($('#hidden-fields-template').html());


            $template.find('#newDescr').attr("name", "descr_" + newStoreItem.id);
            $template.find('#newDescr').attr("id", "descr_" + newStoreItem.id);

            $template.find('#newImg').attr("id", "img_" + newStoreItem.id);

            $template.find('#newFileUpload_label').attr("for", "fileUpload_" + newStoreItem.id);
            $template.find('#newFileUpload_label').attr("id", "fileUpload_label_" + newStoreItem.id);

            $template.find('#newFileUpload_selected').attr("id", "fileUpload_" + newStoreItem.id + "_selected");
            $template.find('#newFileUpload').attr("id", "fileUpload_" + newStoreItem.id);



            var progress = '<div class="progress" id="progress_' + newStoreItem.id + '">\n' +
                '               <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">\n' +
                '                   <span>0%</span>\n' +
                '               </div>\n' +
                '           </div>';

            $template.find('.fileUpload-right').append(progress);

            $template.find('#newFileUpload_box').append('<input type="hidden" name="fileUploadId_' + newStoreItem.id + '" maxlength="500" value="" id="fileUploadId_' + newStoreItem.id + '" class="form-control">');
            $template.find('#newFileUpload_box').append('<input type="hidden" name="fileUploadName_' + newStoreItem.id + '" maxlength="500" value="" id="fileUploadName_' + newStoreItem.id + '" class="form-control">');
            $template.find('#newFileUpload_box').append('<input type="hidden" name="fileUploadContentType_' + newStoreItem.id + '" maxlength="500" value="" id="fileUploadContentType_' + newStoreItem.id + '" class="form-control">');

            $template.find('#newFileUpload_box').attr("id", "fileUpload_box_" + newStoreItem.id);


            newStoreItem.$fieldGroup = $template.appendTo($fieldsContainer);

            var fileInput = document.getElementById("fileUpload_" + newStoreItem.id);
            fileInput.addEventListener('change', collectFiles, false);


            saveNewStoreItem(newStoreItem);

            return newStoreItem;
        };

        // Remove storeItem related hidden fields.
        // This function should be used when storeItem is completely removed or updated with remove old, create new approach.
        var removeHiddenFields = function (storeItem) {
            // Remove previously setup hidden fields.
            if (storeItem.hiddenFields !== undefined) {
                if (storeItem.hiddenFields.$base64Image) {
                    storeItem.hiddenFields.$base64Image.remove();
                }
                if (storeItem.hiddenFields.$fileUploadBase64ImageContentType) {
                    storeItem.hiddenFields.$fileUploadBase64ImageContentType.remove();
                }
                if (storeItem.hiddenFields.$fileUploadBase64ImageFileName) {
                    storeItem.hiddenFields.$fileUploadBase64ImageFileName.remove();
                }
            }
        };

        // Setup storeItem related base64 hidden fields
        var setupHiddenFields = function (storeItem) {
            var imageUploadForm = $('.image-upload-form');
            // Remove previously setup hidden fields.
            removeHiddenFields(storeItem);

            // Add hidden fields with new values.
            storeItem.hiddenFields = {
                $base64Image: $('<input type="hidden" name="base64Image" id="base64_image_' + storeItem.id + '" value="' + storeItem.imageData + '">').appendTo(imageUploadForm),
                $fileUploadBase64ImageContentType: $('<input type="hidden" name="fileUploadBase64ImageContentType" id="file_upload_content_type_' + storeItem.id + '" value="' + storeItem.type + '">').appendTo(imageUploadForm),
                $fileUploadBase64ImageFileName: $('<input type="hidden" name="fileUploadBase64ImageFileName" id="file_upload_name_' + storeItem.id + '" value="' + storeItem.name + '">').appendTo(imageUploadForm)
            };

            return storeItem;
        };

        // Delete storeItem from Store using storeItemId.
        var deleteStoreItem = function (storeItemId) {
            var storeItem = getStoreItem(storeItemId);
            storeItem.$fieldGroup.remove();

            if (cropEditorEnabled) {
                var $nextTabPane;
                var $nextImageNavItem;

                if (storeItem.$tabPane) {
                    $nextTabPane = storeItem.$tabPane.next();
                    storeItem.$tabPane.remove()
                }

                if (storeItem.$tabNavigationItem) {
                    $nextImageNavItem = storeItem.$tabNavigationItem.next('.image-navigation-item');
                    storeItem.$tabNavigationItem.remove();
                }

                if ($nextTabPane.length > 0 && $nextImageNavItem.length > 0) {
                    $nextTabPane.addClass('active');
                    $nextImageNavItem.addClass('active');
                } else {
                    $('.image-navigation-item').last().addClass('active');
                    $('.tab-pane').last().addClass('active');
                }
            }

            removeHiddenFields(storeItem);
            delete Store.items["item_" + storeItemId];
        };

        // Shows success toast message.
        var DOMToastSuccess = function (message) {
            var $toast = $('.toast-success-blueprint').clone();

            if (message) {
                $toast.find('.toast-message').text(message);
            }

            $('body').append($toast);
            $toast.fadeIn();
            $toast.removeClass('hidden');

            setTimeout(function () {
                $toast.fadeOut(function () {
                    $toast.remove();
                });
            }, 2000)

        };

        // Parses `#aspect-ratio-values` element and retrieves toolbar values to setup aspect-ratio toolbar.
        var DOMsetupAspectRatioToolbar = function () {
            var defaultAspectRatios = $('#aspect-ratio-values').text().trim().split(";");
            var $aspectRatioToolbar = $('.aspect-ratio-buttons');

            var render = function (val) {
                var aspectRatioValues = val.split(':');
                var aspectRatio = aspectRatioValues[0] / aspectRatioValues[1];
                var template =
                    '<label class="btn btn-primary" data-method="setAspectRatio" data-option="' + aspectRatio + '">\n' +
                    '<input type="radio" class="sr-only" name="aspectRatio" value="' + aspectRatio + '">\n' +
                    '<span class="docs-tooltip">' + val + '</span>\n' +
                    '</label>';

                return template;
            };

            for (var i in defaultAspectRatios) {
                if (defaultAspectRatios[i].length > 0) {
                    $aspectRatioToolbar.find('.btn-group').append(render(defaultAspectRatios[i]));
                }
            }
        };

        // Listen to add fields button click events and action on it.
        $('#add-fields').on('click', function (e) {
            e.preventDefault();
            addFields();
        });

        // Listen to file input change events and process files.
        $('.image-upload-form').on('change', 'input:file', function () {
            var input = this;

            if (input.files) {
                for (var i = 0; i < input.files.length; i++) {
                    // Check if allowed file types are uploaded.
                    if (cropEditorEnabled && Store.allowedFileTypes.indexOf(input.files[i].type) === -1) return;

                    var storeItemId = $(input).closest('.form-group').data('storeItemId');
                    // If multiple files, create new field for each that is not first file.
                    if (i !== 0) {
                        storeItemId = addFields().id;
                    }

                    setupStoreItemFile(storeItemId, input.files[i]);
                }
            }

        });

        // Listen to delete fields kebab menu item click events
        // and delete related storeItem in modal.
        $fieldsContainer.on("click", ".delete-fields", function (e) {
            e.preventDefault();
            var storeItemId = parseInt($(this).closest('.form-group').data('storeItemId'));
            deleteStoreItem(storeItemId);
        });

        $(document).on({
            'dragover dragenter': function (e) {
                e.preventDefault();
                e.stopPropagation();
            },
            'drop': function (e) {
                e.preventDefault();
                e.stopPropagation();
                var dataTransfer = e.originalEvent.dataTransfer;
                if (dataTransfer && dataTransfer.files.length) {
                    e.preventDefault();
                    e.stopPropagation();
                    $.each(dataTransfer.files, function (i, file) {

                        var storeItemId = 0;

                        if (i !== 0) {
                            storeItemId = addFields().id;
                        }

                        setupStoreItemFile(storeItemId, file);
                    });
                }
            }
        });


        /**
         * Crop editor.
         */
        if (cropEditorEnabled) {
            Store.allowedFileTypes = [
                "image/jpeg",
                "image/png",
                "image/gif"
            ];

            // Listen to edit fields kebab menu item click events
            // and open related storeItem in modal.
            $fieldsContainer.on('click', '.edit-fields', function (e) {
                e.preventDefault();

                var storeItemId = parseInt($(this).closest('.form-group').data('storeItemId'));
                var storeItem = getStoreItem(storeItemId);

                // Remove previously active tab state.
                $('.bs-cropping-modal').find('.active').removeClass('active');

                // Add new active tab state;
                storeItem.$tabPane.addClass('active');
                storeItem.$tabNavigationItem.addClass('active');

                $('.bs-cropping-modal').modal('show');

            });


            // jQuery reference to crop editor modal window.
            var $cropEditorModal = $('.bs-cropping-modal');

            $('.nav-tabs').on('shown.bs.tab', '[data-toggle="tab"]', function () {
                var storeItemId = parseInt($(this).closest('.image-navigation-item').data('storeItemId'));
                var storeItem = getStoreItem(storeItemId);
                if (!storeItem.hasOwnProperty('cropper')) {
                    setupCropper(storeItemId);
                }
            });

            $cropEditorModal.on('shown.bs.modal', function () {
                var storeItemId = parseInt($(this).find('.tab-pane.active').data('storeItemId'));
                var storeItem = getStoreItem(storeItemId);
                if (!storeItem.hasOwnProperty('cropper')) {
                    setupCropper(storeItemId);
                }
            });

            // Listen on crop editor toolbar buttons clicks and action accordingly.
            $cropEditorModal.on('click', '.btn', function () {
                var $btn = $(this);
                var currentStoreItemId = parseInt($(this).closest('.tab-pane').data('storeItemId'));
                var currentStoreItem = getStoreItem(currentStoreItemId);

                switch ($btn.data('method')) {
                    case 'crop':

                        if ($('.singleImageUpload').length === 1) {
                            var imageData = "";
                            if (currentStoreItem) {
                                imageData = currentStoreItem.cropper.getCroppedCanvas().toDataURL(currentStoreItem.type);
                                currentStoreItem.cropper.replace(imageData);
                                currentStoreItem.imageData = imageData;
                                updateStoreItem(currentStoreItem);
                            }

                            // DOMToastSuccess("Image cropped!");
                        } else {
                            var newStoreItem = addFields();

                            var nameParts = currentStoreItem.name.split(/\.(?=[^\.]+$)/);
                            newStoreItem.name = nameParts[0] + "_" + newStoreItem.id + "." + nameParts[1];

                            newStoreItem.imageData = currentStoreItem.cropper.getCroppedCanvas().toDataURL(currentStoreItem.type);

                            updateStoreItem(newStoreItem);
                        }

                        DOMToastSuccess("Crop created!");

                        break;
                    case 'remove':
                        deleteStoreItem(currentStoreItemId);
                        break;
                    case 'setDragMode':
                        currentStoreItem.cropper.setDragMode($btn.data('option'));
                        break;
                    case 'zoom':
                        currentStoreItem.cropper.zoom($btn.data('option'));
                        break;
                    case 'scaleX':
                        var option = $btn.data('option');
                        currentStoreItem.cropper.scaleX(option);
                        $btn.data('option', -1 * option);
                        break;
                    case 'scaleY':
                        var option = $btn.data('option');
                        currentStoreItem.cropper.scaleY(option);
                        $btn.data('option', -1 * option);
                        break;
                    case 'move':
                        currentStoreItem.cropper.move($btn.data('option'), $btn.data('second-option'));
                        break;
                    case 'rotate':
                        currentStoreItem.cropper.rotate($btn.data('option'));
                        break;
                    case 'setAspectRatio':
                        currentStoreItem.cropper.setAspectRatio($btn.data('option'));
                        break;

                    default:
                        return true;
                }
            });

            var addTab = function (storeItem) {
                var $imageNav = $('.image-navigation');
                var $tabContent = $('.bs-cropping-modal').find('.tab-content');

                // Copy image navigation item - tab navigation item
                var $newTabNavigationItem = $('#image-navigation-item-blueprint').clone();
                $newTabNavigationItem.addClass('image-navigation-item');
                $newTabNavigationItem.find('a').attr('href', '#store_item_' + storeItem.id);
                $newTabNavigationItem.find('a').text(storeItem.name);
                $newTabNavigationItem.removeClass('hidden');
                $newTabNavigationItem.attr('id', 'image-navigation-item_' + storeItem.id);
                $newTabNavigationItem.attr('data-store-item-id', storeItem.id);

                // Copy tab pane
                var $newTabPane = $('#tab-pane-blueprint').clone();
                $newTabPane.addClass('tab-pane');
                $newTabPane.removeClass('hidden');
                $newTabPane.attr('id', 'store_item_' + storeItem.id);
                $newTabPane.attr('data-item-id', storeItem.id);
                $newTabPane.attr('data-store-item-id', storeItem.id);
                var $newImage = $newTabPane.find('.store_item_');
                $newImage.attr('src', storeItem.imageData);
                $newImage.addClass('store_item_' + storeItem.id);

                return {
                    $newTabNavigationItem: $newTabNavigationItem.appendTo($imageNav),
                    $newTabPane: $newTabPane.appendTo($tabContent)
                }
            };

            var setupCropper = function (storeItemId) {
                var storeItem = getStoreItem(storeItemId);
                storeItem.cropper = new Cropper(
                    $('.store_item_' + storeItemId)[0],
                    {
                        maxWidth: 300,
                        maxHeight: 300,
                        aspectRatio: 16 / 9,
                        preview: $('#store_item_' + storeItemId).find('.img-preview'),
                        crop: function (e) {
                            var actionedStoreItem = getStoreItem(storeItemId);
                            var data = e.detail;

                            actionedStoreItem.$tabPane.find('.dataX').val(Math.round(data.x));
                            actionedStoreItem.$tabPane.find('.dataY').val(Math.round(data.y));
                            actionedStoreItem.$tabPane.find('.dataHeight').val(Math.round(data.height));
                            actionedStoreItem.$tabPane.find('.dataWidth').val(Math.round(data.width));
                            actionedStoreItem.$tabPane.find('.dataRotate').val((typeof data.rotate !== 'undefined' ? data.rotate : ''));
                            actionedStoreItem.$tabPane.find('.dataScaleX').val((typeof data.scaleX !== 'undefined' ? data.scaleX : ''));
                            actionedStoreItem.$tabPane.find('.dataScaleY').val((typeof data.scaleY !== 'undefined' ? data.scaleY : ''));
                        }
                    });
                updateStoreItem(storeItem);
            };
        }

        // Setting up initial crop-editor.
        setupInitialStoreItems();
        DOMsetupAspectRatioToolbar();


        /**
         * Handling single image edit form.
         */

        var base64Image = undefined;
        var singleImageEdit = $('#imageUrl');

        // If this is single image edit form retrieve image, convert it to base64 string to add it to Store as storeItem.
        if (singleImageEdit.length === 1) {
            var imagePath = singleImageEdit.data('value');
            toDataUrl(imagePath, function (imageData) {
                base64Image = imageData;
                var storeItem = getStoreItem(0);

                storeItem.name = $('#descr_0').val();
                storeItem.imageData = imageData;

                storeItem.type = imageData.substring("data:".length, imageData.indexOf(";base64"));
                updateStoreItem(storeItem);
            });
        }
    }
});
