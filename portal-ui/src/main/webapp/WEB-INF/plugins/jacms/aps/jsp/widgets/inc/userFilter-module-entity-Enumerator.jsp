<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="formFieldNameVar" value="${userFilterOptionVar.formFieldNames[0]}" />
<div class="control-group">
	<c:set var="i18n_Attribute_Key" value="${userFilterOptionVar.attribute.name}" />
	<label for="<c:out value="${formFieldNameVar}" />" class="control-label"><wp:i18n key="${i18n_Attribute_Key}" /></label>
	<div class="controls">
		<select name="${formFieldNameVar}" id="<c:out value="${formFieldNameVar}" />" class="input-xlarge">
			<option value=""><wp:i18n key="ALL" /></option>
			<c:forEach items="${userFilterOptionVar.attribute.items}" var="enumeratorItemVar">
			<option value="<c:out value="${enumeratorItemVar}" />"  <c:if test="${enumeratorItemVar == userFilterOptionVar.formFieldValues[formFieldNameVar]}"> selected="selected" </c:if>   ><c:out value="${enumeratorItemVar}" /></option>
			</c:forEach>
		</select>
	</div>

</div>
