<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />">File Browser</a>
		&#32;/&#32;
		Delete
	</span>
</h1>
<div id="main">
	
	<s:include value="/WEB-INF/apsadmin/jsp/filebrowser/include/breadcrumbs.jsp" />
	
	<s:form action="delete" namespace="/do/FileBrowser" method="post" cssClass="margin-base-top">
		<div class="alert alert-warning">
			<s:hidden name="deleteFile" />
			<s:hidden name="currentPath" />
			<s:hidden name="filename" />
			<s:hidden name="protectedFolder" />
			<s:hidden name="strutsAction" />
			<p>
				Do you really want to delete
				<code><s:property value="%{filename}" /></code>?
			</p>
			<div class="text-center margin-large-top">
				<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
					<span class="icon fa fa-times-circle"></span>
					<s:text name="label.confirm" />
				</wpsf:submit>
				<wpsf:submit type="button" action="list" cssClass="btn btn-link">
					<s:text name="label.cancel" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>
