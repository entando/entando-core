<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li  class="page-title-container"><s:text name="title.myProfile" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.myProfile" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="label.user.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<i class="fa fa-asterisk required-icon"></i>
<div class="text-right">
    <div class="form-group-separator"><s:text name="label.requiredFields" /></div>               
</div>
<br/>

<div id="main" role="main">
    
    <div class="col-md-12">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.FieldErrors" /></strong>.
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                <ul>
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        
        <s:if test="hasActionMessages()">
            <div class="alert alert-success alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-ok"></span>
                <strong><s:text name="messages.confirm" /></strong>
                <ul>
                    <s:iterator value="actionMessages">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        
    </div>
    
    <div>
        <legend><s:text name="title.changePassword" /></legend>
        <s:form namespace="/do/CurrentUser" action="changePassword" cssClass="form-horizontal">            
            
            <p class="sr-only">
                <wpsf:hidden name="username" />
            </p>
            
            <div class="form-group">
                <label class="col-sm-2 control-label" for="oldPassword"><s:text name="label.oldPassword" />&#32;<i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i></label>                     
                <div class="col-sm-10">   
                    <wpsf:password name="oldPassword" id="oldPassword" cssClass="form-control" />
                </div>
            </div>
            
            <div class="form-group">
                <label class="col-sm-2 control-label" for="password" ><s:text name="label.password" />&#32;<i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i></label>
                <div class="col-sm-10">    
                    <wpsf:password name="password" id="password" cssClass="form-control" />
                </div>
            </div>
            
            <div class="form-group">
                <label class="col-sm-2 control-label" for="passwordConfirm"><s:text name="label.passwordConfirm" />&#32;<i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i></label>
                <div class="col-sm-10">
                    <wpsf:password name="passwordConfirm" id="passwordConfirm" cssClass="form-control" />
                </div>
            </div>
            
            <div class="col-md-12"> 
                <div class="form-group pull-right "> 
                    <div class="btn-group">
                        <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                            <s:text name="label.save" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>                        
        </s:form>
            <legend><s:text name="title.editUserProfile" /></legend>
                <s:action name="edit" namespace="/do/currentuser/profile" executeResult="true"></s:action>
                <wpsa:hookPoint key="core.userProfile.entry" objectName="hookPointElements_core_userProfile_entry">
                    <s:iterator value="#hookPointElements_core_userProfile_entry" var="hookPointElement">
                        <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                    </s:iterator>
                </wpsa:hookPoint>                   
    </div>                
</div>
