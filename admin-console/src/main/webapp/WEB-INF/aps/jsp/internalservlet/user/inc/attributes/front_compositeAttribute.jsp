<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="i18n_parent_attribute_name" value="#attribute.name" />
<s:set name="masterCompositeAttributeTracer" value="#attributeTracer" />
<s:set name="masterCompositeAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" var="attribute">
	<s:set name="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)"></s:set>
	<s:set name="parentAttribute" value="#masterCompositeAttribute"></s:set>
	<s:set var="i18n_attribute_name">userprofile_ATTR<s:property value="%{i18n_parent_attribute_name}" /><s:property value="#attribute.name" /></s:set>
	<s:set var="attribute_id">userprofile_<s:property value="%{i18n_parent_attribute_name}" /><s:property value="#attribute.name" />_<s:property value="#elementIndex" /></s:set>
	
	<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/iteratorAttribute.jsp" />

</s:iterator>
<s:set name="attributeTracer" value="#masterCompositeAttributeTracer" />
<s:set name="attribute" value="#masterCompositeAttribute" />
<s:set name="parentAttribute" value=""></s:set>