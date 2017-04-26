<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<div>
    <div>
        <s:if test="null != references['jacmsContentManagerUtilizers']">
            <wpsa:subset source="references['jacmsContentManagerUtilizers']" count="10" objectName="contentReferences" advanced="true" offset="5" pagerId="contentManagerReferences">
                <s:set var="group" value="#contentReferences" />
                <div>
<%--                     <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /> --%>
<%--                     <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" /> --%>
					<p><s:text name="note.searchIntro" />&#32;<s:property value="#group.size" />&#32;<s:text name="note.searchOutro" /></p>
                </div>
                
                   	<ul class="nav nav-tabs">
					   <li class="active">
					        <a class="text-capitalize" data-toggle="tab" href="#contentListTable" title="Contents">
					            Contents
					        </a>
					    </li>
					    <li>
					        <a class="text-capitalize" data-toggle="tab" href="#resourceListTable" title="Resources">
					            Resources
					        </a>
					    </li>
				    </ul>
                <div class="tab-content">
                	<div id="contentListTable" class="tab-pane fade in active">
                		<table class="table table-striped table-bordered table-hover">
		                    <%-- <caption><span><s:text name="title.contentList" /></span></caption> --%>
		                    <thead>
			                    <tr>
			                        <th><s:text name="label.description" /></th>
				                    <th><s:text name="label.code" /></th>
				                    <th><s:text name="label.type" /></th>
				                    <th><s:text name="label.lastEdit" /></th>
			                    </tr>
		                    </thead>
		                    <tbody>
			                    <s:iterator var="currentContentIdVar">
			                        <jacmswpsa:content contentId="%{#currentContentIdVar}" var="currentContentRecordVar" record="true" />
			                        <tr>
			                        <jacmswpsa:content contentId="%{#currentContentRecordVar.id}" var="currentContentVar" authToEditVar="isAuthToEditVar" workVersion="true" />
			                        <td>
			                        <s:if test="#isAuthToEditVar">
			                            <a href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#currentContentVar.id" /></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="#currentContentVar.descr"/>"><s:property value="#currentContentVar.descr"/></a>
			                        </s:if>
			                        <s:else><s:property value="#currentContentVar.descr"/></s:else>
			                        <s:set var="isAuthToEditVar" value="%{false}" />
			                        </td>
			                        <td>
			                            <span class="monospace"><s:property value="#currentContentVar.id"/></span>
			                        </td>
			                        <td>
			                        <s:property value="#currentContentVar.typeDescr"/>
			                        </td>
			                        <td class="icon">
			                            <code title="<s:date name="#currentContentRecordVar.modify" format="EEEE dd MMMM yyyy" />"><s:date name="#currentContentRecordVar.modify" format="dd/MM/yyyy" /></code>
			                        </td>
			                        </tr>
			                    </s:iterator>
		                    </tbody>
		                </table>
                	</div>
                	
                	<div id="resourceListTable" class="tab-pane fade">
                		 <table class="table table-striped table-bordered table-hover">
		                    <%-- <caption><span><s:text name="title.resourceList" /></span></caption> --%>
		                    <thead>
			                    <tr>
			                        <th><s:text name="label.description" /></th>
				                    <th><s:text name="label.code" /></th>
				                    <th><s:text name="label.type" /></th>
			                    </tr>
		                    </thead>
		                    <tbody>
			                    <s:iterator var="currentResourceIdVar">
			                        <jacmswpsa:resource resourceId="%{#currentResourceIdVar}" var="currentResourceVar" />
			                        <tr>
			                        <s:set var="canEditCurrentResource" value="%{false}" />
			                        <s:set var="currentResourceGroup" value="#currentResourceVar.mainGroup" scope="page" />
			                        <td>
			                        <wp:ifauthorized groupName="${currentResourceGroup}" permission="manageResources"><s:set var="canEditCurrentResource" value="%{true}" /></wp:ifauthorized>
			                        <s:if test="#canEditCurrentResource">
			                            <a href="<s:url action="edit" namespace="/do/jacms/Resource"><s:param name="resourceId" value="#currentResourceVar.id" /></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="#currentResourceVar.descr"/>"><s:property value="#currentResourceVar.descr"/></a>
			                        </s:if>
			                        <s:else><s:property value="#currentResourceVar.descr"/></s:else>
			                        </td>
			                        <td><code><s:property value="#currentResourceVar.id"/></code></td>
			                        <td><s:property value="#currentResourceVar.type"/></td>
			                        </tr>
			                    </s:iterator>
		                    </tbody>
	              		</table>
                	</div>
                </div>
                
                
<!--                 <div class="text-center"> -->
<%--                     <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" /> --%>
<!--                 </div> -->
<%--             </wpsa:subset> --%>
<%--         </s:if> --%>
<%--         <s:else> --%>
<%--             <p class="margin-none"><s:text name="note.category.referencedContents.empty" /></p> --%>
<%--         </s:else> --%>
<!--     </div> -->
<!-- </div> -->
<!-- <div class="panel panel-default"> -->
<!--     <div class="panel-heading"> -->
<%--         <h3 class="margin-none"><s:text name="title.category.resourcesReferenced" /></h3> --%>
<!--     </div> -->
<!--     <div class="panel-body"> -->
<%--         <s:if test="null != references['jacmsResourceManagerUtilizers']"> --%>
<%--             <wpsa:subset source="references['jacmsResourceManagerUtilizers']" count="10" objectName="resourceReferences" advanced="true" offset="5" pagerId="resourceManagerReferences"> --%>
<%--                 <s:set var="group" value="#resourceReferences" /> --%>
<!--                 <div class="text-center"> -->
<%--                     <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /> --%>
<%--                     <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" /> --%>
<!--                 </div> -->
               
                <div class="text-center">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>
            </wpsa:subset>
        </s:if>
        <s:else>
            <p class="margin-none"><s:text name="note.category.referencedResources.empty" /></p>
        </s:else>
    </div>
</div>
