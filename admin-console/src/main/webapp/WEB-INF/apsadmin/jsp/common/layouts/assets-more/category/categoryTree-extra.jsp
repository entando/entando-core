<%@ taglib prefix="s" uri="/struts-tags" %>

<script>
	$(document).ready(function() {
		$("#expandAll").click(function() {
			$('#categoryTree .childrenNodes').removeClass('hidden');
            $('#categoryTree .childrenNodes').removeClass('collapsed');                
            $('#categoryTree .icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
		});
		
		$("#collapseAll").click(function() {
			$('#categoryTree .childrenNodes').addClass('hidden');
            $('#categoryTree .childrenNodes').addClass('collapsed');
            $('#categoryTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');
		});
		
		$(".treeRow ").on("click", function(event) {
			$(".treeRow").removeClass("active");
			$(".moveButtons").addClass("hidden");
			$(this).find('.subTreeToggler').prop("checked", true);
			$(this).addClass("active");
		});
	});
</script>