<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="strutsAction == 1">
	<s:set var="labelTitle" value="%{getText('title.bulk.content.category.join')}"/>
</s:if>
<s:elseif test="strutsAction == 4" >
	<s:set var="labelTitle" value="%{getText('title.bulk.content.category.rem')}"/>
</s:elseif>

<!-- Admin console Breadcrumbs -->
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>APPS</li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
            <s:text name="breadcrumb.jacms.content.list" />
        </a>
    </li>
    <li class="page-title-container"><s:property value="%{#labelTitle}" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}"/>
<h1 class="page-title-container">
    <s:property value="%{#labelTitle}" />&#32;&ndash;&#32;<s:text name="title.bulk.categoryChoice" />
    <span class="pull-right">
        <a tabindex="0" role="button" 
            data-toggle="popover" data-trigger="focus" 
            data-html="true" title="" data-content="${dataContent}" 
            data-placement="left" data-original-title="${dataOriginalTitle}">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<!-- Default separator -->
<div class="text-right">
    <div class="form-group-separator"><s:text name="label.requiredFields" /></div>
</div>

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
			<s:iterator var="contentId" value="selectedIds" >
				<wpsf:hidden name="selectedIds" value="%{#contentId}" />
			</s:iterator>
			<s:iterator var="categoryCode" value="categoryCodes" >
				<wpsf:hidden name="categoryCodes" value="%{#categoryCode}" />
			</s:iterator>
		</p>
		
		<div class="alert alert-info mt-20">
            <span class="pficon pficon-info"></span>
			<s:if test="strutsAction == 1">
				<s:text name="note.bulk.content.category.join.choose" ><s:param name="items" value="%{selectedIds.size()}" /></s:text>
			</s:if>
			<s:elseif test="strutsAction == 4">
				<s:text name="note.bulk.content.category.rem.choose" ><s:param name="items" value="%{selectedIds.size()}" /></s:text>
			</s:elseif>
		</div>

		<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
		
		<s:if test="#categoryTreeStyleVar == 'request'">
		<p class="sr-only">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
		</p>
		</s:if>
		
		<fieldset class="col-xs-12 no-padding" id="category-content-block">
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

				<ul class="list-inline mt-20">
				<s:iterator value="categoryCodes" var="categoryCode">
					<s:set var="category" value="%{getCategory(#categoryCode)}" />
					<li>
					<span class="label label-info">
						<span class="icon fa fa-tag"></span>&#32;
						<abbr title="<s:property value="#category.getFullTitle(currentLang.code)"/>"><s:property value="#category.getShortFullTitle(currentLang.code)" /></abbr>&#32;
						<wpsa:actionParam action="disjoin" var="actionName" >
							<wpsa:actionSubParam name="categoryCode" value="%{#category.code}" />
						</wpsa:actionParam>
						<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #category.defaultFullTitle}" cssClass="btn btn-link">
							<span class="pficon pficon-close white">
							<span class="sr-only">x</span>
						</wpsf:submit>
					</span>
					</li>
				</s:iterator>
				</ul>
			</s:if>
		
		</fieldset>
		
		<div class="row">
		    <div class="col-xs-12">
			<s:if test="strutsAction == 1">
				<s:set var="labelAction" value="%{getText('label.join')}"/>
	                <wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-primary pull-right">
	                    <s:property value="%{#labelAction}" />
	                </wpsf:submit>
			</s:if>
			<s:elseif test="strutsAction == 4" >
				<s:set var="labelAction" value="%{getText('label.remove')}"/>
	                <wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-danger pull-right">
	                    <s:property value="%{#labelAction}" />
	                </wpsf:submit>
			</s:elseif>
		    </div>
		</div>
	</s:form>
</div>