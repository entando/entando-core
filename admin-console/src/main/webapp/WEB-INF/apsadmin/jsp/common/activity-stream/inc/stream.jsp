<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<% pageContext.setAttribute("carriageReturn", "\r"); %>
<% pageContext.setAttribute("newLine", "\n"); %>
<% pageContext.setAttribute("tabChar", "\t");%>
<%-- reading the list from mainBody.jsp with: <wpsa:activityStream var="activityStreamListVar" /> --%>
<c:set var="ajax" value="${param.ajax eq 'true'}" />
<s:set var="ajax" value="#attr.ajax" />
<s:if test="#ajax"><%-- ajax eh? so set the #activityStreamListVar variable accordingly --%>
    <s:set var="activityStreamListVar" value="%{getActionRecordIds()}" />
</s:if>
<s:else><%-- use the #activityStreamListVar from mainBody.jsp --%></s:else>
<c:set var="currentUsernameVar" value="${session.currentUser.username}" />
<s:set var="currentUsernameVar" value="#attr.currentUsernameVar" />
<wp:userProfileAttribute username="${currentUsernameVar}" attributeRoleName="userprofile:fullname" var="browserUserFullnameVar" />
<wp:userProfileAttribute username="${currentUsernameVar}" attributeRoleName="userprofile:email" var="browserUserEmailAttributeVar" />
<wp:ifauthorized permission="superuser" var="browserIsSuperUser" />
<wpsa:activityStreamLastUpdateDate var="lastUpdateDateVar" />
<s:date name="%{#lastUpdateDateVar}" format="yyyy-MM-dd HH:mm:ss|SSS" var="lastUpdateDateVar" />
<s:iterator value="#activityStreamListVar" var="actionLogRecordIdVar" status="currentEvent">
    <wpsa:actionLogRecord key="%{#actionLogRecordIdVar}" var="actionLogRecordVar" />
    <s:set var="usernameVar" value="#actionLogRecordVar.username" scope="page" />
    <wp:userProfileAttribute username="${usernameVar}" attributeRoleName="userprofile:fullname" var="fullnameVar" />
    <wp:userProfileAttribute username="${usernameVar}" attributeRoleName="userprofile:email" var="emailAttributeVar" />
    <s:set var="fullnameVar" value="#attr.fullnameVar" />
    <s:set var="emailAttributeVar" value="#attr.emailAttributeVar" />

    <%--<c:out value="${emailAttributeVar}" />--%>
    <li class="li-custom-avatar" data-entando-id="<s:property value="#actionLogRecordVar.id" />" data-entando-creationdate="<s:date name="#actionLogRecordVar.actionDate" format="yyyy-MM-dd HH:mm:ss|SSS" />"
        data-entando-updatedate="<s:property value="#lastUpdateDateVar" />">
        <div class="avatar-utente">
            <img alt=" " src="<s:url action="avatarStream" namespace="/do/user/avatar">
                     <s:param name="gravatarSize">36</s:param>
                     <s:param name="username" value="#actionLogRecordVar.username" />
                 </s:url>" width="36" height="36" class="img-circle media-object" />
        </div>
        <div class="avatar-utente-logger activity-stream-event <s:if test="#currentEvent.first && !#ajax">event-first</s:if>">
            <s:set var="activityStreamInfoVar" value="#actionLogRecordVar.activityStreamInfo" />
            <s:set var="authGroupNameVar" value="#activityStreamInfoVar.linkAuthGroup" scope="page" />
            <s:set var="authPermissionNameVar" value="#activityStreamInfoVar.linkAuthPermission" scope="page" />
            <wp:ifauthorized groupName="${authGroupNameVar}" permission="${authPermissionNameVar}" var="isAuthorizedVar" />
            <div class="user-notification" data-entando="ajax-update">
                <c:choose>
                    <c:when test="${isAuthorizedVar}">
                        <a  href="<s:url action="view" namespace="/do/userprofile"><s:param name="username" value="#actionLogRecordVar.username"/></s:url>"
                            title="<s:text name="label.viewProfile" />: <s:property value="#actionLogRecordVar.username" />">
                            <s:if test="null != #fullnameVar && #fullnameVar.length() > 0">
                                <s:property value="#fullnameVar" />
                            </s:if>
                            <s:else>
                                <s:property value="#actionLogRecordVar.username" />
                            </s:else>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <s:if test="null != #fullnameVar && #fullnameVar.length() > 0">
                            <s:property value="#fullnameVar" />
                        </s:if>
                        <s:else>
                            <s:property value="#actionLogRecordVar.username" />
                        </s:else>
                    </c:otherwise>
                </c:choose>
                <!--                &#32;&middot;&#32;-->
                <wpsa:activityTitle actionName="%{#actionLogRecordVar.actionName}" namespace="%{#actionLogRecordVar.namespace}" actionType="%{#activityStreamInfoVar.actionType}" />:&#32;
                <s:set var="linkTitleVar" value="%{getTitle('view/edit', #activityStreamInfoVar.objectTitles)}" />
                <c:choose>
                    <c:when test="${isAuthorizedVar}">
                        <s:url
                            action="%{#activityStreamInfoVar.linkActionName}"
                            namespace="%{#activityStreamInfoVar.linkNamespace}"
                            var="actionUrlVar">
                            <wpsa:paramMap map="#activityStreamInfoVar.linkParameters" />
                        </s:url>
                        <a class href="<s:property value="#actionUrlVar" escapeHtml="false" />"><s:property value="#linkTitleVar" /></a>
                    </c:when>
                    <c:otherwise>
                        <s:property value="#linkTitleVar" />
                    </c:otherwise>
                </c:choose>
                <wpsa:activityStreamLikeRecords recordId="%{#actionLogRecordIdVar}" var="activityStreamLikeRecordsVar" />
                <s:set value="%{#activityStreamLikeRecordsVar.containsUser(#currentUsernameVar)}" var="likeRecordsContainsUserVar" />
                <p class="time-notifications">
                    <time datetime="<s:date name="#actionLogRecordVar.actionDate" format="yyyy-MM-dd HH:mm" />" title="<s:date name="#actionLogRecordVar.actionDate" format="yyyy-MM-dd HH:mm" />" >
                        <s:date name="#actionLogRecordVar.actionDate" nice="true" />
                    </time>
                    <s:if test="#activityStreamLikeRecordsVar.size() > 0">
                        &#32;|&#32;
                        <span
                            data-toggle="tooltip"
                            data-placement="bottom"
                            data-original-title="<s:iterator value="#activityStreamLikeRecordsVar" var="activityStreamLikeRecordVar"><s:property value="#activityStreamLikeRecordVar.displayName" />&#32;
                            </s:iterator><s:text name="label.like.likesthis" />">
                            <s:property value="#activityStreamLikeRecordsVar.size()" />
                            &#32;
                            <s:text name="label.like.number" />
                        </span>
                    </s:if>
                    &#32;|&#32;
                    <%-- like / unlike --%>
                    <a class="like-notifications" data-entando="like-link" href="<s:url namespace="/do/ActivityStream" action="%{#likeRecordsContainsUserVar ? 'unlikeActivity' : 'likeActivity'}">
                           <s:param name="recordId" value="%{#actionLogRecordIdVar}" />
                       </s:url>">
                        <s:if test="%{#likeRecordsContainsUserVar}" >
                            <%--<s:text name="label.like.unlike" />--%>
                            <i class="fa fa-thumbs-down" aria-hidden="true"></i></s:if>
                        <s:else>
                            <%--<s:text name="label.like.like" />--%>
                            <i class="fa fa-thumbs-up" aria-hidden="true"></i></s:else>
                        </a>
                    </p>

                </div>

            <%-- comments section --%>
            <wpsa:activityStreamCommentRecords recordId="%{#actionLogRecordIdVar}" var="activityStreamCommentListVar" />
            <div class=""  data-entando="ajax-update">
                <h4 class="sr-only"><s:text name="activity.stream.title.comments" /></h4>
                <s:iterator value="#activityStreamCommentListVar" var="activityStreamCommentVar">
                    <div class="media comment-panel" data-entando-commentid="<s:property value="#activityStreamCommentVar.id" />" data-entando-commentdate="<s:date name="%{#activityStreamCommentVar.commentDate}" format="yyyy-MM-dd HH:mm:ss|SSS" />">
                        <a class="pull-left" href="<s:url action="view" namespace="/do/userprofile"><s:param name="username" value="#activityStreamCommentVar.username"/></s:url>" title="<s:text name="label.viewProfile" />:&#32;<s:property value="#activityStreamCommentVar.displayName" />">
                            <img class="img-circle " style="width: 26px "src="<s:url action="avatarStream" namespace="/do/user/avatar">
                                     <s:param name="gravatarSize">26</s:param>
                                     <s:param name="username" value="#activityStreamCommentVar.username" />
                                 </s:url>" />

                        </a>
                        <div class="user-notification">
                            <a class="user-notification-comment"  href="<s:url action="view" namespace="/do/userprofile"><s:param name="username" value="#activityStreamCommentVar.username"/></s:url>"
                               title="<s:text name="label.viewProfile" />:&#32;<s:property value="#activityStreamCommentVar.displayName" />">
                                <s:property value="#activityStreamCommentVar.displayName" />
                            </a>
                            <!--delete comment--> 
                            <span  class="delete-comment">
                                <s:if test="#activityStreamCommentVar.username == #currentUsernameVar || #attr.browserIsSuperUser">
                                    <a href="#delete" data-entando="delete-comment-ajax" class="pull-right btn-delete-comment">
                                        <span class="fa fa-times"></span>
                                    </a>
                                </s:if>
                            </span>

                            <%-- comment text --%>
                            <span class="media-text avatar-utente-comment">
                                <c:set var="STRING_TO_ESCAPE"><s:property value="#activityStreamCommentVar.commentText" escapeHtml="true"/></c:set>
                                <c:set var="ESCAPED_STRING" value="${fn:replace(fn:replace(fn:replace(fn:trim(STRING_TO_ESCAPE), carriageReturn, ' ') , newLine, '<br />'), tabChar, '&emsp;')}" />
                                <c:set var="ESCAPED_STRING" value="${fn:replace(ESCAPED_STRING,'<br /><br /><br />','<br />')}" />
                                <c:out value="${ESCAPED_STRING}" escapeXml="false" />
                            </span>

                            <p class="time-notifications">
                                <time datetime="<s:date name="#activityStreamCommentVar.commentDate" format="yyyy-MM-dd HH:mm" />" title="<s:date name="#activityStreamCommentVar.commentDate" format="yyyy-MM-dd HH:mm" />">
                                    <s:date name="%{#activityStreamCommentVar.commentDate}" nice="true" />
                                </time>

                            </p>
                        </div>

                    </div>
                </s:iterator>
            </div>
            <div class="comment-panel-insert"  >
                <a role="button" data-toggle="collapse" href="#openComment<s:property value="#actionLogRecordVar.id" />"  aria-controls="openComment<s:property value="#actionLogRecordVar.id" />">
                    <s:text name="activity.stream.comment.Comment" />
                </a>
            </div>

            <div class="collapse comment-box-panel" id="openComment<s:property value="#actionLogRecordVar.id" />">

                <div class="comment-box ">
                    <div class="insert-comment media <s:if test="#ajax"> hide hidden </s:if>">
                            <span class="pull-left" >
                                <img class="img-circle avatar-utente-comment-img" src="<s:url action="avatarStream" namespace="/do/user/avatar">
                                     <s:param name="gravatarSize">26</s:param>
                                     <s:param name="username" value="#currentUsernameVar" />
                                 </s:url>" />
                        </span>
                        <div class="media-body">
                            <%-- insert comment --%>
                            <form action="<s:url action="addComment" namespace="/do/ActivityStream" />">
                                <wpsf:hidden name="streamRecordId" value="%{#actionLogRecordIdVar}" />
                                <textarea  id="textarea-stream-<s:property value="#actionLogRecordIdVar" />" 
                                           class="form-control" cols="38" rows="1" placeholder="<s:property value="%{getText('activity.stream.comment.insert.tip')}" />" title="<s:property value="%{getText('activity.stream.comment.insert.tip')}" />"
                                           name="commentText" maxlength="200">
                                </textarea>
                                <wpsf:submit type="button" data-entando="add-comment-button" data-loading-text="%{getText('activity.stream.note.loading')}" value="%{getText('activity.stream.button.submit.comment')}"
                                             cssClass="btn btn-primary btn-sm pull-right btn-submit-spacer">
                                    <s:text name="activity.stream.button.submit.comment" />
                                </wpsf:submit>
                            </form>
                        </div>
                    </div>
                </div>   
            </div>
    </li>
</s:iterator>
