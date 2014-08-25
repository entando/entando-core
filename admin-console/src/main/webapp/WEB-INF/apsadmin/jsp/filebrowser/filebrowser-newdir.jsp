<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />">File Browser</a>
		&#32;/&#32;
		Folder
	</span>
</h1>
<div id="main">

	<s:include value="/WEB-INF/apsadmin/jsp/filebrowser/include/breadcrumbs.jsp" />

	<s:form
		cssClass="margin-base-top form-horizontal"
		action="createDir"
		namespace="/do/FileBrowser"
		method="post"
		>
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon icon-remove"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<div class="form-group">
			<div class="col-xs-12">
				<s:hidden name="currentPath" />
				<s:hidden name="strutsAction" />
				<s:hidden name="protectedFolder" />
				<label for="new-folder-name display-block">New Folder Name</label>
				<s:textfield id="new-folder-name" name="dirname" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-save"></span>&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit
					action="list"
					value="%{getText('label.cancel')}"
					cssClass="btn btn-link" />
			</div>
		</div>
	</s:form>
</div>
