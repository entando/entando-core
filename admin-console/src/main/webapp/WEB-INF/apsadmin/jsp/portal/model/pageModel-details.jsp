<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/PageModel"></s:url>"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageModelManagement" />">
		<s:text name="title.pageModelManagement" /></a>
		&#32;/&#32;
		<s:text name="title.pageModelDetail" />
	</span>
</h1>

<div id="main">
	<div class="table-responsive">
		<table class="table table-bordered">
			<tr>
				<th class="text-right"><s:text name="label.description" /></th>
				<td><s:property value="description" /></td>
			</tr>
			<tr>
				<th class="text-right"><s:text name="label.code" /></th>
				<td><code><s:property value="code" /></code></td>
			</tr>
			<tr>
				<th class="text-right"><s:text name="label.pluginCode" /></th>
				<td><code><s:property value="pluginCode" /></code></td>
			</tr>
			<%--
			<tr>
				<th class="text-right"><s:text name="label.configuration" /></th>
				<td><s:property value="xmlConfiguration" /></td>
			</tr>
			--%>
			<tr>
				<th class="text-right"><s:text name="label.template" /></th>
				<td>
					<s:set var="template"><s:property value="template" /></s:set>
					<pre><code><s:property
						value="#template.replaceAll('\t', '&emsp;').replaceAll('\r', '').replaceAll('\n', '<br />')"
						escapeCsv="false"
						escapeHtml="false"
						escapeJavaScript="false"
						escapeXml="false"
					 />
					 </code></pre>
				</td>
			</tr>
		</table>
	</div>
	<p class="text-right">
		<a
			class="btn btn-primary"
			href="<s:url namespace="/do/PageModel" action="edit">
			<s:param name="code"><s:property value="code" /></s:param>
		</s:url>">
			<span class="icon fa fa-edit"></span>&#32;
			<s:text name="label.edit" />
		</a>
	</p>

	<s:include value="/WEB-INF/apsadmin/jsp/portal/model/include/pageModel-references.jsp" />

	<wpsa:hookPoint key="core.pageModelDetails" objectName="hookPointElements_core_pageModelDetails">
		<s:iterator value="#hookPointElements_core_pageModelDetails" var="hookPointElementVar">
			<wpsa:include value="%{#hookPointElementvar.filePath}"></wpsa:include>
		</s:iterator>
	</wpsa:hookPoint>



</div>
