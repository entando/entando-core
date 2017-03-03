<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />

<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/bootstrap-switch.min.css"/>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/pages/settingsPage.css"/>

<script src="<wp:resourceURL />administration/js/bootstrap-switch.min.js"></script>
<script src="<wp:resourceURL />administration/js/pages/settingsPage.js"></script>

<%-- placeholder --%>
<script>
$(document).ready ( function (){  
    $("#expandAll").click(function(){
         $(".childrenNodes").removeClass("hidden");
    });
    $("#collapseAll").click(function(){
         $(".childrenNodes").addClass("hidden");
    });
    var isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;
    $('.table-treegrid').treegrid(null,isTreeOnRequest);
    $(".treeRow ").on("click",function(event){
        $(".treeRow").removeClass("active");
        $(".moveButtons").addClass("hidden");
        $(this).find('.subTreeToggler').prop("checked", true);
        $(this).addClass("active");
        $(this).find(".moveButtons").removeClass("hidden");
    });
    
});
</script>