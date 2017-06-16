<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="%{#currentRoot.getChildren().length==0}">
    <s:set var="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
    <s:set var="treeItemIconNameVar" value="'fa-folder'" />
</s:else>

<s:if test="%{null == #actionName}">
    <s:set var="actionName" value="'joinCategory'"/>
</s:if>

<s:set var="isHidden" value="%{#selectedTreeNode == null || (#selectedTreeNode != #currentRoot.code)}" ></s:set>
<s:set var="isSelected" value="%{#currentRoot.code == #selectedTreeNode}" ></s:set>

<tr id="${currentRoot.code}" data-parent="#${currentRoot.parent.code}" 
    class="treeRow <s:if test="%{#currentRoot.code != 'home' && #isHidden}">collapsed childrenNodes</s:if> tree_node_flag ${liClassName}" >
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
	    <td class="text-center">
	        <wpsf:submit action="%{#actionName}" type="button" 
	           title="%{getText('label.join')}" cssClass="btn btn-sm btn-link js_joinCategory">
	           <span class="icon fa fa-plus"></span>
	        </wpsf:submit>
	    </td>
</tr>

<s:if test="%{#currentRoot.getChildren().length>0}">
    <s:iterator value="#currentRoot.children" var="node">
        <s:set var="currentRoot" value="#node" />
        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilderCategoriesJoin.jsp" />
    </s:iterator>
</s:if>