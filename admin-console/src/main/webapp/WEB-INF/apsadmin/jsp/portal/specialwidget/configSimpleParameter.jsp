<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<s:text name="title.configPage" />
	</span>
</h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="currentPage.code" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
	<s:param name="selectedNode" value="currentPage.code"></s:param>
</s:action>

<s:form action="saveConfigSimpleParameter" cssClass="form-horizontal">
	<p class="sr-only">
		<wpsf:hidden name="pageCode" />
		<wpsf:hidden name="frame" />
		<wpsf:hidden name="widgetTypeCode" value="%{widget.type.code}" />
	</p>
	<div class="panel panel-default">
		<div class="panel-heading">
			<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
		</div>
		<div class="panel-body">
			<h2 class="h5 margin-small-vertical">
				<label class="sr-only"><s:text name="name.widget" /></label>
				<span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
				<s:property value="%{getTitle(widget.type.code, widget.type.titles)}" />
			</h2>
			<fieldset class="margin-base-top">
				<legend><s:text name="title.editFrame.settings" /></legend>
				<s:iterator value="widget.type.typeParameters" var="widgetParam">
					<div class="form-group">
						<div class="col-xs-12">
							<label for="config-simple-parameter-<s:property value="#widgetParam.name" />">
								<code class="label label-info" ><s:property value="#widgetParam.name" /></code>
								&#32;<s:property value="#widgetParam.descr" />
							</label>
							<wpsf:textfield id="%{'config-simple-parameter-'+#widgetParam.name}" name="%{#widgetParam.name}" value="%{widget.config[#widgetParam.name]}" cssClass="form-control" />
						</div>
					</div>
				</s:iterator>
			</fieldset>
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