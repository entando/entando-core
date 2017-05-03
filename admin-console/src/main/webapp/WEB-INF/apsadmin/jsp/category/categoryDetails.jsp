<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

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
           data-content="TO be inserted" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">
    <s:set var="breadcrumbs_pivotCategoryCode" value="categoryCode"/>
    <div class="row">
        <div class="col-xs-3 col-label">
            <s:text name="name.categoryCode"/>
        </div>
        <div class="col-xs-9">
            <s:property value="categoryCode"/>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-3 col-label">
            <s:text name="name.categoryTitle"/>
        </div>
        <div class="col-xs-9">
            <s:iterator value="langs" status="categoryInfo_rowStatus" var="lang">
                <s:if test="%{!#categoryInfo_rowStatus.first}">,&#32;</s:if>
                <span class="monospace">(<abbr title="<s:property value="descr" />"><s:property
                        value="code"/></abbr>)</span>&#32;
                <s:property value="titles[#lang.code]"/>
                <s:if test="%{titles[#lang.code] == null}"><abbr
                        title="<s:text name="label.none" />">&ndash;</abbr></s:if>
            </s:iterator>
        </div>
    </div>
    <br>

    <wpsa:hookPoint key="core.categoryDetails" objectName="hookPointElements_core_categoryDetails">
        <s:iterator value="#hookPointElements_core_categoryDetails" var="hookPointElement">
            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
        </s:iterator>
    </wpsa:hookPoint>
    <s:include value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo-references.jsp"/>
    
</div>
