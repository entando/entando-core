<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li class="page-title-container"><s:text
			name="%{'title.' + entityManagerName + '.management'}" /></li>
</ol>

<h1 class="page-title-container">
	<s:text name="%{'title.' + entityManagerName + '.management'}" />
	<span class="pull-right"> <a tabindex="0" role="button"
		data-toggle="popover" data-trigger="focus" data-html="true" title=""
		data-content="TO be inserted" data-placement="left"
		data-original-title=""><i class="fa fa-question-circle-o"
			aria-hidden="true"></i></a>
	</span>
</h1>

<div class="text-right">
	<div class="form-group-separator">
		<%--<s:text name="label.requiredFields" />--%>
	</div>
</div>
<br>


<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert">
			<span class="icon fa fa-times"></span>
		</button>
		<p>
			<s:text name="message.title.FieldErrors" />
		</p>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</div>
</s:if>

<a
	href="<s:url namespace="/do/Entity" action="initAddEntityType" ><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>"
	class="btn btn-primary pull-right" style="margin-bottom: 5px"> <s:text
		name="menu.entityAdmin.entityTypes.new" />
</a>

<s:if test="%{entityPrototypes.size > 0}">
	<table class="table table-striped table-bordered"
		style="margin-top: 1em;">
		<thead>
			<tr>
				<th><s:text name="label.code" /></th>
				<th><s:text name="label.name" /></th>
				<th class="text-center" style="width: 89px;"><s:text
						name="label.state" /></th>
				<th style="width: 59px;"><s:text name="label.actions" /></th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="entityPrototypes" var="entityType"
				status="counter">
				<s:set var="entityAnchor" value="%{'entityCounter'+#counter.count}" />
				<tr>
					<td><code>
							<s:property value="#entityType.typeCode" />
						</code></td>
					<td><s:property value="#entityType.typeDescr" /></td>
					<td class="text-center"><s:if
							test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 1">
							<span class="icon fa fa-spinner"
								title="<s:text name="label.references.status.wip" />"> </span>
						</s:if> <s:elseif
							test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 2">
							<span class="icon fa fa-exclamation text-warning"
								title="<s:text name="label.references.status.ko" />"> </span>
						</s:elseif> <s:elseif
							test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 0">
							<span class="icon fa fa-check text-success"
								title="<s:text name="label.references.status.ok" />"> </span>
						</s:elseif></td>
					<td class="text-center text-nowrap">
						<div class="dropdown dropdown-kebab-pf">
							<button class="btn btn-menu-right dropdown-toggle" type="button"
								data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
								<span class="fa fa-ellipsis-v"></span>
							</button>
							<ul class="dropdown-menu dropdown-menu-right"
								aria-labelledby="dropdownKebabRight">
								<li>
									<%-- edit --%> <a class=""
									id="<s:property value="#entityAnchor" />"
									href="
                                       <s:url namespace="/do/Entity" action="initEditEntityType">
                                           <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                           <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                       </s:url>
                                       "
									title="<s:text name="label.edit" />: <s:property value="#entityType.typeDescr" />">
										<span class="sr-only"><s:text name="label.edit" />&#32;<s:property
												value="#entityType.typeDescr" /></span> <s:text name="label.edit" />
								</a>
								</li>
								<li><a
									href="<s:url namespace="/do/Entity" action="reloadEntityTypeReferences" anchor="%{#entityAnchor}"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param><s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                       </s:url>"
									title="<s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 2"><s:text name="label.references.status.ko" /></s:if><s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 0"><s:text name="label.references.status.ok" /></s:if>"
									class=""
									<s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 1">disabled="true"</s:if>>
										<s:text name="label.reload" /> <span class="sr-only"><s:text
												name="label.references.status.ko" /></span> </span>
								</a></li>
								<%-- remove --%>
								<li><a
									href="
                                       <s:url namespace="/do/Entity" action="trashEntityType">
                                           <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                           <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                       </s:url>
                                       "
									title="<s:text name="label.remove" />: <s:property value="#entityType.typeDescr" />"
									class=""> <s:text name="label.delete" /> <span
										class="sr-only"><s:text name="label.alt.clear" /></span>
								</a></li>
							</ul>
						</div>
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
<s:else>
	<p>
		<s:text name="%{'note.' + entityManagerName + '.empty'}" />
	</p>
</s:else>

<s:include
	value="/WEB-INF/apsadmin/jsp/entity/include/entity-type-references-operations-reload.jsp" />
