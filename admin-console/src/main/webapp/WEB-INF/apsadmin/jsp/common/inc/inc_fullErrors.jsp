<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="hasErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.ActionErrors" /></strong>
        <ul>
            <s:if test="hasActionErrors()">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                </s:if>
                <s:if test="hasFieldErrors()">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:iterator>
                </s:if>
        </ul>
    </div>
</s:if>
