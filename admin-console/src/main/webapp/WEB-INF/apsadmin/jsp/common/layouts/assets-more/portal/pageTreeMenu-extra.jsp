<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/entando.grid-generator.css"/>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/pages/pageTreeMenu.css"/>

<script src="<wp:resourceURL />administration/js/entando.alert.js"></script>
<script src="<wp:resourceURL />administration/js/jquery.xml2json.js"></script>
<script src="<wp:resourceURL />administration/js/lodash.js"></script>
<script src="<wp:resourceURL />administration/js/entando.grid-generator.js"></script>
<script src="<wp:resourceURL />administration/js/jquery-ui-dragndrop.min.js"></script>

<script>
    var PROPERTY = {};
    PROPERTY.code = '<s:property value="pageCode"/>' || '<s:property value="selectedNode"/>';
    PROPERTY.pagemodel = '<s:property value="%{getCurrentPage().getDraftMetadata().getModel().getCode()}"/>';
    PROPERTY.baseUrl = '<wp:info key="systemParam" paramName="applicationBaseURL" />';
    PROPERTY.currentLang = '<s:property value="%{currentLang.code}"/>';
    PROPERTY.defaultLang = '<wp:info key="defaultLang" />';

    var PERMISSION = {
        <wp:ifauthorized permission="superuser">superuser: true,</wp:ifauthorized>
    };

    var TEXT = {
        'error.grid.overlappingFrames': '<s:text name="error.grid.overlappingFrames"/>',
        'error.grid.malformedFrames': '<s:text name="error.grid.malformedFrames"/>',
        'error.grid.gridError': '<s:text name="error.grid.gridError"/>',

        'widgetActions.details': '<s:text name="widgetActions.details"/>',
        'widgetActions.settings': '<s:text name="widgetActions.settings"/>',
        'widgetActions.api': '<s:text name="widgetActions.api"/>',
        'widgetActions.newWidget': '<s:text name="widgetActions.newWidget"/>',
        'widgetActions.delete': '<s:text name="widgetActions.delete"/>'
    };

</script>
<script src="<wp:resourceURL />administration/js/pages/pageTreeMenu.js"></script>

