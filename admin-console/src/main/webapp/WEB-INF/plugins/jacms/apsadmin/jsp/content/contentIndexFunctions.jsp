<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.app"/>
    </li>
    <li>
        <s:text name="breadcrumb.jacms"/>
    </li>
    <li class="page-title-container">
        <s:text name="menu.contents.settings" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="menu.contents.settings" />
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="note.reload.contentReferences.help" />" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">
    <s:form class="form-horizontal" action="updateSystemParams">
        <s:if test="hasActionMessages()">
            <div class="alert alert-success alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-ok"></span>
                <strong><s:text name="messages.confirm"/></strong>
                <s:iterator value="actionMessages">
                    <span><s:property escapeHtml="false"/></span>
                </s:iterator>
            </div>
        </s:if>

        <div class="form-group">
            <div class="col-xs-2 control-label">
                <span class="display-block"><s:text name="note.reload.contentReferences.start"/></span>
            </div>
            <div class="col-xs-10 ">
                <div class="btn-group">
                    <s:if test="contentManagerStatus == 1">
                        <p class="text-info">
                            <a class="btn btn-primary" href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />" title="<s:text name="note.reload.contentReferences.refresh" />">
                                <s:text name="label.refresh" />
                            </a>
                            &#32;(<s:text name="note.reload.contentReferences.status.working" />)
                        </p>
                    </s:if>
                    <s:else>
                        <p>
                            <a class="btn btn-primary" href="<s:url action="reloadContentsReference" namespace="/do/jacms/Content/Admin" />">
                                <s:text name="note.reload.contentReferences.start" />
                            </a>
                            &#32;(
                            <s:if test="contentManagerStatus == 2">
                                <span class="text-info"><s:text name="note.reload.contentReferences.status.needToReload" /></span>
                            </s:if>
                            <s:elseif test="contentManagerStatus == 0">
                                <s:text name="note.reload.contentReferences.status.ready" />
                            </s:elseif>
                            )
                        </p>
                    </s:else>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-2 control-label">
                <span class="display-block"><s:text name="title.reload.contentIndexes"/></span>
            </div>
            <div class="col-xs-10 ">
                <div class="btn-group">
                    <s:if test="searcherManagerStatus == 1">
                        <p class="text-info">
                            <a class="btn btn-primary" href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />" title="<s:text name="note.reload.contentIndexes.refresh" />">
                                <s:text name="label.refresh" />
                            </a>
                            &#32;(<s:text name="note.reload.contentIndexes.status.working" />)
                        </p>
                    </s:if>
                    <s:else>
                        <p>
                            <a class="btn btn-primary" href="<s:url action="reloadContentsIndex" namespace="/do/jacms/Content/Admin" />">
                                <s:text name="note.reload.contentIndexes.start" />
                            </a>
                            &#32;(
                            <s:if test="searcherManagerStatus == 2">
                                <span class="text-warning"><s:text name="note.reload.contentIndexes.status.needToReload" /></span>
                            </s:if>
                            <s:elseif test="searcherManagerStatus == 0">
                                <span><s:text name="note.reload.contentIndexes.status.ready" /></span>
                            </s:elseif>
                            )
                        </p>
                    </s:else>
                    <s:if test="lastReloadInfo != null">
                        <p class="text-info">
                            <s:text name="note.reload.contentIndexes.lastOn.intro" />&#32;<span class="important"><s:date name="lastReloadInfo.date" format="dd/MM/yyyy HH:mm" /></span>,
                            <s:if test="lastReloadInfo.result == 0">
                                <span class="text-error"><s:text name="note.reload.contentIndexes.lastOn.ko" /></span>.
                            </s:if>
                            <s:else>
                                <s:text name="note.reload.contentIndexes.lastOn.ok" />.
                            </s:else>
                        </p>
                    </s:if>
                </div>
            </div>
        </div>


        <fieldset class="form-group">
            <div class="col-xs-2 control-label">
                <span class="display-block"><s:text name="label.chooseYourEditor"/></span>
            </div>
            <div class="col-xs-10 ">
                <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-default <s:if test="systemParams['hypertextEditor'] == 'none'"> active</s:if>">
                            <input type="radio" class="radiocheck" id="admin-settings-area-hypertextEditor_none"
                                   name="hypertextEditor"
                                   value="none"
                            <s:if test="systemParams['hypertextEditor'] == 'none'">checked="checked"</s:if> />
                        <s:text name="label.none"/>
                    </label>
                    <label class="btn btn-default <s:if test="systemParams['hypertextEditor'] == 'fckeditor'"> active</s:if>">
                            <input type="radio" class="radiocheck"
                                   id="admin-settings-area-hypertextEditor_fckeditor" name="hypertextEditor"
                                   value="fckeditor"
                            <s:if test="systemParams['hypertextEditor'] == 'fckeditor'">checked="checked"</s:if> />
                        <s:text name="name.editor.ckeditor"/>
                    </label>
                </div>
            </div>
        </fieldset>

        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save"/>
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
