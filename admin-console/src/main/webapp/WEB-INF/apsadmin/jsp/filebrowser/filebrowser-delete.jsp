<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure" /></li>
	<li><s:text name="menu.filebrowserAdmin" /></li>
	<li class="page-title-container"><s:text name="label.delete" /></li>
</ol>
<h1 class="page-title-container">
	<s:text name="label.delete" />
	<span class="pull-right"> <a tabindex="0" role="button"
		data-toggle="popover" data-trigger="focus" data-html="true" title=""
		data-content="TO be inserted" data-placement="left"
		data-original-title=""><i class="fa fa-question-circle-o"
			aria-hidden="true"></i></a>
	</span>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>
<s:include
	value="/WEB-INF/apsadmin/jsp/filebrowser/include/breadcrumbs.jsp" />
<div class="text-center">
	<s:form action="delete" namespace="/do/FileBrowser" method="post"
		cssClass="margin-base-top">
		<s:hidden name="deleteFile" />
		<s:hidden name="currentPath" />
		<s:hidden name="filename" />
		<s:hidden name="protectedFolder" />
		<s:hidden name="strutsAction" />

		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline">
			<s:text name="label.delete" />
		</p>
		<p>
			<s:text name="label.delete.confirm" />
			&#32;
			<code>
				<s:property value="%{filename}" />
			</code>
			?
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button"
				cssClass="btn btn-danger button-fixed-width">
				<s:text name="label.delete" />
			</wpsf:submit>
		</div>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button" action="list" cssClass="btn btn-link">
				<s:text name="label.cancel" />
			</wpsf:submit>
		</div>
	</s:form>
</div>
