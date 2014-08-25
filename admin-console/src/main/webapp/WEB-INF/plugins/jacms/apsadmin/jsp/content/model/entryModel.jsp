<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/jacms/ContentModel" />">
		<s:text name="title.contentModels" />
		</a>&#32;/&#32;
		<s:if test="strutsAction == 1">
			<s:text name="title.contentModels.new" />
		</s:if>
		<s:if test="strutsAction == 2">
			<s:text name="title.contentModels.edit" />
		</s:if>
	</span>
</h1>

<div id="main" role="main">

<s:form action="save" namespace="/do/jacms/ContentModel" cssClass="form-horizontal" >
<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<p><s:text name="message.title.FieldErrors" /></p>
	</div>
</s:if>
<p class="sr-only">
	<wpsf:hidden name="strutsAction" />
	<s:if test="strutsAction == 2">
		<wpsf:hidden name="modelId" />
	</s:if>
</p>

<div class="panel panel-default">
	<div class="panel-body">
		<div class="form-group<s:if test="strutsAction == 1 && null == contentType"> has-warning</s:if>">
		<div class="col-xs-12">
			<label for="contentType" class="control-label"><s:text name="contentModel.type" /></label>
			<div class="input-group">
			<wpsf:select id="contentType" list="smallContentTypes" name="contentType"
				listKey="code" listValue="descr" cssClass="form-control" />
				<span class="input-group-btn">
				<s:if test="strutsAction == 1 && null == contentType">
					<wpsf:submit type="button" action="lockContentType" cssClass="btn btn-warning" value="%{getText('label.set')}"/>
				</s:if>
				<s:else>
					<wpsf:submit type="button" action="lockContentType" cssClass="btn btn-info" value="%{getText('label.change')}"/>
				</s:else>
				</span>
			</div>
			<s:if test="strutsAction == 1 && null == contentType">
			<span class="help-block"><span class="icon fa fa-info-circle"></span>&#32;<s:text name="note.contentModel.assist.intro" /></span>
			</s:if>
		</div>
		</div>
	</div>
</div>
<div class="form-group<s:property value="controlGroupErrorClassVar" />">
	<div class="col-xs-12">
		<s:set var="modelIdFieldErrorsVar" value="%{fieldErrors['modelId']}" />
		<s:set var="modelIdHasFieldErrorVar" value="#modelIdFieldErrorsVar!= null && !#modelIdFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#modelIdHasFieldErrorVar ? ' has-error' : ''}" />
		<label for="modelId"><s:text name="contentModel.id" /></label>
		<wpsf:textfield name="modelId" id="modelId" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
		<s:if test="#modelIdHasFieldErrorVar">
		  <p class="text-danger padding-small-vertical"><s:iterator value="#modelIdFieldErrorsVar"><s:property /> </s:iterator></p>
		</s:if>
	</div>
</div>
<div class="form-group<s:property value="controlGroupErrorClassVar" />">
	<div class="col-xs-12">
		<s:set var="descriptionFieldErrorsVar" value="%{fieldErrors['description']}" />
		<s:set var="descriptionHasFieldErrors" value="#descriptionFieldErrorsVar!= null && !#descriptionFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#descriptionHasFieldErrors ? ' has-error' : ''}" />
		<label for="description"><s:text name="label.description" /></label>
		<wpsf:textfield name="description" id="description" cssClass="form-control" />
		<s:if test="#descriptionHasFieldErrors">
		  <p class="text-danger padding-small-vertical"><s:iterator value="#descriptionFieldErrorsVar"><s:property /> </s:iterator></p>
		</s:if>
	</div>
</div>
<div class="form-group<s:property value="controlGroupErrorClassVar" />">
	<div class="col-xs-12">
		<s:set var="contentShapeFieldErrorsVar" value="%{fieldErrors['contentShape']}" />
		<s:set var="contentShapeHasFieldErrorVar" value="#contentShapeFieldErrorsVar != null && !#contentShapeFieldErrorsVar.isEmpty()" />
		<label for="contentShape"><s:text name="contentModel.label.shape" /></label>
		<div class="display-block">
			<s:textarea name="contentShape" id="contentShape" cols="50" rows="10" cssClass="form-control" />
		</div>
		<span class="help-block"><span class="icon fa fa-info-circle"></span>&#32;
			<s:if test="strutsAction == 2 || (strutsAction == 1 && null != contentType)">(<s:text name="note.contentModel.help" />)&#32;</s:if>
			<s:text name="note.contentModel.contentAssist" />:&#32;<em class="important"><s:text name="label.on" /></em>.&#32;
			<s:if test="strutsAction == 2 || (strutsAction == 1 && null != contentType)">[<s:text name="note.contentModel.attributeHelp" />:&#32;<em class="important"><s:text name="label.on" /></em>]</s:if>
			<s:else>[<s:text name="note.contentModel.attributeHelp" />:&#32;<em class="important"><s:text name="label.off" /></em>]</s:else>
		</span>
		<s:if test="#contentShapeHasFieldErrorVar">
		  <p class="text-danger padding-small-vertical"><s:iterator value="#contentShapeFieldErrorsVar"><s:property /> </s:iterator></p>
		</s:if>
	</div>
</div>
<div class="form-group">
	<div class="col-xs-12">
		<label for="newModel_stylesheet"><s:text name="contentModel.label.stylesheet" /></label>
		<wpsf:textfield name="stylesheet" id="newModel_stylesheet" cssClass="form-control" />
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
