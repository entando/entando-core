<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/jacms/Content"/>">
			<s:text name="jacms.menu.contentAdmin" />
		</a>&#32;/&#32;
		<s:if test="getStrutsAction() == 1">
			<s:text name="label.new" />
		</s:if>
		<s:else>
			<s:text name="label.edit" />
		</s:else>
	</span>
</h1>
<div id="main" role="main">
	<div data-autosave="messages_container">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
				<p class="margin-none margin-base-top">
					<s:text name="message.content.error" />
					&ensp;<span
						class="icon fa fa-question-circle cursor-pointer"
						title="<s:text name="label.all" />"
						data-toggle="collapse"
						data-target="#content-error-messages"></span>
					<span class="sr-only"><s:text name="label.all" /></span>
				</p>
				<ul class="unstyled collapse margin-small-top" id="content-error-messages">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><%-- <s:property value="key" />&emsp;|--%><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
	</div>
	<p class="sr-only"><s:text name="note.editContent" /></p>
	<s:url namespace="/do/jacms/Content/Ajax"  action="autosave" var="dataAutosaveActionVar" />
	<s:form cssClass="tab-container action-form" data-form-type="autosave" data-autosave-action="%{#dataAutosaveActionVar}">
		<s:set var="myNameIsJack" value="true" />
		<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/include/snippet-content.jsp" />
		<p class="sr-only">
			<wpsf:hidden name="contentOnSessionMarker" />
		</p>
		<p class="sr-only" id="quickmenu"><s:text name="title.quickMenu" /></p>
		<ul class="nav nav-tabs tab-togglers" id="tab-togglers">
			<li class="sr-only"><a data-toggle="tab" href="#info_tab"><s:text name="title.contentInfo" /></a></li>
			<s:iterator value="langs" var="lang" status="langStatusVar">
				<li <s:if test="#langStatusVar.first"> class="active" </s:if>>
					<a data-toggle="tab" href="#<s:property value="#lang.code" />_tab">
						<s:property value="#lang.descr" />
					</a>
				</li>
			</s:iterator>
		</ul>
		<div class="panel panel-default" id="tab-container"><%-- panel --%>
			<div class="panel-body"><%-- panel body --%>
				<div class="tab-content"><%-- tabs container --%>
					<s:iterator value="langs" var="lang" status="langStatusVar"><%-- lang iterator --%>
						<div id="<s:property value="#lang.code" />_tab" class="tab-pane <s:if test="#langStatusVar.first"> active </s:if>"><%-- tab --%>
							<h2 class="sr-only">
								<s:property value="#lang.descr" />
							</h2>
							<p class="sr-only">
								<a class="sr-only" href="#quickmenu" id="<s:property value="#lang.code" />_tab_quickmenu"><s:text name="note.goBackToQuickMenu" /></a>
							</p>
							<s:iterator value="content.attributeList" var="attribute"><%-- attributes iterator --%>
								<div id="<s:property value="%{'contentedit_'+#lang.code+'_'+#attribute.name}" />"><%-- contentedit div --%>
									<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" /><%-- tracer init --%>

									<s:set var="attributeFieldErrorsVar" value="%{fieldErrors[#attribute.name]}" />
									<s:set var="attributeHasFieldErrorVar" value="#attributeFieldErrorsVar != null && !#attributeFieldErrorsVar.isEmpty()" />
									<s:set var="attributeFieldNameErrorsVar" value="%{fieldErrors[#attributeTracer.getFormFieldName(#attribute)]}" />
									<s:set var="attributeHasFieldNameErrorVar" value="#attributeFieldNameErrorsVar != null && !#attributeFieldNameErrorsVar.isEmpty()" />
									<s:set var="attributeFieldNameErrorsVarV2" value="%{fieldErrors[#attribute.type+':'+#attribute.name]}" />
									<s:set var="attributeHasFieldNameErrorVarV2" value="#attributeFieldNameErrorsVarV2 != null && !#attributeFieldNameErrorsVarV2.isEmpty()" />

									<s:set var="attributeHasErrorVar" value="%{#attributeHasFieldErrorVar||#attributeHasFieldNameErrorVar||#attributeHasFieldNameErrorVarV2}" />
									<s:set var="controlGroupErrorClassVar" value="''" />
									<s:set var="inputErrorClassVar" value="''" />
									<s:if test="#attributeHasErrorVar">
										<s:set var="controlGroupErrorClassVar" value="' has-error'" />
										<s:set var="inputErrorClassVar" value="' input-with-feedback'" />
									</s:if>

									<s:if test="null != #attribute.description"><s:set var="attributeLabelVar" value="#attribute.description" /></s:if>
									<s:else><s:set var="attributeLabelVar" value="#attribute.name" /></s:else>

									<div class="form-group<s:property value="#controlGroupErrorClassVar" />"><%-- form group --%>
										<s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
												<label class="display-block"><span class="icon fa fa-list"></span>&#32;<s:property value="#attributeLabelVar" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
										</s:if>
										<s:elseif test="#attribute.type == 'Image' || #attribute.type == 'Attach' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
												<label class="display-block"><s:property value="#attributeLabelVar" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
										</s:elseif>
										<s:elseif test="#attribute.type == 'Composite'">
												<label class="display-block"><span class="icon fa fa-list-alt"></span>&#32;<s:property value="#attributeLabelVar" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
										</s:elseif>
										<s:elseif test="#attribute.type == 'Monotext' || #attribute.type == 'Text' || #attribute.type == 'Longtext' || #attribute.type == 'Hypertext' || #attribute.type == 'Number' || #attribute.type == 'Date' || #attribute.type == 'Timestamp' || #attribute.type == 'Link' || #attribute.type == 'Enumerator'">
												<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#attributeLabelVar" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
										</s:elseif>
										<s:if test="#attribute.type == 'Attach'">
											<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/attachAttribute.jsp" />
										</s:if>
										<s:elseif test="#attribute.type == 'Boolean'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'CheckBox'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Date'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Enumerator'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Hypertext'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Image'">
											<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Link'">
											<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/linkAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Longtext'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Monotext'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Number'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Text'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'ThreeState'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Timestamp'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/timestampAttribute.jsp" />
										</s:elseif>

										<s:elseif test="#attribute.type == 'Composite'">
											<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/compositeAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'List'">
											<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/listAttribute.jsp" />
										</s:elseif>
										<s:elseif test="#attribute.type == 'Monolist'">
											<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/monolistAttribute.jsp" />
										</s:elseif>
										<wpsa:hookPoint key="jacms.entryContent.attributeExtra" objectName="hookPointElements_jacms_entryContent_attributeExtra">
											<s:iterator value="#hookPointElements_jacms_entryContent_attributeExtra" var="hookPointElement">
												<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
											</s:iterator>
										</wpsa:hookPoint>

										<s:if test="#attributeHasErrorVar">
											<p class="text-danger padding-small-vertical">
												<jsp:useBean id="attributeErrorMapVar" class="java.util.HashMap" scope="request"/>
												<s:iterator value="#attributeFieldErrorsVar"><s:set var="attributeCurrentError" scope="page" /><c:set target="${attributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/> </s:iterator>
												<s:iterator value="#attributeFieldNameErrorsVar"><s:set var="attributeCurrentError" scope="page" /><c:set target="${attributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/> </s:iterator>
												<s:iterator value="#attributeFieldNameErrorsVarV2"><s:set var="attributeCurrentError" scope="page" /><c:set target="${attributeErrorMapVar}" property="${attributeCurrentError}" value="${attributeCurrentError}"/> </s:iterator>
												<c:forEach items="${attributeErrorMapVar}" var="attributeCurrentError">
													<c:out value="${attributeCurrentError.value}" /><br />
												</c:forEach>
												<c:set var="attributeErrorMapVar" value="${null}" />
												<c:set var="attributeCurrentError" value="${null}" />
											</p>
										</s:if>
										<s:set var="attributeHasErrorVar" value="%{null}" />
										<s:set var="attributeFieldErrorsVar" value="%{null}" />
										<s:set var="attributeHasFieldErrorVar" value="%{null}" />
										<s:set var="attributeFieldNameErrorsVar" value="%{null}" />
										<s:set var="attributeHasFieldNameErrorVar" value="%{null}" />
										<s:set var="attributeFieldNameErrorsVarV2" value="%{null}" />
										<s:set var="attributeHasFieldNameErrorVarV2" value="%{null}" />
										<s:set var="attributeHasErrorVar" value="%{null}" />
										<s:set var="controlGroupErrorClassVar" value="%{null}" />
										<s:set var="inputErrorClassVar" value="%{null}" />
									</div><%-- form group --%>
								</div><%-- contentedit div --%>
							</s:iterator><%-- attributes iterator --%>
							<%-- preview --%>
								<s:set var="showingPageSelectItems" value="showingPageSelectItems" />
								<wpsa:actionParam action="preview" var="previewActionName" >
									<wpsa:actionSubParam name="%{'jacmsPreviewActionLangCode_' + #lang.code}" value="%{#lang.code}" />
								</wpsa:actionParam>
								<hr />
								<div class="form-group  margin-none margin-large-top">
									<s:if test="!#showingPageSelectItems.isEmpty()">
										<div class="input-group input-group-lg">
											<s:set var="previewActionPageCodeLabelId">jacmsPreviewActionPageCode_<s:property value="#lang.code" /></s:set>
											<label class="sr-only" for="<s:property value="#previewActionPageCodeLabelId" />">
												<s:text name="name.preview.page" />
											</label>
											<wpsf:select
												name="%{'jacmsPreviewActionPageCode_' + #lang.code}"
												id="%{#previewActionPageCodeLabelId}"
												list="#showingPageSelectItems"
												listKey="key"
												listValue="%{getText('name.preview.page') + ': ' +value}"
												cssClass="form-control"
												data-autosave="ignore" />
											<span class="input-group-btn">
												<%-- <wpsf:select name="jacmsPreviewActionPageCode" id="%{#previewActionPageCodeLabelId}" list="#showingPageSelectItems" listKey="key" listValue="value" /></p>  --%>
												<wpsf:submit
													type="button"
													cssClass="btn btn-info"
													action="%{#previewActionName}"
													title="%{getText('note.button.previewContent')}"
													>
													<span class="icon fa fa-eye"></span>&#32;<s:text name="label.preview" />
												</wpsf:submit>
											</span>
										</div>
									</s:if>
									<s:else>
										<p class="static-control text-center text-info"><s:text name="label.preview.noPreviewPages" /></p>
									</s:else>
								</div>

						</div><%-- tab --%>
					</s:iterator><%-- lang iterator --%>
				</div><%-- tabs container --%>
			</div><%-- panel body --%>
		</div><%-- panel --%>
		<div id="info" class="panel panel-default"><%-- info section --%>
			<div class="panel-heading">
				<h2 class="h4 margin-none">
					<s:text name="title.contentInfo" />
					<a href="#quickmenu" id="info_content_goBackToQuickMenu" class="pull-right" title="<s:text name="note.goBackToQuickMenu" />"><span class="icon fa fa-arrow-circle-up"></span><span class="sr-only"><s:text name="note.goBackToQuickMenu" /></span></a>
				</h2>
			</div>
			<div class="panel-body">
				<fieldset class="col-xs-12"><%-- extra groups --%>
					<legend><s:text name="label.extraGroups" /></legend>
					<%-- group add --%>
						<div class="form-group">
							<label for="extraGroups" class="basic-mint-label">
								<s:text name="label.join" />&#32;<s:text name="label.group" />
							</label>
							<div class="input-group">
								<wpsf:select
									name="extraGroupName"
									id="extraGroups"
									list="groups"
									listKey="name"
									listValue="descr"
									cssClass="form-control"
									data-autosave="ignore" />
								<span class="input-group-btn">
									<wpsf:submit
										type="button"
										action="joinGroup"
										cssClass="btn btn-default">
											<span class="icon fa fa-plus"></span>&#32;
											<s:text name="label.join" />
									</wpsf:submit>
								</span>
							</div>
						</div>
					<%-- groups added --%>
						<s:if test="content.groups.size != 0">
							<div class="form-group">
								<div class="input-group">
								<s:iterator value="content.groups" var="groupName">
									<wpsa:actionParam action="removeGroup" var="actionName" >
										<wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
									</wpsa:actionParam>
									<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
										<span class="icon fa fa-tag"></span>&#32;
										<s:property value="%{getGroupsMap()[#groupName].getDescr()}"/>&#32;
										<wpsf:submit type="button" cssClass="btn btn-default btn-xs badge" action="%{#actionName}" title="%{getText('label.remove')+' '+getGroupsMap()[#groupName].getDescr()}">
											<span class="icon fa fa-times"></span>
											<span class="sr-only">x</span>
										</wpsf:submit>
									</span>
								</s:iterator>
								</div>
							</div>
						</s:if>
				</fieldset><%-- extra groups --%>
			</div>
			<%-- categories --%>
				<div class="panel-body">
					<s:action name="showCategoryBlockOnEntryContent" namespace="/do/jacms/Content" executeResult="true">
						<s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
					</s:action>
				</div>
			<%-- hookpoint general section --%>
				<wpsa:hookPoint key="jacms.entryContent.tabGeneral" objectName="hookPointElements_jacms_entryContent_tabGeneral">
					<s:iterator value="#hookPointElements_jacms_entryContent_tabGeneral" var="hookPointElement">
						<div class="panel-body">
							<wpsa:include value="%{#hookPointElement.filePath}" />
						</div>
					</s:iterator>
				</wpsa:hookPoint>
		</div><%-- info section --%>
		<%-- actions --%>
			<h2 class="sr-only"><s:text name="title.contentActionsIntro" /></h2>
			<wpsa:hookPoint key="jacms.entryContent.actions" objectName="hookPointElements_jacms_entryContent_actions">
				<s:iterator value="#hookPointElements_jacms_entryContent_actions" var="hookPointElement">
					<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
				</s:iterator>
			</wpsa:hookPoint>
	</s:form>
</div><%-- main --%>