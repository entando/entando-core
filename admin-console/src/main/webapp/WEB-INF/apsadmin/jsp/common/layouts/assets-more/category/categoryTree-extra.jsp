<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
    $(document).ready(function() {
        var isTreeOnRequest = <s:property value="#categoryTreeStyleVar == 'request'"/>;

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

        $('.table-treegrid').treegrid(null, isTreeOnRequest);
    });
</script>
