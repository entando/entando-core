<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li>
		<s:text name="%{'title.' + entityManagerName + '.management'}"/>
	</li>
	<li class="page-title-container">
		<s:text name="title.entityTypes.editType.remove" />
	</li>
</ol>
<h1 class="page-title-container">
	<s:text name="title.entityTypes.editType.remove"/>&#32;<s:text name="%{'title.' + entityManagerName + '.management'}"/>
	<span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
		   data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o"
																						 aria-hidden="true"></i></a>
    </span>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>


<div class="text-center">
	<s:form action="removeEntityType">
		<p class="sr-only">
			<wpsf:hidden name="entityManagerName" />
			<wpsf:hidden name="entityTypeCode" />
		</p>
		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline">
			<s:text name="title.entityTypes.editType.remove"/>&#32;<s:text name="%{'title.' + entityManagerName + '.management'}"/>
		</p>
		<p>
			<s:text name="note.entityTypes.deleteType.areYouSure" />:&#32;
			<code><s:property value="entityTypeCode" />
					<s:property value="%{getEntityPrototype(entityTypeCode).typeDescr}" />?
			</code>
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
				<s:text name="label.delete"/>
			</wpsf:submit>
		</div>
		<div class="text-center margin-large-top">
			<a class="btn btn-link" href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>">
				<s:text name="note.goToSomewhere" />: <s:text name="%{'title.' + entityManagerName + '.management'}" />
			</a>
		</div>
	</s:form>
</div>
