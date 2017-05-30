<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="<wp:resourceURL />administration/js/entando.alert.js"></script>
<script src="<wp:resourceURL />administration/js/jquery.xml2json.js"></script>
<script src="<wp:resourceURL />administration/js/lodash.js"></script>
<script src="<wp:resourceURL />administration/js/jquery-ui-dragndrop.min.js"></script>
<script src="<wp:resourceURL />administration/js/jquery-confirm.min.js"></script>


<script type="text/javascript">
    $(document).ready(function () {

        // Vars
        var baseUrl = '<wp:info key="systemParam" paramName="applicationBaseURL" />',
            moveTreeURL = baseUrl+'do/rs/Page/moveTree',
            isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;

         // Objects
        var alertService = new EntandoAlert('.alert-container');

        // jQuery selectors
        var $pageTreeRow = $('#pageTree tbody tr');

        $("#expandAll").click(function () {
            $("#pageTree .childrenNodes").removeClass("hidden collapsed");
            $('#pageTree .icon.fa-angle-right').removeClass('fa-angle-right')
                .addClass('fa-angle-down');
        });
        $("#collapseAll").click(function () {
            $("#pageTree .treeRow").addClass("childrenNodes");
            $("#homepage").removeClass("childrenNodes");
            $(".childrenNodes").addClass("hidden collapsed");
            $('#pageTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');
        });

        $(".treeRow ").on("click", function (event) {
            $(".treeRow").removeClass("active");
            $(".moveButtons").addClass("hidden");
            $(this).find('.subTreeToggler').prop("checked", true);
            $(this).addClass("active");
            $(this).find(".moveButtons").removeClass("hidden");
        });

        $('.table-treegrid').treegrid(null, isTreeOnRequest);

        setDroppable($pageTreeRow);
        setDraggable($pageTreeRow);

       /**
        * Set Draggable items 
        */
        function setDraggable(selector) {
            // Set Handle icon for draggable items 
            selector.find("td:first-child").prepend('<span class="fa fa-arrows dragIcon btn-primary"></span>');
            selector.draggable({
                opacity: 0.8,
                axis: "y",
                handle: ".dragIcon",
                helper: function () {
                    return $(this).clone()
                        .addClass("draggableClone active")
                        .css({width:$(this).width()});
                },
                start: function(event, ui) {
                    $(this).find("td:not(:first-child)").addClass("hidden");
                    $("thead th:not(:first-child), thead th:first-child button").addClass("hidden");
                    expandNode(".table-treegrid .childrenNodes");
                },
                stop: function(event, ui) {
                    $(this).find("td:not(:first-child)").removeClass("hidden");
                    $("thead th:not(:first-child), thead th:first-child button").removeClass("hidden");
                }
            });
        }

        /**
         * Set Droppable items 
         */
         function setDroppable(selector) {
            selector.droppable({
                accept: '.treeRow',
                greedy: true,
                drop: function( event, ui ) {
                    moveTree({
                        selectedNode: ui.draggable.attr("id"),
                        parentPageCode: $(this).attr("id")
                    });
                }
             });
        }

        /**
         * Change parentePage for the selectedNode
         */
        function moveTree(data) {
            $.ajax(moveTreeURL, {
                dataType: 'json',
                data: data,
                success: function (response) {
                    
                    if (response.actionErrors.length > 0) {
                        alertService.showResponseAlerts({actionErrors: response.actionErrors});
                    } else {
                        updateTree(data);
                        alertService.addDismissableSuccess(response.actionMessages);
                    }

                    $(".alert-dismissable").fadeOut(8000);
                    $('html, body').animate({ scrollTop: 0 }, 'fast');
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alertService.addDismissableError("<strong>"+textStatus+" "+
                        jqXHR.status+"</strong> "+errorThrown);
                }
            });
        }

        function expandNode (selector) {
            $(selector).removeClass("hidden collapsed")
                .find('.icon.fa-angle-right').removeClass('fa-angle-right')
                .addClass('fa-angle-down');
        }

        /**
         * Refresh treegrid
         */
        function updateTree(treeNodes) {
            var previousParentId = $("#"+treeNodes.selectedNode).clone().data("parent");
            
            $("#"+treeNodes.parentPageCode).after($("#"+treeNodes.selectedNode)
                .attr("data-parent","#"+treeNodes.parentPageCode));

            /* indent selectedNode */
            indentNode(treeNodes.selectedNode, treeNodes.parentPageCode);
            /* update selectedNode subTree */
            sortSubtree(treeNodes.selectedNode);
            
            /* update previous parentNode */
            if($("[data-parent='"+previousParentId+"']").length == 0) {
                $(previousParentId).find(".expand-icon").removeClass("fa-angle-down fa-angle-right");
                $(previousParentId).find(".node-icon")
                  .removeClass("fa-folder")
                  .addClass("fa-folder-o");
            }
            /* update current parentNode */
            if($("[data-parent='#"+treeNodes.parentPageCode+"']").length > 0) {
                $("#"+treeNodes.parentPageCode).find(".expand-icon").addClass("fa-angle-down");
                $("#"+treeNodes.parentPageCode).find(".node-icon")
                  .addClass("fa-folder")
                  .removeClass("fa-folder-o");
            }
        }

        /**
         * Update the treeNode's subTree element position recursively
         */
        function sortSubtree(treeNode) {
            var children = $("[data-parent='#"+treeNode+"']");
            if(children.length > 0) {
                for(var i=0; i < children.length; i++) {
                    $("#"+treeNode).after(children[i]);
                    indentNode($(children[i]).attr("id"),treeNode);
                    sortSubtree($(children[i]).attr("id"));
                }
            }
        }

        /**
         * Update the treeNode's indentation
         */
        function indentNode(treeNode, parentNode) {
            var delta = $("#"+parentNode+" span.indent").length - $("#"+treeNode+" span.indent").length;
            var indentNode = $("#"+treeNode);
            if(delta >= 0) {
                for(var i = 0; i <= Math.abs(delta); i++) {
                    indentNode.find("span.indent").last().after('<span class="indent"></span>');
                }
            } else {
                indentNode.find(".indent:gt("+delta+")").remove();
            }
        }
});
</script>
