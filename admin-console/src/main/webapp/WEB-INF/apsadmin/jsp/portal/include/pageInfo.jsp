REFACTORING IN CORSO

<%--
<%@ taglib prefix="s" uri="/struts-tags" %>
<p><s:text name="note.youAreHere" />: 

<s:set value="%{getBreadCrumbsTargets(currentPage.code)}" name="breadCrumbsTargets" ></s:set>
<s:iterator value="#breadCrumbsTargets" id="target" status="rowstatus">
<s:if test="%{#rowstatus.index != 0}"> &raquo; </s:if>
<s:if test="%{#rowstatus.index == (#breadCrumbsTargets.size()-1) || !isUserAllowed(#target)}">
<s:property value="getTitle(#target.code, #target.titles)" />
</s:if>
<s:else>
<a href="<s:url namespace="/do/Page" action="viewTree" ><s:param name="selectedNode"><s:property value="#target.code" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:property value="getTitle(#target.code, #target.titles)" />"><s:property value="getTitle(#target.code, #target.titles)" /></a>
</s:else>
</s:iterator>

</p>
<h3><s:text name="title.configPage.youAreDoing" /></h3>

<dl class="dl-horizontal">
	<dt><s:text name="name.pageTitle" /></dt>
		<dd>
<s:iterator value="langs" status="rowStatus">
		<s:if test="#rowStatus.index != 0">, </s:if><span class="monospace">(<abbr title="<s:property value="descr" />"><s:property value="code" /></abbr>)</span> <s:property value="currentPage.getTitles()[code]" />
</s:iterator>
		</dd>
	<dt><s:text name="label.ownerGroup" /></dt>
		<dd><s:property value="systemGroups[currentPage.group].descr" /></dd>
	<dt><s:text name="label.extraGroups" /></dt>
		<dd>
			<s:if test="currentPage.extraGroups.size() != 0">
				<s:iterator value="currentPage.extraGroups" id="groupName" status="groupStatus">
					<s:property value="%{getSystemGroups()[#groupName].getDescr()}"/><s:if test="!#groupStatus.last">,&#32;</s:if> 
				</s:iterator>
			</s:if>
			<s:else>
				<abbr title="<s:text name="note.viewOnlyGroups.notAvailable" />">&ndash;</abbr>
			</s:else>
		</dd>
	<dt><s:text name="name.pageModel" /></dt>
		<dd><s:property value="currentPage.getModel().getDescr()" /></dd>		
</dl>

 --%>