<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head>
	
	<title>Entando - Sign in</title>

	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta charset="utf-8" />

	<link rel="stylesheet" href="<wp:resourceURL />administration/bootstrap/css/bootstrap.min.css" media="screen" />
	<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-theme-entando-ce/css/bootstrap.min.css" media="screen" />
	
	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
		<script src="<wp:resourceURL />administration/js/html5shiv.js"></script>
		<script src="<wp:resourceURL />administration/js/respond.min.js"></script>
	<![endif]-->
	
</head>
<body>

<%--
<h1 class="raw centerText">Entando &ndash; OAuth login form</h1>
<h3 class="raw centerText">"<c:out value="${oauthParam_CONSUMER_DESCRIPTION}" />" is trying to access your information.</h3>
    
    <c:if test="${oauthParam_INVALID_CREDENTIALS}" >
        <div id="actionErrorsBox" class="message message_error">
            <h2>Error:</h2>
            <ul>
                <li>Invalid credentials</li>
            </ul>
	</div>
    </c:if>
--%>

<div class="container margin-large-top">
	<div class="row margin-large-top padding-small-top">
		<div class="col-sm-6 col-sm-offset-3 col-md-offset-3 col-lg-offset-3">
		<div class="panel panel-default">
			<div class="panel-body">
				<h1 class="margin-small-bottom text-center">
					<img src="<wp:resourceURL />administration/img/entando-logo-134x100.png" alt="Entando - Access. Build. Connect." width="134" height="100" />
				</h1>
				<c:if test="${oauthParam_INVALID_CREDENTIALS}" >
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
					<h2 class="h4 margin-none">Error</h2>
					<ul class="margin-base-vertical">
						<li>Invalid credentials</li>
					</ul>
				</div>
				</c:if>
				<c:if test="${null != sessionScope.currentUser && sessionScope.currentUser != 'guest'}">
<form action="<wp:info key="systemParam" paramName="applicationBaseURL" />OAuth/authorize" method="post" cssClass="padding-base-vertical">
<h2 class="text-center margin-base-bottom"><wp:i18n key="WELCOME" />&#32;<c:out value="${sessionScope.currentUser}"/></h2>
<input type="hidden" name="loggedUser" value="true" />
<input type="hidden" name="oauth_token" value="<c:out value="${oauthParam_REQUEST_TOKEN}" />" />
<input type="hidden" name="oauth_callback" value="<c:out value="${oauthParam_CALLBACK_URL}" />" />
<div class="row">
	<p class="padding-base-top col-sm-8 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">
		<button type="submit" value="Authorize" class="btn btn-primary btn-lg btn-block">
			<span class="icon fa fa-sign-in"></span>&#32;Authorize
		</button>
	</p>
</div>
<p>or login with other user</p>
</form>
				</c:if>
<form action="<wp:info key="systemParam" paramName="applicationBaseURL" />OAuth/authorize" method="post" cssClass="padding-base-vertical">
<div class="margin-base-vertical form-group">
<label for="username" class="sr-only">Username</label>
	<div class="input-group">
		<div class="input-group-addon"><span class="icon fa fa-user"></span></div>
		<input type="text" name="username" value="<c:out value="${oauthParam_USERNAME}" />" id="username" class="form-control input-lg" placeholder="Username"/>
	</div>
</div>
<%--
<p><label for="username">Username:</label><br />
<input type="text" name="username" id="username" size="20" class="text" value="<c:out value="${oauthParam_USERNAME}" />" /></p>
--%>
<div class="margin-base-vertical form-group">
	<label for="password" class="sr-only">Password</label>
	<div class="input-group">
		<div class="input-group-addon"><span class="icon fa fa-lock"></span></div>
		<input type="password" name="password" id="password" class="form-control input-lg" placeholder="Password"/>
	</div>
</div>
<%--
<p><label for="password">Password:</label><br />
<input type="password" name="password" id="password" size="20" class="text" /></p>
--%>
<input type="hidden" name="oauth_token" value="<c:out value="${oauthParam_REQUEST_TOKEN}" />" />
<input type="hidden" name="oauth_callback" value="<c:out value="${oauthParam_CALLBACK_URL}" />" />
<div class="row">
	<p class="padding-base-top col-sm-8 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">
		<button type="submit" value="Authorize" class="btn btn-primary btn-lg btn-block">
			<span class="icon fa fa-sign-in"></span>&#32;Authorize
		</button>
	</p>
</div>
</form>
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

<%--

<h1 class="raw centerText">Entando &ndash; OAuth login form</h1>
<h3 class="raw centerText">"<c:out value="${oauthParam_CONSUMER_DESCRIPTION}" />" is trying to access your information.</h3>
    
    <c:if test="${oauthParam_INVALID_CREDENTIALS}" >
        <div id="actionErrorsBox" class="message message_error">
            <h2>Error:</h2>
            <ul>
                <li>Invalid credentials</li>
            </ul>
	</div>
    </c:if>
    
<div class="login">

<fieldset id="fieldset_space"><legend>Login</legend>

<c:if test="${null != sessionScope.currentUser && sessionScope.currentUser != 'guest'}">
<form action="<wp:info key="systemParam" paramName="applicationBaseURL" />OAuth/authorize" method="post">
<p><wp:i18n key="WELCOME" />, <em><c:out value="${sessionScope.currentUser}"/></em>!</p>
<p class="centerText">
    <input type="hidden" name="loggedUser" value="true" />
    <input type="hidden" name="oauth_token" value="<c:out value="${oauthParam_REQUEST_TOKEN}" />" />
    <input type="hidden" name="oauth_callback" value="<c:out value="${oauthParam_CALLBACK_URL}" />" />
    <input class="button uppercase" type="submit" name="Authorize" value="Continue"/>
</p>
<p>
    or login with other user
</p>
</form>
</c:if>

<form action="<wp:info key="systemParam" paramName="applicationBaseURL" />OAuth/authorize" method="post">
    <p><label for="username">Username:</label><br />
    <input type="text" name="username" id="username" size="20" class="text" value="<c:out value="${oauthParam_USERNAME}" />" /></p>
    <p><label for="password">Password:</label><br />
    <input type="password" name="password" id="password" size="20" class="text" /></p>
    <input type="hidden" name="oauth_token" value="<c:out value="${oauthParam_REQUEST_TOKEN}" />" />
    <input type="hidden" name="oauth_callback" value="<c:out value="${oauthParam_CALLBACK_URL}" />" />
<p class="centerText">
    <input class="button uppercase" type="submit" name="Authorize" value="Authorize"/>
</p>
</form>

</fieldset>

</div>

--%>

    </body>
</html>
