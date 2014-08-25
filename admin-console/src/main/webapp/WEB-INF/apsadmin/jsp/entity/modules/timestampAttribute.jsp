<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="%{#lang.default || #attributeTracer.listElement}">
		<div class="row">
			<div class="col-md-3 col-lg-3">
				<s:set var="currentDateAttributeName" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
				<label class="sr-only" for="<s:property value="%{#currentDateAttributeName}" />"><s:text name="label.date" /></label>
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/dateAttributeInputField.jsp" />
			</div>
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/timestampAttributeExtraInputField.jsp" />
		</div>
</s:if>
<s:else>
	<span class="form-control-static text-info"><s:text name="note.editContent.doThisInTheDefaultLanguage.must" />.</span>
</s:else>