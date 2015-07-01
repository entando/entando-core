<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set name="checkedValue" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
<wpsf:checkbox useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="%{attribute_id}" value="#checkedValue"/>
