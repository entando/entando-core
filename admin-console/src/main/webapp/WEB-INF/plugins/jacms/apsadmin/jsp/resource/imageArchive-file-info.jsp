<%@ taglib prefix="s" uri="/struts-tags" %>
<%--<code>
    <s:set var="fileNameVar" value="#resource.masterFileName" />
    <s:if test='%{#fileNameVar.length()>24}'>
        <s:set var="fileNameVar" value='%{#fileNameVar.substring(0,10)+"..."+#fileNameVar.substring(#fileNameVar.length()-10)}' />
        <s:property value="#fileNameVar" />
    </s:if>
    <s:else>
        <s:property value="#fileNameVar" />
    </s:else>
</code>
<s:if test="%{#resource.mainGroup != null && !#resource.mainGroup.equals('free')}">
    <span class="text-muted icon fa fa-lock"></span>
</s:if>--%>
<s:set var="dimensionId" value="0"/>
<s:set var="resourceInstance" value='%{#resource.getInstance(#dimensionId,null)}'/>
<a href="<s:property value="%{#resource.getImagePath(#dimensionId)}" />" class="list-group-item">
    <span class="badge">
        <s:property value='#resourceInstance.fileLength.replaceAll(" ", "&nbsp;")' escapeXml="false" escapeHtml="false"
                    escapeJavaScript="false"/>
    </span>
    <s:text name="label.size.original"/>
</a>
<s:set var="dimensionId" value="null"/>
<s:set var="resourceInstance" value="null"/>
<s:iterator value="#imageDimensionsVar" var="dimInfo">
    <s:set var="dimensionId" value="#dimInfo.idDim"/>
    <s:set var="resourceInstance" value='%{#resource.getInstance(#dimensionId,null)}'/>
    <s:if test="#resourceInstance != null">
        <a href="<s:property value="%{#resource.getImagePath(#dimensionId)}"/>" class="list-group-item">
            <span class="badge">
                <s:property value='#resourceInstance.fileLength.replaceAll(" ", "&nbsp;")' escapeXml="false"
                            escapeHtml="false" escapeJavaScript="false"/>
            </span>
            <s:property value="#dimInfo.dimx"/>
            x<s:property value="#dimInfo.dimy"/>&nbsp;px
        </a>
    </s:if>
</s:iterator>