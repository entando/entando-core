<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:headInfo type="CSS" info="widgets/api.css"/>
<s:set var="apiServiceVar" value="%{getApiService(serviceKey)}" />
<div class="entando-api api-resource-detail">
<h2><wp:i18n key="ENTANDO_API_SERVICE" />&#32;<s:property value="serviceKey" /></h2>
<s:if test="hasActionMessages()">
	<div class="message message_confirm">
		<h3><wp:i18n key="ENTANDO_API_ERROR" /></h3>
		<ul>
			<s:iterator value="actionMessages">
				<li><s:property escapeHtml="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<s:if test="hasActionErrors()">
	<div class="message message_error">
		<h3><wp:i18n key="ENTANDO_API_ERROR" /></h3>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property escapeHtml="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>

<!-- DESCRIPTION -->
<p class="description"><s:property value="getTitle(serviceKey, #apiServiceVar.description)" /></p>

<s:set var="masterMethodVar" value="#apiServiceVar.master" />

<!-- INFO -->
<dl class="dl-horizontal">
	<dt><wp:i18n key="ENTANDO_API_SERVICE_KEY" /></dt>
		<dd><s:property value="serviceKey" /></dd>
	<dt><wp:i18n key="ENTANDO_API_SERVICE_PARENT_API" /></dt>
		<dd><s:property value="#masterMethodVar.description" />&#32;(/<s:if test="#masterMethodVar.namespace!=null && #masterMethodVar.namespace.length()>0"><s:property value="#masterMethodVar.namespace" />/</s:if><s:property value="#masterMethodVar.resourceName" />)</dd>
	<dt>
		<wp:i18n key="ENTANDO_API_SERVICE_AUTHORIZATION" />
	</dt>
		<dd>
			<s:if test="%{!#apiServiceVar.requiredAuth}" >
				<wp:i18n key="ENTANDO_API_SERVICE_AUTH_FREE" />
			</s:if>
			<s:elseif test="%{null == #apiServiceVar.requiredPermission && null == #apiServiceVar.requiredGroup}">
				<wp:i18n key="ENTANDO_API_SERVICE_AUTH_SIMPLE" />
			</s:elseif>
			<s:else>
				<s:set var="serviceAuthGroupVar" value="%{getGroup(#apiServiceVar.requiredGroup)}" />
				<s:set var="serviceAuthPermissionVar" value="%{getPermission(#apiServiceVar.requiredPermission)}" />
				<s:if test="%{null != #serviceAuthPermissionVar}">
					<wp:i18n key="ENTANDO_API_SERVICE_AUTH_WITH_PERM" />&#32;<s:property value="#serviceAuthPermissionVar.description" />
				</s:if>
				<s:if test="%{null != #serviceAuthGroupVar}">
					<s:if test="%{null != #serviceAuthPermissionVar}"><br /></s:if>
					<wp:i18n key="ENTANDO_API_SERVICE_AUTH_WITH_GROUP" />&#32;<s:property value="#serviceAuthGroupVar.descr" />
				</s:if>
			</s:else>
		</dd>
	<dt><wp:i18n key="ENTANDO_API_SERVICE_URI" /></dt>
		<dd>
			<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />legacyapi/rs/<wp:info key="currentLang" />/getService?key=<s:property value="serviceKey" />"><wp:info key="systemParam" paramName="applicationBaseURL" />legacyapi/rs/<wp:info key="currentLang" />/getService?key=<s:property value="serviceKey" /></a>
		</dd>
	<dt>
		<wp:i18n key="ENTANDO_API_EXTENSION" />
	</dt>
		<dd>
			<wp:i18n key="ENTANDO_API_EXTENSION_NOTE" />
		</dd>
	<dt>
		<wp:i18n key="ENTANDO_API_SERVICE_SCHEMAS" />
	</dt>
		<dd class="schemas">
			<wp:action path="/ExtStr2/do/Front/Api/Service/responseSchema.action" var="responseSchemaURLVar" >
				<wp:parameter name="serviceKey"><s:property value="serviceKey" /></wp:parameter>
			</wp:action>
			<a href="<c:out value="${responseSchemaURLVar}" escapeXml="false" />" >
				<wp:i18n key="ENTANDO_API_SERVICE_SCHEMA_RESP" />
			</a>
		</dd>
</dl>

<s:if test="%{null != #apiServiceVar.freeParameters}" >
<table class="table table-striped table-bordered table-condensed" summary="<wp:i18n key="ENTANDO_API_SERVICE_PARAMETERS_SUMMARY" />">
	<caption><span><wp:i18n key="ENTANDO_API_SERVICE_PARAMETERS" /></span></caption>
	<tr>
		<th><wp:i18n key="ENTANDO_API_SERVICE_PARAM_NAME" /></th>
		<th><wp:i18n key="ENTANDO_API_SERVICE_PARAM_DESCRIPTION" /></th>
		<th><wp:i18n key="ENTANDO_API_SERVICE_PARAM_REQUIRED" /></th>
		<th><wp:i18n key="ENTANDO_API_SERVICE_PARAM_DEFAULT_VALUE" /></th>
	</tr>
	<s:iterator value="#apiServiceVar.freeParameters" var="apiParameterNameVar" >
		<s:set var="apiParameterValueVar" value="%{#apiServiceVar.parameters[#apiParameterNameVar]}" />
		<s:set var="apiParameterVar" value="%{#apiServiceVar.master.getParameter(#apiParameterNameVar)}" />
		<s:set var="apiParameterRequiredVar" value="%{#apiParameterVar.required && null == #apiParameterValueVar}" />
		<tr>
			<td><label for="<s:property value="#apiParameterNameVar" />"><s:property value="#apiParameterNameVar" /></label></td>
			<td><s:property value="%{#apiParameterVar.description}" /></td>
			<td class="icon required_<s:property value="#apiParameterRequiredVar" />">
				<s:if test="#apiParameterRequiredVar" ><wp:i18n key="YES" /></s:if>
				<s:else><wp:i18n key="NO" /></s:else>
			</td>
			<td><s:if test="null != #apiParameterValueVar"><s:property value="#apiParameterValueVar" /></s:if><s:else>-</s:else></td>
		</tr>
	</s:iterator>
</table>
</s:if>
<p class="api-back">
	<a class="btn btn-primary" href="<wp:action path="/ExtStr2/do/Front/Api/Resource/list.action" />"><span class="icon-arrow-left icon-white"></span>&#32;<wp:i18n key="ENTANDO_API_GOTO_LIST" /></a>
</p>
</div>