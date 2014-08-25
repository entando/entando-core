<%@ taglib prefix="s" uri="/struts-tags" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/Role" action="list" />">
			<s:text name="title.roleManagement" />
		</a>
		&#32;/&#32;
		<s:text name="title.roleManagement.assignToUsers" />: <s:property value="authName" />
	</span>
</h1>

<s:include value="/WEB-INF/apsadmin/jsp/user/include_configAuthority.jsp" />

<%--
<s:set var="targetNS" value="%{'/do/Role'}" />
<h1><s:text name="title.roleManagement" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main" role="main">

<h2><s:text name="title.roleManagement.assignToUsers" />: <s:property value="authName" /></h2>


</div>
--%>