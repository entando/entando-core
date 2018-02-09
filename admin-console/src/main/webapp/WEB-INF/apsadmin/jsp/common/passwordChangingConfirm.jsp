<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li class="page-title-container">
        <s:text name="title.changePassword" />
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.changePassword" />
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">
    <div class="alert alert-success">
        <strong><s:text name="messages.confirm" /></strong>
        &#32;<s:text name="message.passwordChanged" />
        <s:if test="!#session.currentUser.credentialsNotExpired">
            &emsp;
            <a class="alert-link" href="<s:url action="logout" />" >
                <s:text name="note.login.again" />
            </a>
        </s:if>
    </div>
</div>



