<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.pageModelManagement" />
	</span>
</h1>

<div id="main">

	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.title.ActionErrors" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
					</s:iterator>
			</ul>
		</div>
	</s:if>
	<p>
		<a class="btn btn-default margin-base-bottom"
		   href="<s:url namespace="/do/PageModel" action="new" />">
			<span class="icon fa fa-plus-circle">
				&#32;
				<s:text name="title.pageModelManagement.pageModelNew" />
			</span>
		</a>
	</p>
	<s:set var="pageModelsVar" value="pageModels" />
	<s:if test="%{#pageModelsVar.size > 0}">
		<div class="table-responsive">
			<table class="table table-bordered">
				<tr>
					<th class="text-center text-nowap col-xs-6 col-sm-3 col-md-3 col-lg-3 "><s:text name="label.actions" /></th>
						<%-- <th><s:text name="label.code" /></th> --%>
					<th><s:text name="label.description" /></th>
				</tr>
				<s:iterator value="#pageModelsVar" var="pageModelVar">
					<tr>
						<td class="text-center text-nowrap">
							<div class="btn-group btn-group-xs">
								<a class="btn btn-default"
									href="<s:url action="edit"><s:param name="code" value="#pageModelVar.code"/></s:url>"
									title="<s:text name="label.edit" />:&#32;<s:property value="#pageModelVar.description" />&#32;(<s:property value="#pageModelVar.code" />)" >
									<span class="icon fa fa-pencil-square-o fa-fw"></span>
									<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#pageModelVar.description" /></span>
								</a>
								<a class="btn btn-default"
									href="<s:url action="details"><s:param name="code" value="#pageModelVar.code"/></s:url>"
									title="<s:text name="note.detailsFor" />:&#32;<s:property value="#pageModelVar.description" />&#32;(<s:property value="#pageModelVar.code" />)" >
									<span class="icon fa fa-info"></span>
									<span class="sr-only"><s:text name="note.detailsFor" />:&#32;<s:property value="#pageModelVar.description" /></span>
								</a>
								<wpsa:hookPoint key="core.pageModel.list.action" objectName="hookPointElements_core_pageModel_list_actionVar">
								<s:iterator value="#hookPointElements_core_pageModel_list_actionVar" var="hookPointElementVar">
									<wpsa:include value="%{#hookPointElementVar.filePath}"></wpsa:include>
								</s:iterator>
								</wpsa:hookPoint>
							</div>
							<div class="btn-group btn-group-xs">
								<a
									class="btn btn-warning"
									href="<s:url action="trash"><s:param name="code" value="#pageModelVar.code"/></s:url>"
									title="<s:text name="label.remove" />: <s:property value="#pageModelVar.code" />">
									<span class="sr-only"><s:text name="label.remove" />: <s:property value="#pageModelVar.description" /></span>
									<span class="icon fa fa-times-circle-o"></span>
								</a>
							</div>
						</td>
						<%--
						<td>
							<code><s:property value="#pageModelVar.code" /></code>
						</td>
						--%>
						<td>
							<s:property value="#pageModelVar.description" />
						</td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</s:if>
	<s:else>
		<p>
			<s:text name="noPageModels.found" />
		</p>
	</s:else>

</div>
