<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head>
	<title>Entando - Login</title>
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/common/css/administration.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/common/css/layout-general.css" media="screen" />
	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/common/css/layout-general-ie7.css" media="screen" />
	<![endif]-->
	
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/basic/css/administration.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/basic/css/layout-general.css" media="screen" />

	<script type="text/javascript" src="<wp:resourceURL />administration/common/js/mootools-core-1.3-full-compat-yc.js"></script>
	<script type="text/javascript" src="<wp:resourceURL />administration/common/js/mootools-more-1.3-full-compat-yc.js"></script>
	<script type="text/javascript" src="<wp:resourceURL />administration/common/js/login.js" ></script>	
	
</head>
<body>

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
    </body>
</html>
