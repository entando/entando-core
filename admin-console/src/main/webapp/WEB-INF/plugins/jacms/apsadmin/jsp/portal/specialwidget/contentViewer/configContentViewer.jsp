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
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" data-content="<s:text name="name.widget.help" />" data-placement="left" data-original-title="">
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

    <!-- Form -->
    <s:form action="saveViewerConfig" namespace="/do/jacms/Page/SpecialWidget/Viewer"
            cssClass="form-horizontal">
        <div class="panel panel-default mt-20">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
            </div>
            <div class="panel-body">
                <h2 class="h5 margin-small-vertical">
                    <label class="sr-only"><s:text name="name.widget" /></label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                    <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                </h2>

                <p class="sr-only">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="frame" />
                    <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
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

                <s:set var="showletParams" value="showlet.type.parameter" />

                <%--
                <s:property value="#showletParams['contentId'].descr" />
                <h4><s:text name="title.configContentViewer.settings" /></h4>
                --%>

                <!-- Configure content -->
                <s:if test="showlet.config['contentId'] != null">
                    <s:set var="content" value="%{getContentVo(showlet.config['contentId'])}"></s:set>
                    <s:set var="canEditCurrentContent" value="%{false}" />
                    <c:set var="currentContentGroup"><s:property value="#content.mainGroupCode" escapeHtml="false"/></c:set>

                    <wp:ifauthorized groupName="${currentContentGroup}" permission="editContents"><s:set var="canEditCurrentContent" value="%{true}" /></wp:ifauthorized>

                        <!-- Edit/Change content -->
                        <fieldset class="col-xs-12 no-padding">
                            <legend><s:text name="label.info" /></legend>

                        <div class="form-group">
                            <label class="col-sm-2 control-label"><s:text name="label.content" /></label>
                            <div class="col-sm-10">
                                <code><s:property value="#content.id" /></code>&#32;&mdash;&#32;
                                <s:if test="#canEditCurrentContent">
                                    <s:url action="edit" namespace="/do/jacms/Content" var="editContentAction">
                                        <s:param name="contentId" value="#content.id" />
                                    </s:url>
                                    <s:set var="editContentLabel">
                                        <s:text name="label.edit" />:&#32;<s:property value="#content.descr"/>
                                    </s:set>
                                    <a href="${editContentAction}" title="${editContentLabel}">
                                        <s:property value="#content.descr"/>
                                    </a>
                                </s:if>
                                <s:else>
                                    <s:property value="#content.descr" />
                                </s:else>
                            </div>
                        </div>

                        <p class="sr-only">
                            <wpsf:hidden name="contentId" value="%{getShowlet().getConfig().get('contentId')}" />
                        </p>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <wpsf:submit action="searchContents" value="%{getText('label.change')}" cssClass="btn btn-default" />
                            </div>
                        </div>
                    </fieldset>

                    <!-- Select content model -->
                    <fieldset class="col-xs-12 no-padding">
                        <legend data-toggle="collapse" data-target="#options-publishing">
                            <s:text name="title.publishingOptions" />&#32;<span class="icon fa fa-chevron-down"></span>
                        </legend>

                        <div class="collapse" id="options-publishing">
                            <div class="form-group">
                                <label for="modelId" class="col-sm-2 control-label">
                                    <s:text name="label.contentModel" />
                                </label>
                                <div class="col-sm-10">
                                    <wpsf:select id="modelId" name="modelId"
                                                 value="%{getShowlet().getConfig().get('modelId')}"
                                                 list="%{getModelsForContent(showlet.config['contentId'])}"
                                                 headerKey="" headerValue="%{getText('label.default')}"
                                                 listKey="id" listValue="description" cssClass="form-control" />
                                </div>
                            </div>
                        </div>
                    </fieldset>

                    <%--
                            Uncomment this if you add some custom parameters to this Widget

                    <s:set var="showletTypeParameters" value="showlet.type.typeParameters"></s:set>
                    <s:if test="#showletTypeParameters.size()>2">
                    <fieldset class="col-xs-12 margin-large-top"><legend><s:text name="label.otherSettings" /></legend>
                            <s:iterator value="#showletTypeParameters" var="showletParam" >
                                    <s:if test="!#showletParam.name.equals('contentId') && !#showletParam.name.equals('modelId')">
                                            <div class="form-group">
                                                    <label for="fagianoParam_<s:property value="#showletParam.name" />" class="control-label"><s:property value="#showletParam.descr" /></label>
                                                    <wpsf:textfield cssClass="form-control" id="%{'fagianoParam_'+#showletParam.name}" name="%{#showletParam.name}" value="%{showlet.config[#showletParam.name]}" />
                                            </div>
                                    </s:if>
                            </s:iterator>
                    </fieldset>
                    </s:if>
                    --%>

                </s:if>

                <!-- Choose a content -->
                <s:else>
                    <div class="alert alert-info">
                        <span class="pficon pficon-info"></span>
                        <s:text name="note.noContentSet" />
                    </div>
                    <wpsf:submit action="searchContents" type="button"
                                 value="%{getText('label.choose')}"
                                 cssClass="btn btn-primary" />
                </s:else>

            </div>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
