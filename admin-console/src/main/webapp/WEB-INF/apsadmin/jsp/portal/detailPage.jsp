<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
		<a href="<s:url action="viewTree" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>
    </li>
    <li><s:text name="title.detailPage" /></li>
</ol>

<h1><s:text name="title.detailPage" /></h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="selectedNode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action  name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="selectedNode"></s:param></s:action>

<wpsa:hookPoint key="core.detailPage" objectName="hookPointElements_core_detailPage">
<s:iterator value="#hookPointElements_core_detailPage" var="hookPointElement">
	<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
</s:iterator>
</wpsa:hookPoint>

</div>
