<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a>&#32;/&#32;
		<s:text name="title.pageManagement.pageTrash" />
	</span>
</h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="selectedNode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />
<%--
<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="selectedNode"></s:param></s:action>
--%>
<div class="alert alert-danger">
	<h2  class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
	<p><s:text name="message.note.resolveReferences" /></p>
</div>

<wpsa:hookPoint key="core.pageReferences" objectName="hookPointElements_core_pageReferences">
<s:iterator value="#hookPointElements_core_pageReferences" var="hookPointElement">
	<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
</s:iterator>
</wpsa:hookPoint>

</div>