<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="strutsAction == 1">
	<s:set var="labelTitle" value="%{getText('title.bulk.addCategories')}"/>
</s:if>
<s:elseif test="strutsAction == 4" >
	<s:set var="labelTitle" value="%{getText('title.bulk.removeCategories')}"/>
</s:elseif>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url action="list" namespace="/do/jacms/Content"/>"><s:text name="jacms.menu.contentAdmin" /></a></li>
    <li>
		<s:property value="%{#labelTitle}" />
    </li>
</ol>

<h1 class="page-title-container"><s:property value="%{#labelTitle}" />&#32;-&#32;<s:text name="label.bulk.confirm" /></h1>

<div id="main" role="main">
	<s:if test="hasErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">
				<span class="pficon pficon-close"></span>
			</button>
			<span class="pficon pficon-error-circle-o"></span>
			<s:text name="message.title.ActionErrors" />
			<ul>
			<s:if test="hasActionErrors()">
				<s:iterator value="actionErrors">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:if>
			<s:if test="hasFieldErrors()">
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:iterator>
			</s:if>
			</ul>
		</div>
	</s:if>
	<s:form action="checkApply" namespace="/do/jacms/Content/Category" >
		<p class="sr-only">
			<wpsf:hidden name="strutsAction"/>
		<s:iterator var="contentId" value="contentIds" >
			<wpsf:hidden name="contentIds" value="%{#contentId}" />
		</s:iterator>
		<s:iterator var="categoryCode" value="categoryCodes" >
			<wpsf:hidden name="categoryCodes" value="%{#categoryCode}" />
		</s:iterator>
		</p>
		
		<div>
			<p>
			<s:if test="strutsAction == 1">
				<s:text name="note.bulk.addCategories.choose" ><s:param name="items" value="%{contentIds.size()}" /></s:text>
			</s:if>
			<s:elseif test="strutsAction == 4">
				<s:text name="note.bulk.removeCategories.choose" ><s:param name="items" value="%{contentIds.size()}" /></s:text>
			</s:elseif>
			</p>
		</div>
		
		<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
		
		<s:if test="#categoryTreeStyleVar == 'request'">
		<p class="sr-only">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
		</p>
		</s:if>
		
		<fieldset class="margin-base-vertical" id="category-content-block">
			<legend><span class="icon fa fa-tags"></span>&#32;<s:text name="title.categoriesManagement"/></legend>
			<div class="well">
				<ul id="categoryTree" class="fa-ul list-unstyled">
					<s:set var="inputFieldName" value="'categoryCode'" />
					<s:set var="selectedTreeNode" value="categoryCode" />
					<s:set var="liClassName" value="'category'" />
					<s:set var="treeItemIconName" value="'fa-folder'" />
		
					<s:if test="#categoryTreeStyleVar == 'classic'">
						<s:set var="currentRoot" value="categoryRoot" />
					 	<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
					</s:if>
					<s:elseif test="#categoryTreeStyleVar == 'request'">
						<s:set var="currentRoot" value="showableTree" />
						<s:set var="openTreeActionName" value="'entry'" />
						<s:set var="closeTreeActionName" value="'entry'" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
					</s:elseif>
				</ul>
				<div data-toggle="tree-toolbar">
					<div data-toggle="tree-toolbar-actions">
						<wpsf:submit action="join" type="button" title="%{getText('label.join')}" cssClass="btn btn-info btn-sm margin-small-vertical" data-toggle="tooltip">
							<span class="icon fa fa-plus"></span>
						</wpsf:submit>
					</div>
				</div>
			</div>
		<s:if test="categoryCodes != null && categoryCodes.size() > 0">
		
		<h4 class="margin-base-vertical"><s:text name="note.contentCategories.summary"/></h4>
		
		<s:iterator value="categoryCodes" var="categoryCode">
			<s:set var="category" value="%{getCategory(#categoryCode)}" />
			<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
				<span class="icon fa fa-tag"></span>&#32;
				<abbr title="<s:property value="#category.getFullTitle(currentLang.code)"/>"><s:property value="#category.getShortFullTitle(currentLang.code)" /></abbr>&#32;
				<wpsa:actionParam action="disjoin" var="actionName" >
					<wpsa:actionSubParam name="categoryCode" value="%{#category.code}" />
				</wpsa:actionParam>
				<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #category.defaultFullTitle}" cssClass="btn btn-default btn-xs badge">
					<span class="icon fa fa-times"></span>
					<span class="sr-only">x</span>
				</wpsf:submit>
			</span>
		</s:iterator>
		</s:if>
		
		</fieldset>
		
		<s:if test="strutsAction == 1">
			<s:set var="labelAction" value="%{getText('label.bulk.addCategories')}"/>
		</s:if>
		<s:elseif test="strutsAction == 4" >
			<s:set var="labelAction" value="%{getText('label.bulk.removeCategories')}"/>
		</s:elseif>
		<div class="col-xs-12">
			<wpsf:submit type="button" title="%{#labelTitle}" cssClass="btn btn-success">
				<span class="icon fa fa-times-circle"></span>
				<s:property value="%{#labelTitle}" />
			</wpsf:submit>
		</div>
	</s:form>
</div>