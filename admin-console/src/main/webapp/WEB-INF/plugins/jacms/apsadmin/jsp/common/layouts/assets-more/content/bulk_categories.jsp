<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
<script src="<wp:resourceURL />administration/js/jquery.entando.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        var treeStyle = '<wp:info key="systemParam" paramName="treeStyle_category" />';
        var isTreeOnRequest = (treeStyle === 'request') ? true : false;
        $('.table-treegrid').treegrid(null, isTreeOnRequest);
        $(".treeRow ").on("click", function (event) {
            $(".treeRow").removeClass("active");
            $(this).find('.subTreeToggler').prop("checked", true);
            $(this).addClass("active");
        });

        $("#expandAll").click(function () {
            $('#categoryTree .treeRow').removeClass('hidden');
            $('#categoryTree .treeRow').removeClass('collapsed');
            $('#categoryTree .icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
        });

        $("#collapseAll").click(function () {
            $('#categoryTree .treeRow:not(:first-child)').addClass('hidden');
            $('#categoryTree .treeRow').addClass('collapsed');
            $('#categoryTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');
        });

        var selectedNode = $(".table-treegrid .subTreeToggler:checked");
        if (isTreeOnRequest)
            $(selectedNode).closest(".treeRow").addClass("active").removeClass("hidden");
        else
            $(selectedNode).closest(".treeRow").addClass("active").removeClass("hidden").addClass("collapsed");
    });
</script>