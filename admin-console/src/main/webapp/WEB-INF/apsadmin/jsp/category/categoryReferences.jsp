<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Category" />" 
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.categoryManagement" />">
		<s:text name="title.categoryManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.categoryReference" />
	</span>
</h1>
<div id="main" role="main">
	<div class="alert alert-danger alert-dismissable fade in">
		<h2 class="h3 margin-none"><s:text name="message.title.ActionErrors" /></h2>
		<p><s:text name="message.note.resolveReferences" /></p>
	</div>

	<s:include value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo-references.jsp" />
</div>