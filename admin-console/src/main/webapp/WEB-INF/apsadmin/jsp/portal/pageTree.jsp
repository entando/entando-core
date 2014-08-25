<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="title.pageManagement" /></span></h1>

<div id="main" role="main">

<p><s:text name="note.pageTree.intro" /></p>

<s:if test="hasActionErrors()">
<div class="alert alert-danger alert-dismissable">
	<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
	<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
	<ul>
	<s:iterator value="actionErrors">
		<li><s:property escape="false" /></li>
	</s:iterator>
	</ul>
</div>
</s:if>

<div role="search">
	<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageSearchForm.jsp" />

	<hr />

	<%--
	<h2 class="margin-base-vertical"><s:text name="title.pageTree" /></h2>
	--%>

	<s:form cssClass="action-form">

	<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>

	<div class="well">
		<ul id="pageTree" class="fa-ul list-unstyled">
			<s:set var="inputFieldName" value="%{'selectedNode'}" />
			<s:set var="selectedTreeNode" value="%{selectedNode}" />
			<s:set var="liClassName" value="'page'" />
			<s:set var="treeItemIconName" value="'fa-folder'" />

			<s:if test="#pageTreeStyleVar == 'classic'">
			<s:set var="currentRoot" value="allowedTreeRootNode" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
			</s:if>
			<s:elseif test="#pageTreeStyleVar == 'request'">
			<s:set var="currentRoot" value="showableTree" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
			</s:elseif>
		</ul>
	</div>
	<p class="sr-only"><wpsf:hidden name="copyingPageCode" /></p>

	<fieldset data-toggle="tree-toolbar"><legend><s:text name="title.pageActions" /></legend>
		<p class="sr-only"><s:text name="title.pageActionsIntro" /></p>

		<div class="btn-toolbar" data-toggle="tree-toolbar-actions">
			<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
				<wpsf:submit action="configure" type="button" title="%{getText('page.options.configure')}" cssClass="btn btn-info" data-toggle="tooltip">
					<span class="icon fa fa-cog"></span>
				</wpsf:submit>
				<wpsf:submit action="detail" type="button" title="%{getText('page.options.detail')}" cssClass="btn btn-info" data-toggle="tooltip">
					<span class="icon fa fa-info"></span>
				</wpsf:submit>
			</div>
			<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
				<wpsf:submit action="copy" type="button" title="%{getText('page.options.copy')}" cssClass="btn btn-info" data-toggle="tooltip">
					<span class="icon fa fa-files-o"></span>
				</wpsf:submit>
				<wpsf:submit action="paste" type="button" title="%{getText('page.options.paste')}" cssClass="btn btn-info" data-toggle="tooltip">
					<span class="icon fa fa-clipboard"></span>
				</wpsf:submit>
				<wpsf:submit action="moveUp" type="button" title="%{getText('page.options.moveUp')}" cssClass="btn btn-info" data-toggle="tooltip">
					<span class="icon fa fa-sort-desc"></span>
				</wpsf:submit>
				<wpsf:submit action="moveDown" type="button" title="%{getText('page.options.moveDown')}" cssClass="btn btn-info" data-toggle="tooltip">
					<span class="icon fa fa-sort-asc"></span>
				</wpsf:submit>
			</div>
			<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
				<wpsf:submit action="new" type="button" title="%{getText('page.options.new')}" cssClass="btn btn-info" data-toggle="tooltip">
					<span class="icon fa fa-plus-circle"></span>
				</wpsf:submit>
				<wpsf:submit action="edit" type="button" title="%{getText('page.options.modify')}" cssClass="btn btn-info" data-toggle="tooltip">
					<span class="icon fa fa-pencil-square-o"></span>
				</wpsf:submit>
			</div>
			<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
				<wpsf:submit action="trash" type="button" title="%{getText('page.options.delete')}" cssClass="btn btn-warning" data-toggle="tooltip">
					<span class="icon fa fa-times-circle"></span>
				</wpsf:submit>
			</div>
		</div>
	</fieldset>
	</s:form>
</div>
</div>