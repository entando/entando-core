<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:if test="#attribute.attributes.size() != 0">
	<ul class="unstyled">
</s:if>

<s:set name="masterListAttributeTracer" value="#attributeTracer" />
<s:set name="masterListAttribute" value="#attribute" />	

<s:iterator value="#attribute.attributes" var="attribute" status="elementStatus">
	<s:set name="attributeTracer" value="#masterListAttributeTracer.getMonoListElementTracer(#elementStatus.index)"></s:set>
	<s:set name="elementIndex" value="#elementStatus.index" />
	<s:set var="i18n_attribute_name">userprofile_ATTR<s:property value="#attribute.name" /></s:set>
	<s:set var="attribute_id">userprofile_<s:property value="#attribute.name" />_<s:property value="#elementStatus.count" /></s:set>

	<li class="control-group  <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<s:if test="#attribute.type == 'Composite'">
				<s:property value="#elementStatus.count" /><span class="noscreen">&#32;<s:text name="label.compositeAttribute.element" /></span>
				&#32;
				<s:if test="#lang.default">
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_operationModule.jsp" />
				</s:if>
			</s:if>
			<s:else>
				<s:property value="#elementStatus.count" />
				&#32;
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_operationModule.jsp" />
			</s:else>
		</label>
		<div class="controls">
			<s:if test="#attribute.type == 'Boolean'">
				<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_booleanAttribute.jsp" />
			</s:if>
			<s:elseif test="#attribute.type == 'CheckBox'">
				<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_checkBoxAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Composite'">
				<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_compositeAttribute.jsp" />
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
			<s:elseif test="#attribute.type == 'Longtext'">
				<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_hypertextAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Monotext'">
				<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Number'">
				<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_numberAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'ThreeState'">
				<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_threeStateAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Text'">
				<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
			</s:elseif>
			<s:else>
				else?<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
			</s:else>
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</li>
<%--
		<li class="contentAttributeBox">
		<p>
			<span class="important">
				<s:property value="#elementStatus.count" />&#32;<s:text name="label.compositeAttribute.element" />
			</span>
			<s:if test="#lang.default">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_operationModule.jsp" />
			</s:if>
		</p>
	</s:if>
	<s:else>
		<li>
			<label for="<s:property value="#attribute_id" />"><s:property value="#elementStatus.index + 1" /></label>
	</s:else>
		
		<s:if test="#attribute.type == 'Boolean'">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_booleanAttribute.jsp" />
		</s:if>

		<s:elseif test="#attribute.type == 'Monotext'">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Text'">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Longtext'"> 
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Date'">
		 	<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_dateAttribute.jsp" />
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

	--%>
</s:iterator>

<s:set name="attributeTracer" value="#masterListAttributeTracer" />
<s:set name="attribute" value="#masterListAttribute" />
<s:set name="elementIndex" value="" />
<s:if test="#attribute.attributes.size() != 0">
</ul>
</s:if>

<s:if test="#lang.default">
	<div class="control-group">
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/listAttributes/front_allList_addElementButton.jsp" />
		</div>
	</div>

</s:if>