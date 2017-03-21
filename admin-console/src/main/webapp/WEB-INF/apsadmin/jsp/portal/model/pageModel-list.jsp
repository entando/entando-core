<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.ux_patterns" /></li>
    <li class="page-title-container"><s:text name="title.pageModelManagement" /></li>

</ol>

<h1 class="page-title-container">
    <s:text name="title.pageModelManagement" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div id="main">

    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <s:text name="messages.title.ActionErrors" />
            <ul class="margin-base-top">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <p>
        <a class="btn btn-primary"
           href="<s:url namespace="/do/PageModel" action="new" />">
            <s:text name="label.add" />
            </span>
        </a>
    </p>
    <s:set var="pageModelsVar" value="pageModels" />
    <s:if test="%{#pageModelsVar.size > 0}">

        <table class="table table-striped table-bordered table-hover">
            <tr>
                <th><s:text name="label.code" /></th> 
                <th><s:text name="label.description" /></th>
                <th class="text-center " style="width: 20px"><s:text name="label.actions" /></th>
            </tr>
            <s:iterator value="#pageModelsVar" var="pageModelVar">
                <tr>
                    <td><s:property value="#pageModelVar.code" /></td>
                    <td><s:property value="#pageModelVar.description" /></td>

                    <td class="text-center text-nowrap">

                        <div class="dropdown dropdown-kebab-pf">
                            <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-ellipsis-v"></span></button>
                            <ul class="dropdown-menu dropdown-menu-right">								
                                <li>
                                    <a 
                                        href="<s:url action="edit"><s:param name="code" value="#pageModelVar.code"/></s:url>"
                                        title="<s:text name="label.edit" />:&#32;<s:property value="#pageModelVar.description" />&#32;(<s:property value="#pageModelVar.code" />)" >
                                        <s:text name="label.edit" />&#32;<s:property value="#pageModelVar.description" />
                                        <span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#pageModelVar.description" /></span>
                                    </a>
                                </li>
                                <li>
                                    <a 
                                        href="<s:url action="details"><s:param name="code" value="#pageModelVar.code"/></s:url>"
                                        title="<s:text name="note.detailsFor" />:&#32;<s:property value="#pageModelVar.description" />&#32;(<s:property value="#pageModelVar.code" />)" >
                                        <s:text name="note.detailsFor" />:&#32;<s:property value="#pageModelVar.description" />
                                        <span class="sr-only"><s:text name="note.detailsFor" />:&#32;<s:property value="#pageModelVar.description" /></span>
                                    </a>
                                </li>
                                <li>
                                    <wpsa:hookPoint key="core.pageModel.list.action" objectName="hookPointElements_core_pageModel_list_actionVar">
                                        <s:iterator value="#hookPointElements_core_pageModel_list_actionVar" var="hookPointElementVar">
                                            <wpsa:include value="%{#hookPointElementVar.filePath}"></wpsa:include>
                                        </s:iterator>
                                    </wpsa:hookPoint>
                                </li>
                                <li>
                                    <a
                                        href="<s:url action="trash"><s:param name="code" value="#pageModelVar.code"/></s:url>"
                                        title="<s:text name="label.remove" />: <s:property value="#pageModelVar.code" />">
                                        <span class="sr-only"><s:text name="label.remove" />: <s:property value="#pageModelVar.description" /></span>
                                        <s:text name="label.remove" />: <s:property value="#pageModelVar.description" />
                                    </a>
                                </li>
                            </ul>
                        </div>

                    </td>

                </tr>
            </s:iterator>
        </table>

    </s:if>
    <s:else>
        <p>
            <s:text name="noPageModels.found" />
        </p>
    </s:else>

</div>
