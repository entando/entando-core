<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<span><s:text name="note.searchIntro" />&#32;<s:property value="#group.size" />&#32;<s:text name="note.searchOutro" /> |
    <s:text name="label.page" /> <s:property value="#group.currItem" /> - <s:property value="#group.maxItem" /></span>