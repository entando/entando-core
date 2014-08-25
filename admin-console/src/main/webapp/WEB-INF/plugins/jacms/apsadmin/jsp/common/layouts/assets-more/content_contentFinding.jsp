<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />

<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<script>
//one domready to rule 'em all
$(function() {
	//for content categories
	<s:if test="#categoryTreeStyleVar == 'classic'">

		var catTree = jQuery("#categoryTree").EntandoWoodMenu({
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
		<s:if test="%{categoryCode != null && !(categoryCode.equalsIgnoreCase(''))}">
			startIndex: "fagianonode_<s:property value="categoryCode" />",
			toolTextIntro: "<s:text name="label.introExpandAll" />",
			toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
			toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"
		</s:if>
		});

	</s:if>
});
</script>