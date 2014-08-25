<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.changePassword" />
	</span>
</h1>

<div id="main" role="main">
	<s:form action="changePassword" cssClass="form-horizontal">
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
				<wpsf:hidden name="username" />
			</p>
			<%-- old password --%>
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['oldPassword']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<div class="col-xs-12">
						<label for="oldPassword"><s:text name="label.oldPassword" /></label>
						<wpsf:password name="oldPassword" id="oldPassword" cssClass="form-control" />
						<s:if test="#fieldHasFieldErrorVar">
							<span class="help-block text-danger">
								<s:iterator value="%{#fieldFieldErrorsVar}"><s:property escape="false" />&#32;</s:iterator>
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
						<label for="password"><s:text name="label.password" /></label>
						<wpsf:password name="password" id="password" cssClass="form-control" />
						<s:if test="#fieldHasFieldErrorVar">
							<span class="help-block text-danger">
								<s:iterator value="%{#fieldFieldErrorsVar}"><s:property escape="false" />&#32;</s:iterator>
							</span>
						</s:if>
					</div>
				</div>
			<%-- password confirm --%>
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['passwordConfirm']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<div class="col-xs-12">
						<label for="passwordConfirm"><s:text name="label.passwordConfirm" /></label>
						<wpsf:password name="passwordConfirm" id="passwordConfirm" cssClass="form-control" />
						<s:if test="#fieldHasFieldErrorVar">
							<span class="help-block text-danger">
								<s:iterator value="%{#fieldFieldErrorsVar}"><s:property escape="false" />&#32;</s:iterator>
							</span>
						</s:if>
					</div>
				</div>
		<%-- save button --%>
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