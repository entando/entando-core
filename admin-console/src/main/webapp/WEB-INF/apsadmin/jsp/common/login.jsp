<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<!DOCTYPE html>

<html lang="en">
<head>
    <title>Entando - Log in</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" href="<wp:resourceURL />administration/img/favicon-entando.png">

    <!--CSS inclusions-->
    <link rel="stylesheet" href="<wp:resourceURL />administration/bootstrap/css/bootstrap.min.css" media="screen"/>
    <link rel="stylesheet" href="<wp:resourceURL />administration/css/entando-admin-console-default-theme.css"/>
    <!--JS inclusions-->
    <script src="<wp:resourceURL />administration/js/jquery-3.4.1.min.js"></script>
    <script src="<wp:resourceURL />administration/js/jquery-migrate-3.0.1.min.js"></script>
    <script src="<wp:resourceURL />administration/bootstrap/js/bootstrap.min.js"></script>
</head>

<body id="background-full" class="display-table ">
<s:set var="passwordFieldErrorsVar" value="%{fieldErrors['password']}"/>
<s:set var="passwordHasFieldErrorVar" value="#passwordFieldErrorsVar != null && !#passwordFieldErrorsVar.isEmpty()"/>
<s:set var="controlGroupErrorClassVar" value="''"/>
<s:set var="usernameFieldErrorsVar" value="%{fieldErrors['username']}"/>
<s:set var="usernameHasFieldErrorVar" value="#usernameFieldErrorsVar != null && !#usernameFieldErrorsVar.isEmpty()"/>
<s:set var="controlGroupErrorClassVar" value="''"/>

<div class="display-cell ">
    <div class="col-md-6 v-align ">
        <div class="center1">
            <img class="logo-entando-login" src="<wp:resourceURL />administration/img/entando-logo.svg"/>
            <p class="ux_brand"><strong>THE LEADING PLATFORM</strong></p>
            <p class="ux_brand_subtitle"> FOR CLOUD NATIVE APPLICATIONS</p>
            <div class="spacer-login"></div>
            <div class="entando-intro">
                We help customers bring to production enterprise applications that are lightweight, cloud native, and highly customized, and do so significantly faster than their competitors. Learn quickly, develop easily, deploy rapidly.
            </div>
            <div class="copyright-entando">Copyright 2020 <span class="entando-sm-write">Entando</span></div>
        </div>
    </div>

    <div class="col-md-6 v-align ">
        <div class="center2">
            <s:form action="doLogin" id="form-login">
                <!-----------sezione errori----------->
                <s:if test="hasActionErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <ul class="margin-base-vertical">
                            <s:iterator value="actionErrors">
                                <li>
                                    <s:property/>
                                </li>
                            </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <s:if test="#passwordHasFieldErrorVar || #usernameFieldErrorsVar">
                    <div class="alert alert-danger alert-dismissable">
                        <ul class="margin-base-vertical">
                            <s:text name="error.user.login.credentialsEmpty"/>
                        </ul>
                    </div>
                </s:if>
                <!-----------sezione errori----------->

                <s:if test="#session.currentUser != null && #session.currentUser.username != 'guest'">
                    <h2 class="welcome-back">
                        <s:text name="note.userbar.welcome"/>&#32;
                        <s:property value="#session.currentUser"/>&nbsp;!
                    </h2>
                    <br>
                    <s:if test="!#session.currentUser.credentialsNotExpired">
                        <div class="col-xs-5">
                            <strong><s:text name="note.login.expiredPassword.intro"/></strong><br/>
                        </div>
                        <div class="col-xs-5 submit">
                            <a href="<s:url action="editPassword" />" class="btn btn-custom-login btn-warning">
                                <s:text name="note.login.expiredPassword.outro"/>
                            </a>
                        </div>
                    </s:if>
                    <s:else>
                        <wp:ifauthorized permission="enterBackend" var="checkEnterBackend"/>
                        <c:choose>
                            <c:when test="${checkEnterBackend}">
                                <div class="col-xs-5">
                                    <p class="entando-installed">
                                        <strong><s:text name="note.login.yetLogged"/></strong>
                                    </p>

                                </div>
                                <div class="col-xs-5 submit">
                                    <a href="<s:url action="main" />" class="btn btn-custom-login btn-primary">
                                        <s:text name="note.goToMain"/>
                                    </a>
                                    <a href="<s:url action="logout" namespace="/do" />"
                                       class="btn btn-custom-login btn-danger">
                                        <s:text name="menu.exit"/>
                                    </a>
                                </div>
                            </c:when>

                            <c:otherwise>
                                <div class="col-xs-5">
                                    <strong><s:text name="note.login.notAllowed"/></strong><br/>
                                </div>
                                <div class="col-xs-5 submit">
                                    <a href="<s:url action="logout" namespace="/do" />"
                                       class="btn btn-custom-login btn-danger">
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
                        <s:set var="controlGroupErrorClassVar" value="' has-error'"/>
                    </s:if>

                    <div class="form-group<s:property value="controlGroupErrorClassVar" />">
                        <label for="username" class="control-label control-label-entando">
                            <s:text name="label.username"/>
                        </label>
                        <div>
                            <wpsf:textfield name="username" id="username" cssClass="entando-input"/>
                            <s:if test="#usernameHasFieldErrorVar">
                            <span class="help-block help-block-entando"><s:iterator value="#usernameFieldErrorsVar">
                                <s:property/>
                                            </s:iterator>
                                        </s:if>
                        </div>
                    </div>

                    <s:if test="%{#passwordHasFieldErrorVar || hasActionErrors()}">
                        <s:set var="controlGroupErrorClassVar" value="' has-error'"/>
                    </s:if>

                    <div class="form-group<s:property value="controlGroupErrorClassVar" />">
                        <label for="password" class="control-label control-label-entando">
                            <s:text name="label.password"/>
                        </label>
                        <div>
                            <wpsf:password name="password" id="password" cssClass="entando-input"/>
                            <s:if test="passwordHasFieldErrorVar">
                                <span class="help-block help-block-entando"><s:iterator value="#passwordFieldErrorsVar"><s:property/></s:iterator></span>
                            </s:if>
                        </div>
                    </div>
                    <!-------------------sezione user e password validation-------------->

                    <!----------- lingue e log in button------------------>
                    <div class="login-buttons">
                        <div data-toggle="buttons">
                            <label class="btn btn-custom-login active">
                                <input type="radio" name="request_locale" value="en" checked="checked"/> English
                            </label>
                            <label class="btn btn-custom-login ">
                                <input type="radio" name="request_locale" value="it"/> Italiano
                            </label>
                        </div>
                        <wpsf:submit type="button" id="button-login" cssClass="btn btn-login pull-right">
                            <s:text name="label.signin"/>
                        </wpsf:submit>
                    </div>
                    <!----------- lingue e log in button------------------>
                </s:else>
            </s:form>
        </div>
    </div>
</body>

</html>
