$(document).ready(function () {
    var allowedFileTypes = [
        "image/jpeg",
        "image/png",
        "image/gif",
    ];
    var isCropEditorModalShown = false;
    var pendingStoreItems = [];
    $('.image-upload-form').on('change', 'input:file', function () {
        var input = this;

        if (input.files && input.files[0] && allowedFileTypes.includes(input.files[0].type)) {
            $('.bs-cropping-modal').modal('show');

            var reader = new FileReader();

            reader.onload = function (e) {
                // Creating first storeItem from original photo.
                var storeItem = setupNewStoreItem(true, input.files[0].name);
                storeItem.imageData = e.target.result;
                storeItem.type = input.files[0].type;
                save(storeItem);
            };

            reader.readAsDataURL(input.files[0]);
        }
    });

    var $cropEditorModal = $('.bs-cropping-modal');

    $cropEditorModal.on('shown.bs.modal', function () {
        isCropEditorModalShown = true;
        pendingStoreItems.map(function (storeItemId) {
            store[storeItemId].cropper = setupCropper(storeItemId);
        });
        pendingStoreItems = [];
        // store[getCurrentStoreItemId()].cropper = setupCropper(getCurrentStoreItemId());
    });

    $cropEditorModal.on('hidden.bs.modal', function () {
        isCropEditorModalShown = false;
    });


    $cropEditorModal.on('click', '.btn', function () {
        switch ($(this).data('method')) {
            case 'crop':
                save(setupNewStoreItem(false));
                break;
            case 'remove':
                remove(getCurrentStoreItemId());
                break;
            case 'setDragMode':
                getCurrentStoreItem().cropper.setDragMode($(this).data('option'));
                break;
            case 'zoom':
                getCurrentStoreItem().cropper.zoom($(this).data('option'));
                break;
            case 'scaleX':
                getCurrentStoreItem().cropper.scaleX($(this).data('option'));
                break;
            case 'scaleY':
                var option = $(this).data('option');
                getCurrentStoreItem().cropper.scaleY();
                $(this).data('option', -option)
                break;
            case 'move':
                getCurrentStoreItem().cropper.move($(this).data('option'), $(this).data('second-option'));
                break;
            case 'rotate':
                getCurrentStoreItem().cropper.rotate($(this).data('option'));
                break;
            case 'setAspectRatio':
                getCurrentStoreItem().cropper.setAspectRatio($(this).data('option'));
                break;

            default:
                return true;
        }
    });


    var store = [];

    var getCurrentStoreItemId = function () {
        var currentStoreItemId = 0;
        if (store.length > 0) {
            currentStoreItemId = $('.bs-cropping-modal').find('.tab-pane.active').data('itemId');
        }

        return currentStoreItemId;
    };

    var getCurrentStoreItem = function () {
        return store[getCurrentStoreItemId()];
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


    var setupNewStoreItem = function (isInitial, name) {
        var currentItemId = getCurrentStoreItemId();
        var newId = store.length;
        if (isInitial) {
            return {
                id: newId,
                name: name
            };

        } else {
            var nameParts = store[currentItemId].name.split(/\.(?=[^\.]+$)/);
            return {
                id: newId,
                name: nameParts[0] + "_" + newId + "." + nameParts[1],
                imageData: store[currentItemId].cropper.getCroppedCanvas().toDataURL('image/png')
            };
        }
    };

    var save = function (storeItem) {
        store.push(storeItem);
        addTab(storeItem);

        if (store.length > 1) {
            addFields();
        }
        $('#descr_' + storeItem.id).val(storeItem.name);
        $('#img_' + storeItem.id).attr("src", storeItem.imageData);
        $('.image-upload-form').append('<input type="hidden" name="base64Image" id="bas64_image_' + storeItem.id + '" value="' + storeItem.imageData + '">')
        $('.image-upload-form').append('<input type="hidden" name="fileUploadBase64ImageContentType" id="file_upload_content_type_' + storeItem.id + '" value="' + storeItem.type + '">')
        $('.image-upload-form').append('<input type="hidden" name="fileUploadBase64ImageFileName" id="file_upload_name_' + storeItem.id + '" value="' + storeItem.name + '">')
    };

    var remove = function (storeItemId) {
        store[storeItemId].cropper.destroy();
        store.splice(storeItemId, 1);
        removeTab(storeItemId);
        removeField(storeItemId);
        removeHiddenFields(storeItemId);

    };

    var addTab = function (storeItem) {
        var $imageNav = $('.image-navigation');
        var $tabContent = $('.bs-cropping-modal').find('.tab-content');

        // Remove active states
        $imageNav.find('.active').removeClass('active');
        $tabContent.find('.active').removeClass('active');


        // Copy image navigation item - tab navigation item
        var $newTabNavigationItem = $('#image-navigation-item-blueprint').clone();
        $newTabNavigationItem.find('a').attr('href', '#store_item_' + storeItem.id);
        $newTabNavigationItem.find('a').text(storeItem.name);
        $newTabNavigationItem.removeClass('hidden');
        $newTabNavigationItem.attr('id', 'image-navigation-item_' + storeItem.id);
        $newTabNavigationItem.appendTo($imageNav);

        // Copy tab pane
        var $newTabPane = $('#tab-pane-blueprint').clone();
        $newTabPane.removeClass('hidden');
        $newTabPane.attr('id', 'store_item_' + storeItem.id);
        $newTabPane.attr('data-item-id', storeItem.id);
        var $newImage = $newTabPane.find('.store_item_');
        $newImage.attr('src', storeItem.imageData);
        $newImage.addClass('store_item_' + storeItem.id);
        $newTabPane.appendTo($tabContent);

        // Add active statest to newly created tab and nav item
        $newTabNavigationItem.addClass('active');
        $newTabPane.addClass('active');

        if (store.length > 0) {
            if (isCropEditorModalShown) {
                store[storeItem.id].cropper = setupCropper(storeItem.id);
            } else {
                pendingStoreItems.push(storeItem.id);
            }
        }


    };

    var removeTab = function (storeItemId) {
        $('#store_item_' + storeItemId).remove();
        $('#image-navigation-item_' + storeItemId).remove();
        $('.image-navigation-item').last().addClass('.active');
    };

    var removeField = function (storeItemId) {
        $('#descr_' + storeItemId).closest('.form-group').remove();
    };

    var removeHiddenFields = function (storeItemId) {
        $('#bas64_image_' + storeItemId).remove();
        $('#file_upload_content_type_' + storeItemId).remove();
        $('#file_upload_name_' + storeItemId).remove();
    };


    var addFields = function () {
        var numItems = $('.file-description').length;
        var template = $('#hidden-fields-template').html();

        $('#fields-container').append(template);

        var newId = parseInt(numItems);

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
    });


    $('#fields-container').on("click", ".delete-fields", function (e) {
        e.preventDefault();
        $fieldContainer = $(this).parent('div');
        var storeItemId = $fieldContainer.find('.file-description').attr('id').split('_')[1];
        remove(storeItemId);

    });

    $('form#save').on("keyup", ".file-description", function (e) {
        if(isDescriptionsFilled()) {
            $('#submit').removeAttr('disabled');
        } else {
            $('#submit').attr('disabled', 'disabled');
        }
    });

    var isDescriptionsFilled = function () {
        var isFilled = true;
        $('.file-description').each(function (index) {
            if ($(this).val().length === 0) {
                isFilled = false;
                return false;
            }
        });

        return isFilled;
    }

});