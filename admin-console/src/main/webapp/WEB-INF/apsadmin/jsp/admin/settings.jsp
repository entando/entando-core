<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li class="page-title-container">
		<s:text name="menu.configure" />
	</li>
</ol>
<h1 class="page-title-container">
	<div>
		<s:text name="menu.configure" />
		<span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
			   data-content="TO be inserted" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
	</div>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>

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
			<a href="<s:url action="list" namespace="/do/LocaleString" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-th-list"></span>&#32;
					<s:text name="menu.languageAdmin.labels" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.languageAdmin.labels.keywords" />
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
			<a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName">UserProfileManager</s:param></s:url>" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-chevron-circle-right"></span>&#32;
					<s:text name="menu.accountAdmin.userProfileTypes" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				&nbsp;
			</small>
		</div>
	</wp:ifauthorized>

	<wp:ifauthorized permission="editContents">
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url action="reloadChoose" namespace="/do/BaseAdmin" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-refresh"></span>&#32;
					<s:text name="menu.reload" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.reload.keywords" />
			</small>
		</div>
	</wp:ifauthorized>

	<wp:ifauthorized permission="superuser">
		<%--
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url namespace="/do/BaseAdmin" action="reloadConfig" />" class="btn btn-default btn-lg btn-block">
					<span class="icon fa fa-refresh"></span>&#32;
					<s:text name="menu.reload.config" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.reload.config.keywords" />
			</small>
		</div>
		--%>
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
