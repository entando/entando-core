<%@ taglib prefix="s" uri="/struts-tags" %>
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
        <a tabindex="0" role="button"data-toggle="popover" data-trigger="focus" data-html="true" data-content="${dataContent}" data-placement="left"
           data-original-title="${dataOriginalTitle}">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<hr>

<div id="main" role="main">
    <!-- Info Details -->
    <div class="button-bar mt-20">
        <s:action namespace="/do/Page" name="printPageDetails"
                  executeResult="true" ignoreContextParams="true">
            <s:param name="selectedNode" value="currentPage.code"></s:param>
        </s:action>
    </div>

    <!-- Form -->
    <s:form action="saveConfigSimpleParameter" cssClass="form-horizontal mt-20">
        <p class="sr-only">
            <wpsf:hidden name="pageCode" />
            <wpsf:hidden name="frame" />
            <wpsf:hidden name="widgetTypeCode" value="%{widget.type.code}" />
        </p>

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

                <fieldset class="col-xs-12 no-padding">
                    <legend>
                        <s:text name="title.editFrame.settings" />
                    </legend>
                    <s:iterator value="widget.type.typeParameters" var="widgetParam">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"
                                   for="%{'config-simple-parameter-'+#widgetParam.name}"> <s:property
                                    value="#widgetParam.name" />&nbsp; <a tabindex="0" role="button"
                                          data-toggle="popover" data-trigger="focus" data-html="false"
                                          data-original-title="Help Info"
                                          data-content="<s:property value="#widgetParam.descr" />"
                                          data-placement="top"> <span class="fa fa-info-circle"></span>
                                </a>
                            </label>
                            <div class="col-sm-10">
                                <wpsf:textfield
                                    id="%{'config-simple-parameter-'+#widgetParam.name}"
                                    name="%{#widgetParam.name}"
                                    value="%{widget.config[#widgetParam.name]}"
                                    cssClass="form-control" />
                            </div>
                        </div>
                    </s:iterator>
                </fieldset>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
