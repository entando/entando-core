<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- TODO: inserire in extra-resources --%>
<script src="<wp:resourceURL />administration/js/jquery-dateFormat.min.js"></script>
<script src="<wp:resourceURL />administration/js/patternfly/components/c3/c3.min.js"></script>
<script src="<wp:resourceURL />administration/js/patternfly/components/d3/d3.min.js"></script>

<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate value="${now}" pattern="EEEE, dd MMMM yyyy" var="currentDate"/>

<%-- TODO: inserire in CSS --%>
<style type="text/css">
.page-title-container .date {
    font-size: 14px;
    font-weight: 300;
}
.fs-16 {font-size: 16px}
.ln-16 {font-size: 16px}

.card-pf-body ul {
    line-height: 30px;
    margin-left: auto;
    margin-right: auto;
    padding-top: 30px;
    font-size: 14px;
}

.fs-20 {font-size: 20px!important}

.card-pf-body ul .fa {font-size: 16px;}

.sidebar-pf {height: 100%;}
</style>

<!-- Admin console Breadcrumbs -->
<s:url action="results" namespace="/do/jacms/Content" var="contentListURL"/>
<s:text name="note.goToSomewhere" var="contentListURLTitle"/>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li class="page-title-container"><s:text name="breadcrumb.dashboard" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}" />
<h1 class="page-title-container">
    <s:text name="label.dashboard" />
    <span class="pull-right"> 
        <span class="date mr-20"><s:property value="#attr.currentDate"/></span>
        <a tabindex="0" role="button"
            data-toggle="popover" data-trigger="focus" data-html="true" title=""
            data-content="${dataContent}" data-placement="left"
            data-original-title="${dataOriginalTitle}"> <span
            class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<!-- Default separator -->
<div class="form-group-separator"></div>

<div id="main" role="main" class="mt-20">
    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-8 col-md-9">
                <div class="cards-pf">
                    <div class="container-fluid container-cards-pf no-mt">
                        <div class="row row-cards-pf">

                            <!-- Pages -->
                            <div class="col-xs-12 col-sm-4 col-md-4">
                                <div class="card-pf card-pf-accented card-pf-aggregate-status">
                                    <h2 class="card-pf-title no-mb text-left bold">
                                        <s:text name="dashboard.pageStatus" />
                                    </h2>
                                    <div class="text-left"><span id="lastUpdate-pages"></span></div>
                                    <div class="card-pf-body">
                                            <ul class="mt-10 text-left ml-20">
                                                <li>
                                                    <span class="fa fa-circle green mr-10"></span>
                                                    <span class="card-pf-aggregate-status-count"> 
                                                        <span id="online-pages"></span> <s:text name="label.online" />
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="fa fa-circle gray mr-10"></span>
                                                    <span class="card-pf-aggregate-status-count"> 
                                                        <span id="onlineWithChanges-pages"></span> <s:text name="label.draftnotonline" />
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="fa fa-circle yellow mr-10"></span>
                                                    <span class="card-pf-aggregate-status-count"> 
                                                        <span id="draft-pages"></span> <s:text name="label.draft" />
                                                    </span>
                                                </li>
                                            </ul>
                                            <%--
                                            <div class="text-right mt-40">
                                            </div>
                                             --%>
                                    </div>
                                    <wp:ifauthorized permission="managePages">
                                        <s:url namespace="/do/Page" action="viewTree"
                                            var="pageListURL" />
                                        <a href="${pageListURL}" class="card-pf-link-with-icon display-block text-right" style="padding: 5px" 
                                            title="<s:text name="note.goToSomewhere" />: <s:text name="dashboard.pageList" />">
                                            <s:text name="dashboard.pageList" />
                                        </a>
                                    </wp:ifauthorized>
                                </div>
                            </div>
                            
                            <!-- Contents -->
                            <div class="col-xs-12 col-sm-8 col-md-8">
                                <div class="card-pf card-pf-utilization card-pf-accented card-pf-aggregate-status">
                                    <h2 class="card-pf-title no-mb text-left bold">
                                        <s:text name="dashboard.pageStatus" />
                                    </h2>
                                    <div class="text-left">Last 30 days</div>
                                    <div class="card-pf-body">
                                        <div id="contents-donut-chart"
                                            class="example-donut-chart-right-legend"></div>
                                    </div>
                                    <wp:ifauthorized permission="manageContents">
                                        <s:url action="results" namespace="/do/jacms/Content"
                                            var="contentListURL" />
                                        <a href="${contentListURL}"
                                            class="card-pf-link-with-icon display-block text-right"
                                            style="padding: 5px"
                                            title="<s:text name="note.goToSomewhere" />: <s:text name="dashboard.contentList" />">
                                            <s:text name="dashboard.contentList" />
                                        </a>
                                    </wp:ifauthorized>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Page table -->
                <div class="row">
                    <div class="container-fluid container-cards-pf">
                        <div class="row row-cards-pf">
                            <div class="col-xs-12">
                                <div class="card-pf card-pf-utilization">
                                    <div class="card-pf-heading">
                                        <p class="card-pf-heading-details">
                                            <s:url namespace="/do/Page" action="new" var="newPageURL" />
                                            <s:text name="dashboard.newPage" var="newPageLabel" />
                                            <a href="${newPageURL}" class="btn btn-default"
                                                title="${newPageLabel}"> <s:text
                                                    name="dashboard.newPage" />
                                            </a>
                                        <h2 class="card-pf-title">
                                            <s:text name="dashboard.pageList" />
                                        </h2>
                                    </div>
                                    <div class="card-pf-body">
                                        <table class="table table-striped table-bordered">
                                            <thead>
                                                <tr>
                                                    <th><s:text name="label.description" /></th>
                                                    <th><s:text name="label.status" /></th>
                                                    <th><s:text name="label.lastModified" /></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            <%-- TODO: last page list --%>
                                            </tbody>
                                        </table>
                                    </div>
                                    <wp:ifauthorized permission="managePages">
                                        <s:url namespace="/do/Page" action="viewTree"
                                            var="pageListURL" />
                                        <a href="${pageListURL}" class="card-pf-link-with-icon display-block text-right" style="padding: 5px" 
                                            title="<s:text name="note.goToSomewhere" />: <s:text name="dashboard.pageList" />">
                                            <s:text name="dashboard.pageList" />
                                        </a>
                                    </wp:ifauthorized>
                                </div>

                            </div>
                        </div>
                        <!-- /row -->
                    </div>
                    <!-- /container -->
                </div>

                <!-- Content Table -->
                <div class="row">
                    <div class="container-fluid container-cards-pf">
                        <div class="row row-cards-pf">
                            <div class="col-xs-12">
                                <div class="card-pf card-pf-utilization">
                                    <div class="card-pf-heading">
										<span class="card-pf-heading-details"> <wpsa
											<wpsa:entityTypes entityManagerName="jacmsContentManager" var="contentTypesVar" />
											<span class="btn-group">
									            <button type="button" class="btn btn-default dropdown-toggle"
									                data-toggle="dropdown">
									                <s:text name="dashboard.addContent"/>&#32; 
									                <span class="caret"></span>
                                                </button>
												<ul class="dropdown-menu" role="menu">
													<s:iterator var="contentTypeVar" value="#contentTypesVar">
														<jacmswpsa:contentType
															typeCode="%{#contentTypeVar.typeCode}"
															property="isAuthToEdit" var="isAuthToEditVar" />
														<s:if test="%{#isAuthToEditVar}">
															<li>
																<s:url action="createNew" namespace="/do/jacms/Content" var="newContentURL">
	                                                                <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
	                                                            </s:url>
	                                                            <a href="${newContentURL}" 
	                                                               title="<s:text name="label.new" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" />">
	                                                                <s:text name="label.new" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" />
	                                                            </a>
                                                            </li>
														</s:if>
													</s:iterator>
												</ul>
											</span> 
										</span>
										<h2 class="card-pf-title">
                                            <s:text name="dashboard.contentList" />
                                        </h2>
                                    </div>
                                    <div class="card-pf-body">
                                        <table class="table table-striped table-bordered">
                                            <thead>
                                                <tr>
                                                    <th><s:text name="label.description" /></th>
                                                    <th><s:text name="label.type" /></th>
                                                    <th><s:text name="label.status" /></th>
                                                    <th><s:text name="label.lastModified" /></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            <%-- TODO: last content list --%>
                                            </tbody>
                                        </table>
                                    </div>
                                    <wp:ifauthorized permission="editContents">
                                        <s:url action="results" namespace="/do/jacms/Content"
                                            var="contentListURL" />
                                        <a href="${contentListURL}"
                                            class="card-pf-link-with-icon display-block text-right"
                                            style="padding: 5px"
                                            title="<s:text name="note.goToSomewhere" />: <s:text name="dashboard.contentList" />">
                                            <s:text name="dashboard.contentList" />
                                        </a>
                                    </wp:ifauthorized>
                                </div>

                            </div>
                        </div>
                        <!-- /row -->
                    </div>
                    <!-- /container -->


                </div>

            </div><!-- /col -->
        
        <!-- Right Column -->
			<div class="col-sm-4 col-md-3 sidebar-pf sidebar-pf-right">
				<div class="card-pf card-pf-view card-pf-view-xs mt-20">
					<div class="card-pf-body">
						<div class="card-pf-heading-kebab">
							<h2 class="card-pf-title">
								<s:text name="dashboard.fastSettings"/>
								<span class="fa fs-20 fa-cog pull-right"></span>
							</h2>
						</div>
						<ul>
							<li><s:text name="label.userSettings" /></li>
							<li><a
								href='<s:url action="viewTree" namespace="/do/Category" />'>
									<span class="list-group-item-value"><s:text
											name="label.categories" /></span>
							</a></li>
							<li><a href='<s:url action="list" namespace="/do/Lang" />'>
									<span class="list-group-item-value"> <s:text
											name="label.labels" />&amp;<s:text name="label.languages" />
								</span>
							</a></li>
							<li><a
								href='<s:url action="reloadChoose" namespace="/do/BaseAdmin" />'>
									<span class="list-group-item-value"><s:text
											name="title.reload.config" /></span>
							</a></li>
						</ul>
					</div>
				</div>
                <div class="bottom-actions">
                       <wpsa:entityTypes entityManagerName="jacmsContentManager" var="contentTypesVar" />
                       <div class="dropdown mb-5">
                           <button type="button" class="btn btn-default dropdown-toggle btn-block"
                               data-toggle="dropdown">
                               <s:text name="dashboard.addContent"/>&#32; 
                               <span class="caret"></span>
                           </button>
                           <ul class="dropdown-menu" role="menu">
                               <s:iterator var="contentTypeVar" value="#contentTypesVar">
                                   <jacmswpsa:contentType
                                       typeCode="%{#contentTypeVar.typeCode}"
                                       property="isAuthToEdit" var="isAuthToEditVar" />
                                   <s:if test="%{#isAuthToEditVar}">
                                       <li>
                                           <s:url action="createNew" namespace="/do/jacms/Content" var="newContentURL">
                                               <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
                                           </s:url>
                                           <a href="${newContentURL}" 
                                              title="<s:text name="label.new" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" />">
                                               <s:text name="label.new" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" />
                                           </a>
                                       </li>
                                   </s:if>
                               </s:iterator>
                           </ul>
                       </div>
                       
					   <wp:ifauthorized permission="manageResources">
                       <div class="dropdown mb-5">
                           <button type="button" class="btn btn-default dropdown-toggle btn-block"
                               data-toggle="dropdown">
                               <s:text name="dashboard.addAsset"/>&#32; 
                               <span class="caret"></span>
                           </button>
                           <ul class="dropdown-menu" role="menu">
                               <li>
                                    <s:url namespace="/do/jacms/Resource" action="new" var="newImageURL">
                                        <s:param name="resourceTypeCode" value="'Image'" />
                                        <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                                    </s:url>
                                   <a href="${newImageURL}" 
                                      title="<s:text name="label.new" />&#32;<s:text name="label.image"/>">
                                       <s:text name="label.new" />&#32;<s:text name="label.image"/>
                                   </a>
                               </li>
                               <li>
                                    <s:url namespace="/do/jacms/Resource" action="new" var="newAttachURL">
                                        <s:param name="resourceTypeCode" value="'Attach'" />
                                        <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                                    </s:url>
                                   <a href="${newAttachURL}" 
                                      title="<s:text name="label.new" />&#32;<s:text name="label.attach"/>">
                                       <s:text name="label.new" />&#32;<s:text name="label.attach"/>
                                   </a>
                               </li>
                           </ul>
                       </div>
                       
                        </wp:ifauthorized>
                        
                       <wp:ifauthorized permission="editUsers">
	                       <s:url namespace="/do/User" action="new" var="addUserURL" />
	                       <a href="${addUserURL}" class="btn btn-default btn-block"
	                          title="<s:text name="dashbard.addUser" />">
	                           <s:text name="dashbard.addUser" />
	                       </a>
                       </wp:ifauthorized>
                </div>
			</div>
		</div>
    </div>
</div>

<%-- TODO: inserire in extra-resources --%>
<script type="text/javascript">
$(document).ready(function(){
    
	var action = '<s:url namespace="/do/rs/Page"  action="status" />';
	
	$.ajax({
        url: action,
        cache: false,
        crossoDomain: true,
        complete: function(resp, status) {
            if (status == 'success') {
            	resp = $.parseJSON(resp.responseText);
            	$('#online-pages').html(resp.online);
            	$('#onlineWithChanges-pages').html(resp.onlineWithChanges);
            	$('#draft-pages').html(resp.draft);
            	$('#lastUpdate-pages').html($.format.date(resp.lastUpdate, "dd/MM/yyyy HH:mm:ss"));
            	console.log(resp.lastUpdate);
            	
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            window.location.reload(true);
        },
        processData: false,
        dataType: 'json'
    });	
	

    var c3ChartDefaults = $().c3ChartDefaults();

    var donutData = {
        type : 'donut',
        colors : {
            "Approved (54)" : "#3f9c35",
            "Suspended (250)" : "#f0ab00",
            "ApprovedNotWork (54)" : "#8b8d8f",
        },
        columns : [
                [ 'Approved (54)', 54 ],
                [ 'Suspended (250)', 250 ],
                [ 'ApprovedNotWork (54)', 54 ],
        ],
    };

    // Right Legend
    var donutChartRightConfig = c3ChartDefaults
            .getDefaultDonutConfig();
    donutChartRightConfig.bindto = '#contents-donut-chart';
    donutChartRightConfig.tooltip = {
        show : true
    };
    donutChartRightConfig.data = donutData;
    donutChartRightConfig.legend = {
        show : true,
        position : 'right'
    };
    donutChartRightConfig.size = {
        width : 400,
        height : 161
    };
    donutChartRightConfig.tooltip = {
        contents : $().pfDonutTooltipContents
    };

    var donutChartRightLegend = c3
            .generate(donutChartRightConfig);
    $().pfSetDonutChartTitle(
            "#contents-donut-chart", "358", "Contents");
});
</script>