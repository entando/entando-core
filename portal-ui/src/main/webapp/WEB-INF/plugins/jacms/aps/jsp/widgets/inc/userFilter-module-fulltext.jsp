<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<c:set var="formFieldNameVar" value="${userFilterOptionVar.formFieldNames[0]}" />
<c:set var="formFieldOptionVar" value="${userFilterOptionVar.formFieldNames[1]}" />
<c:set var="formFieldOptionValue" value="${userFilterOptionVar.formFieldValues[formFieldOptionVar]}" />
<c:set var="formFieldAttachOptionVar" value="${userFilterOptionVar.formFieldNames[2]}" />
<c:set var="formFieldAttachOptionValue" value="${userFilterOptionVar.formFieldValues[formFieldAttachOptionVar]}" />
<div class="control-group">
    <label for="<c:out value="${formFieldNameVar}" />" class="controlTitle"><wp:i18n key="FULL_TEXT_SEARCH" /></label><br>
    <div class="controls">
        <input name="<c:out value="${formFieldNameVar}" />" id="<c:out value="${formFieldNameVar}" />" value="${userFilterOptionVar.formFieldValues[formFieldNameVar]}" type="text" class="input-xlarge inputCustom"/>
        <select name="${formFieldOptionVar}" id="<c:out value="${formFieldOptionVar}" />" class="input-large">
            <option value="allwords" <c:if test="${'allwords' == formFieldOptionValue}">selected="selected"</c:if>><wp:i18n key="jacms_SEARCH_TEXT_ALL_WORDS" /></option>
            <option value="oneword" <c:if test="${'oneword' == formFieldOptionValue}">selected="selected"</c:if>><wp:i18n key="jacms_SEARCH_TEXT_AT_LEAST_ONE_WORD" /></option>
            <option value="exact" <c:if test="${'exact' == formFieldOptionValue}">selected="selected"</c:if>><wp:i18n key="jacms_SEARCH_TEXT_EXACT" /></option>
        </select>
        <label for="true_<c:out value="${formFieldAttachOptionVar}" />" class="checkbox">
        <input name="<c:out value="${formFieldAttachOptionVar}" />" id="true_<c:out value="${formFieldAttachOptionVar}" />" 
               <c:if test="${null != formFieldAttachOptionValue || formFieldAttachOptionValue == 'true'}">checked="checked"</c:if> 
                   value="true" type="checkbox" />
        <wp:i18n key="jacms_SEARCH_INCLUDE_ATTACHMENTS" /></label>
    </div>
</div>
