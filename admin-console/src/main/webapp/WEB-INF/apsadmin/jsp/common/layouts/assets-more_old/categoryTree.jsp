<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<script src="<wp:resourceURL />administration/js/bootstrap3-typeahead.min.js"></script>
<script src="<wp:resourceURL />administration/js/entando-typeahead-tree.js"></script>
<script src="<wp:resourceURL />administration/js/entando-updater.js"></script>

<script>
//one domready to rule 'em all
$(function() {

<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-categoryTree.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/js_trees_context_menu.jsp" />

$('#modal-move-tree').on('show.bs.modal', function(ev){
	var input = $('input[type="text"]', this);
	input.val("");
});

$('#modal-move-tree').on('shown.bs.modal', function(ev){
	var input = $('input[type="text"]', this);
	input.get(0).focus();
})

new EntandoTypeaheadTree({
	url: '<s:url action="searchParentsForMove" />',
	treetypeahead: $('#treetypeahead'),
	labelNoResult: '<s:property value="%{getText('label.noResults')" escapeCsv="false" escapeHtml="false" escapeJavaScript="true" escapeXml="false" />',
	dataBuilder: function(query) {
			return jQuery.extend({
			'selectedNode': $('[name="selectedNode"]:checked', '#categoryTree').first().val()
		}, {
			'categoryCodeToken': query
		})
	}
});

Entando.UpdateBar($('[data-entando-progress-url]'));

$('[data-entando-progress-url]').on('entando.progress', function(ev, perc) {
	if(perc==100){
		$(this).parents('.alert.alert-info').removeClass('alert-info').addClass('alert-success');
	}
});

});//domready
</script>
