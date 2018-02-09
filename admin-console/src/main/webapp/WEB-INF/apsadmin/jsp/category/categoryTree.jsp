<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li>
        <s:text name="title.categoryManagement" />
    </li>
    <li class="page-title-container">
        <s:text name="title.categoryTree" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="title.categoryManagement"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="page.category.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active">
                    <a href="<s:url namespace="/do/Category" action="viewTree" />"><s:text name="title.categoryTree"/></a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/Category" action="configSystemParams" />"><s:text name="title.categorySettings"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main" role="main">
    <div role="search">

        <a href="<s:url namespace="/do/Category" action="new" />"
           class="btn btn-primary pull-right"
           title="<s:text name="label.new" />" style="margin-bottom: 5px">
            <s:text name="label.add" />
        </a>
        <s:form cssClass="action-form">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <strong><s:text name="message.title.ActionErrors" /></strong>
                    <s:iterator value="actionErrors">
                        <span><s:property escapeHtml="false" /></span>
                    </s:iterator>
                </div>

            </s:if>
            <s:if test="hasActionMessages()">
                <div class="alert alert-success alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-ok"></span>
                    <strong><s:text name="messages.confirm" /></strong>
                    <s:iterator value="actionMessages">
                        <span><s:property escapeHtml="false" /></span>
                    </s:iterator>
                </div>
            </s:if>

            <s:if test="serviceStatus != 0">
                <div class="alert alert-info">
                    <button class="close hidden" data-dismiss="alert">
                        <span class="icon fa fa-times"></span>
                    </button>
                    <span
                        data-entando-progress-url="<s:url action="displayUpdatingReferencesStatus" />"
                        data-entando-progress-template-wip="<strong>Updating references {{percentage}}%&hellip;</strong>&#32;<small class='text-muted'>({{done}} / {{total}})</small>"
                        data-entando-progress-template-done="<strong>References updated successfully!</strong>">
                        <strong>Updating references</strong>
                    </span>
                </div>
            </s:if>

            <s:set var="categoryTreeStyleVar">
                <wp:info key="systemParam" paramName="treeStyle_category" />
            </s:set>

            <script src="<wp:resourceURL />administration/js/entando-typeahead-tree.js"></script>

            <div class="table-responsive overflow-visible">
                <table id="categoryTree"
                       class="table table-bordered table-hover table-treegrid">
                    <thead>
                        <tr>
                            <th> <s:text name="title.categoryTree"/>
                                <s:if test="#categoryTreeStyleVar == 'classic'">
                                    <button type="button" class="btn-no-button expand-button"
                                            id="expandAll">
                                        <i class="fa fa-plus-square-o treeInteractionButtons"
                                           aria-hidden="true"></i>&#32;<s:text name="label.category.expandAll"/>
                                    </button>
                                    <button type="button" class="btn-no-button" id="collapseAll">
                                        <i class="fa fa-minus-square-o treeInteractionButtons"
                                           aria-hidden="true"></i>&#32;<s:text name="label.category.collapseAll"/>
                                    </button>
                                </s:if>
                            </th>
                            <th class="text-center table-w-5"><s:text name="label.category.actions"/></th>
                        </tr>
                    </thead>

                    <tbody>
                        <s:set var="inputFieldName" value="%{'selectedNode'}" />
                        <s:set var="selectedTreeNode" value="%{selectedNode}" />
                        <s:set var="selectedPage" value="%{getCategory(selectedNode)}" />
                        <s:set var="isPosition" value="true" />
                        <s:set var="treeItemIconName" value="'fa-folder'" />
                        <s:if test="#categoryTreeStyleVar == 'classic'">
                            <s:set var="currentRoot" value="allowedTreeRootNode" />
                            <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
                        </s:if>
                        <s:elseif test="#categoryTreeStyleVar == 'request'">
                            <style>
                                .table-treegrid span.collapse-icon, .table-treegrid span.expand-icon {
                                    cursor: pointer;
                                    display: none;
                                }
                            </style>
                            <s:set var="openTreeActionName" value="'openCloseCategoryTree'" />
                            <s:set var="closeTreeActionName" value="'openCloseCategoryTree'" />
                            <s:set var="currentRoot" value="showableTree" />
                            <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
                        </s:elseif>
                    </tbody>
                </table>
            </div>

        </s:form>
    </div>
</div>
