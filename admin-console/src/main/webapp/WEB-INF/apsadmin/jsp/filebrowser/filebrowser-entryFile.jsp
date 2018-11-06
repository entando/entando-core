<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li><a href="<s:url action="list" namespace="/do/FileBrowser" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="menu.filebrowserAdmin" />"><s:text
                name="menu.filebrowserAdmin" /></a></li>
    <li class="page-title-container"><s:text name="label.addTextFile" /></li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="label.addTextFile" />
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

    <s:form action="save" namespace="/do/FileBrowser"
            cssClass="margin-base-top form-horizontal">

        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button type="button" class="close" data-dismiss="alert"
                        aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="message.title.FieldErrors" />
                <ul class="margin-base-top">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button class="close" data-dismiss="alert">
                    <span class="icon icon-remove"></span>
                </button>
                <h2 class="h4 margin-none">
                    <s:text name="message.title.ActionErrors" />
                </h2>
                <ul class="margin-base-top">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>

        <div class="form-group">
            <div class="col-xs-12">
                <s:hidden name="currentPath" />
                <s:hidden name="protectedFolder" />
                <s:if test="strutsAction == 2">
                    <s:hidden name="filename" />
                </s:if>
                <s:hidden name="strutsAction" />

                <s:if test="strutsAction == 2">
                    <%-- ** EDIT text file ** --%>
                    <div class="form-group">
                        <div class="col-xs-12">
                            <label class="col-sm-2 control-label"><s:text
                                    name="label.name" /></label>
                            <div class="col-sm-10">
                                <p class="form-control-static">
                                    <s:property value="filename" />
                                    &#32; <a title="Download: <s:property value="filename"/>"
                                             href="<s:url namespace="/do/FileBrowser" action="download" >
                                                 <s:param name="currentPath"><s:property escapeHtml="true" value="%{currentPath}"/></s:param>
                                                 <s:param name="filename"><s:property escapeHtml="false" value="filename"/></s:param>
                                                 <s:param name="protectedFolder"><s:property escapeHtml="false" value="%{protectedFolder}"/></s:param>
                                             </s:url>">
                                        <span class="icon fa fa-download"></span> <span
                                            class="sr-only"> <s:text name="label.download" />: <s:property
                                                value="#fileVar.name" />
                                        </span>
                                    </a>
                                </p>
                            </div>
                        </div>
                    </div>
                </s:if>

                <s:elseif test="strutsAction == 11">
                    <%-- ** NEW text file ** --%>
                    <div class="form-group">
                        <div class="col-xs-12">
                            <label for="filename"
                                   class="col-sm-2 control-label display-block"> <s:text
                                    name="label.name" />
                            </label>
                            <div class="row col-sm-10">
                                <div class="col-md-9">
                                    <wpsf:textfield name="filename" id="filename"
                                                    cssClass="form-control col-md-9" />
                                </div>
                                <div class="col-md-3">
                                    <label for="file-extension" class="sr-only"><s:text
                                            name="label.extension" /></label>
                                    <wpsf:select id="file-extension" list="textFileTypes"
                                                 name="textFileExtension" cssClass="form-control col-md-3" />
                                </div>
                            </div>
                        </div>
                    </div>
                </s:elseif>

            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-12">
                <label class="col-sm-2 control-label" for="file-content"><s:text
                        name="label.content" /></label>
                <div class="col-sm-10">
                    <wpsf:textarea cssClass="form-control" id="file-content"
                                   placeholder="file content here..." name="fileText" rows="20"
                                   cols="50" />
                </div>
            </div>
        </div>
        <div class="row form-group pull-right">
            <div class="col-sm-12">
                <wpsf:submit action="list" value="%{getText('label.cancel')}"
                             cssClass="btn btn-default" />
                <wpsf:submit type="button" action="save" cssClass="btn btn-primary">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
