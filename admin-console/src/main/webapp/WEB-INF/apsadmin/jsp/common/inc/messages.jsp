<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="hasFieldErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
        <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
        <ul class="margin-base-vertical">
            <s:iterator value="fieldErrors">
                <s:iterator value="value">
                    <li><s:property escapeHtml="false" /></li>
                </s:iterator>
            </s:iterator>
        </ul>
    </div>
</s:if>
<s:if test="hasActionErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
        <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
        <ul class="margin-base-vertical">
            <s:iterator value="actionErrors">
                <li><s:property escapeHtml="false" /></li>
            </s:iterator>
        </ul>
    </div>
</s:if>
<s:if test="hasActionMessages()">
    <div class="alert alert-info alert-dismissable">
        <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
        <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>	
        <ul class="margin-base-vertical">
            <s:iterator value="actionMessages">
                <li><s:property escapeHtml="false" /></li>
            </s:iterator>
        </ul>
    </div>
</s:if>