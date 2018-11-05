<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:if test="strutsAction != 2">
    <script
            src="<wp:resourceURL />administration/js/generate-code-from-title.js"></script>
    <script>
        $(document).ready(function () {
            generateCodeFromTitle('description', 'name');
        });
    </script>
</s:if>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.userSettings"/></li>
    <li><a href="<s:url namespace="/do/Role" action="list" />"> <s:text
            name="title.roleManagement"/>
    </a></li>
    <li class="page-title-container"><s:if
            test="getStrutsAction() == 1">
        <s:text name="title.roleManagement.roleNew"/>
    </s:if> <s:if test="getStrutsAction() == 2">
        <s:text name="title.roleManagement.roleEdit"/>
    </s:if></li>
</ol>

<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="title.roleManagement.roleNew"/>
    </s:if>
    <s:if test="getStrutsAction() == 2">
        <s:text name="title.roleManagement.roleEdit"/>
    </s:if>
    <span class="pull-right">
        <a tabindex="0" role="button"
           data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="page.role.help" />" data-placement="left"
           data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>


<i class="fa fa-asterisk required-icon"></i>
<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields"/>
    </div>
</div>
<br/>
<s:form action="save" cssClass="form-horizontal">
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"
                    aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <s:text name="message.title.FieldErrors"/>
            <ul class="margin-base-top">
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escapeHtml="false"/></li>
                    </s:iterator>
                </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"
                    aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <s:text name="message.title.ActionErrors"/>
            <ul class="margin-base-top">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false"/></li>
                </s:iterator>
            </ul>
        </div>
    </s:if>
    <p class="sr-only">
        <wpsf:hidden name="strutsAction"/>
        <s:if test="getStrutsAction() == 2">
            <wpsf:hidden name="name"/>
        </s:if>
    </p>

    <!-- NAME -->

    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['description']}"/>
    <s:set var="fieldHasFieldErrorVar"
           value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()"/>
    <s:set var="controlGroupErrorClassVar"
           value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}"/>
    <div
            class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <div class="col-xs-12">
            <label class="control-label col-sm-2" for="description"><s:text
                    name="label.name"/>&nbsp; <i class="fa fa-asterisk required-icon"
                                                 style="position: relative; top: -4px; right: 0px"></i> <a
                    role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                    data-html="true" title="" data-placement="top"
                    data-content="<s:text name="help.role.name" /> "> <span
                    class="fa fa-info-circle"></span></a></label>
            <div class="col-xs-10">
                <wpsf:textfield name="description" id="description"
                                cssClass="form-control"/>
                <s:if test="#fieldHasFieldErrorVar">
                    <p class="text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}">
                            <s:property/>
                            &#32;
                        </s:iterator>
                    </p>
                </s:if>
            </div>
        </div>
    </div>

    <!-- CODE -->
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['name']}"/>
    <s:set var="fieldHasFieldErrorVar"
           value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()"/>
    <s:set var="controlGroupErrorClassVar"
           value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}"/>
    <div
            class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <div class="col-xs-12">
            <label class="control-label col-sm-2" for="name"><s:text
                    name="label.code"/>&nbsp; <i class="fa fa-asterisk required-icon"
                                                 style="position: relative; top: -4px; right: 0px"></i> <a
                    role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                    data-html="true" title="" data-placement="top"
                    data-content="<s:text name="help.role.code" /> "> <span
                    class="fa fa-info-circle"></span></a></label>
            <div class="col-xs-10">
                <wpsf:textfield name="name" id="name"
                                disabled="%{getStrutsAction() == 2}" cssClass="form-control"/>
                <s:if test="#fieldHasFieldErrorVar">
                    <p class="text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}">
                            <s:property/>
                            &#32;
                        </s:iterator>
                    </p>
                </s:if>
            </div>
        </div>
    </div>

    <legend><s:text name="name.permissions"/></legend>
    <div class="form-group">
        <div class="col-xs-12">
            <s:set var="permissionNamesVar" value="permissionNames"/>
            <div class="col-sm-12 col-12-checkbox-list">
                <s:iterator value="%{systemPermissions}" var="permissionVar">
                    <div class="col-xs-4">
                        <label class="control-label col-sm-8" for="<s:property value="%{#permissionVar.name}" />">
                            <s:property value="%{#permissionVar.description}"/>
                        </label>
                        <div class="col-xs-1">
                            <input type="checkbox" class="bootstrap-switch"
                                   name="permissionNames" id="<s:property value="%{#permissionVar.name}" />"
                                   value="<s:property value="%{#permissionVar.name}" />"
                                    <s:if test="%{#permissionNamesVar.contains(#permissionVar.name)}"> checked="checked" </s:if> />
                        </div>
                    </div>
                </s:iterator>
            </div>
        </div>
    </div>

    <%-- save button --%>
    <div class="col-xs-12">
        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit type="button" action="save"
                             cssClass="btn btn-primary pull-right">
                    <s:text name="label.save"/>
                </wpsf:submit>
            </div>
        </div>
    </div>
</s:form>
