<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.pageManagement.pageTrash" />
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.pageManagement.pageTrash" />
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">

    <s:set var="breadcrumbs_pivotPageCode" value="selectedNode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="alert alert-danger">
        <h2  class="h4 margin-none">
            <s:text name="message.title.ActionErrors" />
        </h2>
        <p>
            <s:text name="message.note.resolveReferences" />
        </p>
    </div>

    <wpsa:hookPoint key="core.pageReferences" objectName="hookPointElements_core_pageReferences">
        <s:iterator value="#hookPointElements_core_pageReferences" var="hookPointElement">
            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
        </s:iterator>
    </wpsa:hookPoint>

</div>
