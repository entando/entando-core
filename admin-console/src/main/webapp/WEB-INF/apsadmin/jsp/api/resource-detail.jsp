<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="apiResourceVar" value="apiResource" />
<s:set var="GETMethodVar" value="#apiResourceVar.getMethod" />
<s:set var="POSTMethodVar" value="#apiResourceVar.postMethod" />
<s:set var="PUTMethodVar" value="#apiResourceVar.putMethod" />
<s:set var="DELETEMethodVar" value="#apiResourceVar.deleteMethod" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.integrations" /></li>
    <li><s:text name="title.apiManagement" /></li>
    <li>
        <a href='<s:url action="list" namespace="/do/Api/Resource" />'>
            <span class="list-group-item-value"><s:text name="name.api.resource" /></span>
        </a>
    </li>
    <li class="page-title-container"><s:text name="title.apiResourceEdit" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.apiResourceEdit" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.api.resources.edit.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">
    <s:if test="hasActionMessages()">
        <div class="alert alert-info alert-dismissable fade in">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h3>
                <ul class="margin-base-top">
                    <s:iterator value="actionMessages">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
        </div>
    </s:if>
    <p class="sr-only">
        <s:text name="note.workingOn" />: <s:property value="#apiResourceVar.namespace!=null && #apiResourceVar.namespace.length()>0 ? '/' + #apiResourceVar.namespace : ''" />/<s:property value="#apiResourceVar.resourceName" />
    </p>
    <table class="table">
        <tr>
            <th class="td-pagetree-width"><s:text name="label.api.resource.name" /></th>
            <td><s:property value="#apiResourceVar.resourceName" /></td>
        </tr>
        <tr>
            <th class="td-pagetree-width"><span lang="en"><s:text name="label.api.resource.namespace" /></span></th>
            <td>/<s:property value="#apiResourceVar.namespace" /></td>
        </tr>
        <tr>
            <th class="td-pagetree-width"><s:text name="label.api.resource.description" /></th>
            <td><s:property value="#apiResourceVar.description" /></td>
        </tr>
        <tr>
            <th class="td-pagetree-width"><s:text name="label.api.resource.source" /></th>
            <td><s:property value="#apiResourceVar.source" /></td>
        </tr>
        <tr>
            <th class="td-pagetree-width"><s:text name="label.api.resource.plugin" /></th>
            <td>
                <s:if test="#apiResourceVar.pluginCode == null||#apiResourceVar.pluginCode.length == 0">
                    <s:text name="api.resource.status.na" />
                </s:if>
                <s:else>
                    <s:property value="%{getText(#apiResourceVar.pluginCode+'.name')}" />&#32;
                    (<s:property value="%{#apiResourceVar.pluginCode}" />)
                </s:else>
            </td>
        <tr>
            <th class="td-pagetree-width"><s:text name="label.api.resource.uri" /></th>
            <td>
                <c:set var="resourceUriVar"><wp:info key="systemParam" paramName="applicationBaseURL" />legacyapi/rs/<wp:info key="defaultLang" /><s:if test="null != #apiResourceVar.namespace">/<s:property value="#apiResourceVar.namespace" /></s:if>/<s:property value="#apiResourceVar.resourceName" /></c:set>
                <a href="<c:out value="${resourceUriVar}" escapeXml="false" />">
                    <span class="icon fa fa-globe"></span>&#32;
                    <c:out value="${resourceUriVar}" escapeXml="false" />
                </a>
            </td>
        </tr>
        <tr>
            <th class="td-pagetree-width"><s:text name="label.api.resource.extensions" /></th>
            <td>
                <a href="<c:out value="${resourceUriVar}" escapeXml="false" />.xml">
                    <span class="icon fa fa-globe"></span>&#32;
                    <s:text name="note.extensions.xml" />
                </a>
                <br />
                <a href="<c:out value="${resourceUriVar}" escapeXml="false" />.json">
                    <span class="icon fa fa-globe"></span>&#32;
                    <s:text name="note.extensions.json" />
                </a>
            </td>
        </tr>
    </table>

    <div class="panel">
        <legend><s:text name="label.api.options.for.all" /></legend>

        <s:form namespace="/do/Api/Resource" action="updateAllMethodStatus" cssClass="form-horizontal">
            <div class="form-group">
                <wpsf:hidden name="resourceName" value="%{#apiResourceVar.resourceName}" />
                <wpsf:hidden name="namespace" value="%{#apiResourceVar.namespace}" />
                <div class="col-sm-2 control-label">
                    <label for="all_active">
                        <s:text name="label.active" />
                    </label>
                </div>
                <div class="col-sm-10">
                    <wpsf:checkbox name="active" value="true" id="all_active" cssClass="bootstrap-switch" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <label for="all_hidden">
                        <s:text name="label.hidden" />
                    </label>
                </div>
                <div class="col-sm-10">
                    <wpsf:checkbox name="hidden" id="all_hidden" cssClass="bootstrap-switch" />
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="all_auth"><s:text name="label.api.authorization" /></label>
                <div class="col-sm-10">
                    <wpsf:select name="methodAuthority" list="methodAuthorityOptions" listKey="key" listValue="value" headerKey="" headerValue="%{getText('note.choose')}" id="all_auth" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-12">
                    <div class="pull-right">
                        <wpsf:submit type="button" action="resetAllMethodStatus" id="%{''}" cssClass="btn btn-default">
                            <s:text name="label.reset.default" />
                        </wpsf:submit>
                        <wpsf:submit type="button" action="updateAllMethodStatus" id="%{''}" cssClass="btn btn-primary">
                            <s:text name="label.update" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </s:form>

    </div>

    <%-- GET --%>
    <s:set var="methodVar" value="#GETMethodVar" />
    <s:set var="titleMethod" value="%{'GET'}" />
    <s:include value="/WEB-INF/apsadmin/jsp/api/include/resource-method-detail.jsp" />
    <%-- POST --%>
    <s:set var="methodVar" value="#POSTMethodVar" />
    <s:set var="titleMethod" value="%{'POST'}" />
    <s:include value="/WEB-INF/apsadmin/jsp/api/include/resource-method-detail.jsp" />
    <%-- PUT --%>
    <s:set var="methodVar" value="#PUTMethodVar" />
    <s:set var="titleMethod" value="%{'PUT'}" />
    <s:include value="/WEB-INF/apsadmin/jsp/api/include/resource-method-detail.jsp" />
    <%-- DELETE --%>
    <s:set var="methodVar" value="#DELETEMethodVar" />
    <s:set var="titleMethod" value="%{'DELETE'}" />
    <s:include value="/WEB-INF/apsadmin/jsp/api/include/resource-method-detail.jsp" />
</div>
