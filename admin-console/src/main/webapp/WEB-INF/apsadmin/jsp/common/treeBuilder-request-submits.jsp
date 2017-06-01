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
<tr id="${currentRoot.code}" data-parent="#${currentRoot.parent.code}" class="treeRow tree_node_flag ${liClassName}" >
    <td class="treegrid-node">
<%-- <li class="<s:property value="#liClassName" /> tree_node_flag"> --%>

	<s:if test="null == #openTreeActionName"><s:set var="openTreeActionName" value="'openCloseTreeNode'" /></s:if>
	<s:if test="null == #closeTreeActionName"><s:set var="closeTreeActionName" value="'openCloseTreeNode'" /></s:if>

	<s:if test="!#currentRoot.open && !#currentRoot.empty">
		<wpsa:actionParam action="%{#openTreeActionName}" var="openTreeAction" >
			<wpsa:actionSubParam name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
			<wpsa:actionSubParam name="targetNode" value="%{#currentRoot.code}" />
			<wpsa:actionSubParam name="treeNodeActionMarkerCode" value="open" />
		</wpsa:actionParam>
		<wpsf:submit cssClass="btn btn-link btn-xs" action="%{#openTreeAction}" type="button" value="%{getText('label.open')}" title="%{getText('label.open')}">
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
		<wpsf:submit cssClass="btn btn-link btn-xs" action="%{#closeTreeAction}" type="button" value="%{getText('label.close')}" title="%{getText('label.close')}">
			<span class="icon fa fa-minus" title="<s:text name="label.close" />"></span>
			<span class="sr-only"><s:text name="label.close" /></span>
		</wpsf:submit>
	</s:elseif>
	<input
		type="radio"
		name="<s:property value="#inputFieldName" />"
		id="fagianonode_<s:property value="#currentRoot.code" />"
		value="<s:property value="#currentRoot.code" />"
		class="subTreeToggler ${(isSelected)?'active':''} hidden 
		<s:if test="#currentRoot.children.length > 0">  tree_<s:property value="#currentRoot.code" /> </s:if>"
		<s:if test="#currentRoot.code == #selectedTreeNode"> checked="checked" </s:if> />
	<label for="fagianonode_<s:property value="#currentRoot.code" />">
		<span class="icon node-icon fa ${treeItemIconNameVar}"></span>
		<s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
                <s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;<span class="text-muted icon fa fa-lock"></span></s:if>
	</label>
	<s:if test="#currentRoot.children.length > 0">
		<ul class="treeToggler fa-ul" id="tree_<s:property value="#currentRoot.code" />">
			<s:iterator value="#currentRoot.children" var="node">
				<s:set var="currentRoot" value="#node" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
			</s:iterator>
		</ul>
	</s:if>
</td>
</tr>
