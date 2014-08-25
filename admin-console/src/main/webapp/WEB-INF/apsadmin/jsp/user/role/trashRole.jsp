<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/Role" action="list" />">
			<s:text name="title.roleManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.roleManagement.roleTrash" />
	</span>
</h1>
<s:form action="delete">
	<p class="sr-only">
		<wpsf:hidden name="name"/>
	</p>
	<div class="alert alert-warning">
		<p>	
			<s:text name="note.roleConfirm.trash" />&#32;
			<code><s:property value="name" /></code>&#32;?
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
				<span class="icon fa fa-times-circle"></span>&#32;
				<s:text name="label.confirm" />
			</wpsf:submit>
			<a class="btn btn-link"	href="<s:url action="list" namespace="/do/Role" />">
				<s:text name="note.goToSomewhere" />&#32;<s:text name="title.roleManagement" />
			</a>
		</div>
	</div>
</s:form>
