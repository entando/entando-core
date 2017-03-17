<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#attribute.getAttributeList(#lang.code).size() != 0">
<ul>
</s:if> 
<s:set var="masterListAttributeTracer" value="#attributeTracer" />
<s:set var="masterListAttribute" value="#attribute" />
	<s:iterator value="#attribute.getAttributeList(#lang.code)" var="attribute" status="elementStatus">
		<s:set var="attributeTracer" value="#masterListAttributeTracer.getListElementTracer(#lang, #elementStatus.index)"></s:set>
		<s:set var="i18n_attribute_name">userprofile_ATTR<s:property value="#attribute.name" /></s:set>
		<s:set var="attribute_id">userprofile_<s:property value="#attribute.name" />_<s:property value="#elementStatus.index + 1" /></s:set>
		
		
		<s:set var="elementIndex" value="#elementStatus.index" />
		
			<s:if test="#attribute.type == 'Composite'">
				<li class="contentAttributeBox">
				<p><span class="important"><s:property
					value="#elementStatus.index + 1" />&#32;<s:text
					name="label.compositeAttribute.element" />:</span> <s:if
					test="#lang.default">
					<s:include
						value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_operationModule.jsp" />
				</s:if></p>
			</s:if>
			<s:else> 
				<li>
					<label for="<s:property value="%{attribute_id}" />"><s:property value="#elementStatus.index + 1" /></label>
			</s:else> 
				<s:if test="#attribute.type == 'Monotext'">
					<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
				</s:if>
				<s:elseif test="#attribute.type == 'Text'">
					<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Longtext'"> 
					<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Date'">
				 	<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_dateAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Enumerator'">
					<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_enumeratorAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'EnumeratorMap'">
					<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_enumeratorMapAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Hypertext'">
					<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_hypertextAttribute.jsp" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Composite'">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/compositeAttribute.jsp" />
				</s:elseif>
				&#32;
				<s:if test="#attribute.type != 'Composite'">	
					<s:if test="#lang.default">
						<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/listAttributes/front_allList_operationModule.jsp" />
					</s:if>
				</s:if> 
			</li>
	</s:iterator>

<s:set var="attributeTracer" value="#masterListAttributeTracer" />
<s:set var="attribute" value="#masterListAttribute" />
<s:set var="elementIndex" value="" />
<s:if test="#attribute.getAttributeList(#lang.code).size() != 0">
</ul>
</s:if>

<p><s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/listAttributes/front_allList_addElementButton.jsp" /></p>