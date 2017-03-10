<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:set var="currentSize" value="screenSize" />
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/patternfly/css/patternfly.min.css"/>
    <link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/pages/previewPage.css"/>

    <script src="<wp:resourceURL />administration/js/jquery-2.2.4.min.js"></script>
    <script>
        var PROPERTY = {
            baseUrl: '<wp:info key="systemParam" paramName="applicationBaseURL" />',
            lang: '<s:property value="lang" />',
            token: '<s:property value="token" />',
            pageCode: '<s:property value="pageCode" />',
            previewWidth: '<s:property value="#currentSize.width" />',
            previewHeight: '<s:property value="#currentSize.width" />'
        };
    </script>
    <script src="<wp:resourceURL />administration/js/pages/previewPage.js"></script>
</head>
<body>
    <div class="main-container">
        <iframe id="previewFrame">
        </iframe>
    </div>
    <div class="bottom-bar">
        <label for="preview-mode-select"><s:text name="previewPage.previewMode" /></label>
        <select id="preview-mode-select" class="preview-mode-select">
            <option value="desktop"><s:text name="previewPage.option.desktop" /></option>
            <option value="tablet"><s:text name="previewPage.option.tablet" /></option>
            <option value="smartphone"><s:text name="previewPage.option.smartphone" /></option>
            <option value="custom"><s:text name="previewPage.option.custom" /></option>
        </select>

        <div class="custom-panel">
            <input class="custom-width" onkeypress="return event.charCode >= 48 && event.charCode <= 57"/>
            x
            <input class="custom-height" onkeypress="return event.charCode >= 48 && event.charCode <= 57"/>
            <button class="btn custom-size-btn"><s:text name="label.ok" /></button>
        </div>
        
        <label for="preview-mode-lang"><s:text name="previewPage.lang" /></label>
        <s:select  list="langs" id="preview-mode-lang" name="lang" listKey="code" listValue="descr"></s:select>
        
    </div>
</body>
</html>


