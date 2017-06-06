<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Category" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.categoryManagement" />">
            <s:text name="title.categoryManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.categoryReference" />
    </li>
</h1>

<h1 class="page-title-container">
    <s:text name="title.categoryReference" />
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">

    <s:if test="serviceStatus == 0">

        <s:form action="executeMoveTree">
            <p class="sr-only">
                <wpsf:hidden name="selectedNode"/>
                <wpsf:hidden name="parentCategoryCode"/>
            </p>
            <div class="alert alert-warning">
                <p><s:text name="message.note.move.alertReferences" /></p>

                <div class="text-center">
                    <wpsf:submit type="button" cssClass="btn btn-warning">
                        <s:text name="label.confirm" />
                    </wpsf:submit>
                    <a class="btn btn-link" href="<s:url action="viewTree" />">
                        <s:text name="note.goToSomewhere" />:&#32;<s:text name="title.categoryTree" />
                    </a>
                </div>
            </div>
        </s:form>

        <s:set var="categoryInfoReferencesExtraPaginatorParamMap" value="#{'parentCategoryCode': parentCategoryCode}" />
        <s:include value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo-references.jsp" />
    </s:if>
    <s:elseif test="serviceStatus == 1">
        <s:iterator var="serviceStatusMap">
            <p>
                <s:property value="key"/> : <s:property value="value"/>
            </p>
        </s:iterator>
    </s:elseif>
    <s:else>
        INVALID_STATUS: <s:property value="serviceStatus" />
    </s:else>
</div>

