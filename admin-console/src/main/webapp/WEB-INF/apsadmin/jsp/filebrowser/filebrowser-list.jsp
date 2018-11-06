<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li class="page-title-container"><s:text
            name="menu.filebrowserAdmin" /></li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="menu.filebrowserAdmin" />
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
    <s:set var="currentPath" value="%{currentPath}" />
    <s:set var="filesAttributes" value="filesAttributes" />
    <s:if test="%{null != getProtectedFolder()}">
        <div class="pull-right" style="margin-bottom: 5px">
            <p class="margin-large-top btn-group pull-right">
                <a class="btn btn-primary"
                   href="<s:url namespace="/do/FileBrowser" action="uploadNewFileEntry" >
                       <s:param name="currentPath"><s:property escapeHtml="true" value="%{#currentPath}"/></s:param>
                       <s:param name="protectedFolder"><s:property value="%{getProtectedFolder()}"/></s:param>
                   </s:url>">
                    <span class="icon fa fa-upload"></span>&#32;<s:text
                        name="label.upload" />
                </a> <a class="btn btn-primary pull-right"
                        href="<s:url namespace="/do/FileBrowser" action="newFileEntry" >
                            <s:param name="currentPath"><s:property escapeHtml="true" value="%{#currentPath}"/></s:param>
                            <s:param name="protectedFolder"><s:property value="%{getProtectedFolder()}"/></s:param>
                        </s:url>">
                    <span class="icon fa fa-file-text"></span>&#32;<s:text
                        name="label.addTextFile" />
                </a> <a class="btn btn-primary pull-right"
                        href="<s:url namespace="/do/FileBrowser" action="newDirEntry" >
                            <s:param name="currentPath"><s:property escapeHtml="true" value="%{#currentPath}"/></s:param>
                            <s:param name="protectedFolder"><s:property value="%{getProtectedFolder()}"/></s:param>
                        </s:url>">
                    <span class="icon fa fa-folder"></span>&#32;<s:text
                        name="label.createFolder" />
                </a>
            </p>
        </div>
    </s:if>

    <s:include
        value="/WEB-INF/apsadmin/jsp/filebrowser/include/breadcrumbs.jsp" />
    
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
    
    <s:if test="#filesAttributes.length>0">
        <table class="table table-striped table-hover">
            <thead>
                <tr class="sr-only">
                    <th class="control-label col-lg-1 col-md-1"></th>
                    <th class="text-nowrap"><s:text name="label.name" /></th>
                    <th class="control-label col-lg-1 col-md-1 text-center"><s:text
                            name="label.size" /></th>
                    <th class="control-label col-lg-2 col-md-2 text-center"><s:text
                            name="label.lastEdit" /></th>
                    <th class="control-label col-lg-1 col-md-1 text-center"><s:text
                            name="label.actions" /></th>
                </tr>
                <s:if test="%{null != getProtectedFolder()}">
                    <tr>
                        <th></th>
                        <th><s:if test="currentPath!=''">
                                <s:set var="isUpProtectedFileVar" value="protectedFolder" />
                            </s:if> <a
                                href="<s:url namespace="/do/FileBrowser" action="list" >
                                    <s:param name="protectedFolder" value="#isUpProtectedFileVar" />
                                    <s:param name="currentPath"><s:property escapeHtml="true" value="breadCrumbsTargets.get(breadCrumbsTargets.size()-2).key"/></s:param>
                                </s:url>">
                                <span class="icon fa fa-share fa-rotate-270"></span> &#32; up ..
                            </a></th>
                        <th class="control-label col-lg-1 col-md-1 text-center"><s:text
                                name="label.size" /></th>
                        <th class="control-label col-lg-2 col-md-2 text-center"><s:text
                                name="label.lastEdit" /></th>
                        <th class="control-label col-lg-1 col-md-1 text-center"><s:text
                                name="label.actions" /></th>
                    </tr>
                </s:if>
            </thead>
            <tbody>
                <s:iterator value="#filesAttributes" var="fileVar"
                            status="fileStatus">
                    <s:if test="%{null == getProtectedFolder()}">
                        <s:set var="isProtectedFileVar"
                               value="%{#fileVar.isProtectedFolder()}" />
                        <s:set var="filenameVar" value="''" />
                    </s:if>
                    <s:else>
                        <s:set var="isProtectedFileVar" value="%{getProtectedFolder()}" />
                        <s:set var="filenameVar" value="#fileVar.name" />
                    </s:else>
                    <tr>
                        <td class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></td>
                        <%-- name --%>
                        <td class="text-nowrap"><s:if test="#fileVar.directory">
                                <a class="display-block"
                                   href="<s:url namespace="/do/FileBrowser" action="list" >
                                       <s:param name="currentPath"><s:property escapeHtml="true" value="currentPath"/><s:property escapeHtml="true" value="#filenameVar"/></s:param>
                                       <s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
                                   </s:url>">
                                    <span class="icon fa fa-folder"></span> <span class="sr-only">Folder</span>
                                    <s:property value="#fileVar.name" />
                                </a>
                            </s:if> <s:else>
                                <span class="sr-only">File</span>
                                <s:if test="%{isTextFile(#fileVar.name)}">
                                    <a class="display-block"
                                       title="Edit: <s:property value="#fileVar.name"/>"
                                       href="<s:url namespace="/do/FileBrowser" action="edit" >
                                           <s:param name="currentPath"><s:property escapeHtml="true" value="%{#currentPath}"/></s:param>
                                           <s:param name="filename"> <s:property escapeHtml="false" value="#filenameVar"/></s:param>
                                           <s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
                                       </s:url>">
                                        <span class="icon fa fa-file-text"></span> <s:property
                                            value="#fileVar.name" />
                                    </a>
                                </s:if>
                                <s:else>
                                    <a class="display-block"
                                       title="Download: <s:property value="#fileVar.name"/>"
                                       href="<s:url namespace="/do/FileBrowser" action="download" >
                                           <s:param name="currentPath"><s:property escapeHtml="true" value="%{#currentPath}"/></s:param>
                                           <s:param name="filename"> <s:property escapeHtml="false" value="#filenameVar"/></s:param>
                                           <s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
                                       </s:url>">
                                        <span class="icon fa fa-file-archive"></span> <s:property
                                            value="#fileVar.name" />
                                    </a>
                                </s:else>
                            </s:else></td>
                            <%-- size --%>
                        <td class="text-nowrap text-center"
                            title="<s:if test="!#fileVar.directory"><s:property value="#fileVar.size" /> bytes</s:if>">
                            <s:if test="#fileVar.directory">
                                <span class="sr-only">N.A</span>
                            </s:if> <s:else>
                                <s:property
                                    value="(#fileVar.size / 1024) > 0 ? (#fileVar.size / 1024) + ' KB' : (#fileVar.size) + ' bytes' " />
                            </s:else>
                        </td>
                        <%-- last edit --%>
                        <td class="text-nowrap col-lg-1 text-center"
                            title="<s:date name="%{#fileVar.lastModifiedTime}" format="dd/MM/yyyy, HH:mm:ss" />">
                            <s:if test="#fileVar.directory">
                                <span class="sr-only">N.A</span>
                            </s:if> <s:else>
                                <time
                                    datetime="<s:date name="%{#fileVar.lastModifiedTime}"  format="yyyy-MM-dd HH:mm:ss" />">
                                    <code>
                                        <s:date name="%{#fileVar.lastModifiedTime}" nice="true"
                                                format="EEEE dd/MMM/yyyy, HH:mm:ss" />
                                    </code>
                                </time>
                            </s:else>
                        </td>
                        <td
                            class="table-view-pf-actions text-center col-xs-1 col-sm-1 col-md-1 col-lg-1">
                            <div class="dropdown dropdown-kebab-pf">
                                <s:if
                                    test="%{!#fileVar.directory || null != getProtectedFolder()}">
                                    <button class="btn btn-menu-right dropdown-toggle"
                                            type="button" data-toggle="dropdown" aria-haspopup="true"
                                            aria-expanded="false">
                                        <span class="fa fa-ellipsis-v"></span>
                                    </button>
                                </s:if>
                                <ul class="dropdown-menu dropdown-menu-right">
                                    <s:if test="!#fileVar.directory">
                                        <li><a title="<s:text name="label.download" />: <s:property value="#fileVar.name"/>"
                                                href="<s:url namespace="/do/FileBrowser" action="download" >
                                                    <s:param name="currentPath"><s:property escapeHtml="true" value="%{#currentPath}"/></s:param>
                                                    <s:param name="filename"><s:property escapeHtml="false" value="#filenameVar"/></s:param>
                                                    <s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
                                                </s:url>">
                                                <span><s:text name="label.download" /></span>
                                            </a></li>
                                        </s:if>
                                        <s:if test="%{null != getProtectedFolder()}">
                                        <li><a
                                                title="<s:text name="label.delete" />: <s:property value="#fileVar.name"/>"
                                                href="<s:url namespace="/do/FileBrowser" action="trash" >
                                                    <s:param name="currentPath"><s:property escapeHtml="true" value="%{#currentPath}"/></s:param>
                                                    <s:param name="filename"><s:property value="#fileVar.name"/></s:param>
                                                    <s:param name="deleteFile" value="%{!#fileVar.directory}" />
                                                    <s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
                                                </s:url>">
                                                <span><s:text name="label.delete" /></span>
                                            </a></li>
                                        </s:if>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>
    </s:if>
</div>
