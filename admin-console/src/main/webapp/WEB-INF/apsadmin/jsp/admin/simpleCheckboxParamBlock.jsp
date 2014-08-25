<%@ taglib prefix="s" uri="/struts-tags" %>
<input type="checkbox" class="radiocheck" 
	   id="<s:property value="%{'admin-settings-area-'+#paramName}" />" 
	   name="<s:property value="#paramName"/>" value="true" <s:if test="systemParams[#paramName]">checked="checked"</s:if> />
