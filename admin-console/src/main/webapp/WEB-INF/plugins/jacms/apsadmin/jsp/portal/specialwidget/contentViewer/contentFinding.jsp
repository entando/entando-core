<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
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
	</span>
</h1>

<div id="main" role="main"><%-- main --%>
	<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
	<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />
	<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

	<s:form action="searchContents" cssClass="form-horizontal"><%-- form searchContents --%>
		<div class="panel panel-default"><%-- panel --%>
			<div class="panel-heading">
				<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
			</div>
			<div class="panel-body"><%-- panel-body --%>
				<s:set var="widgetType" value="%{getWidgetType(widgetTypeCode)}"></s:set>
				<h3 class="h5 margin-small-top margin-large-bottom">
					<label class="sr-only"><s:text name="name.widget" /></label>
					<span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
					<s:property value="%{getTitle(#widgetType.code, #widgetType.titles)}" />
				</h3>
				<p class="sr-only">
					<wpsf:hidden name="pageCode" />
					<wpsf:hidden name="frame" />
					<wpsf:hidden name="widgetTypeCode" />
					<wpsf:hidden name="modelId" />
				</p>
				<s:if test="hasFieldErrors()">
					<div class="alert alert-danger alert-dismissable">
						<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
						<h4 class="margin-none"><s:text name="message.title.FieldErrors" /></h4>
						<ul class="margin-base-vertical">
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
							<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
						</ul>
					</div>
				</s:if>
				<div class="form-group" role="search"><%-- form-group search --%>
					<div class="input-group col-sm-12">
						<span class="input-group-addon">
							<span class="icon fa fa-file-text-o fa-lg"></span>
						</span>
						<label for="text" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.description"/></label>
						<wpsf:textfield name="text" id="text" cssClass="form-control input-lg" placeholder="%{getText('label.search.topic')}" title="%{getText('label.search.by')} %{getText('label.description')}" />
						<div class="input-group-btn">
							<wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
								<span class="icon fa fa-search"></span>
								<span class="sr-only"><s:text name="label.search" /></span>
							</wpsf:submit>
							<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
								<span class="sr-only"><s:text name="title.searchFilters" /></span>
								<span class="caret"></span>
							</button>
						</div>
					</div>
					<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<div id="search-advanced" class="collapse well collapse-input-group">
						<%-- type --%>
							<div class="form-group">
								<label for="contentType" class="control-label col-sm-2 text-right">
									<s:text name="label.type" />
								</label>
								<div class="col-sm-5">
									<wpsf:select name="contentType" id="contentType"	list="contentTypes" listKey="code" listValue="descr" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" />
								</div>
							</div>
						<%-- code --%>
							<div class="form-group">
								<label for="contentIdToken" class="control-label col-sm-2 text-right">
									<s:text name="label.code" />
								</label>
								<div class="col-sm-5">
									<wpsf:textfield name="contentIdToken" id="contentIdToken" cssClass="form-control" />
								</div>
							</div>
						<%-- status --%>
							<div class="form-group">
								<label for="state" class="control-label col-sm-2 text-right"><s:text name="label.state" /></label>
								<div class="col-sm-5">
									<wpsf:select name="state" id="state" list="avalaibleStatus" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" listKey="key" listValue="%{getText(value)}" />
								</div>
							</div>
						<%-- search --%>
							<div class="form-group">
								<div class="col-sm-5 col-sm-offset-2">
									<wpsf:submit type="button" cssClass="btn btn-primary">
										<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
									</wpsf:submit>
								</div>
							</div>
						</div><!--// search-advanced -->
					</div>
				</div><%-- form-group search --%>
				<hr />
				<wpsa:subset source="contents" count="10" objectName="groupContent" advanced="true" offset="5">
					<s:set var="group" value="#groupContent" />
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					<p class="sr-only">
						<wpsf:hidden name="lastGroupBy" />
						<wpsf:hidden name="lastOrder" />
					</p>
					<div class="table-responsive2"><%-- table-responsive --%>
						<table class="table table-bordered">
						<tr>
							<th><a href="
							<s:url action="changeContentListOrder">
								<s:param name="text">
									<s:property value="#request.text"/>
								</s:param>
								<s:param name="contentIdToken">
									<s:property value="#request.contentIdToken"/>
								</s:param>
								<s:param name="contentType">
									<s:property value="#request.contentType"/>
								</s:param>
								<s:param name="state">
									<s:property value="#request.state"/>
								</s:param>
								<s:param name="pagerItem">
									<s:property value="#groupContent.currItem"/>
								</s:param>
								<s:param name="pageCode">
									<s:property value="#request.pageCode"/>
								</s:param>
								<s:param name="frame">
									<s:property value="#request.frame"/>
								</s:param>
								<s:param name="modelId">
									<s:property value="#request.modelId"/>
								</s:param>
								<s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
								<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
								<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
								<s:param name="groupBy">descr</s:param>
							</s:url>
						"><s:text name="label.description" /></a></th>
							<th><a href="
							<s:url action="changeContentListOrder">
								<s:param name="text">
									<s:property value="#request.text"/>
								</s:param>
								<s:param name="contentIdToken">
									<s:property value="#request.contentIdToken"/>
								</s:param>
								<s:param name="contentType">
									<s:property value="#request.contentType"/>
								</s:param>
								<s:param name="state">
									<s:property value="#request.state"/>
								</s:param>
								<s:param name="pagerItem">
									<s:property value="#groupContent.currItem"/>
								</s:param>
								<s:param name="pageCode">
									<s:property value="#request.pageCode"/>
								</s:param>
								<s:param name="frame">
									<s:property value="#request.frame"/>
								</s:param>
								<s:param name="modelId">
									<s:property value="#request.modelId"/>
								</s:param>
								<s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
								<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
								<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
								<s:param name="groupBy">code</s:param>
							</s:url>
						"><s:text name="label.code" /></a></th>
							<th><s:text name="label.group" /></th>
							<th class="text-center"><a href="
							<s:url action="changeContentListOrder">
								<s:param name="text">
									<s:property value="#request.text"/>
								</s:param>
								<s:param name="contentIdToken">
									<s:property value="#request.contentIdToken"/>
								</s:param>
								<s:param name="contentType">
									<s:property value="#request.contentType"/>
								</s:param>
								<s:param name="state">
									<s:property value="#request.state"/>
								</s:param>
								<s:param name="pagerItem">
									<s:property value="#groupContent.currItem"/>
								</s:param>
								<s:param name="pageCode">
									<s:property value="#request.pageCode"/>
								</s:param>
								<s:param name="frame">
									<s:property value="#request.frame"/>
								</s:param>
								<s:param name="modelId">
									<s:property value="#request.modelId"/>
								</s:param>
								<s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
								<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
								<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
								<s:param name="groupBy">created</s:param>
							</s:url>
						"><s:text name="label.creationDate" /></a></th>
							<th class="text-center"><a href="
							<s:url action="changeContentListOrder">
								<s:param name="text">
									<s:property value="#request.text"/>
								</s:param>
								<s:param name="contentIdToken">
									<s:property value="#request.contentIdToken"/>
								</s:param>
								<s:param name="contentType">
									<s:property value="#request.contentType"/>
								</s:param>
								<s:param name="state">
									<s:property value="#request.state"/>
								</s:param>
								<s:param name="pagerItem">
									<s:property value="#groupContent.currItem"/>
								</s:param>
								<s:param name="pageCode">
									<s:property value="#request.pageCode"/>
								</s:param>
								<s:param name="frame">
									<s:property value="#request.frame"/>
								</s:param>
								<s:param name="modelId">
									<s:property value="#request.modelId"/>
								</s:param>
								<s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
								<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
								<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
								<s:param name="groupBy">lastModified</s:param>
							</s:url>
						"><s:text name="label.lastEdit" /></a></th>
						</tr>
						<s:iterator id="contentId">
								<s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
						<tr>
						<td><input type="radio" name="contentId" id="contentId_<s:property value="#content.id"/>" value="<s:property value="#content.id"/>" />
						<label for="contentId_<s:property value="#content.id"/>"><s:property value="#content.descr" /></label></td>
						<td><code><s:property value="#content.id" /></code></td>
						<td><s:property value="%{getGroup(#content.mainGroupCode).descr}" /></td>
						<td class="text-center text-nowrap">
							<code><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></code>
						</td>
						<td class="text-center text-nowrap">
							<code><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></code>
						</td>
						</tr>
						</s:iterator>
						</table>
					</div><%-- table-responsive --%>
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
				</wpsa:subset>
				<%-- save --%>

				<div class="form-group">
					<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
						<wpsf:submit type="button" action="joinContent" cssClass="btn btn-primary btn-block">
							<s:text name="label.confirm" />
						</wpsf:submit>
					</div>
				</div>

			</div><%-- panel-body --%>
		</div><%-- panel --%>
	</s:form>
</div><%-- main --%>