<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wpsf:textfield useTabindexAutoIncrement="true" id="%{attribute_id}" 
	name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.getTextForLang(#lang.code)}"
	maxlength="254" />