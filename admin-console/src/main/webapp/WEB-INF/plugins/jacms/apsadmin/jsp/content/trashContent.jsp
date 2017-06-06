<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
            <s:text name="jacms.menu.contentAdmin" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.contentManagement.trashContent" />
    </li>
</h1>
<h1 class="page-title-container">
    <s:text name="name.widget" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<s:form action="deleteContentGroup" cssClass="form-horizontal">

    <p class="sr-only">
        <s:iterator var="contentIdToDelete" value="contentIds"><wpsf:hidden name="contentIds" value="%{#contentIdToDelete}" /></s:iterator>
        <wpsf:hidden name="text" />
        <wpsf:hidden name="contentType" />
        <wpsf:hidden name="state" />
        <wpsf:hidden name="onLineState" />
        <wpsf:hidden name="categoryCode" />
        <wpsf:hidden name="viewTypeDescr" />
        <wpsf:hidden name="viewGroup" />
        <wpsf:hidden name="viewCode" />
        <wpsf:hidden name="viewStatus" />
        <wpsf:hidden name="viewCreationDate" />
        <wpsf:hidden name="lastGroupBy" />
        <wpsf:hidden name="lastOrder" />
        <wpsf:hidden name="contentIdToken" />
        <wpsf:hidden name="ownerGroupName" />
        <s:set var="searchableAttributes" value="searchableAttributes" ></s:set>
        <s:if test="null != #searchableAttributes && #searchableAttributes.size() > 0">
            <s:iterator var="attribute" value="#searchableAttributes">
                <s:if test="#attribute.textAttribute">
                    <s:set var="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
                    <wpsf:hidden name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
                </s:if>
                <s:elseif test="#attribute.type == 'Date'">
                    <s:set var="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
                    <s:set var="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
                    <wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
                    <wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
                </s:elseif>
                <s:elseif test="#attribute.type == 'Number'">
                    <s:set var="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
                    <s:set var="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
                    <wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
                    <wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
                </s:elseif>
                <s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
                    <s:set var="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
                    <wpsf:hidden name="%{#booleanInputFieldName}" value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
                </s:elseif>
            </s:iterator>
        </s:if>
    </p>

    <div class="alert alert-warning">
        <p>
            <s:text name="note.trashContent.areYouSure" />
        </p>
        <ul class="margin-small-vertical">
            <s:iterator var="contentIdToDelete" value="contentIds">
                <s:set var="content" value="%{getContentVo(#contentIdToDelete)}">
                </s:set>
                <li>
                    <span>
                        <s:property value="#contentIdToDelete" />
                    </span> &ndash;
                    <s:property value="#content.descr" /> (<s:property value="%{getSmallContentType(#content.typeCode).descr}" />)
                </li>
            </s:iterator>
        </ul>
        <div class="text-center">
            <a class="btn btn-primary" href="<s:url action="list" namespace="/do/jacms/Content"/>" >
                <s:text name="title.contentList" />
            </a>
            <wpsf:submit type="button" action="deleteContentGroup" cssClass="btn btn-warning">
                <s:text name="label.remove" />
            </wpsf:submit>
        </div>
    </div>
</s:form>
