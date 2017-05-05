<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<script src="<wp:resourceURL />administration/js/bootstrap3-typeahead.min.js"></script>
<script src="<wp:resourceURL />administration/js/entando-typeahead-tree.js"></script>

<script>
//one domready to rule 'em all
$(function() {

<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-pageTree.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/js_trees_context_menu.jsp" />

$('#modal-move-branch').on('show.bs.modal', function(){
	$('input[type="text"]', this).val("");
});


new EntandoTypeaheadTree({
	url: '<s:url action="searchParentsForMove" />',
	treetypeahead: $('#treetypeahead'),
	labelNoResult: '<s:property value="%{getText('label.noResults')" escapeCsv="false" escapeHtml="false" escapeJavaScript="true" escapeXml="false" />',
	labelError: '<s:property value="%{getText('label.searchError')" escapeCsv="false" escapeHtml="false" escapeJavaScript="true" escapeXml="false" />',
	dataBuilder: function(query) {
			return jQuery.extend({
			'selectedNode': $('[name="selectedNode"]:checked', '#pageTree').first().val()
		}, {
			'pageCodeToken': query
		})
	}
})

Entando.UpdateBar($('[data-entando-progress-url]'));

$('[data-entando-progress-url]').on('entando.progress', function(ev, perc) {
	if(perc==100){
		$(this).parents('.alert.alert-info').removeClass('alert-info').addClass('alert-success');
	}
});


});
</script>
