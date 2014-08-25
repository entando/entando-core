<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>

<div id="divPageLink" class="tab">
<form id="form_pageLink">
<input type="hidden" name="contentOnSessionMarker" value="<s:property value="contentOnSessionMarker" />" />
<p><s:text name="note.choosePageToLink" />.</p>
<ul id="pageTree">
	<s:set name="inputFieldName" value="'selectedNode'" />
	<s:set name="selectedTreeNode" value="selectedNode" />
	<s:set name="liClassName" value="'page'" />
	
	<s:if test="#pageTreeStyleVar == 'classic'">
		<s:set name="currentRoot" value="allowedTreeRootNode" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
	</s:if>
	
	<s:elseif test="#pageTreeStyleVar == 'request'">
		<s:set name="openTreeActionName" value="'configInternalLink'" />
		<s:set name="closeTreeActionName" value="'configInternalLink'" />
		<s:set name="currentRoot" value="showableTree" />
		<s:set var="treeNodeExtraParamName" value="'activeTab'" />
		<s:set var="treeNodeExtraParamValue" value="1" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
	</s:elseif>
	
</ul>
<p><input id="button_pageLink" name="button_pageLink" type="submit" value="<s:text name="label.confirm" />" class="button" /></p>
</form>
</div>