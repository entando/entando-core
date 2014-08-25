<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<s:text name="title.roleManagement" />
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
	href="<s:url namespace="/do/Role" action="new" />">
		<span class="icon fa fa-plus-circle">
			&#32;
			<s:text name="title.roleManagement.roleNew" />
		</span>
</a>
<div class="table-responsive">
	<table class="table table-bordered">
		<tr>
			<th class="text-center text-nowap col-xs-6 col-sm-3 col-md-3 col-lg-3 "><s:text name="label.actions" /></th>
			<th><s:text name="name.userRoles" /></th>
			<th><s:text name="label.description" /></th>
		</tr>
		<s:iterator value="roles" var="role">
			<tr>
				<td class="text-center text-nowrap">
					<div class="btn-group btn-group-xs">
						<a 
							class="btn btn-default" 
							title="<s:text name="label.edit" />:&#32;<s:property value="#role.name" />" 
							href="<s:url action="edit"><s:param name="name" value="#role.name"/></s:url>">
								<span class="icon fa fa-pencil-square-o"></span>
								<span class="sr-only"><s:text name="label.edit" />:&#32;<s:property value="#role.name" /></span>
						</a>
						<a 
							class="btn btn-default" 
							title="<s:text name="note.assignToUsers" />:&#32;<s:property value="#role.name" />" 
							href="<s:url namespace="/do/Role/Auth" action="config"><s:param name="authName" value="#role.name"/></s:url>">
								<span class="icon fa fa-users"></span>
								<span class="sr-only"><s:text name="note.assignToUsers" />:&#32;<s:property value="#role.name" /></span>
						</a>
					</div>
					<div class="btn-group btn-group-xs">
						<a 
							class="btn btn-warning" 
							title="<s:text name="label.remove" />: <s:property value="#role.name" />" 
							href="<s:url action="trash"><s:param name="name" value="#role.name"/></s:url>">
								<span class="icon fa fa-times-circle-o" />
								<span class="sr-only"><s:text name="label.remove" />: <s:property value="#role.name" /></span>
						</a>
					</div>
				</td>
				<td><code><s:property value="#role.name" /></a></td>
			 	<td><s:property value="#role.description" /></td>
			</tr>
		</s:iterator>
	</table>
</div>