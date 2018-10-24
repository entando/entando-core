<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="#currentRoot.isEmpty()">
    <s:set var="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
    <s:set var="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>

<s:if test="%{null == #actionName}">
    <s:set var="actionName" value="'joinCategory'"/>
</s:if>

<s:if test="null == #openTreeActionName"><s:set var="openTreeActionName" value="'openCloseTreeNode'" /></s:if>
<s:if test="null == #closeTreeActionName"><s:set var="closeTreeActionName" value="'openCloseTreeNode'" /></s:if>

<s:set var="isSelected" value="%{#currentRoot.code == #selectedTreeNode}" ></s:set>
<s:set var="nodeType" value="%{(#currentRoot.code != 'home')?'childrenNodes ':''}"/>
<s:set var="margin" value="%{(#currentRoot.empty)?'ml-20':'no-ml'}"/>

<tr id="${currentRoot.code}" data-parent="#${currentRoot.parent.code}"
    class="treeRow <s:if test="%{#currentRoot.code != 'home' && #isHidden}">collapsed childrenNodes</s:if> tree_node_flag ${liClassName}" >
        <td class="treegrid-node pointer">
        <s:if test="!#currentRoot.open && !#currentRoot.empty">
            <wpsa:actionParam action="%{#openTreeActionName}" var="openTreeAction" >
                <wpsa:actionSubParam name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
                <wpsa:actionSubParam name="targetNode" value="%{#currentRoot.code}" />
                <wpsa:actionSubParam name="treeNodeActionMarkerCode" value="open" />
                <wpsa:actionSubParam name="openCollapsed" value="'true'" />
            </wpsa:actionParam>
            <wpsf:submit cssClass="btn btn-link btn-xs" action="%{#openTreeAction}"
                         type="button" value="%{getText('label.open')}" title="%{getText('label.open')}">
                <span class="icon fa fa-plus" title="<s:text name="label.open" />"></span>
                <span class="sr-only"><s:text name="label.open" /></span>
                <span class="icon node-icon fa ${treeItemIconNameVar}"></span>
                <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
            </wpsf:submit>
        </s:if>
        <s:elseif test="#currentRoot.open && !#currentRoot.empty">
            <wpsa:actionParam action="%{#closeTreeActionName}" var="closeTreeAction" >
                <wpsa:actionSubParam name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
                <wpsa:actionSubParam name="targetNode" value="%{#currentRoot.code}" />
                <wpsa:actionSubParam name="treeNodeActionMarkerCode" value="close" />
                <wpsa:actionSubParam name="openCollapsed" value="'true'" />
            </wpsa:actionParam>
            <wpsf:submit cssClass="btn btn-link btn-xs" action="%{#closeTreeAction}"
                         type="button" value="%{getText('label.close')}" title="%{getText('label.close')}">
                <span class="icon fa fa-minus" title="<s:text name="label.close" />"></span>
                <span class="sr-only"><s:text name="label.close" /></span>
                <span class="icon node-icon fa ${treeItemIconNameVar}"></span>
                <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
            </wpsf:submit>
        </s:elseif>
        <input type="radio" name="${inputFieldName}"
               id="fagianonode_${currentRoot.code}"
               value="${currentRoot.code}"
               class="subTreeToggler <s:if test="#isSelected">active </s:if> hidden<s:if test="#currentRoot.children.length > 0">  tree_<s:property value="#currentRoot.code" /> </s:if>"
               <s:if test="#isSelected"> checked="checked"</s:if> />&#32;
        <label for="fagianonode_${currentRoot.code}">
            <s:if test="#currentRoot.empty">
                <span class="icon node-icon fa ${treeItemIconNameVar}"></span>
                <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
            </s:if>
            <s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;
                <span class="text-muted icon fa fa-lock"></span>
            </s:if>
        </label>
    </td>
    <s:if test="%{#skipJoinAction == null || #skipJoinAction.equals('false')}">
        <td class="text-center">
            <s:if test="%{!#currentRoot.isRoot()}">
                <wpsa:actionParam action="joinCategory" var="joinCategoryActionName" >
                    <wpsa:actionSubParam name="categoryCode" value="%{#currentRoot.code}" />
                </wpsa:actionParam>
                <wpsf:submit action="%{#joinCategoryActionName}" type="button"
                             title="%{getText('label.join')}" cssClass="btn btn-sm btn-link js_joinCategory">
                    <span class="icon fa fa-plus"></span>
                </wpsf:submit>
            </s:if>
        </td>
    </s:if>
</tr>

<s:if test="#currentRoot.children.length > 0">
    <ul class="treeToggler fa-ul" id="tree_<s:property value="#currentRoot.code" />">
        <s:iterator value="#currentRoot.children" var="node">
            <s:set var="currentRoot" value="#node" />
            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilder-request-categories.jsp" />
        </s:iterator>
    </ul>
</s:if>
