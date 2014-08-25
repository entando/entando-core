<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="title.apiConsumerManagement" /></a>
		&#32;/&#32;
		<s:text name="title.apiConsumerManagement.trash" />
	</span>
</h1>
<div id="main" role="main">
	<s:form action="delete">
		<p class="sr-only">
			<wpsf:hidden name="consumerKey" />
		</p>
		<s:set var="consumerVar" value="%{getConsumer(consumerKey)}" />
		<div class="alert alert-warning">
			<p>
				<s:text name="note.api.consumer.trash" />:&#32;
				<code><s:property value="consumerKey" /></code>
				<s:property value="#consumerVar.description" />?
			</p>
			<div class="text-center margin-large-top">
				<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
					<span class="icon fa fa-times-circle"></span>&#32;
					<s:text name="label.remove" />
				</wpsf:submit>
				<a class="btn btn-link" href="<s:url action="list" namespace="/do/Api/Consumer" />"><s:text name="note.goToSomewhere" />:&#32;<s:text name="menu.apisAdmin.consumers" /></a>
			</div>
		</div>
	</s:form>
</div>