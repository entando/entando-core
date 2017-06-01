<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<!-- Admin console Breadcrumbs -->
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li>
        <s:url action="configure" namespace="/do/Page" var="configureURL">
            <s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
        </s:url>
        <s:set var="configureTitle">
            <s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />
        </s:set>
        <a href="${configureURL}" title="${configureTitle}"><s:text name="title.configPage" /></a>
    </li>
    <li class="page-title-container"><s:text name="name.widget" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}"/>
<h1 class="page-title-container">
    <s:text name="name.widget" />
    <span class="pull-right">
        <a tabindex="0" role="button"
           data-toggle="popover" data-trigger="focus" data-html="true"
           data-content="<s:text name="name.widget.help" />" data-placement="left"
           data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<hr>

<div id="main" role="main">
    <!-- Info Details  -->
    <div class="button-bar mt-20">
        <s:action namespace="/do/Page" name="printPageDetails"
                  executeResult="true" ignoreContextParams="true">
            <s:param name="selectedNode" value="currentPage.code"></s:param>
        </s:action>
    </div>

    <s:form action="executeJoinContent" namespace="/do/jacms/Page/SpecialWidget/RowListViewer" cssClass="form-horizontal mt-20">
        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
            </div>
            <div class="panel-body">
                <s:set var="widgetType" value="%{getWidgetType(widgetTypeCode)}"></s:set>
                    <h2 class="h5 margin-small-top margin-large-bottom">
                        <label class="sr-only"><s:text name="name.widget" /></label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                    <s:property value="%{getTitle(#widgetType.code, #widgetType.titles)}" />
                </h2>

                <p class="sr-only">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="frame" />
                    <wpsf:hidden name="widgetTypeCode" />
                    <wpsf:hidden name="contents" value="%{#parameters['contents']}"  />
                    <wpsf:hidden name="maxElemForItem" value="%{#parameters['maxElemForItem']}"  />
                    <wpsf:hidden name="pageLink" value="%{#parameters['pageLink']}" />
                    <wp:info key="langs" var="langsVar" />
                    <c:forEach var="iteratorLang" items="${langsVar}" varStatus="status">
                        <s:set var="langCodeVar" ><c:out value="${iteratorLang.code}" /></s:set>
                        <wpsf:hidden name="%{'linkDescr_' + #langCodeVar}" value="%{#parameters['linkDescr_' + #langCodeVar]}" />
                        <wpsf:hidden name="%{'title_' + #langCodeVar}" value="%{#parameters['title_' + #langCodeVar]}" />
                    </c:forEach>
                    <wpsf:hidden name="contentId" />
                </p>

                <!-- Form errors -->
                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
                        <ul class="margin-base-vertical">
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property escapeHtml="false" /></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <s:set var="contentVoVar" value="%{getContentVo(contentId)}"></s:set>

                    <fieldset class="col-xs-12 no-padding">
                        <legend><s:text name="label.info" /></legend>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><s:text name="label.content" /></label>
                        <div class="col-sm-10">
                            <code><s:property value="#contentVoVar.id" /></code>&#32;&mdash;&#32;
                            <s:property value="#contentVoVar.descr" />
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <wpsf:submit action="searchContents" value="%{getText('label.change')}" cssClass="btn btn-default" />
                        </div>
                    </div>
                </fieldset>

                <fieldset class="col-xs-12 no-padding">
                    <legend data-toggle="collapse" data-target="#options-publishing">
                        <s:text name="title.publishingOptions" />&#32;<span class="icon fa fa-chevron-down"></span>
                    </legend>
                    <div class="collapse" id="options-publishing">
                        <div class="form-group">
                            <label for="modelId" class="col-sm-2 control-label"><s:text name="label.contentModel" /></label>
                            <div class="col-sm-10">
                                <wpsf:select id="modelId" name="modelId"
                                             list="%{getModelsForContent(contentId)}"
                                             headerKey="" headerValue="%{getText('label.default')}" listKey="id"
                                             listValue="description" cssClass="form-control" />
                            </div>
                        </div>
                    </div>
                </fieldset>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <wpsf:submit type="button" action="executeJoinContent"
                             cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
