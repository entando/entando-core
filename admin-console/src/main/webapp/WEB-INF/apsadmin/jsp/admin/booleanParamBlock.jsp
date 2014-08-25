<%@ taglib prefix="s" uri="/struts-tags" %>
	<label class="btn btn-default <s:if test="systemParams[#paramName]"> active</s:if>">
		<input type="radio" class="radiocheck" id="admin-settings-area-<s:property value="#paramName"/>_true" name="<s:property value="#paramName"/>" value="true" <s:if test="systemParams[#paramName]">checked="checked"</s:if> />
		<s:text name="label.yes" />
	</label>
	<label class="btn btn-default <s:if test="systemParams[#paramName] == 'false'"> active</s:if>">
		<input type="radio" class="radiocheck" id="admin-settings-area-<s:property value="#paramName"/>_false" name="<s:property value="#paramName"/>" value="false" <s:if test="systemParams[#paramName] == 'false'">checked="checked"</s:if> />
		<s:text name="label.no" />
	</label>