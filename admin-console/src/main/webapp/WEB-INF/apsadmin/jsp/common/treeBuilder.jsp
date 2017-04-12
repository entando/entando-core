<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>


<s:if test="%{#currentRoot.getChildren().length==0}">
    <s:set var="treeItemIconNameVar" value="'fa-folder-o'"/>
</s:if>
<s:else>
    <s:set var="treeItemIconNameVar" value="'fa-folder'"/>
</s:else>

<s:set var="isSelected" value="%{#currentRoot.code == #selectedTreeNode}" ></s:set>

<tr class="treeRow" id="<s:if test="#currentRoot.isRoot()">home</s:if><s:else><s:property value="#node.code"/></s:else>" data-parent='#<s:property value="#currentRoot.parent.code"/>'>
    <td class="treegrid-node">
        <%-- 		<span class="icon fa fa-li <s:property value="#treeItemIconNameVar" />"></span>&#32; --%>
        <input type="radio" name="<s:property value="#inputFieldName" />"
               id="fagianonode_<s:property value="#currentRoot.code" />"
               value="<s:property value="#currentRoot.code" />"
               <s:if test="#currentRoot.children.length > 0">class="subTreeToggler tree_<s:property value="#currentRoot.code" />"</s:if>
                <s:if test="#currentRoot.code == #selectedTreeNode"> checked="checked"</s:if> />&#32;
        <span class='class="icon fa <s:property value="treeItemIconNameVar"/>'></span> &#32;
        <label for="fagianonode_<s:property value="#currentRoot.code" />">
            <s:property value="getTitle(#currentRoot.code, #currentRoot.titles)"/>
            <s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;
                <span class="text-muted icon fa fa-lock"></span>
            </s:if>
        </label>
    </td>
    <td class="text-center">
        <div class="moveButtons<s:if test="!#isSelected" > hidden</s:if>">
            <wpsf:submit action="moveUp" type="button" title="%{getText('page.options.new')}" cssClass="btn-no-button"
                         data-toggle="tooltip">
                <i class="fa fa-plus" aria-hidden="true"></i>
            </wpsf:submit>
            <%--<wpsf:submit action="moveUp" type="button" title="%{getText('page.options.moveUp')}" cssClass="btn-no-button" data-toggle="tooltip">
                <i class="fa fa-caret-up" aria-hidden="true"></i>
            </wpsf:submit>
            <wpsf:submit action="moveDown" type="button" title="%{getText('page.options.moveDown')}" cssClass="btn-no-button" data-toggle="tooltip">
                <i class="fa fa-caret-down" aria-hidden="true"></i>
            </wpsf:submit>--%>
        </div>
    </td>
    <td class="text-center">
        <div class="dropdown  dropdown-kebab-pf">
            <button class="btn btn-link dropdown-toggle" type="button"
                    id="dropdownKebab" data-toggle="dropdown" aria-haspopup="true"
                    aria-expanded="true">
                <span class="fa fa-ellipsis-v"></span>
            </button>
            <ul class="dropdown-menu " aria-labelledby="dropdownKebab">
                <li>
                    <wpsf:submit type="button" action="detail"
                                 title="%{getText('category.options.detail')}"
                                 data-toggle="tooltip" cssClass="btn btn-info">
                        <span class="sr-only"><s:text name="category.options.detail"/></span>
                        <span> Dettagli </span>
                    </wpsf:submit>
                </li>
                <li>
                    <wpsf:submit type="button" action="new"
                                 title="%{getText('category.options.new')}" data-toggle="tooltip"
                                 cssClass="btn btn-info">
                        <span class="sr-only"><s:text name="category.options.new"/></span>
                        <span> New </span>
                    </wpsf:submit>
                </li>
                <li>
                    <wpsf:submit type="button" action="edit"
                                 title="%{getText('category.options.modify')}"
                                 data-toggle="tooltip" cssClass="btn btn-info">
								<span class="sr-only"><s:text
                                        name="category.options.modify"/></span>
                        <span> Edit </span>
                    </wpsf:submit>
                </li>
                <li>
                    <wpsf:submit type="button" action="trash"
                                 title="%{getText('category.options.delete')}"
                                 data-toggle="tooltip" cssClass="btn btn-warning">
								<span class="sr-only"><s:text
                                        name="category.options.delete"/></span>
                        <span> uccidi </span>
                    </wpsf:submit>
                </li>
            </ul>
        </div>
    </td>
</tr>
<s:if test="%{#currentRoot.getChildren().length>0}">
    <s:iterator status="iterator" value="#currentRoot.children" var="node">
        <s:set var="currentRoot" value="#node"/>
        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp"/>
    </s:iterator>
</s:if>
