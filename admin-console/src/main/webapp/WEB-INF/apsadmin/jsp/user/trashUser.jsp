<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a>
		&#32;/&#32;
		<s:text name="title.userManagement.userTrash" />
	</span>
</h1>
<s:form action="delete" namespace="/do/User">
	<p class="sr-only">
		<wpsf:hidden name="username"/>
	</p>
	<div class="alert alert-warning">
		<p>
			<s:text name="note.userConfirm.trash" />&#32;
			<code><s:property value="%{username}" /></code>?
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
			    <span class="icon fa fa-times-circle"></span>&#32;
				<s:text name="label.confirm" />
			</wpsf:submit>
			<a class="btn btn-link" href="<s:url action="list" namespace="/do/User" />">
			<s:text name="note.goToSomewhere" />:&#32;<s:text name="menu.accountAdmin.users" /></a>
		</div>
	</div>
</s:form>