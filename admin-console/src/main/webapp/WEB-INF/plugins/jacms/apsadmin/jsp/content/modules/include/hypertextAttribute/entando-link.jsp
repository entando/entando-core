<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<!DOCTYPE html>
<html lang="<s:property value="currentLang.code" />">
	<head>
		<title><s:text name="title.configureLink"/></title>
	
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta charset="utf-8" />
	
		<link rel="stylesheet" href="<wp:resourceURL />administration/css/entando-admin-console-default-theme.css" media="screen" />
		<link rel="stylesheet" href="<wp:resourceURL />administration/bootstrap/css/bootstrap.min.css" media="screen">
		<link rel="stylesheet" href="<wp:resourceURL />administration/patternfly/css/patternfly.min.css">
		<link rel="stylesheet" href="<wp:resourceURL />administration/patternfly/css/patternfly-additions.min.css">
		<link rel="stylesheet" href="<wp:resourceURL />administration/css/entando-admin-console-default-theme.css">
		<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/pages/settingsPage.css">
	
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="<wp:resourceURL />administration/js/html5shiv.js"></script>
			<script src="<wp:resourceURL />administration/js/respond.min.js"></script>
		<![endif]-->
	
		<!--JS inclusions-->
		<script src="<wp:resourceURL />administration/js/jquery-2.2.4.min.js"></script>
		<script src="<wp:resourceURL />administration/patternfly/js/patternfly.js"></script>
		<script src="<wp:resourceURL />administration/js/pages/settingsPage.js"></script>
		<script src="<wp:resourceURL />administration/bootstrap/js/bootstrap.min.js"></script>
		<script src="<wp:resourceURL />administration/js/bootstrap-switch.min.js"></script>
		<script src="<wp:resourceURL />administration/js/bootstrap-offcanvas.js"></script>
		
		<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
	
		<%-- entando-link stuff --%>
		<script type="text/javascript" src="<wp:resourceURL />administration/js/ckeditor/ckeditor.js"></script>
		<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-pageTree.jsp" />
        <%-- form extras --%>
		<script type="text/javascript">
			jQuery(function(){
				<%--
				when the dom is ready we try to read the hash
				and recognize what is the tab to open.
				--%>
	
				if (document.location.hash.length > 0) {
					var tabContainer = $('#tab-container'); //tab conainer
					var tabTogglersContainer = $('#tab-togglers'); //toggler container
					//find the parent with class .tab-pane inside the tab container (and get its id)
					var tabId = $($(document.location.hash).parent('.tab-pane'), tabContainer).first().attr('id');
					if (tabId == undefined) {
						var tabId = $(document.location.hash+'.tab-pane', tabContainer).first().attr('id');
					}
					if (tabId!==undefined) {
						//find the element with [href][data-toggle] matching our hash
						var toggler = $('[href="#'+ tabId +'"][data-toggle="tab"]', tabTogglersContainer).first();
						if (toggler.length==1) {
							$(toggler).tab('show');
						}
					}
				}
			});
			jQuery(function(){
				var entandoApplyLinkToEditor = function (href) {
					// var editor = window.opener.entandoCKEditor[window.name];
					// var selection = editor.getSelection();
					// var range = selection.getRanges( 1 )[ 0 ];
					// var style = new CKEDITOR.style({"element": "a", "attributes": { "data-cke-saved-href": href, "href": href } });
					// style.type = CKEDITOR.STYLE_INLINE; // need to override... dunno why.
					// style.applyToRange( range );
					// range.select();
					var editor = window.opener.entandoCKEditor[window.name];
					var selection = editor.getSelection();
					var ranges = selection.getRanges(true);
					if (ranges.length == 1 && ranges[0].collapsed) {
						var text = new CKEDITOR.dom.text('creato', editor.document);
						ranges[0].insertNode(text);
						ranges[0].selectNodeContents(text);
						selection.selectRanges(ranges);
					}
					var style = new CKEDITOR.style({"element": "a", "attributes": { "data-cke-saved-href": href, "href": href } });
					style.type = CKEDITOR.STYLE_INLINE;	// need to override... dunno why.
					style.apply(editor.document);
				};
	
				var insertAlert = function(text, target) {
					$('<div class="alert alert-danger alert-dismissable fade in">'+
						  '<button class="close" data-dismiss="alert" aria-hidden="true"><span class="pficon pficon-close"></span></button>'+
						  '<span class="pficon pficon-error-circle-o"></span>'+
							'<strong><s:property value="%{getText('message.title.FieldErrors')}" escapeJavaScript="true" /></strong>. '+
							text+
						'</div>').prependTo(target);
				};
				//link to url
				$('#form_externalUrl').on('submit', function(ev){
					ev.preventDefault();
					if ( $('#txtName').val().length == 0 ) {
						insertAlert('<s:property value="%{getText('note.typeValidURL')}" escapeJavaScript="true" />', $('#form_externalUrl')) ;
					}
					else {
						var hrefValue = "#!U;" + $('#txtName').val() + "!#";
						entandoApplyLinkToEditor(hrefValue);
						window.close();
					}
				});
				//link to page
				$('#form_pageLink').on('submit', function(ev){
					ev.preventDefault();
					var checkedPage = $('[name="selectedNode"]:checked', '#pageTree').first();
					if (checkedPage.length==0) {
							insertAlert("<s:property value="%{getText('note.choosePageToLink')}" escapeJavaScript="true" />", $('#form_pageLink'));
					}
					else {
						var hrefValue = "#!P;" + checkedPage.val() + "!#";
						entandoApplyLinkToEditor(hrefValue);
						window.close();
					}
				});
				//link to content
				$('#button_contentLink').on('click touchstart keypress', function(ev){
					ev.preventDefault();
					var checkedContent = $('[name="contentId"]:checked', '#contentListTable').first();
					if (checkedContent.length==0) {
						insertAlert("<s:property value="%{getText('note.chooseContentToLink')}" escapeJavaScript="true" />", $('#content-link'));
					}
					else {
						var hrefValue = "#!C;" + checkedContent.val() + "!#";
						entandoApplyLinkToEditor(hrefValue);
						window.close();
					}
				});
				//link to resource
				$('#button_resourceLink').on('click touchstart keypress', function(ev){
					ev.preventDefault();
					var checkedResource = $('[name="resourceId"]:checked', '#resourceListTable').first();
					if (checkedResource.length==0) {
						insertAlert("<s:property value="%{getText('note.chooseResourceToLink')}" escapeJavaScript="true" />", $('#resource-link'));
					}
					else {
						var hrefValue = "#!R;" + checkedResource.val() + "!#";
						entandoApplyLinkToEditor(hrefValue);
						window.close();
					}
				});
			});
		</script>
	</head>
    <body>
		<div class="container" id="container-main">
			<div class="row">
				<div class="col-md-10 col-lg-8 col-md-offset-1 col-lg-offset-2" id="container-content">
					<!-- Page Title -->
					<s:set var="dataContent" value="%{'help block'}" />
					<s:set var="dataOriginalTitle" value="%{'Section Help'}" />
					<h1 class="page-title-container">
					    <s:text name="title.link" />
					    <span class="pull-right"> <a tabindex="0" role="button"
					        data-toggle="popover" data-trigger="focus" data-html="true" title=""
					        data-content="${dataContent}" data-placement="left"
					        data-original-title="${dataOriginalTitle}"> <span
					            class="fa fa-question-circle-o" aria-hidden="true"></span>
					    </a>
					    </span>
					</h1>
					<!-- Default separator -->
					<div class="form-group-separator"></div>
						<c:set var="linkTypeVar" value="${param.linkTypeVar}" scope="page"/>
						<c:set var="contentOnSessionMarker" value="${param.contentOnSessionMarker}" scope="page"/>
						<!-- Tab Menu -->
                        <ul class="nav nav-tabs tab-togglers mt-20" id="tab-togglers">
                            <s:url action="entandoInternalUrlLink" anchor="url-link" var="externalLinkURL">
                                <s:param name="linkTypeVar" value="1" />
                                <s:param name="contentOnSessionMarker" value="%{#attr.contentOnSessionMarker}" />
                            </s:url>
                            <s:url action="entandoInternalPageLink" anchor="page-link" var="pageLinkURL">
                                <s:param name="linkTypeVar" value="2" />
                                <s:param name="contentOnSessionMarker" value="%{#attr.contentOnSessionMarker}" />
                            </s:url>
                            <s:url action="entandoInternalContentLink" anchor="content-link" var="contentLinkURL">
                                <s:param name="linkTypeVar" value="3" />
                                <s:param name="contentOnSessionMarker" value="%{#attr.contentOnSessionMarker}" />
                            </s:url>
                            <s:url action="entandoInternalResourceLink" anchor="resource-link" var="resourceLinkURL">
                                <s:param name="linkTypeVar" value="4" />
                                <s:param name="contentOnSessionMarker" value="%{#attr.contentOnSessionMarker}" />
                            </s:url>
							<li ${((param.linkTypeVar eq 1) || (empty param.linkTypeVar))?'class="active"':''}><a href="${externalLinkURL}"><s:text name="note.URLLinkTo" /></a></li>
							<li ${(param.linkTypeVar eq 2)?'class="active"':''}><a href="${pageLinkURL}"><s:text name="note.pageLinkTo" /></a></li>
							<li ${(param.linkTypeVar eq 3)?'class="active"':''}><a href="${contentLinkURL}"><s:text name="note.contentLinkTo" /></a></li>
							<li ${(param.linkTypeVar eq 5)?'class="active"':''}><a href="${resourceLinkURL}"><s:text name="note.resourceLinkTo" /></a></li>
						</ul>
						<!-- Link types -->
						<div class="panel panel-default no-top-border" id="tab-container">
							<div class="panel-body">
								<div class="tab-content">
									<div id="url-link">
										<s:if test="%{#attr.linkTypeVar == 1}">
										<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/hypertextAttribute/type-1.jsp" />
										</s:if>
									</div>
									<div id="page-link" >
										<s:if test="%{#attr.linkTypeVar == 2}">
										<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/hypertextAttribute/type-2.jsp" />
										</s:if>
									</div>
									<div id="content-link" >
										<s:if test="%{#attr.linkTypeVar == 3}">
										<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/hypertextAttribute/type-3.jsp" />
										</s:if>
									</div>
									<div id="resource-link">
										<s:if test="%{#attr.linkTypeVar == 5}">
										<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/hypertextAttribute/type-5.jsp" />
										</s:if>
									</div>
								</div>
							</div>
						</div>
				</div>
				<div class="clearfix"></div>
				<div class="col-sm-12 margin-large-top">
					<ul class="sr-only">
						<li><a href="#fagiano_mainContent"><s:text name="note.backToMainContent" /></a></li>
						<li><a href="#fagiano_start"><s:text name="note.backToStart" /></a></li>
					</ul>
				</div>
			</div>
		</div>
	</body>
</html>