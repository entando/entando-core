<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<c:set var="formFieldNameControlVar" value="${userFilterOptionVar.formFieldNames[2]}" />
<input name="<c:out value="${formFieldNameControlVar}" />" type="hidden" value="true" />
<c:set var="formFieldNameIgnoreVar" value="${userFilterOptionVar.formFieldNames[1]}" />
<c:set var="formFieldIgnoreValue" value="${userFilterOptionVar.formFieldValues[formFieldNameIgnoreVar]}" />
<c:set var="formFieldControlValue" value="${userFilterOptionVar.formFieldValues[formFieldNameControlVar]}" />

<div class="controls">
	<label for="ignore_<c:out value="${userFilterOptionVar.formFieldNames[1]}" />" class="checkbox">
	<input id="ignore_<c:out value="${userFilterOptionVar.formFieldNames[1]}" />" name="<c:out value="${userFilterOptionVar.formFieldNames[1]}" />" <c:if test="${(null == formFieldIgnoreValue && null == formFieldControlValue) || (null != formFieldControlValue && formFieldIgnoreValue == 'true')}">checked="checked"</c:if> value="true" type="checkbox" />
	<wp:i18n key="IGNORE" /></label>
</div>