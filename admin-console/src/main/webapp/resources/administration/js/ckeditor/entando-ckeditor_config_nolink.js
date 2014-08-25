CKEDITOR.editorConfig = function(config) {
	config.removePlugins = 'link,entandolink'; //the link plugin is disabled because we have entandolink
	config.toolbar = [['Bold', 'Italic', '-', 'NumberedList','BulletedList', '-', '-', 'Undo','Redo', '-', 'Table', '-', 'Source']];
	config.height = 250;
	config.forcePasteAsPlainText = true;
	config.docType = '<!DOCTYPE html>';
	config.pasteFromWordNumberedHeadingToList = true;
	config.pasteFromWordPromptCleanup = true;
};