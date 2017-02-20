<%@ taglib prefix="wp" uri="/aps-core" %>
<div class="navbar-header">
	<button type="button" class="navbar-toggle">
		<span class="sr-only">Toggle navigation</span>
		<span class="icon-bar"></span>
		<span class="icon-bar"></span>
		<span class="icon-bar"></span>
	</button>
	<a href="/" class="navbar-brand">
		<img class="navbar-brand-icon logo-entando" src="<wp:resourceURL />administration/img/entando-logo.svg" alt="Entando 4.3" />
	</a>
</div>
<nav class="collapse navbar-collapse">

	<ul class="nav navbar-nav navbar-right navbar-iconic">
		</li>
		<li class="dropdown">
			<a class="dropdown-toggle nav-item-iconic" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				<span title="Help" class="fa pficon-help"></span>
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
				<li><a href="#">Help</a></li>
				<!--<li><a href="#">About</a></li>-->
			</ul>
		</li>
		<li class="dropdown">
			<a class="dropdown-toggle nav-item-iconic" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				<span title="Username" class="fa pficon-user"></span>
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu" aria-labelledby="dropdownMenu2">
				<li><a href="#">My profile</a></li>
				<li><a href="#">Logout</a></li>
			</ul>
		</li>
	</ul>
</nav>