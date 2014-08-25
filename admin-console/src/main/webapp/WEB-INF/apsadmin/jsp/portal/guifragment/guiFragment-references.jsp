<%@ taglib prefix="s" uri="/struts-tags" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/Group"></s:url>" 
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.guiFragmentManagement" />">
		<s:text name="title.guiFragmentManagement" />
		</a>
		&#32;/&#32;
		<s:text name="guiFragment.label.delete" />
	</span>
</h1>

<div id="main" role="main">

<div class="message message_error">
<h2><s:text name="message.title.ActionErrors" /></h2>
<p><s:text name="message.note.resolveReferences" />:</p>

</div>

<s:include value="/WEB-INF/apsadmin/jsp/portal/guifragment/include/guiFragmentInfo-references.jsp" />

</div>