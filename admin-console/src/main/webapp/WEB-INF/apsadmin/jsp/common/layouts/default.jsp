<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<!DOCTYPE html>
<html lang="<s:property value="currentLang.code" />">
<head>

	<title>Entando - <s:set var="documentTitle"><tiles:getAsString name="title"/></s:set><s:property value="%{getText(#documentTitle)}" escape="false" /></title>

	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta charset="utf-8" />

	<link rel="stylesheet" href="<wp:resourceURL />administration/bootstrap/css/bootstrap.min.css" media="screen" />
		<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-theme-entando-eee/css/bootstrap.min.css" media="screen" />
	<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-override.css" media="screen" />
	<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-offcanvas.css" media="screen" />
	<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-addendum.css" media="screen" />
	<c:catch>
		<tiles:insertAttribute name="cssSpecial" ignore="true" />
	</c:catch>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
	<script src="<wp:resourceURL />administration/js/html5shiv.js"></script>
	<script src="<wp:resourceURL />administration/js/respond.min.js"></script>
<![endif]-->

	<script src="<wp:resourceURL />administration/js/jquery-1.9.1.min.js"></script>
	<script src="<wp:resourceURL />administration/bootstrap/js/bootstrap.js"></script>
	<script src="<wp:resourceURL />administration/js/bootstrap-offcanvas.js"></script>
	<script src="<wp:resourceURL />administration/js/shortcuts.js"></script>

	<wp:info key="systemParam" paramName="firstTimeMessages" var="firstTimeMessagesVar" />
	<c:if test="${firstTimeMessagesVar eq true}">
		<script>
			var Entando = Entando||{};
			Entando.info = Entando.type || {};
			Entando.info.applicationBaseURL = "<wp:info key="systemParam" paramName="applicationBaseURL" />";
		</script>
		<script src="<wp:resourceURL />administration/js/first-time-messages.js"></script>
	</c:if>

	<tiles:insertAttribute name="extraResources"/>
</head>
<body>

	<div class="container" id="container-main">

	<%--
	<tiles:insertAttribute name="shortcuts-quickbar"/>
	--%>

	<p class="sr-only" id="fagiano_start"><s:text name="title.mainFunctions" /></p>
	<div class="row row-offcanvas row-offcanvas-right">
		<div class="col-sm-3 sidebar-offcanvas col-sm-push-9 col-md-push-9 col-lg-push-9" id="sidebar">
			<div class="panel-group margin-base-bottom" role="navigation">
				<div class="panel panel-default">
					<div class="panel-heading" id="user-avatar">
						<a data-toggle="collapse" href="#submenu-user" class="display-block">
							<c:set var="current_username" value="${sessionScope.currentUser}" />
							<c:if test="${null != sessionScope.currentUser.profile}">
								<c:set var="current_username" value="${sessionScope.currentUser.profile.displayName}" />
							</c:if>
							<c:out value="${current_username}" />
							<img src="<s:url action="avatarStream" namespace="/do/currentuser/avatar">
							<s:param name="gravatarSize">34</s:param>
						</s:url>" width="34" height="34" alt=" " class="img-rounded" />
						</a>
					</div>
					<div id="submenu-user" class="panel-collapse collapse">
						<ul class="panel-body nav nav-pills nav-stacked">
							<c:if test="${sessionScope.currentUser.japsUser}">
							<li>
								<a href="<s:url action="editProfile" namespace="/do/CurrentUser" />">
									<span class="icon fa fa-user fa-fw"></span>&#32;
									<s:text name="note.changeYourPassword" />
								</a>
							</li>
							</c:if>
							<li>
								<a href="<s:url value="/" />" title="<s:text name="note.goToPortal" /> ( <s:text name="note.sameWindow" /> )">
									<span class="icon fa fa-globe fa-fw"></span>&#32;
									<s:text name="note.goToPortal" />
								</a>
							</li>
							<li>
								<a href="<s:url action="logout" namespace="/do" />">
									<span class="icon fa fa-power-off fa-fw"></span>&#32;
									<s:text name="menu.exit" />
								</a>
							</li>
						</ul>
					</div>
				</div>
			</div>

			<%-- shortcuts --%>
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/menu-shortcuts.jsp" />

			<nav role="navigation">
				<tiles:insertAttribute name="menu"/>
				<p class="sr-only">
					<a href="#fagiano_start" id="fagiano_mainContent"><s:text name="note.backToStart" /></a>
				</p>
			</nav>

			<c:if test="${firstTimeMessagesVar}">
				<div class="alert alert-info margin-none margin-base-top fade in" id="first-time-messages">
					<button class="close" data-dismiss="alert" data-first-time-messages="dismiss"><span class="icon fa fa-times"></span></button>
					<p class="margin-none">
						<s:text name="label.firstTimeMessages.moreFeatures" /><br />
						<a class="alert-link" href="<s:url action="configSystemParams" namespace="/do/BaseAdmin" />#additional-features">
							<s:text name="label.firstTimeMessages.goActivateFeatures" />&#32;<span class="icon fa fa-arrow-right"></span>
						</a>
					</p>
				</div>
				<div class="alert alert-warning margin-none margin-base-top fade out hide" id="first-time-messages-undo">
					<a class="alert-link" href="#" data-first-time-messages="dismiss" data-dismiss="alert"><s:text name="label.firstTimeMessages.undo" /></a>&#32;<s:text name="label.firstTimeMessages.ifYouMissed" />&#32;<a class="alert-link" href="<s:url action="configSystemParams" namespace="/do/BaseAdmin" />#additional-features"><code><s:text name="label.firstTimeMessages.settingsGeneral" /></code></a>
				</div>
			</c:if>
		</div>
		<div class="col-sm-9 col-sm-pull-3 col-md-pull-3 col-lg-pull-3" id="container-content">
			<div class="navbar navbar-default navbar-static-top" id="navbar" role="banner">
				<div class="navbar-header">
					<a href="#sidebar" class="btn-offcanvas navbar-toggle pull-right visible-xs" data-toggle="offcanvas">
						<span class="sr-only"><s:text name="label.menu" /></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</a>
					<a class="navbar-brand" href="<s:url action="main" namespace="/do" />"><img src="<wp:resourceURL />administration/img/entando-logo-symbol-70x42.png" alt="Entando - Simplifying Enterprise Portals" width="70" height="42" /></a>
				</div>
			</div>
			<tiles:insertAttribute name="body"/>
		</div>
		<div class="clearfix"></div>
		<div class="col-sm-12 margin-large-top">
			<ul class="sr-only">
				<li><a href="#fagiano_mainContent"><s:text name="note.backToMainContent" /></a></li>
				<li><a href="#fagiano_start"><s:text name="note.backToStart" /></a></li>
			</ul>
			<div class="text-center" role="contentinfo">
				<tiles:insertAttribute name="footer"/>
			</div>
		</div>
		</div>

	</div>
</body>
</html>
