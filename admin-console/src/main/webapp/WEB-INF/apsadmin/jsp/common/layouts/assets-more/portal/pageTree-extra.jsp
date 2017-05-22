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
     .ui-droppable-active {
        background-color: #ccc!important;}
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
            containment: $("#pageTree"),
            start: function(event, ui) {
            	setDroppable($(selector).not($(this)));
            }
        });

    	setHandleIcon ();
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
    		over: function(event, ui){
//     			expandNode($("[data-parent='#"+$(this).attr("id")+"']"));
    		},
            out: function( event, ui ) {
//                 console.log($(this));
            },
            drop: function( event, ui ) {
            	console.log();
            	console.log();
            	moveTree({
            		selectedNode: ui.draggable.attr("id"),
            		parentNode: $(this).attr("id")
            	});
            }
    	 });
    	return;
    }

    /**
     * Change parentePage for the selectedNode
     */
    function moveTree(data) {
    	 console.log(data);
        $.ajax(moveTreeURL, {
            dataType: 'json',
            data: data,
            success: function (response) {
                if (alertService.showResponseAlerts(response)) {
                    return;
                }
                updateTree();
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
    function updateTree() {
    	console.log("tree refreshed");
    	return;
    }

});
</script>
