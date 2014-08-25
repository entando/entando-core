<%@ taglib prefix="s" uri="/struts-tags" %>

<p class="sr-only"><s:text name="note.monolist.intro" /></p>

<s:if test="#attribute.attributes.size() != 0">
<ul>
</s:if>
<s:set name="masterListAttributeTracer" value="#attributeTracer" />
<s:set name="masterListAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" id="attribute" status="elementStatus">
<s:set name="attributeTracer" value="#masterListAttributeTracer.getMonoListElementTracer(#elementStatus.index)"></s:set>

<s:set name="elementIndex" value="#elementStatus.index" />

	<s:if test="#attribute.type == 'Composite'">
<li class="contentAttributeBox">	
		<p>
			<span class="important"><s:property value="#elementStatus.index + 1" />&#32;<s:text name="label.compositeAttribute.element" />:</span>
		</p>
	</s:if>
	<s:else>
<li>	
		<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#elementStatus.index + 1" /></label>
	</s:else>
	<s:if test="#attribute.type == 'Monotext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monotextAttribute.jsp" />
	</s:if>
	<s:elseif test="#attribute.type == 'Text'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Longtext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/longtextAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Date'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/dateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Timestamp'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/timestampAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Number'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Hypertext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/hypertextAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Enumerator'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Composite'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/compositeAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Boolean'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/booleanAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'ThreeState'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/threeStateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'CheckBox'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/checkBoxAttribute.jsp" />
	</s:elseif>
</li>
</s:iterator>

<s:set name="attributeTracer" value="#masterListAttributeTracer" />
<s:set name="attribute" value="#masterListAttribute" />
<s:set name="elementIndex" value="" />
<s:if test="#attribute.attributes.size() != 0">
</ul>
</s:if>
<s:else><s:text name="label.attribute.listEmpty" /></s:else>