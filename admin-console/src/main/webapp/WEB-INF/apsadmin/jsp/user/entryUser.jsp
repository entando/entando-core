<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a>
		&#32;/&#32;
		<s:if test="getStrutsAction() == 1">
			<s:text name="title.userManagement.userNew" />
		</s:if>
		<s:elseif test="getStrutsAction() == 2">
			<s:text name="title.userManagement.userEdit" />
		</s:elseif>
	</span>
</h1>
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
		<wpsf:hidden name="strutsAction" />
		<s:if test="getStrutsAction() == 2">
			<wpsf:hidden name="username" />
		</s:if>
	</p>

	<%-- username --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['username']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="username"><s:text name="username" /></label>
				<wpsf:textfield name="username" id="username" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- password --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['password']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="password"><s:text name="password" /></label>
				<wpsf:password name="password" id="password" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- confirm password --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['passwordConfirm']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="passwordConfirm"><s:text name="passwordConfirm" /></label>
				<wpsf:password name="passwordConfirm" id="passwordConfirm" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- active --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['active']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label class="checkbox">
					<wpsf:checkbox name="active" id="active" />
					<s:text name="note.userStatus.active" />
				</label>
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- additional info when edit mode --%>
		<s:if test="getStrutsAction() == 2">
		<div class="panel panel-default">
			<div class="panel-body">
				<%-- registration date --%>
				<div class="form-group">
					<div class="col-xs-12">
						<label><s:text name="label.date.registration" /></label>
						<p class="form-control-static">
							<s:date name="user.creationDate" format="dd/MM/yyyy HH:mm" />
							</p>
					</div>
				</div>
				<%-- last login --%>
				<div class="form-group">
					<div class="col-xs-12">
						<label><s:text name="label.date.lastLogin" /></label>
						<p class="form-control-static">
							<s:if test="user.lastAccess != null">
								<s:date name="user.lastAccess" format="dd/MM/yyyy HH:mm" />
								<s:if test="!user.accountNotExpired">
									<span class="text-muted">&#32;(<s:text name="note.userStatus.expiredAccount" />)</span>
								</s:if>
							</s:if>
							<s:else><span class="icon fa fa-minus" title="<s:text name="label.none" />"><span class="sr-only"><s:text name="label.none" /></span></span></s:else>
						</p>
					</div>
				</div>
				<%-- last password change --%>
				<div class="form-group">
					<div class="col-xs-12">
						<label><s:text name="label.date.lastPasswordChange" /></label>
						<p class="form-control-static">
							<s:if test="user.lastPasswordChange != null">
								<s:date name="user.lastPasswordChange" format="dd/MM/yyyy HH:mm" />
								<s:if test="!user.credentialsNotExpired">
									<span class="text-muted">&#32;(<s:text name="note.userStatus.expiredPassword" />)</span>
								</s:if>
							</s:if>
							<s:else><span class="icon fa fa-minus" title="<s:text name="label.none" />"><span class="sr-only"><s:text name="label.none" /></span></span></s:else>
						</p>
					</div>
				</div>
				<%-- reset info --%>
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['reset']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<div class="col-xs-12">
						<label class="checkbox">
							<wpsf:checkbox name="reset" />
							<s:text name="note.userStatus.reset" />
						</label>
						<s:if test="#fieldHasFieldErrorVar">
							<span class="help-block text-danger">
								<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
							</span>
						</s:if>
					</div>
				</div>
			</div>
		</div>
	</s:if>
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