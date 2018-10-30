<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<script type="text/javascript">
    $(document).ready(function() {
        var treeStyle = '<wp:info key="systemParam" paramName="treeStyle_category" />';
        var isTreeOnRequest = (treeStyle === 'request') ? true : false;
        $('.table-treegrid').treegrid(null, isTreeOnRequest);
        $("#expandAll").click(function () {
            $("#categoryTree .childrenNodes").removeClass("hidden collapsed");
            $('#categoryTree .icon.fa-angle-right').removeClass('fa-angle-right')
                .addClass('fa-angle-down');
        });
        $("#collapseAll").click(function () {
            $("#categoryTree .treeRow").addClass("childrenNodes");
            $("#home").removeClass("childrenNodes");
            $(".childrenNodes").addClass("hidden collapsed");
            $('#categoryTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');
        });

        $(".treeRow ").on("click", function (event) {
            $(".treeRow").removeClass("active");
            $(this).find('.subTreeToggler').prop("checked", true);
            $(this).addClass("active");
        });

    });
</script>
