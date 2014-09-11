<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:if test="#methodVar == null">
	<p>
		<s:property value="#currentMethodNameVar" />,&#32;<wp:i18n key="ENTANDO_API_METHOD_KO" />
	</p>
</s:if>
<s:else>
	<dl class="dl-horizontal">
		<dt>
			<wp:i18n key="ENTANDO_API_METHOD" />
		</dt>
			<dd>
				<wp:i18n key="ENTANDO_API_METHOD_OK" />
			</dd>
		<s:if test="#methodVar != null">
			<dt>
				<wp:i18n key="ENTANDO_API_DESCRIPTION" />
			</dt>
				<dd><s:property value="#methodVar.description" /></dd>
			<dt>
				<wp:i18n key="ENTANDO_API_METHOD_AUTHORIZATION" />
			</dt>
				<dd>
					<s:if test="%{null != #methodVar.requiredPermission}">
						<%-- <wp:i18n key="ENTANDO_API_METHOD_AUTH_WITH_PERM" /> --%>
						<s:iterator value="methodAuthorityOptions" var="permission"><s:if test="#permission.key==#methodVar.requiredPermission"><s:property value="#permission.value" /></s:if></s:iterator>
					</s:if>
					<s:elseif test="%{#methodVar.requiredAuth}">
						<wp:i18n key="ENTANDO_API_METHOD_AUTH_SIMPLE" />
					</s:elseif>
					<s:else>
						<wp:i18n key="ENTANDO_API_METHOD_AUTH_FREE" />
					</s:else>
				</dd>
			<s:if test='%{!#methodVar.resourceName.equalsIgnoreCase("getService")}' >
			<dt>
				<wp:i18n key="ENTANDO_API_METHOD_SCHEMAS" />
			</dt>
				<dd class="schemas">
					<s:if test='%{#methodVar.httpMethod.toString().equalsIgnoreCase("POST") || #methodVar.httpMethod.toString().equalsIgnoreCase("PUT")}'>
						<wp:action path="/ExtStr2/do/Front/Api/Resource/requestSchema.action" var="requestSchemaURLVar" >
							<wp:parameter name="resourceName"><s:property value="#methodVar.resourceName" /></wp:parameter>
							<wp:parameter name="namespace"><s:property value="#methodVar.namespace" /></wp:parameter>
							<wp:parameter name="httpMethod"><s:property value="#methodVar.httpMethod" /></wp:parameter>
						</wp:action>
						<a href="<c:out value="${requestSchemaURLVar}" escapeXml="false" />" >
							<wp:i18n key="ENTANDO_API_METHOD_SCHEMA_REQ" />
						</a>
						<br />
					</s:if>
						<wp:action path="/ExtStr2/do/Front/Api/Resource/responseSchema.action" var="responseSchemaURLVar" >
							<wp:parameter name="resourceName"><s:property value="#methodVar.resourceName" /></wp:parameter>
							<wp:parameter name="namespace"><s:property value="#methodVar.namespace" /></wp:parameter>
							<wp:parameter name="httpMethod"><s:property value="#methodVar.httpMethod" /></wp:parameter>
						</wp:action>
						<a href="<c:out value="${responseSchemaURLVar}" escapeXml="false" />" >
							<wp:i18n key="ENTANDO_API_METHOD_SCHEMA_RESP" />
						</a>
				</dd>
			</s:if>
		</s:if>
	</dl>
	<s:if test="#methodVar != null">
		<s:set var="methodParametersVar" value="#methodVar.parameters" />
		<s:if test="null != #methodParametersVar && #methodParametersVar.size() > 0">
			<table class="table table-striped table-bordered table-condensed">
				<caption><wp:i18n key="ENTANDO_API_METHOD_REQUEST_PARAMS" /></caption>
				<tr>
					<th><wp:i18n key="ENTANDO_API_PARAM_NAME" /></th>
					<th><wp:i18n key="ENTANDO_API_PARAM_DESCRIPTION" /></th>
					<th><wp:i18n key="ENTANDO_API_PARAM_REQUIRED" /></th>
				</tr>
				<s:iterator value="#methodParametersVar" var="apiParameter" >
					<tr>
						<td><s:property value="#apiParameter.key" /></td>
						<td><s:property value="#apiParameter.description" /></td>
						<td class="icon required_<s:property value="#apiParameter.required" />">
							<s:if test="#apiParameter.required">
								<wp:i18n key="YES" />
							</s:if>
							<s:else>
								<wp:i18n key="NO" />
							</s:else>
						</td>
					</tr>
				</s:iterator>
			</table>
		</s:if>
	</s:if>
</s:else>