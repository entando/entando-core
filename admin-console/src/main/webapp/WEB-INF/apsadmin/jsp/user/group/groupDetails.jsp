<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/Group"></s:url>"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.groupManagement" />">
		<s:text name="title.groupManagement" /></a>
		&#32;/&#32;
		<s:text name="title.groupDetail" />
	</span>
</h1>
<div class="form-horizontal">
	<div class="form-group">
		<label class="control-label col-lg-3 col-md-3"><s:text name="label.group" /></label>
		<div class="col-md-9 col-lg-9 form-control-static">
			<code><s:property value="name" /></code>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-lg-3 col-md-3"><s:text name="label.description" /></label>
		<div class="col-md-9 col-lg-9 form-control-static">
			<s:property value="description" />
		</div>
	</div>
	<wpsa:hookPoint key="core.groupDetails" objectName="hookPointElements_core_groupDetails">
		<s:iterator value="#hookPointElements_core_groupDetails" var="hookPointElement">
			<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
		</s:iterator>
	</wpsa:hookPoint>
</div>
<s:include value="/WEB-INF/apsadmin/jsp/user/group/include/groupInfo-references.jsp" />