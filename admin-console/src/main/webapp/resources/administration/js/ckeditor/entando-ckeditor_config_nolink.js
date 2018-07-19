CKEDITOR.editorConfig = function( config ) {
	config.removeDialogTabs = 'link:advanced';
	config.removePlugins = 'link, entandolink' ;
	config.toolbar = [
		{ name: 'clipboard', items: [  'Undo', 'Redo' ] },
		{ name: 'insert', items: [ 'Table', 'HorizontalRule', 'SpecialChar' ] },
		{ name: 'tools', items: [ 'Maximize' ] },
		{ name: 'basicstyles', items: [ 'Bold', 'Italic', 'Strike', '-', 'RemoveFormat' ] },
		{ name: 'paragraph', items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote' ] },
		{ name: 'document', items: [ 'Source' ] },
	];
    config.height = 250;
    config.forcePasteAsPlainText = true;
    config.docType = '<!DOCTYPE html>';
    config.pasteFromWordNumberedHeadingToList = true;
    config.pasteFromWordPromptCleanup = true;
    config.extraAllowedContent = 'a[!href,rel,target,hreflang]';
};
