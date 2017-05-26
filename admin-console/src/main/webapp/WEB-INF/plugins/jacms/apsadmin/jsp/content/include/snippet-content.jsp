<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="contentDescriptionVar" value="%{content.descr}" />
<s:set var="attributeFieldErrorsVar" value="%{fieldErrors['descr']}" />
<s:set var="attributeHasFieldErrorVar" value="#attributeFieldErrorsVar != null && !#attributeFieldErrorsVar.isEmpty()" />
<s:set var="controlGroupErrorClassVar" value="' panel-default'" />
<s:set var="inputErrorClassVar" value="''" />
<s:if test="#attributeHasFieldErrorVar">
    <s:set var="controlGroupErrorClassVar" value="' margin-small-bottom panel-danger'" />
    <s:set var="inputErrorClassVar" value="' input-with-feedback'" />
</s:if>

<div class="form-group">
    <label class="col-sm-2 control-label" >
        <s:text name="label.versioning" />
    </label>
    <div class=" col-sm-10">
        <s:if test="!#myNameIsJack">
            <a href="<s:url action="backToEntryContent" ><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>" title="<s:text name="note.content.backToEdit" />" >
            </s:if>
            <s:property value="content.descrDisablingTemporarily" />
            <s:if test="!#myNameIsJack">
            </a>
        </s:if>

        <strong title="<s:text name="name.version" />"> </span><span data-autosave="version"><s:property value="content.version" /></strong>
        <s:text name="name.create" />
        &#32;
        <s:text name="note.firstEditor" />&#32;
        <span data-autosave="firstEditor">
            <s:set var="firstEditorVar" value="content.firstEditor" scope="page" />
            <wp:ifauthorized permission="superuser" var="authorizedSuperUserVar" />
            <c:if test="${!empty firstEditorVar && authorizedSuperUserVar}"><a href="<s:url action="edit" namespace="/do/User"><s:param name="username" value="content.firstEditor"/></s:url>" title="<s:text name="label.edit" />: <s:property value="content.firstEditor" />"></c:if>
                <c:choose>
                    <c:when test="${empty firstEditorVar || (firstEditorVar eq sessionScope.currentUser)}"><s:property value="content.firstEditor" /> <span class="label label-info"><s:text name="label.you" /></span></c:when>
                    <c:otherwise><s:property value="content.firstEditor" /></c:otherwise>
                </c:choose>
                <c:if test="${!empty firstEditorVar && authorizedSuperUserVar}"></a></c:if>
            </span>

            &#32;
        <s:text name="note.lastEditor" />&#32;
        <span data-autosave="lastEditor">
            <s:set var="lastEditorVar" value="content.lastEditor" scope="page" />
            <wp:ifauthorized permission="superuser" var="authorizedSuperUserVar" />
            <c:if test="${!empty lastEditorVar && authorizedSuperUserVar}"><a href="<s:url action="edit" namespace="/do/User"><s:param name="username" value="content.lastEditor"/></s:url>" title="<s:text name="label.edit" />: <s:property value="content.lastEditor" />"></c:if>
                <c:choose>
                    <c:when test="${empty lastEditorVar || (lastEditorVar eq sessionScope.currentUser)}"><s:property value="content.lastEditor" />&nbsp;<span class="label label-info"><s:text name="label.you" /></span></c:when>
                    <c:otherwise><s:property value="content.lastEditor" /></c:otherwise>
                </c:choose>
                <c:if test="${!empty lastEditorVar && authorizedSuperUserVar}"></a></c:if>
            </span>

        <s:if test="content.onLine">
            &#32-&#32;<s:text name="note.lastApprovedIntro" />&#32;<a href="<s:url action="inspect" namespace="/do/jacms/Content" ><s:param name="contentId" value="content.id" /><s:param name="currentPublicVersion" value="'true'" /></s:url>"><s:text name="note.lastApproved" /></a>
        </s:if>
    </div>
</div>


<div class="form-group">
    <label class="col-sm-2 control-label" for="contentType"><s:text name="label.contentType"/></label>
    <div class="col-sm-10">
        <input type="text" id="contentType" readonly="readonly" class="form-control" value="${content.typeDescr}"/>
    </div>
</div>
<div class="form-group  ${(attributeHasFieldErrorVar)?'has-error':''}">
    <label class="col-sm-2 control-label" for="contentType">
        <s:text name="label.contentDescription"/>
        <a tabindex="0" role="button" data-toggle="popover"
           data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="note.description.provide" />&#32;<s:text name="note.description.usedefault" />."
           data-placement="top"><span class="fa fa-info-circle"></span>
        </a>
    </label>
    <div class="col-sm-10">
        <input class="form-control" type="text" name="descr"
               value="${content.descr}"
               placeholder="<s:text name="note.description.placeholder" />" id="contentDescription"/>
        <s:if test="#attributeHasFieldErrorVar">
            <p class="text-danger no-mb"><s:iterator value="#attributeFieldErrorsVar"><s:property /> </s:iterator></p>
        </s:if>
    </div>
</div>
