<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="selectedPage" value="%{getPage(selectedNode)}" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li><s:text name="title.configPage.full" /></li>
</ol>

<h1><s:property value="%{getTitle(selectedNode, #selectedPage.draftTitles)}" /></h1>

<s:if test="hasActionErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <s:text name="message.title.ActionErrors" />
        <ul>
            <s:iterator value="actionErrors">
                <li><s:property escape="false" /></li>
                </s:iterator>
        </ul>
    </div>
</s:if>

<div class="main-container">
    <div class="alert-container"></div>
    <div>
        <button type="button" data-toggle="collapse" data-target="#page-info" class="btn btn-sm btn-primary margin-large-bottom">
            <span class="icon fa fa-chevron-down"></span>&nbsp;&nbsp;
            <span class="info-title"><s:text name="page.treeInfo" /></span>
        </button>

        <button class="btn btn-success publish-btn pull-right"><s:text name="pageActions.publish" /></button>
        <button class="btn btn-danger unpublish-btn pull-right"><s:text name="pageActions.unpublish" /></button>
        <button class="btn btn-warning restore-online-btn pull-right"><s:text name="pageActions.restore" /></button>
    </div>

    <div id="page-info" class="collapse">
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
                    <th class="td-pagetree-width">Owner Group</th>
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
                        <abbr lang="en" title="<s:text name="name.SEO.full" />">
                            <s:text name="name.SEO.short" />
                        </abbr>: <s:text name="name.useBetterTitles" />   </th>
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
            <div class="table-responsive overflow-visible table-menu-left-postion" >
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

<!--            <fieldset data-toggle="tree-toolbar"><legend><s:text name="title.pageActions" /></legend>
                <p class="sr-only"><s:text name="title.pageActionsIntro" /></p>

                <div class="btn-toolbar" data-toggle="tree-toolbar-actions">
                    <div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
            <wpsf:submit action="new" type="button" title="%{getText('page.options.new')}" cssClass="btn btn-info" data-toggle="tooltip">
                <span class="icon fa fa-plus-circle"></span>
            </wpsf:submit>
        </div>
    </div>
</fieldset>-->
        </s:form>
    </div>
    <script>

        $(document).ready(function () {
            // Show/Hide Notifications Drawer
            if ($('#sidebar-page-tree').hasClass('drawer-pf-expanded')) {
                $('.moveButtons-right-container').show();
            } else {
                $('.moveButtons-right-container').hide();
            }

            $('#widget-sidebar-page-tree-btn').click(function () {

                var $drawer = $('#sidebar-page-tree');

                $(this).toggleClass('open');
                if ($drawer.hasClass('hide')) {
                    $drawer.removeClass('hide');


                    setTimeout(function () {
                        if (window.dispatchEvent) {
                            window.dispatchEvent(new Event('resize'));
                        }
                        // Special case for IE
                        if ($(document).fireEvent) {
                            $(document).fireEvent('onresize');
                        }
                    }, 100);
                } else {
                    console.log('animate in');
                    $drawer.addClass('hide');
                }
            });


            $('#close-page-tree-sidebar').click(function () {
                var $drawer = $('.drawer-pf');
                $drawer.addClass('hide');

            });


            $('#toggle-expand').click(function () {
                var $drawer = $('#sidebar-page-tree');
                var $drawerNotifications = $drawer.find('.drawer-pf-notification');

                if ($drawer.hasClass('drawer-pf-expanded')) {
                    $('.moveButtons-right-container').hide();
                    $drawer.removeClass('drawer-pf-expanded');
                    $drawerNotifications.removeClass('expanded-notification');
                } else {
                    $('.moveButtons-right-container').show();
                    $drawer.addClass('drawer-pf-expanded');
                    $drawerNotifications.addClass('expanded-notification');
                }

            });

            // Mark All Read
            $('.panel-collapse').each(function (index, panel) {
                var $panel = $(panel);
                $panel.on('click', '.drawer-pf-action .btn', function () {
                    $panel.find('.unread').removeClass('unread');
                    $(panel.parentElement).find('.panel-counter').text('0 New Events');
                });
            });

            $('#notification-drawer-accordion').initCollapseHeights('.panel-body');

        });

    </script>
    <script>

        $(document).ready(function () {

            $("#expandAll").click(function () {
                $(".childrenNodes").removeClass("hidden");
            });
            $("#collapseAll").click(function () {
                $(".childrenNodes").addClass("hidden");
            });

            var isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;
            $('.table-treegrid').treegrid(null, isTreeOnRequest);
            $(".treeRow ").on("click", function (event) {
                $(".treeRow").removeClass("active");
                $(".moveButtons-right").addClass("hidden");
                $(".table-view-pf-actions").addClass("hidden");
                $(this).find('.subTreeToggler').prop("checked", true);
                $(this).addClass("active");
                $(this).find(".moveButtons-right").removeClass("hidden");
                $(this).find(".table-view-pf-actions").removeClass("hidden");
            });
        });

    </script> 

</div>
<s:url action="preview" var="previewURL">
	<s:param name="pageCode" value="pageCode" />
	<s:param name="token" value="previewToken" />
</s:url>
<a href="<s:property value="#previewURL" escape="false" escapeXml="false" escapeHtml="false" />" target="_blank">PREVIEW</a>


