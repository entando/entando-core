<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li class="page-title-container">
        <s:text name="title.changePassword" />
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.changePassword" />
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">
    <s:form action="changePassword" cssClass="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.FieldErrors" />
                </strong>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.ActionErrors" /></strong>
                <ul class="margin-base-top">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <p class="sr-only">
            <wpsf:hidden name="username" />
        </p>
        <%-- old password --%>
        <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['oldPassword']}" />
        <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <div class="col-xs-12">
                <label for="oldPassword"><s:text name="label.oldPassword" /></label>
                <wpsf:password name="oldPassword" id="oldPassword" cssClass="form-control" />
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}"><s:property escapeHtml="false" />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- password --%>
        <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['password']}" />
        <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <div class="col-xs-12">
                <label for="password"><s:text name="label.password" /></label>
                <wpsf:password name="password" id="password" cssClass="form-control" />
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}"><s:property escapeHtml="false" />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- password confirm --%>
        <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['passwordConfirm']}" />
        <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <div class="col-xs-12">
                <label for="passwordConfirm"><s:text name="label.passwordConfirm" /></label>
                <wpsf:password name="passwordConfirm" id="passwordConfirm" cssClass="form-control" />
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}"><s:property escapeHtml="false" />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- save button --%>
        <div class="form-group">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
