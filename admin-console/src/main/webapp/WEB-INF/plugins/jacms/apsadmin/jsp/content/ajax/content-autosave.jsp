<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>{
	<s:set var="contentIdVar" value="%{content.id}" scope="page" />
	<c:set var="id">null</c:set>
	<c:if test="${!(empty contentIdVar)}">
		<c:set var="id">"<c:out value="${contentIdVar}" />"</c:set>
	</c:if>
	<s:set var="fieldErrorsVar">
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
							<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:else>&#32;</s:else>
	</s:set>
	<s:set var="lastEditorVar" value="content.lastEditor" scope="page" />
	<s:set var="lastEditorVar">
		<wp:ifauthorized permission="superuser" var="authorizedSuperUserVar" />
		<c:if test="${!empty lastEditorVar && authorizedSuperUserVar}"><a href="<s:url action="edit" namespace="/do/User"><s:param name="username" value="content.lastEditor"/></s:url>" title="<s:text name="label.edit" />: <s:property value="content.lastEditor" />"></c:if>
		<c:choose>
			<c:when test="${empty lastEditorVar || (lastEditorVar eq sessionScope.currentUser)}"><s:property value="content.lastEditor" /> <span class="text-muted">(<s:text name="label.you" />)</span></c:when>
			<c:otherwise><s:property value="content.lastEditor" /></c:otherwise>
		</c:choose>
		<c:if test="${!empty lastEditorVar && authorizedSuperUserVar}"></a></c:if>
	</s:set>
	"id": <c:out value="${id}" escapeXml="false" />,
	"hasFieldErrors": <s:property value="hasFieldErrors()" />,
	"fieldErrors": "<s:property value="#fieldErrorsVar" escapeJavaScript="true" escapeHtml="false" escapeXml="false" />",
	"fieldErrorsKeys": "<s:iterator value="fieldErrors"><s:iterator value="value"><s:property value="key" /> </s:iterator></s:iterator>",
	"version": "<s:property value="%{content.version}" />",
	"lastEditor": "<s:property value="#lastEditorVar" escapeJavaScript="true" escapeHtml="false" escapeXml="false" />"
}