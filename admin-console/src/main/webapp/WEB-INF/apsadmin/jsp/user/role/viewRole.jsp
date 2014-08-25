<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:set var="targetNS" value="%{'/do/Role'}" />
<h1><s:text name="title.roleManagement" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main" role="main">
NAME: <s:property value="name" />

<br />

DESCRIPTION: <s:property value="description" />

<br />

<s:iterator value="rolePermissions">
	PERMISSION: <s:property value="name" /> - <s:property value="description" />
	
	<br />
	
</s:iterator>

</div>