<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<s:set var="validationRules" value="#attribute.validationRules" />
<s:set var="hasValidationRulesVar" value="%{#validationRules != null && #validationRules.ognlValidationRule != null&& #validationRules.ognlValidationRule.expression != null}" />
<s:if test="#attribute.required">&#32;<span class="icon fa fa-asterisk text-muted" title="<s:text name="Entity.attribute.flag.mandatory.full" />"></span></s:if>
<s:if test="#attribute.required || #attribute.searchable || #attribute.indexingType != 'NONE' || (#attribute.textAttribute && (#attribute.minLength != -1 || #attribute.maxLength != -1)) || (#hasValidationRulesVar) ">

<s:set var="htmlContent">
	<ul class="fa-ul">

	<s:if test="#attribute.required">
		<li><span class="icon fa fa-li fa-asterisk"></span><s:text name="Entity.attribute.flag.mandatory.full" /></li>
	</s:if>

	<s:if test="#attribute.searchable">
		<li><span class="icon fa fa-li fa-filter"></span><s:text name="Entity.attribute.flag.searchable.full" /></li>
	</s:if>

	<s:if test="#attribute.indexingType != 'NONE'">
		<li><span class="icon fa fa-li fa-search"></span><s:text name="Entity.attribute.flag.indexed.full" /></li>
	</s:if>

	<s:if test="#attribute.textAttribute">
		<s:if test="#attribute.minLength != -1">
			<li><span class="icon fa fa-li fa-ellipsis-h"></span><s:text name="Entity.attribute.flag.minLength.short" />:	<s:property value="#attribute.minLength" /></li>
		</s:if>

		<s:if test="#attribute.maxLength != -1">
			<li><span class="icon fa fa-li fa-ellipsis-h"></span><s:text name="Entity.attribute.flag.maxLength.short" />: <s:property value="#attribute.maxLength" /></li>
		</s:if>
	</s:if>

	<s:if test="#hasValidationRulesVar">
		<li><span class="icon fa fa-li fa-check-circle-o"></span>
		<s:if test="#validationRules.ognlValidationRule.helpMessageKey != null">
			<s:set var="labelKey" value="#validationRules.ognlValidationRule.helpMessageKey" scope="page" />
			<s:set var="langCode" value="currentLang.code" scope="page" />
				<wp:i18n key="${labelKey}" lang="${langCode}" />
		</s:if>
		<s:else>
			<s:property value="#validationRules.ognlValidationRule.helpMessage" />
		</s:else>
		</li>
	</s:if>

	</ul>
</s:set>
</s:if>

<span class="label label-info pull-right" data-toggle="popover" data-placement="left" data-html="true" data-content="<s:property value="htmlContent" />" title="" data-original-title="Attribute info"><span class="icon fa fa-info"></span></span>