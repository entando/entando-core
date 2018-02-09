<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="null == #operationButtonDisabled">
    <s:set var="operationButtonDisabled" value="false" />
</s:if>
<div class="btn-toolbar">
    <div class="btn-group btn-group-sm">
        <wpsa:actionParam action="moveListElement" var="actionName" >
            <wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
            <wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
            <wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
            <wpsa:actionSubParam name="movement" value="UP" />
        </wpsa:actionParam>
        <wpsf:submit disabled="%{#operationButtonDisabled}" action="%{#actionName}" type="button" cssClass="btn btn-default" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex}">
            <span class="icon fa fa-sort-asc"></span>
            <span class="sr-only"><s:text name="label.moveInPositionNumber" />: <s:property value="%{#elementIndex}" /></span>
        </wpsf:submit>

        <wpsa:actionParam action="moveListElement" var="actionName" >
            <wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
            <wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
            <wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
            <wpsa:actionSubParam name="movement" value="DOWN" />
        </wpsa:actionParam>
        <wpsf:submit disabled="%{#operationButtonDisabled}" action="%{#actionName}" type="button" cssClass="btn btn-default" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex+2}">
            <span class="icon fa fa-sort-desc"></span>
            <span class="sr-only"><s:text name="label.moveInPositionNumber" />: <s:property value="%{#elementIndex}" /></span>
        </wpsf:submit>
    </div>
    <div class="btn-group btn-group-sm">
        <wpsa:actionParam action="removeListElement" var="actionName" >
            <wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
            <wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
            <wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
        </wpsa:actionParam>
        <wpsf:submit disabled="%{#operationButtonDisabled}" action="%{#actionName}" type="button" cssClass="btn btn-danger" title="%{getText('label.remove')}: %{#elementIndex}">

            <s:text name="label.remove" />
        </wpsf:submit>
    </div>
</div>
