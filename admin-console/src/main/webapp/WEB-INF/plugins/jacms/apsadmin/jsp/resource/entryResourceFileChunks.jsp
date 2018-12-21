<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>



<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.app"/>
    </li>
    <li>
        <s:text name="breadcrumb.jacms"/>
    </li>

    <s:if test="onEditContent">
        <li>
            <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
                <s:text name="breadcrumb.jacms.content.list"/>
            </a>
        </li>
        <li>
            <a href="<s:url action="backToEntryContent" ><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                <s:text name="breadcrumb.jacms.content.edit"/>
            </a>
        </li>
    </s:if>
    <s:else>
        <li>
            <s:text name="breadcrumb.digitalAsset"/>
        </li>
    </s:else>
    <li>
        <s:if test="onEditContent">
            <a href="<s:url action="findResource"><s:param name="resourceTypeCode" value="resourceTypeCode" /><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}"/>
            </a>
        </s:if>
        <s:else>
            <a href="<s:url action="list"><s:param name="resourceTypeCode" value="resourceTypeCode" /></s:url>">
                <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}"/>
            </a>
        </s:else>
    </li>
    <li class="page-title-container">
        <s:if test="getStrutsAction() == 1">
            <s:text name="title.%{resourceTypeCode}.new"/>
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
            <s:text name="title.%{resourceTypeCode}.edit"/>
        </s:elseif>
    </li>
</ol>
<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="title.%{resourceTypeCode}.new"/>
    </s:if>
    <s:elseif test="getStrutsAction() == 2">
        <s:text name="title.%{resourceTypeCode}.edit"/>
    </s:elseif>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-original-title=""
           data-content="<s:property value="%{getText('help.' + resourceTypeCode + '.' + strutsAction + '.info')}" escapeXml="true" />"
           data-placement="left">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>
<i class="fa fa-asterisk required-icon"></i>
<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields"/>
    </div>
</div>
<br/>
<div class="row">
    <div class="col-xs-12">
        <s:form id="uploadForm" enctype="multipart/form-data" action="save" cssClass="form-horizontal image-upload-form">
            <s:file name="fileUpload" id="file_input" label="label.file" />
            <button id="delete_file">Delete File</button>
        </s:form>

    </div>

</div>




<script>
    (function () {
        var action = document.getElementById("uploadForm").action;
        var uploadId = uuidv4();
        var fileInput = document.getElementById('file_input');

        if (fileInput.files.length)
            processFile();
        fileInput.addEventListener('change', processFile, false);

        function processFile(e) {
            var file = fileInput.files[0];
            var size = file.size;
            var sliceSize = 1048576;
            var start = 0;

            setTimeout(loop, 1);

            function loop() {
                var end = start + sliceSize;
                if (size - end < 0) {
                    end = size;
                }
                var s = slice(file, start, end);
                send(s, start, end, size, fileInput.files[0].name);
                if (end < size) {
                    start += sliceSize;
                    setTimeout(loop, 200);
                }
            }
        }

        function send(piece, start, end, size, fileName) {
            var formdata = new FormData();
            var xhr = new XMLHttpRequest();
            xhr.open('POST', action, true);
            formdata.append('start', start);
            formdata.append('end', end);
            formdata.append('fileUpload', piece);
            formdata.append('descr', 'Test description for chunks upload');
            formdata.append('uploadId', uploadId);
            formdata.append('fileSize', size);
            formdata.append('fileName', fileName);
            xhr.send(formdata);
        }

        /**
         * Formalize file.slice
         */
        function slice(file, start, end) {
            var slice = file.mozSlice ? file.mozSlice :
                file.webkitSlice ? file.webkitSlice :
                    file.slice ? file.slice : noop;
            return slice.bind(file)(start, end);
        }

        function noop() {
        }


        var deleteButton = document.getElementById('delete_file');
        deleteButton.addEventListener('click', deleteFile, false);

        function deleteFile(e) {
            e.preventDefault();
            var deleteAction = "stopUploadAndDelete.action";
            var fileId = '7038df88-4ca7-499c-b1b3-0b8ef06f2fcc';
            var formdata = new FormData();
            var xhr = new XMLHttpRequest();
            xhr.open('POST', deleteAction, true);
            formdata.append('fileID', fileId);
            xhr.send(formdata);
        }


        /*
         * Generating unique IDs for each upload
         * This function is an attempt to implement RFC-4222 https://www.ietf.org/rfc/rfc4122.txt
         * It was found in this SO answer: https://stackoverflow.com/a/2117523/2267244
         *
         */
        function uuidv4() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        }
    })();
</script>