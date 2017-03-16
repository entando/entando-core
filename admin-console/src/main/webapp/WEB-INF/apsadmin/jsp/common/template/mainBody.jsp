<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>



<div id="main" role="main">
    <!--<h1><s:text name="title.activityStream" /></h1>-->
    <s:set var="currentUsernameVar"><c:out value="${sessionScope.currentUser.username}" /></s:set>
        <wpsa:activityStream var="activityStreamListVar" />
    <s:if test="null != #activityStreamListVar && #activityStreamListVar.size() != 0">
        <div id="stream-updates-alert" aria-live="polite" class="hide btn-primary btn-links">
            <s:text name="activity.stream.note.show.updates" /> (<span class="n"></span>)
        </div>
        <ul class="list-unstyled" id="activity-stream">
            <s:include value="/WEB-INF/apsadmin/jsp/common/activity-stream/inc/stream.jsp" />
        </ul>

        <div class="col-xs-12 text-center">
            <button class="btn btn-primary btn-submit-spacer btn-sm" data-entando="load-more-button" data-loading-text="<s:text name="activity.stream.note.loading" />&hellip;"><s:text name="activity.stream.note.loadMore" /></button>
        </div>

    </s:if>
    <s:else>
        <s:text name="activity.stream.note.no.activity" />
    </s:else>
</div> 
</html>


