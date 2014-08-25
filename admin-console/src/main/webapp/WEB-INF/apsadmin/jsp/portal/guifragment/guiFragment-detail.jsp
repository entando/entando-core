<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/Portal/GuiFragment"></s:url>"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.guiFragmentManagement" />">
		<s:text name="title.guiFragmentManagement" /></a>
		&#32;/&#32;
		<s:text name="title.guiFragmentDetail" />
	</span>
</h1>
<div id="main">
	<div class="table-responsive">
		<table class="table table-bordered">
			<tr>
				<th class="text-right"><s:text name="label.guiFragment" /></th>
				<td><code><s:property value="code" /></code></td>
			</tr>
			<tr>
				<th class="text-right"><s:text name="label.widgetType" /></th>
				<td>
					<s:set value="%{getWidgetType(widgetTypeCode)}" var="widgetTypeVar" />
					<s:property value="getTitle(#widgetTypeVar.code, #widgetTypeVar.titles)" />
				</td>
			</tr>
			<tr>
				<th class="text-right"><s:text name="label.pluginCode" /></th>
				<td><code><s:property value="pluginCode"/></code></td>
			</tr>
		</table>
	</div>
	<p class="text-right">
			<a
				href="<s:url namespace="/do/Portal/GuiFragment" action="edit">
				<s:param name="code"><s:property value="code" /></s:param>
				</s:url>"
				class="btn btn-primary">
				<s:text name="label.edit" />
			</a>
	</p>
		<!--
		<wpsa:hookPoint key="core.groupDetails" objectName="hookPointElements_core_groupDetails">
			<s:iterator value="#hookPointElements_core_groupDetails" var="hookPointElement">
				<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
			</s:iterator>
		</wpsa:hookPoint>
		 -->
	<s:include value="/WEB-INF/apsadmin/jsp/portal/guifragment/include/guiFragmentInfo-references.jsp" />
</div>
