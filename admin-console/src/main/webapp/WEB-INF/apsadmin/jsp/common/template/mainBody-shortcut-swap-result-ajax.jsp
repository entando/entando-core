<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/javascript; charset=UTF-8" %>{
	"action": "swap",
	"positionTarget": "<c:out value="${param.positionTarget}" />",
	"positionDest": "<c:out value="${param.positionDest}" />",
	"result": <s:if test="%{hasFieldErrors()||hasActionErrors()}">false</s:if><s:else>true</s:else>,
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
}