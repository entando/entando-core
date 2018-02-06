<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%/*
This converter shall always return a json object like this: 
	var ENTANDO_MODEL_VOCABULARY = {
			"$content": {
				"AttributeA": {
					"propertyA": null,
					"propertyB": null,
				},
				"AttributeB": {
					"propertyC": null,
					"propertyD": null,
				}
			},
			"$i18n":  {
				"getLabel(\"\")":  null
			}
		};
*/%>
<s:set var="contentPrototype" value="contentPrototype" />
	"$content": {
	<s:iterator value="allowedPublicContentMethods" var="contentMethod" status="contentMethodStatus">
		"<s:property value="#contentMethod" escapeJavaScript="false" escapeHtml="false" />": null<s:if test="(!#contentMethodStatus.last || (#contentPrototype != null && #contentPrototype.attributeList.size>0))">,</s:if>
	</s:iterator>
	<s:if test="#contentPrototype != null">
	<s:iterator value="#contentPrototype.attributeList" var="attribute" status="attributeStatus">
		<s:set var="allowedMethods" value="%{getAllowedAttributeMethods(#contentPrototype, #attribute.name)}" />
		"<s:property value="#attribute.name" />": <s:if test="#allowedMethods.empty">null</s:if><s:else>{
		<s:iterator value="#allowedMethods" var="method" status="status"> 
			"<s:property value="#method" escapeJavaScript="false" escapeHtml="false" />": null<s:if test="!#status.last">,</s:if>
		</s:iterator>
		}<s:if test="!#attributeStatus.last">,</s:if>
		</s:else>
	</s:iterator>
	</s:if>
	},
	"$i18n": {
		"getLabel(\"<LABEL_CODE>\")": null, 
		"getLabelWithParams(\"<LABEL_CODE>\").addParam(\"<PARAM_KEY>\", \"<PARAM_VALUE>\")": null 
	},
	"$info": {
		"getConfigParameter(\"<PARAM_NAME>\")": null, 
		"getCurrentPage()": null, 
		"getCurrentLang()": null, 
		"getCurrentWidget()": null
	}
