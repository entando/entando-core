<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>
    </li>
    <li>
        <a href="<s:url action="configure" namespace="/do/Page">
               <s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
           </s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />">
            <s:text name="title.configPage" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="name.widget" />
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="name.widget" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div id="main" role="main">
    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />
    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
    <s:form namespace="/do/jacms/Page/SpecialWidget/ListViewer" cssClass="form-horizontal">
        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
            </div>

            <div class="panel-body">
                <s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
                    <h2 class="h5 margin-small-vertical">
                        <label class="sr-only">
                        <s:text name="name.widget" />
                    </label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                    <s:property value="%{getTitle(#showletType.code, #showletType.titles)}" />
                </h2>
                <p class="sr-only">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="frame" />
                    <wpsf:hidden name="widgetTypeCode" />
                </p>

                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <strong><s:text name="message.title.FieldErrors" /></strong>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property escapeHtml="false" /></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <p class="sr-only">
                    <wpsf:hidden name="contentType" />
                    <wpsf:hidden name="categories" value="%{#parameters['categories']}" />
                    <wpsf:hidden name="orClauseCategoryFilter" value="%{#parameters['orClauseCategoryFilter']}" />
                    <wpsf:hidden name="userFilters" value="%{#parameters['userFilters']}" />
                    <wpsf:hidden name="filters" />
                    <wpsf:hidden name="modelId" />
                    <wpsf:hidden name="maxElemForItem" />
                    <wpsf:hidden name="pageLink" value="%{#parameters['pageLink']}" />
                    <s:iterator var="lang" value="langs">
                        <wpsf:hidden name="%{'linkDescr_' + #lang.code}" value="%{#parameters['linkDescr_' + #lang.code]}" />
                        <wpsf:hidden name="%{'title_' + #lang.code}" value="%{#parameters['title_' + #lang.code]}" />
                    </s:iterator>
                    <wpsf:hidden name="userFilterKey" value="category" />
                </p>

                <div class="form-group">
                    <div class="col-xs-12">
                        <label for="userFilterCategoryCode">
                            <s:text name="label.userFilterCategory" />
                        </label>
                        <wpsf:select name="userFilterCategoryCode" id="userFilterCategoryCode" list="categories" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" />
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xs-12">
            <wpsf:submit action="addUserFilter" type="button" cssClass="btn btn-primary pull-right">
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </s:form>
</div>
