<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="breadcrumb.app" /></li>
	<li><s:text name="breadcrumb.jacms" /></li>
	<s:if test="onEditContent">
		<li><a href="<s:url action="list" namespace="/do/jacms/Content"/>"><s:text name="breadcrumb.jacms.content.list" /></a></li>
		<li><a href="<s:url action="backToEntryContent" ><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
			<s:if test="getStrutsAction() == 1"><s:text name="breadcrumb.jacms.content.new" /></s:if><s:else><s:text name="breadcrumb.jacms.content.edit" /></s:else>
		</a></li>
	</s:if>
	<s:else>
		<li><s:text name="breadcrumb.dataAsset" /></li>
	</s:else>
	<li class="page-title-container"><s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}" /></li>
</ol>

<div class="row">
	<div class="col-sm-12 col-md-6 cols-nopad-right">
		<h1 class="page-title-container page-title-caret">
			<s:property value="%{getText('title.' + resourceTypeCode + '.management')}" />
			<span class="pull-right">
				<a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" 
					data-original-title="<s:property value="%{getText('help.' + resourceTypeCode + '.list.title')}" escapeXml="true" />" 
					data-content="<s:property value="%{getText('help.' + resourceTypeCode + '.list.info')}" escapeXml="true" />" 
					data-placement="left" ><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
			</span>
		</h1>
	</div>
	<div class="col-sm-12 col-md-6 cols-nopad-left cols-nopad-right">
		<ul class="nav nav-tabs nav-justified nav-tabs-pattern" id="tabs-pattern" role="tablist">
			<li role="presentation" <s:if test="%{resourceTypeCode == 'Image'}" >class="active" </s:if>>
				<s:if test="onEditContent">
					<s:property value="%{getText('title.Image.tab')}" />
				</s:if>
				<s:else>
					<a href="<s:url action="list" ><s:param name="resourceTypeCode" >Image</s:param></s:url>" role="tab" ><s:property value="%{getText('title.Image.tab')}" /></a>
				</s:else>
			</li>
			<li role="presentation" <s:if test="%{resourceTypeCode == 'Attach'}" >class="active" </s:if>>
				<s:if test="onEditContent">
					<s:property value="%{getText('title.Attach.tab')}" />
				</s:if>
				<s:else>
					<a href="<s:url action="list" ><s:param name="resourceTypeCode" >Attach</s:param></s:url>" role="tab" ><s:property value="%{getText('title.Attach.tab')}" /></a>
				</s:else>
			</li>
		</ul>
	</div>
</div>

<div class="tab-content" class="tab-pane active" >

	<s:include value="inc/resource_searchForm.jsp" />
	
	<wp:ifauthorized permission="manageResources">
		<p><a href="<s:url action="new" >
				<s:param name="resourceTypeCode" value="resourceTypeCode" /><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
			</s:url>"
			class="btn btn-primary pull-right" title="<s:property value="%{getText('label.' + resourceTypeCode + '.new')}" escapeXml="true" />">
			<s:property value="%{getText('label.' + resourceTypeCode + '.new')}" />
		</a></p>
	</wp:ifauthorized>
	
	<s:form action="search">
	<p class="sr-only">
		<wpsf:hidden name="text" />
		<wpsf:hidden name="categoryCode" />
		<wpsf:hidden name="resourceTypeCode" />
		<wpsf:hidden name="fileName" />
		<wpsf:hidden name="ownerGroupName" />
	<s:if test="#categoryTreeStyleVar == 'request'">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
			<wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/>
		</s:iterator>
	</s:if>
		<wpsf:hidden name="contentOnSessionMarker" />
	</p>
	
	<wpsa:subset source="resources" count="10" objectName="groupResource" advanced="true" offset="5" >
	<s:set var="group" value="#groupResource" />

	<s:if test="onEditContent"><h3><s:text name="title.chooseAttach" /></h3><div class="list-group"></s:if>
	<s:if test="!onEditContent"><ul></s:if>
	<s:iterator var="resourceid" status="status">
	<s:set var="resource" value="%{loadResource(#resourceid)}"></s:set>
	<s:set var="resourceInstance" value="%{#resource.getInstance()}"></s:set>
		<s:if test="onEditContent">
		<a href="<s:url action="joinResource" namespace="/do/jacms/Content/Resource"><s:param name="resourceId" value="%{#resourceid}" /><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>"
		title="<s:text name="label.use"/>" class="list-group-item" >
			<s:if test="!#resource.categories.empty">
			<div class="row"><div class="col-lg-12">
				<s:iterator var="category_resource" value="#resource.categories">
						<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
							<span class="icon fa fa-tag"></span>&#32;
							<s:property value="%{#category_resource.getTitle(currentLang.code)}"/></span>
				</s:iterator>
			</div></div>
			</s:if>
			<div class="row">
			<div class="col-lg-12">
					<s:set var="fileDescr" value="#resource.descr" />
					<s:if test='%{#fileDescr.length()>90}'>
						<s:set var="fileDescr" value='%{#fileDescr.substring(0,30)+"..."+#fileDescr.substring(#fileDescr.length()-30)}' />
						<abbr title="<s:property value="#resource.descr" />">
						<s:property value="#fileDescr" /></abbr>
					</s:if>
					<s:else>
						<s:property value="#resource.descr" />
					</s:else>
	                                <s:if test="%{#resource.mainGroup != null && !#resource.mainGroup.equals('free')}"><span class="text-muted icon fa fa-lock"></span></s:if>
					<s:set var="fileNameVar" value="#resource.masterFileName" />
					<s:if test='%{#fileNameVar.length()>25}'>
						<s:set var="fileNameVar" value='%{#fileNameVar.substring(0,10)+"..."+#fileNameVar.substring(#fileNameVar.length()-10)}' />
						<code class="margin-small-bottom"><abbr title="<s:property value="#fileNameVar" />"><s:property value="#fileNameVar" /></abbr></code>
					</s:if>
					<s:else>
						<code><s:property value="#fileNameVar" /></code>
					</s:else>
					<span class="badge">
					<s:property value="%{#fileNameVar.replaceAll(' ', '&nbsp;')}"  escapeXml="false" escapeHtml="false" escapeJavaScript="false" />
					</span>
				</div>
			</div>
		</a>
		</s:if>
		<s:if test="!onEditContent">
			<li class="list-group-item">
			<div class="row">
				<div class="col-xs-10 col-lg-8">
				<s:if test="!#resource.categories.empty">
						<s:iterator var="category_resource" value="#resource.categories">
							<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
								<span class="icon fa fa-tag"></span>&#32;
							<s:property value="%{#category_resource.getTitle(currentLang.code)}"/></span>
						</s:iterator>
				</s:if>
				</div>
				<div class="col-xs-2 col-lg-4 text-right no-margin">
					<a href="<s:url action="trash" namespace="/do/jacms/Resource" >
							<s:param name="resourceId" value="%{#resourceid}" />
							<s:param name="resourceTypeCode" value="%{#resource.type}" />
							<s:param name="text" value="%{text}" />
							<s:param name="categoryCode" value="%{categoryCode}" />
							<s:param name="fileName" value="%{fileName}" />
							<s:param name="ownerGroupName" value="%{ownerGroupName}" />
							<s:param name="treeNodesToOpen" value="%{treeNodesToOpen}" />
						</s:url>" title="<s:text name="label.remove" />: <s:property value="#resource.descr" /> "><span class="icon fa fa-times-circle text-warning"></span></a>
				</div>
			</div>
			<div class="row">
			<div class="col-lg-12">
				<a href="<s:url action="edit" namespace="/do/jacms/Resource"><s:param name="resourceId" value="%{#resourceid}" /></s:url>" title="<s:text name="label.edit" />: <s:property value="#resource.descr" /> ">
				<span class="icon fa fa-pencil-square-o"></span>&#32;<s:property value="#resource.descr" /></a>
	                        <s:if test="%{#resource.mainGroup != null && !#resource.mainGroup.equals('free')}"><span class="text-muted icon fa fa-lock"></span></s:if>
				<p class="margin-none">
				<a href="<s:property value="%{#resource.documentPath}" />" title="<s:text name="label.download" />: <s:property value="#resource.masterFileName" />" class="pull-left margin-small-top">
				<span class="icon fa fa-download"></span>&#32;
	                                <s:set var="fileNameVar" value="#resource.masterFileName" />
					<s:if test='%{#fileNameVar.length()>25}'>
						<s:set var="fileNameVar" value='%{#fileNameVar.substring(0,10)+"..."+#fileNameVar.substring(#fileNameVar.length()-10)}' />
						<code><abbr title="<s:property value="#resource.masterFileName" />"><s:property value="#fileNameVar" /></abbr></code>
					</s:if>
					<s:else>
					<code><s:property value="#fileNameVar" /></code>
					</s:else>
				</a>
				<span class="badge pull-right margin-small-top">
					<s:property value="%{#resourceInstance.fileLength.replaceAll(' ', '&nbsp;')}" escapeXml="false" escapeHtml="false" escapeJavaScript="false" />
				</span>
				</p>
			</div>
			</div>
			</li>
		</s:if>
	</s:iterator>
	<s:if test="onEditContent"></div></s:if>
	<s:if test="!onEditContent"></ul></s:if>
	<div class="pager clear margin-more-top">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	
	</wpsa:subset>
	
	</s:form>
	
	</div>
</div>