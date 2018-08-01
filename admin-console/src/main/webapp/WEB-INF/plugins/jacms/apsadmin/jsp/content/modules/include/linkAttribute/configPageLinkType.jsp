<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="thirdTitleVar">
    <s:text name="title.configureLinkAttribute" />
</s:set>

<s:include value="linkAttributeConfigIntro.jsp"/>
<s:include value="linkAttributeConfigReminder.jsp" />

<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/linkAttribute/chooseLink-menu.jsp">
    <s:param name="linkTypeVar" value="2"/>
</s:include>

<s:form cssClass="form-horizontal">
    <p class="sr-only">
        <wpsf:hidden name="contentOnSessionMarker" />
        <s:if test="contentId == null">
            <wpsf:hidden name="linkType" value="2"/>
        </s:if>
        <s:else>
            <wpsf:hidden name="contentId"/>
            <wpsf:hidden name="linkType" value="4"/>
        </s:else>
        <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/></s:iterator>
        </p>

        <div class="panel panel-default no-top-border">
            <div class="panel-body">

                <div class="form-group mt-20">
                    <div class="col-xs-12">
                        <label class="col-sm-2 text-right">
                        <s:text name="note.choosePageToLink" />
                        <s:if test="contentId != null">&#32;<s:text name="note.choosePageToLink.forTheContent" />:
                            <s:property value="contentId"/> &ndash; <s:property value="%{getContentVo(contentId).descr}"/></s:if>
                        </label>

                        <div class="col-sm-10">
                        <s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
                        <s:if test="#pageTreeStyleVar == 'request'">
                            <p class="sr-only">
                                <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
                                    <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
                                </s:iterator>
                            </p>
                        </s:if>

                        <div class="table-responsive">
                            <table id="pageTree" class="table table-bordered table-hover table-treegrid">
                                <thead>
                                    <tr>
                                        <th>
                                            <s:text name="label.pageTree" />
                                            <s:if test="#pageTreeStyleVar == 'classic'">
                                                <button type="button" class="btn-no-button expand-button" id="expandAll">
                                                    <span class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;
                                                    <s:text name="label.expandAll" />
                                                </button>
                                                <button type="button" class="btn-no-button" id="collapseAll">
                                                    <span class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;
                                                    <s:text name="label.collapseAll" />
                                                </button>
                                            </s:if>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:set var="inputFieldName" value="'selectedNode'" />
                                    <s:set var="selectedTreeNode" value="selectedNode" />
                                    <s:set var="liClassName" value="'page'" />
                                    <s:set var="treeItemIconName" value="'fa-folder'" />

                                    <wpsa:groupsByPermission permission="managePages" var="groupsByPermission" />
                                    <s:if test="#pageTreeStyleVar == 'classic'">
                                        <s:set var="currentRoot" value="allowedTreeRootNode" />
                                        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
                                    </s:if>
                                    <s:elseif test="#pageTreeStyleVar == 'request'">
                                        <s:set var="treeNodeActionMarkerCode" value="treeNodeActionMarkerCode" />
                                        <s:set var="targetNode" value="%{parentPageCode}" />
                                        <s:set var="treeNodesToOpen" value="treeNodesToOpen" />
                                        <s:set var="currentRoot" value="showableTree" />
                                        <s:set var="contentOnSessionMarker" value="%{contentOnSessionMarker}" />
                                        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
                                    </s:elseif>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Link attributes -->
            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/entando-link-attributes.jsp" />


            <div class="form-group">
                <div class="col-xs-12">
                    <div class="col-sm-11 col-sm-offset-1 text-right">
                        <a href="${contentEntryURL}" title="<s:text name="label.cancel" />" class="btn btn-default mr-10">
                            <s:text name="label.cancel" />
                        </a>
                        <wpsf:submit type="button" action="joinPageLink" cssClass="btn btn-primary">
                            <s:text name="label.save" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </div>
    </div>
</s:form>

<jsp:include page="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/link-attributes-autocomplete.jsp" />
