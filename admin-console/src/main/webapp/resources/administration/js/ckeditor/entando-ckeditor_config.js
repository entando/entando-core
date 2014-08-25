CKEDITOR.editorConfig = function(config) {
	config.removePlugins = 'link'; //the link plugin is disabled because we have entandolink
	config.extraPlugins = 'entandolink';
	config.toolbar = [['Bold', 'Italic', '-', 'NumberedList','BulletedList', '-', 'entandolink', 'entandounlink', '-', 'Undo','Redo', '-', 'Table', '-', 'Source']];
	config.height = 250;
	config.forcePasteAsPlainText = true;
	config.docType = '<!DOCTYPE html>';
	config.pasteFromWordNumberedHeadingToList = true;
	config.pasteFromWordPromptCleanup = true;
};