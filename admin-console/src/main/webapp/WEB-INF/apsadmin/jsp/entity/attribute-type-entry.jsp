<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
		<s:text name="%{'title.' + entityManagerName + '.management'}" /></a>
		&#32;/&#32;
		<s:if test="strutsAction == 2">
			<s:text name="title.attribute.edit"/>
		</s:if>
		<s:else>
			<s:text name="title.attribute.new"/>
		</s:else>
	</span>
</h1>


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

<s:form action="saveAttribute">

<p class="sr-only">
	<wpsf:hidden name="attributeTypeCode" />
	<wpsf:hidden name="strutsAction" />
	<s:if test="null != attributeRoles && attributeRoles.size() > 0">
	<s:iterator value="attributeRoles" id="attributeRole"><wpsf:hidden name="attributeRoles" value="%{#attributeRole}" /></s:iterator>
	</s:if>
	<s:if test="null != disablingCodes && disablingCodes.size() > 0">
	<s:iterator value="disablingCodes" id="disablingCode"><wpsf:hidden name="disablingCodes" value="%{#disablingCode}" /></s:iterator>
	</s:if>
</p>

<s:if test="strutsAction == 2">
	<p class="sr-only">
		<wpsf:hidden name="attributeName" />
	</p>
</s:if>

<s:if test="strutsAction == 1">
	<s:set name="attribute" value="getAttributePrototype(attributeTypeCode)" />
</s:if>
<s:else>
	<s:set name="attribute" value="entityType.getAttribute(attributeName)" />
</s:else>

<fieldset class="col-xs-12 margin-base-top"><legend><s:text name="label.info" /></legend>
	<div class="form-group">
		<label><s:text name="label.type" /></label>
		<wpsf:textfield cssClass="form-control" name="attributeTypeCode" value="%{attributeTypeCode}" disabled="true" />
	</div>

	<div class="form-group">
		<label for="attributeName"><s:text name="label.code" /></label>
		<s:if test="strutsAction == 1">
			<wpsf:textfield cssClass="form-control" name="attributeName" id="attributeName" />
		</s:if>
		<s:else>
			<wpsf:textfield cssClass="form-control" name="attributeName" value="%{attributeName}" id="attributeName" disabled="true" />
		</s:else>	
	</div>
	<div class="form-group">
		<label for="attributeDescription"><s:text name="label.description" /></label>
		<wpsf:textfield name="attributeDescription" id="attributeDescription" cssClass="form-control"/> 
	</div>

	<ul>
		<li class="checkbox">
			<label for="required"><s:text name="Entity.attribute.flag.mandatory.full" />
			<wpsf:checkbox name="required" id="required" cssClass="radiocheck" /></label>
		</li>
		<s:if test="!(#attribute.type == 'List') && !(#attribute.type == 'Monolist')">
			
			<s:if test="isEntityManagerSearchEngineUser() && isIndexableOptionSupported(attributeTypeCode)">
			<li class="checkbox">
				<label for="indexable"><s:text name="Entity.attribute.flag.indexed.full" />
				<wpsf:checkbox name="indexable" id="indexable" cssClass="radiocheck" /></label>
			</li>
			</s:if>
			
			<s:if test="isSearchableOptionSupported(attributeTypeCode)">
			<li class="checkbox">
				<label for="searchable"><s:text name="Entity.attribute.flag.searchable.full" /><wpsf:checkbox name="searchable" id="searchable" cssClass="radiocheck" /></label>
			</li>
			</s:if>

		</s:if>
	</ul>
</fieldset>

<fieldset class="col-xs-12 margin-base-top"><legend><s:text name="name.roles" /></legend>
	<s:set var="freeAttributeRoles" value="%{getFreeAttributeRoleNames()}" />
	<s:if test="null == #freeAttributeRoles || #freeAttributeRoles.isEmpty()">
		<s:text name="note.entityAdmin.entityTypes.attribute.roles.none" />
	</s:if>
	<s:else>

		<div class="form-group">
			<label for="attributeRoleName"><s:text name="name.role" /></label>
			<div class="input-group">
				<wpsf:select name="attributeRoleName" id="attributeRoleName" list="#freeAttributeRoles" 
					listKey="name" listValue="%{name + ' - ' + description}" cssClass="form-control" />
				<span class="input-group-btn">
				<wpsf:submit type="button" action="addAttributeRole" value="%{getText('label.add')}" cssClass="btn btn-info" />
				</span>
			</div>
		</div>

		<s:if test="null != attributeRoles && attributeRoles.size() > 0">
		<h3 class="margin-none"><s:text name="label.roles.assigned" /></h3>
		<ul class="list-group">
		<s:iterator value="attributeRoles" id="attributeRole">
			<wpsa:actionParam action="removeAttributeRole" var="actionName" >
				<wpsa:actionSubParam name="attributeRoleName" value="%{#attributeRole}" />
			</wpsa:actionParam>
			<li class="list-group-item">
			<div class="row">
				<div class="col-sm-10">
					<code><s:property value="attributeRole" /></code>
					<s:property value="%{getAttributeRole(#attributeRole).description}" />
				</div>
				<div class="col-sm-2 text-right">
					<wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-xs btn-warning" title="%{getText('label.remove')}" >
						<span class="icon fa fa-times-circle-o"></span>
					</wpsf:submit>
				</div>
			</div>
			</li>
		</s:iterator>
		</ul>
		</s:if>
	
	</s:else>
</fieldset>

<s:set var="attributeDisablingCodesVar" value="getAttributeDisablingCodes()" ></s:set>

<s:if test="null != #attributeDisablingCodesVar && #attributeDisablingCodesVar.size()>0">
<fieldset class="col-xs-12 margin-base-top"><legend><s:text name="name.disablingCodes" /></legend>
	<div class="form-group">
		<label for="disablingCode"><s:text name="label.code" /></label>
		<div class="input-group">
			<wpsf:select name="disablingCode" id="disablingCode" list="#attributeDisablingCodesVar" cssClass="form-control"/>
			<span class="input-group-btn">
			<wpsf:submit type="button" action="addAttributeDisablingCode" value="%{getText('label.add')}" cssClass="btn btn-info" />
			</span>
		</div>
	</div>
	
	<s:if test="null != disablingCodes && disablingCodes.size() > 0">
	<h3 class="margin-none"><s:text name="label.disablingCodes.assigned" /></h3>
	<ul class="list-group">
		<s:iterator value="disablingCodes" id="disablingCode">
			<wpsa:actionParam action="removeAttributeDisablingCode" var="actionName" >
				<wpsa:actionSubParam name="attributeRoleName" value="%{#disablingCode}" />
			</wpsa:actionParam>
			<li class="list-group-item">
			<div class="row">
				<div class="col-sm-10">
					<code><s:property value="#disablingCode" /></code>
					<s:property value="%{#attributeDisablingCodesVar[#disablingCode]}" />
				</div>
				<div class="col-sm-2 text-right">
				<wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-xs btn-warning" title="%{getText('label.remove')}" >
					<span class="icon fa fa-times-circle-o"></span>
				</wpsf:submit>
				</div>
			</div>
			</li>
		</s:iterator>
	</ul>
	</s:if>
	
</fieldset>
</s:if>

<s:if test="#attribute.textAttribute">
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-text.jsp" />
</s:if>

<s:elseif test="#attribute.type == 'Number'">
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-number.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'Date'">
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-date.jsp" />
</s:elseif>

<s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
<fieldset class="col-xs-12 margin-base-top"><legend><s:text name="label.settings" /></legend>
<div class="form-group">
	<label for="listNestedType"><s:text name="Entity.attribute.setting.listType" /></label>	
	<wpsf:select list="getAllowedNestedTypes(#attribute)" name="listNestedType" id="listNestedType" listKey="type" listValue="type" cssClass="form-control" />
</div>
</fieldset>
</s:if>

<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-ognl.jsp" />
<div class="form-horizontal">
	<div class="form-group">
	  <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
	    <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
	      <s:text name="label.continue" />
	    </wpsf:submit>
	  </div>
	</div>
</div>

</s:form>

</div>