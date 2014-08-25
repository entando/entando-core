<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/Admin/Database" action="entry" />"><s:text name="title.databaseManagement" /></a>
		&#32;/&#32;
		<s:text name="title.databaseBackup.new" />
	</span>
</h1>
<s:set var="subFolderNameVar" value="subFolderName" />
<s:if test="managerStatus != 0" >
	<div class="alert alert-info">
			<s:text name="database.management.note.dump.in.progress" /> ( <a href="<s:url namespace="/do/Admin/Database" action="entry" />" class="alert-link"><s:text name="database.management.refresh" /></a> )
	</div>
</s:if>
<s:else>
	<s:set var="currentComponentsVar" value="currentComponents" />
	<p>
		<s:text name="database.management.note.backup" />
	</p>
	<dl class="dl-horizontal">
		<dt><abbr title="<s:text name="database.management.label.component" /> Core (core)">Core</abbr></dt>
			<dd>
				<s:set var="tableMappingVar" value="entandoTableMapping" />
				<s:include value="/WEB-INF/apsadmin/jsp/admin/inc/datasource-table-names.jsp" />
				<hr class="visible-xs hidden-sm hidden-md hidden-lg" />
			</dd>
		<s:set var="currentComponentsVar" value="currentComponents" />
		<s:iterator var="currentComponentVar" value="#currentComponentsVar" >
			<dt class="margin-medium-top">
					<abbr title="<s:text name="database.management.label.component" />&#32;<s:property value="#currentComponentVar.description" /> (<s:property value="#currentComponentVar.code" />)"><s:property value="#currentComponentVar.description" /></abbr>
			</dt>
				<dd>
					<s:set var="tableMappingVar" value="#currentComponentVar.tableMapping" />
					<s:include value="/WEB-INF/apsadmin/jsp/admin/inc/datasource-table-names.jsp" />
					<hr class="visible-xs hidden-sm hidden-md hidden-lg" />
				</dd>
		</s:iterator>
	</dl>
	<div class="text-center">
		<s:form action="executeBackup" namespace="/do/Admin/Database" method="get">
			<wpsf:submit
				type="button"
				action="executeBackup"
				value="%{getText('database.management.label.backup.execute')}"
				cssClass="btn btn-primary">
					<span class="icon fa fa-check"></span>
					<s:text name="database.management.label.backup.execute" />
			</wpsf:submit>
			<a
				class="btn btn-link"
				href="<s:url namespace="/do/Admin/Database" action="entry" />">
					<s:text name="database.management.label.go.to.list" />
			</a>
		</s:form>
	</div>
</s:else>