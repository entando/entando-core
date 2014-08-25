<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.changePassword" />
	</span>
</h1>
<div id="main" role="main">
	<div class="alert alert-success">
		<strong><s:text name="messages.confirm" /></strong>
		&#32;<s:text name="message.passwordChanged" />
		<s:if test="!#session.currentUser.credentialsNotExpired">
			&emsp;
			<a class="alert-link" href="<s:url action="logout" />" >
				<span class="icon fa fa-arrow-right"></span>
				<s:text name="note.login.again" />
			</a>
		</s:if>
	</div>
</div>



