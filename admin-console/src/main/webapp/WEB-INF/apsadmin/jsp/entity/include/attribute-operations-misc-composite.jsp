<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<div class="btn-group btn-group-xs">
	<wpsa:actionParam action="moveAttributeElement" var="actionName">
		<wpsa:actionSubParam name="attributeIndex" value="%{#elementIndex}" />
		<wpsa:actionSubParam name="movement" value="UP" />
	</wpsa:actionParam>
	<wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.moveUp')}" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex}" cssClass="btn btn-default">
	<span class="icon fa fa-sort-desc"></span>
	</wpsf:submit>

	<wpsa:actionParam action="moveAttributeElement" var="actionName" >
		<wpsa:actionSubParam name="attributeIndex" value="%{#elementIndex}" />
		<wpsa:actionSubParam name="movement" value="DOWN" />
	</wpsa:actionParam>	
	<wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.moveDown')}" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex+2}" cssClass="btn btn-default">
	<span class="icon fa fa-sort-asc"></span>
	</wpsf:submit>
</div>

<div class="btn-group btn-group-xs">
	<wpsa:actionParam action="removeAttributeElement" var="actionName" >
		<wpsa:actionSubParam name="attributeIndex" value="%{#elementIndex}" />
	</wpsa:actionParam>	
	<wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
	<span class="icon fa fa-times-circle"></span>
	</wpsf:submit>
</div>