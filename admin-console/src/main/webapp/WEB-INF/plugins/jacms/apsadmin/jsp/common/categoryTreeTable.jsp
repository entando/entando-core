<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="categoryTreeStyleVar">
    <wp:info key="systemParam" paramName="treeStyle_category" />
</s:set>

<script>
    (function () {

        function getCategoryFormData() {
            var data = [];
            $('#categoryTree').closest('form').serializeArray().forEach(el => {
                // This can cause the "Form too large" exception
                if(el.name !== 'base64Image') {
                    data.push(el);
                }
            });
            return data;
        }

        function updateCategory(postEndpoint, categoryAction, category) {
            var data = getCategoryFormData();
            data.push({
                'name': 'entandoaction:' + categoryAction + '?categoryCode=' + category,
                'value': 'Submit'
            });
            $.post(postEndpoint, data, function (res) {
                var updated = $(res).find('#categoryList');
                $('#categoryList').html(updated);
            });
        }

        window.categoriesAjax = {

            loadTreeNode: function (open, postEndpoint, nodeAction, targetNode) {
                var data = getCategoryFormData();
                data.push({
                    'name': "entandoaction:" + nodeAction + "?treeNodeActionMarkerCode="
                            + (open ? "open" : "close") + ";openCollapsed='true';"
                            + "targetNode=" + targetNode,
                    'value': (open ? "Open" : "Close")
                });
                $.post(postEndpoint, data, function (res) {
                    var updated = $(res).find('#categoryTreeWrapper');
                    $('#categoryTreeWrapper').html(updated);
                    $('.table-treegrid').treegrid(null, true);
                });
            },

            joinCategory: function (postEndpoint, category, joinAction) {
                updateCategory(postEndpoint, joinAction || 'joinCategory', category);
            },

            removeCategory: function (postEndpoint, category, removeAction) {
                updateCategory(postEndpoint, removeAction || 'removeCategory', category);
            }
        };
    })();
</script>

<div class="table-responsive" id="categoryTreeWrapper">

    <s:if test="%{#categoryTreeStyleVar == 'request'}">
        <p class="sr-only">
            <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
                <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
            </s:iterator>
        </p>
    </s:if>

    <table id="categoryTree"
           class="table table-bordered table-hover table-treegrid <s:property value="#categoryTreeStyleVar" />">
        <thead>
            <tr>
                <th>
                    <s:text name="label.category.tree"/>
                    <s:if test="%{#categoryTreeStyleVar == 'classic'}">
                        <button type="button" class="btn-no-button expand-button" id="expandAll">
                            <i class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></i>
                            &#32;
                            <s:text name="label.category.expandAll"/>
                        </button>
                        <button type="button" class="btn-no-button" id="collapseAll">
                            <i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i>
                            &#32;
                            <s:text name="label.category.collapseAll"/>
                        </button>
                    </s:if>
                </th>
                <s:if test="%{#skipJoinAction == null || #skipJoinAction.equals('false')}">
                    <th class="text-center table-w-10">
                        <s:text name="label.category.join"/>
                    </th>
                </s:if>
            </tr>
        </thead>
        <tbody>
            <s:set var="inputFieldName" value="'categoryCode'" />
            <s:set var="selectedTreeNode" value="categoryCode" />
            <s:set var="liClassName" value="'category'" />
            <s:set var="treeItemIconName" value="'fa-folder'" />
            <s:if test="%{#categoryTreeStyleVar == 'classic'}">
                <s:set var="currentRoot" value="%{allowedTreeRootNode}" />
                <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilderCategoriesJoin.jsp" />
            </s:if>
            <s:elseif test="%{#categoryTreeStyleVar == 'request'}">
                <s:set var="currentRoot" value="%{showableTree}" />
                <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilder-request-categories.jsp" />
            </s:elseif>
        </tbody>
    </table>
</div>
