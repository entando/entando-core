<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<c:set var="formFieldNameVar" value="${userFilterOptionVar.formFieldNames[0]}" />
<c:set var="formFieldStartOptionVar" value="${userFilterOptionVar.formFieldNames[1]}" />
<div class="control-group">
	<label for="<c:out value="${formFieldNameVar}" />" class="control-label"><wp:i18n key="TEXT" /></label>
	<div class="controls">
		<input name="<c:out value="${formFieldNameVar}" />" id="<c:out value="${formFieldNameVar}" />" value="${userFilterOptionVar.formFieldValues[formFieldNameVar]}" type="text" class="input-xlarge"/>
		<select name="${formFieldStartOptionVar}" id="<c:out value="${formFieldStartOptionVar}" />" class="input-xlarge">
			<option value="allwords" <c:if test="${'allwords' == userFilterOptionVar.formFieldValues[formFieldStartOptionVar]}">selected="selected"</c:if>><wp:i18n key="jacms_SEARCH_TEXT_ALL_WORDS" /></option>
			<option value="oneword" <c:if test="${'oneword' == userFilterOptionVar.formFieldValues[formFieldStartOptionVar]}">selected="selected"</c:if>><wp:i18n key="jacms_SEARCH_TEXT_AT_LEAST_ONE_WORD" /></option>
			<option value="exact" <c:if test="${'exact' == userFilterOptionVar.formFieldValues[formFieldStartOptionVar]}">selected="selected"</c:if>><wp:i18n key="jacms_SEARCH_TEXT_EXACT" /></option>
		</select>
	</div>
</div>
