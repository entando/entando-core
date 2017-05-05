<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
<s:if test="#categoryTreeStyleVar == 'classic'">
jQuery("#categoryTree").EntandoWoodMenu({
	menuToggler: "subTreeToggler",
	menuRetriever: function(toggler) {
		return $(toggler).parent().children("ul");
	},
	openClass: "node_open",
	closedClass: "node_closed",
	showTools: true,
	onStart: function() {
		this.collapseAll();
	},
	expandAllLabel: "<s:text name="label.expandAll" />",
	collapseAllLabel: "<s:text name="label.collapseAll" />",
<s:if test="%{selectedNode != null && !(selectedNode.equalsIgnoreCase(''))}">
	startIndex: "fagianonode_<s:property value="selectedNode" />",
</s:if>
	toolTextIntro: "<s:text name="label.introExpandAll" />",
	toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
	toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"
});
</s:if>

$('#modal-move-tree').on('show.bs.modal', function(){
	$('input[type="text"]', this).val("");
});

new EntandoTypeaheadTree({
	url: '<s:url action="searchParentsForMove" />',
	treetypeahead: $('#treetypeahead'),
	dataBuilder: function(query) {
			return jQuery.extend({
			'selectedNode': $('[name="selectedNode"]:checked', '#categoryTree').first().val()
		}, {
			'categoryCodeToken': query
		})
	}
})
