<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li class="page-title-container"><s:text name="title.pageTree" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.pageTree" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.pageTree.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageSearchForm.jsp" />

<hr />
<div id="main" role="main">

    <p><s:text name="note.pageTree.intro" /></p>

    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.FieldErrors" /></strong>
            <ul>
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <div role="search">

        <s:form action="search" cssClass="action-form">
            <p class="sr-only"><wpsf:hidden name="pageCodeToken" /></p>
            <s:set var="pagesFoundVar" value="pagesFound" />
            <s:if test="%{#pagesFoundVar != null && #pagesFoundVar.isEmpty() == false}">
                <wpsa:subset source="#pagesFoundVar" count="10"
                             objectName="pagesFoundGroupsVar" advanced="true" offset="5">
                    <s:set var="group" value="#pagesFoundGroupsVar" />
                    <div class="mt-20">
                        <table class="table table-striped table-bordered table-hover no-mb">
                            <thead>
                                <tr>
                                    <th><s:text name="label.code" /></th>
                                        <%--<th><s:text name="label.title" /></th>--%>
                                    <th><s:text name="label.fullPath" /></th>
                                    <th style="width: 8%;"><s:text name="label.state" /></th>
                                    <th style="width: 8%;"><s:text name="label.pageInMenu" /></th>
                                    <th class="text-center table-w-5"><s:text name="label.actions" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator var="pageVar">
                                    <tr>
                                        <td><s:property value="#pageVar.code" /></td>
                                        <%-- <td><s:property value="getTitle(#pageVar.code, #pageVar.titles)" /></td> --%>
                                        <td><s:property value="%{#pageVar.getFullTitle(currentLang.code)}" /></td>
                                        <td class="text-center">
                                            <span class="statusField">
                                                <s:if test="%{!#pageVar.isOnline()}">
                                                    <i class="fa fa-circle gray" aria-hidden="true" title="Draft"></i>
                                                </s:if>
                                                <s:elseif test="%{#pageVar.isChanged()}">
                                                    <i class="fa fa-circle yellow" aria-hidden="true" title="Online&#32;&ne;&#32;Draft"></i>
                                                </s:elseif>
                                                <s:else>
                                                    <i class="fa fa-circle green" aria-hidden="true" title="Online"></i>
                                                </s:else>
                                            </span>
                                        </td>
                                        <td class="text-center">
                                            <s:if test="%{#pageVar.isOnline() && #pageVar.getMetadata().isShowable()}">
                                                <s:text name="label.pageInMenu.displayed" />
                                            </s:if>
                                            <s:else>
                                                <s:text name="label.pageInMenu.notdisplayed" />
                                            </s:else>
                                        </td>

                                        <td class=" table-view-pf-actions text-center">
                                            <div class="dropdown dropdown-kebab-pf">
                                                <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                                    <span class="fa fa-ellipsis-v"></span></button>
                                                <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight">
                                                    <li>
                                                        <a title="<s:text name="page.options.viewInTree" />"
                                                           href="<s:url action="viewTree"><s:param name="selectedNode" value="%{#pageVar.code}" /></s:url>">
                                                            <span><s:text name="label.viewInTree" /></span></a>
                                                    </li>
                                                    <li>
                                                        <a title="<s:text name="page.options.new" />"
                                                           href="<s:url action="new"><s:param name="selectedNode" value="%{#pageVar.code}" /></s:url>">
                                                            <span><s:text name="label.add" /></span></a>
                                                    </li>
                                                    <li>
                                                        <a title="<s:text name="page.options.modify" />"
                                                           href="<s:url action="edit"><s:param name="selectedNode" value="%{#pageVar.code}" /></s:url>">
                                                            <span><s:text name="label.edit" /></span></a>
                                                    </li>
                                                    <li>
                                                        <a title="<s:text name="page.options.configure" />"
                                                           href="<s:url action="doConfigure"><s:param name="selectedNode" value="%{#pageVar.code}" /></s:url>">
                                                            <span><s:text name="label.configure" /></span></a>
                                                    </li>
                                                    <li>
                                                        <a title="<s:text name="page.options.detail" />"
                                                           href="<s:url action="detail"><s:param name="selectedNode" value="%{#pageVar.code}" /></s:url>">
                                                            <span><s:text name="label.view" /></span></a>
                                                    </li>
                                                    <li>
                                                        <a title="<s:text name="page.options.copy" />"
                                                           href="<s:url action="copy"><s:param name="selectedNode" value="%{#pageVar.code}" /></s:url>">
                                                            <span><s:text name="title.clonePage" /></span></a>
                                                    </li>
                                                    <s:if test="%{#pageVar.online}">
                                                        <li>
                                                            <a title="<s:text name="page.options.offline" />"
                                                               href="<s:url action="checkSetOffline"><s:param name="selectedNode" value="%{#pageVar.code}" /></s:url>">
                                                                <span><s:text name="page.options.offline" /></span></a>
                                                        </li>
                                                    </s:if>
                                                    <s:else>
                                                        <li>
                                                            <a title="<s:text name="page.options.delete" />"
                                                               href="<s:url action="trash"><s:param name="selectedNode" value="%{#pageVar.code}" /></s:url>">
                                                                <span><s:text name="label.delete" /></span></a>
                                                        </li>
                                                    </s:else>
                                                    <s:if test="%{!#pageVar.online || #pageVar.changed}">
                                                        <li>
                                                            <a title="<s:text name="page.options.online" />"
                                                               href="<s:url action="checkSetOnline"><s:param name="selectedNode" value="%{#pageVar.code}" /></s:url>">
                                                                <span><s:text name="page.options.online" /></span></a>
                                                        </li>
                                                    </s:if>
                                                </ul>
                                            </div>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                    </div>
                    <div class="content-view-pf-pagination clearfix">
                        <div class="form-group">
                            <span><s:include
                                    value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                            <div class="mt-5">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                            </div>
                        </div>
                    </div>
                </wpsa:subset>
            </s:if>
            <s:else>
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <strong><s:text name="noPages.found" /></strong>
                </div>
            </s:else>
        </s:form>
    </div>
</div>
