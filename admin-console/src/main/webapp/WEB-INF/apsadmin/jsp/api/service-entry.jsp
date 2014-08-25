<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.apiServiceManagement" />
	</span>
</h1>

<div id="main" role="main">
	<s:if test="strutsAction == 1 || strutsAction == 3">
		<s:set var="masterApiMethodVar" value="%{getMethod(namespace, resourceName)}" />
	</s:if>
	<s:if test="strutsAction == 2">
		<h2 class="margin-more-bottom"><s:text name="label.edit" />: <s:property value="serviceKey" /></h2>
	</s:if>
	<s:elseif test="strutsAction == 1">
		<h2 class="margin-more-bottom"><s:text name="title.api.apiService.newFrom" />: <span class="monospace"><s:property value="#masterApiMethodVar.methodName" /></span></h2>
	</s:elseif>
	<s:elseif test="strutsAction == 3">
		<h2 class="margin-more-bottom"><s:text name="title.api.apiService.copyFrom" />: <span class="monospace"><s:property value="#masterApiMethodVar.methodName" /></span> in <span class="monospace"><s:property value="pageCode" /></span></h2>
	</s:elseif>
	<p>
		<s:set var="masterMethodVar" value="%{getMethod(namespace, resourceName)}" />
		<s:text name="label.service.parentApi" />: <em><s:property value="#masterMethodVar.description" />&#32;(/<s:if test="namespace!=null && namespace.length()>0"><s:property value="namespace" />/</s:if><s:property value="resourceName" />)</em>
	</p>
	<s:form action="save" >
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
					<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
		<fieldset class="col-xs-12">
			<legend><s:text name="label.info" /></legend>
			<p class="sr-only">
				<wpsf:hidden name="strutsAction" />
				<s:if test="strutsAction == 2">
					<wpsf:hidden name="serviceKey" />
				</s:if>
				<s:if test="strutsAction == 3">
					<wpsf:hidden name="pageCode" />
					<wpsf:hidden name="framePos" />
				</s:if>
				<wpsf:hidden name="resourceName" />
				<wpsf:hidden name="namespace" />
			</p>
			<div class="form-group">
				<label for="<s:property value="serviceKey" />" class="control-label"><s:text name="name.api.service" /> <abbr title="<s:text name="Entity.attribute.flag.mandatory.full"/>"><s:text name="Entity.attribute.flag.mandatory.short"/></abbr>:</label>
				<wpsf:textfield id="serviceKey" name="serviceKey" disabled="%{strutsAction == 2}" cssClass="form-control" />
			</div>
			<s:iterator value="systemLangs">
				<div class="form-group">
					<label for="lang_<s:property value="code"/>" class="control-label"><span class="monospace">(<s:property value="code" />)</span>&#32;<s:text name="label.description" /> <abbr title="<s:text name="Entity.attribute.flag.mandatory.full"/>"><s:text name="Entity.attribute.flag.mandatory.short"/></abbr>:</label>
					<s:textarea cols="50" rows="3" id="%{'lang_'+code}" name="%{'lang_'+code}" value="%{descriptions[code]}" cssClass="form-control" />
				</div>
			</s:iterator>
			<div class="form-group">
				<label for="tag" class="control-label"><s:text name="label.tag" />:</label>
				<wpsf:textfield id="tag" name="tag" cssClass="form-control" />
			</div>
		</fieldset>
		<fieldset class="col-xs-12">
			<legend><s:text name="label.options" /></legend>
			<ul>
			<li class="checkbox">				
			<label for="activeService"><s:text name="label.active" /><wpsf:checkbox name="activeService" id="activeService"/></label>
			</li>
			<li class="checkbox">
				<label for="hiddenService"><s:text name="label.hidden" /><wpsf:checkbox name="hiddenService" id="hiddenService" /></label>
			</li>
			<li class="checkbox">
				<label for="myEntandoService"><s:text name="label.myEntando.compatible" /><wpsf:checkbox name="myEntandoService" id="myEntandoService" /></label>
			</li>
		</fieldset>
		<fieldset class="col-xs-12">
			<legend><s:text name="label.api.authorities" /></legend>
			<div class="checkbox">
				<label for="requiredAuth"><s:text name="label.api.authority.autenticationRequired" /><wpsf:checkbox name="requiredAuth" id="requiredAuth" /></label>
			</div>
			<div class="form-group">
				<label class="control-label" for="requiredPermission"><s:text name="label.api.authority.permission" />:</label>
				<wpsf:select headerKey="" headerValue="%{getText('label.none')}" name="requiredPermission" list="permissionAutorityOptions" listKey="key" listValue="value" id="requiredPermission" cssClass="form-control"/>
			</div>
			<div class="form-group">
				<label class="control-label" for="requiredGroup"><s:text name="label.api.authority.group" />:</label>
				<wpsf:select headerKey="" headerValue="%{getText('label.none')}" name="requiredGroup" list="groups" listKey="name" listValue="descr" id="requiredGroup" cssClass="form-control"/>
			</div>
		</fieldset>
		<fieldset class="col-xs-12">
			<legend><s:text name="label.parameters" /></legend>
			<div class="table-responsive">
			<table class="table table-bordered table-hover table-condensed table-striped">
				<caption><s:text name="label.parameters" /></caption>
				<tr>
					<th><s:text name="label.name" /></th>
					<th><s:text name="label.description" /></th>
					<th><s:text name="label.required" /></th>
					<th><s:text name="label.default" /></th>
					<th><s:text name="label.canBeOverridden" /></th>
				</tr>
				<s:iterator value="apiParameters" var="apiParameterVar" >
					<tr>
						<td>
							<label for="<s:property value="%{#apiParameterVar.key + '_apiParam'}" />">
								<s:property value="#apiParameterVar.key" />
							</label>
						</td>
						<td><s:property value="#apiParameterVar.description" /></td>
						<td>
						<s:property value="#apiParameterVar.required"/>
						</td>
						<td>
						<wpsf:textfield id="%{#apiParameterVar.key + '_apiParam'}" name="%{#apiParameterVar.key + '_apiParam'}" value="%{apiParameterValues[#apiParameterVar.key]}" cssClass="form-control" /></td>
                        <td>
				        <s:set var="freeParameterFieldNameVar" value="%{'freeParameter_' + #apiParameterVar.key}" />
				        <wpsf:radio name="%{#freeParameterFieldNameVar}"
				        id="%{'true_' + #freeParameterFieldNameVar}" value="true" checked="%{freeParameters.contains(#apiParameterVar.key)}" />
				        <label for="<s:property value="%{'true_' + #freeParameterFieldNameVar}" />"><s:text name="label.yes"/></label><br />
				        <wpsf:radio name="%{#freeParameterFieldNameVar}"
				        id="%{'false_' + #freeParameterFieldNameVar}" value="false" checked="%{!freeParameters.contains(#apiParameterVar.key)}" />
				        <label for="<s:property value="%{'false_' + #freeParameterFieldNameVar}" />"><s:text name="label.no"/></label>
						</td>
					</tr>
				</s:iterator>
			</table>
		</fieldset>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-floppy-o"></span>&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>