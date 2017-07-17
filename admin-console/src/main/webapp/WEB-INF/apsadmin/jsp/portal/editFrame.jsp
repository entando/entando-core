<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a>&#32;/&#32;
    </li>
    <li>
        <a href="<s:url action="configure" namespace="/do/Page">
               <s:param name="pageCode">
                   <s:property value="currentPage.code"/>
               </s:param>
           </s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text name="title.configPage" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.editFrame" />
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.editFrame" />
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<p class="sr-only"><a href="#editFrame"><s:text name="note.goToEditFrame" /></a></p>

<div id="main" role="main">

    <s:set var="breadcrumbs_pivotPageCode" value="currentPage.code" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="currentPage.code"></s:param></s:action>

    <%-- Error message handling --%>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.ActionErrors" /></strong>
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <div id="editFrame" class="panel panel-default">
        <div class="panel-heading">
            <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
        </div>
        <div class="panel-body">
            <s:if test="showlet != null">
                <h3 class="h5 margin-small-vertical">
                    <label class="sr-only">
                        <s:text name="name.widget" />
                    </label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />">
                    </span>
                    <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                </h3>
                <p><s:text name="note.editFrame.noConfigNeeded" /></p>
            </s:if>
            <s:else>
                <p class="sr-only">
                    <s:text name="note.editFrame.chooseAWidget" />
                </p>
                <s:form action="joinWidget">
                    <p class="sr-only">
                        <wpsf:hidden name="pageCode" />
                        <wpsf:hidden name="frame" />
                    </p>
                    <div class="form-group margin-base-vertical">
                        <label for="showletCode" class="sr-only">
                            <s:text name="title.editFrame.chooseAWidget" />
                        </label>
                        <div class="input-group">
                            <span class="input-group-addon" title="<s:text name="title.editFrame.chooseAWidget" />">
                                <span class="icon fa fa-puzzle-piece"></span>
                            </span>
                            <select name="widgetTypeCode" id="showletCode" class="form-control">
                                <s:iterator var="showletFlavour" value="showletFlavours">
                                    <wpsa:set var="tmpShowletType">tmpShowletTypeValue</wpsa:set>
                                    <s:iterator var="showletType" value="#showletFlavour" >
                                        <s:if test="#showletType.optgroup != #tmpShowletType">
                                            <s:if test="#showletType.optgroup == 'stockShowletCode'">
                                                <wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.stock" /></wpsa:set>
                                            </s:if>
                                            <s:elseif test="#showletType.optgroup == 'customShowletCode'">
                                                <wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.custom" /></wpsa:set>
                                            </s:elseif>
                                            <s:elseif test="#showletType.optgroup == 'userShowletCode'">
                                                <wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.user" /></wpsa:set>
                                            </s:elseif>
                                            <s:else>
                                                <wpsa:set var="pluginPropertyName" value="%{getText(#showletType.optgroup + '.name')}" />
                                                <wpsa:set var="pluginPropertyCode" value="%{getText(#showletType.optgroup + '.code')}" />
                                                <wpsa:set var="optgroupLabel"><s:text name="%{#pluginPropertyName}" /></wpsa:set>
                                            </s:else>
                                            <optgroup label="<s:property value="#optgroupLabel" />">
                                            </s:if>
                                            <option value="<s:property value="#showletType.key" />"><s:property value="#showletType.value" /></option>
                                            <wpsa:set var="tmpShowletType"><s:property value="#showletType.optgroup" /></wpsa:set>
                                        </s:iterator>
                                    </optgroup>
                                </s:iterator>
                            </select>
                            <span class="input-group-btn">
                                <wpsf:submit type="button" cssClass="btn btn-success">
                                    <s:text name="label.confirm" />
                                </wpsf:submit>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </s:form>
    </s:else>
</div>
