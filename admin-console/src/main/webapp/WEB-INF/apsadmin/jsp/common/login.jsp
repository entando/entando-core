<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<!DOCTYPE html>

<html lang="en">
<head>

	<title>Entando - Sign in</title>

	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta charset="utf-8" />

	<link rel="stylesheet" href="<wp:resourceURL />administration/bootstrap/css/bootstrap.min.css" media="screen" />
	<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-theme-entando-eee/css/bootstrap.min.css" media="screen" />


	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
		<script src="<wp:resourceURL />administration/js/html5shiv.js"></script>
		<script src="<wp:resourceURL />administration/js/respond.min.js"></script>
	<![endif]-->

</head>
<body>

<div class="container margin-large-top">

	<div class="row margin-large-top padding-small-top">
		<div class="col-sm-6 col-sm-offset-3 col-md-offset-3 col-lg-offset-3">

		<div class="panel panel-default">
			<div class="panel-body">

				<h1 class="margin-small-bottom text-center">
					<img src="<wp:resourceURL />administration/img/entando-logo-134x100.png" alt="Entando - Simplifying Enterprise Portals" width="134" height="100" />
				</h1>

				<s:form action="doLogin" cssClass="padding-base-vertical">

				<s:if test="hasActionErrors()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
					<s:if test="hasActionErrors()">
					<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
					<ul class="margin-base-vertical">
						<s:iterator value="actionErrors">
						<li><s:property /></li>
						</s:iterator>
					</ul>
					</s:if>
				</div>
				</s:if>

		 		<s:if test="#session.currentUser != null && #session.currentUser.username != 'guest'">

					<h2 class="text-center margin-base-bottom"><s:text name="note.userbar.welcome"/>&#32;<s:property value="#session.currentUser" />!</h2>

					<s:if test="!#session.currentUser.credentialsNotExpired">

						<div class="alert alert-warning">
							<strong><s:text name="note.login.expiredPassword.intro" /></strong><br />
							<div class="text-center margin-base-top">
								<a href="<s:url action="editPassword" />" class="btn btn-lg btn-warning">
									<s:text name="note.login.expiredPassword.outro" />
								</a>
							</div>
						</div>

					</s:if>

				<s:else>
				<wp:ifauthorized permission="enterBackend" var="checkEnterBackend" />

				<c:choose>
					<c:when test="${checkEnterBackend}">

					<div class="alert alert-info">
						<strong><s:text name="note.login.yetLogged" /></strong><br />
						<div class="text-center margin-base-top">
							<div class="btn-group btn-group-justified">
								<a href="<s:url action="main" />" class="btn btn-primary">
									<s:text name="note.goToMain" />
								</a>
								<a href="<s:url action="logout" namespace="/do" />" class="btn btn-danger">
									<s:text name="menu.exit"/>
								</a>
							</div>
						</div>
					</div>
					</c:when>
					<c:otherwise>

					<div class="alert alert-danger">
						<strong><s:text name="note.login.notAllowed" /></strong><br />
						<div class="text-center margin-base-top">
							<a href="<s:url action="logout" namespace="/do" />" class="btn btn-lg btn-danger">
								<s:text name="menu.exit"/>
							</a>
						</div>
					</div>

					</c:otherwise>
				</c:choose>

				</s:else>

				</div>
				</s:if>

				<s:else>

					<s:set var="usernameFieldErrorsVar" value="%{fieldErrors['username']}" />
					<s:set var="usernameHasFieldErrorVar" value="#usernameFieldErrorsVar != null && !#usernameFieldErrorsVar.isEmpty()" />
					<s:set var="controlGroupErrorClassVar" value="''" />

					<s:if test="#usernameHasFieldErrorVar">
						<s:set var="controlGroupErrorClassVar" value="' has-error'" />
					</s:if>

					<div class="margin-base-vertical form-group<s:property value="controlGroupErrorClassVar" />">
						<label for="username" class="sr-only"><s:text name="label.username" /></label>
						<div class="input-group">
							<div class="input-group-addon"><span class="icon fa fa-user"></span></div>
							<wpsf:textfield name="username" id="username" cssClass="form-control input-lg" placeholder="%{getText('label.username')}" />
						</div>
						<s:if test="#usernameHasFieldErrorVar">
							<p class="text-danger padding-small-vertical"><s:iterator value="#usernameFieldErrorsVar"><s:property /> </s:iterator></p>
						</s:if>
					</div>

					<s:set var="passwordFieldErrorsVar" value="%{fieldErrors['password']}" />
					<s:set var="passwordHasFieldErrorVar" value="#passwordFieldErrorsVar != null && !#passwordFieldErrorsVar.isEmpty()" />
					<s:set var="controlGroupErrorClassVar" value="''" />

					<s:if test="#passwordHasFieldErrorVar">
						<s:set var="controlGroupErrorClassVar" value="' has-error'" />
					</s:if>

					<div class="margin-base-vertical form-group<s:property value="controlGroupErrorClassVar" />">
						<label for="password" class="sr-only"><s:text name="label.password" /></label>
						<div class="input-group">
							<div class="input-group-addon"><span class="icon fa fa-lock"></span></div>
							<wpsf:password name="password" id="password" cssClass="form-control input-lg" placeholder="%{getText('label.password')}" />
						</div>
						<s:if test="#passwordHasFieldErrorVar">
							<p class="text-danger padding-small-vertical"><s:iterator value="#passwordFieldErrorsVar"><s:property /> </s:iterator></p>
						</s:if>
					</div>

					<div class="text-center margin-base-vertical">
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-default active">
								<input type="radio" name="request_locale" value="en" checked="checked" /> English
							</label>
							<label class="btn btn-default">
								<input type="radio" name="request_locale" value="it" /> Italiano
							</label>
						</div>
					</div>

					<div class="row">
						<p class="padding-base-top col-sm-8 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">
								<wpsf:submit type="button" cssClass="btn btn-primary btn-lg btn-block">
									<span class="icon fa fa-sign-in"></span>&#32;
									<s:text name="label.signin" />
								</wpsf:submit>
						</p>
					</div>

				</s:else>
				</s:form>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<wp:resourceURL />administration/js/jquery-1.9.1.min.js"></script>
<script src="<wp:resourceURL />administration/bootstrap/js/bootstrap.js"></script>
<script>
	$(function(){
		try { document.getElementById('username').focus(); } catch(e) {}
	});
</script>

</body>
</html>
