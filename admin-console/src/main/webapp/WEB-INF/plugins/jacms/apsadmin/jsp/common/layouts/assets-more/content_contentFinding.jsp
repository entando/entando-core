<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:if test="strutsAction != 2" >
	<script src="<wp:resourceURL />administration/js/generate-code-from-title.js"></script>
</s:if>
<script>
    $(document).ready(function () {
        $("#expandAll").click(function () {
            $(".childrenNodes").removeClass("hidden");
        });
        $("#collapseAll").click(function () {
            $(".childrenNodes").addClass("hidden");
        });
        var isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;
        $('.table-treegrid').treegrid(null, isTreeOnRequest);
        $(".treeRow ").on("click", function (event) {
            $(".treeRow").removeClass("active");
            $(".moveButtons").addClass("hidden");
            $(this).find('.subTreeToggler').prop("checked", true);
            $(this).addClass("active");
            $(this).find(".moveButtons").removeClass("hidden");
        });
	<s:if test="strutsAction != 2" >
		generateCodeFromTitle("lang<wp:info key="defaultLang" />", 'pageCode');
	</s:if>
	});
</script>
