<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<!-- Admin console Breadcrumbs -->
<s:url action="results" namespace="/do/jacms/Content" var="contentListURL"/>
<s:text name="note.goToSomewhere" var="contentListURLTitle"/>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="${contentListURL}" title="${contentListURLTitle}">
            <s:text name="breadcrumb.jacms.content.list" />
        </a>
    </li>
    <li class="page-title-container">
        <s:if test="getStrutsAction() == 1">
            <s:text name="label.add" />
        </s:if>
        <s:else>
            <s:text name="label.edit" />
        </s:else>

        <s:text name="jacms.menu.contentAdmin" />
    </li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}" />
<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="label.add" />
    </s:if>
    <s:else>
        <s:text name="label.edit" />
    </s:else>&#32;
    <s:text name="jacms.menu.contentAdmin" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="label.contents.section.help" />" data-placement="left" data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<!-- Default separator -->
<div class="form-group-separator"></div>

<div id="main" role="main" class="mt-20">
    <div data-autosave="messages_container">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>

                <strong><s:text name="message.content.error" /></strong>
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <p><s:property escapeHtml="false" /></p>
                    </s:iterator>
                </s:iterator>
            </div>
        </s:if>
    </div>

    <p class="sr-only"><s:text name="note.editContent" /></p>
    <s:url namespace="/do/jacms/Content/Ajax"  action="autosave" var="dataAutosaveActionVar" />
    <s:form cssClass="form-horizontal " data-form-type="autosave" data-autosave-action="%{#dataAutosaveActionVar}">


        <legend>
            <s:text name="label.info" /><span class="text-right-required"><s:text name="label.requiredFields" /></span>
        </legend>

        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/include/snippet-content.jsp" />



        <a href="<s:url action="backToEntryContent" >
               <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>"
           title="<s:text name="note.content.backToEdit" />" >
            <s:property value="content.descrDisablingTemporarily" />
        </a>

        <!-- Groups section -->
        <legend>
            <s:text name="label.groups" /><span class="text-right-required"><s:text name="label.requiredFields" /></span>
        </legend>

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="contentMainGroup">
                <s:text name="label.ownerGroup" />&nbsp;
                <span class="fa fa-asterisk required-icon"></span>
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="note.set.MainGroup" />." data-placement="top"><span class="fa fa-info-circle"></span>
                </a>
            </label>
            <div class="col-sm-10">

                <s:if test="%{null != content.mainGroup}">
                    <input type="text" readonly="readonly" value="<s:property value="%{getGroup(content.mainGroup).descr}" />" class="form-control">
                </s:if>

                <s:if test="%{null == content.mainGroup}">
                    <div class="input-group">
                        <wpsf:select name="mainGroup" id="contentMainGroup" list="allowedGroups"
                                     headerKey="" headerValue="%{getText('note.choose')}" value="#session.contentGroupOnSession"
                                     listKey="name" listValue="descr" cssClass="form-control" />
                        <span class="input-group-btn">
                            <wpsf:submit action="configureMainGroup"
                                         type="button" title="Set Group" cssClass="btn btn-primary">
                                <s:text name="label.setGroup" />
                            </wpsf:submit>
                        </span>
                    </div>
                </s:if>
            </div>
        </div>

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="extraGroups">
                <s:text name="label.join" />&#32;<s:text name="label.group" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <!-- Extra Groups Add -->
                <div class="input-group">
                    <wpsf:select name="extraGroupName" headerKey="" headerValue="%{getText('note.choose')}" id="extraGroups" list="groups" listKey="name"  listValue="descr" cssClass="combobox form-control" data-autosave="ignore" />
                    <span class="input-group-btn">
                        <wpsf:submit  type="button" action="joinGroup" cssClass="btn btn-primary">
                            <span class="icon fa fa-plus"></span>
                        </wpsf:submit>
                    </span>
                </div>

                <!-- Extra Groups List -->
                <s:if test="content.groups.size != 0">
                    <div class="mt-20">
                        <s:iterator value="content.groups" var="groupName">
                            <wpsa:actionParam action="removeGroup" var="actionName" >
                                <wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
                            </wpsa:actionParam>

                            <div class="label label-default label-tag label-sm">
                                <s:property value="%{getGroupsMap()[#groupName].getDescr()}"/>&#32;
                                <wpsf:submit type="button" cssClass="btn btn-link" action="%{#actionName}"
                                             title="%{getText('label.remove')+' '+getGroupsMap()[#groupName].getDescr()}">
                                    <span class="pficon pficon-close white"></span>
                                    <span class="sr-only">x</span>
                                </wpsf:submit>
                            </div>
                        </s:iterator>
                    </div>
                </s:if>
            </div>
        </div>

        <!-- Categories section -->
        <legend>
            <s:text name="label.categories" /><span class="text-right-required"><s:text name="label.requiredFields" /></span>
        </legend>


        <div class="form-group">
            <label class="col-sm-2 control-label"><s:text name="label.categories"/></label>
            <div class="col-sm-10">
                <s:action name="showCategoryBlockOnEntryContent" namespace="/do/jacms/Content" executeResult="true">
                    <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                </s:action>
            </div>
        </div>

        <!-- Custom Attributes section -->
        <legend>
            <s:text name="label.custom.attributes" /><span class="text-right-required"><s:text name="label.requiredFields" /></span>
        </legend>

        <p class="sr-only">
            <wpsf:hidden name="contentOnSessionMarker" />
        </p>
        <p class="sr-only" id="quickmenu"><s:text name="title.quickMenu" /></p>

        <ul class="nav nav-tabs tab-togglers" id="tab-togglers">
            <li class="sr-only"><a data-toggle="tab" href="#info_tab"><s:text name="title.contentInfo" /></a></li>
                <s:iterator value="langs" var="lang" status="langStatusVar">
                <li <s:if test="#langStatusVar.first"> class="active" </s:if>>
                    <a data-toggle="tab" href="#<s:property value="#lang.code" />_tab">
                        <s:property value="#lang.descr" />
                    </a>
                </li>
            </s:iterator>
        </ul>

        <div class="tab-content mt-20" id="tab-container">
            <s:iterator value="langs" var="lang" status="langStatusVar"><%-- lang iterator --%>
                <div id="<s:property value="#lang.code" />_tab" class="tab-pane <s:if test="#langStatusVar.first"> active </s:if>"><%-- tab --%>
                        <h2 class="sr-only">
                        <s:property value="#lang.descr" />
                    </h2>
                    <p class="sr-only">
                        <a class="sr-only" href="#quickmenu" id="<s:property value="#lang.code" />_tab_quickmenu"><s:text name="note.goBackToQuickMenu" /></a>
                    </p>
                    <s:iterator value="content.attributeList" var="attribute"><%-- attributes iterator --%>
                        <div id="<s:property value="%{'contentedit_'+#lang.code+'_'+#attribute.name}" />"><%-- contentedit div --%>
                            <wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" /><%-- tracer init --%>

                            <s:set var="attributeFieldErrorsVar" value="%{fieldErrors[#attribute.name]}" />
                            <s:set var="attributeHasFieldErrorVar" value="#attributeFieldErrorsVar != null && !#attributeFieldErrorsVar.isEmpty()" />
                            <s:set var="attributeFieldNameErrorsVar" value="%{fieldErrors[#attributeTracer.getFormFieldName(#attribute)]}" />
                            <s:set var="attributeHasFieldNameErrorVar" value="#attributeFieldNameErrorsVar != null && !#attributeFieldNameErrorsVar.isEmpty()" />
                            <s:set var="attributeFieldNameErrorsVarV2" value="%{fieldErrors[#attribute.type+':'+#attribute.name]}" />
                            <s:set var="attributeHasFieldNameErrorVarV2" value="#attributeFieldNameErrorsVarV2 != null && !#attributeFieldNameErrorsVarV2.isEmpty()" />

                            <s:set var="attributeHasErrorVar" value="%{#attributeHasFieldErrorVar||#attributeHasFieldNameErrorVar||#attributeHasFieldNameErrorVarV2}" />
                            <s:set var="controlGroupErrorClassVar" value="''" />
                            <s:set var="inputErrorClassVar" value="''" />
                            <s:if test="#attributeHasErrorVar">
                                <s:set var="controlGroupErrorClassVar" value="' has-error'" />
                                <s:set var="inputErrorClassVar" value="' input-with-feedback'" />
                            </s:if>

                            <s:if test="null != #attribute.description"><s:set var="attributeLabelVar" value="#attribute.description" /></s:if>
                            <s:else><s:set var="attributeLabelVar" value="#attribute.name" /></s:else>

                                <div class="form-group<s:property value="#controlGroupErrorClassVar" />"><%-- form group --%>
                                <s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
                                    <label class="col-sm-2 control-label"></span>&#32;<s:property value="#attributeLabelVar" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
                                </s:if>
                                <s:elseif test="#attribute.type == 'Image' || #attribute.type == 'Attach' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
                                    <label class="col-sm-2 control-label">
                                        <s:property value="#attributeLabelVar" />&#32;
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
                                    </label>
                                </s:elseif>
                                <s:elseif test="#attribute.type == 'Composite'">
                                    <label class="col-sm-2 control-label"><s:property value="#attributeLabelVar" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
                                </s:elseif>
                                <s:elseif test="#attribute.type == 'Monotext' || #attribute.type == 'Text' || #attribute.type == 'Longtext' || #attribute.type == 'Hypertext' || #attribute.type == 'Number' || #attribute.type == 'Date' || #attribute.type == 'Timestamp' || #attribute.type == 'Link' || #attribute.type == 'Enumerator' || #attribute.type == 'EnumeratorMap'">
                                    <label class="col-sm-2 control-label" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
                                        <s:property value="#attributeLabelVar" />&#32;
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
                                    </label>
                                </s:elseif>
                                <div class="col-sm-10">
                                    <s:if test="#attribute.type == 'Attach'">
                                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/attachAttribute.jsp" />
                                    </s:if>
                                    <s:elseif test="#attribute.type == 'Boolean'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'CheckBox'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Date'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Enumerator'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'EnumeratorMap'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorMapAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Hypertext'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Image'">
                                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Link'">
                                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/linkAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Longtext'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Monotext'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Number'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Text'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'ThreeState'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Timestamp'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/timestampAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Composite'">
                                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/compositeAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'List'">
                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/listAttribute.jsp" />
                                    </s:elseif>
                                    <s:elseif test="#attribute.type == 'Monolist'">
                                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/monolistAttribute.jsp" />
                                    </s:elseif>
                                    <wpsa:hookPoint key="jacms.entryContent.attributeExtra" objectName="hookPointElements_jacms_entryContent_attributeExtra">
                                        <s:iterator value="#hookPointElements_jacms_entryContent_attributeExtra" var="hookPointElement">
                                            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                                        </s:iterator>
                                    </wpsa:hookPoint>


                                    <s:if test="#attributeHasErrorVar">
                                        <p class="text-danger padding-small-vertical">
                                            <jsp:useBean id="attributeErrorMapVar" class="java.util.HashMap" scope="request"/>
                                            <s:iterator value="#attributeFieldErrorsVar"><s:set var="attributeCurrentError" scope="page" /><c:set target="${attributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/> </s:iterator>
                                            <s:iterator value="#attributeFieldNameErrorsVar"><s:set var="attributeCurrentError" scope="page" /><c:set target="${attributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/> </s:iterator>
                                            <s:iterator value="#attributeFieldNameErrorsVarV2"><s:set var="attributeCurrentError" scope="page" /><c:set target="${attributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/> </s:iterator>
                                            <c:forEach items="${attributeErrorMapVar}" var="attributeCurrentError">
                                                <c:out value="${attributeCurrentError.value}" /><br />
                                            </c:forEach>
                                            <c:set var="attributeErrorMapVar" value="${null}" />
                                            <c:set var="attributeCurrentError" value="${null}" />
                                        </p>
                                    </s:if>
                                    <s:set var="attributeHasErrorVar" value="%{null}" />
                                    <s:set var="attributeFieldErrorsVar" value="%{null}" />
                                    <s:set var="attributeHasFieldErrorVar" value="%{null}" />
                                    <s:set var="attributeFieldNameErrorsVar" value="%{null}" />
                                    <s:set var="attributeHasFieldNameErrorVar" value="%{null}" />
                                    <s:set var="attributeFieldNameErrorsVarV2" value="%{null}" />
                                    <s:set var="attributeHasFieldNameErrorVarV2" value="%{null}" />
                                    <s:set var="attributeHasErrorVar" value="%{null}" />
                                    <s:set var="controlGroupErrorClassVar" value="%{null}" />
                                    <s:set var="inputErrorClassVar" value="%{null}" />
                                </div>
                            </div><%-- form group --%>
                        </div><%-- contentedit div --%>
                    </s:iterator><%-- attributes iterator --%>
                    <%-- preview --%>
                    <s:if test="%{isComponentInstalled('entando-portal-ui')}">
                        <s:set var="showingPageSelectItems" value="showingPageSelectItems" />
                        <wpsa:actionParam action="preview" var="previewActionName" >
                            <wpsa:actionSubParam name="%{'jacmsPreviewActionLangCode_' + #lang.code}" value="%{#lang.code}" />
                        </wpsa:actionParam>
                        <hr />
                        <div class="form-group">
                            <div class="col-xs-12">
                                <div class="col-sm-10 col-sm-offset-2 no-padding">

                                    <s:if test="!#showingPageSelectItems.isEmpty()">
                                        <div class="input-group input-group-lg">
                                            <s:set var="previewActionPageCodeLabelId">jacmsPreviewActionPageCode_<s:property value="#lang.code" /></s:set>
                                            <label class="sr-only" for="<s:property value="#previewActionPageCodeLabelId" />">
                                                <s:text name="name.preview.page" />
                                            </label>
                                            <wpsf:select name="%{'jacmsPreviewActionPageCode_' + #lang.code}" id="%{#previewActionPageCodeLabelId}" list="#showingPageSelectItems"
                                                         headerKey="" headerValue="%{getText('note.choose')}"
                                                         listKey="key"
                                                         listValue="%{getText('name.preview.page') + ': ' +value}"
                                                         cssClass="form-control"
                                                         data-autosave="ignore" />
                                            <span class="input-group-btn">
                                                <wpsf:submit type="button" cssClass="btn btn-info" action="%{#previewActionName}" title="%{getText('note.button.previewContent')}" >
                                                    <span class="icon fa fa-eye"></span>&#32;<s:text name="label.preview" />
                                                </wpsf:submit>
                                            </span>
                                        </div>
                                    </s:if>
                                    <s:else>
                                        <p class="static-control text-center text-info"><s:text name="label.preview.noPreviewPages" /></p>
                                    </s:else>
                                </div>
                            </div>
                        </div>
                    </s:if>
                </div><%-- tab --%>
            </s:iterator><%-- lang iterator --%>
        </div><%-- tabs container --%>

        <!-- Sezione Info -->
        <wpsa:hookPoint key="jacms.entryContent.tabGeneral" objectName="hookPointElements_jacms_entryContent_tabGeneral">
            <div id="info" class="panel panel-default">
                <div class="panel-heading">
                    <h2 class="h4 margin-none">
                        <s:text name="title.contentInfo" />
                        <a href="#quickmenu" id="info_content_goBackToQuickMenu" class="pull-right" title="<s:text name="note.goBackToQuickMenu" />"><span class="icon fa fa-arrow-circle-up"></span><span class="sr-only"><s:text name="note.goBackToQuickMenu" /></span></a>
                    </h2>
                </div>

                <!-- hookpoint general section -->
                <s:iterator value="#hookPointElements_jacms_entryContent_tabGeneral" var="hookPointElement">
                    <div class="panel-body">
                        <wpsa:include value="%{#hookPointElement.filePath}" />
                    </div>
                </s:iterator>
            </div>
        </wpsa:hookPoint>

        <%-- actions --%>
        <h2 class="sr-only"><s:text name="title.contentActionsIntro" /></h2>

        <div class="col-xs-12 no-padding" id="sticky-toolbar">
            <div class="row toolbar-pf table-view-pf-toolbar border-bottom">
                <div class="col-xs-12">
                    <wpsa:hookPoint key="jacms.entryContent.actions" objectName="hookPointElements_jacms_entryContent_actions">
                        <s:iterator value="#hookPointElements_jacms_entryContent_actions" var="hookPointElement">
                            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                        </s:iterator>
                    </wpsa:hookPoint>
                </div>
            </div>
        </div>
    </s:form>
</div><%-- main --%>
