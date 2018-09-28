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
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.ActionErrors" /></strong>
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:iterator>
                </ul>
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
                    <s:set var="isNone" value="%{(null != systemParams && systemParams['hypertextEditor'] == 'none') || #parameters['hypertextEditor'][0] == 'none'}" />
                    <label class="btn btn-default <s:if test="#isNone"> active</s:if>">
                            <input type="radio" class="radiocheck" id="admin-settings-area-hypertextEditor_none"
                                   name="hypertextEditor" value="none" <s:if test="#isNone">checked="checked"</s:if> />
                        <s:text name="label.none"/>
                    </label>
                    <s:set var="isFckeditor" value="%{(null != systemParams && systemParams['hypertextEditor'] == 'fckeditor') || #parameters['hypertextEditor'][0] == 'fckeditor'}" />
                    <label class="btn btn-default <s:if test="#isFckeditor"> active</s:if>">
                            <input type="radio" class="radiocheck" name="hypertextEditor" value="fckeditor" <s:if test="#isFckeditor">checked="checked"</s:if> />
                        <s:text name="name.editor.ckeditor"/>
                    </label>
                </div>
            </div>
        </fieldset>
        <fieldset class="col-xs-12 settings-form">
            <h2>
                <s:text name="jacms.menu.resourceMetadataMapping" />
            </h2>
            <s:set var="resourceMetadataKeysVar" value="resourceMetadataKeys" />
            <s:iterator var="metadataKeyVar" value="#resourceMetadataKeysVar" >
            <div class="form-group">
                <div class="row">
                    <s:set var="metadataMetadataFieldNameVar" value="%{'resourceMetadata_mapping_' + #metadataKeyVar}" />
                    <div class="col-xs-2 control-label">
                        <span for="<s:property value="#metadataMetadataFieldNameVar" />">
                            <wpsf:hidden name="metadataKeys" value="%{#metadataKeyVar}" />
                            <s:text name="jacms.label.resourceMetadata" ><s:param value="#metadataKeyVar" /></s:text>
                        </span>
                    </div>
                    <div class="col-xs-2 control-label">
                        <span for="<s:property value="#metadataMetadataFieldNameVar" />">
                            <s:text name="jacms.label.resourceMetadataMapping" />
                        </span>
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" 
                           data-content="<s:text name="jacms.help.resourceMetadataMapping" ><s:param value="#metadataKeyVar" /></s:text>" data-placement="right">
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </div>
                    <div class="col-xs-6">
                        <wpsf:textfield name="%{#metadataMetadataFieldNameVar}" id="%{#metadataMetadataFieldNameVar}" value="%{buildCsv(#metadataKeyVar)}" cssClass="form-control" />
                    </div>
                    <div class="col-xs-2">
                        <wpsa:actionParam action="removeMetadata" var="actionNameVar" >
                            <wpsa:actionSubParam name="metadataKey" value="%{#metadataKeyVar}" />
                        </wpsa:actionParam>
                        <wpsf:submit type="button" action="%{#actionNameVar}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-danger pull-right">
                            <span class="pficon pficon-delete"></span>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
            </s:iterator>
            <div class="metadata-well">
                <div class="separator"></div>
                <label class="col-sm-2 section-label" for="new_metatag">
                    <s:text name="jacms.label.addResourceMetadata" />
                    <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-placement="top" data-content="<s:text name="jacms.help.addResourceMetadata" />"
                       data-original-title="">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <s:set var="fieldErrorsVar" value="%{fieldErrors['metadataKey']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <div class="col-md-4 form-group<s:property value="#controlGroupErrorClass" />">
                    <label class="col-sm-4 control-label" for="new_metadata">
                        <s:text name="label.key" />
                    </label>
                    <div class="col-sm-8">
                        <wpsf:textfield name="metadataKey" id="new_metadata" cssClass="form-control custom-input" />
                        <s:if test="#hasFieldErrorVar">
                            <span class="help-block text-danger">
                                <s:iterator value="%{#fieldErrorsVar}">
                                    <s:property />&#32;
                                </s:iterator>
                            </span>
                        </s:if>
                    </div>
                </div>
                <div class="col-md-4 form-group">
                    <label class="col-sm-4 control-label" for="new_metadata_mapping">
                        <s:text name="jacms.label.metadataMapping" />
                    </label>
                    <div class="col-sm-8">
                        <wpsf:textfield name="metadataMapping" id="new_metadata_mapping" cssClass="form-control custom-input" />
                    </div>
                </div>
                <div class="col-sm-2"> 
                    <wpsf:submit action="addMetadata" type="button" cssClass="btn btn-primary pull-right btn-position">
                        <s:text name="label.add" />
                    </wpsf:submit>
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
