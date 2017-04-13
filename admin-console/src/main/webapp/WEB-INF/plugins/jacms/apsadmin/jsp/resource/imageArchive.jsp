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
		<h1 class="page-title-container">
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

	<div class="row">
		<s:set var="imageDimensionsVar" value="imageDimensions" />
		<s:iterator var="resourceid" status="status">
			<s:set var="resource" value="%{loadResource(#resourceid)}" />
			<s:set var="resourceInstance" value='%{#resource.getInstance(0,null)}' />
			<s:set var="URLoriginal" value="%{#resource.getImagePath(0)}" />
			<s:url var="URLedit" action="edit" namespace="/do/jacms/Resource">
				<s:param name="resourceId" value="%{#resourceid}" />
			</s:url>
			<s:url var="URLuse" action="joinResource" namespace="/do/jacms/Content/Resource">
				<s:param name="resourceId" value="%{#resourceid}" />
				<s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
			</s:url>
			<s:url var="URLtrash" action="trash" namespace="/do/jacms/Resource">
				<s:param name="resourceId" value="%{#resourceid}" />
				<s:param name="resourceTypeCode" value="%{#resource.type}" />
				<s:param name="text" value="%{text}" />
				<s:param name="categoryCode" value="%{categoryCode}" />
				<s:param name="fileName" value="%{fileName}" />
				<s:param name="ownerGroupName" value="%{ownerGroupName}" />
				<s:param name="treeNodesToOpen" value="%{treeNodesToOpen}" />
			</s:url>
			<div class="col-sm-4 col-md-3">
	                    <div class="panel panel-default text-center">
				<s:if test="!onEditContent">
					<div class="panel-heading text-right padding-small-vertical padding-small-right">
						<a href="<s:property value="URLtrash" escapeHtml="false" />" class="icon fa fa-times-circle text-warning">
							<span class="sr-only">Delete</span>
						</a>
					</div>
				</s:if>
	                        <div>
	                                <%-- Dimension forced for img thumbnail --%>
	                                <img src="<s:property value="%{#resource.getImagePath(1)}"/>" alt=" " style="height:90px;max-width:130px" class="margin-small-top" />
	                        </div>
	                        <div class="btn-group margin-small-vertical">
	                        <s:if test="!onEditContent">
	                                <a href="<s:property value="URLedit" escapeHtml="false" />"
	                                         class="btn btn-default"
	                                         title="<s:text name="label.edit" />: <s:property value="#resource.descr" />">
	                                        <span class="icon fa fa-pencil-square-o"></span>&#32;
	                                        <s:text name="label.edit" />
	                                </a>
	                        </s:if>
	                        <s:else>
	                                <a href="<s:property value="URLuse" escapeHtml="false" />"
	                                         class="btn btn-default"
	                                         title="<s:text name="note.joinThisToThat" />:	<s:property value="content.descr" />" >
	                                        <span class="icon fa fa-picture-o"></span>&#32;
	                                        <s:text name="label.use"/>
	                                </a>
	                        </s:else>
	                                <button type="button" class="btn btn-info" data-toggle="popover" data-title="<s:property value="#resource.descr" />">
	                                        <span class="icon fa fa-info"></span>
	                                        <span class="sr-only"><s:text name="label.info" /></span>
	                                </button>
	                        </div>
	                        
	                        <s:set var="fileInfo">
	                                <s:include value="imageArchive-file-info.jsp" />
	                        </s:set>
	
	                        <script>
	                                $("[data-toggle=popover]").popover({
	                                        html: true,
	                                        placement: "top",
	                                        content: '<s:property value="fileInfo" escapeHtml="false" />'
	                                });
	                        </script>
	
	                    </div>
			</div>
	
		</s:iterator>
	
	</div>
	
	
	<div class="pager clear margin-more-top">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	
	</wpsa:subset>
	
	</s:form>
	
	<wp:ifauthorized permission="superuser" >
	<s:if test="!onEditContent">
	<s:action name="openAdminProspect" namespace="/do/jacms/Resource/Admin" ignoreContextParams="true" executeResult="true">
		<s:param name="resourceTypeCode" value="resourceTypeCode" ></s:param>
	</s:action>
	</s:if>
	</wp:ifauthorized>
	
	</div>
</div>