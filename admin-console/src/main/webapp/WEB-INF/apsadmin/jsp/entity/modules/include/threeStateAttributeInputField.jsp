<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="currentThreestateAttributeNameVar" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
<div class="btn-group" data-toggle="buttons">
	<label class="btn btn-default <s:if test="%{#attribute.booleanValue != null && #attribute.booleanValue == true}"> active </s:if>" for="true_<s:property value="#currentThreestateAttributeNameVar" />">
		<wpsf:radio 
			name="%{#currentThreestateAttributeNameVar}" 
			id="true_%{#currentThreestateAttributeNameVar}" 
			value="true" 
			checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}" />
		<s:text name="label.yes"/>
	</label>
	<label class="btn btn-default <s:if test="%{#attribute.booleanValue != null && #attribute.booleanValue == false}"> active </s:if>" for="false_<s:property value="#currentThreestateAttributeNameVar" />">
		<wpsf:radio 
			name="%{#currentThreestateAttributeNameVar}" 
			id="false_%{#currentThreestateAttributeNameVar}" 
			value="false" 
			checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" />
		<s:text name="label.no"/>
	</label>
	<label class="btn btn-default <s:if test="%{#attribute.booleanValue == null}"> active </s:if>" for="none_<s:property value="#currentThreestateAttributeNameVar" />">
		<wpsf:radio 
			name="%{#currentThreestateAttributeNameVar}" 
			id="none_%{#currentThreestateAttributeNameVar}" 
			value="" 
			checked="%{#attribute.booleanValue == null}"/>
		<s:text name="label.bothYesAndNo"/>
	</label>
</div>