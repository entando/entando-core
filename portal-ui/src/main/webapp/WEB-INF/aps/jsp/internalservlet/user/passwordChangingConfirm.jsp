<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<h1><wp:i18n key="userprofile_EDITPASSWORD_TITLE" /></h1>
<p class="alert alert-success"><wp:i18n key="userprofile_PASSWORD_UPDATED" /></p>
<s:if test="!#session.currentUser.credentialsNonExpired">
	<p class="alert alert-info">
		<a href="<s:url namespace="/do" action="logout" />" ><wp:i18n key="userprofile_PLEASE_LOGIN_AGAIN" /></a>
	</p>
</s:if>