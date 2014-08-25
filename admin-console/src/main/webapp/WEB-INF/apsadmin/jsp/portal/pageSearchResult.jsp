<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></span></h1>

<div id="main" role="main">

<p><s:text name="note.pageTree.intro" /></p>

<s:if test="hasFieldErrors()">
<div class="alert alert-danger alert-dismissable">
	<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
	<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
	<ul>
	<s:iterator value="fieldErrors">
		<li><s:property escape="false" /></li>
	</s:iterator>
	</ul>
</div>
</s:if>
<div role="search">

	<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageSearchForm.jsp" />

	<hr />

	<%--
	<h2 class="margin-base-vertical"><s:text name="title.pageManagement.pages" /></h2>
	--%>

	<s:form action="search" cssClass="action-form">

	<p class="sr-only">
		<wpsf:hidden name="pageCodeToken" />
	</p>

	<s:set var="pagesFound" value="pagesFound" />

	<s:if test="%{#pagesFound != null && #pagesFound.isEmpty() == false}">

		<wpsa:subset source="#pagesFound" count="10" objectName="groupPage" advanced="true" offset="5">
		<s:set name="group" value="#groupPage" />

		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>

		<div class="well">
			<ul id="pageTree" class="fa-ul list-unstyled">
			<s:iterator var="singlePage">

				<s:set name="pageFullPath">
					<s:set value="%{getBreadCrumbsTargets(#singlePage.code)}" name="breadCrumbsTargets" ></s:set>
					<s:iterator value="#breadCrumbsTargets" id="target" status="rowstatus">
						<s:if test="%{#rowstatus.index != 0}"> | </s:if>
						<s:property value="#target.titles[currentLang.code]" />
					</s:iterator>
				</s:set>
				<li class="page tree_node_flag"><span class="icon fa fa-li fa-folder"></span>&#32;<wpsf:radio name="selectedNode" id="page_%{#singlePage.code}" value="%{#singlePage.code}" /><label for="page_<s:property value="%{#singlePage.code}" />" title="<s:property value="#pageFullPath" />"><s:property value="%{#singlePage.code}" /></label></li>
				<%-- <s:property value="%{#singlePage.titles[currentLang.code]}" /> --%>
			</s:iterator>
			</ul>
		</div>

		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>
		</wpsa:subset>

	<p class="sr-only">
		<wpsf:hidden name="copyingPageCode" />
	</p>

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
		</div>
		<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
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
	</s:if>
	<s:else>
		<p class="alert alert-info">
			<s:text name="noPages.found" />
		</p>
	</s:else>

	</s:form>
</div>

</div>