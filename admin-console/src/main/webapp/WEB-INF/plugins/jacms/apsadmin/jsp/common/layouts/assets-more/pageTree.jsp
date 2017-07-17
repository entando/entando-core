<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
    $(document).ready(function(){
	    $('.table-treegrid').treegrid(null, false);
	    $(".treeRow ").on("click", function (event) {
	        $(".treeRow").removeClass("active");
	        $(this).find('.subTreeToggler').prop("checked", true);
	        $(this).addClass("active");
	    });
	    
	    $("#expandAll").click(function() {
	        $('.treeRow').removeClass('hidden');
	        $('.treeRow').removeClass('collapsed');
	        $('.icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
	    });
	    
	    $("#collapseAll").click(function() {
	        $('.treeRow:not(:first-child)').addClass('hidden');
	        $('.treeRow').addClass('collapsed');
	        $('.icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');
	    });
	    
	    var selectedNode = $(".table-treegrid .subTreeToggler:checked");
	    $(selectedNode).closest(".treeRow").addClass("active").removeClass("hidden").addClass("collapsed");
    });
</script>
