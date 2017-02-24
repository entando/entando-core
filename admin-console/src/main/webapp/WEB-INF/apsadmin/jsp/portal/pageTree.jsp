<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li><s:text name="title.pageTree" /></li>
</ol>

<style>
    
    .treeInteractionButtons{
        font-size: 16px;
        font-weight: bold;
    }
    .green{
        color: green;
    }
    
   #pageTree .statusField i.fa {
        font-size: 15px;
        margin-top: 6px;
    }
    
    .table-view-pf-actions .btn, .table-view-pf-actions .dropdown-toggle{
        text-align: center;
    }
    </style>

<h1><s:text name="title.pageTree" /></h1>

<div id="main" role="main">
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <s:text name="message.title.ActionErrors" />
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
        <s:form cssClass="action-form">
            <wpsf:submit action="new" type="button" title="%{getText('page.options.new')}" cssClass="btn btn-primary pull-right " data-toggle="tooltip">
                Add page
            </wpsf:submit>
            <br/>
            <br/>
            
            <s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
                
                <div class="table-responsive overflow-visible">
                    <table id="pageTree" class="table table-bordered table-hover table-treegrid">
                        <thead>
                            <tr>
                                <th style="width: 68%;">Tree Pages
                                <s:if test="#pageTreeStyleVar == 'classic'">
                                    <button type="button" class="btn-no-button expand-button" id="expandAll">
                                        <i class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;Expand All
                                    </button>
                                    <button type="button" class="btn-no-button" id="collapseAll">
                                        <i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;Collapse All
                                    </button>
                                </s:if>
                            </th>
                            <th class="text-center" style="width: 8%;">Add&#32;&vert;&#32;Move</th>
                            <th class="text-center" style="width: 8%;">State</th>
                            <th class="text-center" style="width: 8%;">Menu List</th>
                            <th class="text-center" style="width: 5%;">Actions</th>
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
            
            <!--            <fieldset data-toggle="tree-toolbar"><legend><s:text name="title.pageActions" /></legend>
                            <p class="sr-only"><s:text name="title.pageActionsIntro" /></p>
            
                            <div class="btn-toolbar" data-toggle="tree-toolbar-actions">
                                <div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
            <wpsf:submit action="new" type="button" title="%{getText('page.options.new')}" cssClass="btn btn-info" data-toggle="tooltip">
                <span class="icon fa fa-plus-circle"></span>
            </wpsf:submit>
        </div>
    </div>
</fieldset>-->
        </s:form>
    </div>
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
    
</div>
