<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#lang.default">
	<s:property value="%{#attribute.getText()}" />
</s:if>
<s:else>
	<s:if test="#attributeTracer.listElement">
		<s:property value="%{#attribute.getText()}" />
	</s:if>
	<s:else>
		<s:text name="EntityAttribute.monolang.defaultValue" />
	</s:else>
</s:else>