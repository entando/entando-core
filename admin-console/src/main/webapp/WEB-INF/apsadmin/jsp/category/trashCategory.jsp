<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Category" />" 
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.categoryManagement" />">
		<s:text name="title.categoryManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.deleteCategory" />
	</span>
</h1>
<div id="main" role="main">
	<%-- father category <s:property value="getCategory(selectedNode).parent.code" />  --%>
	<s:form action="delete">
		<s:set var="categoryToDelete" value="getCategory(selectedNode)" />
		<p class="sr-only">
			<wpsf:hidden name="selectedNode" />
		</p>
		<div class="alert alert-warning">
			<p>
				<s:text name="note.deleteCategory.areYouSure" />&#32;
				<code><s:property value="getTitle(#categoryToDelete.code, #categoryToDelete.titles)" /></code>?
			</p>
			<div class="text-center margin-large-top">
				<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
					<span class="icon fa fa-times-circle"></span>&#32;
					<s:text name="label.remove" />
				</wpsf:submit>
				<a class="btn btn-link"	href="<s:url action="viewTree" namespace="/do/Category"><s:param name="selectedNode"><s:property value="%{#categoryToDelete.code}" /></s:param></s:url>">
				<s:text name="note.goToSomewhere" />:&#32;<s:text name="title.categoryManagement" />
				</a>
			</div>
		</div>
	</s:form>
</div>