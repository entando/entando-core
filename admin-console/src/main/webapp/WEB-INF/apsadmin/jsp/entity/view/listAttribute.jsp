<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set var="masterListAttributeTracer" value="#attributeTracer" />
<s:set var="masterListAttribute" value="#attribute" />
<s:if test="#attribute.getAttributeList(#lang.code).size() != 0">
	<div class="clearfix"></div>
	<ul class="media-list">
		<s:iterator value="#attribute.getAttributeList(#lang.code)" id="attribute" status="elementStatus">
			<s:set var="attributeTracer" value="#masterListAttributeTracer.getListElementTracer(#lang, #elementStatus.index)" />
			<s:set var="elementIndex" value="#elementStatus.index" />
			<li class="media padding-small-bottom padding-top-bottom">
		 		<div class="pull-left">
					<div class="media-object">
						<h3 class="display-block margin-none">
							<span class="label label-default">
								<s:property value="#elementStatus.count" />
							</span>
						</h3>
					</div>
				</div>
				<div class="media-body">
					<s:if test="#attribute.type == 'Boolean'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/booleanAttribute.jsp" />
					</s:if>
					<s:elseif test="#attribute.type == 'CheckBox'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/checkBoxAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Date'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/dateAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Enumerator'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Monotext'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monotextAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Number'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'ThreeState'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/threeStateAttribute.jsp" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Timestamp'">
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/timestampAttribute.jsp" />
					</s:elseif>
					<s:else>
						<!-- unknown attribute '<s:property value="#attribute.name" />' (type: <s:property value="#attribute.type" />) -->
					</s:else>
				</div>
			</li>
			<s:set var="attributeTracer" value="#masterListAttributeTracer" />
			<s:set var="attribute" value="#masterListAttribute" />
		</s:iterator>
	</ul>
</s:if>
<s:else>
	<s:text name="label.attribute.listEmpty" />
</s:else>
