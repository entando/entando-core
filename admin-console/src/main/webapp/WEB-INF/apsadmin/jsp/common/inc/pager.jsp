<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%-- DA CORREGGERE CON IMPLEMENTAZIONE PAGINATORE AVANZATO --%>

<s:if test="#group.size > #group.max">
	<s:if test="1 == #group.currItem">&laquo; <s:text name="label.prev" /></s:if>
	<s:else><a href="<s:property value="actionLink" />?item=<s:property value="#group.prevItem" />">&laquo; <s:text name="label.prev" /></a></s:else>
	<s:iterator value="#group.items" var="item">
		<s:if test="#item == #group.currItem">&#32;[<s:property />]&#32;</s:if>
		<s:else>&#32;<a href="<s:property value="actionLink" />?item=<s:property />"><s:property /></a>&#32;</s:else>
	</s:iterator>
	<s:if test="#group.maxItem == #group.currItem"><s:text name="label.next" /> &raquo;</s:if>
	<s:else><a href="<s:property value="actionLink" />?item=<s:property value="#group.nextItem" />"><s:text name="label.next" /> &raquo;</a></s:else>
</s:if>
