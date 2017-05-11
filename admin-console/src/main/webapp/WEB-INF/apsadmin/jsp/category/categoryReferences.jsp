<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure" /></li>
	<li><a href="<s:url namespace="/do/Category" action="viewTree" />">
			<s:text name="title.categoryManagement" />
	</a></li>
	<li class="page-title-container"><s:text
			name="title.categoryReference" /></li>
</ol>
<h1 class="page-title-container">
	<s:text name="title.categoryReference" />
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

<div id="main" role="main">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">
			<span class="pficon pficon-close"></span>
		</button>
		<span class="pficon pficon-error-circle-o"></span> <strong><s:text
				name="message.title.ActionErrors" /></strong>
		<s:text name="message.note.resolveReferences" />
	</div>

	<s:include
		value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo-references.jsp" />
</div>