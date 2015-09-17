<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="#attribute.type == 'Boolean'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />	
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_booleanAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />	
		</div>
	</div>
</s:if>
<s:elseif test="#attribute.type == 'CheckBox'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_checkBoxAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:elseif>	
<s:elseif test="#attribute.type == 'Composite'">
	<div class="well well-small">
		<fieldset class=" <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
			<legend class="margin-medium-top"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" /></legend>
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_compositeAttribute.jsp" />
		</fieldset>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Date'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_dateAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Enumerator'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_enumeratorAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'EnumeratorMap'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_enumeratorMapAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Hypertext'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_hypertextAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'List'">
	<div class="well well-small">
		<fieldset class=" <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
			<legend class="margin-medium-top"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" /></legend>
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monolistAttribute.jsp" />
		</fieldset>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Longtext'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_hypertextAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Monolist'">
	<div class="well well-small">
		<fieldset class=" <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
			<legend class="margin-medium-top"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" /></legend>
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monolistAttribute.jsp" />
		</fieldset>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Monotext'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />	
		</div>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Number'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_numberAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Text'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:elseif>
<s:elseif test="#attribute.type == 'ThreeState'">
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_threeStateAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:elseif>
<s:else><%-- for all other types, insert a simple label and a input[type="text"] --%>
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" />">
		<label class="control-label" for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo.jsp" /></label>
		<div class="controls">
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_monotextAttribute.jsp" />
			<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/attributes/front_attributeInfo-help-block.jsp" />
		</div>
	</div>
</s:else>