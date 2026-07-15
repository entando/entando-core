<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#lang.default">
    <s:if test="%{null == #attribute.getMapKey()}" >&ndash;</s:if>
    <s:else>
        <p><label><s:text name="label.key" /></label>:&#32;<s:property value="#attribute.getMapKey()" escapeHtml="true" /></p>
        <p><label><s:text name="label.value" /></label>:&#32;<s:property value="#attribute.getMapValue()" escapeHtml="true" /></p>
    </s:else>
</s:if>
<s:else>
	<s:if test="#attributeTracer.listElement">
            <s:if test="%{null == #attribute.getMapKey()}" >&ndash;</s:if>
            <s:else>
                <p><label><s:text name="label.key" /></label>:&#32;<s:property value="#attribute.getMapKey()" escapeHtml="true" /></p>
                <p><label><s:text name="label.value" /></label>:&#32;<s:property value="#attribute.getMapValue()" escapeHtml="true" /></p>
            </s:else>
	</s:if>
	<s:else>
		<s:text name="EntityAttribute.monolang.defaultValue" />
	</s:else>
</s:else>