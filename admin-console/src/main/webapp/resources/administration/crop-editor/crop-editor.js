$(document).ready(function () {
    var isCropEditorModalShown = false;
    var pendingStoreItems = [];
    $('.image-upload-form').on('change', 'input:file', function(){
        var input = this;
        $('.bs-cropping-modal').modal('show');
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                // Creating first storeItem from original photo.
                var storeItem = setupNewStoreItem(true);
                storeItem.imageData = e.target.result;
                save(storeItem);
            };

            reader.readAsDataURL(input.files[0]);
        }
    });

    var $cropEditorModal = $('.bs-cropping-modal');

    $cropEditorModal.on('shown.bs.modal', function () {
        isCropEditorModalShown = true;
        pendingStoreItems.map(function(storeItemId){
            store[storeItemId].cropper = setupCropper(storeItemId);
        });
        pendingStoreItems = [];
        // store[getCurrentStoreItemId()].cropper = setupCropper(getCurrentStoreItemId());
    });

    $cropEditorModal.on('hidden.bs.modal', function () {
        isCropEditorModalShown = false;
    });


    $cropEditorModal.on('click', '.btn', function () {
        if ($(this).data('method') === 'crop') {
            var storeItem = setupNewStoreItem(false);
            save(storeItem);
        }

    });


    var store = [];

    var getCurrentStoreItemId = function () {
        var currentStoreItemId = 0;
        if (store.length > 0 ) {
            currentStoreItemId = $('.bs-cropping-modal').find('.tab-pane.active').data('itemId');
        }

        return currentStoreItemId;
    };

    var setupCropper = function (storeItemId) {
        // Return cropper created for specific storeItem.
        return new Cropper(
            $('.store_item_' + storeItemId)[0],
            {
                viewMode: 3,
                aspectRatio: 16 / 9,
                preview: $('#store_item_' + storeItemId).find('.img-preview')
            });
    };


    var setupNewStoreItem = function (isInitial) {
        var currentItemId = getCurrentStoreItemId();
        var newId = store.length;
        if (isInitial) {
            return {
                id: newId,
                name: "name" + "_" + newId
            };

        } else {
            var currentStoreItem = store[currentItemId];
            return {
                id: newId,
                name: currentStoreItem.name + "_" + newId,
                imageData: currentStoreItem.cropper.getCroppedCanvas().toDataURL('image/jpeg')
            };
        }
    };

    var save = function (storeItem) {
        store.push(storeItem);
        addTab(storeItem);
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
        $newTabNavigationItem.removeAttr('id');
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

        if(store.length > 0) {
            if(isCropEditorModalShown){
                store[storeItem.id].cropper = setupCropper(storeItem.id);
            } else {
                pendingStoreItems.push(storeItem.id);
            }
        }
    };
});