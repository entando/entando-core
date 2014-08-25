<%@ taglib prefix="s" uri="/struts-tags" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/PageModel"></s:url>" 
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageModelManagement" />">
		<s:text name="title.pageModelManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.pageModelManagement.pageModelTrash" />
	</span>
</h1>

<div id="main" role="main">

<div class="message message_error">
<h2><s:text name="message.title.ActionErrors" /></h2>
<p><s:text name="message.note.resolveReferences" />:</p>

</div>

<s:include value="/WEB-INF/apsadmin/jsp/portal/model/include/pageModel-references.jsp" />

</div>