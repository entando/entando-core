<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="userShortcutCode" value="%{shortcutCode}" />
<s:set var="position" value="%{position}" />
<s:include value="/WEB-INF/apsadmin/jsp/common/shortcut/inc/shortcut-item.jsp" />
