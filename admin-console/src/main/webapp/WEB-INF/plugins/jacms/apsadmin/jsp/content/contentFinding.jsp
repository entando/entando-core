<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%-- radios + checkboxes only --%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />
<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="jacms.menu.contentAdmin" />&#32;/&#32;<s:text name="title.contentList" /></span></h1>

<div id="main" role="main">

		<s:url action="search" var= "formAction" namespace="do/jacms/Content" />
		<s:form action="%{'/' + #formAction}" cssClass="form-horizontal" role="search">

			<p class="sr-only">
				<wpsf:hidden name="lastGroupBy" />
				<wpsf:hidden name="lastOrder" />
			</p>

			<div class="form-group">
				<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<span class="input-group-addon">
						<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="label.description"/>"></span>
					</span>
					<label for="text" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.description"/></label>
					<wpsf:textfield name="text" id="text" cssClass="form-control input-lg" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')} %{getText('label.description')}" />
					<span class="input-group-btn">
						<wpsa:actionParam action="search" var="searchActionName" >
							<wpsa:actionSubParam name="actionCode" value="search" />
						</wpsa:actionParam>
						<wpsf:submit action="%{#searchActionName}" type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
							<span class="sr-only"><s:text name="label.search" /></span>
							<span class="icon fa fa-search"></span>
						</wpsf:submit>
						<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
								<span class="sr-only"><s:text name="title.searchFilters" /></span>
								<span class="caret"></span>
						</button>
					</span>
				</div>
				<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<div id="search-advanced" class="collapse well collapse-input-group">
						<div class="form-group">
							<label for="contentType" class="control-label col-sm-2 text-right">
								<s:text name="label.type"/>
							</label>
							<div class="col-sm-5 input-group">
								<wpsf:select cssClass="form-control" name="contentType" id="contentType"
									list="contentTypes" listKey="code" listValue="descr"
									headerKey="" headerValue="%{getText('label.all')}" />
								<div class="input-group-btn">
									<wpsa:actionParam action="changeContentType" var="changeContentTypeActionName" >
										<wpsa:actionSubParam name="actionCode" value="changeContentType" />
									</wpsa:actionParam>
									<wpsf:submit action="%{#changeContentTypeActionName}" cssClass="btn btn-default" value="%{getText('label.set')}" />
								</div>
							</div>
						</div>
						<s:set var="searchableAttributes" value="searchableAttributes" />
						<s:if test="null != #searchableAttributes && #searchableAttributes.size() > 0">
							<%-- restore when we can dimiss it with a timeout
							<div class="alert alert-info alert-dismissable fade in">
								<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
								<p>Content type successfully set. TODO label.</p>
							</div>
							--%>
							<s:iterator var="attribute" value="#searchableAttributes">
								<s:set var="currentFieldId" value="%{'entityFinding_'+#attribute.name}" />
								<s:if test="#attribute.textAttribute">
									<div class="form-group">
										<s:set var="textInputFieldName"><s:property value="#attribute.name" />_textFieldName</s:set>
										<label for="<s:property value="currentFieldId" />" class="control-label col-sm-3 text-right"><s:property value="#attribute.name" /></label>
										<div class="col-sm-4">
											<wpsf:textfield id="%{currentFieldId}" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" cssClass="form-control" />
										</div>
									</div>
								</s:if>
								<s:elseif test="#attribute.type == 'Date'">
									<s:set var="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
									<s:set var="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
									<div class="form-group">
										<label for="<s:property value="%{currentFieldId}" />_dateStartFieldName_cal" class="control-label col-sm-5 text-right"><s:text name="note.range.from.attribute" />&#32;<s:property value="#attribute.name" /></label>
										<div class="col-sm-2">
											<wpsf:textfield id="%{currentFieldId}_dateStartFieldName_cal" name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" cssClass="form-control datepicker" placeholder="dd/mm/yyyy" />
										</div>
									</div>
									<div class="form-group">
										<label for="<s:property value="%{currentFieldId}" />_dateEndFieldName_cal" class="control-label col-sm-5 text-right"><s:text name="note.range.to.attribute" />&#32;<s:property value="#attribute.name" /></label>
										<div class="col-sm-2">
											<wpsf:textfield id="%{currentFieldId}_dateEndFieldName_cal" name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" cssClass="form-control datepicker" placeholder="dd/mm/yyyy" />
										</div>
									</div>
								</s:elseif>
								<s:elseif test="#attribute.type == 'Number'">
									<s:set var="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
									<s:set var="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
									<p>
										<label for="<s:property value="currentFieldId" />_start"><s:text name="note.range.from.attribute" />&#32;<s:property value="#attribute.name" />:</label>
										<wpsf:textfield id="%{currentFieldId}_start" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
									</p>
									<p>
										<label for="<s:property value="currentFieldId" />_end"><s:text name="note.range.to.attribute" />&#32;<s:property value="#attribute.name" />:</label>
										<wpsf:textfield id="%{currentFieldId}_end" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
									</p>
								</s:elseif>
								<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
									<p>
										<span class="important"><s:property value="#attribute.name" /></span><br />
									</p>
									<s:set var="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
									<s:set var="booleanInputFieldValue" ><s:property value="%{getSearchFormFieldValue(#booleanInputFieldName)}" /></s:set>
									<ul class="noBullet radiocheck">
										<li><wpsf:radio id="none_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="" checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" /><label for="none_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.bothYesAndNo"/></label></li>
										<li><wpsf:radio id="true_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="true" checked="%{#booleanInputFieldValue == 'true'}" /><label for="true_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.yes"/></label></li>
										<li><wpsf:radio id="false_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="false" checked="%{#booleanInputFieldValue == 'false'}" /><label for="false_<s:property value="#booleanInputFieldName" />" class="normal"><s:text name="label.no"/></label></li>
									</ul>
								</s:elseif>
							</s:iterator>
						</s:if>
						<div class="form-group">
							<label for="contentType" class="control-label col-sm-2 text-right">
								<s:text name="label.category" />
							</label>
							<div class="col-sm-5">
								<s:action name="showCategoryTreeOnContentFinding" namespace="/do/jacms/Content" ignoreContextParams="true" executeResult="true"></s:action>
							</div>
						</div>
						<div class="form-group">
							<label for="contentIdToken" class="control-label col-sm-2 text-right"><s:text name="label.code"/></label>
							<div class="col-sm-5">
								<wpsf:textfield name="contentIdToken" id="contentIdToken" cssClass="form-control" placeholder="CNG12" />
							</div>
						</div>
						<s:set var="allowedGroupsVar" value="allowedGroups" />
						<s:if test="null != #allowedGroupsVar && #allowedGroupsVar.size()>1">
							<div class="form-group">
								<label for="ownerGroupName" class="control-label col-sm-2 text-right"><s:text name="label.group" /></label>
								<div class="col-sm-5">
									<wpsf:select name="ownerGroupName" id="ownerGroupName" list="#allowedGroupsVar" headerKey="" headerValue="%{getText('label.all')}" listKey="name" listValue="descr" cssClass="form-control" />
								</div>
							</div>
						</s:if>
						<div class="form-group">
							<label for="state" class="control-label col-sm-2 text-right"><s:text name="label.state"/></label>
							<div class="col-sm-5">
								<wpsf:select name="state" id="state" list="avalaibleStatus" headerKey="" headerValue="%{getText('label.all')}" listKey="key" listValue="%{getText(value)}" cssClass="form-control" />
							</div>
						</div>
						<div class="form-group">
							<div class="btn-group col-sm-5 col-sm-offset-2" data-toggle="buttons">
								<label class="btn btn-default <s:if test="('yes' == onLineState)"> active </s:if>" >
									<input type="radio" name="onLineState" id="approved" <s:if test="('yes' == onLineState)">checked="checked"</s:if> value="yes" />&#32;
									<s:text name="name.isApprovedContent"/>
								</label>
								<label class="btn btn-default <s:if test="('no' == onLineState)"> active </s:if>">
									<input type="radio" name="onLineState" id="notApproved" <s:if test="('no' == onLineState)">checked="checked"</s:if> value="no" />&#32;
									<s:text name="name.isNotApprovedContent"/>
								</label>
								<label class="btn btn-default <s:if test="('yes' != onLineState) && ('no' != onLineState)"> active </s:if>">
									<input type="radio" name="onLineState" id="bothApproved" <s:if test="('yes' != onLineState) && ('no' != onLineState)">checked="checked"</s:if> value="" />&#32;
									<s:text name="name.isApprovedOrNotContent" />
								</label>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-5 col-sm-offset-2">
								<wpsf:submit action="%{#searchActionName}" type="button" cssClass="btn btn-primary">
									<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
								</wpsf:submit>
							</div>
						</div>
					</div><%--// search-advanced --%>
				</div>
			</div>


			<hr />

			<p class="help-block text-right">
				<button type="button" data-toggle="collapse" data-target="#search-configure-results" class="btn btn-link">
					<s:text name="title.searchResultOptions" />&#32;<span class="icon-chevron-down"></span>
				</button>
			</p>

			<div id="search-configure-results" class="collapse">

				<div class="form-group col-sm-12">
					<div class="btn-group" data-toggle="buttons">
						<label class="btn btn-default" for="viewCode">
							<wpsf:checkbox name="viewCode" id="viewCode" />&#32;
							<s:text name="label.code" />
						</label>
						<label class="btn btn-default">
							<wpsf:checkbox name="viewTypeDescr" id="viewTypeDescr" />&#32;
							<s:text name="name.contentType" />
						</label>
						<label class="btn btn-default">
							<wpsf:checkbox name="viewStatus" id="viewStatus" />&#32;
							<s:text name="name.contentStatus" />
						</label>
						<label class="btn btn-default">
							<wpsf:checkbox name="viewGroup" id="viewGroup" />&#32;
							<s:text name="label.group"/>
						</label>
						<label class="btn btn-default">
							<wpsf:checkbox name="viewCreationDate" id="viewCreationDate" />&#32;
							<s:text name="label.creationDate"/>
						</label>
					</div>
				</div>

				<div class="form-group col-sm-12">
					<wpsf:submit action="%{#searchActionName}" type="button" cssClass="btn btn-primary">
							<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
					</wpsf:submit>
				</div>

			</div>

		</s:form>

		<hr />

	<s:form action="search" >
		<p class="sr-only">
			<wpsf:hidden name="text" />
			<wpsf:hidden name="contentType" />
			<wpsf:hidden name="state" />
			<wpsf:hidden name="onLineState" />
			<wpsf:hidden name="categoryCode" />
			<wpsf:hidden name="viewTypeDescr" />
			<wpsf:hidden name="viewGroup" />
			<wpsf:hidden name="viewCode" />
			<wpsf:hidden name="viewStatus" />
			<wpsf:hidden name="viewCreationDate" />
			<wpsf:hidden name="lastGroupBy" />
			<wpsf:hidden name="lastOrder" />
			<wpsf:hidden name="contentIdToken" />
			<wpsf:hidden name="ownerGroupName" />
			<s:iterator var="attribute" value="#searchableAttributes">
				<s:if test="#attribute.textAttribute">
					<s:set var="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
					<wpsf:hidden name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
				</s:if>
				<s:elseif test="#attribute.type == 'Date'">
					<s:set var="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
					<s:set var="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
					<wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
					<wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Number'">
					<s:set var="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
					<s:set var="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
					<wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
					<wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
				</s:elseif>
				<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
					<s:set var="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
					<wpsf:hidden name="%{#booleanInputFieldName}" value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
				</s:elseif>
			</s:iterator>
		</p>

		<%-- New Content Button--%>

		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<span class="icon fa fa-plus-circle"></span>
				<s:text name="label.newContent" />&#32;
				<span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" role="menu">
			<s:iterator var="contentTypeVar" value="#contentTypesVar">
				<jacmswpsa:contentType typeCode="%{#contentTypeVar.typeCode}" property="isAuthToEdit" var="isAuthToEditVar" />
				<s:if test="%{#isAuthToEditVar}">
					<li><a href="<s:url action="createNew" namespace="/do/jacms/Content" >
						   <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
					   </s:url>" ><s:text name="label.new" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" /></a></li>
				</s:if>
			</s:iterator>
		  </ul>
		</div>

		<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable fade in margin-base-top">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none">
				<s:text name="message.title.ActionErrors" />
			</h2>
			<ul class="margin-base-top">
			<s:iterator value="ActionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
			</ul>
		</div>
		</s:if>
		<s:if test="hasActionMessages()">
		<div class="alert alert-success alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
		</s:if>

		<s:set var="contentIdsVar" value="contents" />
		
		<wpsa:subset source="#contentIdsVar" count="10" objectName="groupContent" advanced="true" offset="5">
		<s:set var="group" value="#groupContent" />

		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>

		<s:if test="%{#contentIdsVar.size() > 0}">

		<div class="table-responsive">
			<table class="table table-bordered" id="contentListTable">
			<caption class="sr-only"><s:text name="title.contentList" /></caption>
			<tr>
				<th class="text-center padding-large-left padding-large-right"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<th>
				<a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
					<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
					<s:param name="lastOrder" ><s:property value="lastOrder" /></s:param>
					<s:param name="groupBy">descr</s:param>
					<s:param name="entandoaction:changeOrder">changeOrder</s:param>
					</s:url>"><s:text name="label.description" /></a>
				</th>
			<s:if test="viewCode">
				<th>
				<a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
					<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
					<s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
					<s:param name="groupBy">code</s:param>
					<s:param name="entandoaction:changeOrder">changeOrder</s:param>
					</s:url>"><s:text name="label.code" /></a>
				</th>
			</s:if>
				<s:if test="viewTypeDescr"><th><s:text name="label.type" /></th></s:if>
				<s:if test="viewStatus"><th><s:text name="label.state" /></th></s:if>
				<s:if test="viewGroup"><th><s:text name="label.group" /></th></s:if>
			<s:if test="viewCreationDate">
				<th class="text-center">
				<a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
					<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
					<s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
					<s:param name="groupBy">created</s:param>
					<s:param name="entandoaction:changeOrder">changeOrder</s:param>
					</s:url>"><s:text name="label.creationDate" /></a>
				</th>
			</s:if>
				<th class="text-center">
				<a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
					   <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
					<s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
					<s:param name="groupBy">lastModified</s:param>
					<s:param name="entandoaction:changeOrder">changeOrder</s:param>
					</s:url>"><s:text name="label.lastEdit" /></a>
				</th>
				<th class="text-center">
					<abbr title="<s:text name="name.onLine" />">P</abbr>
				</th>
			</tr>
			<s:iterator var="contentId">
			<s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
			<tr>
			<td class="text-center text-nowrap">
				<div class="btn-group btn-group-xs">
					<a class="btn btn-default" title="<s:text name="label.edit" />: <s:property value="#content.id" /> - <s:property value="#content.descr" />" href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /></s:url>">
						<span class="icon fa fa-pencil-square-o"></span>
						<span class="sr-only"><s:text name="label.edit" />: <s:property value="#content.id" /> - <s:property value="#content.descr" /></span>
					</a>
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu text-left" role="menu">
						<li>
							<a title="<s:text name="label.copyPaste" />: <s:property value="#content.id" /> - <s:property value="#content.descr" />" href="<s:url action="copyPaste" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="copyPublicVersion" value="'false'" /></s:url>">
								<span class="icon fa fa-fw fa-clipboard"></span>
								<s:text name="label.copyPaste" /><span class="sr-only">: <s:property value="#content.id" /> - <s:property value="#content.descr" /></span>
							</a>
						</li>
						<li>
							<a title="<s:text name="label.inspect" />: [<s:text name="name.work" />] <s:property value="#content.id" /> - <s:property value="#content.descr" />" href="<s:url action="inspect" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="currentPublicVersion" value="'false'" /></s:url>">
								<span class="icon fa fa-fw fa-info"></span>
								<s:text name="label.inspect" />&#32;<s:text name="name.work" />
							</a>
						</li>
						<li>
							<a title="<s:text name="label.inspect" />: [<s:text name="name.onLine" />] <s:property value="#content.id" /> - <s:property value="#content.descr" />" href="<s:url action="inspect" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="currentPublicVersion" value="'true'" /></s:url>">
								<span class="icon fa fa-fw fa-info-circle"></span>
								<s:text name="label.inspect" />&#32;<s:text name="name.onLine" />
							</a>
						</li>
						<wpsa:hookPoint key="jacms.contentFinding.contentRow.actions" objectName="hookpoint_contentFinding_contentRow">
						<li class="divider"></li>
							<s:iterator value="#hookpoint_contentFinding_contentRow" var="hookPointElement">
								<li>
									<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
								</li>
							</s:iterator>
						</wpsa:hookPoint>
					</ul>
				</div>
			</td>
			<td>
				<label>
					<input type="checkbox" name="contentIds" id="content_<s:property value="#content.id" />" value="<s:property value="#content.id" />" />&#32;
					<s:property value="#content.descr" />
				</label>
			</td>
			<s:if test="viewCode">
				<td>
					<code><s:property value="#content.id" /></code>
				</td>
			</s:if>
			<s:if test="viewTypeDescr">
				<td>
					<s:property value="%{getSmallContentType(#content.typeCode).descr}" />
				</td>
			</s:if>
			<s:if test="viewStatus">
				<td>
					<s:property value="%{getText('name.contentStatus.' + #content.status)}" />
				</td>
			</s:if>
			<s:if test="viewGroup">
				<td>
					<s:property value="%{getGroup(#content.mainGroupCode).descr}" />
				</td>
			</s:if>
			<s:if test="viewCreationDate">
				<td class="text-center text-nowrap">
					<code><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></code>
				</td>
			</s:if>
			<td class="text-center text-nowrap">
				<code><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></code>
			</td>

			<s:if test="#content.onLine && #content.sync">
				<s:set var="iconName">check</s:set>
				<s:set var="textVariant">success</s:set>
				<s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
			</s:if>
			<s:if test="#content.onLine && !(#content.sync)">
				<s:set var="iconName">adjust</s:set>
				<s:set var="textVariant">info</s:set>
				<s:set var="isOnlineStatus" value="%{getText('label.yes') + ', ' + getText('note.notSynched')}" />
			</s:if>
			<s:if test="!(#content.onLine)">
				<s:set var="iconName">pause</s:set>
				<s:set var="textVariant">warning</s:set>
				<s:set var="isOnlineStatus" value="%{getText('label.no')}" />
			</s:if>

			<td class="text-center">
				<span class="icon fa fa-<s:property value="iconName" /> text-<s:property value="textVariant" />" title="<s:property value="isOnlineStatus" />"></span>
				<span class="sr-only"><s:property value="isOnlineStatus" /></span>
			</td>
			</tr>
			</s:iterator>
			</table>
		</div>
		</s:if>

		<fieldset>
			<legend class="sr-only"><s:text name="title.contentActions" /></legend>
			<p class="sr-only"><s:text name="title.contentActionsIntro" /></p>
			<div class="btn-toolbar">
			<wp:ifauthorized permission="validateContents">
				<div class="btn-group margin-small-vertical">
					<wpsf:submit action="approveContentGroup" type="button" title="%{getText('note.button.approve')}" cssClass="btn btn-success">
						<span class="icon fa fa-check"></span>
						<s:text name="label.approve" />
					</wpsf:submit>
					<wpsf:submit action="suspendContentGroup" type="button" title="%{getText('note.button.suspend')}" cssClass="btn btn-warning">
						<span class="icon fa fa-pause"></span>
						<s:text name="label.suspend" />
					</wpsf:submit>
				</div>
			</wp:ifauthorized>
				<div class="btn-group margin-small-vertical">
					<wpsf:submit action="trashContentGroup" type="button" title="%{getText('note.button.delete')}" cssClass="btn btn-link">
						<span class="icon fa fa-times-circle"></span>
						<s:text name="label.remove" />
					</wpsf:submit>
				</div>
			</div>
		</fieldset>

		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>

		</wpsa:subset>

	</s:form>
</div>