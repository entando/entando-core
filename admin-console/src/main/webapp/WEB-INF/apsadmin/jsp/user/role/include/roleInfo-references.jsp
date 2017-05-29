<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<s:form cssClass="form-horizontal">
    <p class="sr-only">
        <wpsf:hidden name="name" />
    </p>
    <%-- referenced users --%>
   
    <div class="form-group">
        <label class="control-label col-sm-2"><s:text
                name="title.role.referencedUsers" /></label>

        <div class="col-sm-10">
            <s:if test="null != references && references.size() > 0">
                <wpsa:subset source="references" count="10"
                             objectName="userReferences" advanced="true" offset="5"
                             pagerId="userManagerReferences">
                    <s:set var="group" value="#userReferences" />
                    <div class="text-center">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                        <%--<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />--%>
                    </div>


                    <wp:ifauthorized permission="superuser" var="canEditUser" />
                    <!--<div class="table-responsive">-->
                    <table class="table table-bordered" id="userListTable">
                        <thead>
                            <tr>
                                <th><s:text name="label.username" /></th>
                                <th class="text-center"><s:text name="label.date.lastLogin" /></th>
                                <th style="width: 20px" class="text-center"><s:text
                                        name="label.state" /></th>
                                <th style="width: 20px"
                                    class="text-center <c:out value="${canEditUser ? '' : ' hide'}" />"><s:text
                                        name="label.actions" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:iterator var="usernameVar">
                                <s:set var="userVar" value="%{getUser(#usernameVar)}" />
                                <s:if test="null == #userVar || #userVar.disabled">
                                    <s:set var="statusIconClassVar"
                                           value="%{'icon fa fa-pause text-warning'}" />
                                    <s:set var="statusTextVar"
                                           value="%{getText('note.userStatus.notActive')}" />
                                </s:if>
                                <s:elseif test="!#userVar.entandoUser">
                                    <s:set var="statusIconClassVar" value="%{'icon fa fa-minus'}" />
                                    <s:set var="statusTextVar"
                                           value="%{getText('note.userStatus.notEntandoUser')}" />
                                </s:elseif>
                                <s:elseif test="!#userVar.accountNotExpired">
                                    <s:set var="statusIconClassVar"
                                           value="%{'icon fa fa-circle-o text-danger'}" />
                                    <s:set var="statusTextVar"
                                           value="%{getText('note.userStatus.expiredAccount')}" />
                                </s:elseif>
                                <s:elseif test="!#userVar.credentialsNotExpired">
                                    <s:set var="statusIconClassVar"
                                           value="%{'icon fa fa-adjust text-warning'}" />
                                    <s:set var="statusTextVar"
                                           value="%{getText('note.userStatus.expiredPassword')}" />
                                </s:elseif>
                                <s:elseif test="!#userVar.disabled">
                                    <s:set var="statusIconClassVar"
                                           value="%{'icon fa fa-check text-success'}" />
                                    <s:set var="statusTextVar"
                                           value="%{getText('note.userStatus.active')}" />
                                </s:elseif>
                                <tr>

                                    <%-- username --%>
                                    <td><s:property value="#usernameVar" /> <%-- last login --%>
                                    <td class="text-center"><s:if
                                            test="#userVar.entandoUser && #userVar.lastAccess != null">
                                            <code
                                                title="<s:date name="#userVar.lastAccess" format="dd/MM/yyyy" />">
                                                <s:date name="#userVar.lastAccess" format="dd/MM/yyyy" />
                                            </s:if> <s:else>
                                                <span class="icon fa fa-minus text-muted"
                                                      title="<s:text name="label.none" />"></span>
                                                <span class="sr-only"><s:text name="label.none" /></span>
                                        </s:else></td>
                                        <%-- status --%>
                                    <td class="text-center"><span class="sr-only"><s:property
                                                value="#statusTextVar" /></span> <span
                                            class="<s:property value="#statusIconClassVar" />"
                                            title="<s:property value="#statusTextVar" />"></span></td>
                                        <%-- actions --%>
                                    <td
                                        class="text-center <c:out value="${canEditUser ? '' : ' hide'}" />">

                                        <div class="dropdown dropdown-kebab-pf">
                                            <button class="btn btn-menu-right dropdown-toggle"
                                                    type="button" data-toggle="dropdown" aria-haspopup="true"
                                                    aria-expanded="false">
                                                <span class="fa fa-ellipsis-v"></span>
                                            </button>
                                            <ul class="dropdown-menu dropdown-menu-right">
                                                <c:if test="${canEditUser == 'true'}">
                                                    <li>
                                                        <%-- edit user button --%> <a
                                                            href="<s:url namespace="/do/User" action="edit"><s:param name="username" value="#usernameVar"/></s:url>"
                                                            title="<s:text name="label.edit" />: <s:property value="#usernameVar" />">
                                                            <span class="sr-only"><s:text name="label.edit" /></span>
                                                            <s:text name="label.edit" />
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <%-- edit authorization button --%> <a
                                                            href="<s:url action="edit" namespace="/do/User/Authorization"><s:param name="username" value="#usernameVar"/></s:url>"
                                                            title="<s:text name="note.configureAuthorizationsFor" />: <s:property value="#usernameVar" />">
                                                            <span class="sr-only"> <s:text
                                                                    name="note.configureAuthorizationsFor" />: <s:property
                                                                    value="#usernameVar" />
                                                            </span> <s:text name="note.configureAuthorizationsFor" />: <s:property
                                                                value="#usernameVar" />
                                                        </a>
                                                    </li>
                                                </c:if>
                                            </ul>
                                        </div>
                                    </td>
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>
                    <!--</div>-->
                    <div class="text-center">
                        <s:include
                            value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                    </div>
                </wpsa:subset>
            </s:if>
            <s:else>
                <p class="margin-none">
                    <s:text name="note.role.referencedUsers.empty" />
                </p>
            </s:else>
        </div>
    </div>
</s:form>
