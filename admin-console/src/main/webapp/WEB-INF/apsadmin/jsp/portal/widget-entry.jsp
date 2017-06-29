<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="carriageReturnChar" value="\r"/>
<c:set var="tabChar" value="\t"/>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li>
        <a href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" />">
            <s:text name="title.widgetManagement" />
        </a>
    </li>
    <s:if test="strutsAction == 2">
        <li>
            <s:text name="title.widgetManagement.edit" />
        </li>
        <li>
            <wpsa:widgetType key="%{widgetTypeCode}" var="widgetTypeVar" />
            <s:property value="#widgetTypeVar.titles[currentLang.code]" />
        </li>
    </s:if>
    <s:else>
        <li>
            <s:text name="title.newWidgetType" />
        </li>
    </s:else>
</ol>

<h1 class="page-title-container">
    <s:if test="strutsAction == 2">
        <span class="page-title-big"><s:text name="title.widgetManagement.edit" /></span>
    </s:if>
    <s:else>
        <span class="page-title-big"><s:text name="title.newWidgetType" /></span>
    </s:else>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="widget.widgetManagement.add.help" />" data-placement="left" >
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a></span>
</h1>
<hr/>

<div class="mb-20">
    <p>
        <s:elseif test="strutsAction != 2">
            <s:text name="title.newWidgetType.from" />:&#32;
            <s:if test="strutsAction == 5">
                <wpsa:widgetType var="parentWidgetTypeVar" key="%{parentWidgetTypeCode}" />
                <em><s:property value="%{getTitle(#parentWidgetTypeVar.code, #parentWidgetTypeVar.titles)}" /></em>
            </s:if>
            <s:elseif test="strutsAction == 3">
                <s:property value="%{getTitle(showletToCopy.type.code, showletToCopy.type.titles)}" />	<wpsa:page var="pageVar" key="%{pageCode}" />
                <s:text name="note.widgetType.page"/>:&#32;<em class="important"><s:property value="%{getTitle(#pageVar.code, #pageVar.titles)}" /></em>,&#32;<s:text name="note.widgetType.position" />:&#32;<em class="important"><s:property value="framePos" /></em>
            </s:elseif>
        </s:elseif>
    </p>
    <s:form action="save" namespace="/do/Portal/WidgetType" cssClass="form-horizontal">

        <wp:ifauthorized permission="superuser"><s:set var="isSuperuserVar" value="%{true}" /></wp:ifauthorized>

        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.ActionErrors" /></strong>
                <ul>
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.FieldErrors" /></strong>
                <ul  id="content-error-messages">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>

        <p class="sr-only">
            <wpsf:hidden name="strutsAction" />
            <s:if test="strutsAction == 5">
                <wpsf:hidden name="parentWidgetTypeCode" />
            </s:if>
            <s:elseif test="strutsAction == 2">
                <wpsf:hidden name="widgetTypeCode" />
            </s:elseif>
            <s:elseif test="strutsAction == 3">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="framePos" />
            </s:elseif>
        </p>

        <fieldset class="col-xs-12 no-padding">
            <legend><s:text name="label.info" />
                <div class="required-fields text-right"><s:text name="label.requiredFields" /></div>
            </legend>
            <s:set var="controlGroupErrorClassVar" value="''" />
            <s:if test="#pageCodeHasFieldErrorVar">
                <s:set var="controlGroupErrorClassVar" value="' has-error'" />
            </s:if>
            <s:if test="strutsAction != 2">
                <div class="form-group<s:property value="controlGroupErrorClassVar" />">
                    <s:set var="pageCodeFieldErrorsVar" value="%{fieldErrors['showletTypeCode']}" />
                    <s:set var="pageCodeHasFieldErrorVar" value="#pageCodeFieldErrorsVar != null && !#pageCodeFieldErrorsVar.isEmpty()" />
                    <label for="showletTypeCode" class="col-sm-2 control-label"><s:text name="label.code" />&#32;
                        <i class="fa fa-asterisk required-icon"></i>
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="widget.widgetManagement.code.help" />" data-placement="right"><span class="fa fa-info-circle"></span></a>

                    </label>
                    <div class="col-sm-10">
                        <wpsf:textfield id="showletTypeCode" name="showletTypeCode" cssClass="form-control" />
                        <s:if test="#pageCodeHasFieldErrorVar">
                            <p class="text-danger padding-small-vertical"><s:iterator value="#pageCodeFieldErrorsVar"><s:property /> </s:iterator></p>
                        </s:if>
                    </div>
                </div>
            </s:if>

            <div class="form-group<s:property value="controlGroupErrorClassVar" />">
                <s:set var="pageCodeFieldErrorsVar" value="%{fieldErrors['englishTitle']}" />
                <s:set var="pageCodeHasFieldErrorVar" value="#pageCodeFieldErrorsVar != null && !#pageCodeFieldErrorsVar.isEmpty()" />
                <label for="englishTitle" class="col-sm-2 control-label">
                    <span class="label label-info">en</span>&#32;<s:text name="label.title" />&#32;<i class="fa fa-asterisk required-icon"></i>
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield id="englishTitle" name="englishTitle" cssClass="form-control" />
                    <s:if test="#pageCodeHasFieldErrorVar">
                        <p class="text-danger padding-small-vertical"><s:iterator value="#pageCodeFieldErrorsVar"><s:property /> </s:iterator></p>
                    </s:if>
                </div>
            </div>

            <div class="form-group<s:property value="controlGroupErrorClassVar" />">
                <s:set var="pageCodeFieldErrorsVar" value="%{fieldErrors['italianTitle']}" />
                <s:set var="pageCodeHasFieldErrorVar" value="#pageCodeFieldErrorsVar != null && !#pageCodeFieldErrorsVar.isEmpty()" />
                <label for="italianTitle" class="col-sm-2 control-label"><code class="label label-info">it</code>&#32;<s:text name="label.title" />&#32;<i class="fa fa-asterisk required-icon"></i></label>
                <div class="col-sm-10">
                    <wpsf:textfield id="italianTitle" name="italianTitle" cssClass="form-control" />
                    <s:if test="#pageCodeHasFieldErrorVar">
                        <p class="text-danger padding-small-vertical"><s:iterator value="#pageCodeFieldErrorsVar"><s:property /> </s:iterator></p>
                    </s:if>
                </div>
            </div>

            <div class="form-group<s:property value="controlGroupErrorClassVar" />">
                <s:set var="pageCodeFieldErrorsVar" value="%{fieldErrors['mainGroup']}" />
                <s:set var="pageCodeHasFieldErrorVar" value="#pageCodeFieldErrorsVar != null && !#pageCodeFieldErrorsVar.isEmpty()" />
                <label for="mainGroup" class="col-sm-2 control-label"><s:text name="label.group" /></label>
                <div class="col-sm-10">
                    <wpsf:select name="mainGroup" headerKey="" headerValue="%{getText('note.choose')}" id="mainGroup" list="groups" listKey="name" listValue="descr" cssClass="form-control" disabled="%{!#isSuperuserVar}" />
                    <s:if test="#pageCodeHasFieldErrorVar">
                        <p class="text-danger padding-small-vertical"><s:iterator value="#pageCodeFieldErrorsVar"><s:property /> </s:iterator></p>
                    </s:if>
                </div>
            </div>

            <s:if test="null != #widgetTypeVar && #widgetTypeVar.logic && strutsAction == 2">
                <div class="form-group">
                    <label class="col-sm-2 control-label"><s:text name="label.widgetType.parentType" /></label>
                    <div class="col-sm-10">
                        <p class="form-control-static"><s:property value="#widgetTypeVar.parentType.titles[currentLang.code]" /></p>
                    </div>
                </div>
            </s:if>

        </fieldset>

        <s:if test="strutsAction != 1 && (strutsAction != 2 || #widgetTypeVar.logic)">
            <fieldset class="col-xs-12 col-md-12 no-padding">
                <legend><s:text name="title.widgetType.settings" /></legend>
                <s:if test="strutsAction == 5">
                    <s:set var="parentWidgetTypeVar" value="%{getWidgetType(parentShowletTypeCode)}" />
                    <s:iterator value="#parentWidgetTypeVar.typeParameters" var="widgetParamVar" >
                        <div class="form-group">
                            <label for="<s:property value="#widgetParamVar.name" />" class="col-sm-2 control-label"><s:property value="#widgetParamVar.name" />
                                <s:if test="#widgetParamVar.descr != ''">
                                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:property value='#widgetParamVar.descr' />" data-placement="top" ><span class="fa fa-info-circle"></span></a>
                                    </s:if>
                            </label>
                            <div class="col-sm-10">
                                <wpsf:textfield id="%{#widgetParamVar.name}" name="%{#widgetParamVar.name}" value="%{#request.parameters[#widgetParamVar.name]}" cssClass="form-control" />
                            </div>
                        </div>
                    </s:iterator>
                </s:if>
                <s:elseif test="strutsAction == 2">
                    <s:iterator value="#widgetTypeVar.parentType.typeParameters" var="widgetParamVar" >
                        <div class="form-group">
                            <label for="<s:property value="#widgetParamVar.name" />" class="col-sm-2 control-label"><s:property value="#widgetParamVar.name" />
                                <s:if test="#widgetParamVar.descr != ''">
                                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:property value='#widgetParamVar.descr' />" data-placement="top" ><span class="fa fa-info-circle"></span></a>
                                    </s:if>
                            </label>
                            <div class="col-sm-10">
                                <s:if test="#isSuperuserVar && #widgetTypeVar.userType">
                                    <wpsf:textfield id="%{#widgetParamVar.name}" name="%{#widgetParamVar.name}" value="%{#widgetTypeVar.config[#widgetParamVar.name]}" cssClass="form-control" />
                                </s:if>
                                <s:else>
                                    <span class="text-important"><s:property value="#widgetParamVar.name" /></span>&#32;
                                    <s:property value="%{#widgetTypeVar.config[#widgetParamVar.name]}" />
                                </s:else>
                            </div>
                        </div>
                    </s:iterator>
                    <s:set var="isSuperuserVar" value="%{false}" />
                </s:elseif>
                <s:elseif test="strutsAction == 3">
                    <s:iterator value="showletToCopy.type.typeParameters" var="widgetParamVar" >
                        <div class="form-group">
                            <s:if test="#widgetParamVar.descr != ''">
                                <label class="col-sm-2 control-label"><s:property value="#widgetParamVar.descr" /></label>
                            </s:if>
                            <div class="col-sm-10">
                                <p class="form-control-static">
                                    <span class="text-strong">
                                        <s:property value="#widgetParamVar.name" />:&#32;
                                    </span>
                                    <s:property value="%{showletToCopy.config[#widgetParamVar.name]}" />
                                </p>
                            </div>
                        </div>
                    </s:iterator>
                </s:elseif>
            </fieldset>
        </s:if>

        <s:if test="strutsAction != 3 && strutsAction != 5">
            <fieldset class="col-xs-12 col-md-12 no-padding">
                <s:if test="%{null == #widgetTypeVar || (!#widgetTypeVar.logic && !isInternalServletWidget(#widgetTypeVar.code))}">
                    <s:set var="uniqueGuiFragmentVar" value="%{extractUniqueGuiFragment(widgetTypeCode)}" />
                    <s:if test="%{strutsAction == 1 || isEditEmptyFragment() || (null != #uniqueGuiFragmentVar && (null != #uniqueGuiFragmentVar.gui || null != #uniqueGuiFragmentVar.defaultGui))}">
                        <div class="col-xs-12">
                            <div class="form-group">
                                <label class="control-label col-sm-2"></label>
                                <div class=" col-sm-10">
                                    <ul class="nav nav-tabs">
                                        <li class="active">
                                            <a href="#widget-gui" data-toggle="tab">Custom <span title="User Interface">UI</span></a>
                                        </li>
                                        <s:if test="strutsAction == 2">
                                            <s:if test="%{null != #uniqueGuiFragmentVar}">
                                                <li>
                                                    <a href="#widget-default-gui" data-toggle="tab">Default <span title="User Interface">UI</span></a>
                                                </li>
                                            </s:if>
                                        </s:if>
                                    </ul>
                                    <div class="tab-content margin-large-bottom ">
                                        <div class="tab-pane fade in active" id="widget-gui">
                                            <wpsf:textarea name="gui" id="widget_gui" cssClass="form-control" rows="8" cols="50" />
                                        </div>
                                        <div class="tab-pane fade" id="widget-default-gui">
                                            <s:if test="strutsAction == 2">
                                                <s:set var="uniqueGuiFragmentVar" value="%{extractUniqueGuiFragment(widgetTypeCode)}" />
                                                <s:if test="%{null != #uniqueGuiFragmentVar && null != #uniqueGuiFragmentVar.defaultGui}">
                                                    <div class="panel panel-default">
                                                        <div class="panel-body">
                                                            <c:set var="xmlConfigurationVar"><s:property value="#uniqueGuiFragmentVar.defaultGui" escapeHtml="true" /></c:set>
                                                            <c:set var="ESCAPED_STRING_DEFAULT" value="${fn:replace(fn:replace(xmlConfigurationVar, tabChar, '&emsp;'),carriageReturnChar, '')}" />
                                                            <pre><c:out value="${ESCAPED_STRING_DEFAULT}" escapeXml="false" /></pre>
                                                        </div>
                                                    </div>
                                                </s:if>
                                                <s:else>
                                                    <div class="margin-none alert alert-info"><s:text name="label.notAvailable" /></div>
                                                </s:else>
                                            </s:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </s:if>
                    <s:else>
                        <div class="mt-20">
                            <div class="alert alert-warning">
                                <span class="pficon pficon-warning-triangle-o"></span>
                                <strong><s:text name="label.widgetType.fragmentEditingNotAvailable" /></strong>
                            </div>
                        </div>
                    </s:else>
                </fieldset>
            </s:if>
            <s:elseif test="%{null != #widgetTypeVar && #widgetTypeVar.logic}"> <%-- excluded clause <<&& isInternalServletWidget(#widgetTypeVar.parentType.code)>> --%>
                <s:set var="guiFragmentCodesVar" value="%{extractGuiFragmentCodes(#widgetTypeVar.code)}" />
                <s:if test="%{null != #guiFragmentCodesVar && !#guiFragmentCodesVar.isEmpty()}">

                    <fieldset class="col-md-12 margin-large-bottom no-padding">
                        <legend><span title="User Interfaces">UIs</span></legend>
                        <div class="form-group">
                            <label class="control-label col-sm-2"></label>
                            <div class="panel-group col-sm-10" id="accordion">
                                <s:iterator value="guiFragmentCodesVar" var="guiFragmentCodeVar" status="status">
                                    <s:set var="guiFragmentVar" value="getGuiFragment(#guiFragmentCodeVar)" />
                                    <s:if test="%{isEditEmptyFragment() || (null != #guiFragmentVar && (null != #guiFragmentVar.gui || null != #guiFragmentVar.defaultGui))}">
                                        <div class="panel panel-default">
                                            <div class="panel-heading">
                                                <h4 class="panel-title">
                                                    <a class="display-block" data-toggle="collapse" data-parent="#accordion" href="#collapse-<s:property value="#status.count" />">
                                                        <s:property value="#guiFragmentCodeVar" />
                                                    </a>
                                                </h4>
                                            </div>
                                            <div id="collapse-<s:property value="#status.count" />" class="panel-collapse collapse in">
                                                <div class="panel-body">
                                                    <ul class="nav nav-tabs">
                                                        <li class="active"><a href="#widget-gui-<s:property value="#status.count" />" data-toggle="tab">Custom <span title="User Interface">UI</span></a></li>
                                                        <li><a href="#widget-default-gui-<s:property value="#status.count" />" data-toggle="tab">Default <span title="User Interface">UI</span></a></li>
                                                    </ul>
                                                    <div class="tab-content margin-large-bottom">
                                                        <div class="tab-pane fade in active" id="widget-gui-<s:property value="#status.count" />">
                                                            <s:set var="guiFieldNameVar" value="%{#widgetTypeVar.code + '_' + #guiFragmentCodeVar}" />
                                                            <wpsf:textarea name="%{#guiFieldNameVar}" id="%{#guiFieldNameVar}" value="%{guis.getProperty(#guiFieldNameVar)}" cssClass="form-control" rows="8" cols="50" />
                                                        </div>
                                                        <div class="tab-pane fade" id="widget-default-gui-<s:property value="#status.count" />">
                                                            <div class="panel panel-default">
                                                                <div class="panel-body">
                                                                    <c:set var="xmlConfigurationVar"><s:property value="#guiFragmentVar.defaultGui" escapeHtml="true" /></c:set>
                                                                    <c:set var="ESCAPED_STRING" value="${fn:replace(fn:replace(xmlConfigurationVar, tabChar, '&emsp;'),carriageReturnChar, '')}" />
                                                                    <pre><c:out value="${ESCAPED_STRING}" escapeXml="false" /></pre>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </s:if>
                                    <s:else>
                                        <div class="mt-20">
                                            <div class="alert alert-warning">
                                                <span class="pficon pficon-warning-triangle-o"></span>
                                                <strong><s:text name="label.widgetType.fragmentEditingNotAvailable" /></strong>
                                            </div>
                                        </div>
                                    </s:else>
                                </s:iterator>
                            </div>
                        </div>
                    </fieldset>
                </s:if>
            </s:elseif>
        </s:if>
        <wpsa:hookPoint key="core.widgetType.entry" objectName="hookPointElements_core_widget_entry">
            <s:iterator value="#hookPointElements_core_widget_entry" var="hookPointElement">
                <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
            </s:iterator>
        </wpsa:hookPoint>

        <%-- deprecated --%>
        <wpsa:hookPoint key="core.showletType.entry" objectName="hookPointElements_core_showlet_entry">
            <s:iterator value="#hookPointElements_core_showlet_entry" var="hookPointElement">
                <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
            </s:iterator>
        </wpsa:hookPoint>

        <br/>

        <div class="row">
            <div class="form-group col-md-12">

                <div class="form-group pull-right ">
                    <wpsf:submit type="button" cssClass="btn btn-primary">
                        <s:text name="label.save" />
                    </wpsf:submit>
                </div>
                <s:if test="strutsAction == 3">
                    <div class="form-group pull-right ">
                        <wpsa:actionParam action="save" var="actionName" >
                            <wpsa:actionSubParam name="replaceOnPage" value="true" />
                        </wpsa:actionParam>
                        <wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-primary">
                            <span class="icon fa fa-exchange"></span>&#32;
                            <s:text name="label.save.replace" />
                        </wpsf:submit>
                    </div>
                </s:if>
            </div>
        </div>
    </s:form>
</div>
