<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:if test="#currentRoot.isEmpty()">
	<s:set name="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
	<s:set name="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>
<li class="<s:property value="#liClassName" /> tree_node_flag">
	<span class="icon fa fa-li <s:property value="#treeItemIconNameVar" />"></span>&#32;

	<s:if test="null == #openTreeActionName"><s:set var="openTreeActionName" value="'openCloseTreeNode'" /></s:if>
	<s:if test="null == #closeTreeActionName"><s:set var="closeTreeActionName" value="'openCloseTreeNode'" /></s:if>
	<s:if test="!#currentRoot.open && !#currentRoot.empty">
		<a
			href="<s:url action="%{#openTreeActionName}">
			<wpsa:paramMap map="#treeNodeExtraParamsMap" />
			<s:param name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
			<s:param name="copyingPageCode" value="copyingPageCode" />
			<s:param name="treeNodeActionMarkerCode" value="'open'" /><s:param name="targetNode" value="#currentRoot.code" /><s:param name="treeNodesToOpen" value="treeNodesToOpen" /></s:url>">
			<span class="icon fa fa-plus" title="<s:text name="label.open" />"></span>
			<span class="sr-only"><s:text name="label.open" /></span>
		</a>
	</s:if>
	<s:elseif test="#currentRoot.open && !#currentRoot.empty">
		<a class="noborder" href="<s:url action="%{#closeTreeActionName}">
		<wpsa:paramMap map="#treeNodeExtraParamsMap" />
		<s:param name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
		<s:param name="copyingPageCode" value="copyingPageCode" />
		<s:param name="treeNodeActionMarkerCode" value="'close'" /><s:param name="targetNode" value="#currentRoot.code" /><s:param name="treeNodesToOpen" value="treeNodesToOpen" /></s:url>">
			<span class="icon fa fa-minus" title="<s:text name="label.close" />"></span>
			<span class="sr-only"><s:text name="label.close" /></span>
		</a>
	</s:elseif>
	<input
		type="radio"
		name="<s:property value="#inputFieldName" />"
		id="fagianonode_<s:property value="#currentRoot.code" />"
		value="<s:property value="#currentRoot.code" />" <s:if test="#currentRoot.children.length > 0">class="subTreeToggler tree_<s:property value="#currentRoot.code" />" </s:if>
		<s:if test="#currentRoot.code == #selectedTreeNode"> checked="checked"</s:if> />
	<label for="fagianonode_<s:property value="#currentRoot.code" />">
		<s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
	</label>
	<s:if test="#currentRoot.children.length > 0">
		<ul class="treeToggler fa-ul"  id="tree_<s:property value="#currentRoot.code" />">
			<s:iterator value="#currentRoot.children" var="node">
				<s:set var="currentRoot" value="#node" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
			</s:iterator>
		</ul>
	</s:if>
</li>