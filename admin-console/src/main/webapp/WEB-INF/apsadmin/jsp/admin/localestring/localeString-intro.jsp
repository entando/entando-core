<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="targetNS" value="%{'/do/LocaleString'}" />
<h1><s:text name="title.languageAdmin.labels" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main" role="main">

<div class="intro localeString">
	<p><s:text name="note.localeString.intro.html" /></p>	
</div>

</div>