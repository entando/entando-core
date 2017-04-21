<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure" /></li>
	<li class="page-title-container"><s:text
			name="title.groupManagement" /></li>
</ol>
<h1 class="page-title-container">
	<div>
		<s:text name="title.groupManagement" />
		<span class="pull-right"> <a tabindex="0" role="button"
			data-toggle="popover" data-trigger="focus" data-html="true" title=""
			data-content="TO be inserted" data-placement="left"
			data-original-title=""> <i class="fa fa-question-circle-o"
				aria-hidden="true"></i>
		</a>
		</span>
	</div>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>
<s:if test="hasActionErrors()">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert" aria-hidden="true">
			<span class="pficon pficon-close"></span>
		</button>
		<strong><s:text name="messages.title.ActionErrors" /></strong>
		<ul class="margin-base-top">
			<s:iterator value="actionErrors">
				<li><s:property escapeHtml="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<div class="row form-group">
	<div class="col-sm-12">
		<a class="btn btn-primary pull-right"
			href="<s:url namespace="/do/Group" action="new" />"> <s:text
				name="title.groupManagement.groupNew" />
		</a>
	</div>
</div>
<s:if test="%{groups.size > 0}">
	<div>
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th><s:text name="label.name" /></th>
					<th><s:text name="label.code" /></th>
					<th><s:text name="label.actions" /></th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="groups" var="group">
					<tr>
						<td><s:property value="#group.descr" /></td>
						<td><s:property value="#group.name" /></td>
						<td class="table-view-pf-actions">
							<div class="dropdown dropdown-kebab-pf">
								<button class="btn btn-menu-right dropdown-toggle" type="button"
									data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="false">
									<span class="fa fa-ellipsis-v"></span>
								</button>
								<ul class="dropdown-menu dropdown-menu-right">
									<li><a
										title="<s:text name="title.groupDetail" />: <s:property value="#group.name" />"
										href="<s:url action="detail"><s:param name="name" value="#group.name"/></s:url>">
											<span><s:text name="title.groupDetail" /></span>
									</a></li>
									<li><a
										title="<s:text name="label.edit" />:&#32;<s:property value="#group.name" />"
										href="<s:url action="edit"><s:param name="name" value="#group.name"/></s:url>">
											<span><s:text name="label.edit" /></span>
									</a></li>
									<li><a
										title="<s:text name="label.remove" />: <s:property value="#group.name" />"
										href="<s:url action="trash"><s:param name="name" value="#group.name"/></s:url>">
											<span><s:text name="label.remove" /></span>
									</a></li>
								</ul>
							</div>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
</s:if>
<s:else>
	<p>
		<s:text name="noGroups.found" />
	</p>
</s:else>
