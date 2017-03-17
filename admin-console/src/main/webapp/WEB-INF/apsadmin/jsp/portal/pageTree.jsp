<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li class="page-title-container"><s:text name="title.pageTree" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.pageTree" /></h1>

<div id="main" role="main">
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

    <div role="search">

        <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageSearchForm.jsp" />

        <s:form cssClass="form-horizontal">
            <wpsf:submit action="new" type="button" title="%{getText('page.options.new')}" cssClass="btn btn-primary  " data-toggle="tooltip">
                <s:text name="label.add" />
            </wpsf:submit>
            <br>
            <br>
            <s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>

                <div class="table-responsive overflow-visible">
                    <table id="pageTree" class="table table-bordered table-hover table-treegrid">
                        <thead>
                            <tr>
                                <th style="width: 68%;">Tree Pages
                                <s:if test="#pageTreeStyleVar == 'classic'">
                                    <button type="button" class="btn-no-button expand-button" id="expandAll">
                                        <i class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;Expand All
                                    </button>
                                    <button type="button" class="btn-no-button" id="collapseAll">
                                        <i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;Collapse All
                                    </button>
                                </s:if>
                            </th>
                            <th class="text-center" style="width: 8%;"><s:text name="label.add|move" /></th>
                            <th class="text-center" style="width: 8%;"><s:text name="label.state" /></th>
                            <th class="text-center" style="width: 8%;"><s:text name="label.pageInMenu" /></th>
                            <th class="text-center" style="width: 5%;"><s:text name="label.actions" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:set var="inputFieldName" value="%{'selectedNode'}" />
                        <s:set var="selectedTreeNode" value="%{selectedNode}" />
                        <s:set var="selectedPage" value="%{getPage(selectedNode)}" />
                        <s:set var="liClassName" value="'page'" />
                        <s:set var="treeItemIconName" value="'fa-folder'" />
                        <s:if test="#pageTreeStyleVar == 'classic'">
                            <s:set var="currentRoot" value="allowedTreeRootNode" />
                            <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilderPages.jsp" />
                        </s:if>
                        <s:else>
                        <style>
                            .table-treegrid span.collapse-icon, .table-treegrid span.expand-icon {
                                cursor: pointer;
                                display: none;
                            }
                        </style>
                        <s:set var="currentRoot" value="showableTree" />
                        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-linksPages.jsp" />
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

</div>