<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<s:if test="#categoryTreeStyleVar == 'request'">
<p class="sr-only">
<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
</p>
</s:if>

<div class="well">
	<ul id="categoryTree" class="fa-ul list-unstyled">
		<s:set var="inputFieldName" value="'categoryCode'" />
		<s:set var="selectedTreeNode" value="categoryCode" />
		<s:set var="liClassName" value="'category'" />
		<s:set var="treeItemIconName" value="'fa-folder'" />

		<s:if test="#categoryTreeStyleVar == 'classic'">
			<s:set var="currentRoot" value="categoryRoot" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
		</s:if>
		<s:elseif test="#categoryTreeStyleVar == 'request'">
			<s:set var="currentRoot" value="showableTree" />
			<s:set var="openTreeActionName" value="'backToContentList'" />
			<s:set var="closeTreeActionName" value="'backToContentList'" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
		</s:elseif>
	</ul>
</div>