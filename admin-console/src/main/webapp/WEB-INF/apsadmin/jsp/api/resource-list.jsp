<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="title.apiResourceManagement" /></span>
</h1>
<div id="main" role="main">
	<s:if test="hasActionMessages()">
		<div class="alert alert-info alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:set var="resourceFlavoursVar" value="resourceFlavours" />
	<s:if test="#resourceFlavoursVar.size() > 0">
		<%-- icons --%>
			<%-- off --%>
			<s:set var="icon_off"><span class="btn btn-default btn-xs"><span class="icon fa fa-pause text-warning" title="<s:text name="api.resource.status.off" />"><span class="sr-only"><s:text name="api.resource.status.off.alt" /></span></s:set>
			<%-- free --%>
			<s:set var="icon_free"><span class="btn btn-default btn-xs"><span class="icon fa fa-check text-success" title="<s:text name="api.resource.status.free" />"><span class="sr-only"><s:text name="api.resource.status.free.alt" /></span></s:set>
			<%-- logged --%>
			<s:set var="icon_auth"><span class="btn btn-default btn-xs"><span class="icon fa fa-user" title="<s:text name="api.resource.status.auth" />"><span class="sr-only"><s:text name="api.resource.status.auth.alt" /></span></s:set>
			<%-- permission --%>
			<s:set var="icon_lock"><span class="btn btn-default btn-xs"><span class="icon fa fa-unlock-alt" title="<s:text name="api.resource.status.lock" />"><span class="sr-only"><s:text name="api.resource.status.lock" /></span></s:set>
			<%-- hidden, free --%>
			<s:set var="icon_free_hidden"><span class="btn btn-default btn-xs"><span class="icon fa fa-check text-muted" title="<s:text name="api.resource.status.free" /> (<s:text name="label.hidden" />)"><span class="sr-only"><s:text name="api.resource.status.free" />(<s:text name="label.hidden" />)</span></span></span></s:set>
			<%-- hidden, logged --%>
			<s:set var="icon_auth_hidden"><span class="btn btn-default btn-xs"><span class="icon fa fa-user text-muted" title="<s:text name="api.resource.status.auth" /> (<s:text name="label.hidden" />)"><span class="sr-only"><s:text name="api.resource.status.auth.alt" /> (<s:text name="label.hidden" />)</span></s:set>
			<%-- hidden, permission --%>
			<s:set var="icon_lock_hidden"><span class="btn btn-default btn-xs"><span class="icon fa fa-unlock-alt text-muted" title="<s:text name="api.resource.status.lock" /> (<s:text name="label.hidden" />)"><span class="sr-only"><s:text name="api.resource.status.lock" /> (<s:text name="label.hidden" />)</span></s:set>
			<%-- not available --%>
			<s:set var="icon_na"><abbr class="text-muted" title="<s:text name="api.resource.status.na" />">&ndash;</abbr></s:set>
		<%-- icons //end --%>
		<s:iterator var="resourceFlavourVar" value="#resourceFlavoursVar" status="resourceFlavourStatusVar">
			<div class="panel panel-default">
					<div class="panel-heading">
						<s:iterator value="#resourceFlavourVar" var="resourceVar" status="statusVar" >
							<s:if test="#statusVar.first">
								<%-- if we're evaluating the first resource, setup the caption title and table headers --%>
								<s:if test="#resourceVar.source=='core'"><s:set var="captionVar" value="#resourceVar.source" /></s:if>
								<s:else><s:set var="captionVar" value="%{getText(#resourceVar.sectionCode+'.name')}" /></s:else>
								<h2 class="h3 panel-title" title="<s:text name="label.flavour" />&#32;<s:property value="#captionVar" />"><s:property value="#captionVar" /></h2>
							</s:if>
						</s:iterator>
					</div>
					<table class="table table-responsive">
						<s:iterator value="#resourceFlavourVar" var="resourceVar" status="statusVar" >
							<s:if test="#statusVar.first">
								<tr>
									<th><s:text name="name.api.resource" /></th>
									<th><s:text name="label.description" /></th>
									<th class="text-center"><abbr title="GET">G</abbr></th>
									<th class="text-center"><abbr title="POST">P</abbr></th>
									<th class="text-center"><abbr title="PUT">P</abbr></th>
									<th class="text-center"><abbr title="DELETE">D</abbr></th>
								</tr>
							</s:if>
							<tr>
								<%-- CODE and link to edit --%>
								<td class="text-nowrap">
									<s:url var="detailActionURL" action="detail" namespace="/do/Api/Resource"><s:param name="resourceName" value="#resourceVar.resourceName" /><s:param name="namespace" value="#resourceVar.namespace" /></s:url>
									<a title="<s:text name="label.edit" />: <s:property value="%{#resourceVar.namespace.length()>0?#resourceVar.namespace+'/':''}" /><s:property value="#resourceVar.resourceName" />" href="<s:url action="detail" namespace="/do/Api/Resource"><s:param name="resourceName" value="#resourceVar.resourceName" /><s:param name="namespace" value="#resourceVar.namespace" /></s:url>"><span class="icon fa fa-cog"></span>&nbsp;<s:property value="#resourceVar.resourceName" /></a><s:if test="#resourceVar.getMethod != null"><s:if test="#resourceVar.getMethod.canSpawnOthers">&emsp;<a class="btn btn-default btn-xs" href="<s:url action="newService" namespace="/do/Api/Service"><s:param name="resourceName" value="#resourceVar.resourceName" /><s:param name="namespace" value="#resourceVar.namespace" /></s:url>" title="<s:text name="note.api.apiMethodList.createServiceFromMethod" />: <s:property value="#resourceVar.resourceName" />"><span class="icon fa fa-code-fork"></span><span class="sr-only"><s:text name="note.api.apiMethodList.createServiceFromMethod" />: <s:property value="#resourceVar.resourceName" /></span></a></s:if>
									</s:if>
								</td>
								<%-- DESCRIPTION --%>
								<td><s:property value="#resourceVar.description" /></td>
								<%-- GET --%>
								<td class="text-center">	
									<s:if test="#resourceVar.getMethod != null" >
										<a href="<s:property value="#detailActionURL" escapeHtml="false" />#GET_tab">
											<s:if test="!#resourceVar.getMethod.active" ><s:property value="#icon_off" escapeHtml="false" /></s:if>
											<s:elseif test="#resourceVar.getMethod.requiredPermission != null" ><s:if test="#resourceVar.getMethod.hidden" ><s:property value="#icon_lock_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_lock" escapeHtml="false" /></s:else></s:elseif>
											<s:elseif test="#resourceVar.getMethod.requiredAuth" ><s:if test="#resourceVar.getMethod.hidden" ><s:property value="#icon_auth_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_auth" escapeHtml="false" /></s:else></s:elseif>
											<s:else><s:if test="#resourceVar.getMethod.hidden" ><s:property value="#icon_free_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_free" escapeHtml="false" /></s:else></s:else>
										</a>
									</s:if>
									<s:else>
										<s:property value="#icon_na" escapeHtml="false" />
									</s:else>
								</td>
								<%-- POST --%>
								<td class="text-center">
									<s:if test="#resourceVar.postMethod != null" >
										<a href="<s:property value="#detailActionURL" escapeHtml="false" />#POST_tab">
											<s:if test="!#resourceVar.postMethod.active" ><s:property value="#icon_off" escapeHtml="false" /></s:if>
											<s:elseif test="#resourceVar.postMethod.requiredPermission != null" ><s:if test="#resourceVar.postMethod.hidden" ><s:property value="#icon_lock_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_lock" escapeHtml="false" /></s:else></s:elseif>
											<s:elseif test="#resourceVar.postMethod.requiredAuth" ><s:if test="#resourceVar.postMethod.hidden" ><s:property value="#icon_auth_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_auth" escapeHtml="false" /></s:else></s:elseif>
											<s:else><s:if test="#resourceVar.postMethod.hidden" ><s:property value="#icon_free_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_free" escapeHtml="false" /></s:else></s:else>
										</a>
									</s:if>
									<s:else><s:property value="#icon_na" escapeHtml="false" /></s:else>
								</td>
								<%-- PUT --%>
								<td class="text-center">
									<s:if test="#resourceVar.putMethod != null" >
										<a href="<s:property value="#detailActionURL" escapeHtml="false" />#PUT_tab">
											<s:if test="!#resourceVar.putMethod.active" ><s:property value="#icon_off" escapeHtml="false" /></s:if>
											<s:elseif test="#resourceVar.putMethod.requiredPermission != null" ><s:if test="#resourceVar.putMethod.hidden" ><s:property value="#icon_lock_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_lock" escapeHtml="false" /></s:else></s:elseif>
											<s:elseif test="#resourceVar.putMethod.requiredAuth" ><s:if test="#resourceVar.putMethod.hidden" ><s:property value="#icon_auth_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_auth" escapeHtml="false" /></s:else></s:elseif>
											<s:else><s:if test="#resourceVar.putMethod.hidden" ><s:property value="#icon_free_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_free" escapeHtml="false" /></s:else></s:else>
										</a>
									</s:if>
									<s:else><s:property value="#icon_na" escapeHtml="false" /></s:else>
								</td>
								<%-- DELETE --%>
								<td class="text-center">
									<s:if test="#resourceVar.deleteMethod != null" >
										<a href="<s:property value="#detailActionURL" escapeHtml="false" />#DELETE_tab">
											<s:if test="!#resourceVar.deleteMethod.active" ><s:property value="#icon_off" escapeHtml="false" /></s:if>
											<s:elseif test="#resourceVar.deleteMethod.requiredPermission != null" ><s:if test="#resourceVar.deleteMethod.hidden" ><s:property value="#icon_lock_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_lock" escapeHtml="false" /></s:else></s:elseif>
											<s:elseif test="#resourceVar.deleteMethod.requiredAuth" ><s:if test="#resourceVar.deleteMethod.hidden" ><s:property value="#icon_auth_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_auth" escapeHtml="false" /></s:else></s:elseif>
											<s:else><s:if test="#resourceVar.deleteMethod.hidden" ><s:property value="#icon_free_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_free" escapeHtml="false" /></s:else></s:else>
										</a>
									</s:if>
									<s:else><s:property value="#icon_na" escapeHtml="false" /></s:else>
								</td>
							</tr>
						</s:iterator>
					</table>
			</div>
		</s:iterator>
	</s:if>
	<s:else>
		<p><s:text name="note.api.noResources" /></p>
	</s:else>
</div>