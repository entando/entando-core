<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<a href="
						<s:url action="configure" namespace="/do/Page">
							<s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
						</s:url>
			 " title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text name="title.configPage" /></a>&#32;/&#32;
		<s:text name="name.widget" />
</h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

<s:form action="saveViewerConfig" namespace="/do/jacms/Page/SpecialWidget/Viewer" cssClass="form-horizontal">

<div class="panel panel-default">
	<div class="panel-heading">
		<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
	</div>

	<div class="panel-body">

		<h2 class="h5 margin-small-vertical">
			<label class="sr-only"><s:text name="name.widget" /></label>
			<span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
			<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
		</h2>

		<p class="sr-only">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
		</p>

			<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
			<ul class="margin-base-vertical">
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
				<li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
			</ul>
		</div>
			</s:if>

		<s:set name="showletParams" value="showlet.type.parameter" />

<%--
		<s:property value="#showletParams['contentId'].descr" />

		<h4><s:text name="title.configContentViewer.settings" /></h4>
--%>

		<s:if test="showlet.config['contentId'] != null">
			<s:set name="content" value="%{getContentVo(showlet.config['contentId'])}"></s:set>
			<s:set var="canEditCurrentContent" value="%{false}" />
			<c:set var="currentContentGroup"><s:property value="#content.mainGroupCode" escape="false"/></c:set>

			<wp:ifauthorized groupName="${currentContentGroup}" permission="editContents"><s:set var="canEditCurrentContent" value="%{true}" /></wp:ifauthorized>

			<fieldset class="margin-large-top"><legend><s:text name="label.info" /></legend>

				<div class="form-group">
					<div class="col-xs-12">
						<label class="control-label"><s:text name="label.content" /></label>
						<p class="form-control-static">
							<code><s:property value="#content.id" /></code>&#32;&mdash;&#32;
							<s:if test="#canEditCurrentContent">
								<a href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="#content.descr"/>"><s:property value="#content.descr"/></a>
							</s:if>
							<s:else>
								<s:property value="#content.descr" />
							</s:else>
						</p>
					</div>
				</div>

				<p class="sr-only">
					<wpsf:hidden name="contentId" value="%{getShowlet().getConfig().get('contentId')}" />
				</p>

				<div class="form-group">
					<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
						<wpsf:submit action="searchContents" value="%{getText('label.change')}" cssClass="btn btn-info btn-block" />
					</div>
				</div>

			</fieldset>

			<fieldset class="margin-large-top">
				<legend data-toggle="collapse" data-target="#options-publishing"><s:text name="title.publishingOptions" />&#32;<span class="icon fa fa-chevron-down"></span></legend>

				<div class="collapse" id="options-publishing">
					<div class="form-group">
						<div class="col-xs-12">
							<label for="modelId" class="control-label"><s:text name="label.contentModel" /></label>
							<wpsf:select id="modelId" name="modelId" value="%{getShowlet().getConfig().get('modelId')}"
							list="%{getModelsForContent(showlet.config['contentId'])}" headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="form-control" />
						</div>
					</div>
				</div>
			</fieldset>

			<%--
				Uncomment this if you add some custom parameters to this Widget

			<s:set var="showletTypeParameters" value="showlet.type.typeParameters"></s:set>
			<s:if test="#showletTypeParameters.size()>2">
			<fieldset class="col-xs-12 margin-large-top"><legend><s:text name="label.otherSettings" /></legend>
				<s:iterator value="#showletTypeParameters" id="showletParam" >
					<s:if test="!#showletParam.name.equals('contentId') && !#showletParam.name.equals('modelId')">
						<div class="form-group">
							<label for="fagianoParam_<s:property value="#showletParam.name" />" class="control-label"><s:property value="#showletParam.descr" /></label>
							<wpsf:textfield cssClass="form-control" id="%{'fagianoParam_'+#showletParam.name}" name="%{#showletParam.name}" value="%{showlet.config[#showletParam.name]}" />
						</div>
					</s:if>
				</s:iterator>
			</fieldset>
			</s:if>
			--%>

		</s:if>
		<s:else>

			<div class="col-xs-12">
				<div class="alert alert-info margin-base-vertical">
					<s:text name="note.noContentSet" />&#32;
					<wpsf:submit action="searchContents" value="%{getText('label.choose')}" cssClass="btn btn-info" />
				</div>
			</div>
		</s:else>

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