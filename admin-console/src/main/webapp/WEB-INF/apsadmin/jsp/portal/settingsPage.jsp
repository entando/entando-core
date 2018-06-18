<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li class="page-title-container"><s:text name="title.settingsPage" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.settingsPage" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.settingsPage.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"><s:text name="label.requiredFields" /></div>
</div>
<br>

<div id="main" role="main">
    <s:form action="updateSystemParams">
        <s:if test="hasActionMessages()">
            <div class="alert alert-success">
                <span class="pficon pficon-ok"></span>
                <strong><s:text name="messages.confirm" /></strong>
                <ul>
                    <s:iterator value="actionMessages">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>

        <fieldset class="col-xs-12 settings-form">
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 ">
                        <span for="admin-settings-area-homePageCode"><s:text name="sysconfig.homePageCode" /></span>
                        <i class="fa fa-asterisk required-icon"></i>
                    </div>
                    <div class="col-xs-10">
                        <s:set var="paramName" value="'homePageCode'" />
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/selectPageParamBlock.jsp" />
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 ">
                        <span for="admin-settings-area-notFoundPageCode"><s:text name="sysconfig.notFoundPageCode" /></span>
                        <i class="fa fa-asterisk required-icon"></i>
                    </div>
                    <div class="col-xs-10">
                        <s:set var="paramName" value="'notFoundPageCode'" />
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/selectPageParamBlock.jsp" />
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 ">
                        <span for="admin-settings-area-errorPageCode"><s:text name="sysconfig.errorPageCode" /></span>
                        <i class="fa fa-asterisk required-icon"></i>
                    </div>
                    <div class="col-xs-10">
                        <s:set var="paramName" value="'errorPageCode'" />
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/selectPageParamBlock.jsp" />
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 ">
                        <span for="admin-settings-area-loginPageCode"><s:text name="sysconfig.loginPageCode" /></span>
                        <i class="fa fa-asterisk required-icon"></i>
                    </div>
                    <div class="col-xs-10">
                        <s:set var="paramName" value="'loginPageCode'" />
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/selectPageParamBlock.jsp" />
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <s:set var="paramName" value="'baseUrl'" />
                    <div class="col-xs-2 ">
                        <span><s:text name="sysconfig.baseURL" />
                            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="sysconfig.baseURL.contextName.help" />" data-placement="top" data-original-title="">
                                <span class="fa fa-info-circle"></span>
                            </a>
                        </span>
                    </div>
                    <div class="col-xs-10 text-left">
                        <div class="btn-group" data-toggle="buttons">
                            <label class="btn btn-default <s:if test="%{systemParams[#paramName].equals('relative')}">active</s:if>">
                                <input type="radio" class="radiocheck" id="admin-settings-area-urlStyle-relative" name="<s:property value="#paramName"/>" value="relative" <s:if test="%{systemParams[#paramName].equals('relative')}">checked="checked"</s:if> />
                                <s:text name="baseURL.relative" />
                            </label>
                            <label class="btn btn-default <s:if test="%{systemParams[#paramName].equals('request')}">active</s:if>">
                                <input type="radio" class="radiocheck" id="admin-settings-area-urlStyle-baseUrl" name="<s:property value="#paramName"/>" value="request" <s:if test="%{systemParams[#paramName].equals('request')}">checked="checked"</s:if> />
                                <s:text name="baseURL.request" />
                            </label>
                            <label class="btn btn-default <s:if test="%{systemParams[#paramName].equals('static')}">active</s:if>">
                                <input type="radio" class="radiocheck" id="admin-settings-area-urlStyle-static" name="<s:property value="#paramName"/>" value="static" <s:if test="%{systemParams[#paramName].equals('static')}">checked="checked"</s:if> />
                                <s:text name="baseURL.static" />
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 ">
                        <s:text name="sysconfig.baseURL.contextName" />
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="sysconfig.baseURL.contextName2.help" />" data-placement="top" data-original-title="">
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </div>
                    <div class="col-xs-4 text-left">
                        <s:set var="paramName" value="'baseUrlContext'" />
                        <input type="hidden"
                               value="<s:property value="systemParams[#paramName]" />"
                               id="<s:property value="#paramName"/>"
                               name="<s:property value="#paramName"/>" />
                        <input
                            type="checkbox"
                            value="<s:property value="systemParams[#paramName]" />"
                            id="ch_<s:property value="#paramName"/>"
                            class="bootstrap-switch"
                            <s:if test="systemParams[#paramName] == 'true'">checked="checked"</s:if> >
                        </div>

                        <div class="col-xs-2 ">
                        <s:text name="sysconfig.useJsessionId" />
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="sysconfig.useJsessionId.help" />" data-placement="top" data-original-title="">
                            <span class="fa fa-info-circle"></span>
                        </a>

                    </div>
                    <div class="col-xs-4 text-left">
                        <s:set var="paramName" value="'useJsessionId'" />
                        <input type="hidden"
                               value="<s:property value="systemParams[#paramName]" />"
                               id="<s:property value="#paramName"/>"
                               name="<s:property value="#paramName"/>" />
                        <input
                            type="checkbox"
                            value="<s:property value="systemParams[#paramName]" />"
                            id="ch_<s:property value="#paramName"/>"
                            class="bootstrap-switch"
                            <s:if test="systemParams[#paramName] == 'true'">checked="checked"</s:if> >
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-2 ">
                        <s:text name="sysconfig.lang.browser" />
                    </div>
                    <div class="col-xs-4 text-left">
                        <s:set var="paramName" value="'startLangFromBrowser'" />
                        <input type="hidden"
                               value="<s:property value="systemParams[#paramName]" />"
                               id="<s:property value="#paramName"/>"
                               name="<s:property value="#paramName"/>" />
                        <input
                            type="checkbox"
                            value="<s:property value="systemParams[#paramName]" />"
                            id="ch_<s:property value="#paramName"/>"
                            class="bootstrap-switch"
                            <s:if test="systemParams[#paramName] == 'true'">checked="checked"</s:if> >
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                    <s:set var="paramName" value="'treeStyle_page'" />
                    <div class="col-xs-2 ">
                        <span class="display-block"><s:text name="sysconfig.chooseYourPagesTreeStyle" /></span>
                    </div>
                    <div class="col-xs-4 text-left">
                        <div class="btn-group" data-toggle="buttons">

                            <label class="btn btn-default <s:if test="systemParams[#paramName] == 'classic'"> active</s:if>">
                                <input type="radio" class="radiocheck" id="admin-settings-area-<s:property value="#paramName"/>_classic" name="<s:property value="#paramName"/>" value="classic" <s:if test="systemParams[#paramName] == 'classic'">checked="checked"</s:if> />
                                <s:text name="treeStyle.classic" />
                            </label>
                            <label class="btn btn-default <s:if test="systemParams[#paramName] == 'request'"> active</s:if>">
                                <input type="radio" class="radiocheck" id="admin-settings-area-<s:property value="#paramName"/>_request" name="<s:property value="#paramName"/>" value="request" <s:if test="systemParams[#paramName] == 'request'">checked="checked"</s:if> />
                                <s:text name="treeStyle.request" />
                            </label>
                        </div>
                    </div>

                    <s:set var="paramName" value="'urlStyle'" />
                    <div class="col-xs-2 ">
                        <s:text name="sysconfig.URLstyle" />
                    </div>
                    <div class="col-xs-4 text-left">
                        <div class="btn-group" data-toggle="buttons">
                            <label class="btn btn-default <s:if test="systemParams['urlStyle'] == 'classic'"> active</s:if>">
                                <input type="radio" class="radiocheck" id="admin-settings-area-urlStyle-classic" name="urlStyle" value="classic" <s:if test="systemParams['urlStyle'] == 'classic'">checked="checked"</s:if> />
                                <s:text name="URLstyle.classic" />
                            </label>
                            <label class="btn btn-default <s:if test="systemParams['urlStyle'] == 'breadcrumbs'"> active</s:if>">
                                <input type="radio" class="radiocheck" id="admin-settings-area-urlStyle-breadcrumbs" name="urlStyle" value="breadcrumbs" <s:if test="systemParams['urlStyle'] == 'breadcrumbs'">checked="checked"</s:if> />
                                <s:text name="URLstyle.breadcrumbs" />
                            </label>
                        </div>
                    </div>

                </div>
            </div>
            
            <wpsa:hookPoint key="core.pageSettings" objectName="hookPointElements_core_pageSettings">
                <s:iterator value="#hookPointElements_core_pageSettings" var="hookPointElementVar">
                    <wpsa:include value="%{#hookPointElementVar.filePath}"></wpsa:include>
                </s:iterator>
            </wpsa:hookPoint>
            
        </fieldset>
        
        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>

</div>

<script type="application/javascript" >
    $('input[type="checkbox"][id^="ch_"]').on('switchChange.bootstrapSwitch', function (ev, data) {
    var id = ev.target.id.substring(3);
    console.log("id", id);
    var $element = $('#'+id);
    $element.attr('value', ''+data);
    });
</script>


