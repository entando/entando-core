<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:set var="currentSize" value="screenSize" />
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/pages/previewPage.css"/>

    <script src="<wp:resourceURL />administration/js/jquery-2.2.4.min.js"></script>
    <script>
        var PROPERTY = {
            baseUrl: '<wp:info key="systemParam" paramName="applicationBaseURL" />',
            lang: '<s:property value="lang" />',
            pageCode: '<s:property value="pageCode" />',
            previewWidth: '<s:property value="#currentSize.width" />',
            previewHeight: '<s:property value="#currentSize.width" />'
        };
    </script>
    <script src="<wp:resourceURL />administration/js/pages/previewPage.js"></script>
</head>
<body>
    <div class="main-container">
        <iframe id="previewFrame" height="100%" width="<s:property value="#currentSize.width" />px">
        </iframe>
    </div>
    <div class="bottom-bar"></div>
</body>
</html>


