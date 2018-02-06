<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<s:text name="title.configPage" />
	</span>
</h1>

<div id="main" role="main">

    <div class="grid-container"></div>

    <div class="widget-list"></div>

<s:set var="breadcrumbs_pivotPageCode" value="currentPage.code" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
	<s:param name="selectedNode" value="currentPage.code"></s:param>
</s:action>

<s:if test="currentPage.draftWidgets.length > 0">

	<ol start="0" class="list-group">

	<s:set var="showletTypeApiMappingsVar" value="showletTypeApiMappings" />
	<s:iterator var="showlet" value="currentPage.draftWidgets" status="rowstatus">
		<s:set var="showletType" value="#showlet.getType()" ></s:set>
		<s:set var="metadata" value="currentPage.metadata" ></s:set>

		<li class="list-group-item padding-base-top padding-base-bottom" id="widget<s:property value="#rowstatus.count" />">
			<h2 class="h5 list-group-item-heading">
				<s:if test="#metadata.getModel().getMainFrame() == #rowstatus.index">
					<a href="
					<s:url action="editFrame" namespace="/do/Page">
						<s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
						<s:param name="frame"><s:property value="#rowstatus.index"/></s:param>
					</s:url>"
						 class="text-success"
						 title="<s:text name="name.mainFrame" />">
						<span class="icon fa fa-cogs fa-fw"></span>&#32;
				</s:if>
				<s:else>
					<a href="
					<s:url action="editFrame" namespace="/do/Page">
						<s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
						<s:param name="frame"><s:property value="#rowstatus.index"/></s:param>
					</s:url>">
						<span class="icon fa fa-cog fa-fw"></span>&#32;
				</s:else>
						<s:property value="#metadata.getModel().getFrames()[#rowstatus.index]"/>
					</a>
			</h2>
			<s:if test="null != #showletType.code">
				<div class="list-group-item-text padding-base-top row">
					<div class="col-sm-6">
						<s:property value="%{getTitle(#showletType.getCode(), #showletType.getTitles())}" />
					</div>
					<div class="col-sm-6">
						<div class="btn-toolbar pull-right">
							<div class="btn-group btn-group-xs">
								<a href="<s:url namespace="/do/Portal/WidgetType" action="viewWidgetUtilizers">
										<s:param name="widgetTypeCode" value="{#showletType.getCode()}" />
									 </s:url>"
									 title="<s:text name="title.widgetManagement.howmanypages.goToSee" />: <s:property value="%{getTitle(#showletType.getCode(), #showletType.getTitles())}" />"
									 class="btn btn-default">
									<span class="icon fa fa-info"></span>
								</a>
								<wp:ifauthorized permission="superuser">
									<s:if test="!#showletType.isLogic() && null != #showletType.typeParameters && #showletType.typeParameters.size() > 0">
										<a href="
											<s:url namespace="/do/Portal/WidgetType" action="copy">
												<s:param name="pageCode" value="currentPage.code" />
												<s:param name="framePos" value="#rowstatus.index" />
											</s:url>"
											 title="<s:text name="label.userWidget.new.from" />: <s:property value="%{getTitle(#showletType.getCode(), #showletType.getTitles())}" />"
											 class="btn btn-default">
											<span class="icon fa fa-puzzle-piece"></span>
										</a>
									</s:if>
								</wp:ifauthorized>
								<s:if test="null != #showletType.code">
									<wp:ifauthorized permission="superuser">
										<s:if test="!#showletType.isLogic()">
											<s:set var="relatedApiMethodVar" value="#showletTypeApiMappingsVar[#showletType.code]" />
										</s:if>
										<s:else>
											<s:set var="relatedApiMethodVar" value="#showletTypeApiMappingsVar[#showletType.parentType.code]" />
										</s:else>
										<s:if test="null != #relatedApiMethodVar">
											<a href="
												<s:url action="copyFromWidget" namespace="/do/Api/Service">
													<s:param name="pageCode" value="currentPage.code" />
													<s:param name="framePos" value="#rowstatus.index" />
													<s:param name="resourceName" value="#relatedApiMethodVar.resourceName" />
													<s:param name="namespace" value="#relatedApiMethodVar.namespace" />
												</s:url>"
												 title="<s:text name="note.api.apiMethodList.createServiceFromMethod" />: <s:property value="#relatedApiMethodVar.methodName" />"
												 class="btn btn-default">
												<span class="icon fa fa-code-fork"></span>
											</a>
										</s:if>
										<s:set var="relatedApiMethodVar" />
									</wp:ifauthorized>
								</s:if>
							</div>
							<s:if test="null != #showletType.code">
								<div class="btn-group btn-group-xs">
									<%-- move up --%>
									<a class="btn btn-default btn-primary <s:property value="#rowstatus.first ? ' disabled ' : ''" />"
										href="<s:url action="moveWidgetUp" namespace="/do/Page">
											<s:param name="selectedNode" value="currentPage.code" />
											<s:param name="frame" value="#rowstatus.index" />
											<s:param name="widgetTypeCode" value="#showletType.code" />
										</s:url>#widget<s:property value="#rowstatus.count-1" />"
										 title="<s:text name="label.moveUp" />: <s:property value="%{getTitle(#showletType.code, #showletType.titles)}" />">
											<span class="sr-only"><s:text name="label.moveUp" /></span>
											<span class="icon fa fa-arrow-up"></span>
									</a>
									<%-- move down --%>
									<a class="btn btn-default btn-primary <s:property value="#rowstatus.last  ? ' disabled ' : ''" />"
										href="<s:url action="moveWidgetDown" namespace="/do/Page">
											<s:param name="selectedNode" value="currentPage.code" />
											<s:param name="frame" value="#rowstatus.index" />
											<s:param name="widgetTypeCode" value="#showletType.code" />
										</s:url>#widget<s:property value="#rowstatus.count+1" />"
										 title="<s:text name="label.moveDown" />: <s:property value="%{getTitle(#showletType.code, #showletType.titles)}" />">
											<span class="sr-only"><s:text name="label.moveDown" /></span>
											<span class="icon fa fa-arrow-down"></span>
									</a>
								</div>
								<%-- clear position --%>
								<div class="btn-group btn-group-xs">
									<a href="
										<s:url action="trashWidgetFromPage" namespace="/do/Page">
											<s:param name="pageCode" value="currentPage.code" />
											<s:param name="frame" value="#rowstatus.index" />
											<s:param name="widgetTypeCode" value="#showletType.code" />
										</s:url>"
										 title="<s:text name="label.clear" />: <s:property value="%{getTitle(#showletType.code, #showletType.titles)}" />"
										 class="btn btn-warning">
											<span class="icon fa fa-eraser"></span>
									</a>
								</div>
							</s:if>
						</div><!-- // toolbar -->
					</div>
				</div><!-- // list-group-item-text -->
			</s:if>
		</li>
		<s:set var="showletType" />
	</s:iterator>

	</ol>

</s:if>
<s:else>

	<div class="alert alert-info">
		<p>
			<strong><s:text name="label.whoops" /></strong>&#32;<s:text name="note.page.pagemodelWithoutPositions" />
		</p>
		<p class="margin-base-vertical">
			<a href="
				<s:url action="edit" anchor="pagemodel">
					<s:param name="selectedNode" value="currentPage.code" />
				</s:url>" class="btn btn-lg btn-info">
				<span class="icon fa fa-pencil-square-o"></span>&#32;
				<s:text name="page.options.pagemodel" />
			</a>
		</p>
	</div>

</s:else>

</div>