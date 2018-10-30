<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jacms/ContentModel" />">
            <s:text name="title.contentModels" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.contentModels.remove" />
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.contentModels.remove" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div id="main" role="main">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.ActionErrors" /> </strong>
        <ul class="margin-base-vertical">
            <li><s:text name="contentModel.tip" />
            </li>
        </ul>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="margin-none">
                <s:text name="title.contentModels.references" />
            </h3>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-bordered table-hover no-mb">
                <tr>
                    <th>
                        <s:text name="label.page" />
                    </th>
                    <th class="text-center">
                        <s:text name="contentModel.reference.pageStatus" />
                    </th>
                    <th>
                        <s:text name="contentModel.reference.widgetPosition" />
                    </th>
                    <th>
                        <s:text name="contentModel.reference.widgetTitle" />
                    </th>
                    <th>
                        <s:text name="contentModel.reference.contents"/>
                    </th>
                    <th class="table-w-5 text-center">
                        <s:text name="label.actions" />
                    </th>
                </tr>
                <s:iterator var="reference" value="contentModelReferences">
                    <s:set var="page" value="%{getPage(#reference)}">
                    </s:set>
                    <s:set var="widget" value="%{getWidget(#reference)}">
                    </s:set>
                    <tr>
                        <td>
                            <s:property value="#page.titles[currentLang.code]" />
                        </td>
                        <td class="text-center">
                            <span class="statusField">
                                <s:if test="!#reference.online"><i class="fa fa-circle yellow" aria-hidden="true" title="Draft"></i></s:if>
                                <s:elseif test="#reference.online"><i class="fa fa-circle green" aria-hidden="true" title="Online"></i></s:elseif>
                                </span>
                            </td>
                            <td>
                            <s:property value="#reference.widgetPosition" />
                        </td>
                        <td>
                            <s:property value="%{getWidgetTitle(#reference, currentLang.code)}" />
                        </td>
                        <td>
                            <s:if test="%{#widget.getType().getCode() == 'content_viewer_list'}">
                                * <code><s:property value="%{#widget.getConfig().get('contentType')}" /></code>
                            </s:if>
                            <s:else>
                                <s:iterator var="contentId" value="#reference.contentsId" status="incr">
                                    <s:if test="#incr.index > 0">
                                        ,
                                    </s:if>
                                    <s:set var="content" value="%{getContentVo(#contentId)}">
                                    </s:set>
                                    <code><s:property value="#content.id" /></code>
                                    &#32;
                                    <s:property value="#content.descr" />
                                </s:iterator>
                            </s:else>
                        </td>
                        <wp:ifauthorized groupName="${page.group}">
                            <td class=" text-center table-view-pf-actions">
                                <div class="dropdown dropdown-kebab-pf">
                                    <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                        <span class="fa fa-ellipsis-v"></span>
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight">
                                        <li>
                                            <a href="<s:url action="configure" namespace="/do/Page"/>?pageCode=<s:property value="#page.code" />">
                                                <span class="sr-only">
                                                    <s:text name="label.configure" />&#32;
                                                    <s:property value="#page.titles[currentLang.code]" />
                                                </span>
                                                <s:text name="contentModel.reference.configurePage" />
                                            </a>
                                        </li>
                                        <s:if test="!#reference.online">
                                            <li>
                                                <a href="<s:url action="editFrame" namespace="/do/Page"/>?pageCode=<s:property value="#page.code" />&frame=<s:property value="#reference.widgetPosition" />">
                                                    <s:text name="contentModel.reference.widgetSettings" />
                                                </a>
                                            </li>
                                        </s:if>
                                    </ul>
                                </div>
                            </td>
                        </wp:ifauthorized>
                    </tr>
                </s:iterator>
            </table>
        </div>
    </div>
</div>
