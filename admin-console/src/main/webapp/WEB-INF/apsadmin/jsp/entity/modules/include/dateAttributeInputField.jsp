<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="currentDateAttributeName" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
<s:if test="#attribute.failedDateString == null">
	<s:set var="dateAttributeValue" value="#attribute.getFormattedDate('dd/MM/yyyy')" />
</s:if>
<s:else>
	<s:set var="dateAttributeValue" value="#attribute.failedDateString" />
</s:else>


<div class="input-group date" data-provide="datepicker">
    <wpsf:textfield id="%{#currentDateAttributeName}"
		name="%{#currentDateAttributeName}" value="%{#dateAttributeValue}"
		maxlength="254" cssClass="form-control"/>
    <div class="input-group-addon">
        <span class="fa fa-th"></span>
    </div>
</div>
<span class="help help-block">dd/MM/yyyy</span>