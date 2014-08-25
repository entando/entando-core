<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="onEditContent">
	<h1 class="panel panel-default title-page">
		<span class="panel-body display-block">
			<a href="<s:url action="list" namespace="/do/jacms/Content"/>">
				<s:text name="jacms.menu.contentAdmin" />
			</a>&#32;/&#32;
			<a href="<s:url action="backToEntryContent" ><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
				<s:set var="newContentIndicator" value="content.typeCode + '_newContent'" />
				<s:if test="contentOnSessionMarker == #newContentIndicator">
					<s:text name="label.new" />
				</s:if>
				<s:else>
					<s:text name="label.edit" />
				</s:else>
			</a>&#32;/&#32;
			<s:property value="%{getText('title.' + resourceTypeCode + 'Management')}" />
			&#32;/&#32;<%--TODO -- Link to archive--%>
			<s:if test="getStrutsAction() == 1">
				<s:text name="label.new" />
			</s:if>
			<s:else>
				<s:text name="label.edit" />
			</s:else>
		</span>
	</h1>
</s:if>
<s:if test="!onEditContent">
	<s:set var="targetNS" value="%{'/do/jacms/Resource'}" />
	<s:set var="targetParamName" value="%{'resourceTypeCode'}" />
	<s:set var="targetParamValue" value="resourceTypeCode" />
	<h1 class="panel panel-default title-page">
		<span class="panel-body display-block">
			<a href="<s:url action="list" namespace="/do/jacms/Resource">
			<s:param name="resourceTypeCode">
				<s:property value="resourceTypeCode" />
			</s:param>
			</s:url>"
			title="<s:text name="note.goToSomewhere" />:
			<s:text name="title.resourceManagement" />">
			<s:property value="%{getText('title.' + resourceTypeCode + 'Management')}" /></a>
			&#32;/&#32;
			<s:if test="getStrutsAction() == 1">
				<s:text name="label.new" />
			</s:if>
			<s:else>
				<s:text name="label.edit" />
			</s:else>
		</span>
	</h1>
</s:if>

<s:form action="save" method="post" enctype="multipart/form-data" cssClass="action-form">

<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
		<ul clasS="margin-base-vertical">
		<s:iterator value="fieldErrors">
			<s:iterator value="value">
			<li><s:property escape="false" /></li>
			</s:iterator>
		</s:iterator>
		</ul>
	</div>
</s:if>

<p class="sr-only">
	<wpsf:hidden name="strutsAction" />
	<wpsf:hidden name="resourceTypeCode" />
	<wpsf:hidden name="contentOnSessionMarker" />
	<s:iterator value="categoryCodes" id="categoryCode" status="rowstatus">
	<input type="hidden" name="categoryCodes" value="<s:property value="#categoryCode" />" id="categoryCodes-<s:property value="#rowstatus.index" />"/>
	</s:iterator>
	<s:if test="strutsAction != 1">
		<wpsf:hidden name="resourceId" />
	</s:if>
	<s:if test="#categoryTreeStyleVar == 'request'">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/></s:iterator>
	</s:if>
</p>

<div class="panel panel-default">
	<div class="panel-body">
	 	<div class="form-group">
			<label class="control-label" for="descr"><s:text name="label.description" /></label>
			<wpsf:textfield name="descr" id="descr" cssClass="form-control" />
		</div>

		<s:set name="lockGroupSelect" value="%{resourceId != null && resourceId != 0}"></s:set>
		<div class="form-group">
			<label class="control-label" for="mainGroup"><s:text name="label.group" /></label>
			<wpsf:select name="mainGroup" id="mainGroup" list="allowedGroups" value="mainGroup"
			listKey="name" listValue="descr" disabled="%{lockGroupSelect}" cssClass="form-control" />
		</div>

		<s:if test="%{lockGroupSelect}">
			<p class="sr-only">
				<wpsf:hidden name="mainGroup" />
			</p>
		</s:if>
		<div class="form-group">
			<label for="upload"><s:text name="label.file" /></label>
			<s:file name="upload" id="upload" label="label.file"/>
		</div>
  		<div class="checkbox">
    	<label>
			<input type="checkbox" name="normalizeFileName" id="normalizeFileName" value="true">&#32;<s:text name="label.normalize" />
		</label>
		</div>
	</div>
</div>
<fieldset class="margin-base-vertical" id="category-content-block">
	<legend><span class="icon fa fa-tags"></span>&#32;<s:text name="title.categoriesManagement"/></legend>
	<div class="well">
		<ul id="categoryTree" class="fa-ul list-unstyled">
		<s:set name="inputFieldName" value="'categoryCode'" />
		<s:set name="selectedTreeNode" value="selectedNode" />
		<s:set name="liClassName" value="'category'" />
		<s:set name="treeItemIconName" value="'fa-folder'" />

		<s:if test="#categoryTreeStyleVar == 'classic'">
		<s:set name="currentRoot" value="categoryRoot" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
		</s:if>
		<s:elseif test="#categoryTreeStyleVar == 'request'">
		<s:set name="currentRoot" value="showableTree" />
		<s:set name="openTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
		<s:set name="closeTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
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

<s:iterator value="categoryCodes" id="categoryCode">
<s:set name="resourceCategory" value="%{getCategory(#categoryCode)}"></s:set>
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
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
			<span class="icon fa fa-floppy-o"></span>&#32;
			<s:text name="label.save" />
		</wpsf:submit>
	</div>
</div>
</div>
</s:form>