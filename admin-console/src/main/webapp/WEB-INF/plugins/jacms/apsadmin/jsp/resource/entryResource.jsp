<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="breadcrumb.app" /></li>
	<li><s:text name="breadcrumb.jacms" /></li>
	<s:if test="onEditContent">
		<li><s:text name="breadcrumb.jacms.content.list" /></li>
		<li>
			<s:if test="getStrutsAction() == 1"><s:text name="breadcrumb.jacms.content.new" /></s:if><s:else><s:text name="breadcrumb.jacms.content.edit" /></s:else>
		</li>
	</s:if>
	<s:else>
		<li><s:text name="breadcrumb.dataAsset" /></li>
	</s:else>
	<li class="page-title-container">
		<s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}" />
	</li>
</ol>

<h1 class="page-title-container">
	<s:if test="getStrutsAction() == 1">
		<s:text name="title.%{resourceTypeCode}.new" />
	</s:if>
	<s:elseif test="getStrutsAction() == 2">
		<s:text name="title.%{resourceTypeCode}.edit" />
	</s:elseif>
	<span class="pull-right">
		<a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" 
				data-original-title="<s:property value="%{getText('help.' + resourceTypeCode + '.' + strutsAction + '.title')}" escapeXml="true" />" 
				data-content="<s:property value="%{getText('help.' + resourceTypeCode + '.' + strutsAction + '.info')}" escapeXml="true" />" 
				data-placement="left" ><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
	</span>
</h1>

<i class="fa fa-asterisk required-icon"></i>
<div class="text-right">
    <div class="form-group-separator"><s:text name="label.requiredFields" /></div>               
</div>
<br/>




<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
<s:set var="lockGroupSelect" value="%{resourceId != null && resourceId != 0}"></s:set>

<s:form action="save" method="post" enctype="multipart/form-data" cssClass="form-horizontal">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/inc_fullErrors.jsp" />
	
	<p class="sr-only">
		<wpsf:hidden name="strutsAction" />
		<wpsf:hidden name="resourceTypeCode" />
		<wpsf:hidden name="contentOnSessionMarker" />
		<s:iterator value="categoryCodes" var="categoryCode" status="rowstatus">
			<input type="hidden" name="categoryCodes" value="<s:property value="#categoryCode" />" id="categoryCodes-<s:property value="#rowstatus.index" />"/>
		</s:iterator>
		<s:if test="strutsAction != 1">
			<wpsf:hidden name="resourceId" />
		</s:if>
		<s:if test="#categoryTreeStyleVar == 'request'">
			<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/></s:iterator>
		</s:if>
		<s:if test="%{lockGroupSelect}">
			<wpsf:hidden name="mainGroup" />
		</s:if>
	</p>
	
	<%-- descr --%>
	<s:set var="fieldErrorsVar" value="%{fieldErrors['descr']}" />
	<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
	<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
	
	<div class="form-group<s:property value="#controlGroupErrorClass" />">
		<label class="col-sm-2 control-label" for="descr">
			<s:text name="label.description" />
			<i class="fa fa-asterisk required-icon"></i>
		</label>
		<div class="col-sm-10">
			<wpsf:textfield name="descr" id="descr" cssClass="form-control" />
			<s:if test="#hasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
			</s:if>
		</div>
	</div>
	
	<%-- mainGroup --%>
	<s:set var="fieldErrorsVar" value="%{fieldErrors['mainGroup']}" />
	<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
	<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
	
	<div class="form-group<s:property value="#controlGroupErrorClass" />">
		<label class="col-sm-2 control-label" for="mainGroup">
			<s:text name="label.group" />
			<i class="fa fa-asterisk required-icon"></i>
		</label>
		<div class="col-sm-10">
			<wpsf:select name="group" id="group" list="allowedGroups" listKey="name" listValue="description" disabled="%{lockGroupSelect}" cssClass="combobox form-control"></wpsf:select>
			<s:if test="#hasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
			</s:if>
		</div>
	</div>
	
	<%-- upload --%>
	<s:set var="uploadFieldErrorsVar" value="%{fieldErrors['upload']}" />
	<s:set var="fileNameFieldErrorsVar" value="%{fieldErrors['fileName']}" />
	<s:set var="hasFieldErrorVar" value="(#uploadFieldErrorsVar != null && !#uploadFieldErrorsVar.isEmpty()) || (#fileNameFieldErrorsVar != null && !#fileNameFieldErrorsVar.isEmpty())" />
	<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
	
	<div class="form-group<s:property value="#controlGroupErrorClass" />">
		<label class="col-sm-2 control-label" for="upload">
			<s:text name="label.file" />
			<i class="fa fa-asterisk required-icon"></i>
		</label>
		<div class="col-sm-10">
			<s:file name="upload" id="upload" label="label.file" />
			<s:if test="#hasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#uploadFieldErrorsVar}"><s:property />&#32;</s:iterator>
					<s:iterator value="%{#fileNameFieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
			</s:if>
		</div>
	</div>

	<fieldset class="margin-base-vertical" id="category-content-block">
		<legend><span class="icon fa fa-tags"></span>&#32;<s:text name="title.categoriesManagement"/></legend>
		<div class="well">
			<ul id="categoryTree" class="fa-ul list-unstyled">
			<s:set var="inputFieldName" value="'categoryCode'" />
			<s:set var="selectedTreeNode" value="selectedNode" />
			<s:set var="liClassName" value="'category'" />
			<s:set var="treeItemIconName" value="'fa-folder'" />
	
			<s:if test="#categoryTreeStyleVar == 'classic'">
			<s:set var="currentRoot" value="categoryRoot" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
			</s:if>
			<s:elseif test="#categoryTreeStyleVar == 'request'">
			<s:set var="currentRoot" value="showableTree" />
			<s:set var="openTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
			<s:set var="closeTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
			</s:elseif>
			</ul>
		</div>
		<div data-toggle="tree-toolbar">
			<div data-toggle="tree-toolbar-actions">
				<wpsf:submit action="joinCategory" type="button" title="%{getText('label.join')}" cssClass="btn btn-info btn-sm margin-small-vertical" data-toggle="tooltip">
					<span class="icon fa fa-plus"></span>
				</wpsf:submit>
			</div>
		</div>
		
		<s:if test="categoryCodes != null && categoryCodes.size() > 0">
		<h2 class="h4 margin-base-vertical"><s:text name="note.resourceCategories.summary"/></h2>
		
		<s:iterator value="categoryCodes" var="categoryCode">
		<s:set var="resourceCategory" value="%{getCategory(#categoryCode)}"></s:set>
			<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
				<span class="icon fa fa-tag"></span>&#32;
				<abbr title="<s:property value="#resourceCategory.getFullTitle(currentLang.code)"/>"><s:property value="#resourceCategory.getShortFullTitle(currentLang.code)" /></abbr>&#32;
				<wpsa:actionParam action="removeCategory" var="actionName" >
					<wpsa:actionSubParam name="categoryCode" value="%{#resourceCategory.code}" />
				</wpsa:actionParam>
				<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #resourceCategory.defaultFullTitle}" cssClass="btn btn-default btn-xs badge">
					<span class="icon fa fa-times"></span>
					<span class="sr-only">x</span>
				</wpsf:submit>
			</span>
		</s:iterator>
		</s:if>
	
	</fieldset>
	
	<s:if test="strutsAction == 2">
		<s:set var="referencingContentsId" value="references['jacmsContentManagerUtilizers']" />
		<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/portal/include/referencingContents.jsp" />
	</s:if>
	
	<div class="form-horizontal">
		<div class="form-group">
			<div class="col-sm-12 margin-small-vertical">
				<wpsf:submit type="button" cssClass="btn btn-primary pull-right">
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</div>
</s:form>