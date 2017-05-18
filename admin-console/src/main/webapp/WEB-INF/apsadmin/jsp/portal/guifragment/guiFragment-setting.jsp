<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li class="page-title-container"><a href='<s:url action="list" namespace="/do/Portal/GuiFragment" />'>
            <span><s:text name="title.guiFragmentManagement" /></a>
    </li>
    <li class="page-title-container"><s:text name="label.settings" /></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="label.settings" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.guiFragments.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern" id="frag-tab">
                <li><a href="<s:url action="list" namespace="/do/Portal/GuiFragment"/>" role="tab"><s:text name="label.list" /></a></li>
                <li class="active"><a href="<s:url action="systemParams" namespace="/do/Portal/GuiFragment"/>" role="tab"><s:text name="label.settings" /></a></li>
            </ul>
        </div>
    </div>
</div>
<br/>


<div class="nav">
    <!-- Tab panes -->
    <div class="tab-content margin-large-bottom">
        <div class="tab-pane" id="frag-list">
        </div>
        <div class="tab-pane active" id="frag-settings">

            <s:form action="updateSystemParams" cssClass="form-horizontal">
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
                <fieldset class="col-xs-12 margin-large-top">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-3 col-label">
                                <span class="display-block"><s:text name="title.sysconfig.editEmptyFragmentEnabled" /></span>
                            </div>
                            <div class="col-xs-9 text-left">
                                <s:set var="paramName" value="'editEmptyFragmentEnabled'" />
                                <input type="hidden" value="<s:property value="systemParams[#paramName]" />" id="<s:property value="#paramName"/>"
                                       name="<s:property value="#paramName"/>" />
                                <input type="checkbox" value="<s:property value="systemParams[#paramName]" />" id="ch_<s:property value="#paramName"/>" class="bootstrap-switch"
                                       <s:if test="systemParams[#paramName] == 'true'">checked="checked"</s:if> />
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
    </div>
</div>

<script type="application/javascript" >
    $('input[type="checkbox"][id^="ch_"]').on('switchChange.bootstrapSwitch', function (ev, data) {
    var id = ev.target.id.substring(3);
    console.log("id", id);
    var $element = $('#'+id);
    $element.attr('value', ''+data);
    });
</script>
