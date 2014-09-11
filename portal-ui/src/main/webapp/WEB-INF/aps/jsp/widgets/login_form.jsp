<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1><wp:i18n key="RESERVED_AREA" /></h1>
<c:choose>
	<c:when test="${sessionScope.currentUser != 'guest'}">
		<p><wp:i18n key="WELCOME" />, <em><c:out value="${sessionScope.currentUser}"/></em>!</p>
		<c:if test="${sessionScope.currentUser.entandoUser}">
		<table class="table table-condensed">
			<tr>
				<th><wp:i18n key="USER_DATE_CREATION" /></th>
				<th><wp:i18n key="USER_DATE_ACCESS_LAST" /></th>
				<th><wp:i18n key="USER_DATE_PASSWORD_CHANGE_LAST" /></th>
			</tr>
			<tr>
				<td><c:out value="${sessionScope.currentUser.creationDate}"/></td>
				<td><c:out value="${sessionScope.currentUser.lastAccess}"/></td>
				<td><c:out value="${sessionScope.currentUser.lastPasswordChange}"/></td>
			</tr>
		</table>
			<c:if test="${!sessionScope.currentUser.credentialsNotExpired}">
			<div class="alert alert-block">
				<p><wp:i18n key="USER_STATUS_EXPIRED_PASSWORD" />: <a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/editPassword.action"><wp:i18n key="USER_STATUS_EXPIRED_PASSWORD_CHANGE" /></a></p>
			</div>
			</c:if>
		</c:if>
		<wp:ifauthorized permission="enterBackend">
		<h2><wp:i18n key="ADMINISTRATION" />:</h2>
		<div class="btn-group">
			<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/main.action?request_locale=<wp:info key="currentLang" />&amp;backend_client_gui=normal" class="btn"><wp:i18n key="ADMINISTRATION_BASIC" /></a>
			<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/main.action?request_locale=<wp:info key="currentLang" />&amp;backend_client_gui=advanced" class="btn btn-primary"><wp:i18n key="ADMINISTRATION_MINT" /></a>
		</div>
		</wp:ifauthorized>
		<p class="pull-right"><a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/logout.action" class="btn"><wp:i18n key="LOGOUT" /></a></p>
		
		<wp:pageWithWidget widgetTypeCode="userprofile_editCurrentUser" var="userprofileEditingPageVar" listResult="false" />
		<c:if test="${null != userprofileEditingPageVar}" >
		<p><a href="<wp:url page="${userprofileEditingPageVar.code}" />" ><wp:i18n key="userprofile_CONFIGURATION" /></a></p>
		</c:if>
	</c:when>
	<c:otherwise>

	<c:if test="${accountExpired}">
	<div class="alert alert-block alert-error">
		<p><wp:i18n key="USER_STATUS_EXPIRED" /></p>
	</div>
	</c:if>
	<c:if test="${wrongAccountCredential}">
	<div class="alert alert-block alert-error">
		<p><wp:i18n key="USER_STATUS_CREDENTIALS_INVALID" /></p>
	</div>
	</c:if>

	<form action="<wp:url/>" method="post" class="form-horizontal margin-medium-top">
		<c:if test="${null != param['returnUrl']}">
		<input type="hidden" name="returnUrl" value="<c:out value="${param['returnUrl']}" />" />
		</c:if>
	<div class="control-group">
			<label for="username" class="control-label"><wp:i18n key="USERNAME" /></label>
			<div class="controls">
				<input id="username" type="text" name="username" class="input-xlarge" />
			</div>
		</div>
		<div class="control-group">
			<label for="password" class="control-label"><wp:i18n key="PASSWORD" /></label>
			<div class="controls">
				<input id="password" type="password" name="password" class="input-xlarge" />
		</div>
		<div class="form-actions">
			<input type="submit" value="<wp:i18n key="SIGNIN" />" class="btn btn-primary" />
		</div>
	</form>
	</c:otherwise>
</c:choose>