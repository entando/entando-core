<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>

<!-- Contents -->
<div class="col-xs-12 no-padding">
    <legend><s:text name="title.category.contentReferenced" /></legend>
    <s:if test="null != references['jacmsContentManagerUtilizers']">
        <wpsa:subset source="references['jacmsContentManagerUtilizers']" count="10" objectName="contentReferences" advanced="true" offset="5" pagerId="contentManagerReferences">
            <s:set var="group" value="#contentReferences" />

            <table class="table table-striped table-bordered table-hover no-mb" id="contentListTable">
                <thead>
                    <tr>
                        <th>
                            <s:text name="label.description" />
                        </th>
                        <th class="table-w-15">
                            <s:text name="label.code" />
                        </th>
                        <th class="table-w-15">
                            <s:text name="label.type" />
                        </th>
                        <th class="table-w-15 text-center">
                            <s:text name="label.lastEdit" />
                        </th>
                    </tr>
                </thead>
                <tbody>
                <s:iterator var="currentContentIdVar">
                    <jacmswpsa:content contentId="%{#currentContentIdVar}" var="currentContentRecordVar" record="true" />
                    <tr>
                        <jacmswpsa:content contentId="%{#currentContentRecordVar.id}" var="currentContentVar" authToEditVar="isAuthToEditVar" workVersion="true" />
                        <td>
                            <s:if test="#isAuthToEditVar">
                                <a href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#currentContentVar.id" /></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="#currentContentVar.descr"/>"><s:property value="#currentContentVar.descr"/></a>
                            </s:if>
                            <s:else><s:property value="#currentContentVar.descr"/></s:else>
                            <s:set var="isAuthToEditVar" value="%{false}" />
                        </td>
                        <td>
                            <code><s:property value="#currentContentVar.id"/></code>
                        </td>
                        <td>
                            <s:property value="#currentContentVar.typeDescr"/>
                        </td>
                        <td class="text-center">
                            <span title="<s:date name="#currentContentRecordVar.modify" format="EEEE dd MMMM yyyy" />"><s:date name="#currentContentRecordVar.modify" format="dd/MM/yyyy" /></span>
                        </td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
            <div class="content-view-pf-pagination table-view-pf-pagination clearfix">
                <div class="form-group">
                    <span>
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/>
                    </span>
                </div>
            </div>
            <div class="text-center">
                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
            </div>
        </wpsa:subset>
    </s:if>
    <s:else>
        <div class="alert alert-info">
            <span class="pficon pficon-info"></span>
            <strong><s:text name="note.category.referencedContents.empty" /></strong>
        </div>
    </s:else>
</div>

<!-- Digital Assets -->
<div class="col-xs-12 no-padding mt-20">
        <legend class="margin-none"><s:text name="title.category.resourcesReferenced" /></legend>
        <s:if test="null != references['jacmsResourceManagerUtilizers']">
            <wpsa:subset source="references['jacmsResourceManagerUtilizers']" count="10" objectName="resourceReferences" advanced="true" offset="5" pagerId="resourceManagerReferences">
                <s:set var="group" value="#resourceReferences" />
                <table class="table table-striped table-bordered table-hover no-mb" id="resourceListTable">
                    <thead>
                        <tr>
                            <th class="table-w-60">
                                <s:text name="label.description" />
                            </th>
                            <th class="table-w-15">
                                <s:text name="label.code" />
                            </th>
                            <th class="table-w-15">
                                <s:text name="label.type" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator var="currentResourceIdVar">
                            <jacmswpsa:resource resourceId="%{#currentResourceIdVar}" var="currentResourceVar" />
                            <tr>
                                <s:set var="canEditCurrentResource" value="%{false}" />
                                <s:set var="currentResourceGroup" value="#currentResourceVar.mainGroup" scope="page" />
                                <td>
                                    <wp:ifauthorized groupName="${currentResourceGroup}" permission="manageResources"><s:set var="canEditCurrentResource" value="%{true}" /></wp:ifauthorized>
                                    <s:if test="#canEditCurrentResource">
                                        <a href="<s:url action="edit" namespace="/do/jacms/Resource"><s:param name="resourceId" value="#currentResourceVar.id" /></s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="#currentResourceVar.descr"/>"><s:property value="#currentResourceVar.descr"/></a>
                                    </s:if>
                                    <s:else><s:property value="#currentResourceVar.descr"/></s:else>
                                </td>
                                <td><s:property value="#currentResourceVar.id"/></td>
                                <td><s:property value="#currentResourceVar.type"/></td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
                <div class="content-view-pf-pagination table-view-pf-pagination clearfix">
                    <div class="form-group">
                        <span>
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/>
                        </span>
                    </div>
                </div>
                <div class="text-center">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>
        </wpsa:subset>
    </s:if>
    <s:else>
        <div class="alert alert-info">
            <span class="pficon pficon-info"></span>
            <strong><s:text name="note.category.referencedResources.empty" /></strong>
        </div>
    </s:else>
</div>
