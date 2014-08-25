<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />" 
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" />">
		<s:text name="title.widgetManagement" /></a>
		&#32;/&#32;<s:text name="title.widgetManagement.type.delete" />
	</span>
</h1>

<div id="main" role="main">
<s:form namespace="/do/Portal/WidgetType" action="delete">
	<p class="sr-only">
		<wpsf:hidden name="widgetTypeCode" />
	</p>
	<div class="alert alert-warning">
		<p>
			<s:text name="note.deleteType.areYouSure" />&#32;
			<code><s:property value="widgetTypeCode" /></code>?
		</p>
		<div class="text-center margin-large-top">
		<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
			<span class="icon fa fa-times-circle"></span>&#32;
			<s:text name="label.remove" />
		</wpsf:submit>
		<a class="btn btn-link" href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType"/>" ><s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" /></a>
		</div>
	</div>
</s:form>
</div>