<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:form cssClass="form-horizontal" id="form_pageLink">

    <p class="sr-only"><s:text name="note.choosePageToLink" />.</p>
    <p class="sr-only">
        <input type="hidden" name="contentOnSessionMarker" value="<s:property value="contentOnSessionMarker" />" />
        <input type="hidden" name="linkTypeVar" value="2" />
    </p>

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
                     <table id="pageTree" class="table table-bordered table-hover table-treegrid no-mb">
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
                                 <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submit.jsp" />
                             </s:elseif>
                         </tbody>
                     </table>
                 </div>
             </div>
         </div>
     </div>
     <div class="form-group">
         <div class="col-xs-12">
             <div class="col-sm-10 col-sm-offset-2 text-right">
           <button type="submit" id="button_pageLink" name="button_pageLink" class="btn btn-primary">
               <s:text name="label.save" />
           </button>
             </div>
         </div>
     </div>
</s:form>
