<%@ taglib prefix="s" uri="/struts-tags" %>
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

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
<s:form namespace="/do/jacms/Page/SpecialWidget/ListViewer" cssClass="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-heading">
			<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
		</div>

		<div class="panel-body">

			<s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
			<h2 class="h5 margin-small-vertical">
				<label class="sr-only"><s:text name="name.widget" /></label>
				<span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
				<s:property value="%{getTitle(#showletType.code, #showletType.titles)}" />
			</h2>

			<p class="sr-only">
				<wpsf:hidden name="pageCode" />
				<wpsf:hidden name="frame" />
				<wpsf:hidden name="widgetTypeCode" />
			</p>

			<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
				<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
					<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
				</ul>
			</div>
			</s:if>

			<p class="sr-only">
				<wpsf:hidden name="contentType" />
				<wpsf:hidden name="categories" value="%{#parameters['categories']}" />
				<wpsf:hidden name="orClauseCategoryFilter" value="%{#parameters['orClauseCategoryFilter']}" />
				<wpsf:hidden name="userFilters" value="%{#parameters['userFilters']}" />
				<wpsf:hidden name="filters" />
				<wpsf:hidden name="modelId" />
				<wpsf:hidden name="maxElemForItem" />
				<wpsf:hidden name="pageLink" value="%{#parameters['pageLink']}" />
				<s:iterator id="lang" value="langs">
					<wpsf:hidden name="%{'linkDescr_' + #lang.code}" value="%{#parameters['linkDescr_' + #lang.code]}" />
					<wpsf:hidden name="%{'title_' + #lang.code}" value="%{#parameters['title_' + #lang.code]}" />
				</s:iterator>
				<wpsf:hidden name="userFilterKey" value="category" />
			</p>

			<div class="form-group">
				<div class="col-xs-12">
					<label for="userFilterCategoryCode"><s:text name="label.userFilterCategory" /></label>
					<wpsf:select name="userFilterCategoryCode" id="userFilterCategoryCode" list="categories" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" />
				</div>
			</div>

		</div>
	</div>

	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit action="addUserFilter" type="button" cssClass="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.save" />
			</wpsf:submit>
		</div>
	</div>

</s:form>

</div>