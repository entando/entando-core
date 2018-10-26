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
                    <th>
                        <s:text name="label.content"/>
                    </th>
                    <th class="table-w-5 text-center">
                        <s:text name="label.actions" />
                    </th>
                </tr>
                <s:iterator var="contentId" value="referencedContentsOnPages">
                    <s:iterator var="page" value="referencingPages[#contentId]">
                        <tr>
                            <s:set var="pageGroup" value="#page.group">
                            </s:set>
                            <td>
                                <s:property value="#page.titles[currentLang.code]" />
                            </td>
                            <td>
                                <s:set var="content" value="%{getContentVo(#contentId)}">
                                </s:set>
                                <s:property value="#content.id" />
                                <s:property value="#content.descr" />
                            </td>
                            <wp:ifauthorized groupName="${pageGroup}">
                                <td class=" text-center table-view-pf-actions">
                                    <div class="dropdown dropdown-kebab-pf">
                                        <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                            <span class="fa fa-ellipsis-v"></span>
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight">
                                            <li>
                                                <a href="<s:url action="new" namespace="/do/Page"/>?selectedNode=<s:property value="#page.code" />&amp;action:configure=true">
                                                    <span class="sr-only">
                                                        <s:text name="label.edit" />&#32;
                                                        <s:property value="#model.description" />
                                                    </span>
                                                    <s:text name="label.edit" />
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                            </wp:ifauthorized>
                        </tr>
                    </s:iterator>
                </s:iterator>
            </table>
        </div>
    </div>
</div>
