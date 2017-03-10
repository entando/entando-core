<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="col-sm-8">
<s:if test="#lang.default">
	<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
</s:if>
<s:else>
	<s:if test="#attributeTracer.listElement">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
	</s:if>
	<s:else>
            <span class=""><s:text name="note.editContent.doThisInTheDefaultLanguage.must" />.</span>
	</s:else>
</s:else>
</div>
