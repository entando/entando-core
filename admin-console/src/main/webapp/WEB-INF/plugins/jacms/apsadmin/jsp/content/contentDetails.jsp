<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
            <s:text name="breadcrumb.jacms.content.list" />
        </a>
    </li>
    <li>
        <s:text name="title.inspection" />
    </li>
    <s:if test="currentPublicVersion">
        <li>
            <s:text name="name.version.onLine" />
        </li>
    </s:if>
    <s:else>
        <li>
            <s:text name="name.version.work" />
        </li>
    </s:else>
</ol>

<h1 class="page-title-container">
    <s:if test="currentPublicVersion">
        <s:text name="name.version.onLine" />
    </s:if>
    <s:else>
        <s:text name="name.version.work" />
    </s:else>

    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="label.contents.section.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>



<div id="main" role="main"><%-- #main --%>
    <s:if test="content == null"><%-- content is null --%>
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <s:text name="title.error" />
            <p class="margin-base-top"><s:text name="message.noContentToInspect.found" /></p>
        </div>
    </s:if><%-- content is null --%>
    <s:else><%-- content not null --%>
        <p class="sr-only"><s:text name="note.workingOn" />:&#32;<em class="important"><s:property value="content.descr"/></em> (<s:property value="content.typeDescr"/>)</p>
        <h3 class="sr-only"><s:text name="title.quickMenu" /></h3>
        <ul class="sr-only">
            <li><a href="#jpcontentinspection_metadata"><span title="<s:text name="metadata.full" />"><s:text name="metadata" /></span></a></li>
            <li><a href="#jpcontentinspection_referral_contents"><s:text name="title.referencingContents" /></a></li>
            <li><a href="#jpcontentinspection_referring_conts"><s:text name="title.referencedContents" /></a></li>
            <li><a href="#jpcontentinspection_pages"><s:text name="title.referencedPages" /></a></li>
            <li><a href="#jpcontentinspection_contents"><s:text name="title.content" /></a></li>
            <li><a href="#jpcontentinspection_referencing_pages"><s:text name="title.referencingPages" /></a></li>
        </ul>
        <%-- metadata --%>

        <button type="button" data-toggle="collapse" data-target="#jpcontentinspection_metadata" class="btn btn-default">
            <s:text name="title.metadata" />
            <span class="icon-chevron-down"></span>
        </button><br><br>

        <div class="collapse" id="jpcontentinspection_metadata">
            <table class="table table-bordered">
                <tr>
                    <th class="td-pagetree-width"><s:text name="label.description" /></th>
                    <td><s:property value="content.descr" /></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="label.key" /></th>
                    <td><s:property value="content.id" /></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="label.lastEdit" /></th>
                    <td title="<s:date name="content.lastModified" format="EEEE d MMMM yyyy, HH:mm" />">
                        <span title="<s:date name="content.lastModified" format="EEEE d MMMM yyyy, HH:mm" />"><s:date name="content.lastModified" format="dd/MM/yyyy HH:mm" nice="true" /></span>
                    </td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="label.creationDate" /></th>
                    <td><s:date name="content.created" format="dd/MM/yyyy HH:mm" /></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="label.editor" /></th>
                    <td><s:property value="content.lastEditor" /></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="name.version" /></th>
                    <td><s:property value="content.version" /></td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="label.mainGroup" /></th>
                    <td>
                        <s:property value="%{getGroupsMap()[content.mainGroup].getDescr()}"/>&#32;
                    </td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="label.viewgroups" /></th>
                    <td>
                        <s:if test="!content.groups.empty">
                            <s:set var="groupsVar" value="%{''}" />
                            <s:set var="firstVar" value="%{true}" />
                            <s:iterator var="curViewGroupCode" value="content.groups">
                                <s:set var="curViewGroup" value="getGroup(#curViewGroupCode)" />
                                <s:if test="null != #curViewGroup">
                                    <s:if test="!#firstVar">
                                        <s:set var="groupsVar" value="%{#groupsVar + ', ' + #curViewGroup.descr}"/>
                                    </s:if>
                                    <s:else>
                                        <s:set var="groupsVar" value="%{#curViewGroup.descr}"/>
                                    </s:else>
                                    <s:if test="#firstVar"><s:set var="firstVar" value="%{false}" /></s:if>
                                </s:if>
                            </s:iterator>
                            <s:property value="#groupsVar" />
                            <s:if test="#groupsVar==''">
                                <span class="text-muted"><s:text name="note.noViewGroups"/></span>
                            </s:if>
                        </s:if>
                        <s:else>
                            <span class="text-muted"><s:text name="note.noViewGroups"/></span>
                        </s:else>
                    </td>
                </tr>
                <tr>
                    <th class="td-pagetree-width"><s:text name="label.categories" /></th>
                    <td>
                        <s:if test="!content.categories.empty">
                            <ul class="list-unstyled">
                                <s:iterator var="curCategory" value="content.categories" status="catStatus">
                                    <li><s:property value="%{#curCategory.getFullTitle(currentLang.code)}"/></li>
                                    </s:iterator>
                            </ul>
                        </s:if>
                        <s:else>
                            <span class="text-muted"><s:text name="label.none" /></span>
                        </s:else>
                    </td>
            </table>
        </div>
        <%-- references --%><hr>
        <br><br>
        <div class="col-xs-12">
            <div class="form-group">
                <label class="control-label col-sm-2" ><s:text name="title.references" /></label>


                <div class="col-sm-9">
                    <%-- referral conentens --%>
                    <s:set var="referencedContentsIdVar" value="referencedContentsId" />
                    <dl id="jpcontentinspection_referral_contents">
                        <dt>
                            <span class="icon fa fa-file-text-o"></span>&#32;<s:text name="title.referencedContents" />
                        </dt>
                        <s:if test="!#referencedContentsIdVar.empty">
                            <s:iterator var="curReferencedContentId" value="#referencedContentsIdVar">
                                <dd>
                                    <jacmswpsa:content contentId="%{#curReferencedContentId}" var="curReferencedContent" authToEditVar="isAuthToEditVar" workVersion="true" />
                                    <s:if test="#isAuthToEditVar">
                                        <s:url var="contentEditActionURL" action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#curReferencedContent.id" /></s:url>
                                        <a href="<s:property value="#contentEditActionURL" />" title="<s:text name="label.edit" />:&#32;<s:property value="#curReferencedContent.descr" />"><s:property value="#curReferencedContent.descr" />&#32;(<s:property value="#curReferencedContent.id" />)</a>
                                    </s:if>
                                    <s:else>
                                        <s:property value="#curReferencedContent.descr" />&#32;(<s:property value="#curReferencedContent.id" />)
                                    </s:else>
                                </dd>
                            </s:iterator>
                        </s:if>
                        <s:else>
                            <dd><s:text name="label.none" /></dd>
                        </s:else>
                    </dl>
                    <%-- referenced pages --%>
                    <s:set var="referencedPagesVar" value="referencedPages" />
                    <dl id="jpcontentinspection_pages">
                        <dt><span class="icon fa fa-folder"></span>&#32;<s:text name="title.referencedPages" /></dt>
                        <s:if test="!#referencedPagesVar.empty">
                            <s:iterator var="curReferencedPage" value="#referencedPagesVar">
                                <dd>
                                    <s:set var="canEdit" value="%{false}" />
                                    <s:set var="curReferencingPageMainGroupCode" value="%{#curReferencedPage.group}" scope="page" />
                                    <wp:ifauthorized groupName="${curReferencingPageMainGroupCode}" permission="managePages"><s:set var="canEdit" value="%{true}" /></wp:ifauthorized>
                                    <s:if test="#canEdit">
                                        <s:url var="pageEditURL" action="viewTree" namespace="/do/Page"><s:param name="selectedNode" value="#curReferencedPage.code" /></s:url>
                                        <a href="<s:property value="#pageEditURL" />" title="<s:text name="label.edit" />:&#32;<s:property value="#curReferencedPage.getFullTitle(currentLang.code)"/>&#32;(<s:property value="#curReferencedPage.code" />)"><s:property value="#curReferencedPage.getFullTitle(currentLang.code)"/>&#32;(<s:property value="#curReferencedPage.code" />)</a>
                                    </s:if>
                                    <s:else>
                                        <s:property value="#curReferencedPage.getFullTitle(currentLang.code)" />&#32;(<s:property value="#curReferencedPage.code" />)
                                    </s:else>
                                </dd>
                            </s:iterator>
                        </s:if>
                        <s:else>
                            <dd><s:text name="label.none" /></dd>
                        </s:else>
                    </dl>
                </div>
            </div>
        </div>
        <div class="col-xs-12">
            <%-- external references --%>
            <div class="form-group">
                <label class="control-label col-sm-2"><s:text name="title.referencesExternal" /></label>
                <div class="col-sm-9">
                    <%-- referencing contents --%>
                    <s:set var="referencingContentsIdVar" value="referencingContentsId" />
                    <dl id="jpcontentinspection_referring_conts">
                        <dt><span class="icon fa fa-file-text-o"></span>&#32;<s:text name="title.referencingContents" /></dt>
                        <s:if test="!#referencingContentsIdVar.empty">
                            <s:iterator var="curReferencingContentId" value="#referencingContentsIdVar">
                                <jacmswpsa:content contentId="%{#curReferencingContentId}" var="curReferencingContent" authToEditVar="isAuthToEditVar" workVersion="true" />
                                <dd>
                                    <s:if test="#isAuthToEditVar">
                                        <s:url var="contentEditActionURL" action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#curReferencingContent.id" /></s:url>
                                        <a href="<s:property value="#contentEditActionURL" />" title="<s:text name="label.edit" />:&#32;<s:property value="#curReferencingContent.descr" />"><s:property value="#curReferencingContent.descr" />&#32;(<s:property value="#curReferencingContent.id" />)</a>
                                    </s:if>
                                    <s:else>
                                        <s:property value="#curReferencingContent.descr" />&#32;(<s:property value="#curReferencingContent.id" />)
                                    </s:else>
                                </dd>
                            </s:iterator>
                        </s:if>
                        <s:else>
                            <dd><s:text name="label.none" /></dd>
                        </s:else>
                    </dl>
                    <%-- referecing pages --%>
                    <s:set var="referencingPagesVar" value="referencingPages" />
                    <dl id="jpcontentinspection_referencing_pages">
                        <dt><span class="icon fa fa-folder"></span>&#32;<s:text name="title.referencingPages" /></dt>
                        <s:if test="!#referencingPagesVar.empty">
                            <s:iterator var="curReferencingPage" value="#referencingPagesVar">
                                <dd>
                                    <s:set var="canEdit" value="%{false}" />
                                    <c:set var="curReferencingPageMainGroupCode"><s:property value="#curReferencingPage.group" escapeHtml="false"/></c:set>
                                    <wp:ifauthorized groupName="${curReferencingPageMainGroupCode}" permission="managePages"><s:set var="canEdit" value="%{true}" /></wp:ifauthorized>
                                    <s:if test="#canEdit">
                                        <s:url var="pageEditURL" action="viewTree" namespace="/do/Page"><s:param name="selectedNode" value="#curReferencingPage.code" /></s:url>
                                        <a href="<s:property value="#pageEditURL" />" title="<s:text name="label.edit" />:&#32;<s:property value="#curReferencingPage.getFullTitle(currentLang.code)"/>&#32;(<s:property value="#curReferencingPage.code" />)"><s:property value="#curReferencingPage.getFullTitle(currentLang.code)"/>&#32;(<s:property value="#curReferencingPage.code" />)</a>
                                    </s:if>
                                    <s:else>
                                        <s:property value="#curReferencingPage.getFullTitle(currentLang.code)" />&#32;(<s:property value="#curReferencingPage.code" />)
                                    </s:else>
                                </dd>
                            </s:iterator>
                        </s:if>
                        <s:else>
                            <dd><s:text name="label.none" /></dd>
                        </s:else>
                    </dl>
                </div>
            </div>
        </div>

        <div class="col-xs-12">
            <%-- content attributes --%>
            <div class="form-group">
                <div class="tab-container"><%-- tab container --%>
                    <p class="sr-only" id="jpcontentinspection_contents"><s:text name="title.content" /></p>
                    <ul class="nav nav-tabs tab-togglers" id="tab-togglers">
                        <s:iterator value="langs" var="lang" status="langStatusVar">
                            <li <s:if test="#langStatusVar.first"> class="active" </s:if>>
                                <a data-toggle="tab" href="#<s:property value="#lang.code" />_tab">
                                    <s:property value="#lang.descr" />
                                </a>
                            </li>
                        </s:iterator>
                    </ul>
                    <div class="panel panel-default" id="tab-container"><%-- panel --%>
                        <div class="panel-body"><%-- panel body --%>
                            <div class="tab-content"><%-- tabs content --%>
                                <s:iterator var="lang" value="langs" status="langStatusVar"><%-- lang iterator --%>
                                    <div id="<s:property value="#lang.code" />_tab" class="tab-pane <s:if test="#langStatusVar.first"> active </s:if>"><%-- tab --%>
                                        <h3 class="sr-only"><s:property value="#lang.descr" /> (<a class="backLink" href="#quickmenu" id="<s:property value="#lang.code" />_tab_quickmenu"><s:text name="note.goBackToQuickMenu" /></a>)</h3>
                                        <s:iterator value="content.attributeList" var="attribute" status="attributeListStatus"><%-- content.attributeList iterator --%>
                                            <div class="form-group"><%-- form-group --%>
                                                <wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" /><%-- tracer init --%>
                                                <s:if test="null != #attribute.description"><s:set var="attributeLabelVar" value="#attribute.description" /></s:if>
                                                <s:else><s:set var="attributeLabelVar" value="#attribute.name" /></s:else>
                                                <label class="control-label col-sm-2"><s:property value="#attributeLabelVar" /></label>
                                                <div class="input-group"><%--input-group --%>
                                                    <s:if test="#attribute.type == 'Monotext' || #attribute.type == 'Text' || #attribute.type == 'Longtext' || #attribute.type == 'Enumerator' || #attribute.type == 'EnumeratorMap'">
                                                        <s:if test="#lang.default">
                                                            <div>
                                                                <s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
                                                            </div>
                                                        </s:if>
                                                        <s:else>
                                                            <s:if test="%{(#attribute.isMultilingual()) && (#attribute.getTextForLang(#lang.code)!=null)}">
                                                                <div>
                                                                    <s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
                                                                </div>
                                                            </s:if>
                                                            <s:else>
                                                                <div class="text-muted">
                                                                    <s:property value="#attribute.getText()" />
                                                                </div>
                                                            </s:else>
                                                        </s:else>
                                                    </s:if>
                                                    <s:elseif test="#attribute.type == 'Attach'">
                                                        <p><s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/attachAttribute.jsp" /></p>
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'CheckBox'">
                                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/view/checkBoxAttribute.jsp" />
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Date'">
                                                        <p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/dateAttribute.jsp" /></p>
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Enumerator'">
                                                        <p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorAttribute.jsp" /></p>
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'EnumeratorMap'">
                                                        <p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorMapAttribute.jsp" /></p>
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Hypertext'">
                                                        <div><s:include value="/WEB-INF/apsadmin/jsp/entity/view/hypertextAttribute.jsp" /></div>
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Image'">
                                                        <p><s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/imageAttribute.jsp" /></p>
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Link'">
                                                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/linkAttribute.jsp" />
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Number'">
                                                        <p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" /></p>
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'ThreeState'">
                                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/view/threeStateAttribute.jsp" />
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Timestamp'">
                                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/view/timestampAttribute.jsp" />
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Timestamp'">
                                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/view/timestampAttribute.jsp" />
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Composite'">
                                                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/compositeAttribute.jsp" />
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'List'">
                                                        <s:include value="/WEB-INF/apsadmin/jsp/entity/view/listAttribute.jsp" />
                                                    </s:elseif>
                                                    <s:elseif test="#attribute.type == 'Monolist'">
                                                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/monolistAttribute.jsp" />
                                                    </s:elseif>
                                                    <wpsa:hookPoint key="jacms.contentDetails.attributeExtra" objectName="hookPointElements_jacms_contentDetails_attributeExtra">
                                                        <s:iterator value="#hookPointElements_jacms_contentDetails_attributeExtra" var="hookPointElementVar">
                                                            <wpsa:include value="%{#hookPointElementVar.filePath}"></wpsa:include>
                                                        </s:iterator>
                                                    </wpsa:hookPoint>
                                                </div><%--input-group --%>
                                            </div><%-- form-group --%>
                                            <s:if test="!#attributeListStatus.last"><hr /></s:if>
                                        </s:iterator><%-- content.attributeList iterator --%>
                                    </div><%-- tab --%>
                                </s:iterator><%-- lang iterator --%>
                            </div><%-- tabs content --%>
                        </div><%-- panel body --%>
                    </div><%-- panel --%>
                </div><%-- tabs container --%>
            </div>
        </s:else><%-- content is not null --%>
    </div>
</div>
