<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="currentBooleanAttributeNameVar" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
<div class="btn-group" data-toggle="buttons">
	<label class="btn btn-default <s:if test="%{#attribute.value == true}"> active </s:if>" for="true_<s:property value="#currentBooleanAttributeNameVar" />">
		<wpsf:radio name="%{#currentBooleanAttributeNameVar}" id="true_%{#currentBooleanAttributeNameVar}" value="true" checked="%{#attribute.value == true}" />
		<s:text name="label.yes"/>
	</label>
	<label class="btn btn-default <s:if test="%{#attribute.value == false}"> active </s:if>" for="false_<s:property value="#currentBooleanAttributeNameVar" />">
		<wpsf:radio name="%{#currentBooleanAttributeNameVar}" id="false_%{#currentBooleanAttributeNameVar}" value="false" checked="%{#attribute.value == false}" />
		<s:text name="label.no"/>
	</label>
</div>