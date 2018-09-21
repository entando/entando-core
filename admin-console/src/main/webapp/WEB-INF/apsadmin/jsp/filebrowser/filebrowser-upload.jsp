<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li><a href="<s:url action="list" namespace="/do/FileBrowser" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="menu.filebrowserAdmin" />"><s:text
                name="menu.filebrowserAdmin" /></a></li>
    <li class="page-title-container"><s:text name="label.upload" /></li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="label.upload" />
        <span class="pull-right"> <a tabindex="0" role="button"
                                     data-toggle="popover" data-trigger="focus" data-html="true" title=""
                                     data-content="<s:text name="page.filebrowser.help" />" data-placement="left"
                                     data-original-title=""> <i class="fa fa-question-circle-o"
                                       aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div id="main">
    <s:include
        value="/WEB-INF/apsadmin/jsp/filebrowser/include/breadcrumbs.jsp" />
    <s:form action="upload" namespace="/do/FileBrowser" method="post"
            enctype="multipart/form-data"
            cssClass="margin-base-top form-horizontal">

        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.FieldErrors" /></strong>
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <div class="col-xs-12">            
            <div class="form-group">
                <div class="col-sm-5 col-sm-offset-2">
                    <div id="add-resource-button">
                        <button type="button" id="add-fields" >    <span class="fa fa-plus-square-o"></span> 
                            <s:text name="filebrowser.label.add-fileinput" />
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12">
            <s:hidden name="currentPath" />
            <s:hidden name="strutsAction" />
            <s:hidden name="protectedFolder" />

            <div class="form-group">

                <div class="col-sm-5 col-sm-offset-2">                                        

                    <s:label id="fileUpload_0_label" for="fileUpload_0" class="btn btn-default" key="filebrowser.label.button-choose-file" />
                    <s:file name="upload" id="fileUpload_0" cssClass="input-file-button file" label="filebrowser.label.file" />
                    <span id="fileUpload_0_selected" >
                        <s:text name="filebrowser.label.no-file-selected" />
                    </span>

                </div> 
            </div>

            <div id="fields-container" >
            </div>
        </div>

    </div>

    <div class="row form-group pull-right">
        <div class="col-sm-12">
            <wpsf:submit type="button" action="list" cssClass="btn btn-default">
                <s:text name="label.cancel" />
            </wpsf:submit>
            <wpsf:submit type="button" cssClass="btn btn-primary">
                <span class="icon fa fa-save"></span>
                &#32;
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </div>

</s:form>
</div>

<template id="hidden-fields-template">

    <div class="form-group">
        <div class="col-sm-5 col-sm-offset-2">
            <label id="newFileUpload_label" for="newFileUpload" class="btn btn-default" >
                <s:text name="filebrowser.label.button-choose-file" />
            </label>
                <s:file name="upload" id="newFileUpload" cssClass="input-file-button file" label="filebrowser.label.file" />
            <span id="newFileUpload_selected"><s:text name="filebrowser.label.no-file-selected" />
            </span>
        </div> 


        <button type="button" class="btn-danger delete-fields " 
                title="<s:text name="filebrowser.label.remove-fileinput" />"
                >    <span class="fa fa-times white"></span> 
        </button>


    </div>

</template>

<s:include value="/WEB-INF/apsadmin/jsp/filebrowser/include/fileUploadAddFields.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/filebrowser/include/fileUploadFieldLabelI18n.jsp" />
