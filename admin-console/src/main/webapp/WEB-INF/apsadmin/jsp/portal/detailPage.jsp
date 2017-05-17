<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li><a href="<s:url action="viewTree" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageTree" />"><s:text name="title.pageTree" /></a></li>
    <li class="page-title-container"><s:text name="title.detailPage" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.detailPageTitle" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>


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

