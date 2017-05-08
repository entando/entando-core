<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
    
<div class="nav">
    <ul class="nav nav-tabs pull-right margin-large-bottom" id="frag-tab">
        <li><a href="<s:url action="list" namespace="/do/Portal/GuiFragment"/>" role="tab"><s:text name="label.list" /></a></li>
        <li class="active"><a href="<s:url action="systemParams" namespace="/do/Portal/GuiFragment"/>" role="tab"><s:text name="label.settings" /></a></li>
    </ul>
        
    <!-- Tab panes -->
    <div class="tab-content margin-large-bottom">
        <div class="tab-pane" id="frag-list">  
        </div>
        <div class="tab-pane active" id="frag-settings">
            <ol class="breadcrumb" style="margin-top: 40px">
                <li><s:text name="title.ux_patterns" /></li>
                <li class="page-title-container"><s:text name="title.guiFragmentManagement" /></li>
                <li><s:text name="label.settings" /></li>
            </ol>
                
                
            <h1 class="page-title-container">
                <s:text name="title.guiFragmentManagement" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
                </span>
            </h1>
                
            <div class="text-right">
                <div class="form-group-separator"></div>
            </div>
            <br>
            <s:form action="updateSystemParams" cssClass="form-horizontal">
                <s:if test="hasActionMessages()">
                    <div class="alert alert-success alert-dismissable fade in">
                        <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                        <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
                        <ul class="margin-base-top">
                            <s:iterator value="actionMessages">
                                <li><s:property escapeHtml="false" /></li>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>
                <fieldset class="col-xs-12 margin-large-top">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-4 col-label">
                                <span class="display-block"><s:text name="sysconfig.editEmptyFragmentEnabled" /></span>
                            </div>
                            <div class="col-xs-4 text-left">
                                <s:set var="paramName" value="'editEmptyFragmentEnabled'" />
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