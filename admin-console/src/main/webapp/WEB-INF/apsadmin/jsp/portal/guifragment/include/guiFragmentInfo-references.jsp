<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:form cssClass="form-horizontal">
    <p class="sr-only">
        <wpsf:hidden name="name" />
    </p>
    <%-- referenced Fragments --%>

    <div class="form-group">
        <s:if test="null != references['GuiFragmentManagerUtilizers']">
            <p class="col-xs-12">
                <s:text name="title.guiFragment.referencedFragments" />
            </p>

            <div class="col-md-12">
                <wpsa:subset source="references['GuiFragmentManagerUtilizers']" count="10" objectName="guiFragmentReferencesVar" advanced="true" offset="5" pagerId="pageManagerReferences">
                    <s:set var="group" value="#guiFragmentReferencesVar" />
                    <div class="col-xs-12 no-padding table-nomargin-bottom">
                        <table class="table table-bordered table-hover table-treegrid"  id="guiFragListTable">
                            <thead>
                                <tr>
                                    <th class=""><s:text name="label.guiFragment" /></th>
                                    <th class="table-w-5"><s:text name="label.actions" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator var="currentGuiFragmentVar">
                                    <tr class="dl-horizontal dl-striped panel padding-base-top padding-base-bottom">
                                        <td><s:property value="#currentGuiFragmentVar.code" /></td>
                                        <td class="text-center table-view-pf-actions">
                                            <div class="dropdown dropdown-kebab-pf">
                                                <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                    <span class="fa fa-ellipsis-v"></span>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-right">
                                                    <li><a href="<s:url namespace="/do/Portal/GuiFragment" action="edit"><s:param name="code" value="#currentGuiFragmentVar.code" /></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="%{#currentGuiFragmentVar.code}" />"><s:text name="label.edit" />
                                                            <span class="sr-only"><s:text name="label.edit" />:&#32;<s:property value="%{#currentGuiFragmentVar.code}" />
                                                            </span>
                                                        </a>
                                                    </li>
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
                            <span>
                                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/>
                            </span>
                            <div class="mt-5">
                                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp"/>
                            </div>
                        </div>
                    </div>
                </wpsa:subset>
            </div>
        </s:if>
        <s:else>
            <div class="alert alert-info margin-20-left-right" >
                <span class="pficon pficon-info"></span>
                <strong><s:text name="note.guiFragment.referencedFragments.empty" /></strong>
            </div>
        </s:else>
    </div>
    <hr>
    <div class="form-group">
        <%-- referenced PageModels --%>
        <s:if test="null != references['PageModelManagerUtilizers']">
            <p class="col-xs-12" >
                <s:text name="title.guiFragment.referencedPageModels" />
            </p>
            <div class="margin-20-left-right">
                <wpsa:subset source="references['PageModelManagerUtilizers']" count="10" objectName="pageModelReferencesVar" advanced="true" offset="5" pagerId="userManagerReferences">
                    <s:set var="group" value="#pageModelReferencesVar" />
                    <div class="text-center">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                    </div>
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover"  id="userListTable">
                            <tr>
                                <th class="text-center col-xs-5 col-sm-3 col-md-2 col-lg-2">
                                    <abbr title="<s:text name="label.actions" />">&ndash;</abbr>
                                </th>
                                <th><s:text name="label.pageModel.key" /></th>
                                <th><s:text name="label.pageModel.descr" /></th>
                            </tr>
                            <s:iterator var="pageModelVar">
                                <tr class="text-center text-nowrap">
                                    <%-- actions --%>
                                    <td class="text-center text-nowrap">
                                        <a class="btn btn-default"
                                           href="<s:url namespace="/do/PageModel" action="edit"><s:param name="code" value="#pageModelVar.code" /></s:url>"
                                           title="<s:text name="label.edit" />:&#32;<s:property value="%{#pageModelVar.code}" />"><span class="icon fa fa-pencil-square-o"></span><span class="sr-only"><s:text name="label.edit" />:&#32;<s:property value="%{#pageModelVar.code}" /></span>
                                        </a>
                                    </td>
                                    <td><s:property value="#pageModelVar.code" /></td>
                                    <td><s:property value="#pageModelVar.description" /></td>
                                </tr>
                            </s:iterator>
                        </table>
                    </div>
                    <div class="content-view-pf-pagination clearfix margin-20-left-right" >
                        <div class="form-group">
                            <span><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                            <div class="mt-5">
                                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                            </div>
                        </div>
                    </div>
                </wpsa:subset>
            </div>
        </s:if>
        <s:else>
            <div class="alert alert-info margin-20-left-right">
                <span class="pficon pficon-info"></span>
                <strong><s:text name="note.guiFragment.referencedPageModels.empty" /></strong>
            </div>
        </s:else>

    </div>
    <hr>
    <%-- referenced widgets --%>
    <div class="form-group">

        <s:if test="null != references['WidgetTypeManagerUtilizers']">
            <p class="col-xs-12">
                <s:text name="title.guiFragment.referencedWidgetTypes" />
            </p>
            <div class="col-xs-12 table-nomargin-bottom">
                <wpsa:subset source="references['WidgetTypeManagerUtilizers']" count="10" objectName="widgetTypeReferencesVar" advanced="true" offset="5" pagerId="widgetTypeReferences">
                    <s:set var="group" value="#widgetTypeReferencesVar" />
                    <table class="table table-striped table-bordered table-hover"  id="widgetTypeListTable">
                        <tr>
                            <th><s:text name="label.title" /></th>
                            <th><s:text name="label.code" /></th>
                        </tr>
                        <s:iterator var="currentWidgetVar">
                            <tr>
                                <td><s:property value="%{getTitle(#currentWidgetVar.code, #currentWidgetVar.titles)}" /></td>
                                <td><s:property value="#currentWidgetVar.code" /></td>
                            </tr>
                        </s:iterator>
                    </table>
                </div>
                <div class="content-view-pf-pagination clearfix margin-20-left-right" >
                    <div class="form-group">
                        <span><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                        <div class="mt-5">
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                        </div>
                    </div>
                </div>
            </wpsa:subset>
        </s:if>
        <s:else>
            <div class="alert alert-info margin-20-left-right">
                <span class="pficon pficon-info"></span>
                <strong><s:text name="note.guiFragment.referencedWidgetTypes.empty" /></strong>
            </div>
        </s:else>

    </div>
</s:form>

