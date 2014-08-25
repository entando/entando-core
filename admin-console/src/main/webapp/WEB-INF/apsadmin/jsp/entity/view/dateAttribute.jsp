<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#lang.default || #attributeTracer.listElement">
	<s:if test="#attribute.value==null">
		<s:text name="label.none" />
	</s:if>
	<s:else>
		<s:property value="#attribute.getFormattedDate('dd/MM/yyyy')" />
	</s:else>
</s:if>
<s:else>
	<s:text name="EntityAttribute.monolang.defaultValue" />
</s:else>
