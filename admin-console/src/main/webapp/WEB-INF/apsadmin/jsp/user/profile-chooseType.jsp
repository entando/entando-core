<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.userprofileManagement" />
	</span>
</h1>

<div id="main" role="main">
	<p><s:text name="title.chooseUserProfileType" />:&#32;<code><s:property value="username" /></code></p>

	<s:form action="new" cssClass="form-horizontal">
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
		<p class="sr-only">
			<wpsf:hidden name="username" />
		</p>
		<div class="form-group">
			<div class="col-xs-12">
				<label for="profileTypeCode"><s:text name="label.profile" />:</label>
				<wpsf:select list="userProfileTypes" id="profileTypeCode" name="profileTypeCode" listKey="typeCode" listValue="typeDescr" cssClass="form-control" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" title="%{getText('label.continue')}" cssClass="btn btn-primary btn-block">
					<s:text name="label.continue" />&#32;
					<span class="icon fa fa-long-arrow-right"></span>
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>