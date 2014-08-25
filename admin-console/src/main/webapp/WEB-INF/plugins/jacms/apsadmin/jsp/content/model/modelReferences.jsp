<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/jacms/ContentModel" />">
		<s:text name="title.contentModels" /></a>
		&#32;/&#32;
		<s:text name="title.contentModels.remove" />
	</span>
</h1>

<div id="main" role="main">
<div class="alert alert-danger alert-dismissable fade in">
	<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
	<h2 class="h4 margin-none">
		<s:text name="message.title.ActionErrors" />
	</h2>
	<p class="margin-base-vertical">
		<p><s:text name="contentModel.tip" /></p>
	</p>
</div>

<div class="panel panel-default">
<div class="panel-heading"><h3 class="margin-none"><s:text name="title.contentModels.references" /></h3></div>
<div class="panel-body">
<table class="table table-bordered">
	<tr>
		<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
		<th>Page</th>
		<th>Content</th>
	</tr>
	<s:iterator id="contentId" value="referencedContentsOnPages">
		<s:iterator id="page" value="referencingPages[#contentId]">
			<tr>
				<s:set name="pageGroup" value="#page.group"></s:set>
				<wp:ifauthorized groupName="${pageGroup}">
					<td class="text-center text-nowrap">
						<div class="btn-group btn-group-xs">
							<a class="btn btn-default" href="<s:url action="new" namespace="/do/Page"/>?selectedNode=<s:property value="#page.code" />&amp;action:configure=true">
								<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#model.description" /></span>
								<span class="icon fa fa-cog"></span>
							</a>
						</div>
					</td>
				</wp:ifauthorized>
				<td>
					<s:property value="#page.titles[currentLang.code]" />
				</td>
				<td>
					<s:set name="content" value="%{getContentVo(#contentId)}"></s:set>
					<code><s:property value="#content.id" /></code>&#32;<s:property value="#content.descr" />
				</td>
			</tr>
		</s:iterator>
	</s:iterator>
</table>
</div>
</div>
</div>