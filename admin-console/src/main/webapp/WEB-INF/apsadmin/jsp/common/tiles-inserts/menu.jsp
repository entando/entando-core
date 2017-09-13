<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<script>
	$(document).ready(function () {
		// matchHeight the contents of each .card-pf and then the .card-pf itself
		$(".row-cards-pf > [class*='col'] > .card-pf .card-pf-title").matchHeight();
		$(".row-cards-pf > [class*='col'] > .card-pf > .card-pf-body").matchHeight();
		$(".row-cards-pf > [class*='col'] > .card-pf > .card-pf-footer").matchHeight();
		$(".row-cards-pf > [class*='col'] > .card-pf").matchHeight();
		// Initialize the vertical navigation
		$().setupVerticalNavigation(true);
		$('[data-toggle=popover]').popovers();
//        $(".bootstrap-switch").bootstrapSwitch();
});
</script>

<wp:ifauthorized permission="superuser" var="isSuperUser" />

<ul class="list-group">
    <wp:ifauthorized permission="managePages" var="isManagePage" />
    <c:if test="${isManagePage || isSuperUser}">
        <!-- Page Designer -->
        <li class="list-group-item secondary-nav-item-pf" data-target="#page-designer-secondary">
            <a>
                <span class="fa fa-files-o" data-toggle="tooltip" title="<s:text name="menu.pageDesigner" />" ></span>
                <span class="list-group-item-value"><s:text name="menu.pageDesigner" /></span>
            </a>

            <div id="page-designer-secondary" class="nav-pf-secondary-nav">
                <div class="nav-item-pf-header">
                    <a class="secondary-collapse-toggle-pf" data-toggle="collapse-secondary-nav"></a>
                    <span><s:text name="menu.pageDesigner" /></span>
                </div>

                <!-- Page Designer Secondary -->
                <ul class="list-group">
                    <li class="list-group-item">
                        <a id="linkHome" href='<s:url action="viewTree" namespace="/do/Page" />'>
                            <span class="list-group-item-value"><s:text name="menu.pageDesigner.pageTree" /></span>
                        </a>
                    </li>
                    <li class="list-group-item">
                        <a id="" href='<s:url action="viewTreeMenu" namespace="/do/Page/Console" />'>
                            <span class="list-group-item-value"><s:text name="menu.pageDesigner.pageConfiguration" /></span>
                        </a>
                    </li>
                    <c:if test="${isSuperUser}">
                        <li class="list-group-item">
                            <a href='<s:url action="systemParams" namespace="/do/Page" />'>
                                <span class="list-group-item-value"><s:text name="menu.pageDesigner.pageSettings" /></span>
                            </a>
                        </li>
                    </c:if>
                </ul>
                <!--Fine Page Designer Secondary-->
            </div>
        </li>
    </c:if>

    <!-- UX Patterns -->
    <c:if test="${isManagePage || isSuperUser}">
        <li class="list-group-item secondary-nav-item-pf" data-target="#ux-pattern-secondary">
            <a>
                <span class="fa fa-object-ungroup" data-toggle="tooltip" title="<s:text name="menu.UXPattern" />"></span>
                <span class="list-group-item-value"><s:text name="menu.UXPattern" /></span>
            </a>

            <div id="ux-pattern-secondary" class="nav-pf-secondary-nav">
                <div class="nav-item-pf-header">
                    <a class="secondary-collapse-toggle-pf" data-toggle="collapse-secondary-nav"></a>
                    <span><s:text name="menu.UXPattern" /></span>
                </div>

                <!-- UX Patterns Secondary -->
                <ul class="list-group">
                    <li class="list-group-item">
                        <a href='<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />'>
                            <span class="list-group-item-value"><s:text name="menu.UXPattern.widget" /></span>
                        </a>
                    </li>
                    <c:if test="${isSuperUser}">
                        <li class="list-group-item">
                            <a href='<s:url action="list" namespace="/do/Portal/GuiFragment" />'>
                                <span class="list-group-item-value"><s:text name="menu.UXPattern.fragments" /></span>

                            </a>
                        </li>

                        <li class="list-group-item">
                            <a href='<s:url action="list" namespace="/do/PageModel" />'>
                                <span class="list-group-item-value"><s:text name="menu.UXPattern.pageModels" /></span>
                            </a>
                        </li>
                    </c:if>
                </ul>
                <!--Fine UX Patterns Secondary-->
            </div>
        </li>
    </c:if>

    <!-- Integrations -->

    <li class="list-group-item secondary-nav-item-pf" data-target="#integration-secondary">
        <a>
            <span class="fa fa-cubes" data-toggle="tooltip" title="<s:text name="menu.integrations" />"></span>
            <span class="list-group-item-value"><s:text name="menu.integrations" /></span>
        </a>

        <!--Integrations secondary-->

        <div id="integration-secondary" class="nav-pf-secondary-nav">
            <div class="nav-item-pf-header">
                <a class="secondary-collapse-toggle-pf" data-toggle="collapse-secondary-nav"></a>
                <span><s:text name="menu.integrations" /></span>
            </div>

            <ul class="list-group">
                <li class="list-group-item tertiary-nav-item-pf" data-target="integrations-ux-components-tertiary">
                    <a>
                        <span class="list-group-item-value"><s:text name="menu.integrations.components" /></span>
                    </a>
					<wpsa:pluginsSubMenu objectName="pluginsSubMenusVar" />
					<s:if test="#pluginsSubMenusVar.size > 0">
						<div id="integrations-ux-components-tertiary" class="nav-pf-tertiary-nav">
							<div class="nav-item-pf-header">
								<a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
								<span><s:text name="menu.integrations.components" /></span>
							</div>
							<ul class="list-group">

								<s:iterator value="#pluginsSubMenusVar" var="pluginSubMenuVar">
									<s:include value="%{#pluginSubMenuVar.subMenuFilePath}" />
								</s:iterator>
							</ul>
						</div>
					</s:if>
                </li>
                <c:if test="${isSuperUser}">
                    <li class="list-group-item tertiary-nav-item-pf" data-target="integrations-api-tertiary">
                        <a>
                            <span class="list-group-item-value"><s:text name="menu.integrations.APIManagement" /></span>
                        </a>

                        <div id="integrations-api-tertiary" class="nav-pf-tertiary-nav">
                            <div class="nav-item-pf-header">
                                <a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
                                <span><s:text name="menu.integrations.APIManagement" /></span>
                            </div>
                            <ul class="list-group">
                                <li class="list-group-item">
                                    <a href='<s:url action="list" namespace="/do/Api/Resource" />'>
                                        <span class="list-group-item-value"><s:text name="menu.integrations.API.resources" /></span>
                                    </a>
                                </li>
                                <li class="list-group-item">
                                    <a href='<s:url action="list" namespace="/do/Api/Service" />'>
                                        <span  class="list-group-item-value"><s:text name="menu.integrations.API.services" /></span>
                                    </a>
                                </li>
                                <li class="list-group-item">
                                    <a href='<s:url action="list" namespace="/do/Api/Consumer" />'>
                                        <span class="list-group-item-value"><s:text name="menu.integrations.API.consumers" /></span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </li>
                </c:if>
            </ul>
        </div>
    </li>

    <wp:ifauthorized permission="viewUsers" var="isViewUsers" />
    <wp:ifauthorized permission="editUsers" var="isEditUsers" />
    <wp:ifauthorized permission="editUserProfile" var="isEditProfiles" />
    <c:if test="${isViewUsers || isEditUsers || isEditProfiles}">
        <!--  Users Settings -->
        <li class="list-group-item secondary-nav-item-pf" data-target="#user-settings-secondary">
            <a>
                <span class="fa fa-users" data-toggle="tooltip" title="<s:text name="menu.userSettings" />" ></span>
                <span class="list-group-item-value"><s:text name="menu.userSettings" /></span>
            </a>

            <div id="#user-settings-secondary" class="nav-pf-secondary-nav">
                <div class="nav-item-pf-header">
                    <a class="secondary-collapse-toggle-pf" data-toggle="collapse-secondary-nav"></a>
                    <span><s:text name="menu.userSettings"/></span>
                </div>

                <!-- Users Settings Secondary -->

                <ul class="list-group">
                    <li class="list-group-item">
                        <a href='<s:url action="list" namespace="/do/User" />'>
                            <span class="list-group-item-value"><s:text name="menu.usersSettings.users" /></span>
                        </a>
                    </li>

                    <c:if test="${isSuperUser}">
                        <li class="list-group-item">
                            <a href='<s:url action="initViewEntityTypes" namespace="/do/Entity" ><s:param name="entityManagerName">UserProfileManager</s:param></s:url>'>
								<span class="list-group-item-value"><s:text name="menu.usersSettings.profileTypes" /></span>
							</a>
						</li>

                        <c:if test="${isSuperUser}">
                            <li class="list-group-item">
                                <a href='<s:url action="systemParams" namespace="/do/User" />'>
                                    <span class="list-group-item-value"><s:text name="menu.usersSettings.usersRestriction" /></span>
                                </a>
                            </li>
                        </c:if>
                        <li class="list-group-item">
                            <a href='<s:url action="list" namespace="/do/Role" />'>
                                <span class="list-group-item-value"><s:text name="menu.usersSettings.roles" /></span>
                            </a>
                        </li>
                    </c:if>
                </ul>
                <!--Fine Users Settings Secondary-->
            </div>
        </li>
    </c:if>

    <!-- APPS -->
    <li class="list-group-item secondary-nav-item-pf" data-target="#apps-secondary">
        <a>
            <span class="fa fa-rocket" data-toggle="tooltip" title="<s:text name="menu.APPS" />"></span>
            <span class="list-group-item-value"><s:text name="menu.APPS" /></span>
        </a>

        <!--Integrations secondary-->

        <div id="apps-secondary" class="nav-pf-secondary-nav">
            <div class="nav-item-pf-header">
                <a class="secondary-collapse-toggle-pf" data-toggle="collapse-secondary-nav"></a>
                <span><s:text name="menu.APPS" /></span>
            </div>
            <wp:ifauthorized permission="editContents" var="isEditContents" />
            <wp:ifauthorized permission="manageResources" var="isManageResources" />

            <ul class="list-group">
                <c:if test="${isEditContents || isManageResources}">
                    <li class="list-group-item tertiary-nav-item-pf" data-target="apps-cms-tertiary">
                        <a>
                            <span class="list-group-item-value"><s:text name="menu.APPS.CMS" /></span>
                        </a>
                        <div id="apps-cms-tertiary" class="nav-pf-tertiary-nav">
                            <div class="nav-item-pf-header">
                                <a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
                                <span><s:text name="menu.APPS.CMS" /></span>
                            </div>
                            <ul class="list-group">
                                <c:if test="${isEditContents}">
                                    <li class="list-group-item">
                                        <a href="<s:url action="list" namespace="/do/jacms/Content" />">
											<span class="list-group-item-value"><s:text name="menu.APPS.CMS.contents" /></span>
                                        </a>
                                    </li>
                                </c:if>
                                <c:if test="${isManageResources}">
                                    <li class="list-group-item">
                                        <a href="<s:url action="list" namespace="/do/jacms/Resource" ><s:param name="resourceTypeCode" >Image</s:param></s:url>">
											<span class="list-group-item-value"><s:text name="menu.APPS.CMS.digitalAssets" /></span>
										</a>
									</li>
                                </c:if>
                                <c:if test="${isSuperUser}">
                                    <li class="list-group-item">
                                        <a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName">jacmsContentManager</s:param></s:url>">
											<span class="list-group-item-value"><s:text name="menu.APPS.CMS.contentTypes" /></span>
										</a>
									</li>
									<li class="list-group-item">
										<a href="<s:url action="list" namespace="/do/jacms/ContentModel" />">
											<span class="list-group-item-value"><s:text name="menu.APPS.CMS.contentModels" /></span>
                                        </a>
                                    </li>
                                    <li class="list-group-item">
                                        <a href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />">
											<span class="list-group-item-value"><s:text name="menu.APPS.CMS.contentSettings" /></span>
                                        </a>
                                    </li>
                                </c:if>
                            </ul>
                        </div>
                    </li>
                </c:if>
                <li class="list-group-item disabled">
                    <a>
                        <span class="ml-5"><s:text name="menu.APPS.IoT" /></span>
                    </a>
                </li>
            </ul>
        </div>
    </li>
	
	
	
    <!-- DATA TYPE -->
    <c:if test="${isSuperUser}">
        <li class="list-group-item secondary-nav-item-pf" data-target="#datatype-secondary">
            <a>
                <span class="fa fa-object-ungroup" data-toggle="tooltip" title="<s:text name="menu.DataType" />"></span>
                <span class="list-group-item-value"><s:text name="menu.DataType" /></span>
            </a>
            <div id="datatype-secondary" class="nav-pf-secondary-nav">
                <div class="nav-item-pf-header">
                    <a class="secondary-collapse-toggle-pf" data-toggle="collapse-secondary-nav"></a>
                    <span><s:text name="menu.DataType" /></span>
                </div>
                <!-- DataType Secondary -->
                <ul class="list-group">
                    <c:if test="${isSuperUser}">
						<li class="list-group-item">
							<a href="<s:url action="list" namespace="/do/dataobject/model" />">
								<span class="list-group-item-value"><s:text name="menu.DataType.UX" /></span>
							</a>
						</li>
                    </c:if>
                </ul>
                <!--Fine DATA TYPE Secondary-->
            </div>
        </li>
    </c:if>
	
	
</ul>

<wp:ifauthorized permission="manageCategories" var="isCategories" />
<c:if test="${isCategories || isSuperUser}">
    <ul class="list-group fixed-bottom">

        <li class="list-group-item secondary-nav-item-pf" data-target="#settings-secondary">
            <a>
                <span class="fa fa-cogs" data-toggle="tooltip" title="<s:text name="menu.settings" />"></span>
                <span class="list-group-item-value"><s:text name="menu.settings" /></span>
            </a>

            <div id="#settings-secondary" class="nav-pf-secondary-nav">
                <div class="nav-item-pf-header">
                    <a class="secondary-collapse-toggle-pf" data-toggle="collapse-secondary-nav"></a>
                    <span><s:text name="menu.settings" /></span>
                </div>

                <!-- Settings Secondary -->

                <ul class="list-group">
                    <c:if test="${isCategories}">
                        <li class="list-group-item">
                            <a href='<s:url action="viewTree" namespace="/do/Category" />'>
                                <span class="list-group-item-value"><s:text name="menu.settings.categories" /></span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${isSuperUser}">
                        <li class="list-group-item">
                            <a href='<s:url action="list" namespace="/do/Lang" />'>
                                <span class="list-group-item-value"><s:text name="menu.settings.labelsLanguages" /></span>
                            </a>
                        </li>

                        <li class="list-group-item">
                            <a href='<s:url action="reloadChoose" namespace="/do/BaseAdmin" />'>
                                <span class="list-group-item-value"><s:text name="menu.settings.reloadConfigurations" /></span>
                            </a>
                        </li>

                        <li class="list-group-item">
                            <a href='<s:url action="entry" namespace="/do/Admin/Database" />'>
                                <span class="list-group-item-value"><s:text name="menu.settings.database" /></span>
                            </a>
                        </li>

                        <li class="list-group-item">
                            <a href='<s:url action="list" namespace="/do/FileBrowser" />'>
                                <span class="list-group-item-value"><s:text name="menu.settings.fileBrowser" /></span>
                            </a>
                        </li>

                        <li class="list-group-item">
                            <a href='<s:url action="list" namespace="/do/Group" />'>
                                <span class="list-group-item-value"><s:text name="menu.settings.groups" /></span>
                            </a>
                        </li>
                    </c:if>
                </ul>
                <!--Fine Users Settings Secondary-->
            </div>

        </li>
    </ul>
</c:if>
