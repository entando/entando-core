<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:form cssClass="form-horizontal">
    <p class="sr-only">
        <wpsf:hidden name="code" />
    </p>
    <%-- referenced pages --%>


    <div class="">

        <label class="col-sm-2 control-label"><s:text name="title.pageModel.referencedPages" /></label>
        <div class="col-sm-10">


            <s:if test="null != references['PageManagerUtilizers']">
                <wpsa:subset source="references['PageManagerUtilizers']" count="10" objectName="pageReferences" advanced="true" offset="5" pagerId="pageManagerReferences">
                    <s:set name="group" value="#pageReferences" />
                    <div class="text-center">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                        <%--<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />--%>
                    </div>
                    <table class="table table-striped table-bordered" id="pageListTable">
                        <tr>
                            <th><s:text name="label.page" /></th>
                            <th class="text-center" style="width: 20px">
                                <s:text name="label.actions" />
                            </th>
                        </tr>
                        <s:iterator var="currentPageVar">
                            <s:set var="canEditCurrentPage" value="%{false}" />
                            <s:set var="currentPageGroup" value="#currentPageVar.group" scope="page" />
                            <wp:ifauthorized groupName="${currentPageGroup}" permission="managePages"><s:set var="canEditCurrentPage" value="%{true}" /></wp:ifauthorized>
                                <tr>
                                    <td> <s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" /></td>
                                <td class="text-center text-nowrap">
                                    <div class="dropdown dropdown-kebab-pf">
                                        <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <span class="fa fa-ellipsis-v"></span>
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-right">								
                                            <s:if test="#canEditCurrentPage">
                                                <li>
                                                    <a href="<s:url namespace="/do/Page" action="viewTree"><s:param name="selectedNode" value="#currentPageVar.code" /></s:url>"
                                                       title="<s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />">
                                                        <span class="sr-only"><s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" /></span>
                                                        <s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="<s:url namespace="/do/Page" action="configure"><s:param name="pageCode" value="#currentPageVar.code" /></s:url>"
                                                       title="<s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />">
                                                        <span class="sr-only"><s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" /></span>
                                                        <s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />
                                                    </a>
                                                </li>
                                            </s:if>
                                        </ul>
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
                <p class="margin-none"><s:text name="note.pageModel.referencedPages.empty" /></p>
            </s:else>
        </div>
    </div>
    <%-- hoookpoint core.groupReferences --%>
    <wpsa:hookPoint key="core.pageModelReferences" objectName="hookPointElements_core_pageModelReferences">
        <s:iterator value="#hookPointElements_core_pageModelReferences" var="hookPointElement">
            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
        </s:iterator>
    </wpsa:hookPoint>
</s:form>
