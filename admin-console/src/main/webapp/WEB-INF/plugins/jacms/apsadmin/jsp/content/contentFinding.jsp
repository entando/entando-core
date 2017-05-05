<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%-- radios + checkboxes only --%>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core"%>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />

<!-- Admin console Breadcrumbs -->
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="breadcrumb.app" /></li>
	<li><s:text name="breadcrumb.jacms" /></li>
	<li class="page-title-container"><s:text
			name="breadcrumb.jacms.content.list" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}" />
<h1 class="page-title-container">
	<s:text name="title.contentList" />
	<span class="pull-right"> <a tabindex="0" role="button"
		data-toggle="popover" data-trigger="focus" data-html="true" title=""
		data-content="${dataContent}" data-placement="left"
		data-original-title="${dataOriginalTitle}"> <span
			class="fa fa-question-circle-o" aria-hidden="true"></span>
	</a>
	</span>
</h1>

<!-- Default separator -->
<div class="text-right">
	<div class="form-group-separator"></div>
</div>

<div id="main" role="main" class="mt-20">
	<wpsa:entityTypes entityManagerName="jacmsContentManager"
		var="contentTypesVar" />
	<div class="col-xs-12  ">
		<s:url action="search" var="formAction" namespace="do/jacms/Content" />

		<s:form action="%{'/' + #formAction}" cssClass="form-horizontal"
			role="search">
			<s:set var="categoryTreeStyleVar">
				<wp:info key="systemParam" paramName="treeStyle_category" />
			</s:set>
			<p class="sr-only">
				<wpsf:hidden name="lastGroupBy" />
				<wpsf:hidden name="lastOrder" />
			</p>

			<div class="well col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8 col-sm-offset-1 col-sm-10">
				<p class="search-label">
					<s:text name="label.search.label" />
				</p>

				<div class="form-group">
					<label class="control-label col-sm-2" for="text" class="sr-only"><s:text
							name="label.description" /></label>
					<div class="col-sm-9">
						<wpsf:textfield name="text" id="text" cssClass="form-control"
							placeholder="%{getText('label.description')}"
							title="%{getText('label.search.by')} %{getText('label.description')}" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-sm-2" for="contentIdToken"
						class="control-label col-sm-2 text-right"><s:text
							name="label.code" /></label>
					<div class="col-sm-9">
						<wpsf:textfield name="contentIdToken" id="contentIdToken"
							cssClass="form-control" placeholder="CNG12" />
					</div>
				</div>
				<br> <br>
				
				<!-------------Advanced Search----------->
				<div class="panel-group advanced-search" id="accordion-markup">
					<div class="panel panel-default">
						<div class="panel-heading">
							<p class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion-markup"
									href="#collapseOne"><s:text name="label.advancedSearch"/></a>
							</p>
						</div>
						<div id="collapseOne" class="panel-collapse collapse">
							<div class="panel-body">

								<div id="search-advanced"
									class="collapse-input-group <s:if test="(#categoryTreeStyleVar == 'request' && null != treeNodeActionMarkerCode)">in</s:if>">
									<div class="form-group">
										<label class="control-label col-sm-2" for="contentType"
											class="control-label col-sm-2 text-right"> <s:text
												name="label.type" />
										</label>
										<div class="col-sm-9 input-group">
											<wpsf:select cssClass="form-control" name="contentType"
												id="contentType" list="contentTypes" listKey="code"
												listValue="description" headerKey=""
												headerValue="%{getText('label.all')}" />
											<div class="input-group-btn">
												<wpsf:submit cssClass="btn btn-default"
													value="%{getText('label.set')}" />
											</div>
										</div>
									</div>
									<s:set var="searchableAttributes" value="searchableAttributes" />
									<s:if
										test="null != #searchableAttributes && #searchableAttributes.size() > 0">

										<s:iterator var="attribute" value="#searchableAttributes">
											<s:set var="currentFieldId"
												value="%{'entityFinding_'+#attribute.name}" />
											<s:if test="#attribute.textAttribute">
												<div class="form-group">
													<s:set var="textInputFieldName">
														<s:property value="#attribute.name" />_textFieldName</s:set>
													<label class="control-label col-sm-2"
														for="<s:property value="currentFieldId" />"
														class="control-label col-sm-3 text-right"><s:property
															value="#attribute.name" /></label>
													<div class="col-sm-9">
														<wpsf:textfield id="%{currentFieldId}"
															name="%{#textInputFieldName}"
															value="%{getSearchFormFieldValue(#textInputFieldName)}"
															cssClass="form-control" />
													</div>
												</div>
											</s:if>
											<s:elseif test="#attribute.type == 'Date'">
												<s:set var="dateStartInputFieldName">
													<s:property value="#attribute.name" />_dateStartFieldName</s:set>
												<s:set var="dateEndInputFieldName">
													<s:property value="#attribute.name" />_dateEndFieldName</s:set>

												<div class="form-group">
													<label class="control-label col-sm-2"
														for="<s:property value="%{currentFieldId}" />_dateStartFieldName_cal"
														class="control-label col-sm-9 text-right"><s:text
															name="note.range.from.attribute" />&#32;<s:property
															value="#attribute.name" /></label>
													<div class="col-sm-9">
														<wpsf:textfield
															id="%{currentFieldId}_dateStartFieldName_cal"
															name="%{#dateStartInputFieldName}"
															value="%{getSearchFormFieldValue(#dateStartInputFieldName)}"
															cssClass="form-control bootstrap-datepicker"
															placeholder="dd/mm/yyyy" />
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-sm-2"
														for="<s:property value="%{currentFieldId}" />_dateEndFieldName_cal"
														class="control-label col-sm-9 text-right"><s:text
															name="note.range.to.attribute" />&#32;<s:property
															value="#attribute.name" /></label>
													<div class="col-sm-9">
														<wpsf:textfield
															id="%{currentFieldId}_dateEndFieldName_cal"
															name="%{#dateEndInputFieldName}"
															value="%{getSearchFormFieldValue(#dateEndInputFieldName)}"
															cssClass="form-control bootstrap-datepicker"
															placeholder="dd/mm/yyyy" />
													</div>
												</div>
											</s:elseif>
											<s:elseif test="#attribute.type == 'Number'">
												<s:set var="numberStartInputFieldName">
													<s:property value="#attribute.name" />_numberStartFieldName</s:set>
												<s:set var="numberEndInputFieldName">
													<s:property value="#attribute.name" />_numberEndFieldName</s:set>
												<div class="form-group">
													<label class="control-label col-sm-2"
														for="<s:property value="currentFieldId" />_start"><s:text
															name="note.range.from.attribute" />&#32;<s:property
															value="#attribute.name" />:</label>
													<div class="col-sm-9">
														<wpsf:textfield id="%{currentFieldId}_start"
															name="%{#numberStartInputFieldName}"
															value="%{getSearchFormFieldValue(#numberStartInputFieldName)}"
															cssClass="form-control" />
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-sm-2"
														for="<s:property value="currentFieldId" />_end"><s:text
															name="note.range.to.attribute" />&#32;<s:property
															value="#attribute.name" />:</label>
													<div class="col-sm-9">
														<wpsf:textfield id="%{currentFieldId}_end"
															name="%{#numberEndInputFieldName}"
															value="%{getSearchFormFieldValue(#numberEndInputFieldName)}"
															cssClass="form-control" />
													</div>
												</div>

											</s:elseif>
											<s:elseif
												test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
												<p>
													<span class="important"><s:property
															value="#attribute.name" /></span><br />
												</p>
												<s:set var="booleanInputFieldName">
													<s:property value="#attribute.name" />_booleanFieldName</s:set>
												<s:set var="booleanInputFieldValue">
													<s:property
														value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
												</s:set>
												<ul class="noBullet radiocheck">
													<li><wpsf:radio id="none_%{#booleanInputFieldName}"
															name="%{#booleanInputFieldName}" value=""
															checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" /><label
														for="none_<s:property value="#booleanInputFieldName" />"
														class="normal"><s:text name="label.bothYesAndNo" /></label></li>
													<li><wpsf:radio id="true_%{#booleanInputFieldName}"
															name="%{#booleanInputFieldName}" value="true"
															checked="%{#booleanInputFieldValue == 'true'}" /><label
														for="true_<s:property value="#booleanInputFieldName" />"
														class="normal"><s:text name="label.yes" /></label></li>
													<li><wpsf:radio id="false_%{#booleanInputFieldName}"
															name="%{#booleanInputFieldName}" value="false"
															checked="%{#booleanInputFieldValue == 'false'}" /><label
														for="false_<s:property value="#booleanInputFieldName" />"
														class="normal"><s:text name="label.no" /></label></li>
												</ul>
											</s:elseif>
										</s:iterator>
									</s:if>
									<div class="form-group">
										<label for="contentType"
											class="control-label col-sm-2 text-right"> <s:text
												name="label.category" />
										</label>
										<div class="col-sm-9">
											<s:action name="showCategoryTreeOnContentFinding"
												namespace="/do/jacms/Content" ignoreContextParams="true"
												executeResult="true"></s:action>
										</div>
									</div>
								</div>
								<s:set var="allowedGroupsVar" value="allowedGroups" />
								<s:if
									test="null != #allowedGroupsVar && #allowedGroupsVar.size()>1">
									<div class="form-group">
										<label for="ownerGroupName"
											class="control-label col-sm-2 text-right"><s:text
												name="label.group" /></label>
										<div class="col-sm-9">
											<wpsf:select name="ownerGroupName" id="ownerGroupName"
												list="#allowedGroupsVar" headerKey=""
												headerValue="%{getText('label.all')}" listKey="name"
												listValue="descr" cssClass="form-control" />
										</div>
									</div>
								</s:if>
								<div class="form-group">
									<label for="state" class="control-label col-sm-2 text-right"><s:text
											name="label.state" /></label>
									<div class="col-sm-9">
										<wpsf:select name="state" id="state" list="avalaibleStatus"
											headerKey="" headerValue="%{getText('label.all')}"
											listKey="key" listValue="%{getText(value)}"
											cssClass="form-control" />
									</div>
								</div>
								<div class="form-group">
									<div class="btn-group col-sm-9 col-sm-offset-2"
										data-toggle="buttons">
										<label
											class="btn btn-default <s:if test="('yes' == onLineState)"> active </s:if>">
											<input type="radio" name="onLineState" id="approved"
											<s:if test="('yes' == onLineState)">checked="checked"</s:if>
											value="yes" />&#32; <s:text name="name.isApprovedContent" />
										</label> <label
											class="btn btn-default <s:if test="('no' == onLineState)"> active </s:if>">
											<input type="radio" name="onLineState" id="notApproved"
											<s:if test="('no' == onLineState)">checked="checked"</s:if>
											value="no" />&#32; <s:text name="name.isNotApprovedContent" />
										</label> <label
											class="btn btn-default <s:if test="('yes' != onLineState) && ('no' != onLineState)"> active </s:if>">
											<input type="radio" name="onLineState" id="bothApproved"
											<s:if test="('yes' != onLineState) && ('no' != onLineState)">checked="checked"</s:if>
											value="" />&#32; <s:text name="name.isApprovedOrNotContent" />
										</label>
									</div>
								</div>
							</div>
						</div>
					</div>
					<%--// search-advanced --%>
					<div class="form-group">
						<div class="col-sm-12">
							<wpsf:submit type="button" cssClass="btn btn-primary pull-right">
								<s:text name="label.search" />
							</wpsf:submit>
						</div>
					</div>
				</div>
			</div>
	</div>

	<!--Aggiunta colonne tabella lista contenuti-->
	<%-- Sostituito da funzionalità ColVis di DataTables
	<div class="col-xs-12">
		<div class="dropdown  pull-right dropdown-menu-right"
			id="search-configure-results">
			<button class="btn btn-default dropdown-toggle " type="button"
				data-toggle="dropdown">
				<s:text name="title.searchResultOptions" />
				&#32;<span class="fa fa-columns" aria-hidden="true"></span>
			</button>
			<ul class="dropdown-menu" role="menu">
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="#"> <label
						class="<s:if test="%{viewCreationDate}" > active</s:if>">
							<wpsf:checkbox name="viewCreationDate" id="viewCreationDate" />&#32;
							<s:text name="label.creationDate" />
					</label></a></li>
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="#"> <label
						class="<s:if test="%{viewTypeDescr}" > active</s:if>"> <wpsf:checkbox
								name="viewTypeDescr" id="viewTypeDescr" />&#32; <s:text
								name="name.contentType" />
					</label></a></li>
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="#"> <label
						class="<s:if test="%{viewStatus}" > active</s:if>"> <wpsf:checkbox
								name="viewStatus" id="viewStatus" />&#32; <s:text
								name="name.contentStatus" />
					</label></a></li>
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="#"> <label
						class="<s:if test="%{viewGroup}" > active</s:if>"> <wpsf:checkbox
								name="viewGroup" id="viewGroup" />&#32; <s:text
								name="label.group" />
					</label></a></li>
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="#"> <label
						class="<s:if test="%{viewCode}" > active</s:if>" for="viewCode">
							<wpsf:checkbox name="viewCode" id="viewCode" />&#32; <s:text
								name="label.code" />
					</label></a></li>
				<li role="presentation"><wpsf:submit type="button"
						cssClass="btn-no-button">
						<s:text name="label.table.update" />
					</wpsf:submit></li>
			</ul>
		</div>
	</div>
	--%>
	
    <!-- New Content Button -->
    <div class="col-xs-12">
        <div class="btn-group pull-right mt-20">
            <button type="button" class="btn btn-primary dropdown-toggle"
                data-toggle="dropdown">

                <s:text name="label.addContent" />
                &#32; <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu">
                <s:iterator var="contentTypeVar" value="#contentTypesVar">
                    <jacmswpsa:contentType typeCode="%{#contentTypeVar.typeCode}"
                        property="isAuthToEdit" var="isAuthToEditVar" />
                    <s:if test="%{#isAuthToEditVar}">
                        <li><a
                            href="<s:url action="createNew" namespace="/do/jacms/Content" >
                           <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
                            </s:url>"><s:text
                                    name="label.new" />&#32;<s:property
                                    value="%{#contentTypeVar.typeDescr}" /></a></li>
                    </s:if>
                </s:iterator>
            </ul>
        </div>
    </div>
	
	</s:form>

	<div class="col-xs-12 mt-20">
		<s:form action="search" cssClass="form-horizontal">
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
						<s:set var="textInputFieldName">
							<s:property value="#attribute.name" />_textFieldName</s:set>
						<wpsf:hidden name="%{#textInputFieldName}"
							value="%{getSearchFormFieldValue(#textInputFieldName)}" />
					</s:if>
					<s:elseif test="#attribute.type == 'Date'">
						<s:set var="dateStartInputFieldName">
							<s:property value="#attribute.name" />_dateStartFieldName</s:set>
						<s:set var="dateEndInputFieldName">
							<s:property value="#attribute.name" />_dateEndFieldName</s:set>
						<wpsf:hidden name="%{#dateStartInputFieldName}"
							value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
						<wpsf:hidden name="%{#dateEndInputFieldName}"
							value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Number'">
						<s:set var="numberStartInputFieldName">
							<s:property value="#attribute.name" />_numberStartFieldName</s:set>
						<s:set var="numberEndInputFieldName">
							<s:property value="#attribute.name" />_numberEndFieldName</s:set>
						<wpsf:hidden name="%{#numberStartInputFieldName}"
							value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
						<wpsf:hidden name="%{#numberEndInputFieldName}"
							value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
					</s:elseif>
					<s:elseif
						test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
						<s:set var="booleanInputFieldName">
							<s:property value="#attribute.name" />_booleanFieldName</s:set>
						<wpsf:hidden name="%{#booleanInputFieldName}"
							value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
					</s:elseif>
				</s:iterator>
			</p>

			<s:if test="hasActionErrors()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">
						<span class="pficon pficon-close"></span>
					</button>
					<span class="pficon pficon-error-circle-o"></span>
					<s:text name="message.title.ActionErrors" />
					<ul class="margin-base-top">
						<s:iterator value="ActionErrors">
							<li><s:property escapeHtml="false" /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<s:if test="hasActionMessages()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">
						<span class="pficon pficon-close"></span>
					</button>
					<span class="pficon pficon-error-circle-o"></span>
					<s:text name="messages.confirm" />
					<ul class="margin-base-top">
						<s:iterator value="actionMessages">
							<li><s:property escapeHtml="false" /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>

			<s:set var="contentIdsVar" value="contents" />
			
			<s:if test="%{#contentIdsVar.size() > 0}">
			
			<!-- Content List -->
			<wpsa:subset source="#contentIdsVar" count="10" objectName="groupContent" advanced="true" offset="5">
				<s:set var="group" value="#groupContent" />

                    <%-- TODO: verificare filtri per lista
					<!-- Tabs -->
					<div class="col-xs-12 no-padding mt-20">
						<ul class="nav nav-tabs nav-tabs-pf nav-justified">
							<li class="active"><a href="#">My Contents</a></li>
							<li><a href="#">All</a></li>
							<li><a href="#">Approved</a></li>
							<li><a href="#">Suspended</a></li>
							<li><a href="#">Public</a></li>
							<li><a href="#">Draft</a></li>
						</ul>
					</div>
                     --%>

					<!-- Toolbar -->
                    <div class="col-xs-12 no-padding" id="content-list-toolbar">
                        <div class="row toolbar-pf table-view-pf-toolbar border-bottom">
                            <div class="col-xs-12">

                                <!-- toolbar first row  -->
                                <div class="toolbar-pf-actions">
                                    <!-- items selected -->
                                    <div class="col-lg-6 col-md-4 col-xs-12 no-padding">
	                                    <div class="selected-items">
	                                        <span class="selected-items-counter">0</span> <s:text name="title.itemSelected" />
	                                    </div>
                                    </div>

                                    <!-- toolbar -->
                                    <div class="col-lg-6 col-md-8 col-xs-12 no-padding">
                                        <label class="col-lg-2 col-md-4 control-label"> 
                                            <%-- <s:text name="title.contentActions" /> --%>
                                            <s:text name="label.setAs" />
                                        </label>

                                        <div class="col-lg-10 col-md-8 col-sm-12 no-padding">
                                            <div class="btn-toolbar">
                                                <wp:ifauthorized permission="validateContents">
                                                    <div class="btn-group">
                                                        <wpsf:submit action="bulkPutOnline" type="button"
                                                            title="%{getText('note.button.approve')}"
                                                            cssClass="btn btn-success">
                                                            <s:text name="label.approve" />
                                                        </wpsf:submit>
                                                        <wpsf:submit action="bulkPutOffline" type="button"
                                                            title="%{getText('note.button.suspend')}"
                                                            cssClass="btn btn-warning">
                                                            <s:text name="label.suspend" />
                                                        </wpsf:submit>
                                                    </div>
                                                </wp:ifauthorized>

                                                <wpsa:hookPoint
                                                    key="jacms.contentFinding.allContents.actions"
                                                    objectName="hookpoint_contentFinding_allContents">
                                                    <div class="btn-group">
                                                        <s:iterator value="#hookpoint_contentFinding_allContents"
                                                            var="hookPointElement">
                                                            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                                                        </s:iterator>
                                                    </div>
                                                </wpsa:hookPoint>

                                                <div class="btn-group pull-right">
                                                    <wpsf:submit action="bulkRemove" type="button"
                                                        title="%{getText('note.button.delete')}"
                                                        cssClass="btn btn-default">
                                                        <s:text name="label.remove" />
                                                    </wpsf:submit>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- toolbar second row -->
                                <div class="row toolbar-pf-results">
                                    <div class="col-lg-6 col-lg-offset-6 col-md-8 col-md-offset-4 col-xs-12 no-padding">
                                        <label class="col-lg-2 col-md-4 control-label"><s:text name="label.actions" /></label>
                                        <div class="dropdown col-lg-10 col-md-8 no-padding">
                                            <button type="button"
                                                class="btn btn-default dropdown-toggle w100perc text-right"
                                                id="bulkAction" data-toggle="dropdown" aria-haspopup="true"
                                                aria-expanded="false">
                                                <span class="pull-left"><s:text name="label.selectAction"/></span> <span
                                                    class="caret"></span>
                                            </button>
                                            <ol class="dropdown-menu w100perc"
                                                aria-labelledby="bulkAction">
                                                <%-- Group Bulk Action disabled
                                                <li><wpsa:actionParam action="bulkOnGroups"
                                                        var="bulkActionName">
                                                        <wpsa:actionSubParam name="strutsAction" value="1" />
                                                    </wpsa:actionParam> <wpsf:submit action="%{#bulkActionName}" type="button"
                                                        title="%{getText('note.button.addGroups')}"
                                                        cssClass="btn btn-success">
                                                        <span class="icon fa"></span>
                                                        <s:text name="label.addGroups" />
                                                    </wpsf:submit></li>
                                                <li><wpsa:actionParam action="bulkOnGroups"
                                                        var="bulkActionName">
                                                        <wpsa:actionSubParam name="strutsAction" value="4" />
                                                    </wpsa:actionParam> <wpsf:submit action="%{#bulkActionName}" type="button"
                                                        title="%{getText('note.button.removeGroups')}"
                                                        cssClass="btn btn-success">
                                                        <span class="icon fa"></span>
                                                        <s:text name="label.removeGroups" />
                                                    </wpsf:submit></li>
                                                --%>
                                                <li><wpsa:actionParam action="bulkOnCategories"
                                                        var="bulkActionName">
                                                        <wpsa:actionSubParam name="strutsAction" value="1" />
                                                    </wpsa:actionParam> <wpsf:submit action="%{#bulkActionName}" type="button"
                                                        title="%{getText('note.button.addCategories')}"
                                                        cssClass="btn btn-success">
                                                        <span class="icon fa"></span>
                                                        <s:text name="label.addCategories" />
                                                    </wpsf:submit></li>
                                                <li><wpsa:actionParam action="bulkOnCategories"
                                                        var="bulkActionName">
                                                        <wpsa:actionSubParam name="strutsAction" value="4" />
                                                    </wpsa:actionParam> <wpsf:submit action="%{#bulkActionName}" type="button"
                                                        title="%{getText('note.button.removeCategories')}"
                                                        cssClass="btn btn-success">
                                                        <span class="icon fa"></span>
                                                        <s:text name="label.removeCategories" />
                                                    </wpsf:submit></li>
                                            </ol>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div> 
                    
                    <!-- Content List - Table -->
					<%-- <caption class="sr-only"><s:text name="title.contentList" /></caption> --%>
					<div class="col-xs-12 no-padding">
						<div class="alert alert-warning hidden selectall-box no-mb mt-20">
							<span class="pficon pficon-warning-triangle-o"></span>
		                    <label for="allContentsSelected" class="control-label mr-10">
		                       <s:text name="label.allContentsSelected"/>
		                    </label>
		                    <wpsf:checkbox name="allContentsSelected" id="allContentsSelected" cssClass="bootstrap-switch" value="false"/>
						</div>
					</div>
					
					<div class="col-xs-12 no-padding">
						<div class="mt-20">
							<table class="table table-striped table-bordered table-hover" id="contentListTable">
								<thead>
									<tr>
										<th class="text-center">
										  <label class="sr-only" for="selectAll"><s:text name="label.selectAll" /></label>
										  <input type="checkbox" class="js_selectAll">
										</th>
										<th>
										  <a href="<s:url action="changeOrder" includeParams="all" >
											<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
											<s:param name="lastOrder" ><s:property value="lastOrder" /></s:param>
											<s:param name="groupBy">descr</s:param>
											<s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                            </s:url>">
                                            <s:text name="label.description" /></a>
                                        </th>
									   <th><s:text name="label.author"/></th>
<%-- 										<s:if test="viewCode"> --%>
											<th>
                                                <a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
					                                <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
					                                <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
					                                <s:param name="groupBy">code</s:param>
					                                <s:param name="entandoaction:changeOrder">changeOrder</s:param>
					                                </s:url>"><s:text name="label.code" />
				                                </a>
			                                </th>
<%-- 										</s:if> --%>
<%-- 										<s:if test="viewTypeDescr"> --%>
											<th><s:text name="label.type" /></th>
<%-- 										</s:if> --%>
<%-- 										<s:if test="viewStatus"> --%>
											<th class="text-center cell-w100"><s:text
													name="label.state" /></th>
<%-- 										</s:if> --%>
										<th class="text-center cell-w100"><s:text name="label.visible"/></th>
<%-- 										<s:if test="viewGroup"> --%>
											<th><s:text name="label.group" /></th>
<%-- 										</s:if> --%>
<%-- 										<s:if test="viewCreationDate"> --%>
											<th><a  href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
												<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
												<s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
												<s:param name="groupBy">created</s:param>
												<s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                                </s:url>">
                                                <s:text name="label.creationDate" /></a>
                                            </th>
<%-- 										</s:if> --%>
										<th>
											<a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
											<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
											<s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
											<s:param name="groupBy">lastModified</s:param>
											<s:param name="entandoaction:changeOrder">changeOrder</s:param>
											</s:url>"><s:text name="label.lastEdit" /></a>
										</th>
										<th class="text-center cell-w100"><s:text name="label.actions" /></th>
									</tr>
								</thead>
								<tbody>
									<s:iterator var="contentId">
										<s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
										<tr>
											<td class="text-center"><input type="checkbox"
												name="contentIds"
												id="content_<s:property value="#content.id" />"
												value="<s:property value="#content.id" />" /></td>
											<td><s:property value="#content.descr" />&#32;</td>
											<td>
												<s:if test="%{#content.lastEditor != null && #content.lastEditor != ''}">
												    <s:property value="#content.lastEditor"/>
												</s:if>
												<s:elseif test="%{#content.firstEditor != null && #content.firstEditor != ''}">
												    <s:property value="#content.firstEditor"/>
												</s:elseif>
											</td>
<%-- 											<s:if test="viewCode"> --%>
												<td ><s:property value="#content.id" /></td>
<%-- 											</s:if> --%>
<%-- 											<s:if test="viewTypeDescr"> --%>
												<td><s:property value="%{getSmallContentType(#content.typeCode).descr}" /></td>
<%-- 											</s:if> --%>
<%-- 											<s:if test="viewStatus"> --%>
												<td class="text-center">
												<s:if test="%{#content.onLine && #content.sync}">
												    <s:set value="%{getText('name.contentStatus.' + #content.status)}" var="statusLabel" />
                                                    <span class="fa fa-circle green" aria-hidden="true" title="${statusLabel}"></span>
												</s:if>
												<s:elseif test="%{#content.onLine && !(#content.sync)}">
                                                    <s:set var="statusLabel"><s:property value="%{getText('name.contentStatus.' + 'PUBLIC')}" />&#32;&ne;&#32;<s:property value="%{getText('name.contentStatus.' + 'DRAFT')}" />
                                                    </s:set>
                                                    <span class="fa fa-circle yellow" aria-hidden="true" title="${statusLabel}"></span>
												</s:elseif>
												<s:else>
												    <s:set var="statusLabel" value="%{getText('name.contentStatus.' + 'OFFLINE')}" />
												    <span class="fa fa-circle gray" aria-hidden="true" title="${statusLabel}"></span>
												</s:else>
												</td>
<%-- 											</s:if> --%>
											<%--
                                            <s:if test="#content.onLine && #content.sync">
                                                <s:set var="iconName">check</s:set>
                                                <s:set var="textVariant">success</s:set>
                                                <s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
                                            </s:if>
                                            <s:if test="#content.onLine && !(#content.sync)">
                                                <s:set var="iconName">adjust</s:set>
                                                <s:set var="textVariant">info</s:set>
                                                <s:set var="isOnlineStatus"
                                                    value="%{getText('label.yes') + ', ' + getText('note.notSynched')}" />
                                            </s:if>
                                            <s:if test="!(#content.onLine)">
                                                <s:set var="iconName">pause</s:set>
                                                <s:set var="textVariant">warning</s:set>
                                                <s:set var="isOnlineStatus" value="%{getText('label.no')}" />
                                            </s:if>
                                            <td class="text-center" style="width: 40px;"><span
                                                class="icon fa fa-<s:property value="iconName" /> text-<s:property value="textVariant" />"
                                                title="<s:property value="isOnlineStatus" />"></span> <span
                                                class="sr-only"><s:property value="isOnlineStatus" /></span>
                                            </td>
                                             --%>
                                            
                                            <td class="text-center">
                                            <s:if test="%{#content.mainGroupCode != null && !#content.mainGroupCode.equals('free')}">
                                                <span class="icon fa fa-lock"></span>
                                            </s:if>
                                            <s:else>
                                                <span class="icon fa fa-unlock"></span>
                                            </s:else>
                                            <%-- TODO: extra groups 
                                                <s:property value="%{#content.groups}"/>
                                            --%>
                                            </td>
                                            
<%-- 											<s:if test="viewGroup"> --%>
												<td ><s:property
														value="%{getGroup(#content.mainGroupCode).descr}" /></td>
<%-- 											</s:if> --%>
<%-- 											<s:if test="viewCreationDate"> --%>
												<td class="text-nowrap"><s:date
														name="#content.create" format="dd/MM/yyyy HH:mm" /></td>
<%-- 											</s:if> --%>
											<td class=" text-nowrap"><s:date
													name="#content.modify" format="dd/MM/yyyy HH:mm" /></td>
											<td class=" table-view-pf-actions">
												<div class="dropdown dropdown-kebab-pf">
													<button class="btn btn-menu-right dropdown-toggle"
														type="button" data-toggle="dropdown">
														<span class="fa fa-ellipsis-v"></span>
													</button>
													<ul class="dropdown-menu dropdown-menu-right">
														<li><a
															title="<s:text name="label.copyPaste" />: <s:property value="#content.id" /> - <s:property value="#content.description" />"
															href="<s:url action="copyPaste" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="copyPublicVersion" value="'false'" /></s:url>">
																<s:text name="label.copyPaste" /><span class="sr-only">:
																	<s:property value="#content.id" /> - <s:property
																		value="#content.descr" />
															</span>
														</a></li>
														<li><a
															title="<s:text name="label.inspect" />: [<s:text name="name.work" />] <s:property value="#content.id" /> - <s:property value="#content.descr" />"
															href="<s:url action="inspect" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="currentPublicVersion" value="'false'" /></s:url>">
																<s:text name="label.inspect" />&#32;<s:text
																	name="name.work" />
														</a></li>
														<li><a
															title="<s:text name="label.inspect" />: [<s:text name="name.onLine" />] <s:property value="#content.id" /> - <s:property value="#content.descr" />"
															href="<s:url action="inspect" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="currentPublicVersion" value="'true'" /></s:url>">
																<s:text name="label.inspect" />&#32;<s:text
																	name="name.onLine" />
														</a></li>
														<li><a
															title="<s:text name="label.edit" />: <s:property value="#content.id" /> - <s:property value="#content.description" />"
															href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /></s:url>">
																<s:text name="label.edit" />: <s:property
																	value="#content.id" /> - <s:property
																	value="#content.description" /> <span class="sr-only"><s:text
																		name="label.edit" />: <s:property value="#content.id" />
																	- <s:property value="#content.description" /></span>
														</a></li>
														<wpsa:hookPoint
															key="jacms.contentFinding.contentRow.actions"
															objectName="hookpoint_contentFinding_contentRow">
															<s:iterator value="#hookpoint_contentFinding_contentRow"
																var="hookPointElement">
																<li><wpsa:include
																		value="%{#hookPointElement.filePath}"></wpsa:include>
																</li>
															</s:iterator>
														</wpsa:hookPoint>
													</ul>
												</div>
											</td>
										</tr>
									</s:iterator>
								</tbody>
							</table>
            </div>
					</div>

					<div class="content-view-pf-pagination table-view-pf-pagination clearfix mt-20 mb-20">
						<%-- TODO: abilitare selezione elementi per pagina --%>
						<%-- 
						<div class="form-group">
							<div class="btn-group bootstrap-select pagination-pf-pagesize">
								<button type="button" class="btn dropdown-toggle btn-default"
									data-toggle="dropdown" title="15">
									<span class="filter-option pull-left">15</span>&nbsp;<span
										class="bs-caret"><span class="caret"></span></span>
								</button>
								<div class="dropdown-menu open">
									<ul class="dropdown-menu inner" role="menu">
										<li data-original-index="0"><a tabindex="0" class=""
											style="" data-tokens="null"><span class="text">6</span>
											</a></li>
										<li data-original-index="1"><a tabindex="0" class=""
											style="" data-tokens="null"><span class="text">10</span></a></li>
										<li data-original-index="2" class="selected"><a
											tabindex="0" class="" style="" data-tokens="null"><span
												class="text">15</span></a></li>
										<li data-original-index="3"><a tabindex="0" class=""
											style="" data-tokens="null"><span class="text">25</span></a></li>
										<li data-original-index="4"><a tabindex="0" class=""
											style="" data-tokens="null"><span class="text">50</span></a></li>
									</ul>
								</div>
								<select class="selectpicker pagination-pf-pagesize"
									tabindex="-98">
									<option value="6">6</option>
									<option value="10">10</option>
									<option value="15">15</option>
									<option value="25">25</option>
									<option value="50">50</option>
								</select>
							</div>
							<span><s:text name="label.elementsPerPage"/></span>
						</div>
						--%>
						<div class="form-group">
							<span><s:include
									value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
							<div class="mt-5">
								<s:include
									value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
							</div>
						</div>
					</div>
			</wpsa:subset>
		</s:if>
		<s:else>
	        <div class="alert alert-info">
	            <span class="pficon pficon-info"></span>
	            <strong><s:text name="label.listEmpty" /></strong>&#32;<s:text name="label.noContentFound" />
	        </div>
		</s:else>
	</div>
	
	</s:form>
</div>