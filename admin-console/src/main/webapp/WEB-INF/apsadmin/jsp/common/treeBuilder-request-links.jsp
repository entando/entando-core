<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:if test="#currentRoot.isEmpty()">
    <s:set var="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
    <s:set var="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>

<s:if test="null == #openTreeActionName"><s:set var="openTreeActionName" value="'openCloseTreeNode'" /></s:if>
<s:if test="null == #closeTreeActionName"><s:set var="closeTreeActionName" value="'openCloseTreeNode'" /></s:if>
<tr id="${currentRoot.code}" data-parent="#${currentRoot.parent.code}"
    class="treeRow tree_node_flag ${liClassName}" >
    <td class="treegrid-node pointer">
        <s:if test="!#currentRoot.open && !#currentRoot.empty">
            <wpsa:actionParam action="%{#openTreeActionName}" var="openTreeAction" >
                <wpsa:actionSubParam name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
                <wpsa:actionSubParam name="targetNode" value="%{#currentRoot.code}" />
                <wpsa:actionSubParam name="treeNodeActionMarkerCode" value="open" />
            </wpsa:actionParam>
    <wpsf:submit cssClass="btn btn-link btn-xs" action="%{#openTreeAction}"
                 type="button" value="%{getText('label.open')}" title="%{getText('label.open')}">
        <span class="icon fa fa-plus" title="<s:text name="label.open" />"></span>
        <span class="sr-only"><s:text name="label.open" /></span>
    </wpsf:submit>
</s:if>
<s:elseif test="#currentRoot.open && !#currentRoot.empty">
    <wpsa:actionParam action="%{#closeTreeActionName}" var="closeTreeAction" >
        <wpsa:actionSubParam name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
        <wpsa:actionSubParam name="targetNode" value="%{#currentRoot.code}" />
        <wpsa:actionSubParam name="treeNodeActionMarkerCode" value="close" />
    </wpsa:actionParam>
    <wpsf:submit cssClass="btn btn-link btn-xs" action="%{#closeTreeAction}"
                 type="button" value="%{getText('label.close')}" title="%{getText('label.close')}">
        <span class="icon fa fa-minus" title="<s:text name="label.close" />"></span>
        <span class="sr-only"><s:text name="label.close" /></span>
    </wpsf:submit>
</s:elseif>

<input type="radio" name="${inputFieldName}"
       id="fagianonode_${currentRoot.code}"
       value="${currentRoot.code}"
       class="subTreeToggler ${(isSelected)?'active':''} hidden
       <s:if test="#currentRoot.children.length > 0">  tree_<s:property value="#currentRoot.code" /> </s:if>"
       <s:if test="#isSelected"> checked="checked"</s:if> />&#32;
<label for="fagianonode_${currentRoot.code}" class="${margin}">
    <span class="icon node-icon fa ${treeItemIconNameVar}"></span>
    <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
    <s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;
        <span class="text-muted icon fa fa-lock"></span>
    </s:if>
</label>
</td>
<s:if test="isPosition" >
    <td class="table-view-pf-actions">
        <div class="dropdown dropdown-kebab-pf">
            <button class="btn btn-menu-right dropdown-toggle" type="button" id="dropdownKebabRight1"
                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                <span class="fa fa-ellipsis-v"></span>
            </button>
            <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight1">
                <li>
                <wpsf:submit type="button" name="entandoaction:detail" value="Submit"
                             title="%{getText('category.options.detail')}"
                             data-toggle="tooltip" cssClass="btn-block">
                    <span> <s:text name="category.options.detail"/> </span>
                </wpsf:submit>
                </li>
                <li>
                <wpsf:submit type="button" name="entandoaction:new"
                             title="%{getText('category.options.add')}"
                             data-toggle="tooltip" cssClass="btn-block">
                    <span> <s:text name="category.options.add"/> </span>
                </wpsf:submit>
                </li>
                <li>
                <wpsf:submit type="button" name="entandoaction:edit" value="Submit"
                             title="%{getText('category.options.modify')}"
                             data-toggle="tooltip" cssClass="btn-block">
                    <span> <s:text name="category.options.modify"/> </span>
                </wpsf:submit>
                </li>
                <li>
                <wpsf:submit type="button" name="entandoaction:trash" value="Submit"
                             title="%{getText('category.options.delete')}"
                             data-toggle="tooltip" cssClass="btn-block">
                    <span> <s:text name="category.options.delete"/> </span>
                </wpsf:submit>
                </li>
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
