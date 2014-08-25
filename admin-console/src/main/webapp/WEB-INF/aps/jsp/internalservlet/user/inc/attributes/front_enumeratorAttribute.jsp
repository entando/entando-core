<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<wpsf:select useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="%{attribute_id}"  
	headerKey="" headerValue="" 
	list="#attribute.items" value="%{#attribute.getText()}" />
