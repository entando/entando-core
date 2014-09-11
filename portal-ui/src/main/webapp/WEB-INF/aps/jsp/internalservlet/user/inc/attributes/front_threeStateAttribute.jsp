<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<label class="radio inline" for="<s:property value="%{#attribute_id + '-none'}" />">
	<wpsf:radio 
		useTabindexAutoIncrement="true" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id + '-none'}" 
		value="" 
		checked="%{#attribute.booleanValue == null}" 
		cssClass="radio" />
		<wp:i18n key="userprofile_BOTH_YES_AND_NO" />
</label>
&#32;
<label class="radio inline" for="<s:property value="%{#attribute_id + '-true'}" />">
	<wpsf:radio 
		useTabindexAutoIncrement="true" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id + '-true'}" 
		value="true" 
		checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}"
		cssClass="radio" />
		<wp:i18n key="userprofile_YES" />
</label>
&#32;
<label class="radio inline" for="<s:property value="%{#attribute_id + '-false'}" />">
	<wpsf:radio 
		useTabindexAutoIncrement="true" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id + '-false'}" 
		value="false" 
		checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" 
		cssClass="radio" />
		<wp:i18n key="userprofile_NO" />
</label>
