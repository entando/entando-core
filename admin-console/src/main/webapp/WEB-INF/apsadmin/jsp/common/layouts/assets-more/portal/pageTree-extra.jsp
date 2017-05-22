<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="<wp:resourceURL />administration/js/entando.alert.js"></script>
<script src="<wp:resourceURL />administration/js/jquery.xml2json.js"></script>
<script src="<wp:resourceURL />administration/js/lodash.js"></script>
<script src="<wp:resourceURL />administration/js/jquery-ui-dragndrop.min.js"></script>
<script src="<wp:resourceURL />administration/js/jquery-confirm.min.js"></script>

<style>
    .dragIcon {display: none}
    .draggableClone {
        display: table;
     }
     .draggableClone td:not(:first-child) {display: none}
     .ui-droppable-active td:not(:first-child) {display: none;}
     .ui-droppable-active td {
        width: 100%;
        outline: 1px dashed #bbb;!important;
        outline-offset: -4px;
        padding: 10px!important;
        background: #fafafa; 
     }
     .ui-droppable-active.ui-droppable-hover td {
        transition: 200ms;
        background: #e0e0e0;
        padding-left: 20px!important;
     }
</style>

<script>
    $(document).ready(function () {

        // Vars
        var baseUrl = '<wp:info key="systemParam" paramName="applicationBaseURL" />',
            moveTreeURL = baseUrl+'do/rs/Page/moveTree';

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
            $(".childrenNodes").addClass("hidden");
            $(".childrenNodes").addClass("collapsed");
            $('#pageTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');

        });

        $(".treeRow ").on("click", function (event) {
            $(".treeRow").removeClass("active");
            $(".moveButtons").addClass("hidden");
            $(this).find('.subTreeToggler').prop("checked", true);
            $(this).addClass("active");
            $(this).find(".moveButtons").removeClass("hidden");
        });

        function buildTree() {
            var isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;
            $('.table-treegrid').treegrid(null, isTreeOnRequest);
        }
        buildTree();

        setDroppable($pageTreeRow);
        setDraggable($pageTreeRow);

   /**
    * Set Draggable items 
    */
    function setDraggable(selector) {
    	$(selector).draggable({
            opacity: 0.8,
            axis: "y",
            handle: ".dragIcon",
            helper: function () {
                return $(this).clone()
                    .addClass("draggableClone active")
                    .css({width:$(this).width()});
            },
            start: function(event, ui) {
            	$(this).find("td:not(:first-child)").css({'visibility':'hidden'});
            	$("thead th:not(:first-child)").css({'visibility':'hidden'});
            	expandNode("#pageTree .childrenNodes");
            },
            stop: function(event, ui) {
            	$(this).find("td:not(:first-child)").css({'visibility':'visible'});
            	$("thead th:not(:first-child)").css({'visibility':'visible'});
            }
        });

    	setHandleIcon();
    }

    /**
     * Set Handle icon for draggable items 
     */
    function setHandleIcon () {
        $pageTreeRow.on("mouseover", function(){
            $(this).find(".dragIcon").show();
        });
        $pageTreeRow.on("mouseleave", function(){
            $(this).find(".dragIcon").hide();
        });
        $pageTreeRow.find("td:first-child").prepend('<span class="fa fa-arrows dragIcon"></span>');
    }

    /**
     * Set Droppable items 
     */
     function setDroppable(selector) {
    	 $(selector).droppable({
    		accept: '.treeRow',
    		greedy: true,
    		over: function(event, ui){
//     			expandNode($("[data-parent='#"+$(this).attr("id")+"']"));
    		},
            out: function( event, ui ) {
//                 console.log($(this));
            },
            drop: function( event, ui ) {
            	moveTree({
            		selectedNode: ui.draggable.attr("id"),
            		parentNode: $(this).attr("id")
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
                }
                
                $(".alert-dismissable").fadeOut(8000);
                $('html, body').animate({ scrollTop: 0 }, 'fast');
            }
        });
    }

    function expandNode (selector) {
        $(selector).removeClass("hidden collapsed");
        $('#pageTree .icon.fa-angle-right').removeClass('fa-angle-right')
            .addClass('fa-angle-down');
    }
    
    /**
     * Refresh treegrid PLACEHOLDER
     */
    function updateTree(treeNodes) {
    	var previousParentId = $("#"+treeNodes.selectedNode).data("parent");
    	$("#"+treeNodes.parentNode).after($("#"+treeNodes.selectedNode+", [data-parent='#"+treeNodes.selectedNode+"']"));
    	$("#"+treeNodes.selectedNode).attr("data-parent","#"+treeNodes.parentNode);
    	if($("[data-parent='"+previousParentId+"']").length == 0){
            $(previousParentId).find(".expand-icon").remove();
            $(previousParentId).find(".node-icon")
              .removeClass("fa-folder")
              .addClass("fa-folder-o");
            
        }
    	alertService.addDismissableSuccess('OK');
    }

});
</script>
