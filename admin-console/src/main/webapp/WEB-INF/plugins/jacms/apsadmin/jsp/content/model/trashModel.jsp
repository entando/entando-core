<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><a
		href="<s:url action="list" namespace="/do/jacms/ContentModel" />"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.contentModels" />"><s:text
				name="title.contentModels" /></a></li>
	<li class="page-title-container"><s:text
			name="title.contentModels.remove" /></li>
</ol>
<h1 class="page-title-container">
	<s:text name="title.contentModels.remove" />
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
			<wpsf:hidden name="modelId" />
		</p>
		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline">
			<s:text name="title.contentModels.remove" />
		</p>
		<p>
			<s:text name="note.deleteContentModel.areYouSure" />
			&#32;
			<code>
				<s:property value="modelId" />
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
			<a class="btn btn-default button-fixed-width"
				href="<s:url action="list" namespace="/do/jacms/ContentModel"/>">
				<s:text name="note.back" />
			</a>
		</div>
	</s:form>
</div>
