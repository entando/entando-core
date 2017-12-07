<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp"/>
<script type="text/javascript">
    var ENTANDO_MODEL_VOCABULARY = {
    <jsp:include page="inc/attributeType-json-Model.jsp" />,
        "#foreach ($item in $<LIST>) $item #end": null,
        "#if (<TRUE>) <DO> #else <DOANOTHER> #end": null,
        "#if (<TRUE>) <DO> #end": null,
        "#set ($<VAR> = <VALUE>)": null
    };
</script>
<link rel="stylesheet" href="<wp:resourceURL />administration/js/codemirror/lib/codemirror.css">
<link rel="stylesheet" href="<wp:resourceURL />administration/js/codemirror/theme/eclipse.css">
<link rel="stylesheet" href="<wp:resourceURL />administration/js/codemirror/addon/hint/show-hint.css">
<%--
<script src="<wp:resourceURL />administration/js/codemirror/lib/codemirror.js"></script>
<script src="<wp:resourceURL />administration/js/codemirror/mode/velocity/velocity.js"></script>
<script src="<wp:resourceURL />administration/js/codemirror/addon/hint/show-hint.js"></script>
<script src="<wp:resourceURL />administration/js/codemirror/addon/hint/entando-hint.js"></script>
--%>
<script src="<wp:resourceURL />administration/js/codemirror/lib/codemirror-compressed.js"></script>
<script src="<wp:resourceURL />administration/js/codemirror/addon/hint/entando-hint-compressed.js"></script>
<script type="text/javascript">
    CodeMirror.commands.autocomplete = function (cm) {
        CodeMirror.showHint(cm);
    }
    jQuery(function () {
        var divContainer = $('<div class="panel panel-default margin-none"></div>');
        var textarea = $('#dataObjectShape');
        divContainer.insertBefore(textarea);
        divContainer.css('width', (textarea.width() + 30) + "px");
        divContainer.css('max-width', (textarea.width() + 30) + "px");

        textarea.css('width', (textarea.width() + 30) + "px");
        textarea.css('max-width', (textarea.width() + 30) + "px");
        textarea.appendTo(divContainer);

        CodeMirror.fromTextArea(document.getElementById('dataObjectShape'), {
            value: document.getElementById('dataObjectShape').value,
            theme: 'eclipse',
            lineNumbers: true,
            tabSize: 2,
            lineWrapping: true,
            gutter: false,
            tabMode: "indent",
            indentUnit: 1,
            mode: "velocity",
            extraKeys: {"Ctrl-Space": "autocomplete"}
        });
    });

    $(document).keyup(function (event) {
        if (event.which === 27) {
            $('#popover-inline-editing-assist').popover('hide');
        }
    });
</script>
