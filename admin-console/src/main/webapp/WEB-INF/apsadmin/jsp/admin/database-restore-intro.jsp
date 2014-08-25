<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="subFolderNameVar" value="subFolderName" />
<s:set var="dumpReportVar" value="getDumpReport(#subFolderNameVar)" />
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/Admin/Database" action="entry" />"><s:text name="title.databaseManagement" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/Admin/Database" action="entryBackupDetails" ><s:param name="subFolderName" value="#dumpReportVar.subFolderName" /></s:url>"><s:text name="title.databaseBackup.details" /></a>
		&#32;/&#32;
		<s:text name="title.databaseBackup.restore" />
	</span>
</h1>
<s:if test="managerStatus != 0" >
	<div class="alert alert-info">
			<s:text name="database.management.note.dump.in.progress" /> ( <a href="<s:url namespace="/do/Admin/Database" action="entry" />" class="alert-link"><s:text name="database.management.refresh" /></a> )
	</div>
</s:if>
<s:else>
	<div class="well panel">
		<dl class="margin-none dl-horizontal">
			<dt><s:text name="database.management.label.date" /></dt>
				<dd><code><s:date name="#dumpReportVar.date" format="EEEE dd/MMM/yyyy, HH:mm:ss" /></code></dd>
		</dl>
	</div>
	<s:set var="dumpReportVar" value="getDumpReport(subFolderName)" />
	<s:set var="installedComponentsVar" value="currentComponents" />
	<div class="row">
		<div class="col-md-6 col-lg-6">
			<div class="panel panel-default">
				<div class="panel-body">
					<h2 class="h4 margin-none margin-small-bottom"><s:text name="database.management.label.components.current" /></h2>
					<s:text name="database.management.note.components.current" /><br />
						<ul class="margin-base-top list-unstyled">
							<li class="margin-small-bottom">
								<code>entandoCore</code>
							</li>
							<s:iterator var="currentComponentVar" value="#installedComponentsVar">
								<li class="margin-small-bottom">
									<s:set var="labelComponentDescrVar" value="%{#currentComponentVar.code + '.name'}" />
									<s:text name="%{#labelComponentDescrVar}" var="componentDescrVar" />
										<code title="<s:property value="#currentComponentVar.description" />">
											<s:property value="%{#componentDescrVar.equals(#labelComponentDescrVar) ? #currentComponentVar.code : #componentDescrVar }" />
										</code>
									</li>
							</s:iterator>
						</ul>
					</div>
				</div>
		</div>
		<div class="col-md-6 col-lg-6">
			<div class="panel panel-default">
				<div class="panel-body">
					<h2 class="h4 margin-none margin-small-bottom"><s:text name="database.management.label.components.backup" /></h2>
					<s:text name="database.management.note.components.backup" /><br />
						<ul class="margin-base-top list-unstyled">
							<s:iterator var="currentComponentVar" value="#dumpReportVar.componentsHistory">
								<li class="margin-small-bottom">
									<s:set var="installedCheckVar" value="%{false}" />
									<s:iterator value="#installedComponentsVar" var="c">
										<s:if test="#c.code.equals(#currentComponentVar.componentCode) || #currentComponentVar.componentCode.equals('entandoCore')">
												<s:set var="installedCheckVar" value="%{true}" />
										</s:if>
									</s:iterator>
									<s:if test="%{#installedCheckVar}">
										<span class="icon fa fa-check text-muted"></span>
									</s:if>
									<s:else>
										&emsp;
									</s:else>
			 						<s:set var="labelComponentDescrVar" value="%{#currentComponentVar.componentCode + '.name'}" />
									<s:text name="%{#labelComponentDescrVar}" var="componentDescrVar" />
									<code>
										<s:property value="%{#componentDescrVar.equals(#labelComponentDescrVar) ? #currentComponentVar.componentCode : #componentDescrVar }" />
									</code>
								</li>
							</s:iterator>
						</ul>
					</div>
				</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12 col-lg-12">
			<s:set var="restoreCheckVar" value="%{checkRestore(#installedComponentsVar, #dumpReportVar)}" />
			<s:if test="#restoreCheckVar">
				<p class="text-center text-success">
					<span class="icon fa fa-check">&#32;<s:text name="database.management.note.restore.do.fits" /></span>
				</p>
			</s:if>
			<s:else>
				<p class="text-center text-warning">
					<s:text name="database.management.note.restore.do.not.fits" />
				</p>
			</s:else>
			<s:form action="restoreBackup" method="get" namespace="/do/Admin/Database">
				<p class="text-center">
					<wpsf:hidden name="subFolderName" value="%{subFolderName}" />
					<wpsf:submit type="button" cssClass="btn btn-%{#restoreCheckVar ? 'success' : 'warning'}">
							<s:text name="database.management.label.restore" />
					</wpsf:submit>
				</p>
			</s:form>
		</div>
	</div>
</s:else>