<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><wp:i18n key="userprofile_EDITPASSWORD" /></h1>

<c:choose>
	<c:when test="${sessionScope.currentUser != 'guest'}">
		
		<form action="<wp:action path="/ExtStr2/do/Front/CurrentUser/changePassword.action" />" method="post" class="form-horizontal">
				<s:if test="hasFieldErrors()">
					<div class="alert alert-block">
						<p><strong><wp:i18n key="userprofile_MESSAGE_TITLE_FIELDERRORS" /></strong></p>
						<ul class="unstyled">
							<s:iterator value="fieldErrors">
								<s:iterator value="value">
									<li><s:property escape="false" /></li>
								</s:iterator>
							</s:iterator>
						</ul>
					</div>
				</s:if>
			
				<p class="noscreen">
					<wpsf:hidden name="username" />
				</p>

			<div class="control-group">
				<label for="userprofile-old-password" class="control-label"><wp:i18n key="userprofile_OLDPASSWORD" /></label>
				<div class="controls">
					<wpsf:password 
						useTabindexAutoIncrement="true" 
						name="oldPassword" 
						id="userprofile-old-password" />
				</div>
			</div>
			<div class="control-group">
				<label for="userprofile-new-password" class="control-label"><wp:i18n key="userprofile_NEWPASS" /></label>
				<div class="controls">
					<wpsf:password 
						useTabindexAutoIncrement="true" 
						name="password" 
						id="userprofile-new-password" />
				</div>
			</div>
			<div class="control-group">
				<label for="userprofile-new-password-confirm" class="control-label"><wp:i18n key="userprofile_CONFIRM_NEWPASS" /></label>
				<div class="controls">
					<wpsf:password 
						useTabindexAutoIncrement="true" 
						name="passwordConfirm" 
						id="userprofile-new-password-confirm" />
				</div>
			</div>	
			<p class="form-actions">
				<wp:i18n key="userprofile_SAVE_PASSWORD" var="userprofile_SAVE_PASSWORD" />
				<wpsf:submit 
					useTabindexAutoIncrement="true" 
					value="%{#attr.userprofile_SAVE_PASSWORD}" 
					cssClass="btn btn-primary" />
			</p>
		</form>
			
	</c:when>
	<c:otherwise>
		<p>
			<wp:i18n key="userprofile_PLEASE_LOGIN_TO_EDIT_PASSWORD" />
		</p>
	</c:otherwise>
</c:choose>
