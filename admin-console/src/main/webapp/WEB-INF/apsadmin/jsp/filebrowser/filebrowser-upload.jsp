<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure" /></li>
	<li><a href="<s:url action="list" />"
		title="<s:text name="note.goToSomewhere" />: <s:text name="menu.filebrowserAdmin" />">
			<s:text name="menu.filebrowserAdmin" />
	</a></li>
	<li class="page-title-container"><s:text name="label.upload" /></li>
</ol>
<h1 class="page-title-container">
	<div>
		<s:text name="label.upload" />
		<span class="pull-right"> <a tabindex="0" role="button"
			data-toggle="popover" data-trigger="focus" data-html="true" title=""
			data-content="TO be inserted" data-placement="left"
			data-original-title=""> <i class="fa fa-question-circle-o"
				aria-hidden="true"></i>
		</a>
		</span>
	</div>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>
<div id="main">
	<s:include
		value="/WEB-INF/apsadmin/jsp/filebrowser/include/breadcrumbs.jsp" />
	<s:form action="upload" namespace="/do/FileBrowser" method="post"
		enctype="multipart/form-data"
		cssClass="margin-base-top form-horizontal">

		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert">
					<span class="icon fa fa-times"></span>
				</button>
				<h2 class="h4 margin-none">
					<s:text name="message.title.FieldErrors" />
				</h2>
				<ul class="margin-base-top">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escapeHtml="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<div class="form-group">
			<div class="col-xs-12">
				<s:hidden name="currentPath" />
				<s:hidden name="strutsAction" />
				<s:hidden name="protectedFolder" />
				<div class="form-group">
					<label class="col-sm-2 control-label" for="file-upload"><s:text
							name="label.upload" /></label>
					<div class="col-sm-10">
						<s:file name="upload" id="file-upload" cssClass="form-control" />
					</div>
				</div>
			</div>
		</div>

		<div class="row form-group pull-right">
			<div class="col-sm-12">
				<wpsf:submit type="button" action="list"
					cssClass="btn btn-default">
					<s:text name="label.cancel" />
				</wpsf:submit>
				<wpsf:submit type="button" cssClass="btn btn-primary">
					<span class="icon fa fa-save"></span>
					&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>

	</s:form>
</div>
