<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

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

    <s:form action="saveRowListViewerConfig" namespace="/do/jacms/Page/SpecialWidget/RowListViewer" cssClass="form-horizontal mt-20">
        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
            </div>
            <div class="panel-body">
                <h2 class="h5 margin-small-vertical">
                    <label class="sr-only"><s:text name="name.widget" /></label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                    <s:property value="%{getTitle(widget.type.code, widget.type.titles)}" />
                </h2>

                <p class="sr-only">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="frame" />
                    <wpsf:hidden name="widgetTypeCode" value="%{widget.type.code}" />
                    <wpsf:hidden name="contents" value="%{widget.config.get('contents')}" />
                </p>

                <!-- Form errors -->
                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property escapeHtml="false" /></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <fieldset class="col-xs-12 no-padding">
                    <legend><s:text name="title.contentsPublished" /></legend>
                    <s:set var="contentsPropertiesVar" value="contentsProperties" />
                    <s:if test="%{#contentsPropertiesVar.size()>0}">
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th><s:text name="label.content" /></th>
                                        <th class="table-w-15"><s:text name="label.contentModel" /></th>
                                        <th class="text-center table-w-10"><s:text name="label.actions" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator value="#contentsPropertiesVar" var="contentPropertiesVar" status="elementStatus">
                                        <tr>
                                            <td>
                                                <s:set var="contentIdVar" value="#contentPropertiesVar['contentId']" />
                                                <s:set var="contentVar" value="%{getContentVo(#contentIdVar)}" />
                                                <s:property value="#contentVar.descr" />&#32;<code><s:property value="#contentVar.id" /></code>
                                            </td>
                                            <td>
                                                <s:set var="modelIdVar" value="#contentPropertiesVar['modelId']" />
                                                <s:if test="null != #modelIdVar">
                                                    <s:set var="contentModelVar" value="%{getContentModel(#modelIdVar)}" />
                                                    <s:if test="null != #contentModelVar">
                                                        <s:property value="#contentModelVar.description" />&#32;<code><s:property value="#contentModelVar.id" /></code>
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="#modelIdVar" />
                                                    </s:else>
                                                </s:if>
                                                <s:else><code><s:text name="label.model.default" /></code></s:else>
                                                </td>
                                                <td class="text-center">
                                                <s:set var="elementIndex" value="#elementStatus.index" />
                                                <div class="btn-group btn-group-xs">
                                                    <wpsa:actionParam action="moveContent" var="actionName" >
                                                        <wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
                                                        <wpsa:actionSubParam name="movement" value="UP" />
                                                    </wpsa:actionParam>
                                                    <wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.moveUp')}"
                                                                 title="%{getText('label.moveInPositionNumber')}: %{#elementIndex}" cssClass="btn btn-default">
                                                        <span class="icon fa fa-sort-asc"></span>
                                                    </wpsf:submit>
                                                    <wpsa:actionParam action="moveContent" var="actionName" >
                                                        <wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
                                                        <wpsa:actionSubParam name="movement" value="DOWN" />
                                                    </wpsa:actionParam>
                                                    <wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.moveDown')}"
                                                                 title="%{getText('label.moveInPositionNumber')}: %{#elementIndex+2}" cssClass="btn btn-default">
                                                        <span class="icon fa fa-sort-desc"></span>
                                                    </wpsf:submit>
                                                </div>

                                                <div class="btn-group btn-group-xs">
                                                    <wpsa:actionParam action="removeContent" var="actionName" >
                                                        <wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
                                                    </wpsa:actionParam>
                                                    <wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.remove')}"
                                                                 title="%{getText('label.remove')}" cssClass="btn-remove">
                                                        <span class="fa fa-trash-o fa-lg"></span>
                                                    </wpsf:submit>
                                                </div>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                    </s:if>
                    <s:else>
                        <div class="alert alert-info">
                            <span class="pficon pficon-info"></span>
                            <s:text name="note.noContentSet" />
                        </div>
                    </s:else>

                    <div class="form-group">
                        <div class="col-xs-12">
                            <wpsf:submit type="button" action="addContent" cssClass="btn btn-primary pull-right">
                                <s:text name="label.add" />
                            </wpsf:submit>
                        </div>
                    </div>
                </fieldset>

                <!-- Publishing Option -->
                <fieldset class="col-xs-12 no-padding">
                    <legend data-toggle="collapse" data-target="#options-publishing">
                        <s:text name="title.publishingOptions" />&#32;
                        <span class="icon fa fa-chevron-down"></span>
                    </legend>
                    <div class="collapse" id="options-publishing">
                        <div class="form-group">
                            <label for="maxElemForItem" class="col-sm-2 control-label">
                                <s:text name="label.maxElementsForItem" />
                            </label>
                            <div class="col-sm-10">
                                <wpsf:select name="maxElemForItem" id="maxElemForItem"
                                             value="%{getShowlet().getConfig().get('maxElemForItem')}"
                                             headerKey="" headerValue="%{getText('label.all')}"
                                             list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20,50:50,100:100,500:500}"
                                             cssClass="form-control" />
                            </div>
                        </div>
                    </div>
                </fieldset>

                <!-- Extra Options -->
                <fieldset class="col-xs-12 no-padding">
                    <legend data-toggle="collapse" data-target="#options-extra">
                        <s:text name="title.extraOption" />&#32;
                        <span class="icon fa fa-chevron-down"></span>
                    </legend>
                    <div class="collapse" id="options-extra">
                        <div class="alert alert-info">
                            <span class="pficon pficon-info"></span>
                            <s:text name="note.extraOption.intro" />
                        </div>

                        <s:iterator var="lang" value="langs">
                            <div class="form-group">
                                <label for="title_<s:property value="#lang.code" />" class="col-sm-2 control-label">
                                    <code class="label label-info"><s:property value="#lang.code" /></code>&#32;
                                    <s:text name="label.title" />
                                </label>
                                <div class="col-sm-10">
                                    <wpsf:textfield name="title_%{#lang.code}" id="title_%{#lang.code}" value="%{widget.config.get('title_' + #lang.code)}"
                                                    cssClass="form-control" />
                                </div>
                            </div>
                        </s:iterator>

                        <div class="form-group">
                            <label for="pageLink" class="col-sm-2 control-label">
                                <s:text name="label.page" />
                            </label>
                            <div class="col-sm-10">
                                <wpsf:select list="pages" name="pageLink" id="pageLink" listKey="code" listValue="getShortFullTitle(currentLang.code)" value="%{widget.config.get('pageLink')}" headerKey=""
                                             headerValue="%{getText('label.none')}" cssClass="form-control" />
                            </div>
                        </div>

                        <s:iterator var="lang" value="langs">
                            <div class="form-group">
                                <label for="linkDescr_<s:property value="#lang.code" />" class="col-sm-2 control-label">
                                    <span class="label label-info"><s:property value="#lang.code" /></span>&#32;
                                    <s:text name="label.link.descr"/>
                                </label>
                                <div class="col-sm-10">
                                    <wpsf:textfield name="linkDescr_%{#lang.code}" id="linkDescr_%{#lang.code}" value="%{widget.config.get('linkDescr_' + #lang.code)}" cssClass="form-control" />
                                </div>
                            </div>
                        </s:iterator>
                    </div>
                </fieldset>
            </div>
        </div>


        <div class="row">
            <div class="col-xs-12">
                <wpsf:submit type="button" action="saveRowListViewerConfig"
                             cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
