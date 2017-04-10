<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="labelTitle" value="%{getText('title.bulk.content.online')}"/>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url action="list" namespace="/do/jacms/Content"/>"><s:text name="jacms.menu.contentAdmin" /></a></li>
    <li>
		<s:property value="%{#labelTitle}" />
    </li>
</ol>

<h1 class="page-title-container"><s:property value="%{#labelTitle}" />&#32;-&#32;<s:text name="title.bulk.confirm" /></h1>

<div id="main" role="main">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/inc_fullErrors.jsp" />
	
	<s:form action="applyOnline" namespace="/do/jacms/Content/Bulk" >
		<p class="sr-only">
		<s:iterator var="contentId" value="selectedIds" >
			<wpsf:hidden name="selectedIds" value="%{#contentId}" />
		</s:iterator>
		</p>
		<div>
			<p>
				<s:text name="note.bulk.content.online.doYouConfirm" ><s:param name="items" value="%{selectedIds.size()}" /></s:text>
			</p>
		</div>
		<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/bulk/inc/inc_contentSummary.jsp" />
		
		<div class="col-xs-12">
			<s:set var="labelAction" value="%{getText('label.bulk.content.online.confirm')}"/>
			<wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-success">
				<span class="icon fa fa-times-circle"></span>
				<s:property value="%{#labelAction}" />
			</wpsf:submit>
		</div>
	</s:form>
</div>