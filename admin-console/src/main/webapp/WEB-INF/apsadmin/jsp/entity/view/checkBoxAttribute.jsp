<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#lang.default || #attributeTracer.listElement">
	<s:set var="checkedValue" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
		<s:if test="%{#checkedValue}">
			<s:text name="label.yes"/>
		</s:if>
		<s:else>
			<s:text name="label.no"/>
		</s:else>
</s:if>
<s:else>
		<s:text name="EntityAttribute.monolang.defaultValue" />
</s:else>