<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/jacms/ContentModel" />">
		<s:text name="title.contentModels" /></a>
		&#32;/&#32;
		<s:text name="title.contentModels.remove" />
	</span>
</h1>

<s:form>
	<p class="sr-only">	
		<wpsf:hidden name="modelId"/>
	</p>
	<div class="alert alert-warning">
		<p>
			<s:text name="note.deleteContentModel.areYouSure" />:&#32;
			<code><s:property value="modelId" /></code>&#32;
			<s:property value="description" />?
		</p>
		<div class="text-center margin-large-top">
		<wpsf:submit type="button" action="delete" cssClass="btn btn-warning btn-lg">
			<span class="icon fa fa-times-circle"></span>&#32;
			<s:text name="label.remove" />
		</wpsf:submit>
		<a class="btn btn-link" href="<s:url action="list" namespace="/do/jacms/ContentModel"/>" ><s:text name="note.goToSomewhere" />: <s:text name="title.contentModels" /></a>
		</div>
	</div>
</s:form>
