<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="showletTypeVar" value="getShowletType(widgetTypeCode)" />
<s:set var="showletTypeApiMappingsVar" value="showletTypeApiMappings" />
<s:set var="showletUtilizers" value="showletUtilizers" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li><a href="<s:url action="viewWidgets" />"><s:text name="title.widgetManagement" /></a></li>
    <li><s:text name="title.uxPatterns.info" /></li>
    <li class="page-title-container"><s:property value="getTitle(#showletTypeVar.code, #showletTypeVar.titles)" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.widgetManagement.pages" />&nbsp;<s:property value="getTitle(#showletTypeVar.code, #showletTypeVar.titles)" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="widget.widgetManagement.help" />" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">
    <s:if test="!#showletTypeVar.isLogic()">
        <s:set var="relatedApiMethodVar" value="#showletTypeApiMappingsVar[#showletTypeVar.code]" />
    </s:if>
    <s:else>
        <s:set var="relatedApiMethodVar" value="#showletTypeApiMappingsVar[#showletTypeVar.parentType.code]" />
    </s:else>
    <s:if test="#showletUtilizers != null && #showletUtilizers.size() > 0">
        <s:form action="viewWidgetUtilizers" >
            <p class="sr-only">
                <wpsf:hidden name="widgetTypeCode" />
            </p>
            <wpsa:subset source="#showletUtilizers" count="10" objectName="groupShowletUtilizers" advanced="true" offset="5">
                <s:set var="group" value="#groupShowletUtilizers" />
                <s:iterator var="singlePage">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title row" title="<s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />">
                                <s:if test="%{isUserAllowed(#singlePage)}">
                                    <div class="col-sm-2 col-lg-2">
                                        <div class="btn-group">
                                            <a href="<s:url namespace="/do/Page" action="viewTree">
                                                   <s:param name="selectedNode" value="#singlePage.code" /></s:url>" class="btn btn-default" title="<s:text name="note.goToSomewhere" />: <s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />">
                                                   <span class="icon fa fa-sitemap"></span>
                                               </a>
                                               <a href="<s:url namespace="/do/Page" action="configure"><s:param name="pageCode" value="#singlePage.code" />
                                               </s:url>" class="btn btn-default" title="<s:text name="title.configPage" />: <s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />" />
                                            <span class="icon fa fa-cog"></span>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="col-sm-9 col-lg-9 padding-small-top padding-small-bottom">

                                    </s:if>
                                    <s:else>
                                        <div class="col-sm-9 col-lg-9 padding-small-top padding-small-bottom">
                                        </s:else>
                                            <s:property value="%{#singlePage.getShortFullTitle(currentLang.code)}" />&nbsp;&nbsp;
                                            <s:if test="%{#singlePage.isOnlineInstance()}"><code class="label label-success"><s:text name="label.online" /></code></s:if>
                                            <s:else><code class="label label-default"><s:text name="label.draft" /></code></s:else>
                                    </div>
                            </h2>
                        </div>
                        <ul class="list-group">
                            <s:iterator value="#singlePage.widgets" var="showlet" status="rowstatus">
                                <s:if test="#showlet != null && #showlet.type != null && #showlet.type.code.equals(widgetTypeCode)">
                                    <li class="list-group-item">
                                        <div class="row">
                                            <div class="col-sm-2 col-lg-2">
                                                <div class="btn-group">

                                                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" title="<s:text name="label.new" />...">
                                                        <s:text name="label.add" />
                                                        <span class="caret"></span>
                                                    </button>
                                                    <ul class="dropdown-menu">
                                                        <s:if test="%{null != #showletTypeVar.typeParameters && #showletTypeVar.typeParameters.size() > 0}">
                                                            <li>
                                                                <a href="<s:url namespace="/do/Portal/WidgetType" action="copy"><s:param name="pageCode" value="#singlePage.code" /><s:param name="framePos" value="#rowstatus.index" /></s:url>">
                                                                    <s:text name="label.userWidget.new" />
                                                                </a>
                                                            </li>
                                                        </s:if>
                                                        <s:if test="%{isUserAllowed(#singlePage)}">
                                                            <s:if test="null != #relatedApiMethodVar && !#showletTypeVar.isLogic()">
                                                                <wp:ifauthorized permission="superuser">
                                                                    <li>
                                                                        <a href="<s:url action="copyFromWidget" namespace="/do/Api/Service">
                                                                               <s:param name="pageCode" value="#singlePage.code" />
                                                                               <s:param name="framePos" value="#rowstatus.index" />
                                                                               <s:param name="resourceName" value="#relatedApiMethodVar.resourceName" />
                                                                               <s:param name="namespace" value="#relatedApiMethodVar.namespace" />
                                                                           </s:url>">
                                                                            <s:text name="note.api.apiMethodList.createServiceFromMethod" />&#32;
                                                                            <s:property value="#relatedApiMethodVar.methodName" />
                                                                        </a>
                                                                    </li>
                                                                </wp:ifauthorized>
                                                            </s:if>
                                                            <s:elseif test="null != #relatedApiMethodVar && #showletTypeVar.isLogic()">
                                                                <wp:ifauthorized permission="superuser">
                                                                    <li>
                                                                        <a href="<s:url action="newService" namespace="/do/Api/Service">
                                                                               <s:param name="widgetTypeCode" value="widgetTypeCode" />
                                                                               <s:param name="resourceName" value="#relatedApiMethodVar.resourceName" />
                                                                               <s:param name="namespace" value="#relatedApiMethodVar.namespace" />
                                                                           </s:url>">
                                                                            <s:text name="note.api.apiMethodList.createServiceFromMethod" />&#32;<s:property value="#relatedApiMethodVar.methodName" />
                                                                        </a>
                                                                    </li>
                                                                </wp:ifauthorized>
                                                            </s:elseif>
                                                        </s:if>
                                                    </ul>
                                                </div>
                                                <a href="<s:url action="editFrame" namespace="/do/Page">
                                                       <s:param name="pageCode"><s:property value="#singlePage.code"/></s:param>
                                                       <s:param name="frame"><s:property value="#rowstatus.index"/></s:param>
                                                   </s:url>" title="<s:text name="title.editFrame" />:&#32;<s:property value="#rowstatus.index"/> &ndash;&#32;<s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/>" class="btn btn-default">
                                                    <span class="icon fa fa-cog"></span>
                                                </a>
                                            </div>
                                            <div class="col-sm-10 col-lg-10 padding-small-top padding-small-bottom">
                                                <s:text name="label.placedOn" />&nbsp;<strong><s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/></strong>&nbsp;&nbsp;n.&nbsp;<span class="label label-default"><s:property value="#rowstatus.index"/></span>
                                                <a href="<s:url action="trashWidgetFromPage" namespace="/do/Portal/WidgetType">
                                                       <s:param name="pageCode"><s:property value="#singlePage.code"/></s:param>
                                                       <s:param name="frame"><s:property value="#rowstatus.index"/></s:param>
                                                       <s:param name="widgetTypeCode"><s:property value="#showletTypeVar.code"/></s:param>
                                                   </s:url>" title="<s:text name="label.clear" />:&#32;<s:property value="#rowstatus.index"/>&#32;&middot;&#32;<s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/>" class="pull-right">
                                                    <span class="fa fa-trash-o fa-lg"></span>
                                                </a>
                                            </div>
                                        </div>
                                    </li>
                                </s:if>
                            </s:iterator>
                        </ul>
                    </div>
                </s:iterator>
                <div class="content-view-pf-pagination clearfix">
                    <div class="form-group">
                        <span><s:include
                                value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                        <div class="mt-5">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                        </div>
                    </div>
                </div>
            </wpsa:subset>
        </s:form>
    </s:if>
    <s:else>
        <p><s:text name="note.widget.noUse" /></p>
    </s:else>
</div>
