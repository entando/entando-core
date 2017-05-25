<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="currentDateAttributeName" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
<s:if test="#attribute.failedDateString == null">
    <s:set var="dateAttributeValue" value="#attribute.getFormattedDate('dd/MM/yyyy')" />
</s:if>
<s:else>
    <s:set var="dateAttributeValue" value="#attribute.failedDateString" />
</s:else>

<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />

<div class="input-group datepicker date" >
    <wpsf:textfield  id="%{#currentDateAttributeName}" name="%{#currentDateAttributeName}" value="%{#dateAttributeValue}" placeholder="dd/mm/yyyy" maxlength="254" cssClass="form-control"/>
    <div class="input-group-addon">

    </div>
</div>

