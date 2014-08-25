<%@ taglib prefix="s" uri="/struts-tags" %>
<select name="<s:property value="#paramName"/>" id="admin-settings-area-<s:property value="#paramName"/>" class="form-control">
	<s:iterator value="freePages" var="page">
		<option <s:if test="systemParams[#paramName] == #page.code">selected="selected"</s:if> 
			value="<s:property value="#page.code"/>"><s:if test="!#page.showable"> [i]</s:if><s:property value="#page.getShortFullTitle(currentLang.code)"/></option>
	</s:iterator>
</select>