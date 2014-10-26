<%@ taglib prefix="s" uri="/struts-tags" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/Group"></s:url>" 
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.roleManagement" />">
		<s:text name="title.roleManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.roleManagement.roleTrash" />
	</span>
</h1>

<div id="main" role="main">

<div class="message message_error">
<h2><s:text name="message.title.ActionErrors" /></h2>
<p><s:text name="message.note.resolveReferences" />:</p>

</div>

<s:include value="/WEB-INF/apsadmin/jsp/user/role/include/roleInfo-references.jsp" />

</div>