<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
		<s:text name="%{'title.' + entityManagerName + '.management'}" /></a>
		&#32;/&#32;
		<s:text name="title.entityTypes.editType.remove" />: <code><s:property value="entityTypeCode" /> - <s:property value="%{getEntityPrototype(entityTypeCode).typeDescr}" /></code>
	</span>
</h1>

<div id="main" role="main">

	<div class="alert alert-warning">
		<s:property value="references.size()" />&#32;<s:text name="note.entityTypes.deleteType.references" />
	</div>

	<p><s:text name="note.entityTypes.deleteType.references.outro" />:</p>

	<ul class="list-group">
		<s:iterator value="references" id="entityId">
			<li class="list-group-item">
				<a href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentType"><s:property value="entityTypeCode" /></s:param><s:param name="viewCode"><s:property value="true" /></s:param><s:param name="viewTypeDescr"><s:property value="true" /></s:param><s:param name="contentId"><s:property value="entityId" /></s:param></s:url>">
				<s:property value="#entityId" />
				</a>
			</li>
		</s:iterator>
	</ul>

<s:if test="entityManagerName == 'jacmsContentManager'">
	<p>
		<a href="<s:url action="search" namespace="/do/jacms/Content"><s:param name="contentType"><s:property value="entityTypeCode" /></s:param><s:param name="viewCode"><s:property value="true" /></s:param><s:param name="viewTypeDescr"><s:property value="true" /></s:param></s:url>"><s:text name="note.goToSomewhere" />: <s:text name="jacms.menu.contentAdmin.list" /></a>
	</p>
</s:if>

<p>
	<a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>"><s:text name="note.backToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" /></a>
</p>

</div>