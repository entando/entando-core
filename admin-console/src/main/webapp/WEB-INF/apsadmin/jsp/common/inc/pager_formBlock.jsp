<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="#group.size > #group.max">

<ul class="pagination">
	<s:if test="null != #group.pagerId">
		<s:set var="pagerIdMarker" value="#group.pagerId" />
	</s:if>
	<s:else>
		<s:set var="pagerIdMarker">pagerItem</s:set>
	</s:else>

	<s:if test="#group.advanced">
	<li>
		<wpsf:submit name="%{#pagerIdMarker + '_1'}" type="button" disabled="%{1 == #group.currItem}" title="%{getText('label.goToFirst')}">
			<span class="icon fa fa-step-backward"></span>
		</wpsf:submit>
	</li>
	<li>
		<wpsf:submit name="%{#pagerIdMarker + '_' + (#group.currItem - #group.offset) }" type="button" disabled="%{1 == #group.beginItemAnchor}" title="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.backward')}">
			<span class="icon fa fa-fast-backward"></span>
		</wpsf:submit>
	</li>
	</s:if>

	<li>
		<wpsf:submit name="%{#pagerIdMarker + '_' + #group.prevItem}" type="button" title="%{getText('label.prev.full')}" disabled="%{1 == #group.currItem}">
			<span class="icon fa fa-long-arrow-left"></span>
		</wpsf:submit>
	</li>

	<s:subset source="#group.items" count="#group.endItemAnchor-#group.beginItemAnchor+1" start="#group.beginItemAnchor-1">
		<s:iterator id="item">
			<li>
				<wpsf:submit name="%{#pagerIdMarker + '_' + #item}" type="button" disabled="%{#item == #group.currItem}">
					<s:property value="%{#item}" />
				</wpsf:submit>
			</li>
		</s:iterator>
	</s:subset>

	<li>
		<wpsf:submit name="%{#pagerIdMarker + '_' + #group.nextItem}" type="button" title="%{getText('label.next.full')}" disabled="%{#group.maxItem == #group.currItem}">
			<span class="icon fa fa-long-arrow-right"></span>
		</wpsf:submit>
	</li>

	<s:if test="#group.advanced">
	<s:set name="jumpForwardStep" value="#group.currItem + #group.offset"></s:set>
	<li>
		<wpsf:submit name="%{#pagerIdMarker + '_' + (#jumpForwardStep)}" type="button" disabled="%{#group.maxItem == #group.endItemAnchor}" title="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.forward')}">
			<span class="icon fa fa-fast-forward"></span>
		</wpsf:submit>
	</li>
	<li>
		<wpsf:submit name="%{#pagerIdMarker + '_' + #group.size}" type="button" disabled="%{#group.maxItem == #group.currItem}" title="%{getText('label.goToLast')}">
			<span class="icon fa fa-step-forward"></span>
		</wpsf:submit>
	</li>
	</s:if>

	<s:set var="pagerIdMarker" value="null" />
</ul>

</s:if>