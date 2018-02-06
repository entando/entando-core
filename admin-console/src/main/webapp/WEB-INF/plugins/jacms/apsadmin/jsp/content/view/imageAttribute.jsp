<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="currentResource" value="#attribute.resources[#lang.code]" />
<s:set var="defaultResource" value="#attribute.resource" />
<s:if test="#lang.default"><%-- default lang --%>
    <s:if test="#currentResource != null">
        <img
            class="img-thumbnail"
            src="<s:property value="#defaultResource.getImagePath('1')"/>"
            alt="<s:property value="#defaultResource.descr"/>" />
        <span class="sr-only">
            <abbr
                title="<s:text name="label.img.text.long" />">
                <s:text name="label.img.text.short" /></abbr>:&#32;
        </span>
        <s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
    </s:if>
    <s:else>
        <span class="text-muted"><s:text name="label.none" /></span>
    </s:else>
</s:if>
<s:else><%-- other languages --%>
    <s:if test="#defaultResource == null"><%-- resource null --%>
        <s:text name="label.attribute.resources.null" />
    </s:if>
    <s:else>
        <s:if test="#currentResource != null">
            <img class="img-thumbnail" src="<s:property value="#currentResource.getImagePath('1')"/>" alt="<s:property value="#currentResource.descr"/>" />
            <span class="sr-only">
                <abbr title="<s:text name="label.img.text.long" />"> <s:text name="label.img.text.short" /></abbr>:&#32;
            </span>
        </s:if>
        <s:else>
            <img
                class="img-thumbnail"
                src="<s:property value="#defaultResource.getImagePath('1')"/>"
                alt="<s:property value="#defaultResource.descr"/>" />
            <span class="sr-only">
                <abbr
                    title="<s:text name="label.img.text.long" />">
                    <s:text name="label.img.text.short" /></abbr>:&#32;
            </span>
        </s:else>
        <s:if test="#attribute.getTextForLang(#lang.code)!=null">
            <s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
        </s:if>
        <s:else>
            <span class="text-muted"><s:text name="label.attribute.resources.null" /></span>
        </s:else>
    </s:else>
</s:else>
