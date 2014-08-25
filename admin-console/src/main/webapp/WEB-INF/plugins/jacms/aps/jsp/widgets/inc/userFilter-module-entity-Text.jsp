<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<c:set var="formFieldNameVar" value="${userFilterOptionVar.formFieldNames[0]}" />
<c:set var="i18n_Attribute_Key" value="${userFilterOptionVar.attribute.name}" />
<div class="control-group">
	<label for="<c:out value="${formFieldNameVar}" />" class="control-label"><wp:i18n key="${i18n_Attribute_Key}" /></label>
	<div class="controls">
		<input name="<c:out value="${formFieldNameVar}" />" id="<c:out value="${formFieldNameVar}" />" value="${userFilterOptionVar.formFieldValues[formFieldNameVar]}" type="text" class="input-xlarge"/>
	</div>
</div>