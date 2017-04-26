<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="%{#currentRoot.getChildren().length==0}">
	<s:set var="treeItemIconNameVar" value="'fa-folder-o'"/>
</s:if>
<s:else>
	<s:set var="treeItemIconNameVar" value="'fa-folder'"/>
</s:else>

<s:set var="isHidden" value="%{#selectedPage == null || (#selectedPage.code != #currentRoot.code && !#selectedPage.isChildOf(#currentRoot.code))}" ></s:set>
<s:set var="isSelected" value="%{#currentRoot.code == #selectedTreeNode}" ></s:set>

<tr id="<s:if test="#currentRoot.isRoot()">home</s:if><s:else><s:property value="#node.code"/></s:else>" data-parent='#<s:property value="#currentRoot.parent.code"/>' class="treeRow pointer <s:if test="%{#currentRoot.code != 'home' && #isHidden}">collapsed childrenNodes</s:if>">
	<td class="treegrid-node">

		<input type="radio" name="<s:property value="#inputFieldName" />"
			   id="fagianonode_<s:property value="#currentRoot.code" />"
			   value="<s:property value="#currentRoot.code" />"
			   class="subTreeToggler hidden <s:if test="#isSelected">active</s:if> <s:if test="#currentRoot.children.length > 0">  tree_<s:property value="#currentRoot.code" /> </s:if>"
				<s:if test="#isSelected"> checked="checked"</s:if> />

		<span class='class="icon fa <s:property value="treeItemIconNameVar"/>'></span> &#32;
		<span for="fagianonode_<s:property value="#currentRoot.code" />">
            <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)"/>
            <s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;
				<span class="text-muted icon fa fa-lock"></span>
			</s:if>
        </span>
	</td>
	<td>
		<wpsf:submit action="joinCategory" type="button" title="%{getText('label.join')}" cssClass="btn btn-menu-right btn-sm margin-small-vertical" data-toggle="tooltip">
			<span class="icon fa fa-plus"></span>
		</wpsf:submit>
	</td>
</tr>
<s:if test="%{#currentRoot.getChildren().length>0}">
	<s:iterator status="iterator" value="#currentRoot.children" var="node">
		<s:set var="currentRoot" value="#node"/>
		<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilderCategories.jsp"/>
	</s:iterator>
</s:if>
