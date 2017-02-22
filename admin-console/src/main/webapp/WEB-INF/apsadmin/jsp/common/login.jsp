<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<!DOCTYPE html>

<html lang="en"  class="login-pf">
    <head>

        <title>Entando - Log in</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta charset="utf-8" />
        <link rel="shortcut icon" href="<wp:resourceURL />administration/img/favicon-entando.png">

        <jsp:include page="/WEB-INF/apsadmin/jsp/common/inc/header-include.jsp" />

    </head>
    <body>

        <s:set var="passwordFieldErrorsVar" value="%{fieldErrors['password']}" />
        <s:set var="passwordHasFieldErrorVar" value="#passwordFieldErrorsVar != null && !#passwordFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="''" />

        <s:set var="usernameFieldErrorsVar" value="%{fieldErrors['username']}" />
        <s:set var="usernameHasFieldErrorVar" value="#usernameFieldErrorsVar != null && !#usernameFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="''" />

        <span id="badge">
            <img class="logo-entando-login" src="<wp:resourceURL />administration/img/entando-logo.svg" />
        </span>
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div id="brand">
                        <p class="ux_brand"><strong>THE DXP PLATFORM &nbsp;</strong>FOR UX CONVERGENCE</p>
                    </div>
                </div>

                <div class="col-sm-7 col-md-6 col-lg-5 login">
                    <s:form action="doLogin" cssClass="form-horizontal">
                        <s:if test="hasActionErrors()">
                            <div class="alert alert-danger alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                                    <span class="pficon pficon-close"></span>
                                </button>
                                <span class="pficon pficon-error-circle-o"></span>
                                <ul class="margin-base-vertical">
                                    <s:iterator value="actionErrors">
                                        <li><s:property /></li>
                                        </s:iterator>
                                </ul>
                            </div>
                        </s:if>

                        <s:if test="#passwordHasFieldErrorVar || #usernameFieldErrorsVar">
                            <div class="alert alert-danger alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                                    <span class="pficon pficon-close"></span>
                                </button>
                                <span class="pficon pficon-error-circle-o"></span>
                                <ul class="margin-base-vertical">
                                    <s:text name="error.user.login.credentialsEmpty" />
                                </ul>
                            </div>
                        </s:if>

                        <s:if test="#session.currentUser != null && #session.currentUser.username != 'guest'">
                            <h2 class="welcome-back"><s:text name="note.userbar.welcome"/>&#32;<s:property value="#session.currentUser" />&nbsp;!</h2><br>

                            <s:if test="!#session.currentUser.credentialsNotExpired">
                                <div class="col-xs-8 col-sm-8 col-md-8">
                                    <strong><s:text name="note.login.expiredPassword.intro" /></strong><br />
                                </div>
                                <div class="col-xs-4 col-sm-4 col-md-4 submit">
                                    <a href="<s:url action="editPassword" />" class="btn btn-warning">
                                        <s:text name="note.login.expiredPassword.outro" />
                                    </a>
                                </div>
                            </s:if>
                            <s:else>

                                <wp:ifauthorized permission="enterBackend" var="checkEnterBackend" />
                                <c:choose>
                                    <c:when test="${checkEnterBackend}">

                                        <div class="col-xs-8 col-sm-8 col-md-8">
                                            <p class="entando-installed"><strong><s:text name="note.login.yetLogged" /></strong></p><br />
                                        </div>
                                        <div class="col-xs-4 col-sm-4 col-md-4 submit">
                                            <a href="<s:url action="main" />" class="btn btn-primary">
                                                <s:text name="note.goToMain" />
                                            </a>
                                            <a href="<s:url action="logout" namespace="/do" />" class="btn btn-danger">
                                                <s:text name="menu.exit"/>
                                            </a>
                                        </div>
                                    </c:when>

                                    <c:otherwise>
                                        <div class="col-xs-8 col-sm-8 col-md-8">
                                            <strong><s:text name="note.login.notAllowed" /></strong><br />
                                        </div>
                                        <div class="col-xs-4 col-sm-4 col-md-4 submit">
                                            <a href="<s:url action="logout" namespace="/do" />" class="btn btn-danger">
                                                <s:text name="menu.exit"/>
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </s:else>
                        </s:if>
                        <s:else>

                            <!-------------------sezione user e password validation-------------->

                            <s:if test="%{#usernameHasFieldErrorVar || hasActionErrors()}">
                                <s:set var="controlGroupErrorClassVar" value="' has-error'" />
                            </s:if>

                            <div class="form-group<s:property value="controlGroupErrorClassVar" />">
                                <label for="username" class="col-sm-2 col-md-2 control-label control-label-entando"><s:text name="label.username" /></label>
                                <div class="col-sm-10 col-md-10">
                                    <wpsf:textfield name="username" id="username" cssClass="form-control" placeholder="%{getText('label.username')}" />
                                    <s:if test="#usernameHasFieldErrorVar">
                                        <span class="help-block help-block-entando"><s:iterator value="#usernameFieldErrorsVar"><s:property /></s:iterator></s:if>
                                        </div>
                                    </div>

                            <s:if test="%{#passwordHasFieldErrorVar || hasActionErrors()}">
                                <s:set var="controlGroupErrorClassVar" value="' has-error'" />
                            </s:if>

                            <div class="form-group<s:property value="controlGroupErrorClassVar" />">
                                <label for="password"class="col-sm-2 col-md-2 control-label control-label-entando"><s:text name="label.password" /></label>
                                <div class="col-sm-10 col-md-10">
                                    <wpsf:password name="password" id="password" cssClass="form-control" placeholder="%{getText('label.password')}" />
                                    <s:if test="passwordHasFieldErrorVar">
                                        <span class="help-block help-block-entando"><s:iterator value="#passwordFieldErrorsVar"><s:property /></s:iterator></span>
                                    </s:if>
                                </div>
                            </div><!-------------------sezione user e password validation-------------->

                            <!----------- lingue e log in button------------------>
                            <div class="form-group">
                                <div class="col-xs-8 col-sm-offset-2 col-sm-6 col-md-offset-2 col-md-6">
                                    <div class="btn-group" data-toggle="buttons">
                                        <label class="btn btn-default active">
                                            <input type="radio" name="request_locale" value="en" checked="checked" /> English
                                        </label>
                                        <label class="btn btn-default">
                                            <input type="radio" name="request_locale" value="it" /> Italiano
                                        </label>
                                    </div>
                                </div>
                                <div class="col-xs-4 col-sm-4 col-md-4 submit">
                                    <wpsf:submit type="button" cssClass="btn btn-primary">
                                        <s:text name="label.signin" />
                                    </wpsf:submit>
                                </div>
                            </div> <!----------- lingue e log in button------------------>
                        </s:else>
                    </s:form>
                </div>

                <div class="col-sm-5 col-md-6 col-lg-7 details">
                    <p><strong>Welcome to Entando,</strong>
                        the lightest open source, enterprise ready Digital Experience Platform (DXP) for user experience convergence to build next generation applications. Entando unifies user experience across different applications, people, devices while reducing technical complexity, time-to-market and project development costs.
                </div>

            </div>
        </div>
    </body>
</html>
