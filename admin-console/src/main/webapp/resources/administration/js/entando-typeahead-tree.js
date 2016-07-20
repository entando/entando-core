function EntandoTypeaheadTree (config) {
	var config = jQuery.extend({
		treetypeahead: undefined,
		url: 'searchParentsForMove',
		labelNoResult: 'No result available',
		dataBuilder: function(query) {
			return jQuery.extend(
				{
					'selectedNode': $('[name="selectedNode"]:checked').first().val()
				},
				{
					'pageCodeToken': query
				}
				)
		},
		ajaxSuccess: function(data, textStatus, jqXHR, process, query) {
			var data = data;
			if (data===undefined || data===null || data.length==undefined || data.length==0) {
				data = [{
					code: query,
					fullTitle: config.labelNoResult,
					title: config.labelNoResult
				}]
			}
			process(data);
		},
		ajaxError: function(jqXHR, textStatus, errorThrown, process, query) {
			process([{
					code: query,
					fullTitle: '[' +textStatus+ ']' +errorThrown,
					title: errorThrown
				}]
			);
		},
		matcher: function(item) {
		var reg = new RegExp(this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/gi, '\\$&'));
			return  reg.test(item.code.toLowerCase())
		},
		sorter: function(items) {
			$(this.$menu).width($(this.$element).outerWidth()+"px");
			var items = items.sort(function(a, b){
				if(a.code.toLowerCase() > b.code.toLowerCase()) {
					return 1;
				}
				if(a.code.toLowerCase() < b.code.toLowerCase()) {
					return -1;
				}
				return 0;
			});
			return items;
		},
		highlighter: function(item) {
			var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
			var regexp = new RegExp('(' + query + ')', 'ig');

			return ''
			+ '<span class="label label-info">'
			+ (item.code).replace(regexp, function($1, match){ return '<strong class="bg-primary">' + match + '</strong>' })
			+ '</span> '
			+''+' '+item.fullTitle;
		},
		updater: function(item) {
			return item.code
		}
	},config);
	if (config.treetypeahead!==undefined) {
		config.treetypeahead.attr('autocomplete', 'off');
		var th = config.treetypeahead.typeahead({
			source: function(query, process) {
				$.ajax(config.url,{
					async: true,
					cache: false,
					data:	config.dataBuilder.call(this, query),
					success: function(data, textStatus, jqXHR) {
							config.ajaxSuccess.call(this, data, textStatus, jqXHR, process, query);
					},
					error: function(jqXHR, textStatus, errorThrown) {
								config.ajaxError.call(this, jqXHR, textStatus, errorThrown, process, query);
					}
				});
			},
			matcher: config.matcher,
			sorter: config.sorter,
			highlighter: config.highlighter,
			updater: config.updater
		});
	}
	return th;
};

/*
response example:
[
	{
		"code": "",
		"fullTitle": "",
		"title": ""
	},
	{...},
	{...}
]
*/
