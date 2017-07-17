<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib prefix="wp" uri="/aps-core"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.userSetting" /></li>

    <li><a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a></li>

    <li class="page-title-container"><s:text name="title.userProfileDetails" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.userProfileDetails" />
    <span class="pull-right"> <a tabindex="0" role="button"
                                 data-toggle="popover" data-trigger="focus" data-html="true" title=""
                                 data-content="<s:text name="page.userList.help" />" data-placement="left"
                                 data-original-title=""> <i class="fa fa-question-circle-o"
                                   aria-hidden="true"></i>
        </a>
    </span>
</h1>


<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div id="main" role="main">

    <s:set var="lang" value="defaultLang" />
    <s:set var="userProfileVar" value="%{getUserProfile(username)}" />
    <s:if test="%{null != #userProfileVar}">
        <div class="col-sm-12">
            <table class="table table-striped table-bordered">
                <tr>
                    <th class="td-pagetree-width"><s:text name="label.username" /></th>
                    <td><s:property value="username" /></td>
                </tr>

                <s:set var="lang" value="defaultLang" />
                <s:iterator value="#userProfileVar.attributeList" var="attribute">
                    <tr>
                        <%-- INIZIALIZZAZIONE TRACCIATORE --%>
                        <s:set var="attributeTracer"
                               value="initAttributeTracer(#attribute, #lang)" />
                        <s:if test="null != #attribute.description">
                            <s:set var="attributeLabelVar" value="#attribute.description" />
                        </s:if>
                        <s:else>
                            <s:set var="attributeLabelVar" value="#attribute.name" />
                        </s:else>

                        <%-- VISUALIZZAZIONE CONTENUTO ATTRIBUTI  --%>
                        <th class="td-pagetree-width"><s:property
                                value="#attributeLabelVar" /></th>
                        <td>
                            <%-- ############# ATTRIBUTO TESTO MONOLINGUA ############# --%>
                            <s:if test="#attribute.type == 'Monotext'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/monotextAttribute.jsp" />
                            </s:if> <%-- ############# ATTRIBUTO TESTO SEMPLICE MULTILINGUA ############# --%>
                            <s:elseif test="#attribute.type == 'Text'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO TESTOLUNGO ############# --%> <s:elseif
                                test="#attribute.type == 'Longtext'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/longtextAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO HYPERTEXT ############# --%> <s:elseif
                                test="#attribute.type == 'Hypertext'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/hypertextAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO Boolean ############# --%> <s:elseif
                                test="#attribute.type == 'Boolean'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/booleanAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO ThreeState ############# --%> <s:elseif
                                test="#attribute.type == 'ThreeState'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/threeStateAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO Number ############# --%> <s:elseif
                                test="#attribute.type == 'Number'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO Date ############# --%> <s:elseif
                                test="#attribute.type == 'Date'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/dateAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO TESTO Enumerator ############# --%>
                            <s:elseif test="#attribute.type == 'Enumerator'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO TESTO EnumeratorMap ############# --%>
                            <s:elseif test="#attribute.type == 'EnumeratorMap'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorMapAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO Monolist ############# --%> <s:elseif
                                test="#attribute.type == 'Monolist'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/monolistAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO List ############# --%> <s:elseif
                                test="#attribute.type == 'List'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/listAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO Composite ############# --%> <s:elseif
                                test="#attribute.type == 'Composite'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/compositeAttribute.jsp" />
                            </s:elseif> <%-- ############# ATTRIBUTO CheckBox ############# --%> <s:elseif
                                test="#attribute.type == 'CheckBox'">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/entity/view/checkBoxAttribute.jsp" />
                            </s:elseif>
                        </td>
                    </s:iterator>
            </table>
        </div>
    </div>
</s:if>

<div class="form-group ">
    <div class="col-sm-12">
        <a class="btn btn-primary pull-right"
           href="<s:url namespace="/do/User" action="list" />"><s:text
                name="label.back" /></a>
    </div>
</div>
