<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.integrations" /></li>
    <li><s:text name="title.apiManagement" /></li>
    <li><a href="<s:url action="list" />"><s:text name="title.apiConsumerManagement" /></a></li>
    <li class="page-title-container">
        <s:if test="strutsAction == 1">
            <s:text name="title.apiConsumerManagement.new" />
        </s:if>
        <s:if test="strutsAction == 2">
            <s:text name="title.apiConsumerManagement.edit" />
        </s:if>
    </li>
</ol>

<h1 class="page-title-container">
    <s:if test="strutsAction == 1">
        <s:text name="title.apiConsumerManagement.new" />
    </s:if>
    <s:if test="strutsAction == 2">
        <s:text name="title.apiConsumerManagement.edit" />
    </s:if>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.api.resources.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">
    <s:form action="save"  cssClass="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                <ul class="margin-base-top">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <p class="sr-only">
            <wpsf:hidden name="strutsAction" />
            <s:if test="strutsAction == 2">
                <wpsf:hidden name="consumerKey" />
            </s:if>
        </p>

        <s:set var="currentFieldErrorsVar" value="%{fieldErrors['consumerKey']}" />
        <s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <label class="col-sm-2 control-label" for="consumerKey"><s:text name="label.consumerKey" /></label>
            <div class="col-sm-10">
                <wpsf:textfield name="consumerKey" id="consumerKey" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
                <s:if test="#currentFieldHasFieldErrorVar">
                    <span class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></span>
                </s:if>
            </div>
        </div>
        <s:set var="currentFieldErrorsVar" value="%{fieldErrors['secret']}" />
        <s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <label class="col-sm-2 control-label" for="secret"><s:text name="label.secret" /></label>
            <div class="col-sm-10">
                <wpsf:textfield name="secret" id="secret" cssClass="form-control" />
                <s:if test="#currentFieldHasFieldErrorVar">
                    <span class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></span>
                </s:if>
            </div>
        </div>
        <s:set var="currentFieldErrorsVar" value="%{fieldErrors['description']}" />
        <s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <label class="col-sm-2 control-label" for="description"><s:text name="label.description" /></label>
            <div class="col-sm-10">
                <s:textarea  cols="50" rows="3" name="description" id="description" cssClass="form-control"  />
                <s:if test="#currentFieldHasFieldErrorVar">
                    <span class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></span>
                </s:if>
            </div>
        </div>
        <s:set var="currentFieldErrorsVar" value="%{fieldErrors['expirationDate']}" />
        <s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <label class="col-sm-2 control-label" for="expirationDate_cal"><s:text name="label.expirationDate" /></label>
            <div class="col-sm-10">
                <wpsf:textfield name="expirationDate" id="expirationDate_cal" cssClass="form-control datepicker" placeholder="dd/mm/yyyy" title="dd/mm/yyyy"/>
                <s:if test="#currentFieldHasFieldErrorVar">
                    <span class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></span>
                </s:if>
            </div>
        </div>
        <%-- save buttons --%>
        <div class="col-md-12">
            <div class="form-group pull-right ">
                <div class="btn-group">
                    <wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">

                        <s:text name="label.save" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>

