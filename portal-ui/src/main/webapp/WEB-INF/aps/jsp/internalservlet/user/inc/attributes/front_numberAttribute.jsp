<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:if test="#attribute.failedNumberString == null">
	<s:set name="numberAttributeValue" value="#attribute.value"></s:set>
</s:if>
<s:else>
	<s:set name="numberAttributeValue" value="#attribute.failedNumberString"></s:set>
</s:else>
<wpsf:textfield 
		useTabindexAutoIncrement="true" 
		id="%{#attribute_id}" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		value="%{#numberAttributeValue}"
		maxlength="254" />