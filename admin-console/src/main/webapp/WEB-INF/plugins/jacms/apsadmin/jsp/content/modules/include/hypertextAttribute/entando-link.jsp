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
	<title>Configure Link</title>

	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta charset="utf-8" />

	<link rel="stylesheet" href="<wp:resourceURL />administration/bootstrap/css/bootstrap.min.css" media="screen" />
	<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-theme-entando-ce/css/bootstrap.min.css" media="screen" />
	<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-override.css" media="screen" />
	<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-offcanvas.css" media="screen" />
	<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-addendum.css" media="screen" />

	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
		<script src="<wp:resourceURL />administration/js/html5shiv.js"></script>
		<script src="<wp:resourceURL />administration/js/respond.min.js"></script>
	<![endif]-->

	<script src="<wp:resourceURL />administration/js/jquery-1.9.1.min.js"></script>
	<script src="<wp:resourceURL />administration/bootstrap/js/bootstrap.js"></script>
	<script src="<wp:resourceURL />administration/js/bootstrap-offcanvas.js"></script>

	<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />

	<%-- entando-link stuff --%>
	<script type="text/javascript" src="<wp:resourceURL />administration/js/ckeditor/ckeditor.js"></script>
	<script>
		jQuery(function(){
			<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-pageTree.jsp" />
		})
	</script>

	<script>
		<s:set var="activeTab"><%= request.getParameter("activeTab")%></s:set>
		<s:if test="%{#activeTab == 'null' || #activeTab == ''}"><s:set var="activeTab">0</s:set></s:if>
		jQuery(function(){
			<%--
			when the dom is ready we try to read the hash
			and recognize what is the tab to open.
			--%>
			$('#tab-togglers li:eq(<s:property value="#activeTab" />) a').tab('show')

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
			//enable the clickable togglers (as required by bootstrap)
			$('.tab-togglers a').click(function (e) {
				e.preventDefault()
				$(this).tab('show')
			});
		})
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
					  '<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>'+
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
		});
	</script>
</head>
<body>
		<div class="container" id="container-main"><%-- container-main --%>
			<div class="row"><%-- row row-offcanvas --%>
				<div class="col-md-10 col-lg-8 col-md-offset-1 col-lg-offset-2" id="container-content"><%-- container-content --%>
					<%-- content here --%>
						<h1 class="panel panel-default title-page">
							<span class="panel-body display-block">
								<s:text name="jacms.menu.contentAdmin" />
								&#32;/&#32;
								<s:text name="label.edit" />
								&#32;/&#32;
								<s:text name="title.link" />
							</span>
						</h1>
						<ul class="nav nav-tabs tab-togglers" id="tab-togglers">
							<li><a data-toggle="tab" href="#url-link"><s:text name="note.URLLinkTo" /></a></li>
							<li><a data-toggle="tab" href="#page-link"><s:text name="note.pageLinkTo" /></a></li>
							<li><a data-toggle="tab" href="#content-link"><s:text name="note.contentLinkTo" /></a></li>
						</ul>
						<div class="panel panel-default" id="tab-container"><%-- panel --%>
							<div class="panel-body"><%-- panel body --%>
								<div class="tab-content"><%-- tabs container --%>
									<%-- link to url --%>
										<div id="url-link" class="tab-pane">
											<form id="form_externalUrl">
												<div class="col-xs-12">
													<div class="form-group">
														<label class="display-block" for="txtName">
															<s:text name="label.url" />
														</label>
														<input type="hidden" name="contentOnSessionMarker" value="<s:property value="contentOnSessionMarker" />" />
														<wpsf:textfield id="txtName" name="txtName" maxlength="255" cssClass="form-control" />
														<span class="help help-block">
															<s:text name="note.typeValidURL" />
														</span>
													</div>
												</div>
												<div class="form-group">
													<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
														<button type="submit" id="button_externalURL" name="button_externalURL" class="btn btn-primary btn-block">
															<span class="icon fa fa-floppy-o"></span>&#32;
															<s:text name="label.confirm" />
														</button>
													</div>
												</div>
											</form>
										</div>
									<%-- link to page --%>
										<s:action name="entandoPageLink" executeResult="true" />
									<%-- link to content --%>
										<s:if test="#request.activeTab == 2 && null != #request.internalActionName">
											<s:set var="introContentLinkActionName" value="#request.internalActionName" />
										</s:if>
										<s:else>
											<s:set var="introContentLinkActionName" value="%{'entandoIntroContentLink'}" />
										</s:else>
										<s:action name="%{#introContentLinkActionName}" executeResult="true" />
								</div><%-- panel body --%>
							</div><%-- tabs container --%>
						</div><%-- panel --%>
					<%-- content here --%>
				</div><%-- container-content --%>
				<div class="clearfix"></div>
				<div class="col-sm-12 margin-large-top"><%-- footer back --%>
					<ul class="sr-only">
						<li><a href="#fagiano_mainContent"><s:text name="note.backToMainContent" /></a></li>
						<li><a href="#fagiano_start"><s:text name="note.backToStart" /></a></li>
					</ul>
				</div><%-- footer back --%>
			</div><%-- row row-offcanvas --%>
		</div><%-- container-main --%>
	</body>
</html>