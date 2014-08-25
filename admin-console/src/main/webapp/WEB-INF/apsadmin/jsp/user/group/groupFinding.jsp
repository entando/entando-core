<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.groupManagement" />
	</span>
</h1>
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
<a
	class="btn btn-default margin-base-bottom"
	href="<s:url namespace="/do/Group" action="new" />">
		<span class="icon fa fa-plus-circle">
			&#32;
			<s:text name="title.groupManagement.groupNew" />
		</span>
</a>
	<s:if test="%{groups.size > 0}">
		<div class="table-responsive">
			<table class="table table-bordered">
			<tr>
				<th class="text-center text-nowap col-xs-6 col-sm-3 col-md-3 col-lg-3 ">
					<s:text name="label.actions" />
				</th>
				<th><s:text name="label.group" /></th>
				<th><s:text name="label.description" /></th>
			</tr>
			<s:iterator value="groups" var="group">
			<tr>
				<td class="text-center text-nowrap">
					<div class="btn-group btn-group-xs">
						<a 
							class="btn btn-default"
							href="<s:url action="detail"><s:param name="name" value="#group.name"/></s:url>" 
							title="<s:text name="note.detailsFor" />: <s:property value="#group.name" />">
								<span class="icon fa fa-info"></span>
								<span class="sr-only"><s:text name="note.detailsFor" />: <s:property value="#group.name" /></span>
						</a>
						<button type="submit" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu text-left" role="menu">
								<li>
									<a 
										href="<s:url action="edit"><s:param name="name" value="#group.name"/></s:url>" 
										title="<s:text name="label.edit" />:&#32;<s:property value="#group.name" />"
										>
										<span class="icon fa fa-pencil-square-o fa-fw"></span>
										<s:text name="label.edit" />
									</a>
								</li>
								<li>
									<a 
										href="<s:url namespace="/do/Group/Auth" action="config"><s:param name="authName" value="#group.name"/></s:url>" 
										title="<s:text name="note.manageUsersFor" />: <s:property value="#group.name" />">
											<span class="icon fa fa-users fa-fw"></span>
											<s:text name="note.manageUsersFor" />
										</a>
								</li>
							</ul>
					</div>
					<div class="btn-group btn-group-xs">
						<a 
							class="btn btn-warning"
							href="<s:url action="trash"><s:param name="name" value="#group.name"/></s:url>"  
							title="<s:text name="label.remove" />: <s:property value="#group.name" />">
								<span class="sr-only"><s:text name="label.remove" />: <s:property value="#group.name" /></span>
								<span class="icon fa fa-times-circle-o"></span>
							</a>
					</div>
				</td>
				<td>
					<code><s:property value="#group.name" /></code>
				</td>
				<td>
					<s:property value="#group.descr" />
				</td>
			</tr>
			</s:iterator>
			</table>
		</div>
	</s:if>
	<s:else>
		<p>
			<s:text name="noGroups.found" />
		</p>
	</s:else>