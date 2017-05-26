<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<wpsa:actionParam action="addListElement" var="actionName" >
    <wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
    <wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
</wpsa:actionParam>

<wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-primary" >&#32;<s:text name="label.add" /></wpsf:submit>
