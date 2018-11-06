<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li class="page-title-container"><s:text name="title.pageTree" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.pageTree" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.pageTree.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br />

<div id="main" role="main">
    <div class="alert-container"></div>
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
    <div role="search">
        <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageSearchForm.jsp" />
        <a href="<s:url namespace="/do/Page" action="new" />" class="btn btn-primary pull-right" title="<s:text name="label.new" />" style="margin-bottom: 5px">
            <s:text name="label.add" />
        </a>
        <s:form cssClass="form-horizontal">

            <s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>

                <div class="table-responsive overflow-visible">
                    <s:if test="%{#pageTreeStyleVar == 'request'}">
                        <p class="sr-only">
                            <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
                                <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
                            </s:iterator>
                        </p>
                    </s:if>
                    <table id="pageTree" class="table table-bordered table-hover table-treegrid">
                        <thead>
                            <tr>
                                <th class="table-w-60"><s:text name="title.pageTree" />
                                <s:if test="#pageTreeStyleVar == 'classic'">
                                    <button type="button" class="btn-no-button expand-button" id="expandAll">
                                        <i class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;<s:text name="title.pageTree.expand" />
                                    </button>
                                    <button type="button" class="btn-no-button" id="collapseAll">
                                        <i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;<s:text name="title.pageTree.collapse" />
                                    </button>
                                </s:if>
                            </th>
                            <th class="text-center table-w-10"><s:text name="label.add|move" /></th>
                            <th class="text-center table-w-5"><s:text name="label.state" /></th>
                            <th class="text-center table-w-10"><s:text name="label.pageInMenu" /></th>
                            <th class="text-center table-w-5"><s:text name="label.actions" /></th>
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
        </s:form>
    </div>
</div>

