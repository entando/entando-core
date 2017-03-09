$(function() {
	var normalizedBaseUrl = PROPERTY.baseUrl.replace(/\/$/, ''),
		previewUrl = [normalizedBaseUrl, 'preview', PROPERTY.lang, PROPERTY.pageCode].join('/');
	$('iframe#previewFrame')
		.attr('src', previewUrl)
		.attr('width', PROPERTY.previewWidth)
		.attr('height', PROPERTY.previewHeight);
});