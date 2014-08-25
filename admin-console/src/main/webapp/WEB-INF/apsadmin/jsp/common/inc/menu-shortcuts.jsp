<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%--
properties label to add:

shortcuts.modal.button.close=Close
shortcuts.modal.label.set=Set
shortcuts.label.add=Add Shortcut
shortcuts.label.title=Shortcuts
shortcuts.label.configure=Configure Shortcuts
shortcuts.label.edit=Edit
shortcuts.label.moveup=Move Up
shortcuts.label.movedown=Move Down
shortcuts.label.clear=Clear
shortcuts.label.choose=Choose
shortcuts.label.position=Position
--%>
<s:set var="shortcutsIconsVar" value="#{
'components': 'icon fa fa-puzzle-piece',
'tools': 'icon fa fa-gears',
'cms': 'icon fa fa-paperclip',
'plugins': 'icon fa fa-flask',
'portal': 'icon fa fa-globe'
}" />
<wpsa:userShortcutsConfig var="userConfigVar" />
<s:set var="userConfigVar" value="#userConfigVar.config" />
<div id="entando-menu-shortcuts" class="margin-base-bottom">
	<s:set var="emptyShortcutConfigVar" value="%{true}" />
	<s:set var="fullShortcutConfigVar" value="%{0}" />
	<s:iterator value="#userConfigVar" var="userShortcutCode" status="rowstatus">
		<wpsa:shortcut key="%{#userShortcutCode}" var="userShortcut" />
		<s:if test="null != #userShortcut">
				<s:set var="emptyShortcutConfigVar" value="%{false}" />
		</s:if>
		<s:else>
			<s:set var="fullShortcutConfigVar" value="%{#fullShortcutConfigVar+1}" />
		</s:else>
		<s:set var="userShortcut" value="%{null}" />
	</s:iterator>
	<div class="text-muted small display-block">
		<s:text name="shortcuts.label.title" />
		<a
			href="#entando-menu-shortcuts-container"
			class="pull-right <s:if test="#emptyShortcutConfigVar"> hidden </s:if>" id="edit-shortcuts"
			title="<s:text name="shortcuts.label.edit" />"
			data-entando="config-switch-edit"
			>
				<span class="sr-only"><s:text name="shortcuts.label.edit" />&#32;</span>
				<span class="icon fa fa-cog"></span>
		</a>
		<a
			data-entando="config-switch-add"
			data-toggle="modal" data-target="#shortcut-configure-modal" data-entando-action="shortcut-add"
			data-entando-position="0"
			class="btn-toolbar pull-right <s:if test="!#emptyShortcutConfigVar"> hidden </s:if>"
			href="<s:url action="configPosition" namespace="/do/MyShortcut" anchor="shortcut-configure-modal"><s:param name="position" value="0" /><s:param name="strutsAction" value="1" /></s:url>"
			title="add shortcut" >
					<span class="icon fa fa-plus-square"></span>
					<span class="sr-only"><s:text name="shortcuts.label.add" /></span>
		</a>
	</div>
	<div class="shortcuts-container row" id="entando-menu-shortcuts-container">
		<s:iterator value="#userConfigVar" var="userShortcutCode" status="rowstatus">
			<s:set var="position" value="#rowstatus.index" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/shortcut/inc/shortcut-item.jsp" />
		</s:iterator>
	</div>

	<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" id="shortcut-configure-modal" aria-labelledby="shortcut-configure-modal" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel"><label for="shortcutCode"><s:text name="shortcuts.label.configure" /></label></h4>
				</div>
				<s:form
					namespace="/do/MyShortcut"
					action="joinMyShortcut"
					cssClass="modal-body"
					id="form-shortcut-add">
							<p class="noscreen">
								<wpsf:hidden name="position" />
								<wpsf:hidden name="strutsAction" value="1" />
							</p>
							<div class="form-group">
								<label for="shortcutCode" class="control-label sr-only"><s:text name="shortcuts.label.choose" /></label>
								<wpsa:shortcutList type="list_items" var="allowedShortcutSelectItems"/>
								<select name="shortcutCode" id="shortcutCode" class="form-control">
									<s:set var="tmpShortcutGroup" value="%{null}" />
									<s:iterator value="#allowedShortcutSelectItems" var="allowedShortcutItem">
										<s:if test="null != #tmpShortcutGroup && !#allowedShortcutItem.optgroup.equals(#tmpShortcutGroup)">
											</optgroup>
										</s:if>
										<s:if test="null == #tmpShortcutGroup || !#allowedShortcutItem.optgroup.equals(#tmpShortcutGroup)">
											<optgroup label="<s:property value="#allowedShortcutItem.optgroup" />">
										</s:if>
											<option value="<s:property value="#allowedShortcutItem.key" />">
												<s:property value="#allowedShortcutItem.value" />
											</option>
											<s:set var="tmpShortcutGroup" value="#allowedShortcutItem.optgroup" />
									</s:iterator>
									</optgroup>
								</select>
							</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">
								<s:text name="shortcuts.modal.button.close" />
							</button>
							<button type="submit" class="btn btn-primary">
								<s:text name="shortcuts.modal.label.set" />
							</button>
						</div>
				</s:form>
			</div>
		</div>
	</div>

</div>
