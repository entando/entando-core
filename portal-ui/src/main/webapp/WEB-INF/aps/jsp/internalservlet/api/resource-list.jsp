<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h2><wp:i18n key="ENTANDO_API_RESOURCES" /></h2>
<s:if test="hasActionErrors()">
	<div class="alert alert-block alert-error">
		<h3 class="alert-heading"><wp:i18n key="ENTANDO_API_ERROR" /></h3>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property escapeHtml="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<s:set var="resourceFlavoursVar" value="resourceFlavours" />

<s:if test="#resourceFlavoursVar.size() > 0">
	<s:set var="icon_free"><span class="icon icon-ok"></span><span class="noscreen sr-only"><wp:i18n key="ENTANDO_API_METHOD_STATUS_FREE" /></span></s:set>
	<s:set var="title_free"><wp:i18n key="ENTANDO_API_METHOD_STATUS_FREE" />. <wp:i18n key="ENTANDO_API_GOTO_DETAILS" /></s:set>

	<s:set var="icon_auth"><span class="icon icon-user"></span><span class="noscreen sr-only"><wp:i18n key="ENTANDO_API_METHOD_STATUS_AUTH" /></span></s:set>
	<s:set var="title_auth"><wp:i18n key="ENTANDO_API_METHOD_STATUS_AUTH" />. <wp:i18n key="ENTANDO_API_GOTO_DETAILS" /></s:set>

	<s:set var="icon_lock"><span class="icon icon-lock"></span><span class="noscreen sr-only"><wp:i18n key="ENTANDO_API_METHOD_STATUS_LOCK" /></span></s:set>
	<s:set var="title_lock"><wp:i18n key="ENTANDO_API_METHOD_STATUS_LOCK" />. <wp:i18n key="ENTANDO_API_GOTO_DETAILS" /></s:set>

	<s:iterator var="resourceFlavourVar" value="#resourceFlavoursVar" status="resourceFlavourStatusVar">
		<table class="table table-striped table-bordered table-condensed">
			<s:iterator value="#resourceFlavourVar" var="resourceVar" status="statusVar" >
				<s:if test="#statusVar.first">
					<%-- if we're evaluating the first resource, setup the caption title and table headers --%>
					<s:if test="#resourceVar.source=='core'"><s:set var="captionVar"><s:property value="#resourceVar.source" escapeHtml="false" /></s:set></s:if>
					<s:else><s:set var="captionVar"><s:property value="%{getText(#resourceVar.sectionCode+'.name')}" escapeHtml="false" /></s:set></s:else>
					<caption>
						<s:property value="#captionVar" />
						<%--
						<details>
							<summary>YOUR SUMMARY HERE</summary>
							<p><wp:i18n key="ENTANDO_API_TABLE_SUMMARY" /></p>
						</details>
						--%>
					</caption>
					<tr>
						<th class="span3"><wp:i18n key="ENTANDO_API_RESOURCE" /></th>
						<th><wp:i18n key="ENTANDO_API_DESCRIPTION" /></th>
						<th class="text-center span1">GET</th>
						<th class="text-center span1">POST</th>
						<th class="text-center span1">PUT</th>
						<th class="text-center span1">DELETE</th>
					</tr>
				</s:if>
				<tr>
					<%-- CODE and link to edit --%>
					<td>
						<wp:action path="/ExtStr2/do/Front/Api/Resource/detail.action" var="detailActionURL">
							<wp:parameter name="resourceName"><s:property value="#resourceVar.resourceName" /></wp:parameter>
							<wp:parameter name="namespace"><s:property value="#resourceVar.namespace" /></wp:parameter>
						</wp:action>
						<a title="<wp:i18n key="ENTANDO_API_GOTO_DETAILS" />:&#32;/<s:property value="%{#resourceVar.namespace.length()>0?#resourceVar.namespace+'/':''}" /><s:property value="#resourceVar.resourceName" />" href="<c:out value="${detailActionURL}" escapeXml="false" />" ><s:property value="#resourceVar.resourceName" /></a>
					</td>
					<%-- DESCRIPTION --%>
					<td><s:property value="#resourceVar.description" /></td>
					<%-- GET --%>
					<td class="text-center">
						<s:if test="#resourceVar.getMethod != null && #resourceVar.getMethod.active && (!#resourceVar.getMethod.hidden)" >
							<s:if test="#resourceVar.getMethod.requiredPermission != null" ><s:set var="icon" value="#icon_lock" /><s:set var="title" value="#title_lock" /></s:if>
							<s:elseif test="#resourceVar.getMethod.requiredAuth" ><s:set var="icon" value="#icon_auth" /><s:set var="title" value="#title_auth" /></s:elseif>
							<s:else><s:set var="icon" value="#icon_free" /><s:set var="title" value="#title_free" /></s:else>
							<a href="<c:out value="${detailActionURL}" escapeXml="false" />#api_method_GET" title="<s:property value="#title" />">
								<s:property value="#icon" escapeHtml="false" />
							</a>
						</s:if>
						<s:else><abbr title="<wp:i18n key="ENTANDO_API_METHOD_STATUS_NA" />">&ndash;</abbr></s:else>
					</td>
					<%-- POST --%>
					<td class="text-center">
						<s:if test="#resourceVar.postMethod != null && #resourceVar.postMethod.active && (!#resourceVar.postMethod.hidden)" >
							<s:if test="#resourceVar.postMethod.requiredPermission != null" ><s:set var="icon" value="#icon_lock" /><s:set var="title" value="#title_lock" /></s:if>
							<s:elseif test="#resourceVar.postMethod.requiredAuth" ><s:set var="icon" value="#icon_auth" /><s:set var="title" value="#title_auth" /></s:elseif>
							<s:else><s:set var="icon" value="#icon_free" /><s:set var="title" value="#title_free" /></s:else>
							<a href="<c:out value="${detailActionURL}" escapeXml="false" />#api_method_POST" title="<s:property value="#title" />">
								<s:property value="#icon" escapeHtml="false" />
							</a>
						</s:if>
						<s:else><abbr title="<wp:i18n key="ENTANDO_API_METHOD_STATUS_NA" />">&ndash;</abbr></s:else>
					</td>
					<%-- PUT --%>
					<td class="text-center">
						<s:if test="#resourceVar.putMethod != null && #resourceVar.putMethod.active && (!#resourceVar.putMethod.hidden)" >
							<s:if test="#resourceVar.putMethod.requiredPermission != null" ><s:set var="icon" value="#icon_lock" /><s:set var="title" value="#title_lock" /></s:if>
							<s:elseif test="#resourceVar.putMethod.requiredAuth" ><s:set var="icon" value="#icon_auth" /><s:set var="title" value="#title_auth" /></s:elseif>
							<s:else><s:set var="icon" value="#icon_free" /><s:set var="title" value="#title_free" /></s:else>
							<a href="<c:out value="${detailActionURL}" escapeXml="false" />#api_method_PUT" title="<s:property value="#title" />">
								<s:property value="#icon" escapeHtml="false" />
							</a>
						</s:if>
						<s:else><abbr title="<wp:i18n key="ENTANDO_API_METHOD_STATUS_NA" />">&ndash;</abbr></s:else>
					</td>
					<%-- DELETE --%>
					<td class="text-center">
						<s:if test="#resourceVar.deleteMethod != null && #resourceVar.deleteMethod.active && (!#resourceVar.deleteMethod.hidden)" >
							<s:if test="#resourceVar.deleteMethod.requiredPermission != null" ><s:set var="icon" value="#icon_lock" /><s:set var="title" value="#title_lock" /></s:if>
							<s:elseif test="#resourceVar.deleteMethod.requiredAuth" ><s:set var="icon" value="#icon_auth" /><s:set var="title" value="#title_auth" /></s:elseif>
							<s:else><s:set var="icon" value="#icon_free" /><s:set var="title" value="#title_free" /></s:else>
							<a href="<c:out value="${detailActionURL}" escapeXml="false" />#api_method_DELETE" title="<s:property value="#title" />">
								<s:property value="#icon" escapeHtml="false" />
							</a>
						</s:if>
						<s:else><abbr title="<wp:i18n key="ENTANDO_API_METHOD_STATUS_NA" />">&ndash;</abbr></s:else>
					</td>
				</tr>
			</s:iterator>
		</table>

		<s:if test="#resourceVar.source=='core'">
			<a href="<wp:action path="/ExtStr2/do/Front/Api/Service/list.action" />" class="btn btn-primary pull-right"><wp:i18n key="ENTANDO_API_GOTO_SERVICE_LIST" /></a>
		</s:if>
	</s:iterator>
</s:if>
<s:else>
	<p><wp:i18n key="ENTANDO_API_NO_RESOURCES" /></p>
</s:else>
<script>
  $(function () {
    $('#api-togglers a:first').tab('show');
  })
</script>