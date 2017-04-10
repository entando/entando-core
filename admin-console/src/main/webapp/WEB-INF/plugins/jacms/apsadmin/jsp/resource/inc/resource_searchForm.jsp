<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:form action="search" cssClass="form-horizontal" role="search">
	<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
	<p class="sr-only">
		<wpsf:hidden name="resourceTypeCode" />
		<s:if test="#categoryTreeStyleVar == 'request'">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/></s:iterator>
		</s:if>
		<wpsf:hidden name="contentOnSessionMarker" />
	</p>
	<div class="form-group">
		<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<span class="input-group-addon" title="<s:text name="label.search.by" />&#32;<s:text name="label.description" />">
				<span class="icon fa fa-file-text-o fa-lg"></span>
			</span>
			<label for="text" class="sr-only" ><s:text name="label.search.by" />&#32;<s:text name="label.description" /></label>
			<wpsf:textfield name="text" id="text" cssClass="form-control input-lg" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')} %{getText('label.description')}" />
			<div class="input-group-btn">
				<wpsf:submit type="button" title="%{getText('label.search')}" cssClass="btn btn-primary btn-lg" >
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
									<s:set var="openTreeActionName" value="'openCloseCategoryTreeNodeOnResourceFinding'" />
									<s:set var="closeTreeActionName" value="'openCloseCategoryTreeNodeOnResourceFinding'" />
									<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
								</s:elseif>
							</ul>
						</div>
					</div>
				</div>
				<%-- search button --%>
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