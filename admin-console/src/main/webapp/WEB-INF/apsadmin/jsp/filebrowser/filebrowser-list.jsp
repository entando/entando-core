<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		File Browser
	</span>
</h1>
<div id="main">
	<s:include value="/WEB-INF/apsadmin/jsp/filebrowser/include/breadcrumbs.jsp" />
	<s:set var="currentPath" value="%{currentPath}" />
	<s:set var="filesAttributes" value="filesAttributes" />
	<s:if test="#filesAttributes.length>0">
		<div class="table-responsive">
			<table class="table table-hover">
				<thead>
					<tr class="sr-only">
						<th>Actions</th>
						<th class="text-nowrap">Name</th>
						<th class="col-lg-1">Size</th>
						<th class="col-lg-1">Last edit</th>
					</tr>
					<s:if test="%{null != getProtectedFolder()}">
					<tr>
						<th></th>
						<th colspan="3">
							<s:if test="currentPath!=''"><s:set var="isUpProtectedFileVar" value="protectedFolder" /></s:if>
							<a href="<s:url namespace="/do/FileBrowser" action="list" >
								   <s:param name="protectedFolder" value="#isUpProtectedFileVar" />
								   <s:param name="currentPath"><s:property escape="true" value="breadCrumbsTargets.get(breadCrumbsTargets.size()-2).key"/></s:param>
							   </s:url>">
								<span class="icon fa fa-share fa-rotate-270"></span>
								&#32;
								up ..
							</a>
						</th>
					</tr>
					</s:if>
				</thead>
			</s:if>
			<s:iterator value="#filesAttributes" var="fileVar" status="fileStatus">
				<s:if test="%{null == getProtectedFolder()}">
					<s:set var="isProtectedFileVar" value="%{#fileVar.isProtectedFolder()}" />
					<s:set var="filenameVar" value="''" />
				</s:if>
				<s:else>
					<s:set var="isProtectedFileVar" value="protectedFolder" />
					<s:set var="filenameVar" value="#fileVar.name" />
				</s:else>
				<tr>
					<td class="text-right text-nowrap col-xs-1 col-sm-1 col-md-1 col-lg-1 ">
						<s:if test="!#fileVar.directory">
							<div class="btn-group btn-group-xs">
								<%-- edit
								<s:if test="%{isTextFile(#fileVar.name)}" >
									<a
										class="btn btn-default"
										title="Edit: <s:property value="#fileVar.name"/>"
										href="<s:url namespace="/do/FileBrowser" action="edit" >
											<s:param name="currentPath"><s:property escape="true" value="%{#currentPath}"/></s:param>
											<s:param name="filename"> <s:property escape="false" value="#fileVar.name"/></s:param>
										</s:url>">
											<span class="icon fa fa-pencil-square-o"></span>
											<span class="sr-only">
												Edit: <s:property value="#fileVar.name"/>
											</span>
									</a>
								</s:if>
								--%>
								<%--download:--%>
								<a
									class="btn btn-default"
									title="Download: <s:property value="#fileVar.name"/>"
									href="<s:url namespace="/do/FileBrowser" action="download" >
										<s:param name="currentPath"><s:property escape="true" value="%{#currentPath}"/></s:param>
										<s:param name="filename"><s:property escape="false" value="#filenameVar"/></s:param>
										<s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
									</s:url>">
									<span class="icon fa fa-download"></span>
									<span class="sr-only">
										Download: <s:property value="#fileVar.name"/>
									</span>
								</a>
							</div>
						</s:if>
						<div class="btn-group btn-group-xs">
							<s:if test="%{null != getProtectedFolder()}">
							<a
								class="btn btn-warning"
								title="Delete: <s:property value="#fileVar.name"/>"
								href="<s:url namespace="/do/FileBrowser" action="trash" >
									<s:param name="currentPath"><s:property escape="true" value="%{#currentPath}"/></s:param>
									<s:param name="filename"><s:property value="#fileVar.name"/></s:param>
									<s:param name="deleteFile" value="%{!#fileVar.directory}" />
									<s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
								</s:url>">
								<span class="icon fa fa-times-circle-o"></span>
								<span class="sr-only">
									Delete: <s:property value="#fileVar.name"/>
								</span>
							</a>
							</s:if>
						</div>
					</td>

					<%-- name --%>
					<td class="text-nowrap">
						<s:if test="#fileVar.directory">
							<a class="display-block" href="<s:url namespace="/do/FileBrowser" action="list" >
								   <s:param name="currentPath"><s:property escape="true" value="currentPath"/><s:property escape="true" value="#filenameVar"/></s:param>
								   <s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
							   </s:url>">
								<span class="icon fa fa-folder"></span>
								<span class="sr-only">Folder</span>
								<s:property value="#fileVar.name"/>
							</a>
						</s:if>
						<s:else>
							<span class="sr-only">File</span>
							<s:if test="%{isTextFile(#fileVar.name)}" >
								<a
									class="display-block"
									title="Edit: <s:property value="#fileVar.name"/>"
									href="<s:url namespace="/do/FileBrowser" action="edit" >
										<s:param name="currentPath"><s:property escape="true" value="%{#currentPath}"/></s:param>
										<s:param name="filename"> <s:property escape="false" value="#filenameVar"/></s:param>
										<s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
									</s:url>">
									<span class="icon fa fa-file-text"></span>
									<s:property value="#fileVar.name"/>
								</a>
							</s:if>
							<s:else>
								<a
									class="display-block"
									title="Download: <s:property value="#fileVar.name"/>"
									href="<s:url namespace="/do/FileBrowser" action="download" >
										<s:param name="currentPath"><s:property escape="true" value="%{#currentPath}"/></s:param>
										<s:param name="filename"> <s:property escape="false" value="#filenameVar"/></s:param>
										<s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
									</s:url>">
									<span class="icon fa fa-file-archive"></span>
									<s:property value="#fileVar.name"/>
								</a>
							</s:else>
						</s:else>
					</td>
					<%-- size --%>
					<td class="text-nowrap" title="<s:if test="!#fileVar.directory"><s:property value="#fileVar.size" /> bytes</s:if>">
						<s:if test="#fileVar.directory">
							<span class="sr-only">N.A</span>
						</s:if>
						<s:else>
							<s:property value="(#fileVar.size / 1024) > 0 ? (#fileVar.size / 1024) + ' KB' : (#fileVar.size) + ' bytes' " />
						</s:else>
					</td>
					<%-- last edit --%>
					<td class="text-center text-nowrap col-lg-1" title="<s:date name="%{#fileVar.lastModifiedTime}" format="dd/MM/yyyy, HH:mm:ss" />">
						<s:if test="#fileVar.directory">
							<span class="sr-only">N.A</span>
						</s:if>
						<s:else>
							<time datetime="<s:date name="%{#fileVar.lastModifiedTime}"  format="yyyy-MM-dd HH:mm:ss" />">
								<code><s:date name="%{#fileVar.lastModifiedTime}" nice="true" format="EEEE dd/MMM/yyyy, HH:mm:ss" />
								</code>
							</time>
						</s:else>
					</td>
				</s:iterator>
				<s:if test="#filesAttributes.length>0">
			</table>
		</div>
	</s:if>
	
	<s:if test="%{null != getProtectedFolder()}">
	<div class="">
		<p class="margin-large-top btn-group btn-group-sm">
			<a
				class="btn btn-default"
				href="<s:url namespace="/do/FileBrowser" action="uploadNewFileEntry" >
					<s:param name="currentPath"><s:property escape="true" value="%{#currentPath}"/></s:param>
					<s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
				</s:url>">
				<span class="icon fa fa-upload"></span>&#32;
				Upload a file
			</a>
			<a
				class="btn btn-default"
				href="<s:url namespace="/do/FileBrowser" action="newFileEntry" >
					<s:param name="currentPath"><s:property escape="true" value="%{#currentPath}"/></s:param>
					<s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
				</s:url>">
				<span class="icon fa fa-file-text"></span>&#32;
				New Text File
			</a>
			<a
				class="btn btn-default"
				href="<s:url namespace="/do/FileBrowser" action="newDirEntry" >
					<s:param name="currentPath"><s:property escape="true" value="%{#currentPath}"/></s:param>
					<s:param name="protectedFolder"><s:property value="#isProtectedFileVar"/></s:param>
				</s:url>">
				<span class="icon fa fa-folder"></span>&#32;
				New Dir
			</a>
		</p>
	</div>
	</s:if>
</div>