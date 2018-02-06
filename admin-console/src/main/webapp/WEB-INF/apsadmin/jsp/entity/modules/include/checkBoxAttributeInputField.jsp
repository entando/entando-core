<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="checkedValue" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
<s:set var="currentCheckboxAttributeName" value="%{#attributeTracer.getFormFieldName(#attribute)}" />

<label  for="<s:property value="#currentCheckboxAttributeName" />">
    <wpsf:checkbox name="%{#currentCheckboxAttributeName}" id="%{#currentCheckboxAttributeName}" value="#checkedValue" cssClass="bootstrap-switch"/>
</label>


