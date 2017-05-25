<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib uri="/aps-core" prefix="wp"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="%{'title.' + entityManagerName + '.management'}" />
    </li>
    <s:if test="strutsAction == 2">
        <li class="page-title-container"><s:text
                name="title.attribute.edit" /></li>
        </s:if>
        <s:else>
        <li class="page-title-container"><s:text
                name="title.attribute.new" /></li>
        </s:else>
</ol>

<h1 class="page-title-container">
    <s:if test="strutsAction == 2">
        <s:text name="title.attribute.edit" />
    </s:if>
    <s:else>
        <s:text name="title.attribute.new" />
    </s:else>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields" />
    </div>
</div>
<br>
<s:if test="hasFieldErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert"
                aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <p>
            <s:text name="message.title.FieldErrors" />
        </p>
        <ul>
            <s:iterator value="fieldErrors">
                <s:iterator value="value">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                </s:iterator>
        </ul>
    </div>
</s:if>

<s:form action="saveAttribute" cssClass="form-horizontal">

    <p class="sr-only">
        <wpsf:hidden name="attributeTypeCode" />
        <wpsf:hidden name="strutsAction" />
        <s:if test="null != attributeRoles && attributeRoles.size() > 0">
            <s:iterator value="attributeRoles" var="attributeRole">
                <wpsf:hidden name="attributeRoles" value="%{#attributeRole}" />
            </s:iterator>
        </s:if>
        <s:if test="null != disablingCodes && disablingCodes.size() > 0">
            <s:iterator value="disablingCodes" var="disablingCode">
                <wpsf:hidden name="disablingCodes" value="%{#disablingCode}" />
            </s:iterator>
        </s:if>
    </p>

    <s:if test="strutsAction == 2">
        <p class="sr-only">
            <wpsf:hidden name="attributeName" />
        </p>
    </s:if>

    <s:if test="strutsAction == 1">
        <s:set var="attribute"
               value="getAttributePrototype(attributeTypeCode)" />
    </s:if>
    <s:else>
        <s:set var="attribute" value="entityType.getAttribute(attributeName)" />
    </s:else>

    <fieldset class="col-xs-12">
        <legend>
            <s:text name="label.info" />
        </legend>
        <div class="form-group">

            <label class="col-sm-2 control-label"><s:text name="label.type" />
                <a role="button" tabindex="0"  data-toggle="popover" data-trigger="focus" data-html="true" title=""
                   data-placement="top" data-content="to be inserted   "
                   data-original-title=""> <span class="fa fa-info-circle"></span></a>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield cssClass="form-control" name="attributeTypeCode" value="%{attributeTypeCode}" disabled="true" />
            </div>
        </div>

        <div class="form-group">

            <label class="col-sm-2 control-label" for="attributeName"><s:text
                    name="label.code" /> <a role="button" tabindex="0"
                   data-toggle="popover" data-trigger="focus" data-html="true" title=""
                   data-placement="top" data-content="to be inserted   "
                   data-original-title=""> <span class="fa fa-info-circle"></span></a>
            </label>
            <div class="col-sm-10">
                <s:if test="strutsAction == 1">
                    <wpsf:textfield cssClass="form-control" name="attributeName"
                                    id="attributeName" />
                </s:if>
                <s:else>
                    <wpsf:textfield cssClass="form-control" name="attributeName"
                                    value="%{attributeName}" id="attributeName" disabled="true" />
                </s:else>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="attributeDescription"><s:text
                    name="label.description" /> <a role="button" tabindex="0"
                   data-toggle="popover" data-trigger="focus" data-html="true" title=""
                   data-placement="top" data-content="to be inserted   "
                   data-original-title=""> <span class="fa fa-info-circle"></span></a>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="attributeDescription"
                                id="attributeDescription" cssClass="form-control" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="required"><s:text
                    name="Entity.attribute.flag.mandatory.full" /> <a role="button"
                   tabindex="0" data-toggle="popover" data-trigger="focus"
                   data-html="true" title="" data-placement="top"
                   data-content="to be inserted   " data-original-title=""> <span
                        class="fa fa-info-circle"></span>
                </a> </label>
            <div class="col-sm-10">
                <wpsf:checkbox name="required" id="required"
                               cssClass=" bootstrap-switch" />
            </div>
        </div>
        <div>
            <s:if
                test="!(#attribute.type == 'List') && !(#attribute.type == 'Monolist')">
                <s:if
                    test="isEntityManagerSearchEngineUser() && isIndexableOptionSupported(attributeTypeCode)">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="indexable"><s:text
                                name="Entity.attribute.flag.indexed.full" /> <a role="button"
                               tabindex="0" data-toggle="popover" data-trigger="focus"
                               data-html="true" title="" data-placement="top"
                               data-content="to be inserted   " data-original-title=""> <span
                                    class="fa fa-info-circle"></span></a> </label>
                        <div class="col-sm-10">
                            <wpsf:checkbox name="indexable" id="indexable"
                                           cssClass=" bootstrap-switch" />
                        </div>
                    </div>
                </s:if>
                <s:if test="isSearchableOptionSupported(attributeTypeCode)">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="searchable"><s:text
                                name="Entity.attribute.flag.searchable.full" /> <a
                                role="button" tabindex="0" data-toggle="popover"
                                data-trigger="focus" data-html="true" title=""
                                data-placement="top" data-content="to be inserted   "
                                data-original-title=""> <span class="fa fa-info-circle"></span></a>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:checkbox name="searchable" id="searchable"
                                           cssClass=" bootstrap-switch" />
                        </div>
                    </div>
                </s:if>
            </s:if>
        </div>
    </fieldset>

    <fieldset class="col-xs-12">

        <legend>
            <s:text name="name.roles" />
        </legend>

        <s:set var="freeAttributeRoles" value="%{getFreeAttributeRoleNames()}" />
        <s:if
            test="null == #freeAttributeRoles || #freeAttributeRoles.isEmpty()">
            <p>
                <s:text name="note.entityAdmin.entityTypes.attribute.roles.none" />
            </p>
        </s:if>
        <s:else>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="attributeRoleName"><s:text
                        name="name.role" /> <a role="button" tabindex="0"
                       data-toggle="popover" data-trigger="focus" data-html="true"
                       title="" data-placement="top" data-content="to be inserted   "
                       data-original-title=""> <span class="fa fa-info-circle"></span></a>
                </label>
                <div class="col-sm-10">
                    <div class="input-group">
                        <wpsf:select name="attributeRoleName" id="attributeRoleName"
                                     list="#freeAttributeRoles" listKey="name"
                                     listValue="%{name + ' - ' + description}" cssClass="form-control"
                                     headerKey="" headerValue="%{getText('note.choose')}" />
                        <span class="input-group-btn"> <wpsf:submit type="button"
                                     action="addAttributeRole" value="%{getText('label.add')}"
                                     cssClass="btn btn-info" />
                        </span>
                    </div>
                </div>
            </div>

            <s:if test="null != attributeRoles && attributeRoles.size() > 0">
                <h3 class="margin-none">
                    <s:text name="label.roles.assigned" />
                </h3>
                <!--<ul class="list-group">-->

                <div class="form-group-separator"
                     style="border-bottom-color: #ededed"></div>

                <s:iterator value="attributeRoles" var="attributeRole">
                    <div class="form-group" style="margin-top: 10px">
                        <wpsa:actionParam action="removeAttributeRole" var="actionName">
                            <wpsa:actionSubParam name="attributeRoleName"
                                                 value="%{#attributeRole}" />
                        </wpsa:actionParam>
                        <label class="col-sm-2 control-label"> </label>
                        <div class="col-sm-10" style="margin-left: -20px;">
                            <!--<li class="list-group-item">-->
                            <div class="col-md-4">
                                    <!--<code><s:property value="attributeRole" /></code>-->
                                <s:property
                                    value="%{getAttributeRole(#attributeRole).description}" />
                            </div>
                            <div class="col-sm-8">
                                <wpsf:submit type="button" action="%{#actionName}"
                                             cssClass="btn btn-danger" title="%{getText('label.remove')}">
                                    <s:text name="label.delete" />
                                </wpsf:submit>
                            </div>
                            <!--</li>-->
                        </div>
                    </div>
                </s:iterator>

                <!--</ul>-->
            </s:if>
        </s:else>
    </fieldset>

    <s:set var="attributeDisablingCodesVar"
           value="getAttributeDisablingCodes()"></s:set>

    <s:if
        test="null != #attributeDisablingCodesVar && #attributeDisablingCodesVar.size()>0">
        <fieldset class="col-xs-12">
            <legend>
                <s:text name="name.disablingCodes" />
            </legend>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="disablingCode"><s:text
                        name="label.code" /> <a role="button" tabindex="0"
                       data-toggle="popover" data-trigger="focus" data-html="true"
                       title="" data-placement="top" data-content="to be inserted   "
                       data-original-title=""> <span class="fa fa-info-circle"></span></a>
                </label>
                <div class="col-sm-10">
                    <div class="input-group">
                        <wpsf:select name="disablingCode" id="disablingCode"
                                     list="#attributeDisablingCodesVar" cssClass="form-control" />
                        <span class="input-group-btn"> <wpsf:submit type="button"
                                     action="addAttributeDisablingCode"
                                     value="%{getText('label.add')}" cssClass="btn btn-info" />
                        </span>
                    </div>
                </div>
            </div>

            <s:if test="null != disablingCodes && disablingCodes.size() > 0">
                <h3 class="margin-none">
                    <s:text name="label.disablingCodes.assigned" />
                </h3>
                <div class="form-group-separator"
                     style="border-bottom-color: #ededed"></div>

                <s:iterator value="disablingCodes" var="disablingCode">
                    <div class="form-group" style="margin-top: 10px">
                        <wpsa:actionParam action="removeAttributeDisablingCode"
                                          var="actionName">
                            <wpsa:actionSubParam name="attributeRoleName"
                                                 value="%{#disablingCode}" />
                        </wpsa:actionParam>
                        <label class="col-sm-2 control-label"> </label>
                        <div class="col-sm-10" style="margin-left: -20px;">
                            <div class="col-sm-4">
                                    <!--<code><s:property value="#disablingCode" /></code>-->
                                <s:property
                                    value="%{#attributeDisablingCodesVar[#disablingCode]}" />
                            </div>
                            <div class="col-sm-8">
                                <wpsf:submit type="button" action="%{#actionName}"
                                             cssClass="btn btn-danger" title="%{getText('label.remove')}">
                                    <s:text name="label.delete" />
                                </wpsf:submit>
                            </div>
                        </div>
                    </div>
                </s:iterator>
            </s:if>


        </fieldset>
    </s:if>

    <s:if test="#attribute.textAttribute">
        <s:include
            value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-text.jsp" />
    </s:if>

    <s:elseif test="#attribute.type == 'Number'">
        <s:include
            value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-number.jsp" />
    </s:elseif>

    <s:elseif test="#attribute.type == 'Date'">
        <s:include
            value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-date.jsp" />
    </s:elseif>

    <s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
        <fieldset class="col-xs-12">
            <legend>
                <s:text name="label.settings" />
            </legend>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="listNestedType"><s:text
                        name="Entity.attribute.setting.listType" /> <a role="button"
                       tabindex="0" data-toggle="popover" data-trigger="focus"
                       data-html="true" title="" data-placement="top"
                       data-content="to be inserted   " data-original-title=""> <span
                            class="fa fa-info-circle"></span></a> </label>
                <div class="col-sm-10">
                    <wpsf:select list="getAllowedNestedTypes(#attribute)"
                                 name="listNestedType" id="listNestedType" listKey="type"
                                 listValue="type" cssClass="form-control" headerKey=""
                                 headerValue="%{getText('note.choose')}" />
                </div>
            </div>
        </fieldset>
    </s:if>

    <s:include
        value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-ognl.jsp" />
    <fieldset class="col-xs-12">
        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.continue" />
                </wpsf:submit>
            </div>

        </div>
    </fieldset>
</s:form>

