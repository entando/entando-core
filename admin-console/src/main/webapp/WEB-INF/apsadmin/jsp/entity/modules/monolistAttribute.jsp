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
			<s:if test="#lang.default">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_operationModule.jsp" />
			</s:if>
		</p>
	</s:if>
	<s:else>
<li>	
		<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" class="basic-mint-label"><s:property value="#elementStatus.index + 1" /></label>
	</s:else>	 
	
	<s:if test="#attribute.type == 'Monotext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
	</s:if>
	<s:elseif test="#attribute.type == 'Text'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Longtext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Date'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Timestamp'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/timestampAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Number'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Hypertext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Enumerator'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Composite'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/compositeAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Boolean'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'ThreeState'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'CheckBox'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
	</s:elseif>
	&#32;
	<s:if test="#attribute.type != 'Composite'">	
		<s:if test="#lang.default">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_operationModule.jsp" />
		</s:if>
	</s:if>
</li>
</s:iterator>

<s:set name="attributeTracer" value="#masterListAttributeTracer" />
<s:set name="attribute" value="#masterListAttribute" />
<s:set name="elementIndex" value="" />
<s:if test="#attribute.attributes.size() != 0">
</ul>
</s:if>

<s:if test="#lang.default">
<p><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_addElementButton.jsp" /></p>
</s:if>