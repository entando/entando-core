<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
    jQuery(function(){
		<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
		<s:if test="#pageTreeStyleVar == 'classic'">
		    $('.table-treegrid').treegrid(null, false);
		    $(".treeRow ").on("click", function () {
		        $(".treeRow").removeClass("active");
		        $(this).addClass("active").find('.subTreeToggler').prop("checked", true);
		    });
		    
		    $("#expandAll").on("click", function() {
		        $('#pageTree .treeRow').removeClass("hidden collapsed");
		        $('#pageTree .icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
		    });
		    
		    $("#collapseAll").on("click", function() {
		        $('#pageTree .treeRow:not(:first-child)').addClass('hidden');
		        $('#pageTree .treeRow').addClass('collapsed');
		        $('#pageTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');
		    });
		    
		    $(".table-treegrid .subTreeToggler:checked").closest(".treeRow")
		      .addClass("active").removeClass("hidden").addClass("collapsed");
		</s:if>
    });
</script>
