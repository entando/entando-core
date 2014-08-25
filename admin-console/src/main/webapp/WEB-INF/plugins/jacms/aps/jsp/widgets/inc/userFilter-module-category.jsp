<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="formFieldNameVar" value="${userFilterOptionVar.formFieldNames[0]}" />
<c:set var="formFieldValue" value="${userFilterOptionVar.formFieldValues[formFieldNameVar]}" />

<wp:categories var="systemCategories" titleStyle="prettyFull" root="${userFilterOptionVar.userFilterCategoryCode}" />
<div class="control-group">
	<label for="category" class="control-label"><wp:i18n key="CATEGORY" /></label>
	<div class="controls">
		<select id="category" name="<c:out value="${formFieldNameVar}" />" class="input-xlarge">
			<option value=""><wp:i18n key="ALL" /></option>
			<c:forEach items="${systemCategories}" var="systemCategory">
			<option value="<c:out value="${systemCategory.key}" />" <c:if test="${formFieldValue == systemCategory.key}">selected="selected"</c:if> ><c:out value="${systemCategory.value}" /></option>
			</c:forEach>
		</select>
	</div>
</div>