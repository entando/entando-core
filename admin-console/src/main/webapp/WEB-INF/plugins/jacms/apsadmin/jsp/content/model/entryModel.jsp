<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jacms/ContentModel" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.contentModels" />">
            <s:text name="title.contentModels" />
        </a>
    </li>
    <li class="page-title-container">
        <s:if test="strutsAction == 1">
            <s:text name="title.contentModels.new" />
        </s:if> <s:if test="strutsAction == 2">
            <s:text name="title.contentModels.edit" />
        </s:if>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:if test="strutsAction == 1">
            <s:text name="title.contentModels.new" />
        </s:if>
        <s:if test="strutsAction == 2">
            <s:text name="title.contentModels.edit" />
        </s:if>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.contentManagement.help" />"
               data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" accesskey="" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div id="main" role="main">
    <s:form action="save" namespace="/do/jacms/ContentModel"
            cssClass="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.FieldErrors" /></strong>
            </div>
        </s:if>
        <p class="sr-only">
        <wpsf:hidden name="strutsAction" />
        <s:if test="strutsAction == 2">
            <wpsf:hidden name="modelId" />
        </s:if>
    </p>

    <div class="form-group<s:if test="strutsAction == 1 && null == contentType"> has-warning</s:if>">
            <label for="contentType" class="col-sm-2 control-label">
            <s:text name="contentModel.type" />
        </label>
        <div class="col-sm-10">
            <div class="input-group">
                <wpsf:select id="contentType" list="smallContentTypes" name="contentType" listKey="code" listValue="descr" cssClass="form-control" headerKey="" headerValue="%{getText('note.choose')}" />
                <span class="input-group-btn">
                    <s:if test="strutsAction == 1 && null == contentType">
                        <wpsf:submit type="button" action="lockContentType" cssClass="btn btn-primary" value="%{getText('label.set')}" />
                    </s:if>
                    <s:else>
                        <wpsf:submit type="button" action="lockContentType" cssClass="btn btn-primary" value="%{getText('label.change')}" />
                    </s:else>
                </span>
            </div>
            <s:if test="strutsAction == 1 && null == contentType">
                <span class="help-block">
                    <span class="icon fa fa-info-circle"></span>&#32;
                    <s:text name="note.contentModel.assist.intro" />
                </span>
            </s:if>
        </div>
    </div>


    <div class="form-group<s:property value="controlGroupErrorClassVar" />">
        <s:set var="modelIdFieldErrorsVar" value="%{fieldErrors['modelId']}" />
        <s:set var="modelIdHasFieldErrorVar" value="#modelIdFieldErrorsVar!= null && !#modelIdFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#modelIdHasFieldErrorVar ? ' has-error' : ''}" />

        <label class="col-sm-2 control-label" for="modelId">
            <s:text name="contentModel.code" /> <i class="fa fa-asterisk required-icon"></i>
            <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-placement="top" data-content="<s:text name="title.contentModel.code.help" />" data-original-title="">
                <span class="fa fa-info-circle"></span>
            </a>
        </label>

        <div class="col-sm-10">
            <wpsf:textfield name="modelId" id="modelId" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
            <s:if test="#modelIdHasFieldErrorVar">
                <p class="text-danger padding-small-vertical">
                    <s:iterator value="#modelIdFieldErrorsVar">
                        <s:property />
                    </s:iterator>
                </p>
            </s:if>
        </div>
    </div>
    <div class="form-group<s:property value="controlGroupErrorClassVar" />">
        <s:set var="descriptionFieldErrorsVar" value="%{fieldErrors['description']}" />
        <s:set var="descriptionHasFieldErrors"
               value="#descriptionFieldErrorsVar!= null && !#descriptionFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar"
               value="%{#descriptionHasFieldErrors ? ' has-error' : ''}" />
        <label class="col-sm-2 control-label" for="description">
            <s:text name="label.description" /> <i class="fa fa-asterisk required-icon"></i>
            <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-placement="top" data-content="<s:text name="title.contentModel.name.help" />" data-original-title="">
                <span class="fa fa-info-circle"></span>
            </a>
        </label>
        <div class="col-sm-10">
            <wpsf:textfield name="description" id="description" cssClass="form-control" />
            <s:if test="#descriptionHasFieldErrors">
                <p class="text-danger padding-small-vertical">
                    <s:iterator value="#descriptionFieldErrorsVar">
                        <s:property />
                    </s:iterator>
                </p>
            </s:if>
        </div>
    </div>
    <div class="form-group<s:property value="controlGroupErrorClassVar" />">
        <s:set var="contentShapeFieldErrorsVar"
               value="%{fieldErrors['contentShape']}" />
        <s:set var="contentShapeHasFieldErrorVar"
               value="#contentShapeFieldErrorsVar != null && !#contentShapeFieldErrorsVar.isEmpty()" />
        <label class="col-sm-2 control-label" for="contentShape">
            <s:text  name="contentModel.label.shape" /> <i class="fa fa-asterisk required-icon"></i>


        </label>

        <div class="col-sm-10">

            <div class="mb-10">
                <a href="#" id="popover-inline-editing-assist" class="btn btn-success" data-placement="right" data-trigger="focus" data-toggle="popover" data-html="true" title="<s:text name="INLINE.EDITING.ASSIST" />" data-content="<s:text name="inline.edit.assist.help" />">
                    <s:text name="INLINE.EDITING.ASSIST" />
                </a>
            </div>

            <div class="display-block">
                <s:textarea name="contentShape" id="contentShape"  cssClass="form-control" />

                <s:if test="#contentShapeHasFieldErrorVar">
                    <p class="text-danger padding-small-vertical">
                        <s:iterator value="#contentShapeFieldErrorsVar">
                            <s:property />
                        </s:iterator>
                    </p>
                </s:if>
                <span class="text text-success">
                    <span class="icon fa fa-info-circle"></span>&#32;
                    <s:if  test="strutsAction == 2 || (strutsAction == 1 && null != contentType)">
                        (<s:text name="note.contentModel.help" />)&#32;</s:if>
                    <s:text name="note.contentModel.contentAssist" />:&#32;
                    <em class="important"> <s:text name="label.on" /></em>.&#32;
                    <s:if test="strutsAction == 2 || (strutsAction == 1 && null != contentType)">
                        [<s:text name="note.contentModel.attributeHelp" />:&#32;
                        <em class="important"><s:text name="label.on" /></em>]
                    </s:if>
                    <s:else>
                        [<s:text name="note.contentModel.attributeHelp" />:&#32;
                        <em class="important"><s:text name="label.off" /></em>]
                    </s:else>
                </span>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="newModel_stylesheet">
            <s:text name="contentModel.label.stylesheet" />
        </label>
        <div class="col-sm-10">
            <wpsf:textfield name="stylesheet" id="newModel_stylesheet"  cssClass="form-control" />
        </div>
    </div>

    <div class="form-group">
        <div class="col-xs-12">
            <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </div>
</s:form>

</div>
<style>
    .popover {
        max-width: 600px;
    }
</style>
