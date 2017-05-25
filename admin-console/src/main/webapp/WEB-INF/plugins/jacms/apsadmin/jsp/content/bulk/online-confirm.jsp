<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="labelTitle" value="%{getText('title.bulk.content.online')}"/>

<!-- Admin console Breadcrumbs -->
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
            <s:text name="breadcrumb.jacms.content.list" />
        </a>
    </li>
    <li class="page-title-container"><s:property value="%{#labelTitle}" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}"/>
<h1 class="page-title-container">
    <s:property value="%{#labelTitle}" />&#32;&ndash;&#32;<s:text name="title.bulk.confirm" />

</h1>

<!-- Default separator -->
<div class="form-group-separator"></div>

<div id="main" role="main">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/inc_fullErrors.jsp" />

    <s:form action="applyOnline" namespace="/do/jacms/Content/Bulk" >
        <p class="sr-only">
            <s:iterator var="contentId" value="selectedIds" >
                <wpsf:hidden name="selectedIds" value="%{#contentId}" />
            </s:iterator>
        </p>

        <div class="blank-slate-pf mt-20">
            <h2>
                <s:property value="#labelTitle"/>
            </h2>
            <p>
                <s:text name="note.bulk.content.offline.doYouConfirm" ><s:param name="items" value="%{selectedIds.size()}" /></s:text>
                </p>

                <div class="blank-slate-pf-main-action">
                <s:set var="labelAction" value="%{getText('label.bulk.content.online.confirm')}"/>
                <wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-primary btn-lg">
                    <s:property value="%{#labelAction}" />
                </wpsf:submit>
            </div>
        </div>

        <fieldset class="col-xs-12 no-padding">
            <legend><s:text name="label.bulk.report.summary" /></legend>
            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/bulk/inc/inc_contentSummary.jsp" />
        </fieldset>
    </s:form>
</div>
