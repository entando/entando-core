<%@ taglib prefix="s" uri="/struts-tags" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
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