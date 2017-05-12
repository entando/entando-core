<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="<wp:resourceURL />administration/js/jquery-dateFormat.min.js"></script>
<script src="<wp:resourceURL />administration/js/patternfly/components/c3/c3.min.js"></script>
<script src="<wp:resourceURL />administration/js/patternfly/components/d3/d3.min.js"></script>

<script type="text/javascript">
    var pageStatusAction = '<s:url namespace="/do/rs/Page"  action="status" />';
    var pageTableAction = '<s:url namespace="/do/rs/Page"  action="lastUpdated" />';
    var currentLang = '<s:property value="currentLang.code"/>';
    var statusMap = {'online':'<s:text name="dashboard.status.online"/>',
    		'onlineNotDraft':'<s:text name="dashboard.status.online"/>&#32;&ne;&#32;'+
    		'<s:text name="dashboard.pageStatus.draft"/>',
    		'draft':'<s:text name="dashboard.status.draft"/>'
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
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                window.location.reload(true);
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
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                window.location.reload(true);
            },
            processData: false,
            dataType: 'json'
        });
        
        // Content status
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
    
        var donutChartRightConfig = c3ChartDefaults.getDefaultDonutConfig();
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
    
        var donutChartRightLegend = c3.generate(donutChartRightConfig);
        $().pfSetDonutChartTitle("#contents-donut-chart", "358", "Contents");
        
        $("#contents-donut-chart .spinner").remove();
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
    
    function getPageStatus(page) {
        
        /*
        online && !changed = online
        online && changed = onlineNotDraft
        !online = draft
        */
        
        var isOnline = page.online;
        var isChanged = page.changed;
        
        if(isOnline && !isChanged) {
            return statusMap['online'];
        }
        if(isOnline && isChanged) {
            return statusMap['onlineNotDraft'];
        }
        if(!isOnline) {
            return statusMap['draft'];
        }
    }
    
</script>