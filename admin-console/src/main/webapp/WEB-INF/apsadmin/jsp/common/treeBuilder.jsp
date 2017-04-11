<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<s:if test="%{#currentRoot.getChildren().length==0}">
	<s:set var="treeItemIconNameVar" value="'fa-folder-o'" />
</s:if>
<s:else>
	<s:set var="treeItemIconNameVar" value="'fa-folder'" />
</s:else>
<tr class="treeRow" id="<s:if test="true == #currentRoot.isRoot()">home</s:if><s:else><s:property value="#node.code"/></s:else>" data-parent='#<s:property value="#currentRoot.parent.code"/>' >
	<td class="treegrid-node">
<%-- 		<span class="icon fa fa-li <s:property value="#treeItemIconNameVar" />"></span>&#32; --%>
		<input type="radio" name="<s:property value="#inputFieldName" />" id="fagianonode_<s:property value="#currentRoot.code" />" value="<s:property value="#currentRoot.code" />" <s:if test="#currentRoot.children.length > 0">class="subTreeToggler tree_<s:property value="#currentRoot.code" />" </s:if> <s:if test="#currentRoot.code == #selectedTreeNode"> checked="checked"</s:if> />&#32;
		<span class='class="icon fa <s:property value="treeItemIconNameVar"/>'></span> &#32;
		<label for="fagianonode_<s:property value="#currentRoot.code" />">
			<s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
			<s:if test="%{#currentRoot.group != null && !#currentRoot.group.equals('free')}">&#32;
				<span class="text-muted icon fa fa-lock"></span>
			</s:if>
		</label>
	</td>
	<td>
		<div class="dropdown  dropdown-kebab-pf">
			<button class="btn btn-link dropdown-toggle" type="button"
				id="dropdownKebab" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="true">
				<span class="fa fa-ellipsis-v"></span>
			</button>
			<ul class="dropdown-menu " aria-labelledby="dropdownKebab">
				<li>
					<wpsf:submit type="button" action="detail"
						title="%{getText('category.options.detail')}"
						data-toggle="tooltip" cssClass="btn btn-info">
						<span class="sr-only"><s:text name="category.options.detail" /></span>
						<span> Dettagli </span>
					</wpsf:submit>
				</li>
				<li> 
					<a href="#">
						<wpsf:submit type="button" action="edit"
							title="%{getText('category.options.modify')}"
							data-toggle="tooltip" cssClass="btn btn-info">
							<span class="sr-only"><s:text
								name="category.options.modify" /></span>
							<span> Edit </span>
						</wpsf:submit>
					</a>
				</li>
				<wp:ifauthorized permission="superuser">
					<li>
						<a href="#">
							<button class="btn btn-info" data-toggle="modal"
								data-target="#modal-move-tree"
								title="Move this categoty or branch within another one">
								<span class="icon fa fa-sort"></span> </span>
							</button>
						</a>
					</li>
				</wp:ifauthorized>
				<li>
					<a href="#">
						<wpsf:submit type="button" action="trash"
							title="%{getText('category.options.delete')}"
							data-toggle="tooltip" cssClass="btn btn-warning">
							<span class="sr-only"><s:text
									name="category.options.delete" /></span>
							<span class="icon fa fa-times-circle"></span>
						</wpsf:submit>
					</a>
				</li>
			</ul>
		</div>
	</td>
</tr>
<s:if test="%{#currentRoot.getChildren().length>0}">
	<s:iterator status="iterator" value="#currentRoot.children" var="node">
		<s:set var="currentRoot" value="#node" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
	</s:iterator>
</s:if>
