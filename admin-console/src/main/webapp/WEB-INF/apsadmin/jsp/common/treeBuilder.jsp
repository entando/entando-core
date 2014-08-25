<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<s:if test="%{#currentRoot.getChildren().length==0}">
	<s:set name="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
	<s:set name="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>

<li class="<s:property value="#liClassName" /> tree_node_flag"><span class="icon fa fa-li <s:property value="#treeItemIconNameVar" />"></span>&#32;<input type="radio" name="<s:property value="#inputFieldName" />" id="fagianonode_<s:property value="#currentRoot.code" />" value="<s:property value="#currentRoot.code" />" <s:if test="#currentRoot.children.length > 0">class="subTreeToggler tree_<s:property value="#currentRoot.code" />" </s:if> <s:if test="#currentRoot.code == #selectedTreeNode"> checked="checked"</s:if> />&#32;<label for="fagianonode_<s:property value="#currentRoot.code" />"><s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" /></label>
<s:if test="%{#currentRoot.getChildren().length>0}">
	<ul class="treeToggler fa-ul" id="tree_<s:property value="#currentRoot.code" />">
		<s:iterator value="#currentRoot.children" var="node">
			<s:set name="currentRoot" value="#node" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
		</s:iterator>
	</ul>
</s:if>
</li>