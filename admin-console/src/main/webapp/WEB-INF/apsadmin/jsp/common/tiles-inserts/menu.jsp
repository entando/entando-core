<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<p id="manage" class="sr-only"><s:text name="note.userbar.intro" />:</p>

<p>
	<a class="btn btn-info btn-block" href="<s:url namespace="/do/BaseAdmin" action="settings" />"><span class="icon fa fa-cog"></span>&#32;<s:text name="menu.configure" /></a>
</p>

<ul class="nav nav-pills nav-stacked" id="backoffice-menu-main" role="menubar">

	<wp:info key="systemParam" paramName="groupsOnDemand" var="groupsOnDemandVar" />
	<c:if test="${groupsOnDemandVar}" >
	<wp:ifauthorized permission="superuser">
		<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/Group" />"><s:text name="menu.accountAdmin.groups" /></a></li>
	</wp:ifauthorized>
	</c:if>

	<wp:info key="systemParam" paramName="categoriesOnDemand" var="categoriesOnDemandVar" />
	<c:if test="${categoriesOnDemandVar}">
	<wp:ifauthorized permission="manageCategories">
		<li role="presentation"><a role="menuitem" href="<s:url action="viewTree" namespace="/do/Category" />"><s:text name="menu.categoryAdmin" /></a></li>
	</wp:ifauthorized>
	</c:if>

	<wpsa:pluginsSubMenu objectName="pluginsSubMenus" />
	<s:if test="#pluginsSubMenus.size > 0">
		<li class="panel-group" role="presentation">
			<div class="panel panel-default" role="presentation">
				<div class="panel-heading" role="presentation">
					<a data-toggle="collapse" href="#submenu-plugins" class="display-block" id="aria-menu-plugin"  aria-haspopup="true" role="menuitem">
						<s:text name="menu.plugins" />&#32;
						<span class="icon fa fa-chevron-down pull-right"></span>
					</a>
				</div>
				<div id="submenu-plugins" class="panel-collapse collapse">
					<ul class="panel-body nav nav-pills nav-stacked" role="menubar" aria-labelledby="aria-menu-plugin">
						<s:iterator value="#pluginsSubMenus" id="pluginSubMenu">
							<wpsa:include value="%{#pluginSubMenu.subMenuFilePath}"></wpsa:include>
						</s:iterator>
					</ul>
				</div>
			</div>
		</li>
	</s:if>
	<s:else>
		<li role="presentation"><a href="#" role="menuitem" ><s:text name="menu.plugins" /></a></li>
	</s:else>

<wp:ifauthorized permission="managePages" var="isEditPages" />
<wp:ifauthorized permission="superuser" var="isSuperuser" />

<c:if test="${isEditPages || isSuperuser}">

<wp:ifauthorized permission="managePages">
	<li role="presentation"><a role="menuitem" href="<s:url action="viewTree" namespace="/do/Page" />"><s:text name="menu.pageAdmin" /></a></li>
	<li role="presentation"><a role="menuitem" href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />"><s:text name="menu.widgetAdmin" /></a></li>
</wp:ifauthorized>

<wp:info key="systemParam" paramName="apisOnDemand" var="apisOnDemandVar" />
<c:if test="${apisOnDemandVar}" >
<wp:ifauthorized permission="superuser">
	<li class="panel-group" role="presentation">
		<div class="panel panel-default" role="presentation">
			<div class="panel-heading" role="presentation">
				<a data-toggle="collapse" href="#submenu-api" class="display-block" id="aria-menu-api" aria-haspopup="true" role="menuitem">
					<s:text name="menu.apisAdmin" />&#32;
					<span class="icon fa fa-chevron-down pull-right"></span>
				</a>
			</div>
			<div id="submenu-api" class="panel-collapse collapse" role="presentation">
				<ul class="panel-body nav nav-pills nav-stacked"  role="menubar" aria-labelledby="aria-menu-api">
					<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/Api/Resource" />" ><s:text name="menu.apisAdmin.resources" /></a></li>
					<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/Api/Service" />" ><s:text name="menu.apisAdmin.services" /></a></li>
					<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/Api/Consumer" />" ><s:text name="menu.apisAdmin.consumers" /></a></li>
				</ul>
			</div>
		</div>
	</li>
</wp:ifauthorized>
</c:if>

</c:if>

<wp:ifauthorized permission="editContents" var="isEditContents" />
<wp:ifauthorized permission="manageResources" var="isManageResources" />

<c:if test="${isEditContents || isManageResources}">
	<wp:ifauthorized permission="editContents">

		<li class="panel-group" role="presentation">
			<div class="panel panel-default overflow-visible" role="presentation">
				<div class="panel-heading" role="presentation">
					<a data-toggle="collapse" href="#submenu-contents" class="display-block" id="aria-menu-jacms-contentadmin"  aria-haspopup="true" role="menuitem">
						<s:text name="jacms.menu.contentAdmin" />&#32;
						<span class="icon fa fa-chevron-down pull-right"></span>
					</a>
				</div>
				<div id="submenu-contents" class="panel-collapse collapse" role="presentation">
					<ul class="panel-body nav nav-pills nav-stacked" role="menubar" aria-labelledby="aria-menu-jacms-contentadmin">
						<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/jacms/Content" />"><s:text name="jacms.menu.contentAdmin.list" /></a></li>
						<wpsa:entityTypes entityManagerName="jacmsContentManager" var="contentTypesVar" />
						<li class="dropdown hidden-xs hidden-sm visible-md visible-lg" role="presentation">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#" id="aria-menu-jacms-new" role="menuitem">
								<s:text name="label.new.male" />&#32;<s:text name="label.content" />&#32;<span class="caret"></span>
							</a>
							<ul class="dropdown-menu" role="menubar" aria-labelledby="aria-menu-jacms-new">
								<s:iterator var="contentTypeVar" value="#contentTypesVar">
									<jacmswpsa:contentType typeCode="%{#contentTypeVar.typeCode}" property="isAuthToEdit" var="isAuthToEditVar" />
									<s:if test="%{#isAuthToEditVar}">
									<li role="presentation" class="hidden-xs hidden-sm visible-md visible-lg"><a role="menuitem" href="<s:url action="createNew" namespace="/do/jacms/Content" >
											   <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
										   </s:url>" ><s:text name="label.new.male" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" /></a></li>
									</s:if>
								</s:iterator>
							</ul>
						</li>
						<s:iterator var="contentTypeVar" value="#contentTypesVar">
							<jacmswpsa:contentType typeCode="%{#contentTypeVar.typeCode}" property="isAuthToEdit" var="isAuthToEditVar" />
							<s:if test="%{#isAuthToEditVar}">
							<li role="presentation" class="visible-xs visible-sm hidden-md hidden-lg"><a role="menuitem" href="<s:url action="createNew" namespace="/do/jacms/Content" >
									   <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
								   </s:url>" ><s:text name="label.new.male" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" /></a></li>
							</s:if>
						</s:iterator>
						<li role="presentation" class="divider visible-xs visible-sm hidden-md hidden-lg"><hr role="presentation" class="margin-none" />
						<wp:ifauthorized permission="superuser">
							<wp:info key="systemParam" paramName="contentModelsOnDemand" var="contentModelsOnDemandVar" />
							<c:if test="${contentModelsOnDemandVar}" >
							<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/jacms/ContentModel" />"><s:text name="jacms.menu.contentModelAdmin" /></a></li>
							</c:if>
							<wp:info key="systemParam" paramName="contentTypesOnDemand" var="contentTypesOnDemandVar" />
							<c:if test="${contentTypesOnDemandVar}" >
							<li role="presentation"><a role="menuitem" href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName">jacmsContentManager</s:param></s:url>"><s:text name="jacms.menu.contentTypeAdmin" /></a></li>
							</c:if>
						</wp:ifauthorized>
					</ul>
				</div>
			</div>
		</li>

	</wp:ifauthorized>

	<wp:info key="systemParam" paramName="resourceArchivesOnDemand" var="resourceArchivesOnDemandVar" />
	<c:if test="${resourceArchivesOnDemandVar}" >
	<wp:ifauthorized permission="manageResources">
		<li role="presentation" class="panel-group">
			<div role="presentation" class="panel panel-default">
				<div role="presentation" class="panel-heading">
					<a data-toggle="collapse" href="#submenu-resources" class="display-block" id="aria-menu-jacms-resources"  aria-haspopup="true" role="menuitem">
						<s:text name="jacms.menu.resourceAdmin" />&#32;
						<span class="icon fa fa-chevron-down pull-right"></span>
					</a>
				</div>
				<div id="submenu-resources" class="panel-collapse collapse" role="presentation">
					<ul class="panel-body nav nav-pills nav-stacked" role="menubar" aria-labelledby="aria-menu-jacms-resources">
						<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/jacms/Resource"><s:param name="resourceTypeCode" >Image</s:param></s:url>"><s:text name="jacms.menu.imageAdmin" /></a></li>
						<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/jacms/Resource"><s:param name="resourceTypeCode" >Attach</s:param></s:url>"><s:text name="jacms.menu.attachAdmin" /></a></li>
					</ul>
				</div>
			</div>
		</li>
	</wp:ifauthorized>
	</c:if>

</c:if>

<c:if test="${isSuperuser}">
	<li class="panel-group" role="presentation">
		<div class="panel panel-default" role="presentation">
			<div class="panel-heading" role="presentation">
				<a data-toggle="collapse" href="#submenu-template" class="display-block" id="aria-menu-template" aria-haspopup="true" role="menuitem">
					<s:text name="menu.template" />&#32;
					<span class="icon fa fa-chevron-down pull-right"></span>
				</a>
			</div>
			<div id="submenu-template" class="panel-collapse collapse" role="presentation">
				<ul class="panel-body nav nav-pills nav-stacked" role="menubar" aria-labelledby="aria-menu-template">
					<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/PageModel" />"><s:text name="menu.pageModelAdmin" /></a></li>

					<li><a href="<s:url namespace="/do/Portal/GuiFragment" action="list" />" ><s:text name="menu.guiFragmentAdmin" /></a></li>

					<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/FileBrowser" />"><s:text name="menu.filebrowserAdmin" /></a></li>
				</ul>
			</div>
		</div>
	</li>
</c:if>

</ul>
