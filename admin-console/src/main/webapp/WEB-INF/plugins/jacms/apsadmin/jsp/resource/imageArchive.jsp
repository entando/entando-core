<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="onEditContent">
	<h1 class="panel panel-default title-page">
		<span class="panel-body display-block">
			<a href="<s:url action="list" namespace="/do/jacms/Content"/>">
				<s:text name="jacms.menu.contentAdmin" />
			</a>&#32;/&#32;
			<a href="<s:url action="backToEntryContent" ><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
				<s:if test="getStrutsAction() == 1">
					<s:text name="label.new" />
				</s:if>
				<s:else>
					<s:text name="label.edit" />
				</s:else>
			</a>&#32;/&#32;
			<s:property value="%{getText('title.' + resourceTypeCode + 'Management')}" />
		</span>
	</h1>
</s:if>

<s:if test="!onEditContent">
	<h1 class="panel panel-default title-page">
		<span class="panel-body display-block">
			<s:property value="%{getText('title.' + resourceTypeCode + 'Management')}" />
		</span>
	</h1>
</s:if>

<s:form action="search" cssClass="form-horizontal" role="search">
	<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
	<p class="sr-only">
		<wpsf:hidden name="resourceTypeCode" />
		<s:if test="#categoryTreeStyleVar == 'request'">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
		<wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/>
		</s:iterator>
		</s:if>
		<wpsf:hidden name="contentOnSessionMarker" />
	</p>
	<div class="form-group">
		<div class="input-group col-sm-12">
			<span class="input-group-addon">
				<span class="icon fa fa-file-text-o fa-lg"></span>
			</span>
			<label for="text" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.description"/></label>
			<wpsf:textfield name="text" id="text" cssClass="form-control input-lg" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')} %{getText('label.description')}" />
			<div class="input-group-btn">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
					<span class="icon fa fa-search"></span>
					<span class="sr-only"><s:text name="label.search" /></span>
				</wpsf:submit>
				<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
					<span class="sr-only"><s:text name="title.searchFilters" /></span>
					<span class="caret"></span>
				</button>
			</div>
		</div>
		<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div id="search-advanced" class="collapse well collapse-input-group">
				<%-- groups --%>
					<s:set var="allowedGroupsVar" value="allowedGroups"></s:set>
					<s:if test="null != #allowedGroupsVar && #allowedGroupsVar.size()>1">
						<div class="form-group">
							<label for="ownerGroupName" class="control-label col-sm-2 text-right"><s:text name="label.group" /></label>
							<div class="col-sm-5">
								<wpsf:select name="ownerGroupName" id="ownerGroupName" list="#allowedGroupsVar" headerKey="" headerValue="%{getText('label.all')}" listKey="name" listValue="descr" cssClass="form-control" />
							</div>
						</div>
					</s:if>
				<%-- filename --%>
					<div class="form-group">
						<label for="fileName" class="control-label col-sm-2 text-right"><s:text name="label.filename" /></label>
						<div class="col-sm-5">
							<wpsf:textfield name="fileName" id="fileName" cssClass="form-control"/>
						</div>
					</div>
				<%-- category tree --%>
					<div class="form-group">
						<label class="control-label col-sm-2 text-right">
							<s:text name="label.category" />
						</label>
						<div class="col-sm-5">
							<div class="well">
								<ul id="categoryTree" class="fa-ul list-unstyled">
									<s:set name="inputFieldName" value="'categoryCode'" />
									<s:set name="selectedTreeNode" value="categoryCode" />
									<s:set name="liClassName" value="'category'" />
									<s:set name="treeItemIconName" value="'fa-folder'" />

									<s:if test="#categoryTreeStyleVar == 'classic'">
										<s:set name="currentRoot" value="categoryRoot" />
										<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
									</s:if>
									<s:elseif test="#categoryTreeStyleVar == 'request'">
										<s:set name="currentRoot" value="showableTree" />
										<s:set name="openTreeActionName" value="'openCloseCategoryTreeNodeOnResourceFinding'" />
										<s:set name="closeTreeActionName" value="'openCloseCategoryTreeNodeOnResourceFinding'" />
										<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
									</s:elseif>
								</ul>
							</div>
						</div>
					</div>
				<%-- search --%>
					<div class="form-group">
						<div class="col-sm-5 col-sm-offset-2">
							<wpsf:submit type="button" cssClass="btn btn-primary">
								<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
							</wpsf:submit>
						</div>
					</div>
			</div>
		</div>
	</div>
</s:form>


<wp:ifauthorized permission="manageResources">
	<a href="
		<s:url action="new" >
			<s:param name="resourceTypeCode" >Image</s:param><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
		</s:url>"
		class="btn btn-default">
		<span class="icon fa fa-plus-circle"></span>&#32;
		<s:text name="label.new" />&#32;<s:text name="label.image" />
	</a>
</wp:ifauthorized>


<s:form action="search">
<p class="sr-only">
	<wpsf:hidden name="text" />
	<wpsf:hidden name="categoryCode" />
	<wpsf:hidden name="resourceTypeCode" />
	<wpsf:hidden name="fileName" />
	<wpsf:hidden name="ownerGroupName" />
	<s:if test="#categoryTreeStyleVar == 'request'">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
		<wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/>
		</s:iterator>
	</s:if>
	<wpsf:hidden name="contentOnSessionMarker" />
</p>

<wpsa:subset source="resources" count="10" objectName="groupResource" advanced="true" offset="5" >
<s:set name="group" value="#groupResource" />
<div class="pager margin-more-bottom">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<div class="row">

	<s:set var="imageDimensionsVar" value="imageDimensions" />
	<s:iterator var="resourceid" status="status">

		<s:set name="resource" value="%{loadResource(#resourceid)}" />
		<s:set var="resourceInstance" value='%{#resource.getInstance(0,null)}' />
		<s:set var="URLoriginal" value="%{#resource.getImagePath(0)}" />

		<s:url var="URLedit" action="edit" namespace="/do/jacms/Resource">
			<s:param name="resourceId" value="%{#resourceid}" />
		</s:url>
		<s:url var="URLuse" action="joinResource" namespace="/do/jacms/Content/Resource">
			<s:param name="resourceId" value="%{#resourceid}" />
			<s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
		</s:url>

		<s:url var="URLtrash" action="trash" namespace="/do/jacms/Resource">
			<s:param name="resourceId" value="%{#resourceid}" />
			<s:param name="resourceTypeCode" value="%{#resource.type}" />
			<s:param name="text" value="%{text}" />
			<s:param name="categoryCode" value="%{categoryCode}" />
			<s:param name="fileName" value="%{fileName}" />
			<s:param name="ownerGroupName" value="%{ownerGroupName}" />
			<s:param name="treeNodesToOpen" value="%{treeNodesToOpen}" />
		</s:url>

		<div class="col-sm-4 col-md-3">
			<div class="panel panel-default text-center">

			<s:if test="!onEditContent">
				<div class="panel-heading text-right padding-small-vertical padding-small-right">
					<a href="<s:property value="URLtrash" escape="false" />" class="icon fa fa-times-circle text-warning">
						<span class="sr-only">Delete</span>
					</a>
				</div>
			</s:if>
				<div>
					<%-- Dimension forced for img thumbnail --%>
					<img src="<s:property value="%{#resource.getImagePath(1)}"/>" alt=" " style="height:90px;max-width:130px" class="margin-small-top" />
				</div>
				<div class="btn-group margin-small-vertical">
				<s:if test="!onEditContent">
					<a href="<s:property value="URLedit" escape="false" />"
						 class="btn btn-default"
						 title="<s:text name="label.edit" />: <s:property value="#resource.descr" />">
						<span class="icon fa fa-pencil-square-o"></span>&#32;
						<s:text name="label.edit" />
					</a>
				</s:if>
				<s:else>
					<a href="<s:property value="URLuse" escape="false" />"
						 class="btn btn-default"
						 title="<s:text name="note.joinThisToThat" />:	<s:property value="content.descr" />" >
						<span class="icon fa fa-picture-o"></span>&#32;
						<s:text name="label.use"/>
					</a>
				</s:else>
					<button type="button" class="btn btn-info" data-toggle="popover" data-title="<s:property value="#resource.descr" />">
						<span class="icon fa fa-info"></span>
						<span class="sr-only"><s:text name="label.info" /></span>
					</button>
				</div>

				<s:set var="fileInfo">
					<s:include value="imageArchive-file-info.jsp" />
				</s:set>

				<script>
					$("[data-toggle=popover]").popover({
						html: true,
						placement: "top",
						content: '<s:property value="fileInfo" escape="false" />'
					});
				</script>

			</div>
		</div>

	</s:iterator>

</div>


<div class="pager clear margin-more-top">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>

</s:form>

<wp:ifauthorized permission="superuser" >
<s:if test="!onEditContent">
<s:action name="openAdminProspect" namespace="/do/jacms/Resource/Admin" ignoreContextParams="true" executeResult="true">
	<s:param name="resourceTypeCode" value="resourceTypeCode" ></s:param>
</s:action>
</s:if>
</wp:ifauthorized>

</div>