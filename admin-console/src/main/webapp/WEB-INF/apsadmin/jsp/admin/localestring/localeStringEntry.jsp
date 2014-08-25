<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>&#32;/&#32;
		<a href="<s:url namespace="/do/LocaleString" action="list" />"><s:text name="title.languageAdmin.labels" /></a>&#32;/&#32;
		<s:if test="getStrutsAction() == 1"><s:text name="title.generalSettings.locale.new" /></s:if>
		<s:elseif test="getStrutsAction() == 2"><s:text name="title.generalSettings.locale.edit" /> </s:elseif>
	</span>
</h1>
<s:form action="save" namespace="/do/LocaleString" cssClass="form-horizontal">
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none">
				<s:text name="message.title.FieldErrors" />
			</h2>
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
	<p class="sr-only">
		<wpsf:hidden value="%{getStrutsAction()}" name="strutsAction"/>
		<s:if test="getStrutsAction() == 2">
			<wpsf:hidden value="%{key}" name="key" />
		</s:if>
	</p>
	<s:set var="keyFieldErrorsVar" value="%{fieldErrors['key']}" />
	<s:set var="keyHasFieldErrorVar" value="#keyFieldErrorsVar != null && !#keyFieldErrorsVar.isEmpty()" />
  <s:set var="controlGroupErrorClassVar" value="%{#keyHasFieldErrorVar ? ' has-error' : ''}" />
	<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
		<div class="col-xs-12">
			<label for="editLabel_key"><s:text name="label.code" /></label>
			<wpsf:textfield value="%{key}" name="key" id="editLabel_key" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
			<s:if test="#keyHasFieldErrorVar">
				<p class="text-danger padding-small-vertical"><s:iterator value="#keyFieldErrorsVar"><s:property />&#32;</s:iterator></p>
			</s:if>
		</div>
	</div>
	<s:iterator value="langs" var="l">
		<s:if test="#l.default">
			<s:set var="currentFieldErrorsVar" value="%{fieldErrors[#l.code]}" />
			<s:set var="currentHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#currentHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<div class="col-xs-12">
					<label for="lang<s:property value="#l.code"/>">
						<%-- (<s:property value="#l.code" />)&#32;<s:text name="label.description" /> --%>
						<span lang="<s:property value="#l.code" />"><s:property value="#l.descr" /></span>&#32;
						<span class="label label-success"><s:text name="label.default" /></span>
					</label>
					<s:textarea cols="50" rows="3" name="%{code}" id="%{'lang'+code}" value="%{labels[#l.code]}" cssClass="form-control" />
					<s:if test="#currentHasFieldErrorVar">
						<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></p>
					</s:if>
				</div>
			</div>
		</s:if>
	</s:iterator>
	<s:if test="%{langs.size() > 1}">
		<s:iterator value="langs" var="l">
			<s:if test="! #l.default">
				<s:set var="currentFieldErrorsVar" value="%{fieldErrors[#l.code]}" />
				<s:set var="currentHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#currentHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<div class="col-xs-12">
						<label for="lang<s:property value="#l.code"/>">
							<%-- (<s:property value="#l.code" />)</span>&#32;<s:text name="label.description" />--%>
							<span lang="<s:property value="#l.code" />"><s:property value="#l.descr" /></span>
						</label>
						<s:textarea cols="50" rows="3" name="%{code}" id="%{'lang'+code}" value="%{labels[#l.code]}" cssClass="form-control" />
						<s:if test="#currentHasFieldErrorVar">
							<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator></p>
						</s:if>
					</div>
				</div>
			</s:if>
		</s:iterator>
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