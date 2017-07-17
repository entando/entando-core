<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<s:set var="validationRules" value="#attribute.validationRules" />
<s:set var="hasValidationRulesVar" value="%{#validationRules != null && #validationRules.ognlValidationRule != null&& #validationRules.ognlValidationRule.expression != null}" />
<s:if test="#attribute.required"><i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i></s:if>
<s:if test="#attribute.required || #attribute.searchable || #attribute.indexingType != 'NONE' || (#attribute.textAttribute && (#attribute.minLength != -1 || #attribute.maxLength != -1)) || (#hasValidationRulesVar) ">

    <s:set var="htmlContent">
        <s:if test="#attribute.required">
        </s:if>
        <s:if test="#attribute.searchable">
            <p></span><s:text name="Entity.attribute.flag.searchable.full" /></p>
            </s:if>
            <s:if test="#attribute.indexingType != 'NONE'">
        <p></span><s:text name="Entity.attribute.flag.indexed.full" /></p>
        </s:if>
        <s:if test="#attribute.textAttribute">
            <s:if test="#attribute.minLength != -1">
        <p></span><s:text name="Entity.attribute.flag.minLength.short" />:  <s:property value="#attribute.minLength" /></p>
        </s:if>
        <s:if test="#attribute.maxLength != -1">
    <p></span><s:text name="Entity.attribute.flag.maxLength.short" />: <s:property value="#attribute.maxLength" /></p>
    </s:if>
</s:if>
<s:if test="#hasValidationRulesVar">
<p></span>
    <s:if test="#validationRules.ognlValidationRule.helpMessageKey != null">
        <s:set var="labelKey" value="#validationRules.ognlValidationRule.helpMessageKey" scope="page" />
        <s:set var="langCode" value="currentLang.code" scope="page" />
        <wp:i18n key="${labelKey}" lang="${langCode}" />
    </s:if>
    <s:else>
        <s:property value="#validationRules.ognlValidationRule.helpMessage" />
    </s:else>
</p>
</s:if>
</s:set>
<a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:property value="htmlContent" />" data-placement="top" data-original-title="">
    <span class="fa fa-info-circle"></span>
</a>
</s:if>

