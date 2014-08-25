<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set name="lang" value="defaultLang" />
<div class="col-xs-12 margin-large-top">
<%-- START CICLO ATTRIBUTI --%> 
<s:iterator value="userProfile.attributeList" var="attribute">
	<%-- INIZIALIZZAZIONE TRACCIATORE --%>
	<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

	<s:if test="#attribute.active"><s:set var="attributeActiveMarker" value="'modules'" /></s:if>
	<s:else><s:set var="attributeActiveMarker" value="'view'" /></s:else>
	
	<s:if test="null != #attribute.description"><s:set var="attributeLabelVar" value="#attribute.description" /></s:if>
	<s:else><s:set var="attributeLabelVar" value="#attribute.name" /></s:else>
	
	<s:set var="attributeFieldErrorsVar" value="%{fieldErrors[#attributeTracer.getFormFieldName(#attribute)]}" />
	<s:set var="attributeHasFieldErrorVar" value="#attributeFieldErrorsVar != null && !#attributeFieldErrorsVar.isEmpty()" />
	<s:set var="controlGroupErrorClassVar" value="%{''}" />
	<s:set var="inputErrorClassVar" value="%{''}" />
	<s:if test="#attributeHasFieldErrorVar">
		<s:set var="controlGroupErrorClassVar" value="%{' has-error'}" />
		<s:set var="inputErrorClassVar" value="%{' input-with-feedback'}" />
	</s:if>
	<div class="form-group<s:property value="controlGroupErrorClassVar" />">
	
	<s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
	<label class="display-block"><span class="icon fa fa-list"></span>&#32;
	<s:property value="#attributeLabelVar" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
	</s:if>
	<s:elseif test="#attribute.type == 'Image' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState' || #attribute.type == 'Composite'">
	<label class="display-block"><s:property value="#attributeLabelVar" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
	</s:elseif>
	<s:else>
		<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#attributeLabelVar" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
	</s:else>

	<s:if test="#attribute.type == 'Monotext'">
	<!-- ############# ATTRIBUTE Monotext ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/monotextAttribute.jsp'}" />
	</s:if>

	<s:elseif test="#attribute.type == 'Text'">
	<!-- ############# ATTRIBUTE Text ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/textAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Longtext'">
	<!-- ############# ATTRIBUTE Longtext ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/longtextAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Hypertext'">
	<!-- ############# ATTRIBUTE Hypertext ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/hypertextAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'CheckBox'">
	<!-- ############# ATTRIBUTE CheckBox ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/checkBoxAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Boolean'">
	<!-- ############# ATTRIBUTE Boolean ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/booleanAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'ThreeState'">
	<!-- ############# ATTRIBUTE ThreeState ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/threeStateAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Number'">
	<!-- ############# ATTRIBUTE Number ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/numberAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Date'">
	<!-- ############# ATTRIBUTE Date ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/dateAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Enumerator'">
	<!-- ############# ATTRIBUTE TESTO Enumerator ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/enumeratorAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Monolist'">
	<!-- ############# ATTRIBUTE Monolist ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/monolistAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'List'">
	<!-- ############# ATTRIBUTE List ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/listAttribute.jsp'}" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Composite'">
	<!-- ############# ATTRIBUTE Composite ############# -->
	<s:include value="%{'/WEB-INF/apsadmin/jsp/entity/' + #attributeActiveMarker + '/compositeAttribute.jsp'}" />
	</s:elseif>
	<s:if test="#attributeFieldErrorsVar">
		<p class="text-danger padding-small-vertical">
			<s:iterator value="#attributeFieldErrorsVar"><s:property /> </s:iterator>
		</p>
	</s:if>
	</div>
</s:iterator>
<%-- END CICLO ATTRIBUTI --%>
</div>