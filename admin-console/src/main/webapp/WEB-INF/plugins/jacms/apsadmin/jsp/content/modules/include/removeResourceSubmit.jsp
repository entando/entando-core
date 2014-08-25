<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%-- remove button --%>
<s:set name="resourceTypeCode"><%= request.getParameter("resourceTypeCode")%></s:set>
<wpsa:actionParam action="removeResource" var="removeResourceActionName" >
	<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="resourceTypeCode" value="%{#resourceTypeCode}" />
	<wpsa:actionSubParam name="resourceLangCode" value="%{#lang.code}" />
</wpsa:actionParam>
<wpsf:submit action="%{#removeResourceActionName}" type="button" title="%{#attribute.name + ': ' + getText('label.clear')}" cssClass="btn btn-warning btn-xs">
	<span class="icon fa fa-eraser"></span>&#32;
	<s:if test="#resourceTypeCode == 'Image'">
		<s:text name="label.clearImage" />
	</s:if>
	<s:else>
		<s:text name="label.clearAttach" />
	</s:else>
</wpsf:submit>