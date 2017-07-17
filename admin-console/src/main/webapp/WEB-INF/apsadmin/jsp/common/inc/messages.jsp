<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="hasFieldErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><span class="pficon pficon-close"></span></button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.FieldErrors" /></strong>
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
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><span class="pficon pficon-close"></span></button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.ActionErrors" /></strong>
        <ul class="margin-base-vertical">
            <s:iterator value="actionErrors">
                <li><s:property escapeHtml="false" /></li>
            </s:iterator>
        </ul>
    </div>
</s:if>
<s:if test="hasActionMessages()">
    <div class="alert alert-info alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><span class="pficon pficon-close"></span></button>
        <span class="pficon pficon-info"></span>
        <strong><s:text name="messages.confirm" /></strong>	
        <ul class="margin-base-vertical">
            <s:iterator value="actionMessages">
                <li><s:property escapeHtml="false" /></li>
            </s:iterator>
        </ul>
    </div>
</s:if>