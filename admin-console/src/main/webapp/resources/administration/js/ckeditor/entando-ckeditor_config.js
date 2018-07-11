CKEDITOR.editorConfig = function( config ) {
	config.height = 250;
	config.forcePasteAsPlainText = true;
	config.docType = '<!DOCTYPE html>';
	config.pasteFromWordNumberedHeadingToList = true;
	config.pasteFromWordPromptCleanup = true;
	config.removePlugins = 'link' ; //the link plugin is disabled because we have entandolink
    config.extraPlugins = 'entandolink';
	config.toolbar = [
        { name: 'clipboard', items: ['Undo', 'Redo' ] },
		{ name: 'insert', items: [ 'Table', 'HorizontalRule', 'SpecialChar' ] },
		{ name: 'tools', items: [ 'Maximize' ] },
		{ name: 'basicstyles', items: [ 'Bold', 'Italic', 'Strike', '-', 'RemoveFormat' ] },
		{ name: 'paragraph', items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote' ] },
		{ name: 'entando', items: [ 'entandolink', 'entandounlink' ] },
        { name: 'document', items: [ 'Source' ] },
	];
	// Dialog windows are also simplified.
	config.removeDialogTabs = 'link:advanced';
	config.extraAllowedContent = 'a[!href,rel,target,hreflang]';
};
