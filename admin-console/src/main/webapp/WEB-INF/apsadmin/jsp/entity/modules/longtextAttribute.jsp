<%@ taglib uri="/struts-tags" prefix="s" %>
<s:textarea cols="50" rows="3" id="%{#attributeTracer.getFormFieldName(#attribute)}"
	name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.getTextForLang(#lang.code)}" cssClass="form-control" />