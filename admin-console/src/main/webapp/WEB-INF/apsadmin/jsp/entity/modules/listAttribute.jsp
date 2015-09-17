<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#attribute.getAttributeList(#lang.code).size() != 0">
<ul class="list-group">
</s:if>
<s:set var="masterListAttributeTracer" value="#attributeTracer" />
<s:set var="masterListAttribute" value="#attribute" />
<s:iterator value="#attribute.getAttributeList(#lang.code)" var="attribute" status="elementStatus">
	<s:set var="attributeTracer" value="#masterListAttributeTracer.getListElementTracer(#lang, #elementStatus.index)" />
	<s:set var="elementIndex" value="#elementStatus.index" />

	<s:set var="masterAttributeFieldErrorsVar" value="#attributeFieldErrorsVar" />
	<s:set var="masterAttributeHasFieldErrorVar" value="#attributeHasFieldErrorVar" />
	<s:set var="masterAontrolGroupErrorClassVar" value="#controlGroupErrorClassVar" />
	<s:set var="masterAnputErrorClassVar" value="#inputErrorClassVar" />
	<s:set var="masterAttributeFieldNameErrorsVar" value="#attributeFieldNameErrorsVar" />
	<s:set var="masterAttributeHasFieldNameErrorVar" value="#attributeHasFieldNameErrorVar" />
	<s:set var="masterAttributeFieldNameErrorsVarV2" value="#attributeFieldNameErrorsVarV2" />
	<s:set var="masterAttributeHasFieldNameErrorVarV2" value="#attributeHasFieldNameErrorVarV2" />
	<s:set var="masterAttributeHasErrorVar" value="#attributeHasErrorVar" />

	<s:set var="attributeFieldErrorsVar" value="%{fieldErrors[#attribute.name]}" />
	<s:set var="attributeHasFieldErrorVar" value="#attributeFieldErrorsVar != null && !#attributeFieldErrorsVar.isEmpty()" />
	<s:set var="attributeFieldNameErrorsVar" value="%{fieldErrors[#attributeTracer.getFormFieldName(#attribute)]}" />
	<s:set var="attributeHasFieldNameErrorVar" value="#attributeFieldNameErrorsVar != null && !#attributeFieldNameErrorsVar.isEmpty()" />
	<s:set var="attributeFieldNameErrorsVarV2" value="%{fieldErrors[#attribute.type+':'+#attribute.name]}" />
	<s:set var="attributeHasFieldNameErrorVarV2" value="#attributeFieldNameErrorsVarV2 != null && !#attributeFieldNameErrorsVarV2.isEmpty()" />
	<s:set var="attributeHasErrorVar" value="%{#attributeHasFieldErrorVar||#attributeHasFieldNameErrorVar||#attributeHasFieldNameErrorVarV2}" />

	<s:set var="controlGroupErrorClassVar" value="''" />
	<s:set var="inputErrorClassVar" value="''" />
	<s:if test="#attributeHasErrorVar">
		<s:set var="controlGroupErrorClassVar" value="' panel-danger'" />
		<s:set var="inputErrorClassVar" value="' input-with-feedback'" />
	</s:if>

	<li class="list-group-item">
		<div class="panel panel-default margin-none<s:property value="%{#controlGroupErrorClassVar}" />"><%-- panel panel-default --%>
			<div class="panel-heading"><%--panel heading --%>
				<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" class=""><s:property value="#elementStatus.count" /></label>
				<div class="pull-right margin-small-bottom">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_operationModule.jsp" />
				</div>
			</div><%--panel heading --%>
			<div class="clearfix"></div>
			<div class="panel-body">
				<s:if test="#attribute.type == 'CheckBox'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
				</s:if>
				<s:elseif test="#attribute.type == 'Boolean'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Date'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Enumerator'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'EnumeratorMap'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorMapAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Monotext'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Number'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'ThreeState'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Timestamp'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/timestampAttribute.jsp" />
				</s:elseif>

				<s:if test="#attributeHasErrorVar">
					<p class="text-danger margin-none padding-none padding-small-top">
						<jsp:useBean id="ListAttributeErrorMapVar" class="java.util.HashMap" scope="request"/>
						<s:iterator value="#attributeFieldErrorsVar"><s:set var="attributeCurrentError" scope="page" /><c:set target="${ListAttributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/></s:iterator>
						<s:iterator value="#attributeFieldNameErrorsVar"><s:set var="attributeCurrentError" scope="page" /><c:set target="${ListAttributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/></s:iterator>
						<s:iterator value="#attributeFieldNameErrorsVarV2"><s:set var="attributeCurrentError" scope="page" /><c:set target="${ListAttributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/></s:iterator>
						<c:forEach items="${ListAttributeErrorMapVar}" var="attributeCurrentError">
							<c:out value="${attributeCurrentError.value}" /><br />
						</c:forEach>
						<c:set var="ListAttributeErrorMapVar" value="${null}" />
						<c:set var="attributeCurrentError" value="${null}" />
					</p>
				</s:if>

				<s:set var="attributeFieldErrorsVar" value="#masterAttributeFieldErrorsVar" />
				<s:set var="attributeHasFieldErrorVar" value="#masterAttributeHasFieldErrorVar" />
				<s:set var="controlGroupErrorClassVar" value="#masterAontrolGroupErrorClassVar" />
				<s:set var="inputErrorClassVar" value="#masterAnputErrorClassVar" />
				<s:set var="attributeFieldNameErrorsVar" value="#masterAttributeFieldNameErrorsVar" />
				<s:set var="attributeHasFieldNameErrorVar" value="#masterAttributeHasFieldNameErrorVar" />
				<s:set var="attributeFieldNameErrorsVarV2" value="#masterAttributeFieldNameErrorsVarV2" />
				<s:set var="attributeHasFieldNameErrorVarV2" value="#masterAttributeHasFieldNameErrorVarV2" />
				<s:set var="attributeHasErrorVar" value="#masterAttributeHasErrorVar" />

			</div><%-- panel body --%>
		</div><%-- panel panel-default --%>
	</li>
	</s:iterator>

<s:set var="attributeTracer" value="#masterListAttributeTracer" />
<s:set var="attribute" value="#masterListAttribute" />
<s:set var="elementIndex" value="%{null}" />
<s:if test="#attribute.getAttributeList(#lang.code).size() != 0">
</ul>
</s:if>

<p class="button-align"><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_addElementButton.jsp" /></p>