<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.myProfile" />
	</span>
</h1>

<div id="main" role="main">

<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
		<ul>
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
		<ul>
		<s:iterator value="actionErrors">
			<li><s:property escape="false" /></li>
		</s:iterator>
		</ul>
	</div>
</s:if>

<s:if test="hasActionMessages()">
	<div class="alert alert-success alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h3>
		<ul>
			<s:iterator value="actionMessages">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>

<s:form namespace="/do/CurrentUser" action="changePassword" cssClass="form-horizontal">

<div class="col-xs-12">

	<p class="sr-only">
		<wpsf:hidden name="username" />
	</p>

	<div class="form-group">
		<label for="oldPassword"><s:text name="label.oldPassword" />&#32;<span class="icon fa fa-asterisk text-muted" title="<s:text name="Entity.attribute.flag.mandatory.full" />"></span></label>
		<wpsf:password name="oldPassword" id="oldPassword" cssClass="form-control" />
	</div>

	<div class="form-group">
		<label for="password" ><s:text name="label.password" />&#32;<span class="icon fa fa-asterisk text-muted" title="<s:text name="Entity.attribute.flag.mandatory.full" />"></span></label>
		<wpsf:password name="password" id="password" cssClass="form-control" />
	</div>

	<div class="form-group">
		<label for="passwordConfirm"><s:text name="label.passwordConfirm" />&#32;<span class="icon fa fa-asterisk text-muted" title="<s:text name="Entity.attribute.flag.mandatory.full" />"></span></label>
		<wpsf:password name="passwordConfirm" id="passwordConfirm" cssClass="form-control" />
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

<div class="clearfix"></div>

<s:action name="edit" namespace="/do/currentuser/profile" executeResult="true"></s:action>

<wpsa:hookPoint key="core.userProfile.entry" objectName="hookPointElements_core_userProfile_entry">
<s:iterator value="#hookPointElements_core_userProfile_entry" var="hookPointElement">
	<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
</s:iterator>
</wpsa:hookPoint>

</div>