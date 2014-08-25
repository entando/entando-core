<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="%{#lang.default || #attributeTracer.listElement}">
	<s:if test="%{#attribute.value!=null}">
		<abbr title="<s:property value="#attribute.getFormattedDate('EEEE dd MMMM yyyy, HH:mm')" />">
			<s:property value="#attribute.getFormattedDate('dd/MM/yyyy')" />&#32;&ndash;&#32;
			<s:property value="#attribute.getFormattedDate('HH')" />:<s:property value="#attribute.getFormattedDate('mm')" />
		</abbr>
	</s:if>
	<s:else><s:text name="label.none" /></s:else>
</s:if>
<s:else>
	<s:text name="EntityAttribute.monolang.defaultValue" />
</s:else>