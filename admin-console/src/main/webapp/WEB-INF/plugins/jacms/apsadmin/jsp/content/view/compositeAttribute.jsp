<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set value="masterCompositeAttributeTracer" value="#attributeTracer" />
<s:set value="masterCompositeAttribute" value="#attribute" />
<s:if test="#attribute.attributes.size() != 0">
	<div class="list-group">
		<s:iterator value="#attribute.attributes" var="attribute">
			<s:set value="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)" />
			<s:set value="parentAttribute" value="#masterCompositeAttribute" />
			<s:if test="null != #attribute.description"><s:set var="compositeElementLabelVar" value="#attribute.description" /></s:if>
			<s:else><s:set var="compositeElementLabelVar" value="#attribute.name" /></s:else>
			<div class="list-group-item">
				<div class="margin-small-bottom">
					<span class="list-group-item-heading">
						<span class="label label-default">
							<s:property value="#compositeElementLabelVar"/>
						</span>
					</span>
				</div>
				<div class="list-group-item-text">
					<s:if test="#attribute.type == 'Text'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
					</s:if>
					<s:elseif test="#attribute.type == 'Monotext'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monotextAttribute.jsp" />
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
					<s:elseif test="#attribute.type == 'Hypertext'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/hypertextAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Enumerator'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Number'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Image'">
						<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/imageAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Attach'">
						<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/attachAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Link'">
						<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/linkAttribute.jsp" />
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
				</div>
			</div>
		</s:iterator>
	</div>
</s:if>
<s:set value="attributeTracer" value="#masterCompositeAttributeTracer" />
<s:set value="attribute" value="#masterCompositeAttribute" />
<s:set value="parentAttribute" value="" />