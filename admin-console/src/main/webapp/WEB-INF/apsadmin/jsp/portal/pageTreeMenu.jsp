<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="selectedPage" value="%{getPage(selectedNode)}" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li class="page-title-container"><s:text name="title.configPage.full" /></li>
</ol>

<h1 class="page-title-container">
    <i class="fa fa-circle green" title="Online"></i>
    <span class="page-title-big"><s:property value="%{getTitle(selectedNode, #selectedPage.draftTitles)}" /></span>
</h1>

<s:if test="hasActionErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.ActionErrors" /></strong>
        <ul>
            <s:iterator value="actionErrors">
                <li><s:property escapeHtml="false" /></li>
                </s:iterator>
        </ul>
    </div>
</s:if>

<div class="main-container">
    <div class="alert-container"></div>
    <div class="button-bar">
        <button type="button" data-toggle="collapse" data-target="#page-info" class="btn btn-default">
            <span><s:text name="page.treeInfo" /></span>
        </button>
        
        <s:url action="preview" var="previewURL">
            <s:param name="pageCode" value="pageCode" />
            <s:param name="token" value="previewToken" />
        </s:url>
        <a href="<s:property value="#previewURL" escapeXml="false" escapeHtml="false" />" target="_blank">
            <button class="btn btn-primary"><s:text name="pageActions.preview" /></button>
        </a>

        <div class="pull-right">
            <button class="btn btn-warning restore-online-btn"><s:text name="pageActions.restore" /></button>
            <button class="btn btn-default unpublish-btn"><s:text name="pageActions.unpublish" /></button>
            <button class="btn btn-success publish-btn"><s:text name="pageActions.publish" /></button>
        </div>

    </div>
    
    <div class="button-bar">
        <s:url action="setDefaultWidgets" var="setDefaultWidgetsUrl">
                <s:param name="pageCode" value="pageCode" />
        </s:url>
        <a class="defwidgets-btn-wrapper" href="<s:property value="#setDefaultWidgetsUrl" escapeXml="false" escapeHtml="false" />">
        	<button class="btn btn-default defwidgets-btn"><s:text name="pageActions.defwidgets" /></button>
       	</a>
       	<span class="defwidgets-label">
        	<span class="text"><s:text name="pageActions.defWidgetsApplied" /></span> <span class="glyphicon glyphicon-ok-circle" aria-hidden="true"></span>
       	</span>
        <div class="pull-right">
            <s:url action="setViewerPage" var="setViewerPageUrl">
                <s:param name="pageCode" value="pageCode" />
            </s:url>
            
	        <label><s:text name="pageActions.onfly" /></label>
	        <div class="dropdown on-the-fly-dropdown-wrapper">
				<button class="btn btn-default dropdown-toggle" type="button" id="on-the-fly-dropdown" data-toggle="dropdown">
					<span class="on-the-fly-dropdown-text">...</span>
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu" role="menu" aria-labelledby="on-the-fly-dropdown">
					<li role="presentation">
						<a href="<s:property value="#setViewerPageUrl" escapeXml="false" escapeHtml="false" />">
                			<s:text name="label.yes" />
            			</a>
					</li>
					<li role="presentation">
						<a href="#" class="unset-on-the-fly-btn">
                			<s:text name="label.no" />
            			</a>
					</li>
				</ul>
			</div>
        </div>

    </div>

    <div id="page-info" class="collapse" style="margin-top:1em;">
        <table class="table">
            <tbody>
                <tr>
                    <th class="td-pagetree-width"><s:text name="name.pageCode" /></th>
                    <td data-info-pagecode></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="name.pageTitle" /></th>
                    <td data-info-titles></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="name.ownerGroup" /></th>
                    <td data-info-group></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="name.pageModel" /></th>
                    <td data-info-model></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="name.isShowablePage" /></th>
                    <td data-info-showmenu></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width">
                        <span lang="en" title="<s:text name="name.SEO.full" />">
                            <s:text name="name.SEO.short" /></span>: <s:text name="name.useBetterTitles" />
                    </th>
                    <td data-info-extratitles></td>
                </tr>
            </tbody>
        </table>
    </div>
    <!----------------- griglia del template ----------->
    <div class="grid-container"></div>
</div>

<!----------------- widget menu sinistra ----------->

<div id="widget-sidebar" class="drawer-pf-sidebar-right">


    <a  id="widget-sidebar-page-tree-btn" class="drawer-pf-title drawer-pf-title-right-menu drawer-pf-title-clickable ">
        <span class="right-bar-title" >
            <i class="fa fa-sitemap fa-18px" aria-hidden="true"></i>&nbsp;&nbsp;&nbsp;<span class="list-group-item-value"><s:text name="title.pages" /></span>
            <span class="open-button-menu-right pull-right"><i class="fa fa-angle-right" aria-hidden="true"></i></span>
        </span>
    </a>
    <div class="panel-group">
        <div class="drawer-pf-container">
            <s:include value="/WEB-INF/apsadmin/jsp/portal/widget-list-menu.jsp" />
        </div>
    </div>

</div>

<!-----------------drawer menu sinistra----------->

<div id="sidebar-page-tree" class="drawer-pf hide drawer-pf-notifications-non-clickable">
    <div class="drawer-pf-title drawer-pf-title-right-menu">
        <a id="toggle-expand" class="drawer-pf-toggle-expand"></a>
        <span class="right-bar-title-pages"><s:text name="title.pages" /></span>
        <span id="close-page-tree-sidebar" class=" close-button-menu-right pull-right"><i class="fa fa-times" aria-hidden="true"></i></span>
    </div>
    <div class="panel-group" id="notification-drawer-accordion">

        <%--<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageSearchForm.jsp" />--%>
        <s:form cssClass="action-form">
            <wpsf:submit action="new" type="button" title="%{getText('page.options.new')}" cssClass=" btn-primary btn-links " data-toggle="tooltip">
                <i class="fa fa-plus" aria-hidden="true"></i>&nbsp;<s:text name="title.addPage" />
            </wpsf:submit>
            <s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
            <s:if test="#pageTreeStyleVar == 'classic'">
                <div class="drawer-pf-notification drawer-pf-notification-right-menu-expansion ">
                    <button type="button" class="btn-no-button-right" id="expandAll">
                        <i class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></i><span class="treeInteractionButtons">&nbsp;<s:text name="title.expand" /></span>
                    </button>
                    <button type="button" class="btn-no-button-right" id="collapseAll">
                        <i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i><span class="treeInteractionButtons">&nbsp;<s:text name="title.collapse" /></span>
                    </button>
                </div>
            </s:if>
            <div class="overflow-visible table-menu-left-postion" >
                <table id="pageTree" class="table table-tree-sidebar table-hover table-treegrid table-tree-right" style="overflow:  scroll; margin-bottom: 6em;">
                    <thead>
                    </thead>
                    <tbody>
                        <s:set var="inputFieldName" value="%{'selectedNode'}" />
                        <s:set var="selectedTreeNode" value="%{selectedNode}" />
                        <s:set var="liClassName" value="'page'" />
                        <s:set var="treeItemIconName" value="'fa-folder'" />
                        <s:if test="#pageTreeStyleVar == 'classic'">
                            <s:set var="currentRoot" value="allowedTreeRootNode" />
                            <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilderPagesMenu.jsp" />
                        </s:if>
                        <s:else>
                        <style>
                            .table-treegrid span.collapse-icon, .table-treegrid span.expand-icon {
                                cursor: pointer;
                                display: none;
                            }
                        </style>
                        <s:set var="currentRoot" value="showableTree" />
                        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-linksPagesMenu.jsp" />
                    </s:else>
                    </tbody>
                </table>
            </div>
            <p class="sr-only"><wpsf:hidden name="copyingPageCode" /></p>
        </s:form>
    </div>
</div>
