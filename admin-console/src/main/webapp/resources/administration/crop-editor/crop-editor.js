$(document).ready(function () {

    if ($('.image_cropper_enabled').length === 1) {

        // Global configuration for crop editor.
        var store = [];
        var allowedFileTypes = [
            "image/jpeg",
            "image/png",
            "image/gif"
        ];
        var isCropEditorModalShown = false;
        var pendingNewStoreItems = [];


        var $imageNav;
        var $tabContent;
        var $newTabNavigationItem;
        var $newTabPane;
        var $newImage;


        var save = function (storeItem) {
            for (var i in store) {
                if (store[i].id === storeItem.id) {
                    // Replace storeItem with updated one.
                    store[i] = storeItem;
                    DOMStoreItemUpdated(storeItem);
                    return;

                }
            }

            // Add new storeItem.
            store.push(storeItem);

            DOMStoreItemCreated(storeItem);

        };


        $('.image-upload-form').on('change', 'input:file', function () {
            var input = this;

            if (input.files && input.files[0] && allowedFileTypes.indexOf(input.files[0].type) !== -1) {
                $('.bs-cropping-modal').modal('show');

                var reader = new FileReader();

                reader.onload = function (e) {
                    // Check if currently modified storeItem already exists.
                    var storeItemId = $(input).attr('id').split('_')[1];
                    save({
                        id: storeItemId,
                        name: input.files[0].name,
                        imageData: e.target.result,
                        type: input.files[0].type
                    });
                };

                reader.readAsDataURL(input.files[0]);
            }
        });

        var $cropEditorModal = $('.bs-cropping-modal');

        $cropEditorModal.on('shown.bs.modal', function () {
            isCropEditorModalShown = true;

            if ($imageNav !== undefined) {
                $imageNav.find('.active').removeClass('active');
            }

            if ($tabContent !== undefined) {
                $tabContent.find('.active').removeClass('active');
            }

            // Add active statest to newly created tab and nav item
            $newTabNavigationItem.addClass('active');
            $newTabPane.addClass('active');
            processPending();
        });

        $cropEditorModal.on('hidden.bs.modal', function () {
            isCropEditorModalShown = false;
        });


        $cropEditorModal.on('click', '.btn', function () {
            var $btn = $(this);

            switch ($btn.data('method')) {
                case 'crop':

                    if ($('.singleImageUpload').length === 1) {
                        var currentStoreItem = getCurrentStoreItem();
                        var imageData = "";
                        if (currentStoreItem) {
                            imageData = currentStoreItem.cropper.getCroppedCanvas().toDataURL(currentStoreItem.type);
                            currentStoreItem.cropper.replace(imageData);
                            currentStoreItem.imageData = imageData;
                            save(currentStoreItem);
                        }

                        DOMToastSuccess("Image cropped!");
                    } else {
                        DOMCropCreated();
                        save(setupNewStoreItem());
                    }
                    break;
                case 'remove':
                    remove(getCurrentStoreItemId());
                    break;
                case 'setDragMode':
                    getCurrentStoreItem().cropper.setDragMode($btn.data('option'));
                    break;
                case 'zoom':
                    getCurrentStoreItem().cropper.zoom($btn.data('option'));
                    break;
                case 'scaleX':
                    var option = $btn.data('option');
                    getCurrentStoreItem().cropper.scaleX(option);
                    $btn.data('option', -1 * option);
                    break;
                case 'scaleY':
                    var option = $btn.data('option');
                    getCurrentStoreItem().cropper.scaleY(option);
                    $btn.data('option', -1 * option);
                    break;
                case 'move':
                    getCurrentStoreItem().cropper.move($btn.data('option'), $btn.data('second-option'));
                    break;
                case 'rotate':
                    getCurrentStoreItem().cropper.rotate($btn.data('option'));
                    break;
                case 'setAspectRatio':
                    getCurrentStoreItem().cropper.setAspectRatio($btn.data('option'));
                    break;

                default:
                    return true;
            }
        });


        var getCurrentStoreItemId = function () {
            var currentStoreItemId = 0;
            if (store.length > 0) {
                currentStoreItemId = $('.bs-cropping-modal').find('.tab-pane.active').data('itemId');
            }

            return currentStoreItemId;
        };

        var getCurrentStoreItem = function () {
            var currentStoreItemId = getCurrentStoreItemId();

            var result = {};
            for (var i in store) {
                var storeItemI = store[i];
                if (parseInt(storeItemI.id) === currentStoreItemId) {
                    result = storeItemI;
                }

            }

            return result;
        };

        var setupCropper = function (storeItemId) {
            // Return cropper created for specific storeItem.
            return new Cropper(
                $('.store_item_' + storeItemId)[0],
                {
                    maxWidth: 300,
                    maxHeight: 300,
                    aspectRatio: 16 / 9,
                    preview: $('#store_item_' + storeItemId).find('.img-preview')
                });
        };


        var setupNewStoreItem = function (name) {
            var newId = generateNewStoreItemId();
            var currentStoreItem = getCurrentStoreItem();


            var imageData = "";
            if (currentStoreItem) {
                imageData = currentStoreItem.cropper.getCroppedCanvas().toDataURL(currentStoreItem.type);
            }

            if (!name) {
                var nameParts = currentStoreItem.name.split(/\.(?=[^\.]+$)/);
                name = nameParts[0] + "_" + newId + "." + nameParts[1]

            }

            return {
                id: newId,
                name: name,
                imageData: imageData,
                type: currentStoreItem.type
            };


        };

        var generateNewStoreItemId = function () {
            var highestIdInStore = 0;
            for (var i in store) {
                var currentStoreItemId = parseInt(store[i].id);
                if (currentStoreItemId > highestIdInStore) {
                    highestIdInStore = currentStoreItemId;
                }
            }

            highestIdInStore++;

            return highestIdInStore;
        };


        var remove = function (storeItemId) {

            for (var i in store) {
                if (store[i].id == storeItemId) {
                    if (store[i].hasOwnProperty('cropper')) {
                        store[i].cropper.destroy();
                    }

                    // Remove storeItem from store.
                    store.splice(i, 1);

                    removeTab(storeItemId);
                    removeHiddenFields(storeItemId);
                }
            }

            for (var j in pendingNewStoreItems) {
                if (pendingNewStoreItems[j].id == storeItemId) {
                    pendingNewStoreItems.splice(j, 1);
                }
            }

            removeField(storeItemId);
        };

        var addTab = function (storeItem) {
            $imageNav = $('.image-navigation');
            $tabContent = $('.bs-cropping-modal').find('.tab-content');

            // Remove active states
            // $imageNav.find('.active').removeClass('active');
            // $tabContent.find('.active').removeClass('active');


            // Copy image navigation item - tab navigation item
            $newTabNavigationItem = $('#image-navigation-item-blueprint').clone();
            $newTabNavigationItem.addClass('image-navigation-item');
            $newTabNavigationItem.find('a').attr('href', '#store_item_' + storeItem.id);
            $newTabNavigationItem.find('a').text(storeItem.name);
            $newTabNavigationItem.removeClass('hidden');
            $newTabNavigationItem.attr('id', 'image-navigation-item_' + storeItem.id);
            $newTabNavigationItem.appendTo($imageNav);

            // Copy tab pane
            $newTabPane = $('#tab-pane-blueprint').clone();
            $newTabPane.addClass('tab-pane');
            $newTabPane.removeClass('hidden');
            $newTabPane.attr('id', 'store_item_' + storeItem.id);
            $newTabPane.attr('data-item-id', storeItem.id);
            $newImage = $newTabPane.find('.store_item_');
            $newImage.attr('src', storeItem.imageData);
            $newImage.addClass('store_item_' + storeItem.id);
            $newTabPane.appendTo($tabContent);

            // Add active statest to newly created tab and nav item
            if (store.length === 1) {
                $newTabNavigationItem.addClass('active');
                $newTabPane.addClass('active');
            }


            if (store.length > 0) {
                pendingNewStoreItems.push({id: storeItem.id});
            }


        };


        var updateTab = function (storeItem) {
            $imageNav = $('.image-navigation');
            $tabContent = $('.bs-cropping-modal').find('.tab-content');

            // Remove active states
            $imageNav.find('.active').removeClass('active');
            $tabContent.find('.active').removeClass('active');


            // Copy image navigation item - tab navigation item
            $newTabNavigationItem = $('#image-navigation-item_' + storeItem.id);
            $newTabNavigationItem.find('a').attr('href', '#store_item_' + storeItem.id);
            $newTabNavigationItem.find('a').text(storeItem.name);
            $newTabNavigationItem.removeClass('hidden');
            $newTabNavigationItem.attr('id', 'image-navigation-item_' + storeItem.id);
            $newTabNavigationItem.appendTo($imageNav);

            // Copy tab pane
            $newTabPane = $('#store_item_' + storeItem.id);
            $newTabPane.removeClass('hidden');
            $newTabPane.attr('id', 'store_item_' + storeItem.id);
            $newTabPane.attr('data-item-id', storeItem.id);
            $newImage = $newTabPane.find('.store_item_');
            $newImage.attr('src', storeItem.imageData);
            $newImage.addClass('store_item_' + storeItem.id);
            $newTabPane.appendTo($tabContent);

            // Add active statest to newly created tab and nav item
            $newTabNavigationItem.addClass('active');
            $newTabPane.addClass('active');


            store = store.map(function (storeItemI) {
                if (storeItemI.id === storeItem.id) {
                    storeItemI.cropper.destroy();
                }

                return storeItemI;
            });

            pendingNewStoreItems.push({id: storeItem.id});

        };

        var removeTab = function (storeItemId) {
            var $tabPaneToRemove = $('#store_item_' + storeItemId);
            var $imageNavItemToRemove = $('#image-navigation-item_' + storeItemId);

            var $nextTabPane = $tabPaneToRemove.next();
            var $nextImageNavItem = $imageNavItemToRemove.next('.image-navigation-item');

            $tabPaneToRemove.remove();
            $imageNavItemToRemove.remove();


            if ($nextTabPane.length > 0 && $nextImageNavItem.length > 0) {

                $nextTabPane.addClass('active');
                $nextImageNavItem.addClass('active');
            } else {
                $('.image-navigation-item').last().addClass('active');
                $('.tab-pane').last().addClass('active');
            }
            processPending();

        };

        var removeField = function (storeItemId) {
            $('#descr_' + storeItemId).closest('.form-group').remove();
        };

        var removeHiddenFields = function (storeItemId) {
            $('#base64_image_' + storeItemId).remove();
            $('#file_upload_content_type_' + storeItemId).remove();
            $('#file_upload_name_' + storeItemId).remove();
        };


        var addFields = function () {
            var template = $('#hidden-fields-template').html();

            $('#fields-container').append(template);

            var newId = generateNewStoreItemId();

            $('#newDescr').attr("name", "descr_" + newId);
            $('#newDescr').attr("id", "descr_" + newId);

            $('#newImg').attr("id", "img_" + newId);

            $('#newFileUpload_label').attr("for", "fileUpload_" + newId);
            $('#newFileUpload_label').attr("id", "fileUpload_label_" + newId);

            $('#newFileUpload_selected').attr("id", "fileUpload_" + newId + "_selected");
            $('#newFileUpload').attr("id", "fileUpload_" + newId);

            $('#newFileUpload_box').attr("id", "fileUpload_box_" + newId);
        };

        $('#add-fields').click(function (e) {
            e.preventDefault();
            addFields();
            // var storeItem = setupNewStoreItem(true, "image.jpg");
            // pendingNewStoreItems.push(storeItem.id);
            //
            // save(storeItem);
        });

        $('#save').on('click', '.edit-fields', function (e) {
            e.preventDefault();
            $('.bs-cropping-modal').modal('show');

            var storeItemId = $(this).closest('.form-group').find('.file-description').attr('id').split('_')[1];

            $imageNav = $('.image-navigation');
            $tabContent = $('.bs-cropping-modal').find('.tab-content');


            $newTabPane = $('#store_item_' + storeItemId);
            $newTabNavigationItem = $('#image-navigation-item_' + storeItemId);


        });


        $('#fields-container').on("click", ".delete-fields", function (e) {
            e.preventDefault();
            var $fieldContainer = $(this).closest('.form-group');
            var storeItemId = $fieldContainer.find('.file-description').attr('id').split('_')[1];
            remove(storeItemId);

        });

        $('.bs-cropping-modal').on('click', '.image-navigation-item', function () {
            $clickedNavigationItem = $(this);


        });

        $('.nav-tabs').on('shown.bs.tab', '[data-toggle="tab"]', function () {
            processPending();
        });


        var processPending = function () {

            var currentStoreItem = getCurrentStoreItem();


            for (var i in pendingNewStoreItems) {
                var storeItemI = pendingNewStoreItems[i];

                if (currentStoreItem.id === storeItemI.id) {
                    for (var j in store) {
                        var storeItemJ = store[j];

                        if (storeItemI.id === storeItemJ.id) {
                            currentStoreItem.cropper = setupCropper(storeItemJ.id);
                            store[j] = currentStoreItem;
                            pendingNewStoreItems.splice(i, 1);
                        }
                    }

                }
            }
        };


        /*
         * DOM Manipulation functions.
         */
        var DOMStoreItemCreated = function (storeItem) {

            // Perform DOM manipulations.
            addTab(storeItem);

            $('#descr_' + storeItem.id).val(storeItem.name);
            $('#img_' + storeItem.id).attr("src", storeItem.imageData);
            $('.image-upload-form').append('<input type="hidden" name="base64Image" id="base64_image_' + storeItem.id + '" value="' + storeItem.imageData + '">');
            $('.image-upload-form').append('<input type="hidden" name="fileUploadBase64ImageContentType" id="file_upload_content_type_' + storeItem.id + '" value="' + storeItem.type + '">');
            $('.image-upload-form').append('<input type="hidden" name="fileUploadBase64ImageFileName" id="file_upload_name_' + storeItem.id + '" value="' + storeItem.name + '">');

        };

        var DOMCropCreated = function () {
            addFields();
            DOMToastSuccess();
        };

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


        var DOMStoreItemUpdated = function (storeItem) {

            if ($('.singleImageUpload').length !== 1) {
                // Perform DOM manipulations.
                updateTab(storeItem);
            }


            $('#descr_' + storeItem.id).val(storeItem.name);
            $('#img_' + storeItem.id).attr("src", storeItem.imageData);
            $('#base64_image_' + storeItem.id ).val(storeItem.imageData);
            $('#file_upload_content_type_' + storeItem.id ).val(storeItem.type);
            $('#file_upload_name_' + storeItem.id ).val(storeItem.name);
        };

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

        DOMsetupAspectRatioToolbar();

    }

});