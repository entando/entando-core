<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<div class="panel panel-default" id="<s:property value="#methodVar.httpMethod" />_tab">
		<div class="panel-heading"><h3 class="panel-title"><s:property value="#titleMethod" /></h3></div>
				<table class="table">
					<tr>
						<th class="text-right"><s:text name="label.api.resourceMethod" /></th>
						<td>
							<s:if test="#methodVar != null">
								<s:text name="label.api.resource.method.status.ok" />
								&#32;<span class="text-muted">(<s:if test="#methodVar.active" ><s:text name="label.active" /></s:if><s:else><s:text name="label.disabled" /></s:else>)</span>
							</s:if>
							<s:else>
								<s:text name="label.api.resource.method.status.ko" />
							</s:else>
						</td>
					</tr>
					<s:if test="#methodVar != null">
						<tr>
							<th class="text-right"><s:text name="label.api.resource.method.description" /></th>
							<td><s:property value="#methodVar.description" /></td>
						</tr>
						<tr>
							<th class="text-right"><s:text name="label.api.resource.method.visibility" /></th>
							<td>
								<s:if test="#methodVar.hidden" ><s:text name="label.hidden" /></s:if>
								<s:else><s:text name="label.public" /></s:else>
								<span class="text-muted">(<s:text name="label.default" />&#32;
									<s:if test="#methodVar.defaultHidden" ><s:text name="label.hidden" /></s:if>
									<s:else><s:text name="label.public" /></s:else>)</span>
							</td>
						</tr>
						<tr>
							<th class="text-right"><s:text name="label.api.authorization" /></th>
							<td>
								<s:if test="%{null != #methodVar.requiredPermission}">
									<s:iterator value="methodAuthorityOptions" var="permission"><s:if test="#permission.key==#methodVar.requiredPermission"><s:property value="#permission.value" /></s:if></s:iterator>
								</s:if>
								<s:elseif test="%{#methodVar.requiredAuth}">
									<s:text name="label.api.authority.autenticationRequired" />
								</s:elseif>
								<s:else>
									<s:text name="label.none" />
								</s:else>
								<span class="text-muted">(<s:text name="label.default" />&#32;
									<s:if test="%{#methodVar.defaultRequiredPermission!=null}">
										<s:text name="label.api.authority.permission" />&#32;<s:property value="#methodVar.defaultRequiredPermission" />
									</s:if>
									<s:elseif test="#methodVar.defaultRequiredAuth!=null && #methodVar.defaultRequiredAuth">
										<s:text name="label.api.authority.autenticationRequired" />
									</s:elseif>
									<s:else>
										<s:text name="label.none" />
									</s:else>)</span>
							</td>
						</tr>
						<tr>
							<th class="text-right"><s:text name="label.api.resource.method.schemas" /></th>
							<td class="schemas">
								<s:if test='%{#methodVar.httpMethod.toString().equalsIgnoreCase("POST") || #methodVar.httpMethod.toString().equalsIgnoreCase("PUT")}'>
									<p class="schema-get">
										<s:url action="requestSchema" namespace="/do/Api/Resource" var="requestSchemaURLVar">
											<s:param name="resourceName" value="#methodVar.resourceName" />
											<s:param name="namespace" value="#methodVar.namespace" />
											<s:param name="httpMethod" value="#methodVar.httpMethod" />
										</s:url>
										<a title="<s:property value="#requestSchemaURL" escapeHtml="false" />" href="<s:property value="#requestSchemaURLVar" escapeHtml="false" />" >
											<span class="icon fa fa-globe"></span>&#32;
											<s:text name="label.api.resource.method.schemas.req" />
										</a>
									</p>
								</s:if>
								<p class="schema-post">
									<s:url action="responseSchema" namespace="/do/Api/Resource" var="responseSchemaURLVar">
										<s:param name="resourceName" value="#methodVar.resourceName" />
										<s:param name="namespace" value="#methodVar.namespace" />
										<s:param name="httpMethod" value="#methodVar.httpMethod" />
									</s:url>
									<a title="<s:property value="#responseSchemaURL" escapeHtml="false" />" href="<s:property value="#responseSchemaURLVar" escapeHtml="false" />" >
										<span class="icon fa fa-globe"></span>&#32;
										<s:text name="label.api.resource.method.schemas.resp" />
									</a>
								</p>
							</td>
						</tr>
					</s:if>
				</table>
			<s:if test="#methodVar != null">
				<div class="panel-body">
					<s:set var="methodParametersVar" value="#methodVar.parameters" />
					<s:if test="null != #methodParametersVar && #methodParametersVar.size() > 0">
						<div class="panel panel-default">
							<div class="panel-heading"><h4 class="panel-title"><s:text name="label.api.resource.method.requestParameters" /></h3></div>
							<div class="panel-body">
								<div class="table-responsive">
									<table class="table">
										<tr>
											<th class="col-xs-1 col-sm-1 col-md-1 col-lg-1"><s:text name="label.required" /></th>
											<th><s:text name="label.name" /></th>
											<th><s:text name="label.description" /></th>
										</tr>
										<s:iterator value="#methodParametersVar" var="apiParameter" >
											<tr>
												<td class="text-right">
													<span class="icon fa fa-<s:property value="%{#apiParameter.required ? 'ok' : ''}" />" title="<s:property value="%{#apiParameter.required ? getText('label.yes') : getText('label.no')}" />"></span>
													<span class="sr-only"><s:property value="%{#apiParameter.required ? getText('label.yes') : getText('label.no')}" /></span>
												</td>
												<td><code><s:property value="#apiParameter.key" /></code></td>
												<td><s:property value="#apiParameter.description" /></td>
											</tr>
										</s:iterator>
									</table>
								</div>
							</div>
						</div>
					</s:if>
					<s:if test="null != #methodVar.requiredPermission" ><s:set var="selectFieldValue" value="#methodVar.requiredPermission" /></s:if>
					<s:elseif test="#methodVar.requiredAuth"><s:set var="selectFieldValue">0</s:set></s:elseif>
					<s:url namespace="/do/Api/Resource" action="updateMethodStatus" var="updateMethodStatusURL" />
					<form 
						action="<s:url namespace="/do/Api/Resource" action="updateMethodStatus" />#<s:property value="#methodVar.httpMethod" />_tab" 
						class="form-horizontal">
							<div class="form-group">
								<div class="col-xs-12">
									<span class="checkbox">
										<wpsf:hidden name="resourceName" value="%{#methodVar.resourceName}" />
										<wpsf:hidden name="namespace" value="%{#methodVar.namespace}" />
										<wpsf:hidden name="httpMethod" value="%{#methodVar.httpMethod}" />
										<s:set var="activeFieldValue" value="#methodVar.active" />
										<label for="active_<s:property value="%{#methodVar.httpMethod}" />">
											<wpsf:checkbox name="%{#methodVar.httpMethod}_active" value="%{#activeFieldValue}" cssClass="radiocheck" id="active_%{#methodVar.httpMethod}" />
											&#32;
											<s:text name="label.active" />
										</label>
									</span>
								</div>
							</div>
							<div class="form-group">
								<div class="col-xs-12">
									<s:set var="hiddenFieldValue" value="#methodVar.hidden" />
									<div class="checkbox">
										<label for="hidden_<s:property value="%{#methodVar.httpMethod}" />">
											<wpsf:checkbox name="%{#methodVar.httpMethod}_hidden" value="%{#hiddenFieldValue}" cssClass="radiocheck" id="hidden_%{#methodVar.httpMethod}"/>
												<s:text name="label.hidden" />
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-xs-12">
									<label for="methodAuthority_<s:property value="%{#methodVar.httpMethod}" />">
										<s:text name="label.api.authorization" />
									</label>
									<wpsf:select cssClass="form-control" name="%{#methodVar.httpMethod}_methodAuthority" list="methodAuthorityOptions" listKey="key" listValue="value" value="%{selectFieldValue}" id="methodAuthority_%{#methodVar.httpMethod}" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-xs-12">
									<wpsf:submit type="button" action="updateMethodStatus" cssClass="btn btn-primary"><s:text name="label.update" /></wpsf:submit>
									&#32;
									<wpsf:submit type="button" action="resetMethodStatus" id="%{''}" cssClass="btn btn-default"><s:text name="label.reset.default" /></wpsf:submit>
								</div>
							</div>
					</form>
					<s:set var="selectFieldValue" />
			</div><%-- panel --%>
		</s:if>
</div><%-- panel --%>