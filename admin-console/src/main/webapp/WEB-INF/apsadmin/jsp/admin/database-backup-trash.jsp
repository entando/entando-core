<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure" /></li>
	<li><s:text name="title.databaseManagement" /></li>
	<li class="page-title-container"><s:text
			name="title.databaseBackup.remove" /></li>
</ol>
<h1 class="page-title-container">
	<s:text name="title.databaseBackup.remove" />
	<span class="pull-right"> <a tabindex="0" role="button"
		data-toggle="popover" data-trigger="focus" data-html="true"
		title="title.databaseManagement" data-content="TO be inserted"
		data-placement="left" data-original-title=""><i
			class="fa fa-question-circle-o" aria-hidden="true"></i></a>
	</span>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>
<s:set var="subFolderNameVar" value="subFolderName" />
<s:set var="dumpReportVar" value="getDumpReport(#subFolderNameVar)" />
<div class="text-center">
	<s:form action="deleteBackup">
		<p class="sr-only">
			<wpsf:hidden name="subFolderName" />
		</p>

		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline">
			<s:text name="title.databaseBackup.remove" />
		</p>
		<p>
			<s:text name="database.management.label.remove.areyousure" />
			&#32;
			<code>
				<s:date name="#dumpReportVar.date" format="dd/MM/yyyy HH:mm:ss" />
			</code>
			?
		</p>
		<br>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button"
				cssClass="btn btn-danger button-fixed-width">
				<s:text name="label.confirm" />
			</wpsf:submit>
		</div>
		<div class="text-center margin-large-top">
			<a class="btn btn-default button-fixed-width"
				href="<s:url namespace="/do/Admin/Database" action="entry" />">
				<s:text name="note.goToSomewhere" />&#32;<s:text
					name="title.databaseManagement" />
			</a>
		</div>
	</s:form>
</div>