<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<p class="margin-base-vertical text-right">
	<button type="button" data-toggle="collapse" data-target="#page-info"  class="btn btn-link">
		<s:text name="label.info" />&#32;
		<span class="icon fa fa-chevron-down"></span>
	</button>
</p>

<s:set var="details_pivotPage" value="pageToShow" />
<div class="collapse" id="page-info">

	<table class="table table-bordered">
		<tr>
			<th class="text-right"><s:text name="name.pageCode" /></th>
			<td><s:property value="#details_pivotPage.code" /></td>
		</tr>
		<tr>
			<th class="text-right"><s:text name="name.pageTitle" /></th>
			<td>
	<s:iterator value="langs" status="pageInfo_rowStatus" var="lang">
			<s:if test="#pageInfo_rowStatus.index != 0">, </s:if><span class="monospace">(<abbr title="<s:property value="descr" />"><s:property value="code" /></abbr>)</span> <s:property value="#details_pivotPage.getTitles()[#lang.code]" />
	</s:iterator>
			</td>
		</tr>
		<tr>
			<th class="text-right"><s:text name="label.ownerGroup" /></th>
			<td><s:property value="systemGroups[#details_pivotPage.group].descr" /></td>
		</tr>
		<tr>
			<th class="text-right"><s:text name="label.extraGroups" /></th>
			<td>
				<s:if test="#details_pivotPage.extraGroups.size() != 0">
					<s:iterator value="#details_pivotPage.extraGroups" id="groupName" status="groupStatus">
						<s:property value="systemGroups[#groupName].descr"/><s:if test="!#groupStatus.last">,&#32;</s:if>
					</s:iterator>
				</s:if>
				<s:else>
					<abbr title="<s:text name="note.viewOnlyGroups.notAvailable" />">&ndash;</abbr>
				</s:else>
			</td>
		</tr>
		<tr>
			<th class="text-right"><s:text name="name.pageModel" /></th>
			<td><s:property value="#details_pivotPage.model.descr" /></td>
		</tr>

	<s:set var="freeViewerPage" ><s:property value="{isFreeViewerPage(#details_pivotPage)}" /></s:set>
	<s:if test="#freeViewerPage.equals('[true]')">
		<s:set var="iconName">check-square-o</s:set>
		<s:set var="freeViewerPageBooleanStatus" value="%{getText('label.yes')}" />
	</s:if>
	<s:elseif test="#freeViewerPage.equals('[false]')" >
		<s:set var="iconName">square-o</s:set>
		<s:set var="freeViewerPageBooleanStatus" value="%{getText('label.no')}" />
	</s:elseif>

		<tr>
			<th class="text-right"><s:text name="name.isViewerPage" /></th>
			<td><span title="<s:property value="freeViewerPageBooleanStatus" />" class="icon fa fa-<s:property value="#iconName" />"></span></td>
		</tr>

	<s:if test="#details_pivotPage.showable">
		<s:set var="iconName">check-square-o</s:set>
		<s:set var="booleanStatus" value="%{getText('label.yes')}" />
	</s:if>
	<s:else>
		<s:set var="iconName">square-o</s:set>
		<s:set var="booleanStatus" value="%{getText('label.no')}" />
	</s:else>

		<tr>
			<th class="text-right"><s:text name="name.isShowablePage" /></th>
			<td><span title="<s:property value="booleanStatus" />" class="icon fa fa-<s:property value="#iconName" />"></span></td>
		</tr>

	<s:if test="#details_pivotPage.useExtraTitles">
		<s:set var="iconName">check-square-o</s:set>
		<s:set var="useExtraTitlesBooleanStatus" value="%{getText('label.yes')}" />
	</s:if>
	<s:else>
		<s:set var="iconName">square-o</s:set>
		<s:set var="useExtraTitlesBooleanStatus" value="%{getText('label.no')}" />
	</s:else>

		<tr>
			<th class="text-right"><abbr lang="en" title="<s:text name="name.SEO.full" />"><s:text name="name.SEO.short" /></abbr>:&#32;<s:text name="name.useBetterTitles" /></th>
			<td><span title="<s:property value="useExtraTitlesBooleanStatus" />" class="icon fa fa-<s:property value="#iconName" />"></span></td>
		</tr>

	</table>

</div>