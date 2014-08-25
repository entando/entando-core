<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<c:set var="formFieldNameVar" value="${userFilterOptionVar.formFieldNames[0]}" />
<c:set var="formFieldValue" value="${userFilterOptionVar.formFieldValues[formFieldNameVar]}" />
<c:set var="i18n_Attribute_Key" value="${userFilterOptionVar.attribute.name}" />

<fieldset>
<legend><wp:i18n key="${i18n_Attribute_Key}" /></legend>
<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-entity-Boolean-ignoreOption.jsp" />

<div class="control-group">
	<div class="controls">
		<label for="true_<c:out value="${formFieldNameVar}" />" class="checkbox">
		<input name="<c:out value="${formFieldNameVar}" />"  id="true_<c:out value="${formFieldNameVar}" />" <c:if test="${null != formFieldValue || formFieldValue == 'true'}">checked="checked"</c:if> value="true" type="checkbox" />
		<wp:i18n key="YES"/></label>
	</div>
</div>

</fieldset>