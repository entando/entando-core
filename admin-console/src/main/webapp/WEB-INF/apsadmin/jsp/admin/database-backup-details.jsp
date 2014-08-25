<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/Admin/Database" action="entry" />"><s:text name="title.databaseManagement" /></a>
		&#32;/&#32;
		<s:text name="title.databaseBackup.details" />
	</span>
</h1>
<s:set var="subFolderNameVar" value="subFolderName" />
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
	<s:set var="dumpReportVar" value="getDumpReport(#subFolderNameVar)" />
	<s:if test="null == #dumpReportVar">
		<div class="alert alert-warning">
			<s:text name="database.management.note.dump.not.available" />
		</div>
	</s:if>
	<s:else>
		<div class="well panel">
			<dl class="margin-none dl-horizontal">
				<dt><s:text name="database.management.label.date" /></dt>
					<dd><code><s:date name="#dumpReportVar.date" format="EEEE dd/MMM/yyyy, HH:mm:ss" /></code></dd>
				<dt><s:text name="database.management.label.time.required" /></dt>
					<dd>
						<code><s:property value="#dumpReportVar.requiredTime" />&#32;<s:text name="database.management.label.milliseconds" />
						</code>
					</dd>
			</dl>
		</div>

		<div class="table-responsive">
			<table class="table table-bordered table-hover table-condensed table-striped">
				<tr>
					<th>Component Name</th>
					<th class="text-center">Dump Date</th>
				</tr>
				<s:iterator var="componentHistoryVar" value="#dumpReportVar.componentsHistory">
					<tr>
						<td>
							<s:set var="labelComponentDescrVar" value="%{#componentHistoryVar.componentCode + '.name'}" />
							<s:text name="%{#labelComponentDescrVar}" var="componentDescrVar" />
							<s:if test="%{#componentDescrVar.equals(#labelComponentDescrVar)}"><s:property value="#componentHistoryVar.componentCode" /></s:if>
							<s:else><s:property value="#componentDescrVar" /></s:else>
						</td>
						<td class="text-center">
							<code><s:date name="#componentHistoryVar.date" format="dd/MM/yyyy HH:mm:ss" /></code>
						</td>
					</tr>
				</s:iterator>
			</table>
		</div>

		<p class="help-block text-right">
				<button type="button" data-toggle="collapse" data-target="#datasource-details" class="btn btn-link">
					<s:text name="DataSource Details"/> <span class="icon fa fa-chevron-down"></span>
				</button>
		</p>
		<div id="datasource-details" class="collapse">
			<div class="table-responsive">
					<s:iterator var="dataSourceNameVar" value="#dumpReportVar.dataSourceNames" begin="0" end="%{(#dumpReportVar.dataSourceNames.size()/2)-1}" status="status">
						<table class="table table-bordered table-hover table-condensed">
							<caption>
								<span>
									<span lang="en">Datasource</span>&#32;<em><s:property value="#dataSourceNameVar" /></em>
								</span>
							</caption>
							<s:set var="tableReportsVar" value="#dumpReportVar.dataSourcesReports[#dataSourceNameVar]" />
							<tr>
								<th><s:text name="database.management.label.table" /></th>
								<th><s:text name="database.management.label.rows" /></th>
								<th><s:text name="database.management.label.time.required" /></th>
							</tr>
							<s:iterator var="tableReportVar" value="#tableReportsVar">
								<tr>
									<td>
										<s:if test="#tableReportVar.rows == 0"><s:property value="#tableReportVar.tableName" /></s:if>
										<s:else>
												<a
													title="<s:text name="database.management.label.download" />: <s:property value="#dataSourceNameVar" />/<s:property value="#tableReportVar.tableName" />"
													href="<s:url namespace="/do/Admin/Database" action="extractTableDump" >
													<s:param name="tableName" value="#tableReportVar.tableName" />
													<s:param name="dataSourceName" value="#dataSourceNameVar" />
													<s:param name="subFolderName" value="#dumpReportVar.subFolderName" />
												</s:url>"><span class="icon fa fa-arrow-circle-o-down"></span>&#32;<s:property value="#tableReportVar.tableName" /></a>
										</s:else>
									</td>
									<td class="text-right text-nowrap">
										<code><s:property value="#tableReportVar.rows" /></code>
									</td>
									<td class="text-right text-nowrap">
										<code><s:property value="#tableReportVar.requiredTime" /></code>&#32;<abbr title="<s:text name="database.management.label.milliseconds" />"><s:text name="database.management.label.milliseconds.short" /></abbr>
									</td>
								</tr>
							</s:iterator>
						</table>
					</s:iterator>
			</div>
			<s:iterator var="dataSourceNameVar" value="#dumpReportVar.dataSourceNames" begin="%{(#dumpReportVar.dataSourceNames.size()/2)}" status="status">
				<div class="table-responsive">
					<table class="table table-bordered table-hover table-condensed">
						<caption>
							<span>
								<span lang="en">Datasource</span>&#32;<em><s:property value="#dataSourceNameVar" /></em>
							</span>
						</caption>
						<s:set var="tableReportsVar" value="#dumpReportVar.dataSourcesReports[#dataSourceNameVar]" />
						<tr>
							<th><s:text name="database.management.label.table" /></th>
							<th><s:text name="database.management.label.rows" /></th>
							<th><s:text name="database.management.label.time.required" /></th>
						</tr>
						<s:iterator var="tableReportVar" value="#tableReportsVar">
							<tr>
								<td>
									<s:if test="#tableReportVar.rows == 0"><s:property value="#tableReportVar.tableName" /></s:if>
									<s:else>
											<a
												title="<s:text name="database.management.label.download" />: <s:property value="#dataSourceNameVar" />/<s:property value="#tableReportVar.tableName" />"
												href="<s:url namespace="/do/Admin/Database" action="extractTableDump" >
												<s:param name="tableName" value="#tableReportVar.tableName" />
												<s:param name="dataSourceName" value="#dataSourceNameVar" />
												<s:param name="subFolderName" value="#dumpReportVar.subFolderName" />
											</s:url>"><span class="icon fa fa-arrow-circle-o-down"></span>&#32;<s:property value="#tableReportVar.tableName" /></a>
									</s:else>
								</td>
								<td class="text-right text-nowrap">
									<s:property value="#tableReportVar.rows" />
								</td>
								<td class="text-right text-nowrap">
									<s:property value="#tableReportVar.requiredTime" />&#32;<abbr title="<s:text name="database.management.label.milliseconds" />"><s:text name="database.management.label.milliseconds.short" /></abbr>
								</td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</s:iterator>
		</div>
		<hr />
		<s:form namespace="/do/Admin/Database" method="get" cssClass="margin-more-top">
			<p class="text-center">
				<wpsf:hidden name="subFolderName" value="%{#dumpReportVar.subFolderName}" />
				<wpsf:submit type="button" action="restoreIntro" cssClass="btn btn-primary">
					<s:text name="database.management.label.restore" />
				</wpsf:submit>
			</p>
		</s:form>
	</s:else>
</s:else>