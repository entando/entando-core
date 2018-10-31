<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
    jQuery(function () {
        var treeStyle = '<wp:info key="systemParam" paramName="treeStyle_page" />';

        var isTreeOnRequest = (treeStyle === 'request');

        $('.table-treegrid').treegrid(null, isTreeOnRequest);
        $(".treeRow ").on("click", function (event) {
            $(".treeRow").removeClass("active");
            $(this).find('.subTreeToggler').prop("checked", true);
            $(this).addClass("active");
        });

        $("#expandAll").on("click", function () {
            $('#pageTree .treeRow').removeClass("hidden collapsed");
            $('#pageTree .icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
        });

        $("#collapseAll").on("click", function () {
            $('#pageTree .treeRow:not(:first-child)').addClass('hidden');
            $('#pageTree .treeRow').addClass('collapsed');
            $('#pageTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');
        });

        var selectedNode = $(".table-treegrid .subTreeToggler:checked");
        if (isTreeOnRequest) {
            $(selectedNode).closest(".treeRow").addClass("active").removeClass("hidden");
        } else {
            $(selectedNode).closest(".treeRow").addClass("active").removeClass("hidden").addClass("collapsed");
        }
    });
</script>
