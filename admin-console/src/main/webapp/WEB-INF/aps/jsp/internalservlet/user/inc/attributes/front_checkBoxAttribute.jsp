<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wpsf:checkbox useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="%{attribute_id}" value="#checkedValue"/>
