<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="categoryTreeStyleVar"><wp:info key="systemParam" paramName="treeStyle_category"/></s:set>

<s:if test="#categoryTreeStyleVar == 'request'">
    <p class="sr-only">
        <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen"
                                                                                 value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
    </p>
</s:if>

<%--
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
			<s:set var="openTreeActionName" value="'backToContentList'" />
			<s:set var="closeTreeActionName" value="'backToContentList'" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
		</s:elseif>
	</ul>
</div>
  --%>

<div class="table-responsive overflow-visible">
    <table id="categoryTree" class="table table-bordered table-hover table-treegrid">
        <thead>
        <tr>
            <th style="width: 68%;"><s:text name="label.categoriesTree"/>
                <s:if test="#categoryTreeStyleVar == 'classic'">
                    <button type="button" class="btn-no-button expand-button" id="expandAll">
                        <span class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;<s:text
                            name="label.expandAll"/>
                    </button>
                    <button type="button" class="btn-no-button" id="collapseAll">
                        <span class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;<s:text
                            name="label.collapseAll"/>
                    </button>
                </s:if>
            </th>
        </tr>
        </thead>
        <tbody>
            <s:set var="inputFieldName" value="'categoryCode'"/>
            <s:set var="selectedTreeNode" value="categoryCode"/>
            <s:set var="liClassName" value="'category'"/>
            <s:set var="treeItemIconName" value="'fa-folder'"/>

            <s:if test="#categoryTreeStyleVar == 'classic'">
                <s:set var="currentRoot" value="categoryRoot"/>
                <s:set var="isPosition" value="false"/>
                <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilderCategories.jsp"/>
            </s:if>
            <s:elseif test="#categoryTreeStyleVar == 'request'">
                <style>
                    .table-treegrid span.collapse-icon, .table-treegrid span.expand-icon {
                        cursor: pointer;
                        display: none;
                    }
                </style>
                <s:set var="treeNodeActionMarkerCode" value="treeNodeActionMarkerCode"/>
                <s:set var="targetNode" value="%{parentPageCode}"/>
                <s:set var="treeNodesToOpen" value="treeNodesToOpen"/>
                <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp"/>

            </s:elseif>
        </tbody>
    </table>
</div>
<s:if test="#hasFieldErrorVar">
    <p class="text-danger padding-small-vertical"><s:iterator value="#fieldErrorsVar"><s:property/></s:iterator></p>
</s:if>
