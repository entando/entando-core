<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure"/></li>
    <li>
        <a href="<s:url namespace="/do/Category" action="viewTree" />">
            <s:text name="title.categoryManagement" />
        </a>
    </li>
    <li class="page-title-container"><s:text name="title.categoryDetail"/></li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.categoryDetail"/>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="page.category.help"/>" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">
    <s:set var="breadcrumbs_pivotCategoryCode" value="categoryCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo_breadcrumbs.jsp" />
    <s:include value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo_details.jsp" />
    
    <wpsa:hookPoint key="core.categoryDetails" objectName="hookPointElements_core_categoryDetails">
        <s:iterator value="#hookPointElements_core_categoryDetails" var="hookPointElement">
            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
        </s:iterator>
    </wpsa:hookPoint>
    <s:include value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo-references.jsp" />
</div>
