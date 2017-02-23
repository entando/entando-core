<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="title.pageManagement" /></span></h1>

<style>
    .moveButtons{
            font-size: 16px;
    }
    
    </style>
<div id="main" role="main">
    
    <p><s:text name="note.pageTree.intro" /></p>
    
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul>
                <s:iterator value="actionErrors">
                    <li><s:property escape="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    
    <div role="search">
        <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageSearchForm.jsp" />
        
        <hr />
        
        <%--
        <h2 class="margin-base-vertical"><s:text name="title.pageTree" /></h2>
        --%>
        
        <s:form cssClass="action-form">
            
            <s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
                
                <div class="table-responsive overflow-visible">
                    <table id="pageTree" class="table table-bordered table-hover table-treegrid">
                        <thead>
                            <tr>
                                <th style="width: 68%;">Tree Pages</th>
                                <th style="width: 8%;">Add Move</th>
                                <th style="width: 8%;">State</th>
                                <th style="width: 8%;">Menu List</th>
                                <th style="width: 8%;">Actions</th>
                            </tr>
                        </thead>
                        <tbody>  
                        <s:set var="inputFieldName" value="%{'selectedNode'}" />
                        <s:set var="selectedTreeNode" value="%{selectedNode}" />
                        <s:set var="liClassName" value="'page'" />
                        <s:set var="treeItemIconName" value="'fa-folder'" />
                        <s:if test="#pageTreeStyleVar == 'classic'">
                            <s:set var="currentRoot" value="allowedTreeRootNode" />
                            <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilderPages.jsp" />
                        </s:if>
                        <s:elseif test="#pageTreeStyleVar == 'request'">
                        <style>
                            .table-treegrid span.collapse-icon, .table-treegrid span.expand-icon {
                                cursor: pointer;
                                display: none;
                            }
                        </style>
                        <s:set var="currentRoot" value="showableTree" />
                        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-linksPages.jsp" />
                    </s:elseif>
                    </tbody>
                </table>     
                
            </div>
            <p class="sr-only"><wpsf:hidden name="copyingPageCode" /></p>
            
            <fieldset data-toggle="tree-toolbar"><legend><s:text name="title.pageActions" /></legend>
                <p class="sr-only"><s:text name="title.pageActionsIntro" /></p>
                
                <div class="btn-toolbar" data-toggle="tree-toolbar-actions">
                    <div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
                        <wpsf:submit action="new" type="button" title="%{getText('page.options.new')}" cssClass="btn btn-info" data-toggle="tooltip">
                            <span class="icon fa fa-plus-circle"></span>
                        </wpsf:submit>
                    </div>
                </div>
            </fieldset>
            
        </s:form>
    </div>
    
    <script>

        $(document).ready ( function (){      
            $('.table-treegrid').treegrid();
            var isTreeOnRequest = <s:property value="#pageTreeStyleVar == 'request'"/>;
    
            console.log("è su richiesta?"+isTreeOnRequest);        
            $(".treeRow ").on("click",function(event){
                if(isTreeOnRequest){
                    var isAction = $(event.target).hasClass('treeOpenCloseJS');
                    if(!isAction){
                        console.log("NON è azione");        
                        event.preventDefault();
                    }else{
                        console.log("è azione");        
                    }
                }
                $(".treeRow").removeClass("active");
                $(".moveButtons").addClass("hidden");
                $(this).find('.subTreeToggler').prop("checked", true);
                $(this).addClass("active");
                $(this).find(".moveButtons").removeClass("hidden");
            });
        });
    
    </script> 
    
</div>
