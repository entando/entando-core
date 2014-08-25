<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Category" />" 
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.categoryManagement" />">
		<s:text name="title.categoryManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.categoryDetail" />
	</span>
</h1>
<div id="main" role="main">
	<s:set var="breadcrumbs_pivotCategoryCode" value="categoryCode" />
	<s:include value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo_breadcrumbs.jsp" />
	<table class="table table-bordered">
		<tr>
			<th class="text-right"><s:text name="name.categoryCode" /></th>
			<td><s:property value="categoryCode" /></td>
		</tr>
		<tr>
			<th class="text-right"><s:text name="name.categoryTitle" /></th>
			<td>
					<s:iterator value="langs" status="categoryInfo_rowStatus" var="lang">
						<s:if test="%{!#categoryInfo_rowStatus.first}">,&#32;</s:if>
						<span class="monospace">(<abbr title="<s:property value="descr" />"><s:property value="code" /></abbr>)</span>&#32;
						<s:property value="titles[#lang.code]" />
						<s:if test="%{titles[#lang.code] == null}"><abbr title="<s:text name="label.none" />">&ndash;</abbr></s:if>
					</s:iterator>
			</td>
		</tr>
	</table>
	<wpsa:hookPoint key="core.categoryDetails" objectName="hookPointElements_core_categoryDetails">
		<s:iterator value="#hookPointElements_core_categoryDetails" var="hookPointElement">
			<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
		</s:iterator>
	</wpsa:hookPoint>
	<s:include value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo-references.jsp" />
</div>