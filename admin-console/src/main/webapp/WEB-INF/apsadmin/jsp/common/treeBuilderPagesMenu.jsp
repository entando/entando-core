<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="%{#currentRoot.getChildren().length==0}">
    <s:set var="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
    <s:set var="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>

<s:set var="isHidden" value="%{#selectedPage == null || (#selectedPage.code != #currentRoot.code && !#selectedPage.isChildOf(#currentRoot.code))}" ></s:set>
<s:set var="isSelected" value="%{#currentRoot.code == #selectedTreeNode}" ></s:set>

<tr id="<s:property value="#currentRoot.code" />" data-parent="#<s:property value="#currentRoot.parent.code" />" class="treeRow <s:if test="%{#currentRoot.code != 'homepage' && #isHidden}">collapsed childrenNodes</s:if>" >
    <td class="treegrid-node pointer word-wrap text-overflow">
            <input type="radio" name="<s:property value="#inputFieldName" />" id="fagianonode_<s:property value="#currentRoot.code" />" value="<s:property value="#currentRoot.code" />" 
               class="subTreeToggler <s:if test="#isSelected">active </s:if> hidden <s:if test="#currentRoot.children.length > 0">  tree_<s:property value="#currentRoot.code" /> </s:if>"
               <s:if test="#isSelected"> checked="checked"</s:if> />
        &#32;<span class="word-wrap" for="fagianonode_<s:property value="#currentRoot.code" />"><span class="icon node-icon fa <s:property value="#treeItemIconNameVar" />"></span>
            <span class="tree-item-page-title" data-toggle="tooltip" 
                data-html="true" title="<s:property value="getTitle(#currentRoot.code, #currentRoot.titles)"/>">
                <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
            </span>
            <s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;
                <span class="text-muted icon fa fa-lock"></span>
            </s:if>
        </span>
    </td>
    <td class="moveButtons-right-container" style="width:40px">
        <div class="moveButtons-right<s:if test="!#isSelected" > hidden</s:if>">
            <wpsf:submit action="moveUp" type="button" title="%{getText('page.options.moveUp')}" cssClass="btn-no-button" data-toggle="tooltip">
                <i class="fa fa-caret-up" aria-hidden="true"></i>
            </wpsf:submit>
            <wpsf:submit action="moveDown" type="button" title="%{getText('page.options.moveDown')}" cssClass="btn-no-button" data-toggle="tooltip">
                <i class="fa fa-caret-down" aria-hidden="true"></i>
            </wpsf:submit>
        </div>
    </td>
    <td style="width:20px">
        <%-- FOR DEV. DEGUB
        ONLINE: <s:property value="%{#currentRoot.getEntity().isOnline()}"/>
        CHANGED: <s:property value="%{#currentRoot.getEntity().isChanged()}"/
        --%>
        <span class="statusField">
            <s:if test="%{!#currentRoot.getEntity().isOnline()}"><i class="fa fa-circle gray" aria-hidden="true" title="Draft"></i></s:if>
            <s:elseif test="%{#currentRoot.getEntity().isChanged()}"><i class="fa fa-circle yellow" aria-hidden="true" title="Online&#32;&ne;&#32;Draft"></i></s:elseif>
            <s:else><i class="fa fa-circle green" aria-hidden="true" title="Online"></i></s:else>
        </span>
    </td>

        <td class=" table-view-pf-actions hidden" style="width: 30px">
            <div class="dropdown dropdown-kebab-pf">
                <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                    <span class="fa fa-ellipsis-v"></span></button>
                <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight">
                    <li>
                        <wpsf:submit action="new" type="button" title="%{getText('page.options.new')}" cssClass="btn btn-info btn-kebab" data-toggle="tooltip">
                            <span><s:text name="title.addPage" /></span>
                        </wpsf:submit>
                    </li>
                    <li>
                    <wpsf:submit action="edit" type="button" title="%{getText('page.options.modify')}" cssClass="btn btn-info btn-kebab" data-toggle="tooltip">
                        <span class="">Edit</span>
                    </wpsf:submit>
                </li>
                <li><wpsf:submit action="doConfigure" type="button" title="%{getText('page.options.configure')}" cssClass="btn btn-info" data-toggle="tooltip">
                        <span class="">Configure</span>
                    </wpsf:submit>
                </li>
                <li><wpsf:submit action="detail" type="button" title="%{getText('page.options.detail')}" cssClass="btn btn-info" data-toggle="tooltip">
                        <span class="">Details</span>
                    </wpsf:submit>
                </li>
                <li><wpsf:submit action="copy" type="button" title="%{getText('page.options.copy')}" cssClass="btn btn-info" data-toggle="tooltip">
                        <span class="">Clone</span>
                    </wpsf:submit>
                </li>
                <s:if test="%{#currentRoot.getEntity().online}">
                <li>
                    <wpsf:submit action="checkSetOffline" type="button" title="%{getText('page.options.offline')}" cssClass="btn btn-warning" data-toggle="tooltip">
                        <span class=""><s:text name="page.options.offline" /></span>
                    </wpsf:submit>
                </li>
                </s:if>
                <s:else>
                <li>
                    <wpsf:submit action="trash" type="button" title="%{getText('page.options.delete')}" cssClass="btn btn-warning" data-toggle="tooltip">
                        <span class=""><s:text name="label.delete" /></span>
                    </wpsf:submit>
                </li>
                </s:else>
                <s:if test="%{!#currentRoot.getEntity().online || #currentRoot.getEntity().changed}">
                <li>
                    <wpsf:submit action="checkSetOnline" type="button" title="%{getText('page.options.online')}" cssClass="btn btn-warning" data-toggle="tooltip">
                        <span class=""><s:text name="page.options.online" /></span>
                    </wpsf:submit>
                </li>
                </s:if>
            </ul>
        </div>  
    </td>
</tr>

<s:if test="%{#currentRoot.getChildren().length>0}">
    <s:iterator value="#currentRoot.children" var="node">
        <s:set var="currentRoot" value="#node" />
        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilderPagesMenu.jsp" />
    </s:iterator>
</s:if>
