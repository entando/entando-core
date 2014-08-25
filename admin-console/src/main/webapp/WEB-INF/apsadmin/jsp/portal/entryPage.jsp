<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a>&#32;/&#32;
		<s:if test="strutsAction == 1">
			<s:text name="title.newPage" />
		</s:if>
		<s:elseif test="strutsAction == 2">
			<s:text name="title.editPage" />
		</s:elseif>
		<s:elseif test="strutsAction == 3">
			<s:text name="title.pastePage" />
		</s:elseif>
	</span>
</h1>

<div id="main" role="main">

<s:if test="strutsAction == 2"><s:set var="breadcrumbs_pivotPageCode" value="pageCode" /></s:if>
<s:else><s:set var="breadcrumbs_pivotPageCode" value="parentPageCode" /></s:else>
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:form action="save">

	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<p><s:text name="message.title.ActionErrors" /></p>
		</div>
	</s:if>
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<p><s:text name="message.title.FieldErrors" /></p>
		</div>
	</s:if>
	<p class="sr-only">
		<wpsf:hidden name="selectedNode" />
		<wpsf:hidden name="strutsAction" />
		<wpsf:hidden name="copyPageCode" />
		<wpsf:hidden name="parentPageCode" />
		<wpsf:hidden name="groupSelectLock" />
		<s:if test="strutsAction == 2">
			<wpsf:hidden name="pageCode" />
		</s:if>
		<s:iterator value="extraGroups" id="groupName"><wpsf:hidden name="extraGroups" value="%{#groupName}" /></s:iterator>
		<s:if test="strutsAction == 3">
			<wpsf:hidden name="group" />
			<wpsf:hidden name="model" />
			<wpsf:hidden name="defaultShowlet" />
			<wpsf:hidden name="showable" />
			<wpsf:hidden name="useExtraTitles" />
			<wpsf:hidden name="charset" />
			<wpsf:hidden name="mimeType" />
		</s:if>
	</p>
	<fieldset class="col-xs-12"><legend><s:text name="label.info" /></legend>

	<s:set var="controlGroupErrorClassVar" value="''" />
	<s:if test="#pageCodeHasFieldErrorVar">
	  <s:set var="controlGroupErrorClassVar" value="' has-error'" />
	</s:if>

	<div class="form-group<s:property value="controlGroupErrorClassVar" />">
		<s:set var="pageCodeFieldErrorsVar" value="%{fieldErrors['pageCode']}" />
		<s:set var="pageCodeHasFieldErrorVar" value="#pageCodeFieldErrorsVar != null && !#pageCodeFieldErrorsVar.isEmpty()" />
		<label for="pageCode"><s:text name="name.pageCode" /></label>
		<wpsf:textfield name="pageCode" id="pageCode" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
		<s:if test="#pageCodeHasFieldErrorVar">
		  <p class="text-danger padding-small-vertical"><s:iterator value="#pageCodeFieldErrorsVar"><s:property /> </s:iterator></p>
		</s:if>
	</div>
	<s:iterator value="langs">
	<div class="form-group">
		<s:set var="titleFieldErrorsVar" value="%{fieldErrors['lang'+code]}" />
		<s:set var="titleHasFieldErrorVar" value="#titleFieldErrorsVar != null && !#titleFieldErrorsVar.isEmpty()" />
		<label for="lang<s:property value="code" />"><abbr title="<s:property value="descr" />"><code class="label label-info" ><s:property value="code" /></code></abbr>&#32;<s:text name="name.pageTitle" /></label>
		<wpsf:textfield name="%{'lang'+code}" id="%{'lang'+code}" value="%{titles.get(code)}" cssClass="form-control" />
		<s:if test="#titleHasFieldErrorVar">
		  <p class="text-danger padding-small-vertical"><s:iterator value="#titleFieldErrorsVar"><s:property /> </s:iterator></p>
		</s:if>
	</div>
	</s:iterator>

	<s:if test="strutsAction == 3">
	</fieldset>
	</s:if>

<s:if test="strutsAction != 3">

	<div class="form-group">
		<label for="group"><s:text name="label.ownerGroup" /></label>
		<wpsf:select name="group" id="group" list="allowedGroups" listKey="name" listValue="descr" disabled="%{groupSelectLock}" cssClass="form-control"></wpsf:select>
		<s:if test="groupSelectLock"><p class="sr-only"><wpsf:hidden name="group" /></p></s:if>
	</div>
	</fieldset>

<fieldset class="col-xs-12"><legend><s:text name="label.extraGroups" /></legend>
<div class="form-group">
	<label for="extraGroups"><s:text name="label.join" />&#32;<s:text name="label.group" /></label>
	<div class="input-group">
		<wpsf:select name="extraGroupName" id="extraGroups" list="groups"
			listKey="name" listValue="descr" cssClass="form-control" />
		<span class="input-group-btn">
			<wpsf:submit type="button" action="joinExtraGroup" cssClass="btn btn-default">
				<span class="icon fa fa-plus"></span>&#32;
				<s:property value="label.join" />
			</wpsf:submit>
		</span>
	</div>
</div>
<s:if test="extraGroups.size() != 0">
	<s:iterator value="extraGroups" id="groupName">
		<wpsa:actionParam action="removeExtraGroup" var="actionName" >
			<wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
		</wpsa:actionParam>
		<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
			<span class="icon fa fa-tag"></span>&#32;
			<s:property value="%{getSystemGroups()[#groupName].getDescr()}"/>&#32;
			<wpsf:submit type="button" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-default btn-xs badge">
				<span class="icon fa fa-times"></span>
				<span class="sr-only">x</span>
			</wpsf:submit>
		</span>
	</s:iterator>
</s:if>

</fieldset>

<fieldset class="col-xs-12"><legend><s:text name="label.settings" /></legend>

<div class="form-group" id="pagemodel">
	<label for="model"><s:text name="name.pageModel" /></label>
	<wpsf:select name="model" id="model" list="pageModels" listKey="code" listValue="descr" cssClass="form-control"></wpsf:select>
</div>

<ul>
	<li class="checkbox">
		<label for="defaultShowlet"><s:text name="name.hasDefaultWidgets" /><wpsf:checkbox name="defaultShowlet" id="defaultShowlet" /></label>
	</li>
	<li class="checkbox">
		<label for="viewerPage"><s:text name="name.isViewerPage" /><wpsf:checkbox name="viewerPage" id="viewerPage" /></label>
	</li>
	<li class="checkbox">
		<label for="showable"><s:text name="name.isShowablePage" /><wpsf:checkbox name="showable" id="showable" /></label>
	</li>
	<li class="checkbox">
		<label for="useExtraTitles"><abbr lang="en" title="<s:text name="name.SEO.full" />"><s:text name="name.SEO.short" /></abbr>:&#32;<s:text name="name.useBetterTitles" /><wpsf:checkbox name="useExtraTitles" id="useExtraTitles" /></label>
	</li>
</ul>

<div class="form-group">
	<label for="charset"><s:text name="name.charset" /></label>
	<wpsf:select name="charset" id="charset"
				 headerKey="" headerValue="%{getText('label.default')}" list="allowedCharsets" cssClass="form-control" />
</div>

<div class="form-group">
	<label for="mimeType"><s:text name="name.mimeType" /></label>
	<wpsf:select name="mimeType" id="mimeType"
				 headerKey="" headerValue="%{getText('label.default')}" list="allowedMimeTypes" cssClass="form-control" />
</div>

</fieldset>

</s:if>

	<wpsa:hookPoint key="core.entryPage" objectName="hookPointElements_core_entryPage">
	<s:iterator value="#hookPointElements_core_entryPage" var="hookPointElement">
		<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
	</s:iterator>
	</wpsa:hookPoint>

	<div class="form-horizontal">
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-floppy-o"></span>&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</div>
</s:form>

</div>