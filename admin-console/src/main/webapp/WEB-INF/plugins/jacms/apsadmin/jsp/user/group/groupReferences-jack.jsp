<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%-- referenced contents --%>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="margin-none"><s:text name="title.group.referencedContents" /></h3>
		</div>
		<div class="panel-body">
			<s:if test="null != references['jacmsContentManagerUtilizers']">
				<wpsa:subset source="references['jacmsContentManagerUtilizers']" count="10" objectName="contentReferences" advanced="true" offset="5" pagerId="contentManagerReferences">
					<s:set name="group" value="#contentReferences" />
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					<div class="table-responsive">
						<table class="table table-bordered" id="contentListTable">
							<tr>
								<th><s:text name="label.description" /></th>
								<th><s:text name="label.code" /></th>
								<th><s:text name="label.type" /></th>
								<th><s:text name="label.lastEdit" /></th>
							</tr>
							<s:iterator var="currentContentIdVar">
								<jacmswpsa:content contentId="%{#currentContentIdVar}" var="currentContentRecordVar" record="true" />
								<jacmswpsa:content contentId="%{#currentContentRecordVar.id}" var="currentContentVar" authToEditVar="isAuthToEditVar" workVersion="true" />
								<tr>
									<td>
										<s:if test="#isAuthToEditVar">
											<a href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#currentContentVar.id" /></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="#currentContentVar.descr"/>"><s:property value="#currentContentVar.descr"/></a>
										</s:if>
										<s:else><s:property value="#currentContentVar.descr"/></s:else>
									</td>
									<td>
										<code><s:property value="#currentContentVar.id"/></code>
									</td>
									<td>
										<s:property value="#currentContentVar.typeDescr"/>
									</td>
									<td class="icon">
										<code><s:date name="#currentContentRecordVar.modify" format="dd/MM/yyyy" /></code>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					</wpsa:subset>
			</s:if>
			<s:else>
				<p class="margin-none"><s:text name="note.group.referencedContents.empty" /></p>
			</s:else>
		</div>
	</div>
<%-- referenced resources --%>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="margin-none"><s:text name="title.group.referencedResources" /></h3>
		</div>
		<div class="panel-body">
			<s:if test="null != references['jacmsResourceManagerUtilizers']">
				<wpsa:subset source="references['jacmsResourceManagerUtilizers']" count="10" objectName="resourceReferences" advanced="true" offset="5" pagerId="resourceManagerReferences">
					<s:set name="group" value="#resourceReferences" />
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					<table class="table table-bordered" id="resourceListTable">
						<tr>
							<th>
								<s:text name="label.description" />
							</th>
							<th>
								<s:text name="label.type" />
							</th>
						</tr>
						<s:iterator var="currentResourceIdVar" >
							<jacmswpsa:resource resourceId="%{#currentResourceIdVar}" var="currentResourceVar" />
							<tr>
								<s:set var="canEditCurrentResource" value="%{false}" />
								<c:set var="currentResourceGroup"><s:property value="#currentResourceVar.mainGroup" escape="false"/></c:set>
								<td>
									<wp:ifauthorized groupName="${currentResourceGroup}" permission="manageResources"><s:set var="canEditCurrentResource" value="%{true}" /></wp:ifauthorized>
									<s:if test="#canEditCurrentResource">
										<a href="<s:url action="edit" namespace="/do/jacms/Resource"><s:param name="resourceId" value="#currentResourceVar.id" /></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="#currentResourceVar.descr"/>"><s:property value="#currentResourceVar.descr"/></a>
									</s:if>
									<s:else><s:property value="#currentResourceVar.descr"/></s:else>
								</td>
								<td>
									<s:property value="#currentResourceVar.type"/>
								</td>
							</tr>
						</s:iterator>
					</table>
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
				</wpsa:subset>
			</s:if>
			<s:else>
				<p class="margin-none"><s:text name="note.group.referencedResources.empty" /></p>
			</s:else>
		</div>
	</div>