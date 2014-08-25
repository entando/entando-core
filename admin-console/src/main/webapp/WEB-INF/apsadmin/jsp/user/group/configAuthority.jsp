<%@ taglib prefix="s" uri="/struts-tags" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/Group"></s:url>"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.groupManagement" />">
		<s:text name="title.groupManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.groupManagement.membersOf" />: <s:property value="authName" />
	</span>
</h1>

<div id="main" role="main">

<s:include value="/WEB-INF/apsadmin/jsp/user/include_configAuthority.jsp" />

</div>