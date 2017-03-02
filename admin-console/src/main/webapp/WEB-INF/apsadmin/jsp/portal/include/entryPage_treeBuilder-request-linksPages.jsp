<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
    
<s:if test="#currentRoot.isEmpty()">
    <s:set name="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
    <s:set name="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>
<tr id="<s:property value="#currentRoot.code" />" data-parent="#<s:property value="#currentRoot.parent.code" />" class="treeRow <s:if test="%{#currentRoot.code != 'homepage'}"></s:if>" >
        <td class="treegrid-node pointer">
        <s:if test="null == #openTreeActionName"><s:set var="openTreeActionName" value="'openCloseTreeNode'" /></s:if>
        <s:if test="null == #closeTreeActionName"><s:set var="closeTreeActionName" value="'openCloseTreeNode'" /></s:if>
        <s:if test="!#currentRoot.open && !#currentRoot.empty">
            <a class="treeOpenCloseJS"
               href="<s:url action="%{#openTreeActionName}">
                   <wpsa:paramMap map="#treeNodeExtraParamsMap" />
                   <s:param name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
                   <s:param name="copyingPageCode" value="copyingPageCode" />
                   <s:param name="treeNodeActionMarkerCode" value="'open'" /><s:param name="targetNode" value="#currentRoot.code" /><s:param name="treeNodesToOpen" value="treeNodesToOpen" /></s:url>">
                <span class="treeOpenCloseJS icon fa fa-plus" title="<s:text name="label.open" />"></span>
                <span class="sr-only"><s:text name="label.open" /></span>
            </a>
        </s:if>
        <s:elseif test="#currentRoot.open && !#currentRoot.empty">
            <a class="treeOpenCloseJS noborder" href="<s:url action="%{#closeTreeActionName}">
                   <wpsa:paramMap map="#treeNodeExtraParamsMap" />
                   <s:param name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
                   <s:param name="copyingPageCode" value="copyingPageCode" />
                   <s:param name="treeNodeActionMarkerCode" value="'close'" /><s:param name="targetNode" value="#currentRoot.code" /><s:param name="treeNodesToOpen" value="treeNodesToOpen" /></s:url>">
                <span class="treeOpenCloseJS icon fa fa-minus" title="<s:text name="label.close" />"></span>
                <span class="sr-only"><s:text name="label.close" /></span>
            </a>
        </s:elseif>
        <input
            type="radio"
            class="subTreeToggler hidden"
            name="<s:property value="#inputFieldName" />"
            id="fagianonode_<s:property value="#currentRoot.code" />"
            value="<s:property value="#currentRoot.code" />" <s:if test="#currentRoot.children.length > 0">class="subTreeToggler tree_<s:property value="#currentRoot.code" />" </s:if>
            <s:if test="#selectedTreeNode != null && #currentRoot.code == #selectedTreeNode.code"> checked="checked"</s:if> />
        <label for="fagianonode_<s:property value="#currentRoot.code" />">
            <span class="icon node-icon fa <s:property value="#treeItemIconNameVar" />"></span>
            <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
            <s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;<span class="text-muted icon fa fa-lock"></span></s:if>
            </label>
                
        </td>
    </td>
</tr>
    
<s:if test="#currentRoot.children.length > 0">
    <s:iterator value="#currentRoot.children" var="node">
        <s:set var="currentRoot" value="#node" />
        <s:include value="entryPage_treeBuilder-request-linksPages.jsp" />
    </s:iterator>
</s:if>