<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:if test="strutsAction != 2" >
<script src="<wp:resourceURL />administration/js/generate-code-from-title.js"></script>
<script>
$(document).ready(function () {
	generateCodeFromTitle("lang<wp:info key="defaultLang" />", 'categoryCode');
});
</script>
</s:if>