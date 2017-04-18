<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<!-- 
<span><span class="pagination-pf-items-current">1-15</span> of <span class="pagination-pf-items-total">75</span></span>
<ul class="pagination pagination-pf-back">
  <li class="disabled"><a href="#" title="First Page"><span class="i fa fa-angle-double-left"></span></a></li>
<li class="disabled"><a href="#" title="Previous Page"><span class="i fa fa-angle-left"></span></a></li>
</ul>
<label for="{{include.id}}-page" class="sr-only">Current Page</label>
<input class="pagination-pf-page" type="text" value="1" id="{{include.id}}-page"/>
<span>of <span class="pagination-pf-pages">5</span></span>
<ul class="pagination pagination-pf-forward">
  <li><a href="#" title="Next Page"><span class="i fa fa-angle-right"></span></a></li>
<li><a href="#" title="Last Page"><span class="i fa fa-angle-double-right"></span></a></li>
</ul>
 -->
 
<s:if test="#group.size > #group.max">
	<div class="pagination">
		<s:if test="null != #group.pagerId">
			<s:set var="pagerIdMarker" value="#group.pagerId" />
		</s:if>
		<s:else>
			<s:set var="pagerIdMarker">pagerItem</s:set>
		</s:else>
	    
	    <ul class="pagination pagination-pf-back">
            <s:set var="isDisabled" value="%{false}" />
            <s:if test="%{1 == #group.currItem}">
                <s:set var="isDisabled" value="%{true}" />
            </s:if>

			<s:if test="#group.advanced">
				<li ${(isDisabled)?'class="disabled"':''}>
	                <a href="#" title="<s:text name="label.goToFirst"/>">
	                    <span class="i fa fa-angle-double-left"></span>
	                </a>
					<wpsf:submit name="%{#pagerIdMarker + '_1'}" type="button" title="%{getText('label.goToFirst')}" cssClass="hidden">
						<span class="i fa fa-angle-double-left"></span>
					</wpsf:submit>
				</li>
			</s:if>

			<li ${(isDisabled)?'class="disabled"':''}>
                <a href="#" title="<s:text name="label.prev.full"/>">
                    <span class="i fa fa-angle-left"></span>
                </a>
				<wpsf:submit name="%{#pagerIdMarker + '_' + #group.prevItem}" type="button" title="%{getText('label.prev.full')}" disabled="%{1 == #group.currItem}" cssClass="hidden">
					<span class="i fa fa-angle-left"></span>
				</wpsf:submit>
			</li>
		</ul>
	
	    <s:subset source="#group.items" count="#group.endItemAnchor-#group.beginItemAnchor+1" start="#group.beginItemAnchor-1">
			<select class="pagination-pf-page form-control" style="width: 50px">
			 <s:iterator var="item">
			     <s:if test="%{#item == #group.currItem}">
			         <option value="${pagerIdMarker}_${item}" selected="selected"><s:property value="#item"/></option>
			     </s:if>
			     <s:else>
				     <option value="${pagerIdMarker}_${item}"><s:property value="#item"/></option>
			     </s:else>
			 </s:iterator>
			<select> 
			<wpsf:submit name="%{#pagerIdMarker + '_' + #item}" type="button" disabled="%{#item == #group.currItem}" cssClass="hidden">
			    <s:property value="%{#item}" />
			</wpsf:submit>
	    </s:subset>	
	
        <!-- Current Page -->
<%--         <label for="{{include.id}}-page" class="sr-only"><s:text name="label.currentPage"/></label> --%>
<%--         <input class="pagination-pf-page" type="text" value="${group.currItem}" data-button-prefix="${pagerIdMarker}" data-submit="${pagerIdMarker}_${group.currItem}"/> --%>
            <span style="margin-top: 3px">of <span class="pagination-pf-pages"><s:property value="#group.maxItem" /></span></span>
        
		<wpsf:submit name="%{#pagerIdMarker + '_' + #group.currItem}" type="button" cssClass="hidden page-navigator"/>

	    <ul class="pagination pagination-pf-forward">
			<s:set var="isDisabled" value="%{false}" />
			<s:if test="%{#group.maxItem == #group.currItem}">
				<s:set var="isDisabled" value="%{true}" />
			</s:if>

			<li ${(isDisabled)?'class="disabled"':''}>
		         <a href="#" title="<s:text name="label.next.full"/>">
                    <span class="i fa fa-angle-right"></span>
			    </a>
				<wpsf:submit name="%{#pagerIdMarker + '_' + #group.nextItem}" type="button" title="%{getText('label.next.full')}" disabled="%{isDisabled}" cssClass="hidden">
					<span class="i fa fa-angle-right"></span>
				</wpsf:submit>
			</li>
		
			<s:if test="#group.advanced">
				<li ${(isDisabled)?'class="disabled"':''}>
	                <a href="#" title="<s:text name="label.goToLast"/>">
	                    <span class="i fa fa-angle-double-right"></span>
	                </a>
					<wpsf:submit name="%{#pagerIdMarker + '_' + #group.size}" type="button" disabled="%{isDisabled}" title="%{getText('label.goToLast')}" cssClass="hidden">
						<span class="icon fa fa-angle-double-right"></span>
					</wpsf:submit>
				</li>
			</s:if>
		</ul>
		<s:set var="pagerIdMarker" value="null" />
	</div>
</s:if>

<script type="text/javascript">
	$(function(){
		$(".pagination a").click(function(e) {
			e.preventDefault();
			$(this).closest('li').find("button").click();
		});
		
		$(".pagination-pf-page").on("change", function(){
			var form = $(".pagination-pf-page").closest('form');
			var buttonName = $(this).val();
			$("button[name='"+buttonName+"']").click();
		});
	});
</script>