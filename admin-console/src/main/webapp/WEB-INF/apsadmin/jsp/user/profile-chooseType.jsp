<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.userSetting" /></li>
    <li><a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a></li>
    <li class="page-title-container"><s:text name="title.userprofileManagement" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.userprofileManagement" />  
    <span class="pull-right"><a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="text to be inserted" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>               
</div>
<br>

<div id="main" role="main">
    <div class="form-group  "> 
        <label class="col-sm-2 control-label" style="text-align: end;">
            <s:text name="title.chooseUserProfileType" />
        </label>
        <div class="col-sm-8">
            <s:property value="username" />
        </div>
    </div>
    <br>
    <s:form action="new" cssClass="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <p class="sr-only">
            <wpsf:hidden name="username" />
        </p>

        <div class="form-group">
            <div class="col-xs-12">
                <label class="col-sm-2 control-label" for="profileTypeCode">
                    <s:text name="label.profile" />
                </label>
                <div class="col-sm-8">
                    <wpsf:select list="userProfileTypes" headerKey="" headerValue="%{getText('note.choose')}" id="profileTypeCode" name="profileTypeCode" listKey="code" listValue="description" cssClass="form-control" />
                </div>
            </div>
        </div>


        <div class="form-group">
            <div class="col-xs-12">
                <label class="col-sm-2"></label>
                <div class="col-sm-8">
                    <!--<div class="btn-group">-->
                    <wpsf:submit type="button" action="saveAndContinue" title="%{getText('label.continue')}" cssClass="btn btn-default">
                        <s:text name="label.continue" />&#32;
                    </wpsf:submit>
                    <wpsf:submit type="button" action="saveEmpty" cssClass="btn btn-primary">
                        <s:text name="label.saveEmptyProfile" />
                    </wpsf:submit>
                    <!--</div>-->
                </div>
            </div>
        </s:form>
    </div>
</div>
