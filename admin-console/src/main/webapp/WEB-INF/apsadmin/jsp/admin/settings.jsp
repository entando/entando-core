<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="menu.configure" />
</h1>

<div class="row settings-row">
	<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url namespace="/do/BaseAdmin" action="configSystemParams" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-cogs"></span>&#32;
					<s:text name="menu.settings.general" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.settings.general.keywords" />
			</small>
	</div>

	<wp:ifauthorized permission="superuser">
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url action="list" namespace="/do/User" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-user"></span>&#32;
					<s:text name="menu.accountAdmin.users" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.accountAdmin.users.keywords" />
			</small>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url action="list" namespace="/do/Role" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-users"></span>&#32;
					<s:text name="menu.accountAdmin.roles" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.accountAdmin.roles.keywords" />
			</small>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url action="list" namespace="/do/Lang" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-globe"></span>&#32;
					<s:text name="menu.languageAdmin.languages" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.languageAdmin.languages.keywords" />
			</small>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url action="list" namespace="/do/LocaleString" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-th-list"></span>&#32;
					<s:text name="menu.languageAdmin.labels" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.languageAdmin.labels.keywords" />
			</small>
		</div>
	</wp:ifauthorized>

	<wp:ifauthorized permission="editContents">
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-refresh"></span>&#32;
					<s:text name="menu.reload.contents" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.reload.contents.keywords" />
			</small>
		</div>
	</wp:ifauthorized>

	<wp:ifauthorized permission="superuser">
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url namespace="/do/BaseAdmin" action="reloadConfig" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-refresh"></span>&#32;
					<s:text name="menu.reload.config" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.reload.config.keywords" />
			</small>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url namespace="/do/Admin/Database" action="entry" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-archive"></span>&#32;
					<s:text name="menu.settings.database" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.settings.database.keywords" />
			</small>
		</div>
		<%--
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url namespace="/do/Entity" action="viewManagers" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-cog"></span>&#32;
					<s:text name="menu.entityAdmin" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.entityAdmin.keywords" />
			</small>
		</div>
		--%>
	</wp:ifauthorized>
</div>