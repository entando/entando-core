<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="title.guiFragmentManagement" /></a>
		&#32;/&#32;
		<s:if test="getStrutsAction() == 1">
			<s:text name="guiFragment.label.new" />
		</s:if>
		<s:elseif test="getStrutsAction() == 2">
			<s:text name="guiFragment.label.edit" />
		</s:elseif>
	</span>
</h1>
<div id="main">
	<s:form action="save" cssClass="form-horizontal">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<p><s:text name="message.title.FieldErrors" /></p>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon icon-remove"></span></button>
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
			<s:if test="getStrutsAction() == 2">
				<wpsf:hidden name="code" />
				<wpsf:hidden name="widgetTypeCode" />
				<wpsf:hidden name="pluginCode" />
				<wpsf:hidden name="defaultGui" />
			</s:if>
		</p>

		<%-- code --%>
			<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['code']}" />
			<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<div class="col-xs-12">
					<label for="guiFragment_code"><s:text name="label.code" /></label>
					<wpsf:textfield disabled="%{getStrutsAction() == 2}" name="code" id="guiFragment_code" cssClass="form-control" />
					<s:if test="#fieldHasFieldErrorVar">
						<p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
					</s:if>
				</div>
			</div>

		<%-- widgetTypeCode --%>
			<s:if test="%{widgetTypeCode!=null}">
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<div class="col-xs-12">
						<label for="guiFragment_widgetTypeCode"><s:text name="label.widgetType" /></label>
						<s:set value="%{getWidgetType(widgetTypeCode)}" var="widgetTypeVar" />
						<s:property value="getTitle(#widgetTypeVar.code, #widgetTypeVar.titles)" />
					</div>
				</div>
			</s:if>


		<%-- pluginCode --%>
			<s:if test="%{pluginCode != null}">
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<div class="col-xs-12">
						<label for="guiFragment_pluginCode"><s:text name="label.pluginCode" /></label>
						<s:property value="pluginCode" />
					</div>
				</div>
			</s:if>


		<!-- Nav tabs -->
		<ul id="gui-tab" class="nav nav-tabs">
			<li class="active"><a href="#gui-edit" data-toggle="tab">Gui Code</a></li>
			<li><a href="#gui-default" data-toggle="tab">Default Gui Code</a></li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content margin-large-bottom">
			<div class="tab-pane active" id="gui-edit">
							<%-- gui --%>
								<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['gui']}" />
								<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
								<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
								<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
									<div class="col-xs-12">
										<label for="guiFragment_gui" class="sr-only"><s:text name="label.gui" /></label>
										<wpsf:textarea name="gui" id="guiFragment_gui" cssClass="form-control" rows="8" cols="50" />
										<s:if test="#fieldHasFieldErrorVar">
											<p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
										</s:if>
									</div>
								</div>
			</div>
			<div class="tab-pane" id="gui-default">
						<%-- defaultGui --%>
							<s:if test="null != defaultGui">
								<pre><code>
									<s:set var="defguiVar"><s:property value="defaultGui" /></s:set>
									<s:property
										value="#defguiVar.replaceAll('\t', '&emsp;').replaceAll('\r', '').replaceAll('\n', '<br />')"
										escapeCsv="false"
										escapeHtml="false"
										escapeJavaScript="false"
										escapeXml="false"
									 />
								</code></pre>
							</s:if>
							<s:else>
								<div class="margin-none alert alert-info">
									Not available.
								</div>
							</s:else>
					</div>
		</div>



		<%-- save button --%>
			<div class="form-group">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<s:submit type="button" action="save" cssClass="btn btn-primary btn-block">
						<span class="icon icon-save"></span>&#32;
						<s:text name="label.save" />
					</s:submit>
				</div>
			</div>

	</s:form>
</div>

<script>
$('#gui-tab a').click(function (e) {
	e.preventDefault()
	$(this).tab('show')
})
</script>
