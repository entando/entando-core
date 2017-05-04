<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure" /></li>
	<li><a href="<s:url action="list" namespace="/do/Group" />"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.groupManagement" />"><s:text
				name="title.groupManagement" /></a></li>
	<li class="page-title-container"><s:text
			name="title.groupManagement.groupTrash" /></li>
</ol>
<h1 class="page-title-container">
	<s:text name="title.groupManagement.groupTrash" />
	<!--  todo -->
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


<div class="text-center">
	<s:form action="delete">
		<p class="sr-only">
			<wpsf:hidden name="name" />
		</p>
		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline">
			<s:text name="note.gruopConfirm.delete" />
		</p>
		<p>
			<s:text name="note.groupConfirm.trash" />
			&#32;
			<s:property value="name" />
			?
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button"
				cssClass="btn btn-danger button-fixed-width">
				<s:text name="label.delete" />
			</wpsf:submit>
		</div>
		<div class="text-center margin-large-top">
			<a class="btn btn-default button-fixed-width"
				href="<s:url action="list" />"> <s:text
					name="note.backToSomewhere" />&#32;<s:text
					name="title.groupManagement" />
			</a>
		</div>

	</s:form>
</div>