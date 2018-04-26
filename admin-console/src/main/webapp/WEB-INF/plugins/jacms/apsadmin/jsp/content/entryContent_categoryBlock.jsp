<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<s:if test="#categoryTreeStyleVar == 'request'">
	<p class="sr-only">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
		<wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
	</s:iterator>
</p>
</s:if>

<div class="table-responsive">
	<table id="categoryTree" class="table table-bordered table-hover table-treegrid">
		<thead>
			<tr>
				<th> <s:text name="label.categoriesTree" />
					<s:if test="#categoryTreeStyleVar == 'classic'">
						<button type="button" class="btn-no-button expand-button" id="expandAll">
							<span class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;
							<s:text name="label.expandAll" />
						</button>
						<button type="button" class="btn-no-button" id="collapseAll">
							<span class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;
							<s:text name="label.collapseAll" />
						</button>
					</s:if>
				</th>
				<th class="text-center w4perc">
					<s:text name="label.category.join" />
				</th>
			</tr>
		</thead>
		<tbody>
			<s:set var="selectedTreeNode" value="selectedNode" />
			<s:set var="inputFieldName" value="'categoryCode'" />
			<s:set var="selectedTreeNode" value="categoryCode" />
			<s:set var="liClassName" value="'category'" />
			<s:set var="treeItemIconName" value="'fa-folder'" />
			<s:if test="%{#categoryTreeStyleVar == 'classic'}">
				<s:set var="currentRoot" value="%{allowedTreeRootNode}" />
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilderCategoriesJoin.jsp" />
			</s:if>
			<s:elseif test="%{#categoryTreeStyleVar == 'request'}">
				<s:set var="currentRoot" value="%{showableTree}" />
				<s:set var="openTreeActionName" value="'entryContent'" />
				<s:set var="closeTreeActionName" value="'entryContent'" />
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilder-request-categories.jsp" />
			</s:elseif>
		</tbody>
	</table>
</div>

<s:set var="contentCategories" value="content.categories" />
<s:if test="#contentCategories != null && #contentCategories.size() > 0">
    <h4 class="margin-base-vertical"><s:text name="note.contentCategories.summary"/></h4>
    <ul class="list-inline mt-20">
		<s:iterator value="#contentCategories" var="contentCategory">
			<s:set var="category" value="%{getCategory(#categoryCode)}" />
			<li>
				<span class="label label-info">
					<span class="icon fa fa-tag"></span>&#32;
					<abbr title="<s:property value="#contentCategory.getFullTitle(currentLang.code)"/>"><s:property value="#contentCategory.getShortFullTitle(currentLang.code)" /></abbr>&#32;
					<wpsa:actionParam action="removeCategory" var="actionName" >
						<wpsa:actionSubParam name="categoryCode" value="%{#contentCategory.code}" />
					</wpsa:actionParam>
					<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #contentCategory.defaultFullTitle}" cssClass="btn btn-link">
						<span class="pficon pficon-close white"></span>
						<span class="sr-only">x</span>
					</wpsf:submit>
				</span>
			</li>
		</s:iterator>
    </ul>
</s:if>

