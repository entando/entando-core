console.log("init");
$(document).ready(function () {

    // Global configuration for crop editor.
    var store = [];
    var allowedFileTypes = [
        "image/jpeg",
        "image/png",
        "image/gif",
    ];
    var isCropEditorModalShown = false;
    var pendingStoreItems = [];

    // Listen to new store item created events.
    document.addEventListener("storeItemCreated", function (e) {
        DOMStoreItemCreated(e.detail.storeItem);
    });

    // Listen to new crop created events.
    document.addEventListener("cropCreated", function (e) {
        DOMCropCreated();

        save(e.detail.storeItem);
    });

    // Listen to update store item events.
    document.addEventListener("storeItemUpdated", function (e) {
        //TODO Implement update event actions.
    });


    var save = function (storeItem) {
        if (store.indexOf(storeItem.id) > -1) {
            // Replace storeItem with updated one.
            store[storeItem.id] = storeItem;

        } else {
            // Add new storeItem.
            store.push(storeItem);

            var storeItemCreatedEvent = new CustomEvent("storeItemCreated", {
                detail: {storeItem: storeItem}
            });
            document.dispatchEvent(storeItemCreatedEvent);

        }
    };


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
                document.dispatchEvent(
                    new CustomEvent("cropCreated", { detail: {storeItem: setupNewStoreItem(false) }})
                );
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
        var currentStoreItem = getCurrentStoreItem();
        var newId = store.length;
        if (isInitial) {
            return {
                id: newId,
                name: name
            };

        } else {
            var nameParts = currentStoreItem.name.split(/\.(?=[^\.]+$)/);
            return {
                id: newId,
                name: nameParts[0] + "_" + newId + "." + nameParts[1],
                imageData: currentStoreItem.cropper.getCroppedCanvas().toDataURL(currentStoreItem.type),
                type: currentStoreItem.type
            };
        }
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


    /*
     * DOM Manipulation functions.
     */
    var DOMStoreItemCreated = function (storeItem) {

        // Perform DOM manipulations.
        addTab(storeItem);

        $('#descr_' + storeItem.id).val(storeItem.name);
        $('#img_' + storeItem.id).attr("src", storeItem.imageData);
        $('.image-upload-form').append('<input type="hidden" name="base64Image" id="bas64_image_' + storeItem.id + '" value="' + storeItem.imageData + '">');
        $('.image-upload-form').append('<input type="hidden" name="fileUploadBase64ImageContentType" id="file_upload_content_type_' + storeItem.id + '" value="' + storeItem.type + '">');
        $('.image-upload-form').append('<input type="hidden" name="fileUploadBase64ImageFileName" id="file_upload_name_' + storeItem.id + '" value="' + storeItem.name + '">');

    };

    var DOMCropCreated = function () {
        addFields();
    };

});