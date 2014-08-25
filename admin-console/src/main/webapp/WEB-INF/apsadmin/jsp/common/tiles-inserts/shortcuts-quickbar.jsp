<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<wpsa:userShortcutsConfig var="userConfigVar" />

<ul class="menu horizontal">

<s:iterator var="topbarUserShortcutCodeVar" value="#userConfigVar.config" status="rowstatus">
<s:set var="topbarUserShortcutVar" />
<wpsa:shortcut key="%{#topbarUserShortcutCodeVar}" var="topbarUserShortcutVar" />

<li class="topbar-menusection-<s:property value="#topbarUserShortcutVar.menuSectionCode" />">

<s:if test="null != #topbarUserShortcutVar">

<s:set var="userShortcutSectionShortDescr"><s:if test="null != #topbarUserShortcutVar.menuSection.descriptionKey" ><s:text name="%{#topbarUserShortcutVar.menuSection.descriptionKey}" /></s:if><s:else><s:property value="%{#topbarUserShortcutVar.menuSection.description}" /></s:else></s:set>
<s:set var="userShortcutSectionLongDescr"><s:if test="null != #topbarUserShortcutVar.menuSection.longDescriptionKey" ><s:text name="%{#topbarUserShortcutVar.menuSection.longDescriptionKey}" /></s:if><s:else><s:property value="%{#topbarUserShortcutVar.menuSection.longDescription}" /></s:else></s:set>

<s:set var="userShortcutShortDescr"><s:if test="null != #topbarUserShortcutVar.descriptionKey" ><s:text name="%{#topbarUserShortcutVar.descriptionKey}" /></s:if><s:else><s:property value="%{#topbarUserShortcutVar.description}" /></s:else></s:set>
<s:set var="userShortcutLongDescr"><s:if test="null != #topbarUserShortcutVar.longDescriptionKey" ><s:text name="%{#topbarUserShortcutVar.longDescriptionKey}" /></s:if><s:else><s:property value="%{#topbarUserShortcutVar.longDescription}" /></s:else></s:set>

		<%-- SCRIVERE TAG PER AGGIUNGERE MAPPA PARAMETRI ALLA URL --%>
		<a href="<s:url action="%{#topbarUserShortcutVar.actionName}" namespace="%{#topbarUserShortcutVar.namespace}"><wpsa:paramMap map="#topbarUserShortcutVar.parameters" /></s:url>" 
			lang="en" 
			title="<s:property value="%{#userShortcutLongDescr}" escapeHtml="true" />">
				<img src="<wp:resourceURL/>administration/common/img/icons/16x16/topbar-<s:property value="#topbarUserShortcutVar.menuSectionCode" />.png" width="16" height="16" 
					alt="<s:property value="%{#userShortcutSectionShortDescr}" /> - " />
				<span class="toggle-ellipsis"><s:property value="%{#userShortcutShortDescr}" /></span>
		</a>
</s:if>
<s:else>
	<a href="<s:url action="main" namespace="/do" />" class="noborder outlineNone" title="<s:text name="note.goToMainPage" />"><img src="<wp:resourceURL/>administration/common/img/icons/16x16/topbar-<s:property value="#topbarUserShortcutVar.menuSectionCode" />.png" width="16" height="16" alt=" " /><span class="toggle-ellipsis">&nbsp;</span></a>
</s:else>

</li>
</s:iterator>
</ul>