<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<p class="sr-only"><s:text name="note.youAreHere" /></p>

<s:set value="%{getBreadCrumbsTargets(#breadcrumbs_pivotPageCode)}" var="breadCrumbsTargets" ></s:set>
<ol class="breadcrumb margin-base-vertical">
<s:iterator value="#breadCrumbsTargets" var="target" status="rowstatus">
	<li>
<%--	<s:if test="%{#rowstatus.index != 0}">&#32;/&#32;</s:if> --%>
	<s:if test="%{!isUserAllowed(#target)}">
		<s:property value="getTitle(#target.code, #target.titles)" />
	</s:if>
	<s:else>
		<a href="<s:url action="viewTree" ><s:param name="selectedNode"><s:property value="#target.code" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:property value="getTitle(#target.code, #target.titles)" />">
			<s:if test="%{#rowstatus.index == 0}"><span class="icon fa fa-home"></span>&#32;</s:if>
			<s:property value="getTitle(#target.code, #target.titles)" />
		</a>
		<wpsa:page key="%{#target.code}" property="void" var="isVoid" />
		<s:if test="%{!#isVoid && isComponentInstalled('entando-portal-ui')}">
			<a href="<wp:info key="systemParam" paramName="applicationBaseURL" /><s:property value="currentLang.code"/>/<s:property value="#target.code" />.page"
				title="<s:text name="note.goToPortal" />: <s:property value="getTitle(#target.code, #target.titles)" />">
				<span class="icon fa fa-globe"></span>
			</a>
		</s:if>
	</s:else>
	</li>
</s:iterator>
</ol>
