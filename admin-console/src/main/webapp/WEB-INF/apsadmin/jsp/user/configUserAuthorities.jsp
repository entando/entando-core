<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:set var="targetNS" value="%{'/do/User'}" />
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a>
		&#32;/&#32;
		<s:text name="title.userManagement.userAuthorizations" />
	</span>
</h1>

<div id="main" role="main">

<p class="margin-more-bottom">
	<s:text name="note.userAuthorizations.intro" />&#32;<code><s:property value="userAuthsFormBean.username"/></code>, <s:text name="note.userAuthorizations.youCan" />&#32;<a href="#groups"><s:text name="note.userAuthorizations.configureGroups" /></a>&#32;<s:text name="label.or" />&#32;<a href="#roles"><s:text name="note.userAuthorizations.configureRoles" /></a>.
</p>

<s:form action="save" cssClass="form-horizontal">
	<p class="sr-only">
		<wpsf:hidden name="username" value="%{getUserAuthsFormBean().getUsername()}"/>
	</p>

	<div class="panel panel-default margin-none margin-small-bottom">
		<div class="panel-body">
		<h3 class="margin-none margin-base-bottom"><s:text name="note.userAuthorizations.groupList" /></h3>
		<s:if test="%{getUserAuthsFormBean().getGroups().size() > 0}">
			<ul class="list-unstyled">
			<s:iterator id="group" value="userAuthsFormBean.groups">
				<li>
					<wpsa:actionParam action="removeGroup" var="actionName" >
						<wpsa:actionSubParam name="groupName" value="%{#group.name}" />
					</wpsa:actionParam>
					<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
					  <s:property value="descr" />&#32;
						<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') +' '+ descr}" cssClass="btn btn-default btn-xs badge">
							<span class="icon fa fa-times"></span>
							<span class="sr-only">x</span>
						</wpsf:submit>
					</span>
				</li>
			</s:iterator>
			</ul>
		</s:if>
		<s:else><span class="text-info"><s:text name="note.userAuthorizations.groupList.empty" /></span></s:else>
		</div>
	</div>
	<div class="form-group margin-large-bottom">
		<div class="col-xs-12">
			<label for="groupName" ><s:text name="label.group" /></label>
			<div class="input-group">
				<wpsf:select name="groupName" id="groupName" list="groups" listKey="name" listValue="descr" cssClass="form-control" />
				<span class="input-group-btn">
				<wpsf:submit action="addGroup" type="button" cssClass="btn btn-info">
					<span class="icon fa fa-plus"></span>&#32;
					<s:text name="label.add" />
				</wpsf:submit>
				</span>
			</div>
		</div>
	</div>

	<hr />

	<div class="panel panel-default margin-none margin-small-bottom margin-large-top" >
		<div class="panel-body">
		<h3 class="margin-none margin-base-bottom"><s:text name="note.userAuthorizations.roleList" /></h3>
		<s:if test="%{getUserAuthsFormBean().getRoles().size() > 0}">
			<ul class="list-unstyled">
			<s:iterator id="role" value="userAuthsFormBean.roles">
				<li>
					<wpsa:actionParam action="removeRole" var="actionName" >
						<wpsa:actionSubParam name="roleName" value="%{#role.name}" />
					</wpsa:actionParam>
					<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
					  <s:property value="description" />&#32;
						<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') +' '+ description}" cssClass="btn btn-default btn-xs badge">
							<span class="icon fa fa-times"></span>
							<span class="sr-only">x</span>
						</wpsf:submit>
					</span>
				</li>
			</s:iterator>
			</ul>
		</s:if>
		<s:else><span class="text-info"><s:text name="note.userAuthorizations.roleList.empty" /></span></s:else>
		</div>
	</div>

	<div class="form-group">
		<div class="col-xs-12">
			<label for="roleName"><s:text name="name.userRole" />:</label>
			<div class="input-group">
				<wpsf:select name="roleName" id="roleName" list="roles" listKey="name" listValue="description" cssClass="form-control" />
				<span class="input-group-btn">
				<wpsf:submit action="addRole" type="button" cssClass="btn btn-info">
					<span class="icon fa fa-plus"></span>&#32;
					<s:text name="label.add" />
				</wpsf:submit>
				</span>
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.save" />
			</wpsf:submit>
		</div>
	</div>

</s:form>

</div>