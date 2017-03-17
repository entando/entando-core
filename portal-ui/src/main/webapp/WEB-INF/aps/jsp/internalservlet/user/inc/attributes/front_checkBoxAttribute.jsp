<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="checkedValue" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
<wpsf:checkbox useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="%{attribute_id}" value="#checkedValue"/>
