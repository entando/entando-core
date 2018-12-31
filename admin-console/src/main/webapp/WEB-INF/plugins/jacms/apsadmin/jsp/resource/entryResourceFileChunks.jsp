<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>


<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.app"/>
    </li>
    <li>
        <s:text name="breadcrumb.jacms"/>
    </li>

    <s:if test="onEditContent">
        <li>
            <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
                <s:text name="breadcrumb.jacms.content.list"/>
            </a>
        </li>
        <li>
            <a href="<s:url action="backToEntryContent" ><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                <s:text name="breadcrumb.jacms.content.edit"/>
            </a>
        </li>
    </s:if>
    <s:else>
        <li>
            <s:text name="breadcrumb.digitalAsset"/>
        </li>
    </s:else>
    <li>
        <s:if test="onEditContent">
            <a href="<s:url action="findResource"><s:param name="resourceTypeCode" value="resourceTypeCode" /><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}"/>
            </a>
        </s:if>
        <s:else>
            <a href="<s:url action="list"><s:param name="resourceTypeCode" value="resourceTypeCode" /></s:url>">
                <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}"/>
            </a>
        </s:else>
    </li>
    <li class="page-title-container">
        <s:if test="getStrutsAction() == 1">
            <s:text name="title.%{resourceTypeCode}.new"/>
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
            <s:text name="title.%{resourceTypeCode}.edit"/>
        </s:elseif>
    </li>
</ol>
<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="title.%{resourceTypeCode}.new"/>
    </s:if>
    <s:elseif test="getStrutsAction() == 2">
        <s:text name="title.%{resourceTypeCode}.edit"/>
    </s:elseif>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-original-title=""
           data-content="<s:property value="%{getText('help.' + resourceTypeCode + '.' + strutsAction + '.info')}" escapeXml="true" />"
           data-placement="left">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>
<i class="fa fa-asterisk required-icon"></i>
<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields"/>
    </div>
</div>
<br/>
<div class="row">
    <div class="col-xs-12">
        <s:form id="uploadForm" enctype="multipart/form-data" action="save"
                cssClass="form-horizontal image-upload-form">
            <s:file name="fileUpload" id="file_input" label="label.file"/>
            <button id="delete_file">Delete File</button>
        </s:form>

    </div>

</div>
