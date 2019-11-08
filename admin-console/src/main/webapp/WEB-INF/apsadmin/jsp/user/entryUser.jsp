<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>


<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.userSetting" /></li>
    <li>
        <a href="<s:url namespace="/do/User" action="list" />">
            <s:text name="title.userManagement" /></a>
    </li>
    <li class="page-title-container">
        <s:if test="getStrutsAction() == 1">
            <s:text name="title.userManagement.userNew" />
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
            <s:text name="title.userManagement.userEdit" />
        </s:elseif>
    </li>
</ol>

<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="title.userManagement.userNew" />
    </s:if>
    <s:elseif test="getStrutsAction() == 2">
        <s:text name="title.userManagement.userEdit" />
    </s:elseif>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="help.user.page" />" data-placement="left"
           data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true">
            </i>
        </a>
    </span>
</h1>

<i class="fa fa-asterisk required-icon"></i>
<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields" />
    </div>
</div>
<br />

<s:form action="save" cssClass="form-horizontal">
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"
                    aria-hidden="true">
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
        <wpsf:hidden name="strutsAction" />
        <s:if test="getStrutsAction() == 2">
            <wpsf:hidden name="username" />
        </s:if>
    </p>

    <%-- username --%>
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['username']}" />
    <s:set var="fieldHasFieldErrorVar"
           value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar"
           value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />


    <%-- password --%>
    <s:set var="fieldFieldErrorsVar2" value="%{fieldErrors['password']}" />
    <s:set var="fieldHasFieldErrorVar2"
           value="#fieldFieldErrorsVar2 != null && !#fieldFieldErrorsVar2.isEmpty()" />
    <s:set var="controlGroupErrorClassVar2"
           value="%{#fieldHasFieldErrorVar2 ? ' has-error' : ''}" />

    <s:if test="#fieldHasFieldErrorVar || #fieldHasFieldErrorVar2 ">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"
                    aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <ul class="margin-base-vertical">
                <s:text name="title.userManagement.errorOnForms" />
            </ul>
        </div>
    </s:if>

    <div
            class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <label class="col-sm-2 control-label" for="username"><s:text
                name="username" />&nbsp; <i class="fa fa-asterisk required-icon"
                                            style="position: relative; top: -4px; right: 0px"></i> <a
                role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                data-html="true" title="" data-placement="top"
                data-content="<s:text name="help.user.username" /> "> <span
                class="fa fa-info-circle"></span></a></label>
        <div class="col-sm-10">
            <wpsf:textfield name="username" id="username"
                            disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger"> <s:iterator
                        value="%{#fieldFieldErrorsVar}">
                    <s:property />
                    &#32;
                </s:iterator>
                </span>
            </s:if>
        </div>
    </div>

    <div
            class="form-group<s:property value="#controlGroupErrorClassVar2" />">
        <label class="col-sm-2 control-label" for="password"><s:text
                name="password" />&nbsp; <i class="fa fa-asterisk required-icon"
                                            style="position: relative; top: -4px; right: 0px"></i> <a
                role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                data-html="true" title="" data-placement="top"
                data-content="<s:text name="help.user.password" /> "> <span
                class="fa fa-info-circle"></span></a></label>
        <div class="col-sm-10">
            <wpsf:password name="password" id="password" cssClass="form-control" />

            <s:if test="#fieldHasFieldErrorVar2">
                <span class="help-block text-danger"> <s:iterator
                        value="%{#fieldFieldErrorsVar2}">
                    <s:property />
                    &#32;
                </s:iterator>
                </span>
            </s:if>
        </div>
    </div>
    <%-- confirm password --%>
    <s:set var="fieldFieldErrorsVar3"
           value="%{fieldErrors['passwordConfirm']}" />
    <s:set var="fieldHasFieldErrorVar3"
           value="#fieldFieldErrorsVar3 != null && !#fieldFieldErrorsVar3.isEmpty()" />
    <s:set var="controlGroupErrorClassVar3"
           value="%{#fieldHasFieldErrorVar3 ? ' has-error' : ''}" />

    <div
            class="form-group<s:property value="#controlGroupErrorClassVar3" />">
        <label class="col-sm-2 control-label" for="passwordConfirm"><s:text
                name="passwordConfirm" /></label>
        <div class="col-sm-10">
            <wpsf:password name="passwordConfirm" id="passwordConfirm"
                           cssClass="form-control" />

            <s:if test="#fieldHasFieldErrorVar3">
                <span class="help-block text-danger"> <s:iterator
                        value="%{#fieldFieldErrorsVar3}">
                    <s:property />
                    &#32;
                </s:iterator>
                </span>
            </s:if>

        </div>
    </div>

    <%-- additional info when edit mode --%>
    <s:if test="getStrutsAction() == 2">

        <div class="blank-slate-pf-custom">
                <%-- registration date --%>
            <div class="form-group">

                <label class="col-sm-2 control-label"><s:text
                        name="label.date.registration" /></label>
                <div class="col-sm-10">
                    <s:date name="user.creationDate" format="dd/MM/yyyy HH:mm" />
                </div>

            </div>
                <%-- last login --%>
            <div class="form-group">

                <label class="col-sm-2 control-label"><s:text
                        name="label.date.lastLogin" /></label>
                <div class="col-sm-10">
                    <s:if test="user.lastAccess != null">
                        <s:date name="user.lastAccess" format="dd/MM/yyyy HH:mm" />
                        <s:if test="!user.accountNotExpired">
                            <span class="text-muted">&#32;(<s:text
                                    name="note.userStatus.expiredAccount" />)
                            </span>
                        </s:if>
                    </s:if>
                    <s:else>
                        <span class="icon fa fa-minus"
                              title="<s:text name="label.none" />"><span class="sr-only"><s:text
                                name="label.none" /></span></span>
                    </s:else>
                </div>

            </div>
                <%-- last password change --%>
            <div class="form-group">
                <label class="col-sm-2 control-label"><s:text
                        name="label.date.lastPasswordChange" /></label>
                <div class="col-sm-10">
                    <s:if test="user.lastPasswordChange != null">
                        <s:date name="user.lastPasswordChange" format="dd/MM/yyyy HH:mm" />
                        <s:if test="!user.credentialsNotExpired">
                            <span class="text-muted">&#32;(<s:text
                                    name="note.userStatus.expiredPassword" />)
                            </span>
                        </s:if>
                    </s:if>
                    <s:else>
                        <span class="icon fa fa-minus"
                              title="<s:text name="label.none" />"><span class="sr-only"><s:text
                                name="label.none" /></span></span>
                    </s:else>
                </div>
            </div>

                <%-- reset info --%>
            <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['reset']}" />
            <s:set var="fieldHasFieldErrorVar"
                   value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
            <s:set var="controlGroupErrorClassVar"
                   value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
            <div
                    class="form-group<s:property value="#controlGroupErrorClassVar" />">
                <label class="col-sm-2 control-label" for="profileType">
                    <s:text name="label.reset.User" />

                </label>
                <div class="col-sm-10">
                    <wpsf:checkbox name="reset" cssClass="bootstrap-switch" />
                        <%--<s:text name="note.userStatus.reset" />--%>
                    <s:if test="#fieldHasFieldErrorVar">
                        <span class="help-block text-danger"> <s:iterator
                                value="%{#fieldFieldErrorsVar}">
                            <s:property />
                            &#32;
                        </s:iterator>
                        </span>
                    </s:if>
                </div>
            </div>
        </div>
    </s:if>

    <s:else>
        <s:set var="fieldFieldErrorsVar"
               value="%{fieldErrors['profileTypeCode']}" />
        <s:set var="fieldHasFieldErrorVar"
               value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar"
               value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <label class="col-sm-2 control-label" for="profileType">
                <s:text name="label.profileType" />&nbsp;
                <i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:select name="profileTypeCode" headerKey=""
                             headerValue="%{getText('note.choose')}" id="profileType"
                             list="profileTypes" listKey="code" listValue="description"
                             cssClass="form-control" />
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger"> <s:iterator
                            value="%{#fieldFieldErrorsVar}">
                        <s:property />
                        &#32;
                    </s:iterator>
                    </span>
                </s:if>
            </div>
        </div>
    </s:else>

    <%-- active --%>
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['active']}" />
    <s:set var="fieldHasFieldErrorVar"
           value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar"
           value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
    <div
            class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <label class="col-sm-2 control-label" for="active">
            <s:text name="label.state" />
        </label>
        <div class="col-sm-10">
            <wpsf:checkbox name="active" id="active" cssClass="bootstrap-switch" />
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger"> <s:iterator
                        value="%{#fieldFieldErrorsVar}">
                    <s:property />
                    &#32;
                </s:iterator>
                </span>
            </s:if>
        </div>
    </div>

    <%-- save buttons --%>
    <div class="col-md-12">
        <div class="form-group pull-right ">
            <s:if test="strutsAction == 1">
                <div class="btn-group">
                    <wpsf:submit type="button" action="saveAndContinue"
                                 cssClass="btn btn-default btn-block">

                        <s:text name="label.saveAndEditProfile" />
                    </wpsf:submit>
                </div>
            </s:if>
            <div class="btn-group">
                <wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </div>
</s:form>
