<%@ taglib prefix="s" uri="/struts-tags" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/Group"></s:url>" 
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.groupManagement" />">
		<s:text name="title.groupManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.groupManagement.groupTrash" />
	</span>
</h1>

<div id="main" role="main">

<div class="message message_error">
<h2><s:text name="message.title.ActionErrors" /></h2>
<p><s:text name="message.note.resolveReferences" />:</p>

</div>

<s:include value="/WEB-INF/apsadmin/jsp/user/group/include/groupInfo-references.jsp" />

</div>