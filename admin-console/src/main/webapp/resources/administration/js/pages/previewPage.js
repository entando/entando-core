$(function () {



	var sizeMap = {
		desktop: {
			width: '100%',
			height: '100%'
		},
		tablet: {
			width: '768px',
			height: '1024px'
		},
		smartphone: {
			width: '360px',
			height: '640px'
		}
	};

	var $customPanel = $('.custom-panel'),
		$customWidthInput = $customPanel.find('.custom-width'),
		$customHeightInput = $customPanel.find('.custom-height'),
		$customOkBtn = $('.custom-size-btn'),
		$previewModeSelect = $('.preview-mode-select'),
		$langSelect = $('#preview-mode-lang'),
		$previewFrame = $('iframe#previewFrame');



	function updatePreviewSize() {
		var key = $previewModeSelect.val(),
			width, height;

		if (key === 'custom') {
			width = ($customWidthInput.val() || 100) + 'px';
			height = ($customHeightInput.val() || 100) + 'px';
		} else if (key && sizeMap[key]) {
			width = sizeMap[key].width;
			height = sizeMap[key].height;
		}
		$previewFrame
			.attr('width', width)
			.attr('height', height);
	}


	function updateIframeUrl() {
		var normalizedBaseUrl = PROPERTY.baseUrl.replace(/\/$/, ''),
		previewUrl = [normalizedBaseUrl, 'preview', PROPERTY.lang, PROPERTY.pageCode].join('/');
		previewUrl += '?' + [ 'token='+encodeURIComponent(PROPERTY.token) ].join('&');
	
		$previewFrame.attr('src', previewUrl);
	}
	
	
	updateIframeUrl();
	$customWidthInput.val(PROPERTY.previewWidth);
	$customHeightInput.val(PROPERTY.previewHeight);
	$previewModeSelect.val('custom');
	$langSelect.val(PROPERTY.lang);




	$previewModeSelect.change(function () {
		var key = $previewModeSelect.val();
		if (key === 'custom') {
			$customPanel.show();
		} else {
			$customPanel.hide();
		}
		updatePreviewSize();
	});
	
	$langSelect.change(function(){
		PROPERTY.lang = $langSelect.val();
		updateIframeUrl();
		
	});

	$customOkBtn.click(function() {
		updatePreviewSize();
	});

});