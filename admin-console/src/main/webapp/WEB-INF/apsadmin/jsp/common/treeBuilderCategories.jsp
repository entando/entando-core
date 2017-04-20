<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="%{#currentRoot.getChildren().length==0}">
    <s:set var="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
    <s:set var="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>

<s:set var="isHidden" value="%{#selectedTreeNode == null || (#selectedTreeNode != #currentRoot.code)}" ></s:set>
<!--  && !#selectedTreeNode.isChildOf(#currentRoot.code) -->
<s:set var="isSelected" value="%{#currentRoot.code == #selectedTreeNode}" ></s:set>

<s:property value="#isHidden"/>

<tr id="${currentRoot.code}" data-parent="#${currentRoot.parent.code}" 
	class="treeRow <s:if test="%{#currentRoot.code != 'home' && #isHidden}">collapsed childrenNodes</s:if>" >
        <td class="treegrid-node pointer">
	        <input type="radio" name="${inputFieldName}"  
	        	id="fagianonode_${currentRoot.code}" 
	        	value="${currentRoot.code}" 
	            class="subTreeToggler <s:if test="#isSelected">active </s:if> hidden<s:if test="#currentRoot.children.length > 0">  tree_<s:property value="#currentRoot.code" /> </s:if>"
	        	<s:if test="#isSelected"> checked="checked"</s:if> />&#32;
        	<label for="fagianonode_${currentRoot.code}">
        		<span class="icon node-icon fa ${treeItemIconNameVar}"></span>
        		<s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
        		<s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;
                	<span class="text-muted icon fa fa-lock"></span>
                </s:if>
             </label>
        </td>
</tr>

<s:if test="%{#currentRoot.getChildren().length>0}">
    <s:iterator value="#currentRoot.children" var="node">
        <s:set var="currentRoot" value="#node" />
        <s:include value="treeBuilderCategories.jsp" />
    </s:iterator>
</s:if>

