<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"  %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<s:text name="title.languageAdmin" /></span>
</h1>
<s:form action="add" cssClass="form-horizontal">
	<%-- <p><s:text name="title.languageAdmin.languages" /></p> --%>
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
	<div class="form-group">
		<div class="col-xs-12">
			<label for="langCode"><s:text name="name.chooseALanguage" /></label>
			<div class="input-group">
				<select name="langCode" id="langCode" class="form-control">
					<s:iterator id="lang" value="assignableLangs">
					<option value="<s:property value="#lang.code"/>"><s:property value="#lang.code"/> &ndash; <s:property value="#lang.descr"/></option>
					</s:iterator>
				</select>
				<div class="input-group-btn">
					<wpsf:submit type="button" cssClass="btn btn-primary" >
						<span class="icon fa fa-plus-square"></span>&#32;
						<s:text name="label.add" />
					</wpsf:submit>
				</div>
			</div>
		</div>
	</div>
	<table class="table table-bordered table-hover margin-large-top">
		<tr>
			<th class="text-center"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
			<th><s:text name="label.code" /></th>
			<th><s:text name="label.description" /></th>
		</tr>
		<s:iterator id="lang" value="langs">
		<tr>
			<td class="text-center">
				<a
					class="btn btn-xs btn-warning"
					href="<s:url action="remove"><s:param name="langCode" value="#lang.code"/></s:url>"
					title="<s:text name="label.remove" />: <s:property value="#lang.descr" />">
					<span class="icon fa fa-times-circle-o"></span>
					<span class="sr-only"><s:text name="label.alt.clear" /></span>
				</a>
			</td>
			<td>
				<s:set var="labelModifier" value="'default'" />
				<s:set var="labelTitle" value="''" />
				<s:if test="#lang.default">
					<s:set var="labelModifier" value="'success'" />
					<s:set var="labelTitle">
						title="<s:text name="label.default" />"
					</s:set>
				</s:if>
				<code class="label label-<s:property value="labelModifier" />" <s:property value="labelTitle" />><s:property value="#lang.code" /></code>
			</td>
			<td><s:property value="#lang.descr" /></td>
		</tr>
		</s:iterator>
	</table>
</s:form>