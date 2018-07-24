<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set var="prevCode">
    <%= request.getParameter("prevCode") %>
</s:set>

<c:set var="prevLinkTypeVar">
    <%= request.getParameter("prevLinkTypeVar") %>
</c:set>

<c:set var="linkTypeVar">
    <%= request.getParameter("linkTypeVar") %>
</c:set>

  <c:if test="${prevLinkTypeVar!=null && prevLinkTypeVar!=''}">
         <div class="form-group mt-20">
             <div class="col-xs-12">
                 <div class="alert alert-info mt-20 no-mb">
                     <span class="pficon pficon-info"></span>
                         <p>
                             <c:if test="${prevLinkTypeVar == 1}">
                                <s:text name="note.previousLinkSettings" /> <s:text name="note.URLLinkTo" />
                             </c:if>

                             <c:if test="${prevLinkTypeVar == 2}">
                                 <c:if test="${linkTypeVar == 2}">
                                    <s:set var="prevPageVar" value="%{getPage(#prevCode)}" />
                                    <s:text name="note.previousLinkSettings" />&nbsp;<s:text name="note.pageLinkTo" />:&nbsp;
                                    <s:property value="#prevPageVar.code"/> - <s:property value="%{#prevPageVar.getTitle(currentLang.getCode())}"/>
                                 </c:if>
                                 <c:if test="${linkTypeVar != 2}">
                                    <s:text name="note.previousLinkSettings" />:&nbsp;<s:text name="note.pageLinkTo" />
                                 </c:if>
                             </c:if>

                             <c:if test="${prevLinkTypeVar == 3}">
                                 <c:if test="${linkTypeVar == 3}">
                                    <s:set var="prevContentVar" value="%{getContentVo(#prevCode)}" />
                                    <s:text name="note.previousLinkSettings" />&nbsp;<s:text name="note.contentLinkTo" />:&nbsp;
                                    <s:property value="#prevContentVar.id"/> - <s:property value="#prevContentVar.descr"/>
                                 </c:if>
                                 <c:if test="${linkTypeVar != 3}">
                                    <s:text name="note.previousLinkSettings" />:&nbsp;
                                    <s:text name="note.contentLinkTo" />
                                  </c:if>
                             </c:if>

                             <c:if test="${prevLinkTypeVar == 5}">
                                 <c:if test="${linkTypeVar == 5}">
                                    <s:set var="prevResourceVar" value="%{getResource(#prevCode)}" />
                                    <s:text name="note.previousLinkSettings" />&nbsp;<s:text name="note.resourceLinkTo" />:&nbsp;
                                    <s:property value="#prevResourceVar.id"/> - <s:property value="#prevResourceVar.descr"/>
                                 </c:if>
                                 <c:if test="${linkTypeVar != 5}">
                                    <s:text name="note.previousLinkSettings" />:&nbsp;
                                    <s:text name="note.resourceLinkTo" />
                                  </c:if>
                             </c:if>
                         </p>
                     </span>
                 </div>
             </div>
         </div>
     </c:if>
