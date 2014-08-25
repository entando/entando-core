<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>&#32;/&#32;
		<s:text name="title.databaseManagement" />
	</span>
</h1>
<s:if test="hasActionErrors()">
	<div class="alert alert-danger alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
		<ul class="margin-base-top">
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
		<ul class="margin-base-top">
			<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
		</ul>
	</div>
</s:if>
<s:if test="hasActionMessages()">
	<div class="alert alert-info alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
		<ul class="margin-base-top">
			<s:iterator value="actionMessages">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<s:if test="managerStatus != 0" >
	<div class="alert alert-info">
			<s:text name="database.management.note.dump.in.progress" /> ( <a href="<s:url namespace="/do/Admin/Database" action="entry" />" class="alert-link"><s:text name="database.management.refresh" /></a> )
	</div>
</s:if>
<s:else>
		<s:set var="dumpReportsVar" value="dumpReports" />
		<s:if test="null == #dumpReportsVar || #dumpReportsVar.isEmpty()">
			<p class="alert alert-info">
				<s:text name="database.management.note.no.backups" />
			</p>
		</s:if>
		<s:else>
			<div class="table-responsive">
				<table class="table table-bordered table-hover table-condensed table-striped">
					<caption><span><s:text name="database.management.label.backup.list" /></span></caption>
					<tr>
						<th class="text-center"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
						<th class="text-right"><abbr title="<s:text name="database.management.label.number" />"><s:text name="database.management.label.number.short" /></abbr></th>
						<th class="text-center"><s:text name="database.management.label.date" /></th>
						<th class="text-right"><s:text name="database.management.label.time.required" /></th>
					<tr>
					<s:iterator var="dumpReportVar" value="#dumpReportsVar" status="status">
						<tr>
							<td class="text-center">
								<a
									title="<s:text name="database.management.label.remove" />:&#32;<s:property value="#status.count" />&#32;&ndash;&#32;<s:date name="#dumpReportVar.date" format="dd/MM/yyyy HH:mm:ss" />"
									href="<s:url namespace="/do/Admin/Database" action="trashBackup" >
									   <s:param name="subFolderName" value="#dumpReportVar.subFolderName" />
								   </s:url>" class="btn btn-warning btn-xs">
								   <span class="icon fa fa-times-circle-o"></span>
								  </a>
							</td>
							<td class="text-nowrap text-right"><s:property value="#status.count" /></td>
							<td class="text-nowrap text-center">
								<a
									title="<s:text name="database.management.label.details" />:&#32;<s:property value="#status.count" />&#32;&ndash;&#32;<s:date name="#dumpReportVar.date" format="dd/MM/yyyy HH:mm:ss" />"
									href="<s:url namespace="/do/Admin/Database" action="entryBackupDetails" >
								   <s:param name="subFolderName" value="#dumpReportVar.subFolderName" />
							   		</s:url>"><code><s:date name="#dumpReportVar.date" format="dd/MM/yyyy HH:mm:ss" /></code></a>
							</td>
							<td class="text-nowrap text-right">
								<code><s:property value="#dumpReportVar.requiredTime" /></code>&#32;<s:text name="database.management.label.milliseconds" />
							</td>
						</tr>
					</s:iterator>
				</table>
			</div>
		</s:else>
		<form action="<s:url namespace="/do/Admin/Database" action="backupIntro" />" method="get">
			<p class="text-center">
				<button type="submit" class="btn btn-primary">
					<s:text name="database.management.label.backup.create" />
				</button>
			</p>
		</form>
</s:else>