<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="currentDateAttributeName" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
<s:if test="#attribute.failedDateString == null">
	<s:set var="dateAttributeValue" value="#attribute.getFormattedDate('dd/MM/yyyy')" />
</s:if>
<s:else>
	<s:set var="dateAttributeValue" value="#attribute.failedDateString" />
</s:else>
<wpsf:textfield id="%{#currentDateAttributeName}"
		name="%{#currentDateAttributeName}" value="%{#dateAttributeValue}"
		maxlength="254" cssClass="form-control datepicker" />
<%-- //TODO: create label for dd/MM/yyyy --%>
<span class="help help-block">dd/MM/yyyy</span>