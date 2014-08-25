<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="title.categoryManagement" /></span></h1>
<div id="main" role="main">
<s:form cssClass="action-form">
	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
		</div>
	</s:if>
	<s:if test="hasActionMessages()">
		<div class="alert alert-info alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h3>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<%-- <fieldset class="margin-more-top"><legend><s:text name="title.categoryTree" /></legend> --%>

	<s:set var="categoryTreeStyleVar"><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<div class="well">
	<ul id="categoryTree" class="fa-ul list-unstyled">
		<s:set var="inputFieldName" value="%{'selectedNode'}" />
		<s:set var="selectedTreeNode" value="%{selectedNode}" />
		<s:set var="liClassName" value="'category'" />
		<s:set var="treeItemIconName" value="'fa-folder'" />

		<s:if test="#categoryTreeStyleVar == 'classic'">
			<s:set var="currentRoot" value="treeRootNode" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
		</s:if>
		<s:elseif test="#categoryTreeStyleVar == 'request'">
			<s:set var="openTreeActionName" value="'openCloseCategoryTree'" />
			<s:set var="closeTreeActionName" value="'openCloseCategoryTree'" />
			<s:set var="currentRoot" value="showableTree" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
		</s:elseif>
	</ul>
</div>

	<p class="sr-only"><s:text name="title.categoryActionsIntro" /></p>
	<fieldset data-toggle="tree-toolbar"><legend><s:text name="title.categoryActions" /></legend>
		<div class="btn-toolbar" data-toggle="tree-toolbar-actions">
			<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
				<wpsf:submit type="button" action="detail" title="%{getText('category.options.detail')}" data-toggle="tooltip" cssClass="btn btn-info">
					<span class="sr-only"><s:text name="category.options.detail" /></span>
					<span class="icon fa fa-info"></span>
				</wpsf:submit>
			</div>
			<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
				<wpsf:submit type="button" action="new" title="%{getText('category.options.new')}" data-toggle="tooltip" cssClass="btn btn-info">
					<span class="sr-only"><s:text name="category.options.new" /></span>
					<span class="icon fa fa-plus-circle"></span>
				</wpsf:submit>
				<wpsf:submit type="button" action="edit" title="%{getText('category.options.modify')}" data-toggle="tooltip" cssClass="btn btn-info">
					<span class="sr-only"><s:text name="category.options.modify" /></span>
					<span class="icon fa fa-pencil-square-o"></span>
				</wpsf:submit>
			</div>
			<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
				<wpsf:submit type="button" action="trash" title="%{getText('category.options.delete')}" data-toggle="tooltip" cssClass="btn btn-warning">
					<span class="sr-only"><s:text name="category.options.delete" /></span>
					<span class="icon fa fa-times-circle"></span>
				</wpsf:submit>
			</div>
		</div>
	</fieldset>
</s:form>
</div>
