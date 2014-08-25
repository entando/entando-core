<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.apiServiceManagement" />&#32;/&#32;
		<s:text name="title.api.apiService.trash" />
	</span>
</h1>

<div id="main" role="main">
	<s:form action="delete">
		<p class="sr-only">
			<wpsf:hidden name="serviceKey" />
		</p>
		<div class="alert alert-warning">
			<p>
				<s:text name="note.api.apiService.trash" />:&#32;
				<code><s:property value="serviceKey" /></code>?
			</p>
			<div class="text-center margin-large-top">
				<wpsf:submit type="button" action="delete" cssClass="btn btn-warning btn-lg">
					<span class="icon fa fa-times-circle"></span>&#32;
					<s:text name="label.remove" />
				</wpsf:submit>
				<a class="btn btn-link" href="<s:url action="list" namespace="/do/Api/Service"/>" ><s:text name="note.goToSomewhere" />: <s:text name="menu.apisAdmin.services" /></a>
			</div>
		</div>
	</s:form>
</div>