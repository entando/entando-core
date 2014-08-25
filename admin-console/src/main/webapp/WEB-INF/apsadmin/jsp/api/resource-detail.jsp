<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="apiResourceVar" value="apiResource" />
<s:set var="GETMethodVar" value="#apiResourceVar.getMethod" />
<s:set var="POSTMethodVar" value="#apiResourceVar.postMethod" />
<s:set var="PUTMethodVar" value="#apiResourceVar.putMethod" />
<s:set var="DELETEMethodVar" value="#apiResourceVar.deleteMethod" />
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="title.apiResourceManagement" /></a>
		&#32;/&#32;
		<s:text name="title.apiResourceEdit" />
	</span>
</h1>
<div id="main" role="main">
	<s:if test="hasActionMessages()">
		<div class="alert alert-info alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h3>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<p class="sr-only">
		<s:text name="note.workingOn" />: <code><s:property value="#apiResourceVar.namespace!=null && #apiResourceVar.namespace.length()>0 ? '/' + #apiResourceVar.namespace : ''" />/<s:property value="#apiResourceVar.resourceName" /></code>
	</p>
	<table class="table table-bordered">
		<tr>
			<th class="text-right"><s:text name="label.api.resource.name" /></th>
			<td><code><s:property value="#apiResourceVar.resourceName" /></code></td>
		</tr>
		<tr>
			<th class="text-right"><span lang="en"><s:text name="label.api.resource.namespace" /></span></th>
			<td><code>/<s:property value="#apiResourceVar.namespace" /></code></td>
		</tr>
		<tr>
			<th class="text-right"><s:text name="label.api.resource.description" /></th>
			<td><s:property value="#apiResourceVar.description" /></td>
		</tr>
		<tr>
			<th class="text-right"><s:text name="label.api.resource.source" /></th>
			<td><code><s:property value="#apiResourceVar.source" /></code></td>
		</tr>
		<tr>
			<th class="text-right"><s:text name="label.api.resource.plugin" /></th>
				<td>
					<s:if test="#apiResourceVar.pluginCode == null||#apiResourceVar.pluginCode.length == 0">
						<abbr title="<s:text name="label.none" />" class="text-muted">&ndash;</abbr>
					</s:if>
					<s:else>
						<s:property value="%{getText(#apiResourceVar.pluginCode+'.name')}" />&#32;
						(<code><s:property value="%{#apiResourceVar.pluginCode}" /></code>)
					</s:else>
			</td>
		<tr>
			<th class="text-right"><s:text name="label.api.resource.uri" /></th>
			<td>
				<c:set var="resourceUriVar"><wp:info key="systemParam" paramName="applicationBaseURL" />api/rs/<wp:info key="defaultLang" /><s:if test="null != #apiResourceVar.namespace">/<s:property value="#apiResourceVar.namespace" /></s:if>/<s:property value="#apiResourceVar.resourceName" /></c:set>
				<a href="<c:out value="${resourceUriVar}" escapeXml="false" />">
					<span class="icon fa fa-globe"></span>&#32;
					<c:out value="${resourceUriVar}" escapeXml="false" />
				</a>
			</td>
		</tr>
		<tr>
			<th class="text-right"><s:text name="label.api.resource.extensions" /></th>
			<td>
				<a href="<c:out value="${resourceUriVar}" escapeXml="false" />.xml">
					<span class="icon fa fa-globe"></span>&#32;
					<s:text name="note.extensions.xml" />
				</a>
				<br />
				<a href="<c:out value="${resourceUriVar}" escapeXml="false" />.json">
					<span class="icon fa fa-globe"></span>&#32;
					<s:text name="note.extensions.json" />
				</a>
			</td>
		</tr>
	</table>
	<%-- <h2><s:text name="title.apiResourceEdit" /></h2> --%>
	<div class="panel panel-default">
		<div class="panel-heading"><s:text name="label.api.options.for.all" /></div>
		<div class="panel-body">
			<s:form namespace="/do/Api/Resource" action="updateAllMethodStatus" cssClass="form-horizontal">
				<div class="form-group">
					<div class="col-xs-12 checkbox">
						<div class="checkbox">
							<wpsf:hidden name="resourceName" value="%{#apiResourceVar.resourceName}" />
							<wpsf:hidden name="namespace" value="%{#apiResourceVar.namespace}" />
							<label for="all_active">
								<wpsf:checkbox name="active" value="true" id="all_active" />
								<s:text name="label.active" />
							</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-12 checkbox">
						<div class="checkbox">
							<label for="all_hidden">
								<wpsf:checkbox name="hidden" id="all_hidden" cssClass="radiocheck" />
								<s:text name="label.hidden" />
							</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-12">
							<label for="all_auth"><s:text name="label.api.authorization" />:</label>
							<wpsf:select name="methodAuthority" list="methodAuthorityOptions" listKey="key" listValue="value" id="all_auth" cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-12">
							<wpsf:submit type="button" action="updateAllMethodStatus" id="%{''}" cssClass="btn btn-primary">
								<s:text name="label.update" />
							</wpsf:submit>
							&#32;
							<wpsf:submit type="button" action="resetAllMethodStatus" id="%{''}" cssClass="btn btn-default">
								<s:text name="label.reset.default" />
							</wpsf:submit>
					</div>
				</div>
			</s:form>
		</div>
	</div>

<%-- GET --%>
		<s:set var="methodVar" value="#GETMethodVar" />
		<s:set var="titleMethod" value="%{'GET'}" />
		<s:include value="/WEB-INF/apsadmin/jsp/api/include/resource-method-detail.jsp" />
<%-- POST --%>
		<s:set var="methodVar" value="#POSTMethodVar" />
		<s:set var="titleMethod" value="%{'POST'}" />
		<s:include value="/WEB-INF/apsadmin/jsp/api/include/resource-method-detail.jsp" />
<%-- PUT --%>
		<s:set var="methodVar" value="#PUTMethodVar" />
		<s:set var="titleMethod" value="%{'PUT'}" />
		<s:include value="/WEB-INF/apsadmin/jsp/api/include/resource-method-detail.jsp" />
<%-- DELETE --%>
		<s:set var="methodVar" value="#DELETEMethodVar" />
		<s:set var="titleMethod" value="%{'DELETE'}" />
		<s:include value="/WEB-INF/apsadmin/jsp/api/include/resource-method-detail.jsp" />
</div>