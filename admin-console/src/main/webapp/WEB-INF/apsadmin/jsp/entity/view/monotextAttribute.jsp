<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="#lang.default">
	<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
</s:if>
<s:else>
	<s:if test="#attributeTracer.listElement">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
	</s:if>
	<s:else>
	<s:text name="EntityAttribute.monolang.defaultValue" />
	</s:else>
</s:else>