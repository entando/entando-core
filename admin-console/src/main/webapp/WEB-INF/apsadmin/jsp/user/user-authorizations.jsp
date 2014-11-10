<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:set var="targetNS" value="%{'/do/User'}" />
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a>
		&#32;/&#32;
		<s:text name="title.userManagement.userAuthorizations" />
	</span>
</h1>

<div id="main" role="main">

<p class="margin-more-bottom">
	<s:text name="note.userAuthorizations.intro" />&#32;<code><s:property value="userAuthsFormBean.username" /></code><%--, <s:text name="note.userAuthorizations.youCan" />&#32;<a href="#groups"><s:text name="note.userAuthorizations.configureGroups" /></a>&#32;<s:text name="label.or" />&#32;<a href="#roles"><s:text name="note.userAuthorizations.configureRoles" /></a>. --%>
</p>

<s:form action="save" cssClass="form-horizontal">
	
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
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
		<wpsf:hidden name="username" value="%{userAuthsFormBean.username}"/>
	</p>
	
	<s:set var="userAuthorizationsVar" value="%{userAuthsFormBean.authorizations}" />
	<s:if test="%{#userAuthorizationsVar.size()>0}">
		<div class="table-responsive">
			<table class="table table-striped table-hover table-condensed">
				<tr>
					<th class="text-center text-nowrap"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
					<th><s:text name="label.userGroup" /></th>
					<th><s:text name="label.userRole" /></th>
				</tr>
				<s:iterator value="#userAuthorizationsVar" var="userAuthorizationVar" status="elementStatus">
					<tr>
						<td class="text-center text-nowrap">
							<s:set name="elementIndexVar" value="#elementStatus.index" />
							<div class="btn-group btn-group-xs">
								<wpsa:actionParam action="removeAuthorization" var="actionName" >
									<wpsa:actionSubParam name="index" value="%{#elementIndexVar}" />
								</wpsa:actionParam>
								<wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
									<span class="icon fa fa-times-circle"></span>
								</wpsf:submit>
							</div>
						</td>
						<td>
							<s:property value="#userAuthorizationVar.group.description" />&#32;<code><s:property value="#userAuthorizationVar.group.name" /></code>
						</td>
						<td>
							<s:set var="roleVar" value="#userAuthorizationVar.role" />
							<s:if test="null != #roleVar">
								<s:property value="#roleVar.description" />&#32;<code><s:property value="#roleVar.name" /></code>
							</s:if>
							<s:else><code>&ndash;</code></s:else>
						</td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</s:if>
	<s:else>
		<p><s:text name="note.userAuthorizations.empty" /></p>
	</s:else>
	
	<hr />
	
	<fieldset>
		<legend><s:text name="title.newAuthorization" /></legend>
		<div class="form-group">
			<div class="col-xs-12">
				<label for="userGroup"><s:text name="label.userGroup" /></label>
				<wpsf:select id="userGroup" name="groupName" list="groups" 
					headerKey="" headerValue="" 
					listKey="name" listValue="description" cssClass="form-control" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<label for="userRole"><s:text name="label.userRole" /></label>
				<wpsf:select id="userRole" name="roleName" list="roles" 
					headerKey="" headerValue="" 
					listKey="name" listValue="description" cssClass="form-control" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<span class="input-group-btn">
					<wpsa:actionParam action="addAuthorization" var="actionName" />
					<wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-info">
						<span class="icon fa fa-plus"></span>
						<s:text name="label.addAuthorization"></s:text>
					</wpsf:submit>
				</span>
				<%-- </div> --%>
			</div>
			&#32;
		</div>
	</fieldset>
	<div class="form-group margin-large-top">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
				<s:text name="label.save" />
			</wpsf:submit>
		</div>
	</div>

</s:form>

</div>