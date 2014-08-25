<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set var="breadCrumbsTargets" value="%{getBreadCrumbsTargets(#breadcrumbs_pivotCategoryCode)}" />
<p class="sr-only"><s:text name="note.youAreHere" />:</p>
<ol class="breadcrumb margin-base-vertical">
	<s:iterator value="#breadCrumbsTargets" var="target" status="rowstatus">
	<li>
		<a 
			href="<s:url namespace="/do/Category" action="viewTree" ><s:param name="selectedNode"><s:property value="#target.code" /></s:param></s:url>" 
			title="<s:text name="note.goToSomewhere" />: <s:property value="getTitle(#target.code, #target.titles)" />">
				<s:if test="%{#rowstatus.index == 0}"><span class="icon fa fa-home"></span>&#32;</s:if>
				<s:property value="getTitle(#target.code, #target.titles)" />
			</a>
	</li>
	</s:iterator>
</ol>