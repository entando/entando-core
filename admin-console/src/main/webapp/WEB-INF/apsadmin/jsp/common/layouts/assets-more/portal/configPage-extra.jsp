<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/entando.grid-generator.css"/>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/pages/configPage.css"/>

<script src="<wp:resourceURL />administration/js/jquery.xml2json.js"></script>
<script src="<wp:resourceURL />administration/js/lodash.js"></script>
<script src="<wp:resourceURL />administration/js/entando.grid-generator.js"></script>
<script src="<wp:resourceURL />administration/js/jquery-ui-dragndrop.min.js"></script>

<script>
    var PROPERTY = {};
    PROPERTY.code = '<s:property value="pageCode"/>' || '<s:property value="selectedNode"/>';
    PROPERTY.pagemodel = '<s:property value="%{getCurrentPage().getDraftMetadata().getModel().getCode()}"/>';
    PROPERTY.baseUrl = '<wp:info key="systemParam" paramName="applicationBaseURL" />';
    

    var TEXT = {};
    TEXT['error.grid.overlappingFrames'] = '<s:text name="error.grid.overlappingFrames"/>';
    TEXT['error.grid.malformedFrames'] = '<s:text name="error.grid.malformedFrames"/>';
    TEXT['error.grid.gridError'] = '<s:text name="error.grid.gridError"/>';

    var curWidgets = [
    <s:iterator var="curr_widget" value="currentPage.draftWidgets" status="rowstatus">
        {
            index: <s:property value="#rowstatus.index" />,
            widgetType: '<s:property value="%{#curr_widget.getType().getCode()}" />'
        },
    </s:iterator>
        ];
</script>
<script src="<wp:resourceURL />administration/js/pages/configPage.js"></script>

