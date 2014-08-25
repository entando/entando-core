<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:form cssClass="form-horizontal">
	<p class="sr-only">
		<wpsf:hidden name="name" />
	</p>
	<%-- referenced pages --%>
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="margin-none"><s:text name="title.group.referencedPages" /></h3></div>
			<div class="panel-body">
				<s:if test="null != references['PageManagerUtilizers']">
					<wpsa:subset source="references['PageManagerUtilizers']" count="10" objectName="pageReferences" advanced="true" offset="5" pagerId="pageManagerReferences">
						<s:set name="group" value="#pageReferences" />
						<div class="text-center">
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
						</div>
							<table class="table table-bordered" id="pageListTable">
								<tr>
									<!-- <th class="text-center text-nowrap col-xs-6 col-sm-3 col-md-3 col-lg-3"> -->
									<th class="text-center col-xs-5 col-sm-3 col-md-2 col-lg-2">
										<abbr title="<s:text name="label.actions" />">&ndash;</abbr>
									</th>
									<th><s:text name="label.page" /></th>
								</tr>
								<s:iterator var="currentPageVar">
									<s:set var="canEditCurrentPage" value="%{false}" />
									<s:set var="currentPageGroup" value="#currentPageVar.group" scope="page" />
									<wp:ifauthorized groupName="${currentPageGroup}" permission="managePages"><s:set var="canEditCurrentPage" value="%{true}" /></wp:ifauthorized>
									<tr>
										<td class="text-center text-nowrap"><s:if test="#canEditCurrentPage"><div class="btn-group btn-group-xs"><a
														class="btn btn-default" 
														href="<s:url namespace="/do/Page" action="viewTree"><s:param name="selectedNode" value="#currentPageVar.code" /></s:url>"
														title="<s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />"><span class="icon fa fa-folder"></span><span class="sr-only"><s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" /></span></a><a
														class="btn btn-default" 
														href="<s:url namespace="/do/Page" action="configure"><s:param name="pageCode" value="#currentPageVar.code" /></s:url>"
														title="<s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />"><span class="icon fa fa-cog"></span><span class="sr-only"><s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" /></span></a></div></s:if></td>
										<td>
											<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />
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
					<p class="margin-none"><s:text name="note.group.referencedPages.empty" /></p>
				</s:else>
			</div>
		</div>
	<%-- referenced users --%>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="margin-none"><s:text name="title.group.referencedUsers" /></h3>
			</div>
				<div class="panel-body">
					<s:if test="null != references['UserManagerUtilizers']">
						<wpsa:subset source="references['UserManagerUtilizers']" count="10" objectName="userReferences" advanced="true" offset="5" pagerId="userManagerReferences">
							<s:set name="group" value="#userReferences" />
							<div class="text-center">
								<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
								<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
							</div>
							<wp:ifauthorized permission="superuser" var="canEditUser" />
							<div class="table-responsive">
								<table class="table table-bordered" id="userListTable">
									<tr>
										<th class="text-center col-xs-5 col-sm-3 col-md-2 col-lg-2 <c:out value="${canEditUser ? '' : ' hide'}" />">
											<abbr title="<s:text name="label.actions" />">&ndash;</abbr>
										</th>
										<th><s:text name="label.username" /></th>
										<th class="text-center"><s:text name="label.date.lastLogin" /></th>
										<th class="text-center col-xs-1 col-sm-1 col-md-1 col-lg-1"><abbr title="<s:text name="label.state" />">S</abbr></th>
									</tr>
									<s:iterator var="usernameVar">
										<s:if test="null == #usernameVar || #usernameVar.disabled">
											<s:set var="statusIconClassVar" value="%{'icon fa fa-pause text-warning'}" />
											<s:set var="statusTextVar" value="%{getText('note.userStatus.notActive')}" />
										</s:if>
										<s:elseif test="!#usernameVar.entandoUser">
											<s:set var="statusIconClassVar" value="%{'icon fa fa-minus'}" />
											<s:set var="statusTextVar" value="%{getText('note.userStatus.notEntandoUser')}" />
										</s:elseif>
										<s:elseif test="!#usernameVar.accountNotExpired">
											<s:set var="statusIconClassVar" value="%{'icon fa fa-circle-o text-danger'}" />
											<s:set var="statusTextVar" value="%{getText('note.userStatus.expiredAccount')}" />
										</s:elseif>
										<s:elseif test="!#usernameVar.credentialsNotExpired">
											<s:set var="statusIconClassVar" value="%{'icon fa fa-adjust text-warning'}" />
											<s:set var="statusTextVar" value="%{getText('note.userStatus.expiredPassword')}" />
										</s:elseif>
										<s:elseif test="!#usernameVar.disabled">
											<s:set var="statusIconClassVar" value="%{'icon fa fa-check text-success'}" />
											<s:set var="statusTextVar" value="%{getText('note.userStatus.active')}" />
										</s:elseif>
										<tr>
											<%-- actions --%>
												<td class="text-center text-nowrap <c:out value="${canEditUser ? '' : ' hide'}" />">
													<div class="btn-group btn-group-xs">
														<c:if test="${canEditUser == 'true'}">
															<%-- edit user button --%>
																<a 
																	class="btn btn-default"
																	href="<s:url namespace="/do/User" action="edit"><s:param name="username" value="#usernameVar.username"/></s:url>" 
																	title="<s:text name="label.edit" />: <s:property value="#usernameVar.username" />" >
																		<span class="sr-only"><s:text name="label.edit" /></span>
																		<span class="icon fa fa-pencil-square-o"></span>
																</a>
															<%-- edit authorization button --%>
																	<a
																		class="btn btn-default"
																		href="<s:url action="edit" namespace="/do/User/Auth"><s:param name="username" value="#usernameVar"/></s:url>"
																		title="<s:text name="note.configureAuthorizationsFor" />: <s:property value="#usernameVar" />">
																			<span class="sr-only">
																				<s:text name="note.configureAuthorizationsFor" />: <s:property value="#usernameVar" />
																			</span>
																			<span class="icon fa fa-unlock fa-fw"></span>
																	</a>
														</c:if>
													</div>
												</td>
											<%-- username --%>
												<td><code><s:property value="#usernameVar.username" /></code>
											<%-- last login --%>
												<td class="text-center">
													<s:if test="#usernameVar.entandoUser && #usernameVar.lastAccess != null">
														<code title="<s:date name="#usernameVar.lastAccess" format="EEEE d MMMM yyyy" />">
															<s:date name="#usernameVar.lastAccess" format="dd/MM/yyyy" />
														</code>
													</s:if>
													<s:else>
														<span class="icon fa fa-minus text-muted" title="<s:text name="label.none" />"></span>
														<span class="sr-only"><s:text name="label.none" /></span>
													</s:else>
												</td>
											<%-- status --%>
												<td class="text-center">
													<span class="sr-only"><s:property value="#statusTextVar" /></span>
													<span class="<s:property value="#statusIconClassVar" />" title="<s:property value="#statusTextVar" />"></span>
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
						<p class="margin-none"><s:text name="note.group.referencedUsers.empty" /></p>
					</s:else>
				</div>
		</div>
	<%-- referenced widgets --%>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="margin-none"><s:text name="title.group.referencedWidgetTypes" /></h3>
			</div>
			<div class="panel-body">
				<s:if test="null != references['WidgetTypeManagerUtilizers']">
					<wpsa:subset source="references['WidgetTypeManagerUtilizers']" count="10" objectName="widgetTypeReferencesVar" advanced="true" offset="5" pagerId="widgetTypeReferences">
						<s:set name="group" value="#widgetTypeReferencesVar" />
						<div class="text-center">
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
						</div>
						<table class="table table-bordered" id="widgetTypeListTable">
							<tr>
								<th><s:text name="label.title" /></th>
								<th><s:text name="label.code" /></th>
							</tr>
							<s:iterator var="currentWidgetVar">
								<tr>
									<td><s:property value="%{getTitle(#currentWidgetVar.code, #currentWidgetVar.titles)}" /></td>
									<td><code><s:property value="#currentWidgetVar.code" /></code></td>
								</tr>
							</s:iterator>
						</table>
						<div class="text-center">
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
						</div>
					</wpsa:subset>
				</s:if>
				<s:else>
					<p class="margin-none"><s:text name="note.group.referencedWidgetTypes.empty" /></p>
				</s:else>
			</div>
		</div>
	<%-- hoookpoint core.groupReferences --%>
		<wpsa:hookPoint key="core.groupReferences" objectName="hookPointElements_core_groupReferences">
			<s:iterator value="#hookPointElements_core_groupReferences" var="hookPointElement">
				<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
			</s:iterator>
		</wpsa:hookPoint>
</s:form>
