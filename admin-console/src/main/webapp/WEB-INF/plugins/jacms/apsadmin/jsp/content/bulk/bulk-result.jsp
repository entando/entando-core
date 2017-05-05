<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:set var="command" value="command" />

<s:if test="%{#command == null}" >
	<p>
		<s:text name="note.bulk.report.notFound" />
	</p>
</s:if>
<s:else>
	<s:set var="labelTitle" value="%{getText('title.bulk.' + #command.name)}"/>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url action="list" namespace="/do/jacms/Content"/>"><s:text name="jacms.menu.contentAdmin" /></a></li>
    <li>
		<s:property value="%{#labelTitle}" />
    </li>
</ol>

<h1 class="page-title-container"><s:property value="%{#labelTitle}" />&#32;-&#32;<s:text name="title.bulk.result" /></h1>

<div id="main" role="main">
	<s:set var="report" value="#command.report" />
	<p>
		<s:text name="label.bulk.status" />: <s:text name="name.bulk.status.%{#command.status}" />
	</p>
	<s:if test="%{#report.endingTime != null}" >
	<p>
		<s:text name="label.bulk.report.endingTime" />: <s:date name="#report.endingTime" format="dd/MM/yyyy HH:mm:ss"/>
	</p>
	</s:if>
	<p>
		<s:text name="label.bulk.report.total" />: 
		<s:text name="label.bulk.report.total.count" >
			<s:param name="applyTotal" value="%{#report.applyTotal}" />
			<s:param name="total" value="%{#report.total}" />
		</s:text>
	</p>
	<p>
		<s:text name="label.bulk.report.successCount" />: <s:property value="%{#report.applySuccesses}" />
	</p>
	<p>
		<s:text name="label.bulk.report.errorCount" />: <s:property value="%{#report.applyErrors}" />
	</p>
	<s:if test="%{#report.applyErrors > 0}" >
	<div class="alert alert-danger">
		<span class="pficon pficon-error-circle-o"></span>
		<s:text name="message.title.ActionErrors" />
		<ul>
		<s:iterator var="error" value="%{#report.errors}" >
			<jacmswpsa:content contentId="${error.key}" var="content" workVersion="true" />
			<s:text name="error.bulk.%{#command.name}.%{#error.value}" var="errorDescr" />
			<li>
				<s:text name="label.bulk.report.error" >
					<s:param name="content" value="%{#content.description}" />
					<s:param name="error" value="%{#errorDescr}" />
				</s:text>
			</li>
		</s:iterator>
		</ul>
	</div>
	</s:if>
<s:if test="%{#report.endingTime == null}" >
	<s:form action="viewResult" namespace="/do/jacms/Content/Bulk" method="get" >
		<wpsf:hidden name="commandId"/>
		<s:set var="labelAction" value="%{getText('label.bulk.report.refresh')}"/>
		<wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-success">
			<span class="icon fa fa-times-circle"></span>
			<s:property value="%{#labelAction}" />
		</wpsf:submit>
	</s:form>
</s:if>
</div>
</s:else>