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

    <tr id="<s:property value="#currentRoot.code"/>" data-parent='#<s:property value="#currentRoot.parentCode"/>' 
        class="treeRow <s:if test="%{!#currentRoot.isRoot() && #isHidden}">collapsed childrenNodes</s:if>">
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
    <s:if test="isPosition" >
        <td class="table-view-pf-actions text-center">
            <div class="dropdown dropdown-kebab-pf">
                <button class="btn btn-menu-right dropdown-toggle" type="button" id="dropdownKebabRight1"
                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                    <span class="fa fa-ellipsis-v"></span>
                </button>
                <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight1">
                    <li>
                        <wpsf:submit type="button" name="entandoaction:detail" value="Submit"
                                     title="%{getText('category.options.detail')}"
                                     data-toggle="tooltip" cssClass="btn-block">
                            <span> <s:text name="category.options.detail"/> </span>
                        </wpsf:submit>
                    </li>
                    <li>
                        <wpsf:submit type="button" name="entandoaction:new"
                                     title="%{getText('category.options.add')}"
                                     data-toggle="tooltip" cssClass="btn-block">
                            <span> <s:text name="category.options.add"/> </span>
                        </wpsf:submit>
                    </li>
                    <li>
                        <wpsf:submit type="button" name="entandoaction:edit" value="Submit"
                                     title="%{getText('category.options.modify')}"
                                     data-toggle="tooltip" cssClass="btn-block">
                            <span> <s:text name="category.options.modify"/> </span>
                        </wpsf:submit>
                    </li>
                    <s:if test="%{!#currentRoot.isRoot()}">
                        <li>
                            <wpsf:submit type="button" name="entandoaction:trash" value="Submit"
                                         title="%{getText('category.options.delete')}"
                                         data-toggle="tooltip" cssClass="btn-block">
                                <span> <s:text name="category.options.delete"/> </span>
                            </wpsf:submit>
                        </li>
                    </s:if>
                </ul>
            </div>
        </td>
    </s:if>
</tr>

<s:if test="%{#currentRoot.getChildren().length>0}">
    <s:iterator status="iterator" value="#currentRoot.children" var="node">
        <s:set var="currentRoot" value="#node"/>
        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp"/>
    </s:iterator>
</s:if>
