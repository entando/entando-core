<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.userSettings" /></li>
    <li class="page-title-container"><s:text name="title.userSettings" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.userSettings" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.userSettings.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">
    <s:form action="updateSystemParams">
        <s:if test="hasActionMessages()">
            <div class="alert alert-success">
                <span class="pficon pficon-ok"></span>
                <strong><s:text name="messages.confirm" /></strong>
                <s:iterator value="actionMessages">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </div>
        </s:if>

        <fieldset class="col-xs-12 margin-large-top">
            <legend><s:text name="sysconfig.legend.privacyModule" /></legend>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-4 col-label">
                        <span class="display-block"><s:text name="label.active" /></span>
                    </div>
                    <div class="col-xs-4 text-left">
                        <s:set var="paramName" value="'extendedPrivacyModuleEnabled'" />
                        <input type="hidden" value="<s:property value="systemParams[#paramName]" />" id="<s:property value="#paramName"/>" name="<s:property value="#paramName"/>" />
                        <input type="checkbox" value="<s:property value="systemParams[#paramName]" />"  id="ch_<s:property value="#paramName"/>" class="bootstrap-switch"
                               <s:if test="systemParams[#paramName] == 'true'">checked="checked"</s:if> >
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                    <s:set var="paramName" value="'maxMonthsSinceLastAccess'" />
                    <div class="col-xs-4 col-label">
                        <span class="display-block bold"><s:text name="sysconfig.maxMonthsSinceLastAccess" /></span>:   
                        <s:if test="systemParams[#paramName] != 0"><span class="label label-info bold check"><s:property value="systemParams[#paramName]"/></span></s:if>
                        </div>
                        <div class="col-xs-4 text-left">
                            <input type="text" id="admin-settings-area-<s:property value="#paramName"/>_input" name="<s:property value="#paramName"/>" value="<s:property value="systemParams[#paramName]"/>" style="display: none"/>
                        <div class="btn-group" data-toggle="buttons">
                            <label class="btn btn-default <s:if test="systemParams[#paramName] != 0"> active</s:if>" onclick="setCustomValue('<s:property value="#paramName"/>')" id="admin-settings-area-<s:property value="#paramName"/>_label">
                                <input type="radio" class="radiocheck" id="admin-settings-area-<s:property value="#paramName"/>_value" name="<s:property value="#paramName"/>" value="<s:property value="systemParams[#paramName]"/>" <s:if test="systemParams[#paramName] != 0">checked="checked"</s:if> />
                                <s:text name="sysconfig.months" />
                                <%--<s:property value="systemParams[#paramName]"/>--%>
                            </label>
                            <label class="btn btn-default <s:if test="systemParams[#paramName] == 0"> active</s:if>" onclick="hideCustomValue('<s:property value="#paramName"/>')">
                                <input type="radio" class="radiocheck" id="admin-settings-area-<s:property value="#paramName"/>_false" name="<s:property value="#paramName"/>" value="0" <s:if test="systemParams[#paramName] == 0">checked="checked"</s:if> />
                                <s:text name="sysconfig.disable" />
                            </label>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <s:set var="paramName" value="'maxMonthsSinceLastPasswordChange'" />
                    <div class="col-xs-4 col-label">
                        <span class="display-block bold"><s:text name="sysconfig.maxMonthsSinceLastPasswordChange" /></span>:   
                        <s:if test="systemParams[#paramName] != 0"><span class="label label-info bold check"><s:property value="systemParams[#paramName]"/></span></s:if>
                        </div>
                        <div class="col-xs-4 text-left">
                            <input type="text" id="admin-settings-area-<s:property value="#paramName"/>_input" name="<s:property value="#paramName"/>" value="<s:property value="systemParams[#paramName]"/>" style="display: none"/>
                        <div class="btn-group" data-toggle="buttons">
                            <label class="btn btn-default <s:if test="systemParams[#paramName] != 0"> active</s:if>" onclick="setCustomValue('<s:property value="#paramName"/>')" id="admin-settings-area-<s:property value="#paramName"/>_label">
                                <input type="radio" class="" id="admin-settings-area-<s:property value="#paramName"/>_value" name="<s:property value="#paramName"/>" value="<s:property value="systemParams[#paramName]"/>" <s:if test="systemParams[#paramName] != 0">checked="checked"</s:if> />
                                <s:text name="sysconfig.months" />
                                <%--<s:property value="systemParams[#paramName]"/>--%>
                            </label>
                            <label class="btn btn-default <s:if test="systemParams[#paramName] == 0"> active</s:if>" onclick="hideCustomValue('<s:property value="#paramName"/>')">
                                <input type="radio" class="" id="admin-settings-area-<s:property value="#paramName"/>_false" name="<s:property value="#paramName"/>" value="0" <s:if test="systemParams[#paramName] == 0">checked="checked"</s:if> />
                                <s:text name="sysconfig.disable" />
                            </label>
                        </div>

                    </div>

                </div>
            </div>
            <legend><s:text name="sysconfig.legend.misc" /></legend>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-4 col-label">
                        <span class="display-block"><s:text name="sysconfig.gravatarIntegrationEnabled" /></span>
                    </div>
                    <div class="col-xs-4 text-left">
                        <s:set var="paramName" value="'gravatarIntegrationEnabled'" />
                        <input type="hidden" value="<s:property value="systemParams[#paramName]" />" id="<s:property value="#paramName"/>" name="<s:property value="#paramName"/>" />
                        <input type="checkbox"  value="<s:property value="systemParams[#paramName]" />" id="ch_<s:property value="#paramName"/>" class="bootstrap-switch"
                               <s:if test="systemParams[#paramName] == 'true'">checked="checked"</s:if> >
                        </div>
                    </div>
                </div>
            </fieldset>

            <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                <s:text name="label.save" />
            </wpsf:submit>
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

<script type="application/javascript" >
    function setCustomValue(idPar){
    $('#admin-settings-area-'+idPar+'_input').show();
    $('#admin-settings-area-'+idPar+'_value').hide();
    $('#admin-settings-area-'+idPar+'_label').hide();
    }
    function hideCustomValue(idPar){
    $('#admin-settings-area-'+idPar+'_input').hide();
    $('#admin-settings-area-'+idPar+'_value').show();
    $('#admin-settings-area-'+idPar+'_label').show();
    $("#admin-settings-area-"+idPar+"_input").val("0");
    }
</script>
