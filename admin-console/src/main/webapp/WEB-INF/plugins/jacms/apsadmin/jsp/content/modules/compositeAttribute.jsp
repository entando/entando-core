<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<s:set var="parentListAttribute" value="#masterListAttribute" />
<s:set name="masterCompositeAttributeTracer" value="#attributeTracer" />
<s:set name="masterCompositeAttribute" value="#attribute" />
<ul class="list-group">
	<s:iterator value="#attribute.attributes" var="attribute">
		<s:set name="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)"></s:set>
		<s:set name="parentAttribute" value="#masterCompositeAttribute"></s:set>
		<s:property value="%{#attributeTracer.setLang(null)}" />

		<s:set var="CompositeAttributeNestedErrorKeyVar" value="%{(#parentListAttribute!=null ? #parentListAttribute.type+':' : '')+(#masterCompositeAttribute.type)+':'+(#attribute.type)+':'+(#masterCompositeAttribute.name)+'_'+(#attribute.name)+(#parentListAttribute!=null ? '_'+#elementStatus.index.toString() : '')}" />
		<s:set var="CompositeAttributeNestedErrorKeyVarV2" value="%{(#parentListAttribute!=null ? #parentListAttribute.type+':' : '')+(#masterCompositeAttribute.type)+':'+(#attribute.type)+':'+#lang.code+'_'+(#masterCompositeAttribute.name)+'_'+(#attribute.name)+(#parentListAttribute!=null ? '_'+#elementStatus.index.toString() : '')}" />
		<s:set var="CompositeAttributeFieldErrorsVar" value="%{fieldErrors[#attribute.name]}" />
		<s:set var="CompositeAttributeHasFieldErrorVar" value="#CompositeAttributeFieldErrorsVar != null && !#CompositeAttributeFieldErrorsVar.isEmpty()" />
		<s:set var="CompositeAttributeFieldNameErrorsVar" value="%{fieldErrors[#attributeTracer.getFormFieldName(#attribute)]}" />
		<s:set var="CompositeAttributeHasFieldNameErrorVar" value="#CompositeAttributeFieldNameErrorsVar != null && !#CompositeAttributeFieldNameErrorsVar.isEmpty()" />
		<s:set var="CompositeAttributeFieldNameErrorsVarV2" value="%{fieldErrors[#attribute.type+':'+#attribute.name]}" />
		<s:set var="CompositeAttributeHasFieldNameErrorVarV2" value="#CompositeAttributeFieldNameErrorsVarV2 != null && !#CompositeAttributeFieldNameErrorsVarV2.isEmpty()" />
		<s:set var="CompositeAttributeNestedErrorsVar" value="%{fieldErrors[#CompositeAttributeNestedErrorKeyVar]}" />
		<s:set var="CompositeAttributeHasNestedErrorsVar" value="%{#CompositeAttributeNestedErrorsVar != null && !#CompositeAttributeNestedErrorsVar.isEmpty()}" />
		<s:set var="CompositeAttributeNestedErrorsVarV2" value="%{fieldErrors[#CompositeAttributeNestedErrorKeyVarV2]}" />
		<s:set var="CompositeAttributeHasNestedErrorsVarV2" value="%{#CompositeAttributeNestedErrorsVarV2 != null && !#CompositeAttributeNestedErrorsVarV2.isEmpty()}" />
		<s:set var="CompositeAttributeHasErrorVar" value="%{#CompositeAttributeHasFieldErrorVar||#CompositeAttributeHasFieldNameErrorVar||#CompositeAttributeHasFieldNameErrorVarV2||#CompositeAttributeHasNestedErrorsVar||#CompositeAttributeHasNestedErrorsVarV2}" />

		<s:set var="CompositeAttributeControlGroupErrorClassVar" value="''" />
		<s:set var="CompositeAttributeInputErrorClassVar" value="''" />
		<s:if test="#CompositeAttributeHasErrorVar">
			<s:set var="CompositeAttributeControlGroupErrorClassVar" value="' panel-danger'" />
			<s:set var="CompositeAttributeInputErrorClassVar" value="' input-with-feedback'" />
		</s:if>

		<s:property value="%{#attributeTracer.setLang(#lang)}" />

		<li class="list-group-item<s:property value="#CompositeAttributeControlGroupErrorClassVar" />">
			<s:if test="null != #attribute.description"><s:set var="compositeElementLabelVar" value="#attribute.description" /></s:if>
			<s:else><s:set var="compositeElementLabelVar" value="#attribute.name" /></s:else>

			<s:if test="#attribute.type == 'Image' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
				<label class="display-block">
					<s:property value="#compositeElementLabelVar" />
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
				</label>
			</s:if>
			<s:else>
				<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
					<s:property value="#compositeElementLabelVar"/>
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
				</label>
			</s:else>

			<s:if test="#attribute.type == 'Boolean'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
			</s:if>
			<s:elseif test="#attribute.type == 'CheckBox'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Date'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Enumerator'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Hypertext'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Longtext'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Monotext'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Number'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Text'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'ThreeState'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Timestamp'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/timestampAttribute.jsp" />
			</s:elseif>

			<%-- jacms attribute types --%>
				<s:elseif test="#attribute.type == 'Image'">
					<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Attach'">
					<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/attachAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Link'">
					<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/linkAttribute.jsp" />
				</s:elseif>

			<wpsa:hookPoint key="jacms.entryContent.attributeExtra" objectName="hookPointElements_jacms_entryContent_attributeExtra">
			<s:iterator value="#hookPointElements_jacms_entryContent_attributeExtra" var="hookPointElement">
				<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
			</s:iterator>
			</wpsa:hookPoint>

			<s:if test="#CompositeAttributeHasErrorVar">
				<p class="text-danger margin-none padding-none padding-small-top">
					<jsp:useBean id="CompositeAttributeErrorMapVar" class="java.util.HashMap" scope="request"/>
					<s:iterator value="#CompositeAttributeFieldErrorsVar"><s:set var="attributeCurrentError" scope="page" /><c:set target="${CompositeAttributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/></s:iterator>
					<s:iterator value="#CompositeAttributeFieldNameErrorsVar"><s:set var="attributeCurrentError" scope="page" /><c:set target="${CompositeAttributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/></s:iterator>
					<s:iterator value="#CompositeAttributeFieldNameErrorsVarV2"><s:set var="attributeCurrentError" scope="page" /><c:set target="${CompositeAttributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/></s:iterator>
					<s:iterator value="#CompositeAttributeNestedErrorsVar"><s:set var="attributeCurrentError" scope="page" /><c:set target="${CompositeAttributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/></s:iterator>
					<s:iterator value="#CompositeAttributeNestedErrorsVarV2"><s:set var="attributeCurrentError" scope="page" /><c:set target="${CompositeAttributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/></s:iterator>
					<c:forEach items="${CompositeAttributeErrorMapVar}" var="attributeCurrentError">
						<c:out value="${attributeCurrentError.value}" /><br />
					</c:forEach>
					<c:set var="CompositeAttributeErrorMapVar" value="${null}" />
					<c:set var="attributeCurrentError" value="${null}" />
				</p>
			</s:if>
		</li>
	</s:iterator>
</ul>
<s:set var="attributeTracer" value="#masterCompositeAttributeTracer" />
<s:set var="attribute" value="#masterCompositeAttribute" />
<s:set var="parentAttribute" value="%{null}" />