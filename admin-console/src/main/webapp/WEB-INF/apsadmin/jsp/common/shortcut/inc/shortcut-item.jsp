<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="shortcutsIconsVar" value="#{
'components': 'icon fa fa-puzzle-piece',
'tools': 'icon fa fa-gears',
'cms': 'icon fa fa-paperclip',
'plugins': 'icon fa fa-flask',
'portal': 'icon fa fa-globe'
}" />
<wpsa:shortcut key="%{#userShortcutCode}" var="userShortcut" />
<s:if test="%{null != #userShortcut}">
	<s:set var="emptyShortcutConfigVar" value="%{false}" />
	<s:set var="userShortcutSectionShortDescr" value="%{ null != #userShortcut.menuSection.descriptionKey ? getText(#userShortcut.menuSection.descriptionKey) : #userShortcut.menuSection.description }" />
	<s:set var="userShortcutSectionLongDescr" value="%{ null != #userShortcut.menuSection.longDescriptionKey ? getText(#userShortcut.menuSection.longDescriptionKey) : #userShortcut.menuSection.longDescription }" />
	<s:set var="userShortcutShortDescr" value="%{ null != #userShortcut.descriptionKey ? getText(#userShortcut.descriptionKey) : #userShortcut.description }" />
	<s:set var="userShortcutLongDescr" value="%{ null != #userShortcut.longDescriptionKey ? getText(#userShortcut.longDescriptionKey) : #userShortcut.longDescription }" />

	<s:set var="toolbarClass"> full </s:set>
</s:if>
<s:else>
	<s:set var="toolbarClass"> empty sc-hidden </s:set>
</s:else>

<div role="toolbar" data-entando-position="<s:property value="#position" />"
	class="<s:property value="%{ #toolbarClass + ' margin-small-bottom shortcut-item col-lg-6 btn-toolbar '}" /><s:property value="#userShortcut.menuSectionCode" />">
				<s:if test="null != #userShortcut">
					<div class="btn-group btn-group-justified">
						<a
							class="btn btn-block btn-default btn-xs"
							href="<s:url action="%{#userShortcut.actionName}" namespace="%{#userShortcut.namespace}"><wpsa:paramMap map="#userShortcut.parameters" /></s:url>"
							title="[<s:property value="%{#userShortcutSectionShortDescr}" />] <s:property value="%{#userShortcutLongDescr}" />"
							data-entando="shortcut-element"
							>
								<span class="shortcut-text margin-small-left">
								<span class="text-info <s:property value="#shortcutsIconsVar.get(#userShortcut.menuSectionCode)" />" data-entando="shortcut-icon"></span>
									<span data-entando="shortcut-description"><s:property value="%{#userShortcutShortDescr}" /></span>
								</span>
						</a>
					</div>
				</s:if>
				<s:else>
					<a
						data-toggle="modal" data-target="#shortcut-configure-modal"
						class="btn-group btn-group-justified sc-hidden"
						data-entando-action="shortcut-add"
						href="<s:url action="configPosition" namespace="/do/MyShortcut" anchor="shortcut-configure-modal"><s:param name="position" value="%{#position}" /><s:param name="strutsAction" value="1" /></s:url>"
						title="<s:text name="shortcuts.label.configure" />&#32;<s:text name="shortcuts.label.position" />&#32;<s:property value="%{#position + 1}" />"
						>
						 <span class="btn btn-default btn-xs btn-block" style="text-align: left;">
						 	<span class="margin-small-left icon fa fa-plus-square" data-entando="shortcut-icon"></span>
						 	<span data-entando-role="empty"><s:property value="#position+1" /></span>
					 	</span>
					</a>
				</s:else>

				<div class="shortcuts-configure-item-toolbar text-center sc-hidden">
					<%-- move up --%>
						<a
							data-entando-action="shortcut-move-up"
							class="text-decoration-none"
							title="<s:text name="shortcuts.label.moveup" />&#32;<s:property value="%{null != #userShortcut ? #userShortcutShortDescr : #position+1}" />"
							href="<s:url namespace="/do/MyShortcut" action="swapMyShortcut">
								<s:param name="positionTarget" value="%{#position}" />
								<s:param name="strutsAction" value="2" />
								<s:param name="positionDest" value="%{#position-1}" /></s:url>">
								<span class="sr-only"><s:text name="shortcuts.label.moveup" /></span>
								&ensp;<span class="icon fa fa-sort-desc"></span>&ensp;
						</a>
					<%-- move down --%>
						<a
							data-entando-action="shortcut-move-down"
							class="text-decoration-none"
							title="<s:text name="shortcuts.label.movedown" />&#32;<s:property value="%{null != #userShortcut ? #userShortcutShortDescr : #position+1}" />"
							href="<s:url namespace="/do/MyShortcut" action="swapMyShortcut">
								<s:param name="positionTarget" value="%{#position}" />
								<s:param name="strutsAction" value="2" />
								<s:param name="positionDest" value="%{#position+1}" /></s:url>">
								<span class="sr-only"><s:text name="shortcuts.label.movedown" /></span>
								&ensp;<span class="icon fa fa-sort-asc"></span>&ensp;
						</a>
					<s:if test="null != #userShortcut">
						<%-- clear --%>
						<a
							data-entando-action="remove"
							class="text-decoration-none"
							title="<s:text name="shortcuts.label.clear" />&#32;<s:property value="%{null != #userShortcut ? #userShortcutShortDescr : #position+1}" />"
							href="<s:url action="removeMyShortcut" namespace="/do/MyShortcut"><s:param name="position" value="%{#position}" /><s:param name="strutsAction" value="4" /></s:url>">
								<span class="sr-only"><s:text name="shortcuts.label.clear" /></span>
								<span class="icon fa fa-eraser"></span>
						</a>
						<%-- configure --%>
							<%-- <a
								data-entando-action="configure"
								class="btn btn-default btn-xs text-decoration-none"
								rel="shortcut-configure-modal"
								title="<s:text name="label.configure" />&#32;<s:text name="name.position" />&#32;<s:property value="%{#position + 1}" />"
								href="<s:url action="configPosition" namespace="/do/MyShortcut" anchor="shortcut-configure-modal"><s:param name="position" value="%{#position}" /><s:param name="strutsAction" value="1" /></s:url>">
									<span class="icon fa fa-cog"><span class="sr-only"><s:text name="label.set" /></span></span>
							</a>
							--%>
						<%-- move --%>
							<%-- <a
								data-entando-action="move"
								class="btn btn-default btn-xs text-decoration-none"
								title="<s:text name="label.move" />&#32;<s:text name="name.position" />&#32;<s:property value="%{#position + 1}" />"
								href="<s:url action="configPosition" namespace="/do/MyShortcut"><s:param name="positionTarget" value="%{#position}" /><s:param name="strutsAction" value="2" /></s:url>">
									<span class="icon fa fa-arrows"><span class="sr-only"><s:text name="label.move" /></span></span>
							</a>
							--%>
					</s:if>
				</div>
			</div>
<s:set var="userShortcut" value="%{null}" />
<s:set var="userShortcutCode" value="%{null}" />
