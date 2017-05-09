<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>

<!-- Admin console Breadcrumbs -->
<s:url action="list" namespace="/do/jacms/Content" var="contentListURL"/>
<s:text name="note.goToSomewhere" var="contentListURLTitle"/>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="${contentListURL}" title="${contentListURLTitle}">
            <s:text name="breadcrumb.jacms.content.list" />
        </a>
    </li>
        <s:if test="#thirdTitleVar==null">
            <li class="page-title-container">
                <s:text name="label.edit" />
            </li>
        </s:if>
        <s:else>
        <li>
            <s:url action="backToEntryContent" var="contentEntryURL">
                <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
            </s:url>
            <a href="${contentEntryURL}" title="<s:text name="label.edit" />">
                <s:text name="label.edit" />
            </a>
        </li>
        <li class="page-title-container">
            <s:property value="#thirdTitleVar" escapeXml="false" escapeHtml="false" />
        </li>    
        </s:else>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}" />
<h1 class="page-title-container">
    <s:if test="#thirdTitleVar==null">
        <s:text name="label.edit" />
    </s:if>
    <s:else>
        <s:property value="#thirdTitleVar" escapeXml="false" escapeHtml="false" />
    </s:else>&#32;
    <s:text name="jacms.menu.contentAdmin" />
    <span class="pull-right"> <a tabindex="0" role="button"
        data-toggle="popover" data-trigger="focus" data-html="true" title=""
        data-content="${dataContent}" data-placement="left"
        data-original-title="${dataOriginalTitle}"> <span
            class="fa fa-question-circle-o" aria-hidden="true"></span>
    </a>
    </span>
</h1>

<!-- Default separator -->
<div class="form-group-separator"></div>
