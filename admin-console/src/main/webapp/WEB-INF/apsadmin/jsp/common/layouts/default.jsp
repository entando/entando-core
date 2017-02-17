<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<!DOCTYPE html>
<!--[if IE 9]><html lang="en-us" class="ie9 layout-pf layout-pf-fixed"><![endif]-->
<!--[if gt IE 9]><!-->
<html lang="en-us" class="layout-pf layout-pf-fixed">
	<!--<![endif]-->

	<head>
		<title>Entando - <s:set var="documentTitle"><tiles:getAsString name="title"/></s:set><s:property value="%{getText(#documentTitle)}" escape="false" /></title>
			<meta charset="UTF-8">
			<meta http-equiv="X-UA-Compatible" content="IE=Edge">
			<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<link rel="shortcut icon" href="<wp:resourceURL />administration/patternfly/img/favicon.ico">

			<link rel="stylesheet" href="<wp:resourceURL />administration/patternfly/css/patternfly.min.css">
		<link rel="stylesheet" href="<wp:resourceURL />administration/patternfly/css/patternfly-additions.min.css">
		<link rel="stylesheet" href="<wp:resourceURL />administration/css/entando-admin-console-default-theme.css">
		<link rel="stylesheet" href="<wp:resourceURL />administration/css/entando-admin-console-blue-theme.css">

		<script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
		crossorigin="anonymous"></script>
		<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery.matchHeight/0.7.0/jquery.matchHeight-min.js"></script>
		<script src="<wp:resourceURL />administration/patternfly/js/patternfly.js"></script>

		<script>
			$(document).ready(function () {
				// matchHeight the contents of each .card-pf and then the .card-pf itself
				$(".row-cards-pf > [class*='col'] > .card-pf .card-pf-title").matchHeight();
				$(".row-cards-pf > [class*='col'] > .card-pf > .card-pf-body").matchHeight();
				$(".row-cards-pf > [class*='col'] > .card-pf > .card-pf-footer").matchHeight();
				$(".row-cards-pf > [class*='col'] > .card-pf").matchHeight();

				// Initialize the vertical navigation
				$().setupVerticalNavigation(true);
			});
		</script>
	</head>

	<body>
		
		<!--barra di navigazione-->
		<nav class="navbar navbar-pf-vertical">
			<tiles:insertAttribute name="header"/>
		</nav>
		<!--/.navbar-->
		
		<!--barra di menu laterale-->
		<div class="nav-pf-vertical nav-pf-vertical-with-sub-menus nav-pf-vertical-collapsible-menus ">
			<tiles:insertAttribute name="menu"/>
		</div>
		
		<div class="container-fluid container-cards-pf container-pf-nav-pf-vertical">
			<tiles:insertAttribute name="body"/>
		</div>
	</body>
</html>