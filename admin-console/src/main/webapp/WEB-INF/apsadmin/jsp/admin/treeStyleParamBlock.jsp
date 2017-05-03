<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- TODO: page to be deleted - now the content is in categorySettings.jsp-->
<label class="btn btn-default <s:if test="systemParams[#paramName] == 'classic'"> active</s:if>">
	<input type="radio" id="admin-settings-area-<s:property value="#paramName"/>_classic" name="<s:property value="#paramName"/>" value="classic" <s:if test="systemParams[#paramName] == 'classic'">checked="checked"</s:if> />
	<s:text name="treeStyle.classic" />
</label>
<label class="btn btn-default <s:if test="systemParams[#paramName] == 'request'"> active</s:if>">
	<input type="radio" id="admin-settings-area-<s:property value="#paramName"/>_request" name="<s:property value="#paramName"/>" value="request" <s:if test="systemParams[#paramName] == 'request'">checked="checked"</s:if> />
	<s:text name="treeStyle.request" />
</label>
