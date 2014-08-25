<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%-- choose resource button --%>
<s:set var="resourceTypeCode"><%= request.getParameter("resourceTypeCode")%></s:set>
<s:set var="buttonCssClass"><%= request.getParameter("buttonCssClass")%></s:set>
<s:if test="#buttonCssClass==null||#buttonCssClass=='null'">
	<s:set var="buttonCssClass">btn btn-default</s:set>
</s:if>
<wpsa:actionParam action="chooseResource" var="chooseResourceActionName" >
	<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="resourceTypeCode" value="%{#resourceTypeCode}" />
	<wpsa:actionSubParam name="resourceLangCode" value="%{#lang.code}" />
</wpsa:actionParam>
<wpsf:submit action="%{#chooseResourceActionName}" type="button" title="%{#attribute.name + ': ' + getText('label.choose')}" cssClass="%{#buttonCssClass}">
	<s:if test="#resourceTypeCode == 'Image'">
			<span class="icon fa fa-picture-o"></span>&#32;
			<s:text name="label.chooseImage" />
	</s:if>
	<s:else>
			<span class="icon fa fa-paperclip"></span>&#32;
			<s:text name="label.chooseAttach" />
	</s:else>
</wpsf:submit>
