<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" />">
		<s:text name="title.widgetManagement" /></a>
		<s:if test="strutsAction == 2">
		&#32;/&#32;<s:text name="title.widgetManagement.edit" />
		&#32;/&#32;<wpsa:widgetType key="%{widgetTypeCode}" var="widgetTypeVar" />
			<s:property value="#widgetTypeVar.titles[currentLang.code]" />
		</s:if>
		<s:else>&#32;/&#32;
			<s:text name="title.newWidgetType" />
		</s:else>
	</span>
</h1>

<div id="main" role="main">
<p>
<s:if test="strutsAction == 1">
	<s:text name="title.newWidgetType" />
</s:if>
<s:elseif test="strutsAction != 2">
	<s:text name="title.newWidgetType.from" />:&#32;
	<s:if test="strutsAction == 5">
		<wpsa:widgetType var="parentWidgetTypeVar" key="%{parentWidgetTypeCode}" />
		<em><s:property value="%{getTitle(#parentWidgetTypeVar.code, #parentWidgetTypeVar.titles)}" /></em>
	</s:if>
	<s:elseif test="strutsAction == 3">
	<s:property value="%{getTitle(showletToCopy.type.code, showletToCopy.type.titles)}" />	<wpsa:page var="pageVar" key="%{pageCode}" />
	<s:text name="note.widgetType.page"/>:&#32;<em class="important"><s:property value="%{getTitle(#pageVar.code, #pageVar.titles)}" /></em>,&#32;<s:text name="note.widgetType.position" />:&#32;<em class="important"><s:property value="framePos" /></em>
	</s:elseif>
</s:elseif>
</p>
<s:form action="save" namespace="/do/Portal/WidgetType" >

<wp:ifauthorized permission="superuser"><s:set var="isSuperuserVar" value="%{true}" /></wp:ifauthorized>

	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<p><s:text name="message.title.ActionErrors" /></p>
				<ul>
							<s:iterator value="actionErrors">
										<li><s:property escape="false" /></li>
							</s:iterator>
						</ul>
		</div>
	</s:if>
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<p><s:text name="message.title.FieldErrors" /></p>
		</div>
	</s:if>

<p class="sr-only">
	<wpsf:hidden name="strutsAction" />
	<s:if test="strutsAction == 5">
		<wpsf:hidden name="parentWidgetTypeCode" />
	</s:if>
	<s:elseif test="strutsAction == 2">
		<wpsf:hidden name="widgetTypeCode" />
	</s:elseif>
	<s:elseif test="strutsAction == 3">
		<wpsf:hidden name="pageCode" />
		<wpsf:hidden name="framePos" />
	</s:elseif>
</p>

<fieldset class="col-xs-12"><legend><s:text name="label.info" /></legend>
	<s:set var="controlGroupErrorClassVar" value="''" />
	<s:if test="#pageCodeHasFieldErrorVar">
		<s:set var="controlGroupErrorClassVar" value="' has-error'" />
	</s:if>

	<s:if test="strutsAction != 2">
		<div class="form-group<s:property value="controlGroupErrorClassVar" />">
			<s:set var="pageCodeFieldErrorsVar" value="%{fieldErrors['showletTypeCode']}" />
			<s:set var="pageCodeHasFieldErrorVar" value="#pageCodeFieldErrorsVar != null && !#pageCodeFieldErrorsVar.isEmpty()" />
			<label for="showletTypeCode" class="control-label"><s:text name="label.code" /></label>
			<wpsf:textfield id="showletTypeCode" name="showletTypeCode" cssClass="form-control" />
			<s:if test="#pageCodeHasFieldErrorVar">
				<p class="text-danger padding-small-vertical"><s:iterator value="#pageCodeFieldErrorsVar"><s:property /> </s:iterator></p>
			</s:if>
		</div>
	</s:if>

	<div class="form-group<s:property value="controlGroupErrorClassVar" />">
			<s:set var="pageCodeFieldErrorsVar" value="%{fieldErrors['englishTitle']}" />
			<s:set var="pageCodeHasFieldErrorVar" value="#pageCodeFieldErrorsVar != null && !#pageCodeFieldErrorsVar.isEmpty()" />
		<label for="englishTitle" class="control-label"><code class="label label-info">en</code>&#32;<s:text name="label.title" /></label>
		<wpsf:textfield id="englishTitle" name="englishTitle" cssClass="form-control" />
		<s:if test="#pageCodeHasFieldErrorVar">
			<p class="text-danger padding-small-vertical"><s:iterator value="#pageCodeFieldErrorsVar"><s:property /> </s:iterator></p>
		</s:if>
	</div>

	<div class="form-group<s:property value="controlGroupErrorClassVar" />">
		<s:set var="pageCodeFieldErrorsVar" value="%{fieldErrors['italianTitle']}" />
		<s:set var="pageCodeHasFieldErrorVar" value="#pageCodeFieldErrorsVar != null && !#pageCodeFieldErrorsVar.isEmpty()" />
		<label for="italianTitle" class="control-label"><code class="label label-info">it</code>&#32;<s:text name="label.title" /></label>
		<wpsf:textfield id="italianTitle" name="italianTitle" cssClass="form-control" />
		<s:if test="#pageCodeHasFieldErrorVar">
			<p class="text-danger padding-small-vertical"><s:iterator value="#pageCodeFieldErrorsVar"><s:property /> </s:iterator></p>
		</s:if>
	</div>

	<div class="form-group<s:property value="controlGroupErrorClassVar" />">
		<s:set var="pageCodeFieldErrorsVar" value="%{fieldErrors['mainGroup']}" />
		<s:set var="pageCodeHasFieldErrorVar" value="#pageCodeFieldErrorsVar != null && !#pageCodeFieldErrorsVar.isEmpty()" />
		<label for="mainGroup" class="control-label"><s:text name="label.group" /></label>
		<wpsf:select name="mainGroup" id="mainGroup" list="groups" listKey="name" listValue="descr" cssClass="form-control" disabled="%{!#isSuperuserVar}" />
		<s:if test="#pageCodeHasFieldErrorVar">
			<p class="text-danger padding-small-vertical"><s:iterator value="#pageCodeFieldErrorsVar"><s:property /> </s:iterator></p>
		</s:if>
	</div>

	<s:if test="null != #widgetTypeVar && #widgetTypeVar.logic && strutsAction == 2">
	<div class="form-group">
		<label class="control-label"><s:text name="label.widgetType.parentType" /></label>
		<p class="form-control-static"><s:property value="#widgetTypeVar.parentType.titles[currentLang.code]" /></p>
	</div>
	</s:if>

</fieldset>

<s:if test="strutsAction != 1 && (strutsAction != 2 || #widgetTypeVar.logic)">
<fieldset class="col-xs-12"><legend><s:text name="title.widgetType.settings" /></legend>
	<s:if test="strutsAction == 5">
		<s:set var="parentWidgetTypeVar" value="%{getWidgetType(parentShowletTypeCode)}" />
		<s:iterator value="#parentWidgetTypeVar.typeParameters" var="widgetParamVar" >
			<div class="form-group">
				<label for="<s:property value="#widgetParamVar.name" />" class="control-label"><s:property value="#widgetParamVar.name" /></label>
				<wpsf:textfield id="%{#widgetParamVar.name}" name="%{#widgetParamVar.name}" value="%{#request.parameters[#widgetParamVar.name]}" cssClass="form-control" />
				<s:if test="#widgetParamVar.descr != ''">
					<span class="help-block"><span class="icon fa fa-info-circle"></span>&#32;
					<s:property value="#widgetParamVar.descr" />
					</span>
				</s:if>
			</div>
		</s:iterator>
	</s:if>
	<s:elseif test="strutsAction == 2">
		<s:iterator value="#widgetTypeVar.parentType.typeParameters" var="widgetParamVar" >
			<div class="form-group">
				<s:if test="#isSuperuserVar && #widgetTypeVar.userType">
				<label for="<s:property value="#widgetParamVar.name" />" class="control-label"><s:property value="#widgetParamVar.name" /></label>
				<wpsf:textfield id="%{#widgetParamVar.name}" name="%{#widgetParamVar.name}" value="%{#widgetTypeVar.config[#widgetParamVar.name]}" cssClass="form-control" />
				</s:if>
				<s:else>
				<span class="text-important"><s:property value="#widgetParamVar.name" /></span>&#32;
				<s:property value="%{#widgetTypeVar.config[#widgetParamVar.name]}" />
				</s:else>
				<s:if test="#widgetParamVar.descr != ''">
					<span class="help-block"><span class="icon fa fa-info-circle"></span>&#32;<s:property value="#widgetParamVar.descr" /></span>
				</s:if>
			</div>
		</s:iterator>
		<s:set var="isSuperuserVar" value="%{false}" />
	</s:elseif>
	<s:elseif test="strutsAction == 3">
		<s:iterator value="showletToCopy.type.typeParameters" var="widgetParamVar" >
			<div class="form-group">
				<s:if test="#widgetParamVar.descr != ''">
					<label class="label-control"><s:property value="#widgetParamVar.descr" /></label>
				</s:if>
				<p class="form-control-static">
				<span class="text-strong">
				<s:property value="#widgetParamVar.name" />:&#32;
				</span>
				<s:property value="%{showletToCopy.config[#widgetParamVar.name]}" />
				</p>
			</div>
		</s:iterator>
	</s:elseif>
</fieldset>
</s:if>

<s:if test="strutsAction != 3 && strutsAction != 5">
	<s:if test="%{null == #widgetTypeVar || (!#widgetTypeVar.logic && !isInternalServletWidget(#widgetTypeVar.code))}">
		<ul class="nav nav-tabs">
			<li class="active"><a href="#widget-gui" data-toggle="tab">Custom <abbr title="User Interface">UI</abbr></a></li>
			<s:if test="strutsAction == 2">
				<s:set var="uniqueGuiFragmentVar" value="%{extractUniqueGuiFragment(widgetTypeCode)}" />
				<s:if test="%{null != #uniqueGuiFragmentVar}">
					<li><a href="#widget-default-gui" data-toggle="tab">Default <abbr title="User Interface">UI</abbr></a></li>
				</s:if>
			</s:if>
		</ul>
		<div class="tab-content margin-large-bottom">
			<div class="tab-pane fade in active" id="widget-gui">
				<wpsf:textarea name="gui" id="widget_gui" cssClass="form-control" rows="8" cols="50" />
			</div>
			<div class="tab-pane fade" id="widget-default-gui">
				<s:if test="strutsAction == 2">
					<s:set var="uniqueGuiFragmentVar" value="%{extractUniqueGuiFragment(widgetTypeCode)}" />
					<s:if test="%{null != #uniqueGuiFragmentVar && null != #uniqueGuiFragmentVar.defaultGui}">
						<div class="panel panel-default">
							<div class="panel-body">
								<pre><s:property value="#uniqueGuiFragmentVar.defaultGui" /></pre>
							</div>
						</div>
					</s:if>
					<s:else>
						<div class="margin-none alert alert-info">
							Not available.
						</div>
					</s:else>
				</s:if>
			</div>
		</div>
	</s:if>
	<s:elseif test="%{null != #widgetTypeVar && #widgetTypeVar.logic}"> <%-- excluded clause <<&& isInternalServletWidget(#widgetTypeVar.parentType.code)>> --%>
		<s:set var="guiFragmentCodesVar" value="%{extractGuiFragmentCodes(#widgetTypeVar.code)}" />
		<s:if test="%{null != #guiFragmentCodesVar && !#guiFragmentCodesVar.isEmpty()}">
			<fieldset class="margin-large-bottom">
				<legend><abbr title="User Interfaces">UIs</abbr></legend>
				<div class="panel-group" id="accordion">
					<s:iterator value="guiFragmentCodesVar" var="guiFragmentCodeVar" status="status">
						<s:set var="guiFragmentVar" value="getGuiFragment(#guiFragmentCodeVar)" />
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a class="display-block" data-toggle="collapse" data-parent="#accordion" href="#collapse-<s:property value="#status.count" />">
										<s:property value="#guiFragmentCodeVar" />
									</a>
								</h4>
							</div>
							<div id="collapse-<s:property value="#status.count" />" class="panel-collapse collapse in">
								<div class="panel-body">
									<ul class="nav nav-tabs">
										<li class="active"><a href="#widget-gui-<s:property value="#status.count" />" data-toggle="tab">Custom <abbr title="User Interface">UI</abbr></a></li>
										<li><a href="#widget-default-gui-<s:property value="#status.count" />" data-toggle="tab">Default <abbr title="User Interface">UI</abbr></a></li>
									</ul>
									<div class="tab-content margin-large-bottom">
										<div class="tab-pane fade in active" id="widget-gui-<s:property value="#status.count" />">
											<s:set var="guiFieldNameVar" value="%{#widgetTypeVar.code + '_' + #guiFragmentCodeVar}" />
											<wpsf:textarea name="%{#guiFieldNameVar}" id="%{#guiFieldNameVar}" value="%{guis.getProperty(#guiFieldNameVar)}" cssClass="form-control" rows="8" cols="50" />
										</div>
										<div class="tab-pane fade" id="widget-default-gui-<s:property value="#status.count" />">
											<div class="panel panel-default">
												<div class="panel-body">
													<pre><s:property value="#guiFragmentVar.defaultGui" /></pre>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</s:iterator>
				</div>
			</fieldset>
		</s:if>
	</s:elseif>
</s:if>

<wpsa:hookPoint key="core.widgetType.entry" objectName="hookPointElements_core_widget_entry">
<s:iterator value="#hookPointElements_core_widget_entry" var="hookPointElement">
	<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
</s:iterator>
</wpsa:hookPoint>

<%-- deprecated --%>
<wpsa:hookPoint key="core.showletType.entry" objectName="hookPointElements_core_showlet_entry">
<s:iterator value="#hookPointElements_core_showlet_entry" var="hookPointElement">
	<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
</s:iterator>
</wpsa:hookPoint>

<div class="row">
	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.save" />
			</wpsf:submit>
		</div>
		<s:if test="strutsAction == 3">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsa:actionParam action="save" var="actionName" >
					<wpsa:actionSubParam name="replaceOnPage" value="true" />
				</wpsa:actionParam>
				<wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-default btn-block">
					<span class="icon fa fa-exchange"></span>&#32;
					<s:text name="label.save.replace" />
				</wpsf:submit>
			</div>
		</s:if>
	</div>
</div>

</s:form>
</div>