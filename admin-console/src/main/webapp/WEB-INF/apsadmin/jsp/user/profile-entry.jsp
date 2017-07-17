<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.userSetting" /></li>
    <li><a href="<s:url namespace="/do/User" action="list" />"><s:text
                name="title.userManagement" /></a></li>
    <li class="page-title-container"><s:text
            name="title.userProfile.edit" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.userProfile.edit" />
    <span class="pull-right"> <a tabindex="0" role="button"
                                 data-toggle="popover" data-trigger="focus" data-html="true" title=""
                                 data-content="<s:text name="page.userList.help" />" data-placement="left"
                                 data-original-title=""> <i class="fa fa-question-circle-o"
                                   aria-hidden="true"></i>
        </a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields" />
    </div>
</div>
<br>

<div id="main" role="main">
    <s:form cssClass="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"
                        aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span> <strong><s:text
                        name="message.title.FieldErrors" />&ensp;<span
                        class="icon fa fa-question-circle cursor-pointer"
                        title="<s:text name="label.all" />" data-toggle="collapse"
                        data-target="#content-error-messages"></span> <span class="sr-only"><s:text
                            name="label.all" /></span> </strong>
                <ul class="unstyled" id="content-error-messages">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <p class="sr-only">
            <wpsf:hidden name="username" />
        </p>
        <div class="col-xs-12">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="profileType"><s:text
                        name="label.type" /></label>
                <div class="col-sm-8">
                    <span class="spacer-right-on-form"><s:property
                            value="userProfile.typeDescription" /></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="button-position-on-save">
                    <wpsf:submit action="changeProfileType"
                                 value="%{'('+getText('label.change')+')'}"
                                 cssClass="btn btn-primary" />
                </div>
            </div>
            <hr>
            <div class="form-group">
                <label class="col-sm-2 control-label"><s:text
                        name="label.username" /></label>
                <div class="col-sm-8">
                    <span class="spacer-right-on-form"><s:property
                            value="userProfile.username" /></span>
                </div>
            </div>
            <s:set var="lang" value="defaultLang" />
            <%-- attribute iterator --%>
            <s:iterator value="userProfile.attributeList" var="attribute">
                <%-- tracer start --%>
                <wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

                <s:set var="attributeFieldErrorsVar"
                       value="%{fieldErrors[#attribute.name]}" />
                <s:set var="attributeHasFieldErrorVar"
                       value="#attributeFieldErrorsVar != null && !#attributeFieldErrorsVar.isEmpty()" />
                <s:set var="attributeFieldNameErrorsVar"
                       value="%{fieldErrors[#attributeTracer.getFormFieldName(#attribute)]}" />
                <s:set var="attributeHasFieldNameErrorVar"
                       value="#attributeFieldNameErrorsVar != null && !#attributeFieldNameErrorsVar.isEmpty()" />
                <s:set var="attributeFieldNameErrorsVarV2"
                       value="%{fieldErrors[#attribute.type+':'+#attribute.name]}" />
                <s:set var="attributeHasFieldNameErrorVarV2"
                       value="#attributeFieldNameErrorsVarV2 != null && !#attributeFieldNameErrorsVarV2.isEmpty()" />

                <s:set var="attributeHasErrorVar"
                       value="%{#attributeHasFieldErrorVar||#attributeHasFieldNameErrorVar||#attributeHasFieldNameErrorVarV2}" />
                <s:set var="controlGroupErrorClassVar" value="''" />
                <s:set var="inputErrorClassVar" value="''" />
                <s:if test="#attributeHasErrorVar">
                    <div style="position: absolute">
                        <s:set var="controlGroupErrorClassVar" value="' has-error'" />
                        <s:set var="inputErrorClassVar" value="' input-with-feedback'" />
                    </div>
                </s:if>

                <s:if test="null != #attribute.description">
                    <s:set var="attributeLabelVar" value="#attribute.description" />
                </s:if>
                <s:else>
                    <s:set var="attributeLabelVar" value="#attribute.name" />
                </s:else>

                <div
                    class="form-group<s:property value="#controlGroupErrorClassVar" />">
                    <%-- form group --%>
                    <s:if
                        test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
                        <label class="display-block"><span class="icon fa fa-list"></span>&#32;<s:property
                                value="#attributeLabelVar" />&#32;<s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
                        </s:if>
                        <s:elseif
                            test="#attribute.type == 'Image' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState' || #attribute.type == 'Composite'">
                        <label class="display-block"><s:property
                                value="#attributeLabelVar" />&#32;<s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
                        </s:elseif>
                        <s:else>
                        <label class="col-sm-2 control-label"
                               for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
                            <s:property value="#attributeLabelVar" /> <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
                        </label>
                    </s:else>
                    <div class="col-sm-10">
                        <s:if test="#attribute.type == 'Boolean'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
                        </s:if>
                        <s:elseif test="#attribute.type == 'CheckBox'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Composite'">
                            <s:include
                                value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/compositeAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Date'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Enumerator'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'EnumeratorMap'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorMapAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Hypertext'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'List'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/listAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Longtext'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Monolist'">
                            <s:include
                                value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/monolistAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Monotext'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Number'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Text'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'ThreeState'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Timestamp'">
                            <s:include
                                value="/WEB-INF/apsadmin/jsp/entity/modules/timestampAttribute.jsp" />
                        </s:elseif>
                        <s:else>
                            <%-- all other attributes uses monotext --%>
                            <div class="col-sm-10">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
                            </div>
                        </s:else>
                    </div>
                    <s:if test="#attributeHasErrorVar">
                        <p class="text-danger padding-small-vertical">
                            <jsp:useBean id="attributeErrorMapVar" class="java.util.HashMap"
                                         scope="request" />
                            <s:iterator value="#attributeFieldErrorsVar">
                                <s:set var="attributeCurrentError" scope="page" />
                                <c:set target="${attributeErrorMapVar}"
                                       property="${attributeCurrentError}"
                                       value="${attributeCurrentError}" />
                            </s:iterator>
                            <s:iterator value="#attributeFieldNameErrorsVar">
                                <s:set var="attributeCurrentError" scope="page" />
                                <c:set target="${attributeErrorMapVar}"
                                       property="${attributeCurrentError}"
                                       value="${attributeCurrentError}" />
                            </s:iterator>
                            <s:iterator value="#attributeFieldNameErrorsVarV2">
                                <s:set var="attributeCurrentError" scope="page" />
                                <c:set target="${attributeErrorMapVar}"
                                       property="${attributeCurrentError}"
                                       value="${attributeCurrentError}" />
                            </s:iterator>
                            <c:forEach items="${attributeErrorMapVar}"
                                       var="attributeCurrentError">
                                <c:out value="${attributeCurrentError.value}" />
                                <br />
                            </c:forEach>
                            <c:set var="attributeErrorMapVar" value="${null}" />
                            <c:set var="attributeCurrentError" value="${null}" />
                        </p>
                    </s:if>
                </div>
                <%-- form-group --%>
            </s:iterator>
            <div class="form-group ">
                <div class="button-position-on-save">
                    <wpsf:submit type="button" action="save" cssClass="btn btn-primary">
                        <s:text name="label.save" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>
