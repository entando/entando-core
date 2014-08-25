<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="hasActionErrors()">
	<div class="alert alert-danger alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
		<ul class="margin-base-top">
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
		<ul class="margin-base-top">
			<s:iterator value="fieldErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<div class="panel panel-default">
	<div class="panel-body">
		<h3 class="margin-none margin-small-bottom"><s:text name="title.userManagement.userList" /></h3>
		<s:form>
			<p class="sr-only">
				<wpsf:hidden name="username" />
				<wpsf:hidden name="authName" />
			</p>
			<s:set var="authorizedUsersVar" value="authorizedUsers" />
			<s:if test="%{#authorizedUsersVar.size() > 0}">
				<ul class="list-unstyled">
					<s:iterator var="user" value="#authorizedUsersVar">
						<li>
							<wpsa:actionParam action="removeUser" var="actionName" >
								<wpsa:actionSubParam name="usernameToSet" value="%{#user.username}" />
							</wpsa:actionParam>
							<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
							  <s:property value="#user" />&#32;
								<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') +' '+ #user}" cssClass="btn btn-default btn-xs badge">
									<span class="icon fa fa-times"></span>
									<span class="sr-only">x</span>
								</wpsf:submit>
							</span>
						</li>
					</s:iterator>
				</ul>
			</s:if>
			<s:else>
				<p class="text-warning">No users yet.</p><%--//TODO please add a label here --%>
			</s:else>
		</s:form>
	</div>
</div>
<div role="search">
	<s:form action="search" cssClass="margin-base-top form-horizontal">
		<h3 class="h4 margin-none  margin-base-bottom"><s:text name="title.userManagement.searchUsers" /></h3>
		<p class="sr-only">
			<wpsf:hidden name="authName" />
		</p>
		<div class="form-group">
			<div class="input-group col-sm-12">
				<label for="username" class="sr-only"><s:text name="title.userManagement.searchUsers" /></label>
				<span class="input-group-addon" title="<s:text name="title.userManagement.searchUsers" />">
					<span class="icon fa fa-file-text-o fa-lg"></span>
				</span>
				<wpsf:textfield name="text" id="username" cssClass="form-control input-lg" title="%{getText('title.userManagement.searchUsers')}" placeholder="%{getText('label.search.by')+ ' '+ getText('label.username')}" />
				<div class="input-group-btn">
					<wpsf:submit type="button" action="search" cssClass="btn btn-primary btn-lg" title="%{getText('title.userManagement.searchUsers')}">
							<span class="icon fa fa-search" title="<s:text name="label.search" />"></span>
					</wpsf:submit>
				</div>
			</div>
		</div>
	</s:form>

	<s:form action="search" cssClass="form-horizontal">
		<p class="sr-only">
			<wpsf:hidden name="username" />
			<wpsf:hidden name="authName" />
		</p>
		<div class="form-group">
			<div class="input-group col-sm-12">
					<wpsa:subset source="searchResult" count="10" objectName="groupUser" advanced="true" offset="5">
						<s:set name="group" value="#groupUser" />
						<div class="text-center">
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
						</div>
						<div class="table-responsive">
							<table class="table table-bordered">
								<tr>
									<th><s:text name="label.username" /></th>
									<th class="text-center"><s:text name="label.date.registration" /></th>
									<th class="text-center"><s:text name="label.date.lastLogin" /></th>
									<th class="text-center"><s:text name="label.date.lastPasswordChange" /></th>
									<th class="text-center"><abbr title="<s:text name="label.state" />">S</abbr></th>
								</tr>
								<s:iterator var="usernameVar">
									<s:set var="userVar" value="%{getUser(#usernameVar)}" />
									<s:if test="!#userVar.entandoUser">
										<s:set var="statusIconImagePath">fa-sitemap</s:set>
										<s:set var="statusIconText" value="%{getText('note.userStatus.notEntandoUser')}" />
									</s:if>
									<s:elseif test="#userVar.disabled">
										<s:set var="statusIconImagePath">fa-moon-o</s:set>
										<s:set var="statusIconText" value="%{getText('note.userStatus.notActive')}" />
									</s:elseif>
									<s:elseif test="!#userVar.accountNotExpired">
										<s:set var="statusIconImagePath">fa-power-off</s:set>
										<s:set var="statusIconText" value="%{getText('note.userStatus.expiredAccount')}" />
									</s:elseif>
									<s:elseif test="!#userVar.credentialsNotExpired">
										<s:set var="statusIconImagePath">fa-check-square-o</s:set>
										<s:set var="statusIconText" value="%{getText('note.userStatus.expiredPassword')}" />
									</s:elseif>
									<s:elseif test="!#userVar.disabled">
										<s:set var="statusIconImagePath">fa-check-square-o</s:set>
										<s:set var="statusIconText" value="%{getText('note.userStatus.active')}" />
									</s:elseif>
									<tr>
										<td class="text-nowrap">
											<s:set var="usernameAlreadyInVar" value="%{false}" />
											<s:iterator value="#authorizedUsersVar" var="u"><s:if test="%{#u.username == #usernameVar}"><s:set var="usernameAlreadyInVar" value="%{true}" /></s:if></s:iterator>
											<label>
												<s:if test="%{!#usernameAlreadyInVar}">
													<input type="radio" name="usernameToSet" value="<s:property value="#usernameVar"/>" />
												</s:if>
												<s:else><span class="icon fa fa-check-square-o text-muted"></span></s:else>
												&#32;
												<s:property value="#userVar" />
											</label>
										</td>
										<td class="text-center text-nowrap">
											<code>
												<s:if test="#userVar.entandoUser">
													<s:date name="#userVar.creationDate" format="dd/MM/yyyy" />
												</s:if>
												<s:else><abbr title="<s:text name="label.none" />">&ndash;</abbr></s:else>
											</code>
										</td>
										<td class="text-center text-nowrap">
											<code>
												<s:if test="#userVar.entandoUser && #userVar.lastAccess != null">
													<s:date name="#userVar.lastAccess" format="dd/MM/yyyy" />
												</s:if>
												<s:else><abbr title="<s:text name="label.none" />">&ndash;</abbr></s:else>
											</code>
										</td>
										<td class="text-center text-nowrap">
												<code>
													<s:if test="#userVar.entandoUser && #userVar.lastPasswordChange != null">
														<s:date name="#userVar.lastPasswordChange" format="dd/MM/yyyy" />
													</s:if>
													<s:else><abbr title="<s:text name="label.none" />">&ndash;</abbr></s:else>
												</code>
										</td>
										<td class="text-center col-xs-1 col-sm-1 col-md-1 col-lg-1">
											<span
												class="icon fa <s:property value="%{' '+#statusIconImagePath}" />"
												title="<s:property value="#statusIconText" />"
												>
													<span class="sr-only"><s:property value="#statusIconText" /></span>
											</span>
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
						<div class="text-center">
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
						</div>
					</wpsa:subset>
			</div>
		</div>
		<div class="form-group">
		  <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		    <wpsf:submit type="button" action="addUser" cssClass="btn btn-primary btn-block">
		      <span class="icon fa fa-plus"></span>&#32;
					<s:text name="label.add" />
		    </wpsf:submit>
		  </div>
		</div>
	</s:form>
</div>