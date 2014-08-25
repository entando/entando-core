<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/Role" action="list" />">
			<s:text name="title.roleManagement" />
		</a>
		&#32;/&#32;
		<s:if test="getStrutsAction() == 1">
			<s:text name="title.roleManagement.roleNew" />
		</s:if>
		<s:if test="getStrutsAction() == 2">
			<s:text name="title.roleManagement.roleEdit" />
		</s:if>
	</span>
</h1>
<s:form action="save" cssClass="form-horizontal">
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
				            <li><s:property escape="false" /></li>
						</s:iterator>
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
	<p class="sr-only">
		<wpsf:hidden name="strutsAction" />
		<s:if test="getStrutsAction() == 2">
			<wpsf:hidden name="name" />
		</s:if>
	</p>
	<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['name']}" />
	<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
	<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
	<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
		<div class="col-xs-12">
			<label for="name"><s:text name="name" /></label>
			<wpsf:textfield name="name" id="name" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
			<s:if test="#fieldHasFieldErrorVar">
				<p class="text-danger padding-small-vertical">
					<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
				</p>
			</s:if>
		</div>
	</div>
	<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['description']}" />
	<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
	<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
	<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
		<div class="col-xs-12">
			<label for="description"><s:text name="description" /></label>
			<wpsf:textfield name="description" id="description" cssClass="form-control" />
			<s:if test="#fieldHasFieldErrorVar">
				<p class="text-danger padding-small-vertical">
					<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
				</p>
			</s:if>
		</div>
	</div>
	<div class="form-group">
		<div class="col-xs-12">
			<label><s:text name="name.permissions" /></label>
			<span class="help-block"><s:text name="note.permissions.intro" /></span>
				<s:set var="permissionNamesVar" value="permissionNames" />
				<s:iterator value="%{systemPermissions}" var="permissionVar">
					<label class="checkbox">
						<input 
							type="checkbox" 
							name="permissionNames" 
							value="<s:property value="%{#permissionVar.name}" />" 
							<s:if test="%{#permissionNamesVar.contains(#permissionVar.name)}"> checked="checked" </s:if>
							/>
							<s:property value="%{#permissionVar.description}" />
					</label>
				</s:iterator>
		</div>
	</div>
	<%-- save button --%>
	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.save" />
			</wpsf:submit>
		</div>
	</div>
</s:form>