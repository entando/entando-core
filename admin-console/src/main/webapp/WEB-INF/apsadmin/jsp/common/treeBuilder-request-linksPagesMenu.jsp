<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="#currentRoot.isEmpty()">
    <s:set var="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
    <s:set var="treeItemIconNameVar" value="#treeItemIconName" />
</s:else>
<tr id="<s:property value="#currentRoot.code" />" data-parent="#<s:property value="#currentRoot.parentCode" />" class="treeRow <s:if test="%{#currentRoot.code != 'homepage'}"></s:if>" >
        <td class="treegrid-node pointer word-wrap text-overflow">
        <s:if test="null == #openTreeActionName"><s:set var="openTreeActionName" value="'openCloseTreeNode'" /></s:if>
        <s:if test="null == #closeTreeActionName"><s:set var="closeTreeActionName" value="'openCloseTreeNode'" /></s:if>
        <s:if test="!#currentRoot.open && !#currentRoot.empty">
            <a class="treeOpenCloseJS"
               href="<s:url namespace="/do/Page/Console" action="%{#openTreeActionName}">
                   <wpsa:paramMap map="#treeNodeExtraParamsMap" />
                   <s:param name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
                   <s:param name="copyingPageCode" value="copyingPageCode" />
                   <s:param name="treeNodeActionMarkerCode" value="'open'" /><s:param name="targetNode" value="#currentRoot.code" /><s:param name="treeNodesToOpen" value="treeNodesToOpen" /></s:url>">
                <span class="treeOpenCloseJS icon fa fa-plus" title="<s:text name="label.open" />"></span>
                <span class="sr-only"><s:text name="label.open" /></span>
                <span class="icon node-icon fa <s:property value="#treeItemIconNameVar" />"></span>
                <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
            </a>
        </s:if>
        <s:elseif test="#currentRoot.open && !#currentRoot.empty">
            <a class="treeOpenCloseJS noborder" href="<s:url namespace="/do/Page/Console" action="%{#closeTreeActionName}">
                   <wpsa:paramMap map="#treeNodeExtraParamsMap" />
                   <s:param name="%{#treeNodeExtraParamName}" value="%{#treeNodeExtraParamValue}" />
                   <s:param name="copyingPageCode" value="copyingPageCode" />
                   <s:param name="treeNodeActionMarkerCode" value="'close'" /><s:param name="targetNode" value="#currentRoot.code" /><s:param name="treeNodesToOpen" value="treeNodesToOpen" /></s:url>">
                <span class="treeOpenCloseJS icon fa fa-minus" title="<s:text name="label.close" />"></span>
                <span class="sr-only"><s:text name="label.close" /></span>
                <span class="icon node-icon fa <s:property value="#treeItemIconNameVar" />"></span>
                <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
            </a>
        </s:elseif>
        <input
            type="radio"
            class="subTreeToggler hidden"
            name="<s:property value="#inputFieldName" />"
            id="fagianonode_<s:property value="#currentRoot.code" />"
            value="<s:property value="#currentRoot.code" />" <s:if test="#currentRoot.children.length > 0">class="subTreeToggler tree_<s:property value="#currentRoot.code" />" </s:if>
            <s:if test="#currentRoot.code == #selectedTreeNode"> checked="checked"</s:if> />
        <label for="fagianonode_<s:property value="#currentRoot.code" />">
            <s:if test="#currentRoot.empty">
                <span class="icon node-icon fa <s:property value="#treeItemIconNameVar" />"></span>
                <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
            </s:if>
            <s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;<span class="text-muted icon fa fa-lock"></span></s:if>
            </label>

        </td>
        <td class="moveButtons-right-container" style="width:40px">
            <div class="moveButtons hidden">
            <wpsf:submit action="moveUp" type="button" title="%{getText('page.options.moveUp')}" cssClass="btn btn-info" data-toggle="tooltip">
                <i class="fa fa-caret-up" aria-hidden="true"></i>
            </wpsf:submit>
            <wpsf:submit action="moveDown" type="button" title="%{getText('page.options.moveDown')}" cssClass="btn btn-info" data-toggle="tooltip">
                <i class="fa fa-caret-down" aria-hidden="true"></i>
            </wpsf:submit>
        </div>
    </td>
    <td class="table-w-20">
        <span class="statusField">
            <s:if test="%{!#currentRoot.getEntity().isOnline()}"><i class="fa fa-circle gray" aria-hidden="true" title="Draft"></i></s:if>
            <s:elseif test="%{#currentRoot.getEntity().isChanged()}"><i class="fa fa-circle yellow" aria-hidden="true" title="Online&#32;&ne;&#32;Draft"></i></s:elseif>
            <s:else><i class="fa fa-circle green" aria-hidden="true" title="Online"></i></s:else>
            </span>
        </td>
        <td class="text-center table-view-pf-actions">
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
                    <wpsf:submit action="edit" type="button" title="%{getText('page.options.modify')}" cssClass="btn btn-info" data-toggle="tooltip">
                        <span class="">Edit</span>
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
                    <wpsf:submit action="trash" type="button" title="%{getText('page.options.delete')}" cssClass="btn btn-warning" data-toggle="tooltip">
                        <span class="">Delete</span>
                    </wpsf:submit>
                </li>
                <s:if test="%{#currentRoot.getEntity().online}">
                    <li>
                        <wpsf:submit action="checkSetOffline" type="button" title="%{getText('page.options.offline')}" cssClass="btn btn-warning" data-toggle="tooltip">
                            <span class=""><s:text name="page.options.offline" /></span>
                        </wpsf:submit>
                    </li>
                </s:if>
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

<s:if test="#currentRoot.children.length > 0">
    <s:iterator value="#currentRoot.children" var="node">
        <s:set var="currentRoot" value="#node" />
        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-linksPagesMenu.jsp" />
    </s:iterator>
</s:if>
