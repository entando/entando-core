<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="showletTypeVar" value="getShowletType(widgetTypeCode)" />
<s:set var="showletTypeApiMappingsVar" value="showletTypeApiMappings" />
<s:set var="showletUtilizers" value="showletUtilizers" />

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" />">
		<s:text name="title.widgetManagement" /></a>&#32;/&#32;
		<s:text name="title.widgetManagement.pages" />: <s:property value="getTitle(#showletTypeVar.code, #showletTypeVar.titles)" />
	</span>
</h1>

<div id="main" role="main">

<s:if test="!#showletTypeVar.isLogic()">
	<s:set var="relatedApiMethodVar" value="#showletTypeApiMappingsVar[#showletTypeVar.code]" />
</s:if>
<s:else>
	<s:set var="relatedApiMethodVar" value="#showletTypeApiMappingsVar[#showletTypeVar.parentType.code]" />
</s:else>
<s:if test="#showletUtilizers != null && #showletUtilizers.size() > 0">

<s:form action="viewWidgetUtilizers" >

<p class="sr-only">
	<wpsf:hidden name="widgetTypeCode" />
</p>

<wpsa:subset source="#showletUtilizers" count="10" objectName="groupShowletUtilizers" advanced="true" offset="5">
<s:set name="group" value="#groupShowletUtilizers" />

<div class="text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<s:iterator var="singlePage">

<div class="panel panel-default">
	<div class="panel-heading">
		<h2 class="panel-title row" title="<s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />">
		<s:if test="%{isUserAllowed(#singlePage)}">
			<div class="col-sm-3 col-lg-3">
				<div class="btn-group pull-right">
					<a href="
							<s:url namespace="/do/Page" action="viewTree">
								<s:param name="selectedNode" value="#singlePage.code" />
							</s:url>" class="btn btn-default" title="<s:text name="note.goToSomewhere" />: <s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />">
						<span class="icon fa fa-sitemap"></span>
					</a>
					<a href="
							<s:url namespace="/do/Page" action="configure">
								<s:param name="pageCode" value="#singlePage.code" />
							</s:url>" class="btn btn-default" title="<s:text name="title.configPage" />: <s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />" />
						<span class="icon fa fa-cog"></span>
					</a>
				</div>
			</div>
			<div class="col-sm-9 col-lg-9 padding-small-top padding-small-bottom">
		</s:if>
		<s:else>
			<div class="col-sm-9 col-lg-9 col-sm-offset-3 col-lg-offset-3 padding-small-top padding-small-bottom">
		</s:else>
				<s:property value="%{#singlePage.getShortFullTitle(currentLang.code)}" />
			</div>
		</h2>
	</div>
	<ul class="list-group">
	<s:iterator value="#singlePage.widgets" var="showlet" status="rowstatus">
		<s:if test="#showlet != null && #showlet.type != null && #showlet.type.code.equals(widgetTypeCode)">
		<li class="list-group-item">
			<div class="row">
				<div class="col-sm-3 col-lg-3">
					<div class="btn-group pull-right">
						<div class="btn-group">
							<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" title="<s:text name="label.new" />...">
								<span class="icon fa fa-plus-circle"></span>
								<span class="caret"></span>
							</button>
							<ul class="dropdown-menu">
								<s:if test="null != #showlet.type.typeParameters && #showlet.type.typeParameters.size() > 0">
									<li>
										<a href="<s:url namespace="/do/Portal/WidgetType" action="copy"><s:param name="pageCode" value="#singlePage.code" /><s:param name="framePos" value="#rowstatus.index" /></s:url>">
											<s:text name="label.userWidget.new" />
										</a>
									</li>
								</s:if>
							<s:if test="%{isUserAllowed(#singlePage)}">
								<s:if test="null != #relatedApiMethodVar && !#showletTypeVar.isLogic()">
									<wp:ifauthorized permission="superuser">
										<li>
											<a href="<s:url action="copyFromWidget" namespace="/do/Api/Service">
														<s:param name="pageCode" value="#singlePage.code" />
														<s:param name="framePos" value="#rowstatus.index" />
														<s:param name="resourceName" value="#relatedApiMethodVar.resourceName" />
														<s:param name="namespace" value="#relatedApiMethodVar.namespace" />
													</s:url>">
												<s:text name="note.api.apiMethodList.createServiceFromMethod" />&#32;
												<code><s:property value="#relatedApiMethodVar.methodName" /></code>
											</a>
										</li>
									</wp:ifauthorized>
								</s:if>
								<s:elseif test="null != #relatedApiMethodVar && #showletTypeVar.isLogic()">
									<wp:ifauthorized permission="superuser">
										<li>
											<a href="<s:url action="newService" namespace="/do/Api/Service">
														<s:param name="widgetTypeCode" value="widgetTypeCode" />
														<s:param name="resourceName" value="#relatedApiMethodVar.resourceName" />
														<s:param name="namespace" value="#relatedApiMethodVar.namespace" />
													</s:url>">
												<s:text name="note.api.apiMethodList.createServiceFromMethod" />&#32;<code><s:property value="#relatedApiMethodVar.methodName" /></code>
											</a>
										</li>
									</wp:ifauthorized>
								</s:elseif>
							</s:if>
							</ul>
						</div>
						<a href="<s:url action="editFrame" namespace="/do/Page">
									<s:param name="pageCode"><s:property value="#singlePage.code"/></s:param>
									<s:param name="frame"><s:property value="#rowstatus.index"/></s:param>
								</s:url>" title="<s:text name="title.editFrame" />:&#32;<s:property value="#rowstatus.index"/> &ndash;&#32;<s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/>" class="btn btn-default">
							<span class="icon fa fa-cog"></span>
						</a>
					</div>
				</div>

				<div class="col-sm-8 col-lg-8 padding-small-top padding-small-bottom">
					<span class="label label-default"><s:property value="#rowstatus.index"/></span>&#32;
					<s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/>
				</div>

				<div class="col-sm-1">
					<a href="<s:url action="trashWidgetFromPage" namespace="/do/Portal/WidgetType">
								<s:param name="pageCode"><s:property value="#singlePage.code"/></s:param>
								<s:param name="frame"><s:property value="#rowstatus.index"/></s:param>
								<s:param name="widgetTypeCode"><s:property value="#showletTypeVar.code"/></s:param>
							</s:url>" title="<s:text name="label.clear" />:&#32;<s:property value="#rowstatus.index"/>&#32;&middot;&#32;<s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/>" class="btn btn-warning pull-right">
						<span class="icon fa fa-eraser"></span>
					</a>
				</div>

			</div>
		</li>
		</s:if>
	</s:iterator>
	</ul>
</div>


</s:iterator>

<%--

<table class="table table-bordered" summary="<s:text name="note.widgetManagement.pages.summary" />">
<caption><span><s:text name="name.pages" /></span></caption>
<tr>
	<th><s:text name="title.pageActions" /></th>
	<th><s:text name="label.page" /></th>
	<th><s:text name="name.positions" /></th>
</tr>
<s:iterator var="singlePage">
<tr>
	<td class="icon_double">
	<s:if test="%{isUserAllowed(#singlePage)}">
		<a href="<s:url namespace="/do/Page" action="viewTree"><s:param name="selectedNode" value="#singlePage.code" /></s:url>"><img src="<wp:resourceURL />administration/common/img/icons/node-leaf.png" alt="<s:text name="note.goToSomewhere" />: <s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />" title="<s:text name="note.goToSomewhere" />: <s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />" /></a>
		<a href="<s:url namespace="/do/Page" action="configure"><s:param name="pageCode" value="#singlePage.code" /></s:url>"><img src="<wp:resourceURL />administration/common/img/icons/page-configure.png" alt="<s:text name="title.configPage" />: <s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />" title="<s:text name="title.configPage" />: <s:property value="%{#singlePage.getFullTitle(currentLang.code)}" />" /></a>
	</s:if>
	<s:else><abbr title="<s:text name="label.none" />">&ndash;</abbr></s:else>
	</td>
	<td><s:property value="%{#singlePage.getFullTitle(currentLang.code)}" /></td>
	<td>
		<ul class="noBullet">
	<s:iterator value="#singlePage.showlets" id="showlet" status="rowstatus">
		<s:if test="#showlet != null && #showlet.type != null && #showlet.type.code.equals(widgetTypeCode)">
			<li>
			<s:if test="null != #showlet.type.typeParameters && #showlet.type.typeParameters.size() > 0">
				<a href="<s:url namespace="/do/Portal/WidgetType" action="copy" ><s:param name="pageCode" value="#singlePage.code" /><s:param name="framePos" value="#rowstatus.index" /></s:url>" title="<s:text name="label.userWidget.new" />" class="noborder"><img src="<wp:resourceURL/>administration/common/img/icons/16x16/showlet-user-new.png" alt="<s:text name="label.userWidget.new" />" /></a>
			</s:if>
			<s:if test="%{isUserAllowed(#singlePage)}">
				<s:if test="null != #relatedApiMethodVar && !#showletTypeVar.isLogic()">
				<wp:ifauthorized permission="superuser">
				<a class="noborder" href="<s:url action="copyFromWidget" namespace="/do/Api/Service">
						<s:param name="pageCode" value="#singlePage.code" />
						<s:param name="framePos" value="#rowstatus.index" />
						<s:param name="resourceName" value="#relatedApiMethodVar.resourceName" />
						<s:param name="namespace" value="#relatedApiMethodVar.namespace" />
					</s:url>" title="<s:text name="note.api.apiMethodList.createServiceFromMethod" />: <s:property value="#relatedApiMethodVar.methodName" />"><img src="<wp:resourceURL />administration/common/img/icons/16x16/api-service-new.png" alt="<s:text name="label.new" />" /></a>
				</wp:ifauthorized>
				</s:if>
				<s:if test="null != #relatedApiMethodVar && #showletTypeVar.isLogic()">
				<wp:ifauthorized permission="superuser">
				<a class="noborder" href="<s:url action="newService" namespace="/do/Api/Service">
						<s:param name="widgetTypeCode" value="widgetTypeCode" />
						<s:param name="resourceName" value="#relatedApiMethodVar.resourceName" />
						<s:param name="namespace" value="#relatedApiMethodVar.namespace" />
				</s:url>" title="<s:text name="note.api.apiMethodList.createServiceFromMethod" />: <s:property value="#relatedApiMethodVar.methodName" />"><img src="<wp:resourceURL />administration/common/img/icons/16x16/api-service-new.png" alt="<s:text name="label.new" />" /></a>
				</wp:ifauthorized>
				</s:if>
				<a class="noborder" href="<s:url action="trashWidgetFromPage" namespace="/do/Portal/WidgetType">
					<s:param name="pageCode"><s:property value="#singlePage.code"/></s:param>
					<s:param name="frame"><s:property value="#rowstatus.index"/></s:param>
					<s:param name="widgetTypeCode"><s:property value="#showletTypeVar.code"/></s:param>
				</s:url>
				" title="<s:text name="label.clear" />: <s:property value="#rowstatus.index"/> &ndash; <s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/>"><img src="<wp:resourceURL/>administration/common/img/icons/16x16/clear.png" alt="<s:text name="label.alt.clear" />" /></a>
				&#32;<a href="<s:url action="editFrame" namespace="/do/Page"><s:param name="pageCode"><s:property value="#singlePage.code"/></s:param><s:param name="frame"><s:property value="#rowstatus.index"/></s:param></s:url>" title="<s:text name="title.editFrame" />: <s:property value="#rowstatus.index"/> &ndash; <s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/>"><s:property value="#rowstatus.index"/> &ndash; <s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/></a>
			</s:if>
			<s:else><s:property value="#rowstatus.index"/> &ndash; <s:property value="#singlePage.model.getFrames()[#rowstatus.index]"/></s:else>
			</li>
		</s:if>
	</s:iterator>
		</ul>
	</td>
</tr>
</s:iterator>
</table>
--%>

<div class="text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>
</wpsa:subset>

</s:form>

</s:if>
<s:else>
<p><s:text name="note.widget.noUse" /></p>
</s:else>

</div>
