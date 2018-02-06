<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/entando.grid-generator.css"/>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/pages/pageTreeMenu.css"/>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/jquery-confirm.min.css"/>

<script src="<wp:resourceURL />administration/js/entando.alert.js"></script>
<script src="<wp:resourceURL />administration/js/jquery.xml2json.js"></script>
<script src="<wp:resourceURL />administration/js/lodash.js"></script>
<script src="<wp:resourceURL />administration/js/entando.grid-generator.js"></script>
<script src="<wp:resourceURL />administration/js/jquery-ui-dragndrop.min.js"></script>
<script src="<wp:resourceURL />administration/js/jquery-confirm.min.js"></script>

<script>
    var PROPERTY = {};
    PROPERTY.code = '<s:property value="pageCode"/>' || '<s:property value="selectedNode"/>';
    PROPERTY.pagemodel = '<s:property value="%{getCurrentPage().getMetadata().getModel().getCode()}"/>';
    PROPERTY.baseUrl = '<wp:info key="systemParam" paramName="applicationBaseURL" />';
    PROPERTY.currentLang = '<s:property value="%{currentLang.code}"/>';
    PROPERTY.defaultLang = '<wp:info key="defaultLang" />';

    var PERMISSION = {
    <wp:ifauthorized permission="superuser">superuser: true,</wp:ifauthorized>
        };

        var TEXT = {
            'error.grid.overlappingFrames': '<s:text name="error.grid.overlappingFrames"/>',
            'error.grid.malformedFrames': '<s:text name="error.grid.malformedFrames"/>',
            'error.grid.gridError': '<s:text name="error.grid.gridError"/>',
            'widgetActions.details': '<s:text name="widgetActions.details"/>',
            'widgetActions.settings': '<s:text name="widgetActions.settings"/>',
            'widgetActions.api': '<s:text name="widgetActions.api"/>',
            'widgetActions.newWidget': '<s:text name="widgetActions.newWidget"/>',
            'widgetActions.delete': '<s:text name="widgetActions.delete"/>',
            'label.yes': '<s:text name="label.yes"/>',
            'label.no': '<s:text name="label.no"/>'
        };

</script>
<script src="<wp:resourceURL />administration/js/pages/pageTreeMenu.js"></script>
<script>
        $(document).ready(function () {
            // Show/Hide Notifications Drawer
            if ($('#sidebar-page-tree').hasClass('drawer-pf-expanded')) {
                $('.moveButtons-right-container').show();
            } else {
                $('.moveButtons-right-container').hide();
            }

            $('#widget-sidebar-page-tree-btn').click(function () {
                var $drawer = $('#sidebar-page-tree');
                $(this).toggleClass('open');
                if ($drawer.hasClass('hide')) {
                    $drawer.removeClass('hide');
                    setTimeout(function () {
                        if (window.dispatchEvent) {
                            window.dispatchEvent(new Event('resize'));
                        }
                        // Special case for IE
                        if ($(document).fireEvent) {
                            $(document).fireEvent('onresize');
                        }
                    }, 100);
                } else {
                    console.log('animate in');
                    $drawer.addClass('hide');
                }
            });
            $('#close-page-tree-sidebar').click(function () {
                var $drawer = $('.drawer-pf');
                $drawer.addClass('hide');
            });
            $('#toggle-expand').click(function () {
                var $drawer = $('#sidebar-page-tree');
                var $drawerNotifications = $drawer.find('.drawer-pf-notification');

                if ($drawer.hasClass('drawer-pf-expanded')) {
                    $('.moveButtons-right-container').hide();
                    $drawer.removeClass('drawer-pf-expanded');
                    $drawerNotifications.removeClass('expanded-notification');
                } else {
                    $('.moveButtons-right-container').show();
                    $drawer.addClass('drawer-pf-expanded');
                    $drawerNotifications.addClass('expanded-notification');
                }
            });

            // Mark All Read
            $('.panel-collapse').each(function (index, panel) {
                var $panel = $(panel);
                $panel.on('click', '.drawer-pf-action .btn', function () {
                    $panel.find('.unread').removeClass('unread');
                    $(panel.parentElement).find('.panel-counter').text('0 New Events');
                });
            });

            $('#notification-drawer-accordion').initCollapseHeights('.panel-body');

            $("#expandAll").click(function () {
                $(".childrenNodes").removeClass("hidden");
                $(".childrenNodes").removeClass("collapsed");
                $('.icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
            });
            $("#collapseAll").click(function () {
                $("#pageTree .treeRow").addClass("childrenNodes");
                $("#homepage").removeClass("childrenNodes");
                $(".childrenNodes").addClass("hidden");
                $(".childrenNodes").addClass("collapsed");
                $('#pageTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');

            });
            var isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;
            $('.table-treegrid').treegrid(null, isTreeOnRequest);
            $(".treeRow ").on("click", function (event) {
                $(".treeRow").removeClass("active");
                $(".moveButtons-right").addClass("hidden");
                $(".table-view-pf-actions").addClass("hidden");
                $(this).find('.subTreeToggler').prop("checked", true);
                $(this).addClass("active");
                $(this).find(".moveButtons-right").removeClass("hidden");
                $(this).find(".table-view-pf-actions").removeClass("hidden");
            });
            
            // Page title tooltip
            $('[data-toggle="tooltip"]').tooltip();
        });
</script>
