<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li><a href="<s:url action="viewTree" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageTree" />"><s:text name="title.pageTree" /></a></li>
    <li class="page-title-container">
        <s:if test="strutsAction == 1">
            <s:text name="title.newPage" />
        </s:if>
        <s:elseif test="strutsAction == 2">
            <s:text name="title.editPage" />
        </s:elseif>
        <s:elseif test="strutsAction == 3">
            <s:text name="title.clonePage" />
        </s:elseif>
    </li>
</ol>

<h1 class="page-title-container">
    <s:if test="strutsAction == 1">
        <s:text name="title.newPage" /><span class="pull-right"><a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.help" />" data-placement="left"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></span>
            </s:if>
            <s:elseif test="strutsAction == 2">
                <s:text name="title.editPage" /><span class="pull-right"><a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.help" />" data-placement="left"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></span>
            </s:elseif>
            <s:elseif test="strutsAction == 3">
                <s:text name="title.clonePage" /><span class="pull-right"><a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.help" />" data-placement="left"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></span>
            </s:elseif>
</h1>


<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">
    <s:if test="hasErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.ActionErrors" /></strong>
            <ul>
                <s:if test="hasActionErrors()">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:if>
                    <s:if test="hasFieldErrors()">
                        <s:iterator value="fieldErrors">
                            <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                    </s:if>
            </ul>
        </div>
    </s:if>

    <s:if test="strutsAction == 2"><s:set var="breadcrumbs_pivotPageCode" value="pageCode" /></s:if>
    <s:else><s:set var="breadcrumbs_pivotPageCode" value="parentPageCode" /></s:else>
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <s:form action="save" cssClass="form-horizontal">
        <p class="sr-only">
        <wpsf:hidden name="strutsAction" />
        <wpsf:hidden name="copyPageCode" />
        <wpsf:hidden name="groupSelectLock" />
        <s:iterator value="extraGroups" var="groupName"><wpsf:hidden name="extraGroups" value="%{#groupName}" /></s:iterator>
        <s:if test="%{groupSelectLock}">
            <wpsf:hidden name="group" />
        </s:if>
        <s:if test="strutsAction == 2">
            <wpsf:hidden name="parentPageCode" />
            <wpsf:hidden name="pageCode" />
        </s:if>
        <s:elseif test="strutsAction == 3">
            <wpsf:hidden name="group" />
            <wpsf:hidden name="model" />
            <wpsf:hidden name="defaultShowlet" />
            <wpsf:hidden name="showable" />
            <wpsf:hidden name="useExtraTitles" />
            <wpsf:hidden name="charset" />
            <wpsf:hidden name="mimeType" />
        </s:elseif>
    </p>

    <legend><s:text name="label.info" /><span class="required-fields-edit"><s:text name="label.requiredFields" /></span></legend>

    <s:iterator value="langs">
        <%-- lang --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['lang'+code]}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="lang<s:property value="code" />">
                <abbr title="<s:property value="descr" />"><code class="label label-info" ><s:property value="code" /></code></abbr>&#32;<s:text name="name.pageTitle" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="%{'lang'+code}" id="%{'lang'+code}" value="%{titles.get(code)}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
    </s:iterator>
    <%-- pageCode --%>
    <s:set var="fieldErrorsVar" value="%{fieldErrors['pageCode']}" />
    <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <label class="col-sm-2 control-label" for="pageCode">
            <s:text name="name.pageCode" />&nbsp;<i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i>
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.code" />" data-placement="right"><span class="fa fa-info-circle"></span></a>
        </label>
        <div class="col-sm-10">
            <wpsf:textfield name="pageCode" id="pageCode" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
            <s:if test="#hasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                    </span>
            </s:if>
        </div>
    </div>

    <s:if test="strutsAction != 2">
        <s:set var="fieldErrorsVar" value="%{fieldErrors['parentPageCode']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="pageCode">
                <s:text name="title.treePosition" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">

                <s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
                    <div class="table-responsive overflow-visible">
                        <table id="pageTree"
                               class="table table-bordered table-hover table-treegrid">
                            <thead>
                                <tr>
                                    <th style="width: 68%;"> <s:text name="title.pageTree" /><s:if test="#pageTreeStyleVar == 'classic'">
                                        <button type="button" class="btn-no-button expand-button" id="expandAll">
                                            <i class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;Expand All
                                        </button>
                                        <button type="button" class="btn-no-button" id="collapseAll">
                                            <i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;Collapse All
                                        </button>
                                    </s:if>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:set var="inputFieldName" value="%{'parentPageCode'}" />
                            <s:set var="selectedTreeNode" value="%{parentPageCode}" />
                            <s:set var="selectedPage" value="%{getPage(parentPageCode)}" />
                            <s:set var="liClassName" value="'page'" />
                            <s:set var="treeItemIconName" value="'fa-folder'" />

                        <wpsa:groupsByPermission permission="managePages" var="groupsByPermission" />

                        <s:if test="#pageTreeStyleVar == 'classic'">
                            <wpsa:pageTree allowedGroups="${groupsByPermission}" var="currentRoot" online="false" />
                            <s:include value="include/entryPage_treeBuilderPages.jsp" />
                        </s:if>
                        <s:elseif test="#pageTreeStyleVar == 'request'">
                            <style>
                                .table-treegrid span.collapse-icon, .table-treegrid span.expand-icon {
                                    cursor: pointer;
                                    display: none;
                                }
                            </style>
                            <s:set var="treeNodeActionMarkerCode" value="treeNodeActionMarkerCode" />
                            <s:set var="targetNode" value="%{parentPageCode}" />
                            <s:set var="treeNodesToOpen" value="treeNodesToOpen" />

                            <wpsa:pageTree allowedGroups="${groupsByPermission}" var="currentRoot" online="false" onDemand="true"
                                           open="${treeNodeActionMarkerCode!='close'}" targetNode="${targetNode}" treeNodesToOpen="${treeNodesToOpen}" />
                            <s:include value="include/entryPage_treeBuilder-request-linksPages.jsp" />
                        </s:elseif>
                        </tbody>
                    </table>
                </div>
                <s:if test="#hasFieldErrorVar">
                    <p class="text-danger padding-small-vertical"><s:iterator value="#fieldErrorsVar"><s:property /> </s:iterator></p>
                </s:if>
            </div>
        </div>
    </s:if>

    <s:if test="strutsAction != 3">
        <legend><s:text name="page.groups" /><span class="required-fields-edit"><s:text name="label.requiredFields" /></span></legend>

        <%-- ownerGroup --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['group']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="ownerGroup">
                <s:text name="label.ownerGroup" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:select name="group" id="group" list="allowedGroups" listKey="name" listValue="descr" headerKey="" headerValue="%{getText('note.choose')}" disabled="%{groupSelectLock}" cssClass="combobox form-control"></wpsf:select>
                    <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <%-- extraGroups --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['extraGroups']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="extraGroups">
                <s:text name="label.extraGroups" />
            </label>
            <div class="col-sm-10">
                <div class="input-group">
                    <wpsf:select
                        name="extraGroupNameToAdd" id="extraGroups" list="groups"
                        listKey="name" listValue="descr" headerKey=""
                        cssClass="combobox form-control" multiple="true" size="4"
                        />
                    <span class="input-group-btn" style="vertical-align: top">
                        <wpsf:submit type="button" action="joinExtraGroup" cssClass="btn btn-primary" >
                            <span class="icon fa fa-plus"></span>&#32;
                            <s:property value="label.join" />
                        </wpsf:submit>
                    </span>
                </div>
                <br/>
                <s:if test="extraGroups.size() != 0">
                    <s:iterator value="extraGroups" var="groupNameVar">
                        <wpsa:actionParam action="removeExtraGroup" var="actionName" >
                            <wpsa:actionSubParam name="extraGroupNameToRemove" value="%{#groupNameVar}" />
                        </wpsa:actionParam>
                        <div class="label label-default label-tag label-sm">
                            <s:property value="%{getSystemGroups()[#groupNameVar].getDescr()}"/>&#32;
                            <wpsf:submit type="button" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-tag">
                                <span class="icon fa fa-times"></span>
                                <span class="sr-only">x</span>
                            </wpsf:submit>
                        </div>
                    </s:iterator>
                </s:if>
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <legend><s:text name="label.settings" /><span class="required-fields-edit"><s:text name="label.requiredFields" /></span></legend>

        <%-- pageModel --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['model']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="ownerGroup">
                <s:text name="name.pageModel" />&nbsp;<i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i>
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.pagemodel" />" data-placement="right">
                    <span class="fa fa-info-circle"></span>
                </a>
            </label>
            <div class="col-sm-10">
                <wpsf:select name="model" headerKey="" headerValue="%{getText('note.choose')}" id="model" list="pageModels" listKey="code" listValue="descr" cssClass="form-control"></wpsf:select>
                    <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <div class="form-checkbox form-group<s:property value="#controlGroupErrorClass" />">
            <div class="col-sm-2 control-label">
                <label class="display-block" for="showable"><s:text name="name.mainMenu" />
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="name.isShowablePage" />" data-placement="right">
                        <span class="fa fa-info-circle"></span></a></label>
            </div>
            <div class="col-sm-3">
                <wpsf:checkbox name="showable" id="showable" cssClass="bootstrap-switch" />
            </div>

            <div class="col-sm-3 control-label">
                <label class="display-block" for="useExtraTitles"><s:text name="name.SEO.short" />
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.seo" />" data-placement="left">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
            </div>
            <div class="col-sm-4">
                <wpsf:checkbox name="useExtraTitles" id="useExtraTitles" cssClass="bootstrap-switch" />
            </div>
        </div>


        <div class="col-sm-6" id="form-custom-select">
            <%-- charset --%>
            <s:set var="fieldErrorsVar" value="%{fieldErrors['charset']}" />
            <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
            <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
            <div class=" form-group<s:property value="#controlGroupErrorClass" />">
                <label class="col-sm-4 control-label" for="ownerGroup" style="margin-left: -15px">
                    <s:text name="name.charset" />
                    <i class="fa fa-asterisk required-icon"></i>
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.charset" />" data-placement="left">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <div class="col-sm-6 foti_class">
                    <wpsf:select name="charset" id="charset" size="3" style="overflow:hidden" list="allowedCharsets" cssClass="form-control"/>
                    <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                            </span>
                    </s:if>
                </div>
            </div>
        </div>
        <div class="col-sm-6" id="form-custom-select2">
            <%-- mimeType --%>
            <s:set var="fieldErrorsVar" value="%{fieldErrors['mimeType']}" />
            <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
            <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

            <div class="form-group<s:property value="#controlGroupErrorClass" />">
                <label class="col-sm-4 control-label" for="ownerGroup">
                    <s:text name="name.mimeType" />
                    <i class="fa fa-asterisk required-icon"></i>
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.mimetype" />" data-placement="left">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <div class="col-sm-6 foti_class"  style="margin-left: 6px">
                    <wpsf:select name="mimeType" id="mimeType" size="5" style="overflow:hidden" list="allowedMimeTypes" cssClass="form-control" />
                    <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                            </span>
                    </s:if>
                </div>
            </div>
        </div>
    </s:if>

    <wpsa:hookPoint key="core.entryPage" objectName="hookPointElements_core_entryPage">
        <s:iterator value="#hookPointElements_core_entryPage" var="hookPointElement">
            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
            </s:iterator>
    </wpsa:hookPoint>

    <div class="col-md-12">
        <div class="form-group pull-right ">
            <div class="btn-group">
                <wpsf:submit type="button" action="saveConfigure" cssClass="btn btn-success ">
                    <s:text name="label.saveConfig" />
                </wpsf:submit>
            </div>
            <div class="btn-group">
                <wpsf:submit type="button" action="save" cssClass="btn btn-primary ">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </div>
</s:form>
</div>
<script>
    $('#extraGroups option').mousedown(function (e) {
        e.preventDefault();
        $(this).prop('selected', !$(this).prop('selected'));
        return false;
    });
</script>   
