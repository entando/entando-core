<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<s:if test="#lang.default">
    <s:set var="currentEnumeratorMapAttributeNameVar" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
    <wpsf:select name="%{#currentEnumeratorMapAttributeNameVar}" id="%{#currentEnumeratorMapAttributeNameVar}" 
                 headerKey="" headerValue="%{getText('label.none')}" listKey="key" listValue="value" 
                 list="#attribute.mapItems" value="%{#attribute.getText()}" cssClass="form-control" />
</s:if>
<s:else>
    <s:if test="#attributeTracer.listElement">
        <s:set var="currentEnumeratorMapAttributeNameVar" value="%{#attributeTracer.getFormFieldName(#attribute)}" />
        <wpsf:select name="%{#currentEnumeratorMapAttributeNameVar}" id="%{#currentEnumeratorMapAttributeNameVar}" 
                     headerKey="" headerValue="%{getText('label.none')}" 
                     list="#attribute.mapItems" listKey="key" listValue="value" 
                     value="%{#attribute.getText()}" cssClass="form-control" />
    </s:if>
    <s:else>
        <span class="form-control-static text-info"><s:text name="note.editContent.doThisInTheDefaultLanguage.must" />.</span>
    </s:else>
</s:else>