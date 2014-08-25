<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="%{'title.' + entityManagerName + '.management'}" />
	</span>
</h1>

<div class="panel panel-default">
	<div class="panel-body">
		<s:text name="%{'note.' + entityManagerName + '.intro.2'}" />
	</div>
</div>

<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<p><s:text name="message.title.FieldErrors" /></p>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
		            <li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</div>
</s:if>

<a href="<s:url namespace="/do/Entity" action="initAddEntityType" ><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" class="btn btn-default">
<span class="icon fa fa-plus-circle"></span>&#32;
<s:text name="menu.entityAdmin.entityTypes.new" />
</a>

<s:if test="%{entityPrototypes.size > 0}">
<div class="table-responsive margin-base-vertical">
<table class="table table-bordered">
	<tr>
		<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
		<th class="text-center"><abbr title="<s:text name="label.state" />">S</abbr></th>
		<th><s:text name="label.code" /></th>
		<th><s:text name="label.description" /></th>
	</tr> 

	<s:iterator value="entityPrototypes" var="entityType" status="counter">
	<s:set var="entityAnchor" value="%{'entityCounter'+#counter.count}" />
	<tr>
		<td class="text-center text-nowrap">
			<div class="btn-group btn-group-xs">
				<%-- edit --%>
				<a class="btn btn-default" id="<s:property value="#entityAnchor" />" href="
				<s:url namespace="/do/Entity" action="initEditEntityType">
					<s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
					<s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
				</s:url>
				" title="<s:text name="label.edit" />: <s:property value="#entityType.typeDescr" />">
					<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#entityType.typeDescr" /></span>
					<span class="icon fa fa-pencil-square-o"></span>
				</a>

				<a href="
				<s:url namespace="/do/Entity" action="reloadEntityTypeReferences" anchor="%{#entityAnchor}"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param><s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
				</s:url>" title="<s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 2"><s:text name="label.references.status.ko" /></s:if><s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 0"><s:text name="label.references.status.ok" /></s:if>" class="btn btn-default btn-xs"
				<s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 1">disabled="true"</s:if>>
				<span class="icon fa fa-refresh">
				<span class="sr-only"><s:text name="label.references.status.ko" /></span>
				</span>
				</a>
			</div>
			<%-- remove --%>
			<div class="btn-group btn-group-xs">
			<a href="
				<s:url namespace="/do/Entity" action="trashEntityType">
					<s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
					<s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
				</s:url>
				" title="<s:text name="label.remove" />: <s:property value="#entityType.typeDescr" />" class="btn btn-warning">
				<span class="icon fa fa-times-circle-o"></span>&#32;
				<span class="sr-only"><s:text name="label.alt.clear" /></span>
				</a>
			</div>
		</td>
		<td class="text-center">
		<s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 1">
			<span class="icon fa fa-spinner" title="<s:text name="label.references.status.wip" />">
			</span> 
		</s:if>
		<s:elseif test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 2">
			<span class="icon fa fa-exclamation text-warning" title="<s:text name="label.references.status.ko" />">
			</span>
		</s:elseif>
		<s:elseif test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 0">
			<span class="icon fa fa-check text-success" title="<s:text name="label.references.status.ok" />">
			</span>			
		</s:elseif>
		</td>
		<td>
			<code><s:property value="#entityType.typeCode" /></code>
		</td>
		<td><s:property value="#entityType.typeDescr" /></td>
	</tr>
	</s:iterator>
</table>
</div>
</s:if>
<s:else>
	<p>
		<s:text name="%{'note.' + entityManagerName + '.empty'}" />
	</p> 
</s:else>

<s:include value="/WEB-INF/apsadmin/jsp/entity/include/entity-type-references-operations-reload.jsp" />