<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/Admin/Database" action="entry" />"><s:text name="title.databaseManagement" /></a>
		&#32;/&#32;
		<s:text name="title.databaseBackup.remove" />
	</span>
</h1>
<s:set var="subFolderNameVar" value="subFolderName" />
<s:set var="dumpReportVar" value="getDumpReport(#subFolderNameVar)" />
<s:form action="deleteBackup">
	<p class="sr-only">
		<wpsf:hidden name="subFolderName" />
	</p>
	<div class="alert alert-warning">
		<p>
			<s:text name="database.management.label.remove.areyousure" />&#32;
			<code><s:date name="#dumpReportVar.date" format="dd/MM/yyyy HH:mm:ss" /></code>?
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
				<span class="icon fa fa-times-circle"></span>&#32;
				<s:text name="label.confirm" />
			</wpsf:submit>
			<a class="btn btn-link"	href="<s:url namespace="/do/Admin/Database" action="entry" />">
			<s:text name="note.goToSomewhere" />&#32;<s:text name="title.databaseManagement" /></a>
		</div>
	</div>
</s:form>