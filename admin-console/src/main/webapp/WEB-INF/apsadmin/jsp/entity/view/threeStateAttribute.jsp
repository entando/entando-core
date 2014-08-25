<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#lang.default || #attributeTracer.listElement">
	<s:if test="%{#attribute.booleanValue == null}">
		<s:text name="label.bothYesAndNo"/>
	</s:if>
	<s:elseif test="%{#attribute.booleanValue != null && #attribute.booleanValue == true}">
		<s:text name="label.yes"/>
	</s:elseif>
	<s:elseif test="%{#attribute.booleanValue != null && #attribute.booleanValue == false}">
		<s:text name="label.no"/>
	</s:elseif>
</s:if>
<s:else>
	<s:text name="EntityAttribute.monolang.defaultValue" />
</s:else>