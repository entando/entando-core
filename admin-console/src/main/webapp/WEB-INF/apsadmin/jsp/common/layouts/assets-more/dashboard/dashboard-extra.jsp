<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="<wp:resourceURL />administration/js/jquery-dateFormat.min.js"></script>
<script src="<wp:resourceURL />administration/patternfly/js/components/c3/c3.min.js"></script>
<script src="<wp:resourceURL />administration/patternfly/js/components/d3/d3.min.js"></script>

<script type="text/javascript">
    var pageStatusAction = '<s:url namespace="/do/rs/Page" action="status" />';
    var pageTableAction = '<s:url namespace="/do/rs/Page" action="lastUpdated" />';
    var contentStatusAction = '<s:url namespace="/do/jacms/rs/Content" action="status" />';
    var contentTableAction = '<s:url namespace="/do/jacms/rs/Content" action="lastUpdated" />';
    var currentLang = '<s:property value="currentLang.code"/>';
    var pageStatusMap = {'online': '<span class="fa fa-circle green"></span>',
        'onlineNotDraft': '<span class="fa fa-circle yellow"></span>',
        'draft': '<span class="fa fa-circle gray"></span>'
    	}
    var contentStatusMap = {'online':'<s:text name="dashboard.status.approved"/>',
            'onlineWithChanges':'<s:text name="dashboard.status.approvedWithChanges"/>',
            'draft':'<s:text name="dashboard.status.work"/>'
        }
    
    $(document).ready(function(){

        // Page status
        $.ajax({
            url: pageStatusAction,
            cache: false,
            crossoDomain: true,
            complete: function(resp, status) {
                if (status == 'success') {
                    resp = $.parseJSON(resp.responseText);
                    
                    $('#online-pages').html(resp.online);
                    $('#onlineWithChanges-pages').html(resp.onlineWithChanges);
                    $('#draft-pages').html(resp.draft);
                    $('#lastUpdate-pages').html($.format.date(resp.lastUpdate, "dd/MM/yyyy HH:mm:ss"));
                    
                    $("#page-status .spinner").remove();
                    $("#page-status .hidden").removeClass("hidden");
                    
                    updateSidebarHeight();
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
            	console.log("ERROR");
            },
            processData: false,
            dataType: 'json'
        });

        // Page Table
        $.ajax({
            url: pageTableAction,
            cache: false,
            crossoDomain: true,
            complete: function(resp, status) {
                if (status == 'success') {
                    resp = $.parseJSON(resp.responseText);
                    drawPageTable(resp);
                    
                    $("#page-table .spinner").remove();
                    $("#page-table .hidden").removeClass("hidden");
                    
                    updateSidebarHeight();
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
            	console.log("ERROR");
            },
            processData: false,
            dataType: 'json'
        });

        // Content Table
        $.ajax({
            url: contentTableAction,
            cache: false,
            crossoDomain: true,
            complete: function(resp, status) {
                if (status == 'success') {
                    resp = $.parseJSON(resp.responseText);
                    drawContentTable(resp);
                    
                    $("#content-table .spinner").remove();
                    $("#content-table .hidden").removeClass("hidden");
                    
                    updateSidebarHeight();
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
            	console.log("ERROR");
            },
            processData: false,
            dataType: 'json'
        });
        
        // Content status
        $.ajax({
            url: contentStatusAction,
            cache: false,
            crossoDomain: true,
            complete: function(resp, status) {
                if (status == 'success') {
                    resp = $.parseJSON(resp.responseText);
                    drawContentChart(resp);

                    $("#content-table .spinner").remove();
                    $("#content-table .hidden").removeClass("hidden");
                    
                    updateSidebarHeight();
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("ERROR");
            },
            processData: false,
            dataType: 'json'
        });
        
        $("#contents-donut-chart .spinner").remove();
        
        updateSidebarHeight();
    });
    
    $(window).resize(function () {
        if ($('.sidebar-pf').length > 0) {
        	updateSidebarHeight();
        }
      });

    function drawPageTable(jsonList) {
        
        for(var i=0; i < jsonList.length; i++){
            var metadata = jsonList[i].draftMetadata;
            var page = {
                'descr': metadata.titles[currentLang],
                'status': getPageStatus(jsonList[i]),
                'lastUpdate': $.format.date(metadata.updatedAt, "dd MMMM yyyy"),
            };

            $('#page-table tbody').append('<tr><td>'+page.descr+'</td><td class="text-center">'+
            		page.status+'</td><td class="text-center">'+page.lastUpdate+'</td></tr>');
        }
    }

    function drawContentChart(jsonObject) {
    	
        var online = contentStatusMap.online+" ("+jsonObject.online+")";
        var draft = contentStatusMap.draft+" ("+jsonObject.draft+")";
        var onlineWithChanges = contentStatusMap.onlineWithChanges+" ("+jsonObject.onlineWithChanges+")";
        var total = jsonObject.total;
        var lastUpdate = $.format.date(jsonObject.lastUpdate, "dd/MM/yyyy HH:mm:ss");
        var chartTitle = '<s:text name="dashboard.contents.chartTitle"/>'
        
    	var c3ChartDefaults = $().c3ChartDefaults();
        var donutData = {
            type : 'donut',
            colors : {
            	'online' : "#3f9c35",
            	'draft' : "#8b8d8f",
            	'onlineWithChanges' : "#f0ab00",
            },
            names: {
            	'online':online,
            	'draft':draft,
            	'onlineWithChanges':onlineWithChanges,
            },
            columns : [
                    [ 'online', jsonObject.online ],
                    [ 'draft', jsonObject.draft ],
                    [ 'onlineWithChanges', jsonObject.onlineWithChanges ],
            ],
        };

        var donutChartRightConfig = c3ChartDefaults.getDefaultDonutConfig();
        donutChartRightConfig.bindto = '#contents-donut-chart';
        donutChartRightConfig.tooltip = {
            show : false
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
    
        var donutChartRightLegend = c3.generate(donutChartRightConfig);
        $().pfSetDonutChartTitle("#contents-donut-chart", total, chartTitle);
        
        $("#lastUpdate-contents").html(lastUpdate);
    }
    
    function drawContentTable(jsonList) {
        for(var i=0; i < jsonList.length; i++){
            var content = {
                'description': jsonList[i].description,
            	'author': jsonList[i].author,
                'type':jsonList[i].type,
                'status': getPageStatus(jsonList[i]),
                'lastModified': $.format.date(jsonList[i].lastModified, "dd MMMM yyyy"),
            };

            $('#content-table tbody').append('<tr><td>'+content.description+'</td><td>'+
           		content.author+'</td><td>'+content.type+'</td><td class="text-center">'+
           		content.status+'</td><td class="text-center">'+content.lastModified+'</td></tr>');
        }
    }

    function getContentStatus(content) {
        var isOnline = content.online;
        var isChanged = content.changed;
        
        if(isOnline && !isChanged) {
            return contentStatusMap['online'];
        }
        if(isOnline && isChanged) {
            return contentStatusMap['onlineWithChanges'];
        }
        if(!isOnline) {
            return contentStatusMap['draft'];
        }
    }
    
    function getPageStatus(page) {
        var isOnline = page.online;
        var isChanged = page.changed;
        
        if(isOnline && !isChanged) {
            return pageStatusMap['online'];
        }
        if(isOnline && isChanged) {
            return pageStatusMap['onlineNotDraft'];
        }
        if(!isOnline) {
            return pageStatusMap['draft'];
        }
    }

    function updateSidebarHeight() {
	    $(".sidebar-pf").height($(".main-column").height());
	    $(".bottom-actions").css({"width": "100%", "padding-right":"20px"});
    }
</script>
