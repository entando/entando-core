$(document).ready(function () {

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
     *     lastId: 0,
     *
     * }
     *
     *
     */
    var Store = {
        items: {},
        lastStoreItemId: false,
        allowedFileTypes: [
            "image/jpeg",
            "image/png",
            "image/gif"
        ],
        getNewStoreItemId: function () {
            var newStoreItemId = 0;
            if (this.lastStoreItemId !== false) {
                newStoreItemId = this.lastStoreItemId + 1;
            }

            return newStoreItemId;
        },
        saveNewStoreItem: function (newStoreItem) {
            this.lastStoreItemId = newStoreItem.id;
            this.items["item_" + newStoreItem.id] = newStoreItem;
            newStoreItem.$fieldGroup.attr('data-store-item-id', newStoreItem.id);
        },
        updateStoreItem: function (storeItem) {
            this.items["item_" + storeItem.id] = storeItem;

            // Update file description input fields with file name.
            if (storeItem.name) {
                storeItem.$fieldGroup.find('.file-description').val(storeItem.name);
            }

            if (!storeItem.$tabPane && !storeItem.$tabNavigationItem) {
                var newTab = addTab(storeItem);
                storeItem.$tabPane = newTab.$newTabPane;
                storeItem.$tabNavigationItem = newTab.$newTabNavigationItem;
            }

        },
        deleteStoreItem: function (storeItemId) {
            var storeItem = this.getStoreItem(storeItemId);
            storeItem.$fieldGroup.remove();
            if(storeItem.$tabPane){ storeItem.$tabPane.remove()}
            if(storeItem.$tabNavigationItem){ storeItem.$tabNavigationItem.remove()}
            delete this.items["item_" + storeItemId];
        },
        getStoreItem: function (storeItemId) {
            return this.items["item_" + storeItemId];
        },
        setupStoreItemFile: function (storeItemId, file) {
            var storeItem = this.getStoreItem(storeItemId);
            var reader = new FileReader();

            reader.onload = function (e) {
                storeItem.name = file.name;
                storeItem.type = file.type;
                storeItem.imageData = e.target.result;

                Store.updateStoreItem(storeItem);
            };

            reader.readAsDataURL(file);
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
                if (Store.allowedFileTypes.indexOf(input.files[i].type) !== -1) {
                    var storeItemId = $(input).closest('.form-group').data('storeItemId');
                    // If multiple files, create new field for each that is not first file.
                    if (i !== 0) {
                        storeItemId = addFields().id;
                    }

                    Store.setupStoreItemFile(storeItemId, input.files[i]);
                }
            }
        }
    });

    // jQuery reference to fields-container.
    var $fieldsContainer = $('#fields-container');

    // Listen to edit fields kebab menu item click events
    // and open related storeItem in modal.
    $fieldsContainer.on('click', '.edit-fields', function (e) {
        e.preventDefault();

        var storeItemId = parseInt($(this).closest('.form-group').data('storeItemId'));
        var storeItem = Store.getStoreItem(storeItemId);

        // Remove previously active tab state.
        $('.bs-cropping-modal').find('.active').removeClass('active');

        // Add new active tab state;
        storeItem.$tabPane.addClass('active');
        storeItem.$tabNavigationItem.addClass('active');

        $('.bs-cropping-modal').modal('show');

    });

    // Listen to delete fields kebab menu item click events
    // and delete related storeItem in modal.
    $fieldsContainer.on("click", ".delete-fields", function (e) {
        e.preventDefault();
        var storeItemId = parseInt($(this).closest('.form-group').data('storeItemId'));
        Store.deleteStoreItem(storeItemId);
    });

    // jQuery reference to crop editor modal window.
    var $cropEditorModal = $('.bs-cropping-modal');

    $('.nav-tabs').on('shown.bs.tab', '[data-toggle="tab"]', function () {
        var storeItemId = parseInt($(this).closest('.image-navigation-item').data('storeItemId'));
        var storeItem = Store.getStoreItem(storeItemId);
        if(!storeItem.cropper){
            setupCropper(storeItemId);
        }
    });

    $cropEditorModal.on('shown.bs.modal', function () {
        var storeItemId = parseInt($(this).find('.image-navigation-item.active').data('storeItemId'));
        var storeItem = Store.getStoreItem(storeItemId);
        if(!storeItem.cropper){
            setupCropper(storeItemId);
        }
    });

    // Listen on crop editor toolbar buttons clicks and action accordingly.
    $cropEditorModal.on('click', '.btn', function () {
        var $btn = $(this);
        var currentStoreItemId = parseInt($(this).closest('.tab-pane').data('storeItemId'));
        var currentStoreItem = Store.getStoreItem(currentStoreItemId);

        switch ($btn.data('method')) {
            case 'crop':

                if ($('.singleImageUpload').length === 1) {
                    var imageData = "";
                    if (currentStoreItem) {
                        imageData = currentStoreItem.cropper.getCroppedCanvas().toDataURL(currentStoreItem.type);
                        currentStoreItem.cropper.replace(imageData);
                        currentStoreItem.imageData = imageData;
                        Store.updateStoreItem(currentStoreItem);
                    }

                    // DOMToastSuccess("Image cropped!");
                } else {
                    var newStoreItem = addFields();

                    var nameParts = currentStoreItem.name.split(/\.(?=[^\.]+$)/);
                    newStoreItem.name = nameParts[0] + "_" + newStoreItem.id + "." + nameParts[1];

                    addTab(newStoreItem);
                }
                break;
            case 'remove':
                Store.deleteStoreItem(currentStoreItemId);
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

    var setupInitialStoreItems = function () {
        $fieldsContainer.find('.form-group ').each(function (i, element) {
            var newStoreItem = {
                id: Store.getNewStoreItemId(),
                $fieldGroup: $(element)
            };


            Store.saveNewStoreItem(newStoreItem);
        });
    };

    setupInitialStoreItems();


    var addFields = function () {
        var newStoreItem = {
            id: Store.getNewStoreItemId()
        };

        var $template = $($('#hidden-fields-template').html());


        $template.find('#newDescr').attr("name", "descr_" + newStoreItem.id);
        $template.find('#newDescr').attr("id", "descr_" + newStoreItem.id);

        $template.find('#newImg').attr("id", "img_" + newStoreItem.id);

        $template.find('#newFileUpload_label').attr("for", "fileUpload_" + newStoreItem.id);
        $template.find('#newFileUpload_label').attr("id", "fileUpload_label_" + newStoreItem.id);

        $template.find('#newFileUpload_selected').attr("id", "fileUpload_" + newStoreItem.id + "_selected");
        $template.find('#newFileUpload').attr("id", "fileUpload_" + newStoreItem.id);

        $template.find('#newFileUpload_box').attr("id", "fileUpload_box_" + newStoreItem.id);


        newStoreItem.$fieldGroup = $template.appendTo($fieldsContainer);

        Store.saveNewStoreItem(newStoreItem);

        return newStoreItem;
    };

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
            $newTabPane: $newTabNavigationItem.appendTo($imageNav),
            $newTabNavigationItem: $newTabPane.appendTo($tabContent)
        }
    };

    var setupCropper = function (storeItemId) {
        var storeItem = Store.getStoreItem(storeItemId);
        storeItem.cropper = new Cropper(
            $('.store_item_' + storeItemId)[0],
            {
                maxWidth: 300,
                maxHeight: 300,
                aspectRatio: 16 / 9,
                preview: $('#store_item_' + storeItemId).find('.img-preview')
            });
        Store.updateStoreItem(storeItem);
    };
});