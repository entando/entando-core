<%@ taglib prefix="s" uri="/struts-tags" %>
<script>
$(document).ready(function() {
	$("#expandAll").click(function() {
		$("#pageTree .childrenNodes").removeClass("hidden");
                $("#pageTree .childrenNodes").removeClass("collapsed");                
                $('#pageTree .icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
	});
	$("#collapseAll").click(function() {
		$(".childrenNodes").addClass("hidden");
                $(".childrenNodes").addClass("collapsed");
                $('#pageTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');

	});
	
	$(".treeRow ").on("click", function(event) {
		$(".treeRow").removeClass("active");
		$(".moveButtons").addClass("hidden");
		$(this).find('.subTreeToggler').prop("checked", true);
		$(this).addClass("active");
		$(this).find(".moveButtons").removeClass("hidden");
	});
        
        function buildTree(){
            var isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;
            $('.table-treegrid').treegrid(null, isTreeOnRequest);
        }     
        buildTree();
});
</script>