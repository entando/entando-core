<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li>
        <a href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" />">
            <s:text name="title.widgetManagement" />
        </a>
    </li>
    <li><s:text name="title.apiServiceManagement" /></li>
</ol>
<div id="main" role="main" >
    <s:if test="strutsAction == 1 || strutsAction == 3">
        <s:set var="masterApiMethodVar" value="%{getMethod(namespace, resourceName)}" />
    </s:if>
    <s:if test="strutsAction == 2">
        <h1 class="page-title-container">
            <s:text name="label.edit" />: <s:property value="serviceKey" />
            <span class="pull-right">
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="widget.api.service.new.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
            </span>
        </h1>
    </s:if>
    <s:elseif test="strutsAction == 1">
        <h1 class="page-title-container">
            <s:text name="title.api.apiService.newFrom" /> - <span class="monospace"><s:property value="#masterApiMethodVar.methodName" /></span>
            <span class="pull-right">
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="widget.api.service.new.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
            </span>
        </h1>
    </s:elseif>
    <s:elseif test="strutsAction == 3">
        <h1 class="page-title-container">
            <s:text name="title.api.apiService.copyFrom" />: <span class="monospace"><s:property value="#masterApiMethodVar.methodName" /></span> in <span class="monospace"><s:property value="pageCode" /></span>
            <span class="pull-right">
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
            </span>
        </h1>
    </s:elseif>
    <p>
        <s:set var="masterMethodVar" value="%{getMethod(namespace, resourceName)}" />
        <s:text name="label.service.parentApi" />: <em><s:property value="#masterMethodVar.description" />&#32;(/<s:if test="namespace!=null && namespace.length()>0"><s:property value="namespace" />/</s:if><s:property value="resourceName" />)</em>
        </p>
        <div class="text-right">
            <div class="form-group-separator">
            </div>
        </div>
        <br>
    <s:form cssClass="form-horizontal" action="save" >
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.ActionErrors" /></strong><ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <fieldset class="col-xs-12">
            <legend><s:text name="label.info" />
                <div class="required-fields text-right"><s:text name="label.requiredFields" /></div>
            </legend>
            <p class="sr-only">
                <wpsf:hidden name="strutsAction" />
                <s:if test="strutsAction == 2">
                    <wpsf:hidden name="serviceKey" />
                </s:if>
                <s:if test="strutsAction == 3">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="framePos" />
                </s:if>
                <wpsf:hidden name="resourceName" />
                <wpsf:hidden name="namespace" />
            </p>
            <div class="form-group">
                <label for="<s:property value="serviceKey" />" class="col-sm-2 control-label"><s:text name="name.api.service" /> <i class="fa fa-asterisk required-icon"></i></label>
                <div class="col-sm-10">
                    <wpsf:textfield id="serviceKey" name="serviceKey" disabled="%{strutsAction == 2}" cssClass="form-control" />
                </div>
            </div>
            <s:iterator value="systemLangs">
                <div class="form-group">
                    <label for="lang_<s:property value="code"/>" class="col-sm-2 control-label"><span class="label label-info"><s:property value="code" /></span>&#32;<s:text name="label.description" /> <i class="fa fa-asterisk required-icon"></i></label>
                    <div class="col-sm-10">
                        <s:textarea cols="50" rows="3" id="%{'lang_'+code}" name="%{'lang_'+code}" value="%{descriptions[code]}" cssClass="form-control" />
                    </div>
                </div>
            </s:iterator>
            <div class="form-group">
                <label for="tag" class="col-sm-2 control-label"><s:text name="label.tag" />:</label>
                <div class="col-sm-10">
                    <wpsf:textfield id="tag" name="tag" cssClass="form-control" />
                </div>
            </div>
        </fieldset>
        <fieldset class="col-xs-12">
            <legend><s:text name="label.options" />
                <div class="required-fields text-right"></div>
            </legend>
            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <label for="activeService" class=""><s:text name="label.active" /></label>
                </div>
                <div class="col-sm-10">
                    <wpsf:checkbox name="activeService" id="activeService" cssClass="bootstrap-switch"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <label for="hiddenService" ><s:text name="label.hidden" /></label>
                </div>
                <div class="col-sm-10">
                    <wpsf:checkbox name="hiddenService" id="hiddenService" cssClass="bootstrap-switch" />
                </div>
            </div>
        </fieldset>
        <fieldset class="col-xs-12">
            <legend><s:text name="label.api.authorities" />
                <div class="required-fields text-right"></div>
            </legend>
            <div class="form-group">
                <label for="requiredAuth" class="col-sm-2 control-label"><s:text name="label.api.authority.autenticationRequired" /></label>
                <div class="col-sm-10">
                    <wpsf:checkbox name="requiredAuth" id="requiredAuth" cssClass="bootstrap-switch"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="requiredPermission"><s:text name="label.api.authority.permission" />:</label>
                <div class="col-sm-10">
                    <wpsf:select headerKey="" headerValue="%{getText('label.none')}" name="requiredPermission" list="permissionAutorityOptions" listKey="key" listValue="value" id="requiredPermission" cssClass="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="requiredGroup"><s:text name="label.api.authority.group" />:</label>
                <div class="col-sm-10">
                    <wpsf:select headerKey="" headerValue="%{getText('label.none')}" name="requiredGroup" list="groups" listKey="name" listValue="descr" id="requiredGroup" cssClass="form-control"/>
                </div>
            </div>
        </fieldset>
        <fieldset class="col-xs-12">
            <div class="form-group">
                <legend><s:text name="label.parameters" /></legend>
                <label class="col-sm-2 control-label">
                    <s:text name="label.parameters" />
                </label>
                <div class="col-sm-10">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover table-striped">
                            <tr>
                                <th class="table-w-10"><s:text name="label.name" /></th>
                                <th class="table-w-45"><s:text name="label.description" /></th>
                                <th class="table-w-10"><s:text name="label.required" /></th>
                                <th class="table-w-10"><s:text name="label.default" /></th>
                                <th class="table-w-15"><s:text name="label.canBeOverridden" /></th>
                            </tr>
                            <s:iterator value="apiParameters" var="apiParameterVar" >
                                <tr>
                                    <td>
                                        <label for="<s:property value="%{#apiParameterVar.key + '_apiParam'}" />">
                                            <s:property value="#apiParameterVar.key" />
                                        </label>
                                    </td>
                                    <td class="table-txt-wrap"><s:property value="#apiParameterVar.description" /></td>
                                    <td>
                                        <s:property value="#apiParameterVar.required"/>
                                    </td>
                                    <td>
                                        <wpsf:textfield id="%{#apiParameterVar.key + '_apiParam'}" name="%{#apiParameterVar.key + '_apiParam'}" value="%{apiParameterValues[#apiParameterVar.key]}" cssClass="form-control" /></td>
                                    <td>
                                        <s:set var="freeParameterFieldNameVar" value="%{'freeParameter_' + #apiParameterVar.key}" />

                                        <wpsf:radio name="%{#freeParameterFieldNameVar}"
                                                    id="%{'true_' + #freeParameterFieldNameVar}" value="true" checked="%{freeParameters.contains(#apiParameterVar.key)}" />
                                        <label for="<s:property value="%{'true_' + #freeParameterFieldNameVar}" />"><s:text name="label.yes"/></label><br />

                                        <wpsf:radio name="%{#freeParameterFieldNameVar}"
                                                    id="%{'false_' + #freeParameterFieldNameVar}" value="false" checked="%{!freeParameters.contains(#apiParameterVar.key)}" />
                                        <label for="<s:property value="%{'false_' + #freeParameterFieldNameVar}" />"><s:text name="label.no"/></label>

                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </div>
                </div>
        </fieldset>
        <div class="col-xs-12" style="margin-bottom: 20px">
            <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </s:form>
</div>
