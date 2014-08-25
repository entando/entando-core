<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/PageModel"></s:url>"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageModelManagement" />">
		<s:text name="title.pageModelManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.pageModelManagement.pageModelTrash" />
	</span>
</h1>
<s:form action="delete">
	<p class="sr-only"><wpsf:hidden name="code"/></p>
	<div class="alert alert-warning">
		<p>
			<s:text name="note.pageModelConfirm.trash" />&#32;
			<code><s:property value="code" /></code>
			?
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
			    <span class="icon fa fa-times-circle"></span>&#32;
				<s:text name="label.confirm" />
			</wpsf:submit>
			<a class="btn btn-link" href="<s:url action="list" />">
			<s:text name="note.goToSomewhere" />:&#32;<s:text name="menu.accountAdmin.pageModels" /></a>
		</div>
	</div>
</s:form>