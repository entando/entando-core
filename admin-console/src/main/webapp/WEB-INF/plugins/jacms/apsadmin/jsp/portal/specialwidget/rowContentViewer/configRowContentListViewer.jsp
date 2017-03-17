<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<a href="<s:url action="configure" namespace="/do/Page">
			   <s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
		   </s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text name="title.configPage" /></a>&#32;/&#32;
		<s:text name="name.widget" />
	</span>
</h1>

<div id="main" role="main">

	<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
	<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

	<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

	<s:form action="saveRowListViewerConfig" namespace="/do/jacms/Page/SpecialWidget/RowListViewer" cssClass="form-horizontal">

		<div class="panel panel-default">
			<div class="panel-heading">
				<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
			</div>

			<div class="panel-body">

				<%-- title of the current widget --%>
					<h2 class="h5 margin-small-vertical">
						<label class="sr-only"><s:text name="name.widget" /></label>
						<span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
						<s:property value="%{getTitle(widget.type.code, widget.type.titles)}" />
					</h2>

				<%-- hidden --%>
					<p class="sr-only">
						<wpsf:hidden name="pageCode" />
						<wpsf:hidden name="frame" />
						<wpsf:hidden name="widgetTypeCode" value="%{widget.type.code}" />
						<wpsf:hidden name="contents" value="%{widget.config.get('contents')}" />
					</p>

				<%-- errors --%>
					<s:if test="hasFieldErrors()">
						<div class="alert alert-danger alert-dismissable margin-large-top">
							<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
							<h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
							<ul>
								<s:iterator value="fieldErrors">
									<s:iterator value="value">
										<li><s:property escapeHtml="false" /></li>
									</s:iterator>
								</s:iterator>
							</ul>
						</div>
					</s:if>

				<%-- add content --%>
					<fieldset class="margin-large-top">
						<legend><s:text name="title.contentsPublished" /></legend>
						<s:set var="contentsPropertiesVar" value="contentsProperties" />
						<s:if test="%{#contentsPropertiesVar.size()>0}">
							<div class="table-responsive">
								<table class="table table-striped table-hover table-condensed">
									<tr>
										<th class="text-center text-nowrap"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
										<th><s:text name="label.content" /></th>
										<th><s:text name="label.contentModel" /></th>
									</tr>

									<s:iterator value="#contentsPropertiesVar" var="contentPropertiesVar" status="elementStatus">
										<tr>
											<td class="text-center text-nowrap">

												<s:set var="elementIndex" value="#elementStatus.index" />

												<div class="btn-group btn-group-xs">
													<wpsa:actionParam action="moveContent" var="actionName" >
														<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
														<wpsa:actionSubParam name="movement" value="UP" />
													</wpsa:actionParam>
													<wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.moveUp')}" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex}" cssClass="btn btn-default">
														<span class="icon fa fa-sort-desc"></span>
													</wpsf:submit>
													<wpsa:actionParam action="moveContent" var="actionName" >
														<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
														<wpsa:actionSubParam name="movement" value="DOWN" />
													</wpsa:actionParam>
													<wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.moveDown')}" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex+2}" cssClass="btn btn-default">
														<span class="icon fa fa-sort-asc"></span>
													</wpsf:submit>
												</div>

												<div class="btn-group btn-group-xs">
													<wpsa:actionParam action="removeContent" var="actionName" >
														<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
													</wpsa:actionParam>
													<wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
														<span class="icon fa fa-times-circle"></span>
													</wpsf:submit>
												</div>

											</td>
											<td>
												<s:set var="contentIdVar" value="#contentPropertiesVar['contentId']" />
												<s:set var="contentVar" value="%{getContentVo(#contentIdVar)}" />
												<s:property value="#contentVar.descr" />&#32;<code><s:property value="#contentVar.id" /></code>
											</td>
											<td>
												<s:set var="modelIdVar" value="#contentPropertiesVar['modelId']" />
												<s:if test="null != #modelIdVar">
													<s:set var="contentModelVar" value="%{getContentModel(#modelIdVar)}" />
													<s:if test="null != #contentModelVar">
														<s:property value="#contentModelVar.description" />&#32;<code><s:property value="#contentModelVar.id" /></code>
													</s:if>
													<s:else>
														<s:property value="#modelIdVar" />
													</s:else>
												</s:if>
												<s:else><code><s:text name="label.model.default" /></code></s:else>
											</td>
										</tr>
									</s:iterator>
								</table>
							</div>
						</s:if>
						<s:else>
							<p><s:text name="note.contents.not.set" /></p>
						</s:else>

						<div class="form-group">
							<div class="col-xs-12">
									<wpsf:submit type="button" action="addContent" cssClass="btn btn-info">
										<span class="icon fa fa-plus-square"></span>&#32;
										<s:text name="label.addContent" />
									</wpsf:submit>
							</div>
						</div>
					</fieldset>


				<%-- Options --%>
					<fieldset class="margin-large-top">
						<legend data-toggle="collapse" data-target="#options-publishing"><s:text name="title.publishingOptions" />&#32;<span class="icon fa fa-chevron-down"></span></legend>
						<div class="collapse" id="options-publishing">
							<div class="form-group">
								<div class="col-xs-12">
									<label for="maxElemForItem"><s:text name="label.maxElementsForItem" /></label>
									<wpsf:select name="maxElemForItem" id="maxElemForItem" value="%{getShowlet().getConfig().get('maxElemForItem')}"
												 headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20,50:50,100:100,500:500}" cssClass="form-control" />
								</div>
							</div>
						</div>
					</fieldset>

				<%-- TITLES --%>
					<fieldset class="margin-base-top">
						<legend data-toggle="collapse" data-target="#options-extra"><s:text name="title.extraOption" />&#32;<span class="icon fa fa-chevron-down"></span></legend>
						<div class="collapse" id="options-extra">
							<p><s:text name="note.extraOption.intro" /></p>
							<s:iterator var="lang" value="langs">
								<div class="form-group">
									<div class="col-xs-12">
										<label for="title_<s:property value="#lang.code" />">
											<code class="label label-info"><s:property value="#lang.code" /></code>&#32;
											<s:text name="label.title" />
										</label>
										<wpsf:textfield name="title_%{#lang.code}" id="title_%{#lang.code}" value="%{widget.config.get('title_' + #lang.code)}" cssClass="form-control" />
									</div>
								</div>
							</s:iterator>
							<div class="form-group">
								<div class="col-xs-12">
									<label for="pageLink"><s:text name="label.page" /></label>
									<wpsf:select list="pages" name="pageLink" id="pageLink" listKey="code" listValue="getShortFullTitle(currentLang.code)"
												 value="%{widget.config.get('pageLink')}" headerKey="" headerValue="%{getText('label.none')}" cssClass="form-control" />
								</div>
							</div>
							<s:iterator var="lang" value="langs">
								<div class="form-group">
									<div class="col-xs-12">
										<label for="linkDescr_<s:property value="#lang.code" />">
											<code class="label label-info"><s:property value="#lang.code" /></code>&#32;
											<s:text name="label.link.descr"/>
										</label>
										<wpsf:textfield name="linkDescr_%{#lang.code}" id="linkDescr_%{#lang.code}" value="%{widget.config.get('linkDescr_' + #lang.code)}" cssClass="form-control" />
									</div>
								</div>
							</s:iterator>
						</div>
					</fieldset>

			</div><%-- body --%>
		</div><%-- panel --%>
		<%--
		<fieldset class="margin-base-top"><legend><s:text name="title.contentInfo" /></legend>
			<div class="form-group">
		--%>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit action="saveRowListViewerConfig" type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-floppy-o"></span>&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>
