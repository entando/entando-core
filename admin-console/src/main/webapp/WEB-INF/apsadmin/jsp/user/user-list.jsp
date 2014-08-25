<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<s:text name="title.userManagement" />
	</span>
</h1>
<s:form action="search" cssClass="form-horizontal" role="search">
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
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
			</ul>
		</div>
	</s:if>
	<div class="form-group">
		<div class="input-group col-sm-12 col-md-12">
			<span class="input-group-addon">
				<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="label.username"/>"></span>
			</span>
			<label for="search-username" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.username"/></label>
			<wpsf:textfield
				name="username"
				id="search-username"
				cssClass="form-control input-lg"
				title="%{getText('label.search.by')+' '+getText('label.username')}"
				placeholder="%{getText('label.username')}" />
			<div class="input-group-btn">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
					<span class="sr-only"><s:text name="label.search" /></span>
					<span class="icon fa fa-search" title="<s:text name="label.search" />"></span>
				</wpsf:submit>
				<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
					<span class="sr-only"><s:text name="title.searchFilters" /></span>
					<span class="caret"></span>
				</button>
			</div>
		</div>

		<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div id="search-advanced" class="collapse well collapse-input-group">
				<s:set var="searchableAttributesVar" value="searchableAttributes" />
				<s:set var="searchableAttributesPageScope" value="%{#searchableAttributesVar}" scope="page" />
				<div class="form-group">
					<label class="control-label col-sm-2 text-right">Users</label>
					<div class="btn-group col-lg-10" data-toggle="buttons">
						<label class="btn btn-default <s:if test="%{withProfile==null}"> active </s:if>">
							<wpsf:radio id="" name="withProfile" value="" checked="%{withProfile==null}" />
							&#32;<s:text name="label.userprofile.search.usersAllProfile" />
						</label>
						<label class="btn btn-default <s:if test="%{withProfile.toString().equalsIgnoreCase('1')}"> active </s:if>">
							<wpsf:radio id="" name="withProfile" value="1" checked="%{withProfile.toString().equalsIgnoreCase('1')}" />
							&#32;<s:text name="label.userprofile.search.usersWithProfile" />
						</label>
						<label class="btn btn-default <s:if test="%{withProfile.toString().equalsIgnoreCase('0')}"> active </s:if>">
							<wpsf:radio id="" name="withProfile" value="0" checked="%{withProfile.toString().equalsIgnoreCase('0')}"  />
							&#32;<s:text name="label.userprofile.search.usersWithoutProfile" />
						</label>
					</div>
				</div>
				<div class="form-group">
					<label for="userprofile_src_entityPrototypes" class="control-label col-sm-2 text-right"><s:text name="note.userprofile.search.profileType" /></label>
					<div class="col-sm-5 input-group">
						<wpsf:select id="userprofile_src_entityPrototypes" list="entityPrototypes" name="entityTypeCode" headerKey="" headerValue="%{getText('label.all')}" listKey="typeCode" listValue="typeDescr" cssClass="form-control" />
						<div class="input-group-btn">
							<wpsf:submit type="button" cssClass="btn btn-default" action="changeProfileType" value="set">
								<s:text name="label.set" />
							</wpsf:submit>
						</div>
					</div>
				</div>
				<c:if test="${empty searchableAttributesPageScope}">
					<div class="form-group">
						<div class="col-sm-offset-2 col-lg-offset-2 col-sm-10 col-lg-10 input-group">
							<span class="text-info">
								<s:text name="note.userprofile.searchAdvanced.chooseType" />
							</span>
						</div>
					</div>
				</c:if>
				<s:if test="null != #searchableAttributesVar && #searchableAttributesVar.size() > 0">
					<s:iterator value="#searchableAttributesVar" var="attribute">
						<%-- Text Attribute --%>
						<s:if test="#attribute.textAttribute">
							<s:set var="currentAttributeHtmlId" value="%{'userprofile_src_'+#attribute.name}" />
							<s:set var="textInputFieldName" value="%{#attribute.name+'_textFieldName'}" />
							<div class="form-group">
								<label class="control-label col-sm-2 text-right" for="<s:property value="#currentAttributeHtmlId" />">
									<s:property value="#attribute.name" />
								</label>
								<div class="col-sm-5">
									<wpsf:textfield
										id="%{#currentAttributeHtmlId}"
										name="%{#textInputFieldName}"
										value="%{getSearchFormFieldValue(#textInputFieldName)}"
										cssClass="form-control" />
								</div>
							</div>
						</s:if>
						<%-- Date Attribute --%>
						<s:elseif test="#attribute.type == 'Date'">
							<s:set var="currentAttributeHtmlId" value="%{'userprofile_src_'+#attribute.name}" />
							<s:set var="dateStartInputFieldName" value="%{#attribute.name+'_dateStartFieldName'}" />
							<s:set var="dateEndInputFieldName" value="%{#attribute.name+'_dateEndFieldName'}" />
							<div class="form-group">
								<label class="control-label col-sm-2 text-right"><s:property value="#attribute.name" /></label>
								<div class="col-lg-3">
									<label class="sr-only" for="<s:property value="%{#currentAttributeHtmlId+'_dateStartFieldName_cal'}" />">
										<s:property value="#attribute.name" />&#32;<s:text name="label.userprofile.from.date" />
									</label>
									<wpsf:textfield
										cssClass="form-control"
										placeholder="%{getText('label.userprofile.from.date')}"
										title="%{#attribute.name+' '+getText('label.userprofile.from.date')}"
										id="%{#currentAttributeHtmlId}_dateStartFieldName_cal"
										name="%{#dateStartInputFieldName}"
										value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
								</div>
								<div class="col-lg-3">
									<label class="sr-only" for="<s:property value="%{#currentAttributeHtmlId+'_dateEndFieldName_cal'}" />">
										<s:property value="#attribute.name" />&#32;<s:text name="label.userprofile.to.date" />
									</label>
									<wpsf:textfield
										cssClass="form-control"
										placeholder="%{getText('label.userprofile.to.date')}"
										title="%{#attribute.name+' '+getText('label.userprofile.to.date')}"
										id="%{#currentAttributeHtmlId}_dateEndFieldName_cal"
										name="%{#dateEndInputFieldName}"
										value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
								</div>
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<span class="help-block"><s:text name="label.userprofile.date.pattern" /></span>
								</div>
							</div>
						</s:elseif>
						<%-- Number Attribute --%>
						<s:elseif test="#attribute.type == 'Number'">
							<s:set var="currentAttributeHtmlId" value="%{'userprofile_src_'+#attribute.name}" />
							<s:set var="numberStartInputFieldName" value="%{#attribute.name+'_numberStartFieldName'}" />
							<s:set var="numberEndInputFieldName" value="%{#attribute.name+'_numberEndFieldName'}" />
							<div class="form-group">
								<label class="control-label col-sm-2 text-right"><s:property value="#attribute.name" /></label>
								<div class="col-lg-2">
									<label class="sr-only" for="<s:property value="%{#currentAttributeHtmlId+'_start'}" />"><s:property value="#attribute.name" />&#32;<s:text name="label.userprofile.from.value" /></label>
									<wpsf:textfield title="%{#attribute.name+' '+getText('label.userprofile.from.value')}" id="%{#currentAttributeHtmlId}_start" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" cssClass="form-control" placeholder="%{getText('label.userprofile.from.value')}" />
								</div>
								<div class="col-lg-2">
									<label class="sr-only" for="<s:property value="%{#currentAttributeHtmlId+'_end'}" />"><s:property value="#attribute.name" />&#32;<s:text name="label.userprofile.to.value" /></label>
									<wpsf:textfield title="%{#attribute.name+' '+getText('label.userprofile.to.value')}" id="%{#currentAttributeHtmlId}_end" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" cssClass="form-control" placeholder="%{getText('label.userprofile.to.value')}" />
								</div>
							</div>
						</s:elseif>
						<%-- Boolean & ThreeState --%>
						<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
							<s:set var="booleanInputFieldName" value="%{#attribute.name+'_booleanFieldName'}" />
							<s:set var="booleanInputFieldValue" value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
							<div class="form-group">
								<label class="control-label col-sm-2 text-right"><s:property value="#attribute.name" /></label>
								<div class="btn-group col-lg-10" data-toggle="buttons">
									<label class="btn btn-default <s:if test="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}"> active </s:if>">
										<wpsf:radio id="none_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="" checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" />
										&#32;<s:text name="label.bothYesAndNo"/>
									</label>
									<label class="btn btn-default <s:if test="%{#booleanInputFieldValue == 'true'}"> active </s:if>">
										<wpsf:radio id="true_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="true" checked="%{#booleanInputFieldValue == 'true'}" />
										&#32;<s:text name="label.yes"/>
									</label>
									<label class="btn btn-default <s:if test="%{#booleanInputFieldValue == 'false'}"> active </s:if>">
										<wpsf:radio id="false_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="false" checked="%{#booleanInputFieldValue == 'false'}" />
										&#32;<s:text name="label.no"/>
									</label>
								</div>
							</div>
						</s:elseif>
						<%-- unknown attribute --%>
						<s:else>
							<s:set var="currentAttributeHtmlId" value="%{'userprofile_src_'+#attribute.name}" />
							<s:set var="textInputFieldName" value="%{#attribute.name+'_textFieldName'}" />
							<div class="form-group">
								<label class="control-label col-sm-2 text-right" for="<s:property value="#currentAttributeHtmlId" />">
									<s:property value="#attribute.name" />
								</label>
								<div class="col-sm-5">
									<wpsf:textfield
										id="%{#currentAttributeHtmlId}"
										name="%{#textInputFieldName}"
										value="%{getSearchFormFieldValue(#textInputFieldName)}"
										cssClass="form-control" />
								</div>
							</div>
						</s:else>
					</s:iterator>
				</s:if>

				<%-- search by role --%>
				<s:set var="attributeRolesVar" value="attributeRoles" />
				<s:if test="null != #attributeRolesVar && #attributeRolesVar.size() > 0">
					<s:iterator var="attributeRoleVar" value="#attributeRolesVar">
						<s:set var="currentFieldIdVar">userFinding_<s:property value="#attributeRoleVar.name" /></s:set>
						<s:if test="%{#attributeRoleVar.formFieldType.toString().equals('TEXT')}">
							<div class="form-group">
								<div class="col-sm-10 col-md-offset-2">
									<label for="<s:property value="%{#currentFieldIdVar}" />">
										<span class="label label-default"><s:text name="name.role" /></span>&#32;
										<s:property value="#attributeRoleVar.name" />
									</label>
									<s:set name="textInputFieldName"><s:property value="#attributeRoleVar.name" />_textFieldName</s:set>
									<wpsf:textfield id="%{#currentFieldIdVar}" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" cssClass="form-control" />
								</div>
							</div>
						</s:if>
						<%-- COMMENTING OUT UNTIL WE ENCOUNTER SOME VALID USER STORY THAT MAKES US TEST IT
						<s:elseif test="%{#attributeRoleVar.formFieldType.toString().equals('DATE')}">
							<s:set name="dateStartInputFieldName"><s:property value="#attributeRoleVar.name" />_dateStartFieldName</s:set>
							<s:set name="dateEndInputFieldName"><s:property value="#attributeRoleVar.name" />_dateEndFieldName</s:set>
							<div class="form-group">
								<div class="col-sm-10 col-md-offset-2">
									<label for="<s:property value="%{#currentFieldIdVar}" />_dateStartFieldName"><s:property value="#attributeRoleVar.name" /> ** from date **</label>
									<wpsf:textfield id="%{#currentFieldIdVar}_dateStartFieldName" name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
									<span class="help-block">dd/MM/yyyy</span>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10 col-md-offset-2">
									<label for="<s:property value="%{#currentFieldIdVar}" />_dateEndFieldName"><s:property value="#attributeRoleVar.name" />** to date **</label>
									<wpsf:textfield id="%{#currentFieldIdVar}_dateEndFieldName" name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
									<span class="help-block">dd/MM/yyyy</span>
								</div>
							</div>
						</s:elseif>
						<s:elseif test="%{#attributeRoleVar.formFieldType.toString().equals('NUMBER')}">
							<s:set name="numberStartInputFieldName" ><s:property value="#attributeRoleVar.name" />_numberStartFieldName</s:set>
							<s:set name="numberEndInputFieldName" ><s:property value="#attributeRoleVar.name" />_numberEndFieldName</s:set>
							<p>
								<label for="<s:property value="%{#currentFieldIdVar}" />_start"><s:property value="#attributeRoleVar.name" /> ** from value **</label>
								<wpsf:textfield id="%{#currentFieldIdVar}_start" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
							</p>
							<p>
								<label for="<s:property value="%{#currentFieldIdVar}" />_end"><s:property value="#attributeRoleVar.name" /> ** to value **</label>
								<wpsf:textfield id="%{#currentFieldIdVar}_end" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
							</p>
						</s:elseif>
						<s:elseif test="%{#attributeRoleVar.formFieldType.toString().equals('BOOLEAN')}">
							<p>
								<span class="important"><s:property value="#attributeRoleVar.name" /></span>
							</p>
							<s:set name="booleanInputFieldName" ><s:property value="#attributeRoleVar.name" />_booleanFieldName</s:set>
							<s:set name="booleanInputFieldValue" ><s:property value="%{getSearchFormFieldValue(#booleanInputFieldName)}" /></s:set>
							<ul>
								<li><wpsf:radio id="none_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="" checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" /><label for="none_<s:property value="#booleanInputFieldName" />"><s:text name="label.bothYesAndNo"/></label></li>
								<li><wpsf:radio id="true_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="true" checked="%{#booleanInputFieldValue == 'true'}" /><label for="true_<s:property value="#booleanInputFieldName" />"><s:text name="label.yes"/></label></li>
								<li><wpsf:radio id="false_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="false" checked="%{#booleanInputFieldValue == 'false'}" /><label for="false_<s:property value="#booleanInputFieldName" />"><s:text name="label.no"/></label></li>
							</ul>
						</s:elseif>
						--%>
					</s:iterator>
				</s:if>
				<%-- //search by role --%>

				<%-- second search button --%>
				<div class="form-group">
					<div class="col-sm-5 col-sm-offset-2">
						<wpsf:submit type="button" cssClass="btn btn-primary">
							<span class="icon fa fa-search" /></span>
							&#32;
							<s:text name="label.search" />
						</wpsf:submit>
					</div>
				</div>
			</div>
		</div>
	</div>

	<a href="<s:url namespace="/do/User" action="new" />" class="btn btn-default">
		<span class="icon fa fa-plus-circle"></span>&#32;
		<s:text name="title.userManagement.userNew" />
	</a>

	<wpsa:subset source="searchResult" count="10" objectName="groupUserVar" advanced="true" offset="5">
		<s:set var="group" value="#groupUserVar" />
		<wp:ifauthorized permission="editUserProfile" var="hasEditProfilePermission" />
		<s:set var="hasEditProfilePermission" value="#attr.hasEditProfilePermission" />
		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>
		<div class="table-responsive">
			<table class="table table-bordered">
				<tr>
					<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
					<th><s:text name="label.username" /></th>
					<th><s:text name="label.email" /></th>
						<%-- hookpoint core.user-list.table.th --%>
						<wpsa:hookPoint key="core.user-list.table.th" objectName="hookPointElements_core_user_list_table_th">
							<s:iterator value="#hookPointElements_core_user_list_table_th" var="hookPointElement">
								<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
							</s:iterator>
						</wpsa:hookPoint>
					<th class="text-center col-xs-1 col-sm-1 col-md-1 col-lg-1"><abbr title="<s:text name="label.state" />">S</abbr></th>
				</tr>
				<s:iterator var="usernameVar">
					<s:set var="userVar" value="%{getUser(#usernameVar)}" />
					<s:set var="userProfileVar" value="%{getUserProfile(#usernameVar)}" />
					<s:url action="edit" var="editUserActionVar"><s:param name="username" value="#usernameVar"/></s:url>
					<s:url action="edit" namespace="/do/User/Auth" var="editUserAuthActionVar"><s:param name="username" value="#usernameVar"/></s:url>
					<s:url action="edit" namespace="/do/userprofile" var="editUserProfileActionVar"><s:param name="username" value="#usernameVar"/></s:url>
					<s:url action="view" namespace="/do/userprofile" var="viewUserProfileActionVar"><s:param name="username" value="#usernameVar"/></s:url>
					<s:url action="trash" var="userTrashActionVar"><s:param name="username" value="#usernameVar"/></s:url>
					<s:if test="null == #userVar || #userVar.disabled">
						<s:set var="statusIconClassVar" value="%{'icon fa fa-pause text-warning'}" />
						<s:set var="statusTextVar" value="%{getText('note.userStatus.notActive')}" />
					</s:if>
					<s:elseif test="!#userVar.entandoUser">
						<s:set var="statusIconClassVar" value="%{'icon fa fa-minus'}" />
						<s:set var="statusTextVar" value="%{getText('note.userStatus.notEntandoUser')}" />
					</s:elseif>
					<s:elseif test="!#userVar.accountNotExpired">
						<s:set var="statusIconClassVar" value="%{'icon fa fa-circle-o text-danger'}" />
						<s:set var="statusTextVar" value="%{getText('note.userStatus.expiredAccount')}" />
					</s:elseif>
					<s:elseif test="!#userVar.credentialsNotExpired">
						<s:set var="statusIconClassVar" value="%{'icon fa fa-adjust text-warning'}" />
						<s:set var="statusTextVar" value="%{getText('note.userStatus.expiredPassword')}" />
					</s:elseif>
					<s:elseif test="!#userVar.disabled">
						<s:set var="statusIconClassVar" value="%{'icon fa fa-check text-success'}" />
						<s:set var="statusTextVar" value="%{getText('note.userStatus.active')}" />
					</s:elseif>
					<tr>
						<td class="text-center text-nowrap">
							<div class="btn-group btn-group-xs">
								<%-- edit --%>
								<a class="btn btn-default" title="<s:text name="label.edit" />&#32;<s:property value="#usernameVar" />" href="<s:property value="#editUserActionVar" escapeHtml="false" />">
									<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#usernameVar" /></span>
									<span class="icon fa fa-pencil-square-o"></span>
								</a>
								<%-- dropdown button --%>
								<button type="submit" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu text-left"  role="menu">
									<li>
										<%-- edit auth --%>
										<a
											href="<s:property value="#editUserAuthActionVar" escapeHtml="false" />"
											title="<s:text name="note.configureAuthorizationsFor" />: <s:property value="#usernameVar" />"
											>
											<span class="fa-fw icon fa fa-unlock"></span>&#32;
											<s:text name="note.configureAuthorizationsFor" />: <s:property value="#usernameVar" />
										</a>
									</li>
									<s:if test="#hasEditProfilePermission">
										<li>
											<%-- edit profile --%>
											<a
												href="<s:property value="#editUserProfileActionVar" escapeHtml="false" />"
												title="<s:text name="label.editProfile" />: <s:property value="#usernameVar" />">
												<span class="fa-fw icon fa fa-user"></span>&#32;
												<s:text name="label.editProfile" />: <s:property value="#usernameVar" />
											</a>
										</li>
									</s:if>
									<s:if test="null != #userProfileVar">
										<li>
											<%-- view profile --%>
											<a
												href="<s:property value="#viewUserProfileActionVar" escapeHtml="false" />"
												title="<s:text name="label.viewProfile" />: <s:property value="#usernameVar" />">
												<span class="fa-fw icon fa fa-info"></span>&#32;
												<s:text name="label.viewProfile" />: <s:property value="#usernameVar" />
											</a>
										</li>
									</s:if>
								</ul>
							</div>
							<%-- remove --%>
							<div class="btn-group btn-group-xs">
								<a
									href="<s:property value="#userTrashActionVar" escapeHtml="false" />"
									title="<s:text name="label.remove" />: <s:property value="#usernameVar" />"
									class="btn btn-warning"
									>
									<span class="icon fa fa-times-circle-o"></span>&#32;
									<span class="sr-only"><s:text name="label.alt.clear" /></span>
								</a>
							</div>
						</td>
						<%-- username --%>
						<td><s:property value="#usernameVar" /></td>
						<%-- email --%>
						<td>
							<s:if test="null != #userProfileVar">
								<s:set var="mailVar" value="#userProfileVar.getValue(#userProfileVar.mailAttributeName)" />
								<s:if test="#mailVar.length()>25">
									<abbr title="<s:property value="#mailVar" />">
										<s:set var="chunks" value="%{#mailVar.split('@')}" />
										<s:property value="%{#chunks[0].length() > 8 ? #chunks[0].substring(0,8)+'...' : #chunks[0]}" />@<s:property value="%{#chunks[1].length() > 8 ? '...'+#chunks[1].substring(#chunks[1].length()-8) : #chunks[1]}" />
										<%-- <s:property value="%{#mailVar.substring(0,8) + '...' + #mailVar.substring(#mailVar.length()-8)}" /> --%>
									</abbr>
								</s:if>
								<s:else>
									<s:property value="#mailVar"/>
								</s:else>
							</s:if>
							<s:else>
								<abbr title="<s:text name="label.noProfile" />">&ndash;</abbr>
							</s:else>
						</td>
						<%-- td hookpoint --%>
						<wpsa:hookPoint key="core.user-list.table.td" objectName="hookPointElements_core_user_list_table_td">
							<s:iterator value="#hookPointElements_core_user_list_table_td" var="hookPointElement">
								<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
							</s:iterator>
						</wpsa:hookPoint>
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
</s:form>