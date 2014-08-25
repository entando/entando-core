<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set name="checkedValue" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
<s:set var="currentCheckboxAttributeName" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
<label class="checkbox-inline" for="<s:property value="#currentCheckboxAttributeName" />">
	<wpsf:checkbox name="%{#currentCheckboxAttributeName}" id="%{#currentCheckboxAttributeName}" value="#checkedValue" />
	<s:text name="label.true" />
</label>