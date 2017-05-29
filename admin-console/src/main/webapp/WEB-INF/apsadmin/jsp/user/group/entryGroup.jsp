<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib prefix="wp" uri="/aps-core"%>

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
    <li><s:text name="menu.configure" /></li>
    <li><a href="<s:url action="list" namespace="/do/Group" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.groupManagement" />"><s:text
                name="title.groupManagement" /></a></li>
    <li class="page-title-container"><s:if
            test="getStrutsAction() == 1">
            <s:text name="title.groupManagement.groupNew" />
        </s:if> <s:elseif test="getStrutsAction() == 2">
            <s:text name="title.groupManagement.groupEdit" />
        </s:elseif></li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:if test="getStrutsAction() == 1">
            <s:text name="title.groupManagement.groupNew" />
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
            <s:text name="title.groupManagement.groupEdit" />
        </s:elseif>
        <span class="pull-right"> <a tabindex="0" role="button"
                                     data-toggle="popover" data-trigger="focus" data-html="true" title=""
                                     data-content="<s:text name="page.groupManagement.help" />" data-placement="left"
                                     data-original-title=""> <i class="fa fa-question-circle-o"
                                       aria-hidden="true"></i>
            </a>
        </span>
    </div>
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
        <div class="alert alert-danger alert-dismissable fade in">
            <button type="button" class="close" data-dismiss="alert"
                    aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <s:text name="message.title.ActionErrors" />
            <ul class="margin-base-top">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable fade in">
            <button type="button" class="close" data-dismiss="alert"
                    aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <s:text name="message.title.FieldErrors" />
        </div>
    </s:if>
    <p class="sr-only">
        <wpsf:hidden name="strutsAction" />
        <s:if test="getStrutsAction() == 2">
            <wpsf:hidden name="name" />
        </s:if>
    </p>

    <%-- NAME --%>
    <s:set var="fieldErrorsVar" value="%{fieldErrors['description']}" />
    <s:set var="fieldHasFieldErrorVar"
           value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar"
           value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />

    <div
        class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <label class="col-sm-2 control-label" for="description"><s:text name="label.name" />&nbsp;
            <i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i>
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="note.group.name" />" data-placement="top" data-original-title="">
                <span class="fa fa-info-circle"></span></a>
        </label>
        <div class="col-sm-10">
            <wpsf:textfield name="description" id="description"
                            cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger"> <s:iterator
                        value="#fieldErrorsVar">
                        <s:property />
                        &#32;
                    </s:iterator>
                </span>
            </s:if>
        </div>
    </div>



    <%-- CODE --%>
    <s:set var="fieldErrorsVar" value="%{fieldErrors['name']}" />
    <s:set var="fieldHasFieldErrorVar"
           value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar"
           value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />

    <div
        class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <label class="col-sm-2 control-label" for="name"> <s:text name="label.code" />&nbsp;
            <i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i>
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="note.group.code" />" data-placement="top" data-original-title="">
                <span class="fa fa-info-circle"></span></a>
        </label>
        <div class="col-sm-10">
            <wpsf:textfield name="name" id="name"
                            disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger"> <s:iterator
                        value="#fieldErrorsVar">
                        <s:property />
                        &#32;
                    </s:iterator>
                </span>
            </s:if>
        </div>
    </div>
    <br>

    <%-- save --%>
    <div class="form-group">
        <div class="col-sm-12">
            <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </div>
</s:form>
