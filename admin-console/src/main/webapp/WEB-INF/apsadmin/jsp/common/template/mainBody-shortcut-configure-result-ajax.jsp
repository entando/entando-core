<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.*"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- just for test, when the action will work this "shortcut" will be valorized by the action itself. --%>
<%-- please kill me, and make this shortcut live --%>
<wpsa:shortcut key="${param.shortcutCode}" var="shortcut"/>
<c:set var="shortcut" value="${shortcut}" scope="request" />
<s:set var="userShortcut" value="#request.shortcut" />
<%-- please kill me, and make this shortcut live --%>
<%-- just for test, when the action will work this "shortcut" will be valorized by the action itself. --%>
{
<s:if test="%{#userShortcut!=null}">
		"result": <s:if test="%{hasFieldErrors()||hasActionErrors()}">false</s:if><s:else>true</s:else>,
		"position": <s:property value="%{#parameters['position']}" />,
		"shortcut": {
			"id": "<s:property value="#userShortcut.id" />",
			"menuSection": {
				<s:set var="userShortcutSectionShortDescr"><s:if test="null != #userShortcut.menuSection.descriptionKey" ><s:text name="%{#userShortcut.menuSection.descriptionKey}" /></s:if><s:else><s:property value="%{#userShortcut.menuSection.description}" /></s:else></s:set>
				"description": "<s:property value="#userShortcutSectionShortDescr" />",
				<s:set var="userShortcutSectionLongDescr"><s:if test="null != #userShortcut.menuSection.longDescriptionKey" ><s:text name="%{#userShortcut.menuSection.longDescriptionKey}" /></s:if><s:else><s:property value="%{#userShortcut.menuSection.longDescription}" /></s:else></s:set>
				"longDescription": "<s:property value="#userShortcutSectionLongDescr" />",
				"id": "<s:property value="#userShortcut.menuSection.id" />"
			},
			<s:set var="userShortcutShortDescr"><s:if test="null != #userShortcut.descriptionKey" ><s:text name="%{#userShortcut.descriptionKey}" /></s:if><s:else><s:property value="%{#userShortcut.description}" /></s:else></s:set>
			"shortDescr": "<s:property value="#userShortcutShortDescr" />",
			<s:set var="userShortcutLongDescr"><s:if test="null != #userShortcut.longDescriptionKey" ><s:text name="%{#userShortcut.longDescriptionKey}" /></s:if><s:else><s:property value="%{#userShortcut.longDescription}" /></s:else></s:set>
			"longDescr": "<s:property value="#userShortcutLongDescr" />",
			"href": "<s:url action="%{#userShortcut.actionName}" namespace="%{#userShortcut.namespace}"><wpsa:paramMap map="#userShortcut.parameters" /></s:url>",
			"menuSectionCode": "<s:property value="%{#userShortcut.menuSectionCode}" />",
			"requiredPermission": "<s:url action="%{#userShortcut.requiredPermission}" />",
			"source": "<s:url action="%{#userShortcut.source}" />",
			"namespace": "<s:url action="%{#userShortcut.namespace}" />",
			"actionName": "<s:url action="%{#userShortcut.actionName}" />"<s:if test="#userShortcut.parameters!=null">,
			"parameters": {
				<s:iterator value="#userShortcut.parameters" var="parameter" status="status">
					"<s:property value="#parameter.key" />": "<s:property value="#parameter.value}" />"<s:if test="!#status.last">,</s:if>
				</s:iterator>
			}
			</s:if>
		}
</s:if>
<s:else>
		"result": false,
		"messages": [
		<s:if test="hasFieldErrors()">
			<s:iterator value="fieldErrors" status="fieldStatus">
				<s:iterator value="value" status="valueStatus">
					"<s:property escapeHtml="false" escapeJavaScript="true" />"<s:if test="%{!(#valueStatus.last)}">,</s:if>
				</s:iterator><s:if test="%{!(#fieldStatus.last)}">,</s:if>
			</s:iterator>
		</s:if>
		<s:if test="hasActionErrors()">
			<s:if test="hasFieldErrors()">,</s:if>
			<s:iterator value="actionErrors" status="valueStatus">
				"<s:property escapeHtml="false" escapeJavaScript="true" />"<s:if test="%{!(#valueStatus.last)}">,</s:if>
			</s:iterator>
		</s:if>	
		]
</s:else>
}