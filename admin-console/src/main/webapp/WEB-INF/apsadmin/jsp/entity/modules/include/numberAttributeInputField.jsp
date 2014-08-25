<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="#attribute.failedNumberString == null">
	<s:set var="numberAttributeValue" value="#attribute.value" />
</s:if>
<s:else>
	<s:set var="numberAttributeValue" value="#attribute.failedNumberString" />
</s:else>
<wpsf:textfield
		id="%{#attributeTracer.getFormFieldName(#attribute)}"
		name="%{#attributeTracer.getFormFieldName(#attribute)}"
		value="%{#numberAttributeValue}"
		maxlength="254"
		cssClass="form-control"
		/>