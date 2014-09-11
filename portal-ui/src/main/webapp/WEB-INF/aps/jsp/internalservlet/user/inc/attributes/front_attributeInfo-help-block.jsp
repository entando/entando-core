<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="validationRules" value="#attribute.validationRules.ognlValidationRule" />
<s:set var="hasValidationRulesVar" value="%{#validationRules != null && #validationRules.expression != null}" />
&#32;
<s:if test="%{#hasValidationRulesVar || (#attribute.textAttribute && (#attribute.minLength != -1 || #attribute.maxLength != -1)) || #attribute.type == 'Date' }">
	<span class="help-block">
		<s:if test="#attribute.type == 'Date'">dd/MM/yyyy&#32;</s:if>
		<s:if test="%{#validationRules.helpMessageKey != null}">
		<s:set var="label" scope="page" value="#validationRules.helpMessageKey" /><wp:i18n key="${label}" /></s:if>
		<s:elseif test="%{#validationRules.helpMessage != null}"><s:property value="#validationRules.helpMessage" /></s:elseif>
		<s:if test="#attribute.minLength != -1">
			&#32;
			<abbr title="<wp:i18n key="userprofile_ENTITY_ATTRIBUTE_MINLENGTH_FULL" />&#32;<s:property value="#attribute.minLength" />">
				<wp:i18n key="userprofile_ENTITY_ATTRIBUTE_MINLENGTH_SHORT" />:&#32;<s:property value="#attribute.minLength" />
			</abbr>
		</s:if>
		<s:if test="#attribute.maxLength != -1">
			&#32;
			<abbr title="<wp:i18n key="userprofile_ENTITY_ATTRIBUTE_MAXLENGTH_FULL" />&#32;<s:property value="#attribute.maxLength" />">
				<wp:i18n key="userprofile_ENTITY_ATTRIBUTE_MAXLENGTH_SHORT" />:&#32;<s:property value="#attribute.maxLength" />
			</abbr>
		</s:if>
	</span>
</s:if>