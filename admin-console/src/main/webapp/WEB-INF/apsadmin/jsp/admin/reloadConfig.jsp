<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a> / <s:text name="menu.reload.config" /></span>
</h1>

<s:if test="1 == reloadingResult">
	<div class="alert alert-success">
		<strong><s:text name="messages.confirm" /></strong>!&#32;
		<s:text name="message.reloadConfig.ok" />.
	</div>
</s:if>
<s:else>
	<div class="alert alert-warning">
		<strong><s:text name="messages.error" /></strong>!&#32;
		<s:text name="message.reloadConfig.ko" />.
	</div>
</s:else>
