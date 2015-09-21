<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<c:set var="formFieldNameVar" value="${userFilterOptionVar.formFieldNames[0]}" />
<div class="control-group">
    <label for="<c:out value="${formFieldNameVar}" />" class="controlTitle"><wp:i18n key="FULL_TEXT_SEARCH" /></label><br>
    <div class="controls">
        <input name="<c:out value="${formFieldNameVar}" />" id="<c:out value="${formFieldNameVar}" />" value="${userFilterOptionVar.formFieldValues[formFieldNameVar]}" type="text" class="input-xlarge inputCustom"/>
    </div>
</div>
