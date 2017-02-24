<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<div class="navbar-header">
    <button type="button" class="navbar-toggle">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
    </button>
    <a href="<s:url action="main" namespace="/do" />" class="navbar-brand">
        <img class="navbar-brand-icon logo-entando" src="<wp:resourceURL />administration/img/entando-logo.svg" alt="Entando 4.3" />
    </a>
</div>
<nav class="collapse navbar-collapse">

    <ul class="nav navbar-nav navbar-right navbar-iconic">

        <li class="dropdown">
            <a class="dropdown-toggle nav-item-iconic" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                <span title="Info" class="fa pficon-info"></span>
                <span class="caret"></span>
            </a>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                <li><a href="#">About</a></li>
                <li><a href="#">Licence</a></li>
            </ul>
        </li>
        <li id="userDropdown" class="dropdown">
            <a class="dropdown-toggle nav-item-iconic" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                <span title="Username" class="fa pficon-user"></span>
                <c:set var="current_username" value="${sessionScope.currentUser}" />
                <c:if test="${null != sessionScope.currentUser.profile}">
                    <c:set var="current_username" value="${sessionScope.currentUser.profile.displayName}" />
                </c:if>
                <c:out value="${current_username}" />
                <span class="caret"></span>
            </a>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenu2">
                <c:if test="${sessionScope.currentUser.japsUser}">
                    <li>
                        <a href="<s:url action="editProfile" namespace="/do/CurrentUser" />">
                            <i class="fa fa-id-card-o" aria-hidden="true"></i>
                            <s:text name="note.changeYourPassword" />
                        </a>
                    </li>
                </c:if>
                   <%--<li>
                    <a href="<s:url value="/" />" title="<s:text name="note.goToPortal" /> ( <s:text name="note.sameWindow" /> )">
                        <span class="icon fa fa-globe fa-fw"></span>&#32;
                        <s:text name="note.goToPortal" />
                    </a>
                </li>--%>
                <li>
                    <a href="<s:url action="logout" namespace="/do" />">
                        <i class="fa fa-sign-out" aria-hidden="true"></i>&#32;
                        <s:text name="menu.exit" />
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</nav>