<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<script src="<wp:resourceURL />administration/js/jquery.sticky.js"></script>
<script src="<wp:resourceURL />administration/js/data-tables/jquery.dataTables.min.js"></script>
<script src="<wp:resourceURL />administration/js/data-tables/dataTables.fixedColumns.min.js"></script>
<script src="<wp:resourceURL />administration/js/data-tables/dataTables.colVis.min.js"></script>

<s:if test="strutsAction != 2" >
    <script src="<wp:resourceURL />administration/js/generate-code-from-title.js"></script>
</s:if>

<script type="text/javascript">
    $(document).ready(function () {

        var isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;
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
        $(selectedNode).closest(".treeRow").addClass("active").removeClass("hidden").addClass("collapsed");

    <s:if test="strutsAction != 2" >
        generateCodeFromTitle("lang<wp:info key="defaultLang" />", 'categoryCode');
    </s:if>

        /* DataTables con ColVis e FixeColumns */
        var table = $('#contentListTable').DataTable({
            dom: 'Ct',
            "bSort": false,
            scrollY: false,
            scrollX: true,
            scrollCollapse: true,
            paging: false,
            "colVis": {
                "buttonText": '<s:text name="title.searchResultOptions" />&#32;',
                "sAlign": "right"
            },
            columnDefs: [
                {width: 50, targets: 0},
                {width: 200, "targets": [1, 2, 3, 4, 7, 8, 9], },
                {width: 100, "targets": [5, 6, 10], }

            ],
            fixedColumns: {
                leftColumns: 2,
                rightColumns: 1,
            }
        });

        /* Selezione colonne tabella visibili */
        $(".ColVis_Button").addClass("btn btn-primary dropdown-toggle").click(function () {
            $(this).parent(".ColVis").addClass("btn-group open");
            $(".ColVis_collection label").addClass("checkbox");
            $(".ColVis_collectionBackground, .ColVis_catcher").click(function () {
                $(".ColVis").removeClass("open");
            });
        });


        /* Selezione multipla elementi della tabella */
        var itemsNum = $('.DTFC_LeftBodyLiner .content-list tbody input[type="checkbox"]').length;
        $(".js_selectAll").click(function () {
            toggleSelectAll();
            var isChecked = ($(this).prop("checked") == true);
            if (isChecked) {
                $(".DTFC_LeftBodyLiner .content-list tbody input").prop("checked", true);
            } else {
                $(".DTFC_LeftBodyLiner .content-list tbody input").prop("checked", false);
            }
            updateCounter();
        });
        $('.DTFC_LeftBodyLiner .content-list tbody input[type="checkbox"]').click(function () {
            var selectedItemsNum = updateCounter();
            if (itemsNum == selectedItemsNum) {
                $(".js_selectAll").prop("checked", true);
                $(".selectall-box").removeClass("hidden");
            } else {
                $(".js_selectAll").prop("checked", false);
                $(".selectall-box").addClass("hidden");
                $("#allContentsSelected").bootstrapSwitch("state", "false").val("false");
            }
        });

        /* Sticky toolbar */
        $("#content-list-toolbar").sticky({topSpacing: 80, responsiveWidth: true, zIndex: 9999});

        /* Fix per dropdown in FixedColumns */
        $('.dropdown.dropdown-kebab-pf').on('show.bs.dropdown', function () {
            var dropdownMenu = $(this).parent().find(".dropdown-menu").clone();

            $('body').append(dropdownMenu.addClass("clone"));

            var eOffset = $($(this)).offset();

            dropdownMenu.css({
                'display': 'block',
                'top': eOffset.top + $($(this)).outerHeight(),
            });
        });

        $(window).on('hide.bs.dropdown', function (e) {
            $(".clone").remove();
        });
    });

    function toggleSelectAll() {
        $(".selectall-box").toggleClass("hidden");

        if ($(".selectall-box").hasClass("hidden")) {
            $("#allContentsSelected").bootstrapSwitch("state", "false").val("false");
        }

    }

    function updateCounter() {
        var selectedItemsNum = $('.DTFC_LeftBodyLiner .content-list tbody input[type="checkbox"]:checked').length;
        $(".selected-items-counter").html(selectedItemsNum);

        return selectedItemsNum;
    }
</script>
