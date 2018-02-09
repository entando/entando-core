<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.app" />
    </li>
    <li>
        <s:text name="breadcrumb.jacms" />
    </li>
    <li>
        <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
            <s:text name="jacms.menu.contentAdmin" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.contentEditing" />
    </li>
</ol>

<s:form cssClass="form-horizontal">

    <div id="main" role="main">

        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/include/snippet-content.jsp" />

        <div class="message message_error">
            <h2><s:text name="message.title.ActionErrors" /></h2>
            <p><s:text name="message.note.resolveReferences" />:</p>
        </div>

        <s:if test="references['jacmsContentManagerUtilizers']">
            <s:set var="referencingContentsId" value="references['jacmsContentManagerUtilizers']" />
            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/portal/include/referencingContents.jsp" />
        </s:if>

        <div class="panel panel-default">
            <div class="panel-heading"><h3 class="margin-none"><s:text name="title.referencingPages" /></h3></div>
            <div class="panel-body">
                <s:if test="null != references['CmsPageManagerWrapperUtilizers']">
                    <wpsa:subset source="references['CmsPageManagerWrapperUtilizers']" count="10" objectName="pageReferences" advanced="true" offset="5" pagerId="pageManagerReferences">
                        <s:set var="group" value="#pageReferences" />
                        <div class="text-center">
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                        </div>
                        <table class="table table-bordered" id="pageListTable">
                            <tr>
                                <th class="text-center col-xs-5 col-sm-3 col-md-2 col-lg-2">
                                    <abbr title="<s:text name="label.actions" />">&ndash;</abbr>
                                </th>
                                <th><s:text name="label.page" /></th>
                            </tr>
                            <s:iterator var="currentPageVar">
                                <s:set var="canEditCurrentPage" value="%{false}" />
                                <s:set var="currentPageGroup" value="#currentPageVar.group" scope="page" />
                                <wp:ifauthorized groupName="${currentPageGroup}" permission="managePages"><s:set var="canEditCurrentPage" value="%{true}" /></wp:ifauthorized>
                                    <tr>
                                        <td class="text-center text-nowrap">
                                        <s:if test="#canEditCurrentPage">
                                            <div class="btn-group btn-group-xs">
                                                <a class="btn btn-default"
                                                   href="<s:url namespace="/do/Page" action="viewTree"><s:param name="selectedNode" value="#currentPageVar.code" /></s:url>"
                                                   title="<s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />"><span class="icon fa fa-folder"></span><span class="sr-only"><s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" /></span></a>
                                                <a class="btn btn-default"
                                                   href="<s:url namespace="/do/Page" action="configure"><s:param name="pageCode" value="#currentPageVar.code" /></s:url>"
                                                   title="<s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />"><span class="icon fa fa-cog"></span><span class="sr-only"><s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" /></span></a>
                                            </div>
                                        </s:if>
                                    </td>
                                    <td>
                                        <s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                        <div class="text-center">
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                        </div>
                    </wpsa:subset>
                </s:if>
                <s:else>
                    <p class="margin-none"><s:text name="note.group.referencedPages.empty" /></p>
                </s:else>
            </div>
        </div>

        <wpsa:hookPoint key="jacms.contentReferences" objectName="hookPointElements_jacms_contentReferences">
            <s:iterator value="#hookPointElements_jacms_contentReferences" var="hookPointElement">
                <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
            </s:iterator>
        </wpsa:hookPoint>

    </div>

</s:form>
