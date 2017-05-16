<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
    jQuery(function(){
		<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
		
		//for content categories
		<s:if test="#categoryTreeStyleVar == 'classic'">
		    $('.table-treegrid').treegrid(null, false);
		    $(".treeRow ").on("click", function (event) {
		        $(".treeRow").removeClass("active");
		        $(this).find('.subTreeToggler').prop("checked", true);
		        $(this).addClass("active");
		    });
		    
		    $("#expandAll").click(function() {
		        $('#pageTree .treeRow').removeClass('hidden');
		        $('#pageTree .treeRow').removeClass('collapsed');
		        $('#pageTree .icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
		    });
		    
		    $("#collapseAll").click(function() {
		        $('#pageTree .treeRow:not(:first-child)').addClass('hidden');
		        $('#pageTree .treeRow').addClass('collapsed');
		        $('#pageTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');
		    });
		    
		    var selectedNode = $(".table-treegrid .subTreeToggler:checked");
		    $(selectedNode).closest(".treeRow").addClass("active").removeClass("hidden").addClass("collapsed");
		</s:if>
    });
</script>