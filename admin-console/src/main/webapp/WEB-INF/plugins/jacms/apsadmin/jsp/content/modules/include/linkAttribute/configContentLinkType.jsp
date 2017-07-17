<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<s:set var="thirdTitleVar">
    <s:text name="title.configureLinkAttribute" />
</s:set>

<s:include value="linkAttributeConfigIntro.jsp" />
<s:include value="linkAttributeConfigReminder.jsp"/>

<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/linkAttribute/chooseLink-menu.jsp">
    <s:param name="linkTypeVar" value="3"/>
</s:include>

<p class="sr-only"><s:text name="note.chooseContentToLink" /></p>
<s:form action="search" cssClass="form-horizontal">
    <div class="panel panel-default no-top-border">
        <div class="panel-body">
            <!-- Search Form -->
            <div class="well col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8 col-sm-offset-1 col-sm-10 mt-20">
                <p class="search-label">
                    <s:text name="label.search.label" />
                </p>
                <div class="form-group">
                    <label class="control-label col-sm-2" for="text" class="sr-only">
                        <s:text name="label.description" />
                    </label>
                    <div class="col-sm-9">
                        <wpsf:textfield name="text" id="text" cssClass="form-control"
                                        placeholder="%{getText('label.description')}"
                                        title="%{getText('label.search.by')+' '+getText('label.description')}" />
                    </div>
                </div>
                <br><br>
                <!-- Advanced Search -->
                <div class="panel-group advanced-search" id="accordion-markup">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <p class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion-markup"
                                   href="#collapseOne"><s:text name="label.advancedSearch"/>
                                </a>
                            </p>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse">
                            <div class="panel-body">
                                <div id="search-advanced" class="collapse-input-group">
                                    <!--code -->
                                    <div class="form-group">
                                        <label for="contentIdToken" class="control-label col-sm-2 text-right">
                                            <s:text name="label.code" />
                                        </label>
                                        <div class="col-sm-9 input-group">
                                            <wpsf:textfield name="contentIdToken" id="contentIdToken" cssClass="form-control" />
                                        </div>
                                    </div>
                                    <!--type-->
                                    <div class="form-group">
                                        <label class="control-label col-sm-2 text-right" for="contentType"><s:text name="label.type"/></label>
                                        <div class="col-sm-9 input-group input-20px-leftRight">
                                            <wpsf:select name="contentType" id="contentType"
                                                         list="contentTypes" listKey="code" listValue="descr"
                                                         headerKey="" headerValue="%{getText('label.all')}"
                                                         cssClass="form-control" />
                                        </div>
                                    </div>
                                    <!--status -->
                                    <div class="form-group">
                                        <label class="control-label col-sm-2 text-right" for="state"><s:text name="label.state"/></label>
                                        <div class="col-sm-9 input-group">
                                            <wpsf:select name="state" id="state" list="avalaibleStatus"
                                                         headerKey="" headerValue="%{getText('label.all')}"
                                                         cssClass="form-control" listKey="key" listValue="%{getText(value)}" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--search -->
                <div class="form-group">
                    <div class="col-sm-12">
                        <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                            <s:text name="label.search" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>

            <div class="col-xs-12">
                <wpsa:subset source="contents" count="10" objectName="groupContent" advanced="true" offset="5">
                    <p class="sr-only">
                        <wpsf:hidden name="lastGroupBy" />
                        <wpsf:hidden name="lastOrder" />
                        <wpsf:hidden name="contentOnSessionMarker" />
                    </p>
                    <s:set var="group" value="#groupContent" />
                    <s:if test="%{getContents().size() > 0}">
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover no-mb">
                                <thead>
                                    <tr>
                                        <th class="text-center w2perc">
                                            <input type="radio" disabled />
                                        </th>
                                        <th><a href="
                                               <s:url action="changeOrder">
                                                   <s:param name="text">
                                                       <s:property value="#request.text"/>
                                                   </s:param>
                                                   <s:param name="contentIdToken">
                                                       <s:property value="#request.contentIdToken"/>
                                                   </s:param>
                                                   <s:param name="contentType">
                                                       <s:property value="#request.contentType"/>
                                                   </s:param>
                                                   <s:param name="state">
                                                       <s:property value="#request.state"/>
                                                   </s:param>
                                                   <s:param name="pagerItem">
                                                       <s:property value="#groupContent.currItem"/>
                                                   </s:param>
                                                   <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                   <s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
                                                   <s:param name="groupBy">descr</s:param>
                                                   <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                                               </s:url>
                                               "><s:text name="label.description" /></a></th>
                                        <th class="text-center text-nowrap">
                                            <a href="<s:url action="changeOrder">
                                                   <s:param name="text">
                                                       <s:property value="#request.text"/>
                                                   </s:param>
                                                   <s:param name="contentIdToken">
                                                       <s:property value="#request.contentIdToken"/>
                                                   </s:param>
                                                   <s:param name="contentType">
                                                       <s:property value="#request.contentType"/>
                                                   </s:param>
                                                   <s:param name="state">
                                                       <s:property value="#request.state"/>
                                                   </s:param>
                                                   <s:param name="pagerItem">
                                                       <s:property value="#groupContent.currItem"/>
                                                   </s:param>
                                                   <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                   <s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
                                                   <s:param name="groupBy">code</s:param>
                                                   <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                                               </s:url>
                                               "><s:text name="label.code" /></a></th>
                                        <th><s:text name="label.group" />
                                        </th>
                                        <th class="text-center text-nowrap">
                                            <a href="
                                               <s:url action="changeOrder">
                                                   <s:param name="text">
                                                       <s:property value="#request.text"/>
                                                   </s:param>
                                                   <s:param name="contentIdToken">
                                                       <s:property value="#request.contentIdToken"/>
                                                   </s:param>
                                                   <s:param name="contentType">
                                                       <s:property value="#request.contentType"/>
                                                   </s:param>
                                                   <s:param name="state">
                                                       <s:property value="#request.state"/>
                                                   </s:param>
                                                   <s:param name="pagerItem">
                                                       <s:property value="#groupContent.currItem"/>
                                                   </s:param>
                                                   <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                   <s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
                                                   <s:param name="groupBy">created</s:param>
                                                   <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                                               </s:url>
                                               "><s:text name="label.creationDate" /></a>
                                        </th>
                                        <th class="text-center text-nowrap">
                                            <a href="
                                               <s:url action="changeOrder">
                                                   <s:param name="text">
                                                       <s:property value="#request.text"/>
                                                   </s:param>
                                                   <s:param name="contentIdToken">
                                                       <s:property value="#request.contentIdToken"/>
                                                   </s:param>
                                                   <s:param name="contentType">
                                                       <s:property value="#request.contentType"/>
                                                   </s:param>
                                                   <s:param name="state">
                                                       <s:property value="#request.state"/>
                                                   </s:param>
                                                   <s:param name="pagerItem">
                                                       <s:property value="#groupContent.currItem"/>
                                                   </s:param>
                                                   <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                   <s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
                                                   <s:param name="groupBy">lastModified</s:param>
                                                   <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                                               </s:url>"><s:text name="label.lastEdit" /></a>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator var="contentId">
                                        <s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
                                            <tr>
                                                <td><input type="radio" name="contentId" id="contentId_<s:property value="#content.id"/>" value="<s:property value="#content.id"/>" /></td>
                                            <td><s:property value="#content.descr" /></td>
                                            <td class="text-center text-nowrap"><code><s:property value="#content.id" /></code></td>
                                            <td>
                                                <s:property value="%{getGroup(#content.mainGroupCode).descr}" />
                                            </td>
                                            <td class="text-center text-nowrap"><code><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></code></td>
                                            <td class="text-center text-nowrap"><code><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></code></td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>

                        <div class="content-view-pf-pagination table-view-pf-pagination clearfix mb-20">
                            <div class="form-group">
                                <span><s:include
                                        value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                                <div class="mt-5">
                                    <s:include
                                        value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                                </div>
                            </div>
                        </div>
                    </s:if>
                </wpsa:subset>
            </div>

            <div class="col-xs-12">
                <div class="alert alert-warning no-mt">
                    <span class="pficon pficon-warning-triangle-o"></span>
                    <label class="chebox-inline" for="contentOnPageType">
                        <wpsf:checkbox useTabindexAutoIncrement="true" name="contentOnPageType" id="contentOnPageType"/>
                        &#32;<s:text name="note.makeContentOnPageLink" />
                    </label>
                </div>
            </div>

            <div class="col-xs-12">
                <div class="text-right">
                    <a href="${contentEntryURL}" title="<s:text name="label.cancel" />" class="btn btn-default mr-10">
                        <s:text name="label.cancel" />
                    </a>
                    <wpsf:submit type="button" action="joinContentLink" cssClass="btn btn-primary">
                        <s:text name="label.save" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </div>
</s:form>
