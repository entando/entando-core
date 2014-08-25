/*
 entandolink is a customized version of the original link plugin.
*/
CKEDITOR.plugins.add('entandolink', {
		requires: ['fakeobjects'],
		init: function(editor) {
				var pluginName = 'entandolink';
				if (window.entandoCKEditor === undefined) {
						window.entandoCKEditor = {};
				}
				// Add the link and unlink buttons.
				editor.addCommand('entandolink', {
					exec: function(editor) {
						var selection = editor.getSelection();
						var ranges = selection.getRanges(true);
						if (!(ranges.length == 1 && ranges[0].collapsed)) {
							var id = 'entando_link_window_' + editor.element.$.id;
							window.entandoCKEditor[id] = editor;
							var width = window.innerWidth / 2;
							if (width < 780 && window.innerWidth > 780) {
									width = 780;
							} else {
									width = window.innerWidth;
							}
							var height = window.innerHeight - (window.innerHeight / 100 * 2);
							window.open(editor.config.EntandoLinkActionPath, id, 'width=' + width + ',height=' + height + ',location=no,scrollbars=yes,toolbar=no,resizable=1');
						}
					}
				});
				editor.addCommand('entandounlink', {
					/** @ignore */
					exec: function(editor) {
						/*
						 * execCommand( 'unlink', ... ) in Firefox leaves behind <span> tags at where
						 * the <a> was, so again we have to remove the link ourselves. (See #430)
						 *
						 * TODO: Use the style system when it's complete. Let's use execCommand()
						 * as a stopgap solution for now.
						 */
						var selection = editor.getSelection(),
								bookmarks = selection.createBookmarks(),
								ranges = selection.getRanges(),
								rangeRoot,
								element;

						for (var i = 0; i < ranges.length; i++) {
								rangeRoot = ranges[i].getCommonAncestor(true);
								element = rangeRoot.getAscendant('a', true);
								if (!element)
										continue;
								ranges[i].selectNodeContents(element);
						}

						selection.selectRanges(ranges);
						editor.document.$.execCommand('unlink', false, null);
						selection.selectBookmarks(bookmarks);
					},
					startDisabled: false
				});
				editor.ui.addButton('entandolink', {
					label: editor.lang.link.toolbar,
					command: 'entandolink',
					icon: CKEDITOR.plugins.getPath(pluginName) + "entandolink-icon.png",
				});
				editor.ui.addButton('entandounlink', {
					label: editor.lang.unlink,
					command: 'entandounlink',
					icon: CKEDITOR.plugins.getPath(pluginName) + "entandounlink-icon.png"
				});

				// Register selection change handler for the unlink button.
				editor.on('selectionChange', function(evt) {
					if (editor.readOnly) {
							return;
					}
					var command = editor.getCommand('entandounlink'),
							element = evt.data.path.lastElement && evt.data.path.lastElement.getAscendant('a', true);
					if (element && element.getName() == 'a' && element.getAttribute('href') && element.getChildCount())
							command.setState(CKEDITOR.TRISTATE_OFF);
					else
							command.setState(CKEDITOR.TRISTATE_DISABLED);
				});

				// If the "menu" plugin is loaded, register the menu items.
				if (editor.addMenuItems) {
					editor.addMenuItems({
						entandolink: {
								label: editor.lang.link.menu,
								command: 'entandolink',
						},
						entandounlink: {
								label: editor.lang.unlink,
								command: 'entandounlink'
						}
					});
				}
		},

		afterInit: function(editor) {
			// Register a filter to displaying placeholders after mode change.
			var dataProcessor = editor.dataProcessor,
					dataFilter = dataProcessor && dataProcessor.dataFilter,
					htmlFilter = dataProcessor && dataProcessor.htmlFilter,
					pathFilters = editor._.elementsPath && editor._.elementsPath.filters;

			if (dataFilter) {
				dataFilter.addRules({
					elements: {
						a: function(element) {
							var attributes = element.attributes;
							if (!attributes.name)
								return null;

							var isEmpty = !element.children.length;

							if (CKEDITOR.plugins.entandolink.synAnchorSelector) {
								// IE needs a specific class name to be applied
								// to the anchors, for appropriate styling.
								var ieClass = isEmpty ? 'cke_anchor_empty' : 'cke_anchor';
								var cls = attributes['class'];
								if (attributes.name && (!cls || cls.indexOf(ieClass) < 0))
										attributes['class'] = (cls || '') + ' ' + ieClass;

								if (isEmpty && CKEDITOR.plugins.entandolink.emptyAnchorFix) {
										attributes.contenteditable = 'false';
										attributes['data-cke-editable'] = 1;
								}
							} else if (CKEDITOR.plugins.entandolink.fakeAnchor && isEmpty)
								return editor.createFakeParserElement(element, 'cke_anchor', 'anchor');
							return null;
						}
					}
				});
			}

				if (CKEDITOR.plugins.entandolink.emptyAnchorFix && htmlFilter) {
					htmlFilter.addRules({
						elements: {
							a: function(element) {
								delete element.attributes.contenteditable;
							}
						}
					});
				}

				if (pathFilters) {
					pathFilters.push(function(element, name) {
						if (name == 'a') {
							if (CKEDITOR.plugins.entandolink.tryRestoreFakeAnchor(editor, element) ||
								(element.getAttribute('name') && (!element.getAttribute('href') || !element.getChildCount()))) {
								return 'anchor';
							}
						}
					});
				}
		}
});

CKEDITOR.plugins.entandolink = {
	/**
	 *  Get the surrounding link element of current selection.
	 * @param editor
	 * @example CKEDITOR.plugins.entandolink.getSelectedLink( editor );
	 * @since 3.2.1
	 * The following selection will all return the link element.
	 *	 <pre>
	 *  <a href="#">li^nk</a>
	 *  <a href="#">[link]</a>
	 *  text[<a href="#">link]</a>
	 *  <a href="#">li[nk</a>]
	 *  [<b><a href="#">li]nk</a></b>]
	 *  [<a href="#"><b>li]nk</b></a>
	 * </pre>
	 */
	getSelectedLink: function(editor) {
		try {
				var selection = editor.getSelection();
				if (selection.getType() == CKEDITOR.SELECTION_ELEMENT) {
						var selectedElement = selection.getSelectedElement();
						if (selectedElement.is('a'))
								return selectedElement;
				}

				var range = selection.getRanges(true)[0];
				range.shrink(CKEDITOR.SHRINK_TEXT);
				var root = range.getCommonAncestor();
				return root.getAscendant('a', true);
		} catch (e) {
				return null;
		}
	},

	// Opera and WebKit don't make it possible to select empty anchors. Fake
	// elements must be used for them.
	fakeAnchor: CKEDITOR.env.opera || CKEDITOR.env.webkit,

	// For browsers that don't support CSS3 a[name]:empty(), note IE9 is included because of #7783.
	synAnchorSelector: CKEDITOR.env.ie,

	// For browsers that have editing issue with empty anchor.
	emptyAnchorFix: CKEDITOR.env.ie && CKEDITOR.env.version < 8,

	tryRestoreFakeAnchor: function(editor, element) {
		if (element && element.data('cke-real-element-type') && element.data('cke-real-element-type') == 'anchor') {
			var link = editor.restoreRealElement(element);
			if (link.data('cke-saved-name'))
				return link;
		}
	}
};
