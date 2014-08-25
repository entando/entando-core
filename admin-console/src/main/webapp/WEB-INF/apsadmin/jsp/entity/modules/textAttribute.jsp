<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wpsf:textfield id="%{#attributeTracer.getFormFieldName(#attribute)}"
	name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.getTextForLang(#lang.code)}"
	maxlength="254" cssClass="form-control" />