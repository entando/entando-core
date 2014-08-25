<%@ taglib prefix="s" uri="/struts-tags" %>

<h1><s:text name="title.porting22" /></h1>
<div id="main" role="main">
<s:form namespace="/do/jacms/Resource/Porting22" action="executeRefresh" >

<p><s:text name="note.porting22.intro" />&#32;<em class="important"><s:property value="resourcesId.size()" /></em>&#32;<s:text name="note.porting22.outro" /></p>

<p class="centerText">
	<wpsf:submit value="%{getText('label.confirm')}" cssClass="button" />
</p>

</s:form>
</div>