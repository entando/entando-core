<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="categoryTreeStyleVar"><wp:info key="systemParam" paramName="treeStyle_category"/></s:set>

<s:if test="#categoryTreeStyleVar == 'request'">
	<p class="sr-only">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
            <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
		</s:iterator>
	</p>
</s:if>

<div class="table-responsive">
    <table id="categoryTree" class="table table-bordered table-hover table-treegrid ${categoryTreeStyleVar}">
        <thead>
            <tr>
                <th> 
                <s:text name="label.categoriesTree" />
                <s:if test="#categoryTreeStyleVar == 'classic'">
                    <button type="button" class="btn-no-button expand-button" id="expandAll">
                        <span class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;<s:text name="label.expandAll" />
                    </button>
                    <button type="button" class="btn-no-button" id="collapseAll">
                        <span class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;<s:text name="label.collapseAll" />
                    </button>
                </s:if>
	            </th>
	        </tr>
	    </thead>
	    <tbody>
			<s:set var="inputFieldName" value="'categoryCode'" />
			<s:set var="selectedTreeNode" value="categoryCode" />
			<s:set var="liClassName" value="'category'" />
			<s:set var="treeItemIconName" value="'fa-folder'" />
			<s:if test="%{#categoryTreeStyleVar == 'classic'}">
				<s:set var="currentRoot" value="%{allowedTreeRootNode}" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilderCategories.jsp" />
			</s:if>
			<s:elseif test="%{#categoryTreeStyleVar == 'request'}">
				<s:set var="currentRoot" value="%{showableTree}" />
				<s:set var="openTreeActionName" value="'backToContentList'" />
				<s:set var="closeTreeActionName" value="'backToContentList'" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-categories.jsp" />
			</s:elseif>
	    </tbody>
    </table>
</div>
<s:if test="#hasFieldErrorVar">
  <p class="text-danger padding-small-vertical"><s:iterator value="#fieldErrorsVar"><s:property /></s:iterator></p>
</s:if>
