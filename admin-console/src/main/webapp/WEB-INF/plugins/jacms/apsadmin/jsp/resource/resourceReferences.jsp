<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<s:set var="targetNS" value="%{'/do/jacms/Resource'}" />
<s:set var="targetParamName" value="%{'resourceTypeCode'}" />
<s:set var="targetParamValue" value="resourceTypeCode" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app"/></li>
    <li><s:text name="breadcrumb.jacms"/></li>
    <li><s:text name="breadcrumb.digitalAsset" /></li>
    <li>
        <a href="<s:url action="list" ><s:param name="resourceTypeCode" ><s:text name="%{resourceTypeCode}"></s:text></s:param></s:url>">
            <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.resourceManagement.resourceTrash"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.resourceManagement.resourceTrash"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="TO be inserted" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">

    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.ActionErrors" /></strong>
        <p><s:text name="message.note.resolveReferences" /></p>
    </div>

    <s:form>
        <p class="sr-only">
        <wpsf:hidden name="resourceId" />
    </p>

    <s:if test="references['jacmsContentManagerUtilizers']">
        <s:set var="referencingContentsId" value="references['jacmsContentManagerUtilizers']" />
        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/portal/include/referencingContents.jsp" />
    </s:if>

    <wpsa:hookPoint key="jacms.resourceReferences" objectName="hookPointElements_jacms_resourceReferences">
        <s:iterator value="#hookPointElements_jacms_resourceReferences" var="hookPointElement">
            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
        </s:iterator>
    </wpsa:hookPoint>

</s:form>

</div>
