<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="%{#currentRoot.getChildren().length==0}">
    <s:set name="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
    <s:set name="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>

<tr id="<s:property value="#currentRoot.code" />" data-parent="#<s:property value="#currentRoot.parent.code" />" class="treeRow <s:if test="%{#currentRoot.code != 'homepage'}">collapsed childrenNodes</s:if>" >
        <td class="treegrid-node pointer">
            <input type="radio" name="<s:property value="#inputFieldName" />" id="fagianonode_<s:property value="#currentRoot.code" />" value="<s:property value="#currentRoot.code" />" 
               class="subTreeToggler hidden <s:if test="#currentRoot.children.length > 0">  tree_<s:property value="#currentRoot.code" /> </s:if>"
               <s:if test="#currentRoot.code == #selectedTreeNode"> checked="checked"</s:if> />
        &#32;<label for="fagianonode_<s:property value="#currentRoot.code" />"><span class="icon node-icon fa <s:property value="#treeItemIconNameVar" />"></span><s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" /><s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;
                <span class="text-muted icon fa fa-lock"></span></s:if></label>
        </td>
        <td class="moveButtons-right-container">
            <div class="moveButtons-right hidden">
            <wpsf:submit action="new" type="button" title="%{getText('page.options.new')}" cssClass="btn-no-button" data-toggle="tooltip">
                <i class="fa fa-plus" aria-hidden="true"></i>
            </wpsf:submit>
            <wpsf:submit action="moveUp" type="button" title="%{getText('page.options.moveUp')}" cssClass="btn-no-button" data-toggle="tooltip">
                <i class="fa fa-caret-up" aria-hidden="true"></i>
            </wpsf:submit>
            <wpsf:submit action="moveDown" type="button" title="%{getText('page.options.moveDown')}" cssClass="btn-no-button" data-toggle="tooltip">
                <i class="fa fa-caret-down" aria-hidden="true"></i>
            </wpsf:submit>
        </div>
    </td>
    <td class="text-center">
        <%-- FOR DEV. DEGUB
        ONLINE: <s:property value="%{#currentRoot.getEntity().isOnline()}"/>
        CHANGED: <s:property value="%{#currentRoot.getEntity().isChanged()}"/
        --%>
        <span class="statusField">
            <s:if test="%{#currentRoot.getEntity().isOnline()}">Online <!--<i class="fa fa-check-circle-o green" aria-hidden="true"></i>--></s:if>
            <s:if test="%{#currentRoot.getEntity().isOnline() && #currentRoot.getEntity().isChanged()}">&#32;&ne;&#32;Draft</s:if>
            <s:if test="%{!#currentRoot.getEntity().isOnline() && !#currentRoot.getEntity().isChanged()}">Draft</s:if>
            </span>

        </td>

        <td class=" table-view-pf-actions hidden">
            <div class="dropdown dropdown-kebab-pf">
                <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                    <span class="fa fa-ellipsis-v"></span></button>
                <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight">
                    <li>
                    <wpsf:submit action="edit" type="button" title="%{getText('page.options.modify')}" cssClass="btn btn-info btn-kebab" data-toggle="tooltip">
                        <span class="">Edit </span>
                    </wpsf:submit>
                </li>
                <li><wpsf:submit action="configure" type="button" title="%{getText('page.options.configure')}" cssClass="btn btn-info" data-toggle="tooltip">
                        <span class="">Configure</span>
                    </wpsf:submit>
                </li>
                <li><wpsf:submit action="detail" type="button" title="%{getText('page.options.detail')}" cssClass="btn btn-info" data-toggle="tooltip">
                        <span class="">Detail</span>
                    </wpsf:submit>
                </li>
                <li><wpsf:submit action="copy" type="button" title="%{getText('page.options.copy')}" cssClass="btn btn-info" data-toggle="tooltip">
                        <span class="">Clone</span>
                    </wpsf:submit>
                </li>
                <li>
                    <wpsf:submit action="paste" type="button" title="%{getText('page.options.paste')}" cssClass="btn btn-info" data-toggle="tooltip">
                        <span class="">Paste</span>
                    </wpsf:submit>
                </li>
                <li>
                    <wpsf:submit action="trash" type="button" title="%{getText('page.options.delete')}" cssClass="btn btn-warning" data-toggle="tooltip">
                        <span class="">Delete</span>
                    </wpsf:submit>
                </li>
            </ul>
        </div>  
    </td>
</tr>

<s:if test="%{#currentRoot.getChildren().length>0}">
    <s:iterator value="#currentRoot.children" var="node">
        <s:set name="currentRoot" value="#node" />
        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilderPagesMenu.jsp" />
    </s:iterator>
</s:if>
