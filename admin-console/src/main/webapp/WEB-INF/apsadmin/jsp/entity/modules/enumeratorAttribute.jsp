<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<s:if test="#lang.default">
	<s:set var="currentEnumeratorAttributeName" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
	<wpsf:select 
		name="%{#currentEnumeratorAttributeName}" 
		id="%{#currentEnumeratorAttributeName}" 
		headerKey="" 
		headerValue="%{getText('label.none')}" 
		list="#attribute.items" 
		value="%{#attribute.getText()}" 
		cssClass="form-control" />
</s:if>
<s:else>
	<s:if test="#attributeTracer.listElement">
		<s:set var="currentEnumeratorAttributeName" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
		<wpsf:select 
			name="%{#currentEnumeratorAttributeName}" 
			id="%{#currentEnumeratorAttributeName}" 
			headerKey="" 
			headerValue="%{getText('label.none')}" 
			list="#attribute.items" 
			value="%{#attribute.getText()}" 
			cssClass="form-control" />
	</s:if>
	<s:else>
		<span class="form-control-static text-info"><s:text name="note.editContent.doThisInTheDefaultLanguage.must" />.</span>
	</s:else>
</s:else>