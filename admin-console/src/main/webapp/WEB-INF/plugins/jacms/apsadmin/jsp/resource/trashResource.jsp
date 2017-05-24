<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="categoryTreeStyleVar"><wp:info key="systemParam" paramName="treeStyle_category"/></s:set>
<s:set var="targetNS" value="%{'/do/jacms/Resource'}"/>
<s:set var="targetParamName" value="%{'resourceTypeCode'}"/>
<s:set var="targetParamValue" value="resourceTypeCode"/>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app"/></li>
    <li><s:text name="breadcrumb.jacms"/></li>
    <li>
        <s:property value="%{getText('title.' + resourceTypeCode + 'Management')}"/>
    </li>
    <li class="page-title-container">
        <s:text name="title.resourceManagement.resourceTrash"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.resourceManagement.resourceTrash"/>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form action="delete">

        <p class="sr-only">
            <wpsf:hidden name="resourceId"/>
            <wpsf:hidden name="text" value="%{#parameters['text']}"/>
            <wpsf:hidden name="categoryCode" value="%{#parameters['categoryCode']}"/>
            <wpsf:hidden name="resourceTypeCode"/>
            <wpsf:hidden name="fileName" value="%{#parameters['fileName']}"/>
            <wpsf:hidden name="ownerGroupName" value="%{#parameters['ownerGroupName']}"/>
            <s:if test="#categoryTreeStyleVar == 'request'">
                <s:iterator value="%{#parameters['treeNodesToOpen']}" var="treeNodeToOpenVar"><wpsf:hidden
                        name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/></s:iterator>
            </s:if>
        </p>

        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="note.deleteResource.areYouSure"/></p>
        <p class="esclamation-underline-text"><s:property value="%{loadResource(resourceId).descr}"/>&#63;</p>

        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width" href="<s:url action="list" namespace="/do/jacms/Resource">
                   <s:param name="resourceTypeCode">
                       <s:property value="resourceTypeCode" />
                   </s:param>
               </s:url>">
                <s:text name="label.back"/>
            </a>

            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete"/>
            </wpsf:submit>
        </div>
    </s:form>
</div>
