<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:if test="#currentRoot.isEmpty()">
    <s:set var="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
    <s:set var="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>

<s:set var="isSelected" value="%{#currentRoot.code == #selectedTreeNode}" ></s:set>

<s:if test="null == #openTreeActionName"><s:set var="openTreeActionName" value="'openCloseTreeNode'" /></s:if>
<s:if test="null == #closeTreeActionName"><s:set var="closeTreeActionName" value="'openCloseTreeNode'" /></s:if>
<tr id="${currentRoot.code}" data-parent="#${currentRoot.parentCode}"
    class="treeRow tree_node_flag ${liClassName}" >
    <td class="treegrid-node pointer">
        <s:if test="%{!#currentRoot.open && !#currentRoot.empty}">
            <a href="<s:url action="%{#openTreeActionName}">
                   <wpsa:paramMap map="%{#treeNodeExtraParamsMap}" />
                   <s:param name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
                   <s:param name="treeNodeActionMarkerCode" value="'open'" />
                   <s:param name="targetNode" value="%{#currentRoot.code}" />
                   <s:param name="treeNodesToOpen" value="treeNodesToOpen" /></s:url>">
                <span class="icon fa fa-plus" title="<s:text name="label.open" />"></span>
                <span class="sr-only"><s:text name="label.open" /></span>
                <span class="icon node-icon fa ${treeItemIconNameVar}"></span>
                <s:property value="%{getTitle(#currentRoot.code, #currentRoot.titles)}" />
            </a>
        </s:if>
        <s:elseif test="%{#currentRoot.open && !#currentRoot.empty}">
            <a href="<s:url action="%{#closeTreeActionName}">
                   <wpsa:paramMap map="%{#treeNodeExtraParamsMap}" />
                   <s:param name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
                   <s:param name="treeNodeActionMarkerCode" value="'close'" />
                   <s:param name="targetNode" value="%{#currentRoot.code}" />
                   <s:param name="treeNodesToOpen" value="treeNodesToOpen" /></s:url>">
                <span class="icon fa fa-minus" title="<s:text name="label.close" />"></span>
                <span class="sr-only"><s:text name="label.close" /></span>
                <span class="icon node-icon fa ${treeItemIconNameVar}"></span>
                <s:property value="%{getTitle(#currentRoot.code, #currentRoot.titles)}" />
            </a>
        </s:elseif>
        
        <input type="radio" name="${inputFieldName}"
               id="fagianonode_${currentRoot.code}"
               value="${currentRoot.code}"
               class="subTreeToggler <s:if test="#isSelected">active</s:if> hidden
               <s:if test="#currentRoot.children.length > 0">  tree_<s:property value="#currentRoot.code" /> </s:if>"
               <s:if test="#isSelected"> checked="checked"</s:if> />&#32;
        <label for="fagianonode_${currentRoot.code}" class="${margin}">
            <s:if test="%{#currentRoot.empty}">
                <span class="icon node-icon fa ${treeItemIconNameVar}"></span>
                <s:property value="%{getTitle(#currentRoot.code, #currentRoot.titles)}" />
            </s:if>
            <s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;
                <span class="text-muted icon fa fa-lock"></span>
            </s:if>
        </label>
    </td>
    <s:if test="isPosition" >
        <td class="table-view-pf-actions text-center">
            <div class="dropdown dropdown-kebab-pf">
                <button class="btn btn-menu-right dropdown-toggle" type="button" id="dropdownKebabRight1"
                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                    <span class="fa fa-ellipsis-v"></span>
                </button>
                <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight1">
                    <li>
                        <a href="<s:url action="detail">
                               <s:param name="selectedNode" value="%{#currentRoot.code}" /></s:url>"
                           title="<s:text name="category.options.detail" />" class="btn-block">
                            <s:text name="category.options.detail"/>
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="new">
                               <s:param name="selectedNode" value="%{#currentRoot.code}" />
                               <s:param name="parentCategoryCode" value="%{#currentRoot.code}" /></s:url>"
                           title="<s:text name="category.options.add" />" class="btn-block">
                            <s:text name="category.options.add"/>
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="edit">
                               <s:param name="selectedNode" value="%{#currentRoot.code}" /></s:url>"
                           title="<s:text name="category.options.modify" />" class="btn-block">
                            <s:text name="category.options.modify"/>
                        </a>
                    </li>
                    <s:if test="%{!#currentRoot.isRoot()}">
                        <li>
                            <a href="<s:url action="trash">
                                   <s:param name="selectedNode" value="%{#currentRoot.code}" /></s:url>"
                               title="<s:text name="category.options.delete" />" class="btn-block">
                                <s:text name="category.options.delete"/>
                            </a>
                        </li>
                    </s:if>
                </ul>
            </div>
        </td>
    </s:if>
</tr>
<s:if test="#currentRoot.children.length > 0">
    <ul class="treeToggler fa-ul"  id="tree_<s:property value="#currentRoot.code" />">
        <s:iterator value="#currentRoot.children" var="node">
            <s:set var="currentRoot" value="#node" />
            <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
        </s:iterator>
    </ul>
</s:if>
