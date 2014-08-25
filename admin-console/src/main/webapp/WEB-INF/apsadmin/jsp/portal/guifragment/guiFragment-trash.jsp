<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="title.guiFragmentManagement" /></a>&#32;/&#32;
		<s:if test="getStrutsAction() == 4">
			<s:text name="guiFragment.label.delete" />
		</s:if>
	</span>
</h1>

<s:form action="delete" cssClass="form-horizontal">
	<p class="sr-only">
		<wpsf:hidden name="strutsAction" />
		<wpsf:hidden name="code" />
	</p>
	<div class="alert alert-warning">
		<p>
			<s:text name="note.deleteGuiFragment.areYouSure" />&#32;
			<code><s:property value="code"/></code>?
		</p>
		<%-- save button --%>
		<div class="text-center margin-large-top">
			<s:submit type="button" cssClass="btn btn-warning btn-lg">
				<span class="icon fa fa-times-circle"></span>&#32;
				<s:text name="label.remove" />
			</s:submit>
			<a class="btn btn-link" href="<s:url action="list"/>" ><s:text name="note.goToSomewhere" />: <s:text name="title.guiFragmentManagement" /></a>
		</div>
	</div>
</s:form>
