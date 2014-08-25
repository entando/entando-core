<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="thirdTitleVar">
	<s:text name="title.configureLinkAttribute" />
</s:set>
<s:include value="linkAttributeConfigIntro.jsp" />
<s:include value="linkAttributeConfigReminder.jsp" />

<s:form cssClass="action-form form-horizontal">
	<p class="sr-only"><wpsf:hidden name="contentOnSessionMarker" /></p>
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h4>
			<%--
			<ul class="margin-none margin-base-top">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
									<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
			--%>
		</div>
	</s:if>
	<p class="sr-only"><s:text name="title.insertURL" /></legend></p>
	<s:set var="currentFieldErrorsVar" value="%{fieldErrors['url']}" />
	<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
	<s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error'  : ''}" />
	<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
		<div class="col-xs-12">
			<label class="display-block" for="url"><s:text name="label.url" /></label>
			<wpsf:textfield name="url" id="url" cssClass="form-control" />
			<span class="help help-block"><s:text name="note.typeValidURL" />
			<s:if test="#currentFieldHasFieldErrorVar">
				<p class="text-danger padding-small-vertical">
					<s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator>
				</p>
			</s:if>
		</div>
	</div>
	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" action="joinUrlLink" cssClass="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.confirm" />
			</wpsf:submit>
		</div>
	</div>
</s:form>