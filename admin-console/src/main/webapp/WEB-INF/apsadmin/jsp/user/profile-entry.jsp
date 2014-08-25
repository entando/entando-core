<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a>
		&#32;/&#32;
		<s:text name="title.userProfile.edit" />
	</span>
</h1>
<div id="main" role="main">
	<s:form cssClass="form-horizontal">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" />&ensp;<span
						class="icon fa fa-question-circle cursor-pointer"
						title="<s:text name="label.all" />"
						data-toggle="collapse"
						data-target="#content-error-messages"></span>
					<span class="sr-only"><s:text name="label.all" /></span>
				</h2>
				<ul class="unstyled collapse margin-small-top" id="content-error-messages">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><%-- <s:property value="key" />&emsp;|--%><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<div class="col-xs-12">
			<div class="form-group">
				<label class="display-block"><s:text name="label.username" /></label>
					<p class="form-control-static">
						<s:property value="userProfile.username" />
					</p>
			</div>
			<s:set name="lang" value="defaultLang" />
			<%-- attribute iterator --%>
			<s:iterator value="userProfile.attributeList" var="attribute">
				<%-- tracer start --%>
				<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

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
					<s:elseif test="#attribute.type == 'Image' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState' || #attribute.type == 'Composite'">
						<label class="display-block"><s:property value="#attributeLabelVar" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
					</s:elseif>
					<s:else>
						<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#attributeLabelVar" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
					</s:else>
					<s:if test="#attribute.type == 'Attach'">
						<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/attachAttribute.jsp" />
					</s:if>
					<s:elseif test="#attribute.type == 'Boolean'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'CheckBox'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Composite'">
						<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/compositeAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Date'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Enumerator'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Image'">
						<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Hypertext'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Link'">
						<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/linkAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'List'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/listAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Longtext'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Monolist'">
						<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/monolistAttribute.jsp" />
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
					<s:else><%-- all other attributes uses monotext --%>
							<!-- attribute: <s:property value="#attribute.type" /> -->
							<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
					</s:else>
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
				</div><%-- form-group --%>
			</s:iterator><%-- attribute iterator end --%>
	</div>
	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.save" />
			</wpsf:submit>
		</div>
	</div>
	</s:form>
</div>
