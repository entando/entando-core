<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

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
<br>

<div id="main" role="main">

    <p><s:text name="note.pageTree.intro" /></p>

    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.FieldErrors" /></strong>
            <ul>
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <div role="search">

        <s:if test="!hasFieldErrors()"><s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageSearchForm.jsp" /></s:if>

            <hr />


        <s:form action="search" cssClass="action-form">

            <p class="sr-only">
                <wpsf:hidden name="pageCodeToken" />
            </p>

            <s:set var="pagesFound" value="pagesFound" />

            <s:if test="%{#pagesFound != null && #pagesFound.isEmpty() == false}">
                <a href="<s:url namespace="/do/Page" action="new" />" class="btn btn-primary pull-right" title="<s:text name="label.new" />" style="margin-bottom: 5px">
                    <s:text name="label.add" />
                </a>
                <s:form cssClass="form-horizontal" namespace="/do/Page">

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

                </s:form>

            </s:if>
            <s:else>
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <strong><s:text name="noPages.found" /></strong>
                </div>
            </s:else>

        </s:form>
    </div>

</div>

<script>
    $(document).ready(function () {
        $("#expandAll").click(function () {
            $("#pageTree .childrenNodes").removeClass("hidden");
            $("#pageTree .childrenNodes").removeClass("collapsed");
            $('#pageTree .icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
        });
        $("#collapseAll").click(function () {
            $(".childrenNodes").addClass("hidden");
            $(".childrenNodes").addClass("collapsed");
            $('#pageTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');

        });

        $(".treeRow ").on("click", function (event) {
            $(".treeRow").removeClass("active");
            $(".moveButtons").addClass("hidden");
            $(this).find('.subTreeToggler').prop("checked", true);
            $(this).addClass("active");
            $(this).find(".moveButtons").removeClass("hidden");
        });

        function buildTree() {
            var isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;
            $('.table-treegrid').treegrid(null, isTreeOnRequest);
        }
        buildTree();
    });
</script>
