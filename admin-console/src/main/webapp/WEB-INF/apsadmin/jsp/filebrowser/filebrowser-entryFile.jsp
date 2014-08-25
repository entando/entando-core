<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />">File Browser</a>
		&#32;/&#32;
		File
	</span>
</h1>
<div id="main">

	<s:include value="/WEB-INF/apsadmin/jsp/filebrowser/include/breadcrumbs.jsp" />

	<s:form action="save" namespace="/do/FileBrowser" cssClass="margin-base-top form-horizontal" >
		
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon icon-remove"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
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
				<label>Filename</label>
				<p class="form-control-static">
					<s:property value="filename" />
					&#32;
					<a
						title="Download: <s:property value="#fileVar.name"/>"
						href="<s:url namespace="/do/FileBrowser" action="download" >
							<s:param name="currentPath"><s:property escape="true" value="%{currentPath}"/></s:param>
							<s:param name="filename"> <s:property escape="false" value="filename"/></s:param>
						</s:url>">
						<span class="icon fa fa-download"></span>
						<span class="sr-only">
							Download: <s:property value="#fileVar.name"/>
						</span>
					</a>
				</p>
				</s:if>
				
				<s:elseif test="strutsAction == 11">
				<%-- ** NEW text file ** --%>
				<label for="filename" class="display-block">Name</label>
				<div class="row">
					<div class="col-md-9">
						<wpsf:textfield
							name="filename"
							id="filename"
							cssClass="form-control col-md-9" />
					</div>
					<div class="col-md-3">
						<label for="file-extension" class="sr-only">Extension</label>
						<wpsf:select id="file-extension"
							list="textFileTypes"
							name="textFileExtension"
							cssClass="form-control col-md-3" />
					</div>
				</div>
				</s:elseif>
				
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<label for="file-content">Content</label>
				<wpsf:textarea 
					cssClass="form-control"
					id="file-content"
					placeholder="file content here..."
					name="fileText"
					rows="20"
					cols="50"
					/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-save"></span>&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit
					action="list"
					value="%{getText('label.cancel')}"
					cssClass="btn btn-link" />
			</div>
		</div>
	</s:form>
</div>
