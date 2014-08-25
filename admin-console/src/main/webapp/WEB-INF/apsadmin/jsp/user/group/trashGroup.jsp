<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/Group"></s:url>"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.groupManagement" />">
		<s:text name="title.groupManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.groupManagement.groupTrash" />
	</span>
</h1>
<s:form action="delete">
	<p class="sr-only"><wpsf:hidden name="name"/></p>
	<div class="alert alert-warning">
		<p>
			<s:text name="note.groupConfirm.trash" />&#32;
			<code><s:property value="name" /></code>
			?
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
			    <span class="icon fa fa-times-circle"></span>&#32;
				<s:text name="label.confirm" />
			</wpsf:submit>
			<a class="btn btn-link" href="<s:url action="list" />">
			<s:text name="note.goToSomewhere" />:&#32;<s:text name="menu.accountAdmin.groups" /></a>
		</div>
	</div>
</s:form>