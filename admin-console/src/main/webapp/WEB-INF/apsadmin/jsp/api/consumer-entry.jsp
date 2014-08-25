<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="title.apiConsumerManagement" /></a>
		&#32;/&#32;
		<s:if test="strutsAction == 1">
			<s:text name="title.apiConsumerManagement.new" />
		</s:if>
		<s:if test="strutsAction == 2">
			<s:text name="title.apiConsumerManagement.edit" />
		</s:if>
	</span>
</h1>
<div id="main" role="main">
	<s:form action="save" >
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
				<%--
				<ul class="margin-base-top">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
				--%>
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
				<s:if test="strutsAction == 2">
					<wpsf:hidden name="consumerKey" />
				</s:if>
		</p>

		<div class="col-xs-12">
			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['consumerKey']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="consumerKey"><s:text name="label.consumerKey" /></label>
				<wpsf:textfield name="consumerKey" id="consumerKey" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<span class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></span>
				</s:if>
			</div>
			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['secret']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="secret"><s:text name="label.secret" /></label>
				<wpsf:textfield name="secret" id="secret" cssClass="form-control" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<span class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></span>
				</s:if>
			</div>
			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['description']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="description"><s:text name="label.description" /></label>
				<s:textarea  cols="50" rows="3" name="description" id="description" cssClass="form-control"  />
				<s:if test="#currentFieldHasFieldErrorVar">
					<span class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></span>
				</s:if>
			</div>
			<%--
			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['callbackUrl']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="callbackUrl"><s:text name="label.callbackUrl" /></label>
				<wpsf:textfield name="callbackUrl" id="callbackUrl" cssClass="form-control" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<span class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></span>
				</s:if>
			</div>
			--%>
			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['expirationDate']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="expirationDate_cal"><s:text name="label.expirationDate" /></label>
				<wpsf:textfield name="expirationDate" id="expirationDate_cal" cssClass="form-control" />
				<span class="help help-block">dd/mm/yyyy</span>
				<s:if test="#currentFieldHasFieldErrorVar">
					<span class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></span>
				</s:if>
			</div>
		</div>
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

