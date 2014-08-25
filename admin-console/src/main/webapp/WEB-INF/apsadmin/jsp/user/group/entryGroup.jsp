<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/Group"></s:url>"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.groupManagement" />">
			<s:text name="title.groupManagement" /></a>
		&#32;/&#32;
		<s:if test="getStrutsAction() == 1">
			<s:text name="title.groupManagement.groupNew" />
		</s:if>
		<s:if test="getStrutsAction() == 2">
			<s:text name="title.groupManagement.groupEdit" />
		</s:if>
	</span>
</h1>
<s:form action="save" cssClass="form-horizontal" >
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
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
			<%--<ul class="margin-base-top">
				<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
			</ul>--%>
		</div>
	</s:if>
	<p class="sr-only">
		<wpsf:hidden name="strutsAction" />
		<s:if test="getStrutsAction() == 2">
			<wpsf:hidden name="name" />
		</s:if>
	</p>
	<%-- name --%>
	<s:set var="fieldErrorsVar" value="%{fieldErrors['name']}" />
	<s:set var="fieldHasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
	<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
	<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
		<div class="col-xs-12">
			<label for="name"><s:text name="name" /></label>
			<wpsf:textfield name="name" id="name" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
			<s:if test="#fieldHasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="#fieldErrorsVar"><s:property />&#32;</s:iterator>
				</span>
			</s:if>
		</div>
	</div>
	<%-- description --%>
		<s:set var="fieldErrorsVar" value="%{fieldErrors['description']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="description"><s:text name="description" /></label>
				<wpsf:textfield name="description" id="description" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="#fieldErrorsVar"><s:property />&#32;</s:iterator>
				</span>
			</s:if>
			</div>
		</div>
	<%-- save --%>
    <div class="form-group">
      <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
        <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
          <span class="icon fa fa-floppy-o"></span>&#32;
          <s:text name="label.save" />
        </wpsf:submit>
      </div>
    </div>
</s:form>