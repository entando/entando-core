<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
<s:if test="#pageTreeStyleVar == 'classic'">
jQuery("#pageTree").EntandoWoodMenu({
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
