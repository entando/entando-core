<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<li>
    <wpsa:actionParam action="moveAttributeElement" var="actionName">
        <wpsa:actionSubParam name="attributeIndex" value="%{#elementIndex}" />
        <wpsa:actionSubParam name="movement" value="UP" />
    </wpsa:actionParam>
    <wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.moveUp')}" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex}" >

    </wpsf:submit>
</li>
<li>
    <wpsa:actionParam action="moveAttributeElement" var="actionName" >
        <wpsa:actionSubParam name="attributeIndex" value="%{#elementIndex}" />
        <wpsa:actionSubParam name="movement" value="DOWN" />
    </wpsa:actionParam>	
    <wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.moveDown')}" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex+2}" >

    </wpsf:submit>
</li>

<li>
    <wpsa:actionParam action="removeAttributeElement" var="actionName" >
        <wpsa:actionSubParam name="attributeIndex" value="%{#elementIndex}" />
    </wpsa:actionParam>	
    <wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.remove')}" title="%{getText('label.remove')}">
    </wpsf:submit>
</li>