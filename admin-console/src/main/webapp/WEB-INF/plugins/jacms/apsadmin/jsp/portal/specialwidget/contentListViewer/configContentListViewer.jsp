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

<s:form action="saveListViewerConfig" namespace="/do/jacms/Page/SpecialWidget/ListViewer" cssClass="form-horizontal">

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

		<p class="sr-only">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" value="%{widget.type.code}" />
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

		<s:if test="widget.config['contentType'] == null">
		<%-- CONTENT TYPE --%>
		<div class="form-group margin-base-top">
			<div class="col-xs-12">
				<label for="contentType"><s:text name="label.type"/></label>
				<div class="input-group">
					<wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="form-control" />
					<span class="input-group-btn">
						<wpsf:submit type="button" action="configListViewer" cssClass="btn btn-success">
							<span class="icon fa fa-check"></span>&#32;
							<s:text name="label.confirm" />
						</wpsf:submit>
					</span>
				</div>
			</div>
		</div>
	</div>
</div>
		</s:if>
		<s:else>

		<fieldset class="margin-base-top"><legend><s:text name="title.contentInfo" /></legend>
			<div class="form-group">
				<div class="col-xs-12">
					<label for="contentType"><s:text name="label.type"/></label>
					<div class="input-group">
						<wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" value="%{getShowlet().getConfig().get('contentType')}" cssClass="form-control" />
						<span class="input-group-btn">
							<wpsf:submit action="changeContentType" value="%{getText('label.change')}" cssClass="btn btn-info" />
						</span>
					</div>
				</div>
			</div>
			<p class="sr-only">
				<wpsf:hidden name="contentType" value="%{getShowlet().getConfig().get('contentType')}" />
				<wpsf:hidden name="categories" value="%{getShowlet().getConfig().get('categories')}" />
				<s:iterator value="categoryCodes" var="categoryCodeVar" status="rowstatus">
				<input type="hidden" name="categoryCodes" value="<s:property value="#categoryCodeVar" />" id="categoryCodes-<s:property value="#rowstatus.index" />"/>
				</s:iterator>
			</p>

		</fieldset>

		<fieldset class="margin-large-top">
			<legend data-toggle="collapse" data-target="#options-publishing"><s:text name="title.publishingOptions" />&#32;<span class="icon fa fa-chevron-down"></span></legend>

			<div class="collapse" id="options-publishing">
				<div class="form-group">
					<div class="col-xs-12">
						<label for="modelId"><s:text name="label.contentModel" /></label>
						<wpsf:select name="modelId" id="modelId" value="%{getShowlet().getConfig().get('modelId')}"
							list="%{getModelsForContentType(widget.config['contentType'])}" headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="form-control" />
					</div>
				</div>

				<div class="form-group">
					<div class="col-xs-12">
						<label for="maxElemForItem"><s:text name="label.maxElementsForItem" /></label>
						<wpsf:select name="maxElemForItem" id="maxElemForItem" value="%{getShowlet().getConfig().get('maxElemForItem')}"
							headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}" cssClass="form-control" />
					</div>
				</div>

				<div class="form-group">
					<div class="col-xs-12">
						<label for="maxElements"><s:text name="label.maxElements" /></label>
						<wpsf:select name="maxElements" id="maxElements" value="%{getShowlet().getConfig().get('maxElements')}"
							headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}" cssClass="form-control" />
					</div>
				</div>
			</div>
		</fieldset>

		<fieldset class="margin-base-top">
			<legend data-toggle="collapse" data-target="#filters"><s:text name="title.filterOptions" />&#32;<span class="icon fa fa-chevron-down"></span></legend>

			<div class="collapse" id="filters">
				<div class="form-group">
					<div class="col-xs-12">
						<label for="category"><s:text name="label.categories" /></label>
						<div class="input-group">
							<wpsf:select name="categoryCode" id="category" list="categories" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" />
							<span class="input-group-btn">
								<wpsf:submit type="button" action="addCategory" cssClass="btn btn-info">
									<span class="icon fa fa-filter"></span>&#32;
									<s:text name="label.filter" />
								</wpsf:submit>
							</span>
						</div>
					</div>
				</div>

				<s:if test="null != categoryCodes && categoryCodes.size() > 0">
					<h3 class="sr-only"><s:text name="title.resourceCategories.list"/></h3>
					<s:iterator value="categoryCodes" var="categoryCodeVar">
					<s:set name="showletCategory" value="%{getCategory(#categoryCodeVar)}"></s:set>

					<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
					  <span class="icon fa fa-tag"></span>&#32;
					  <abbr title="<s:property value="#showletCategory.getFullTitle(currentLang.code)"/>">
					    <s:property value="#showletCategory.getShortFullTitle(currentLang.code)" />
					  </abbr>&#32;
					  <wpsa:actionParam action="removeCategory" var="actionName" >
					  	<wpsa:actionSubParam name="categoryCode" value="%{#categoryCodeVar}" />
					  </wpsa:actionParam>
					  <wpsf:submit
					    type="button"
					    action="%{#actionName}"
					    title="%{getText('label.remove') + ' ' + #showletCategory.getFullTitle(currentLang.code)}"
					    cssClass="btn btn-default btn-xs badge">
					     <span class="icon fa fa-times"></span>
					      <span class="sr-only">x</span>
					  </wpsf:submit>
					</span>

					</s:iterator>

				</s:if>
				<div class="clearfix"></div>
					<s:set var="clauseOrIsActiveVar" value="%{getShowlet().getConfig().get('orClauseCategoryFilter')}" />
					<s:set var="showClauseOrVar" value="%{null != categoryCodes && categoryCodes.size() > 1}" />
					<%--|<s:property value="#clauseOrIsActiveVar" />|
					|<s:property value="#showClauseOrVar" />|
					|<s:property value="#showClauseOrVar&&#clauseOrIsActiveVar" />|
					--%>
					<div class="margin-base-top margin-large-bottom display-block clearfix">
						<span data-toggle="buttons">
							<label for="category-filter-type-clause" class="btn btn-default
								<s:if test="#showClauseOrVar">
									<s:if test="#clauseOrIsActiveVar"> active </s:if>
								</s:if>
								<s:else> disabled </s:else>">
								<wpsf:checkbox
									name="orClauseCategoryFilter"
									fieldValue="true"
									value="%{#clauseOrIsActiveVar && #showClauseOrVar}"
									id="category-filter-type-clause"
									/>
									<s:text name="label.orClauseCategoryFilter" />
							</label>
						</span>
						<span class="help-block"><s:text name="note.orClauseCategoryFilter" /></span>
					</div>
				<%--
				<s:else>
					<p><s:text name="note.categories.none" /></p>
				</s:else>
				--%>
				<div class="form-group">
					<div class="col-xs-12">
						<label for="filterKey"><s:text name="label.filter" /></label>
						<div class="input-group">
							<wpsf:select name="filterKey" id="filterKey" list="allowedFilterTypes" listKey="key" listValue="value" cssClass="form-control" />
							<span class="input-group-btn">
								<wpsf:submit type="button" action="setFilterType" cssClass="btn btn-info">
									<span class="icon fa fa-plus-square"></span>&#32;
									<s:text name="label.add" />
								</wpsf:submit>
							</span>
						</div>
					</div>
				</div>

				<p class="sr-only">
					<wpsf:hidden name="filters" value="%{getShowlet().getConfig().get('filters')}" />
				</p>

				<s:if test="null != filtersProperties && filtersProperties.size()>0" >
					<ol class="list-group">
					<s:iterator value="filtersProperties" id="filter" status="rowstatus">
					<%--
						<s:property value="#rowstatus.index+1"/>
					--%>
						<li class="list-group-item">
							<s:text name="label.filterBy" /><strong>
								<s:if test="#filter['key'] == 'created'">
									<s:text name="label.creationDate" />
								</s:if>
								<s:elseif test="#filter['key'] == 'modified'">
									<s:text name="label.lastModifyDate" />
								</s:elseif>
								<s:else>
									<s:property value="#filter['key']" />
								</s:else>
							</strong><s:if test="(#filter['start'] != null) || (#filter['end'] != null) || (#filter['value'] != null)">,
							<s:if test="#filter['start'] != null">
								<s:text name="label.filterFrom" /><strong>
									<s:if test="#filter['start'] == 'today'">
										<s:text name="label.today" />
									</s:if>
									<s:else>
										<s:property value="#filter['start']" />
									</s:else>
								</strong>
								<s:if test="#filter['startDateDelay'] != null" >
									<s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['startDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />&nbsp;
								</s:if>
							</s:if>
							<s:if test="#filter['end'] != null">
								<s:text name="label.filterTo" /><strong>
									<s:if test="#filter['end'] == 'today'">
										<s:text name="label.today" />
									</s:if>
									<s:else>
										<s:property value="#filter['end']" />
									</s:else>
								</strong>
								<s:if test="#filter['endDateDelay'] != null" >
									<s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['endDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />
								</s:if>
							</s:if>
							<s:if test="#filter['value'] != null">
								<s:text name="label.filterValue" />:<strong> <s:property value="#filter['value']" /></strong>
									<s:if test="#filter['likeOption'] == 'true'">
										<em>(<s:text name="label.filterValue.isLike" />)</em>
									</s:if>
							</s:if>
							<s:if test="#filter['valueDateDelay'] != null" >
								<s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['valueDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />
							</s:if>
							</s:if>
							<s:if test="#filter['nullValue'] != null" >
								&nbsp;<s:text name="label.filterNoValue" />
							</s:if>
							&middot;
							<s:if test="#filter['order'] == 'ASC'"><s:text name="label.order.ascendant" /></s:if>
							<s:if test="#filter['order'] == 'DESC'"><s:text name="label.order.descendant" /></s:if>

							<div class="btn-toolbar pull-right">
								<div class="btn-group btn-group-sm">
									<wpsa:actionParam action="moveFilter" var="actionName" >
										<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
										<wpsa:actionSubParam name="movement" value="UP" />
									</wpsa:actionParam>
									<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveUp')}" cssClass="btn btn-default">
										<span class="sr-only"><s:text name="label.moveUp" /></span>
										<span class="icon fa fa-sort-desc"></span>
									</wpsf:submit>

									<wpsa:actionParam action="moveFilter" var="actionName" >
										<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
										<wpsa:actionSubParam name="movement" value="DOWN" />
									</wpsa:actionParam>
									<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveDown')}" cssClass="btn btn-default">
										<span class="sr-only"><s:text name="label.moveDown" /></span>
										<span class="icon fa fa-sort-asc"></span>
									</wpsf:submit>
								</div>
								<div class="btn-group btn-group-sm">
									<wpsa:actionParam action="removeFilter" var="actionName" >
										<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
									</wpsa:actionParam>
									<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
										<span class="icon fa fa-times-circle-o"></span>
									</wpsf:submit>
								</div>
							</div>
							<span class="clearfix"></span>
						</li>
					</s:iterator>
				</ol>
				</s:if>
				<%--
				<s:else>
					<p><s:text name="note.filters.none" /></p>
				</s:else>
				--%>
			</div>
		</fieldset>
                    
                    <%-- TITLES --%>
                    <s:if test="%{widget.type.hasParameter('title_%') || widget.type.hasParameter('pageLink') || widget.type.hasParameter('linkDescr_%')}" >
                        <fieldset class="margin-base-top">
                            <legend data-toggle="collapse" data-target="#options-extra"><s:text name="title.extraOption" />&#32;<span class="icon fa fa-chevron-down"></span></legend>
                            <div class="collapse" id="options-extra">
                                <p><s:text name="note.extraOption.intro" /></p>
                                <s:if test="%{widget.type.hasParameter('title_%')}" >
                                    <s:iterator id="lang" value="langs">
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
                                </s:if>
                                <s:if test="%{widget.type.hasParameter('pageLink')}" >
                                    <div class="form-group">
                                        <div class="col-xs-12">
                                            <label for="pageLink"><s:text name="label.page" /></label>
                                            <wpsf:select list="pages" name="pageLink" id="pageLink" listKey="code" listValue="getShortFullTitle(currentLang.code)"
                                                         value="%{widget.config.get('pageLink')}" headerKey="" headerValue="%{getText('label.none')}" cssClass="form-control" />
                                        </div>
                                    </div>
                                </s:if>
                                <s:if test="%{widget.type.hasParameter('linkDescr_%')}" >
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
                                </s:if>
                            </div>
                        </fieldset>
                    </s:if>
                    
                    <%-- USER FILTERS - START BLOCK --%>
                    <s:if test="%{widget.type.hasParameter('userFilters')}" >
                        <fieldset class="margin-base-top">
                            <legend data-toggle="collapse" data-target="#filters-frontend"><s:text name="title.filters.search" />&#32;<span class="icon fa fa-chevron-down"></span></legend>
                            
                            <div class="collapse" id="filters-frontend">
                                
                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <label for="userFilterKey"><s:text name="label.filter" /></label>
                                        <div class="input-group">
                                            <wpsf:select name="userFilterKey" id="userFilterKey" list="allowedUserFilterTypes" listKey="key" listValue="value" cssClass="form-control" />
                                            <span class="input-group-btn">
                                                <wpsf:submit type="button" action="addUserFilter" cssClass="btn btn-info">
                                                    <span class="icon fa fa-plus-square"></span>&#32;
                                                    <s:text name="label.add" />
                                                </wpsf:submit>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                
                                <p class="sr-only">
                                    <wpsf:hidden name="userFilters" value="%{getShowlet().getConfig().get('userFilters')}" />
                                </p>
                                
                                <s:if test="null != userFiltersProperties && userFiltersProperties.size() > 0" >
                                    <ol class="list-group">
                                        <s:iterator value="userFiltersProperties" id="userFilter" status="rowstatus">
                                            <li class="list-group-item">
                                                <s:text name="label.filterBy" />
                                                <strong>
                                                    <s:if test="#userFilter['attributeFilter'] == 'false'">
                                                        <s:if test="#userFilter['key'] == 'fulltext'">
                                                            <s:text name="label.fulltext" />
                                                        </s:if>
                                                        <s:elseif test="#userFilter['key'] == 'category'">
                                                            <s:text name="label.category" />
                                                            <s:if test="null != #userFilter['categoryCode']">
                                                                <s:set name="userFilterCategoryRoot" value="%{getCategory(#userFilter['categoryCode'])}"></s:set>
                                                                (<s:property value="#userFilterCategoryRoot.getFullTitle(currentLang.code)"/>)
                                                            </s:if>
                                                        </s:elseif>
                                                    </s:if>
                                                    <s:elseif test="#userFilter['attributeFilter'] == 'true'">
                                                        <s:property value="#userFilter['key']" />
                                                    </s:elseif>
                                                </strong>
                                                
                                                <div class="btn-toolbar pull-right">
                                                    <div class="btn-group btn-group-sm">
                                                        <wpsa:actionParam action="moveUserFilter" var="actionName" >
                                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                            <wpsa:actionSubParam name="movement" value="UP" />
                                                        </wpsa:actionParam>
                                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveUp')}" cssClass="btn btn-default">
                                                            <span class="icon fa fa-sort-desc"></span>
                                                        </wpsf:submit>
                                                        <wpsa:actionParam action="moveUserFilter" var="actionName" >
                                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                            <wpsa:actionSubParam name="movement" value="DOWN" />
                                                        </wpsa:actionParam>
                                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveDown')}" cssClass="btn btn-default">
                                                            <span class="icon fa fa-sort-asc"></span>
                                                        </wpsf:submit>
                                                    </div>
                                                    <div class="btn-group btn-group-sm">
                                                        <wpsa:actionParam action="removeUserFilter" var="actionName" >
                                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                        </wpsa:actionParam>
                                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
                                                            <span class="icon fa fa-times-circle-o"></span>
                                                        </wpsf:submit>
                                                    </div>
                                                </div>
                                                <span class="clearfix"></span>
                                            </li>
                                        </s:iterator>
                                    </ol>
                                </s:if>
                                
                            </div>
                            
                        </fieldset>
                    </s:if>
		<%-- USER FILTERS - END BLOCK --%>
	</div>
</div>

            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <wpsf:submit action="saveListViewerConfig" type="button" cssClass="btn btn-primary btn-block">
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="label.save" />
                    </wpsf:submit>
                </div>
            </div>
            
        </s:else>

    </s:form>
    
</div>
<div id="categoryAnchor" lastCategoryCode="<s:property value="%{getCategoryAnchor()}" />" />

<script>
    
    $('document').ready(function() {

        var category = document.getElementById("categoryAnchor").getAttribute("lastCategoryCode");
        if (category) {  
            setTimeout(function(){
                window.location.href = "#fagianonode_" + category;
            }, 250);
        }
    });

    
</script>