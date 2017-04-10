
function registerAceTextarea(fieldId, theme, mode) {
	var editor = ace.edit('ace_' + fieldId);
	var textarea = $('#' + fieldId);
	textarea.hide();
	editor.getSession().setValue(textarea.val());
	editor.setTheme(theme);
	editor.getSession().setMode(mode);
	editor.getSession().on('change', function(){
		textarea.val(editor.getSession().getValue());
		textarea.trigger("change");
	});
}

$(function() {

    var match = window.location.href.match(/(^.+\/do\/)/ ),
        baseUrl = match ? match[0] : window.location.protocol + '//' + window.location.host,
        serviceUrl = baseUrl + 'rs/PageModel/frames?code=' + PROPERTY.code,
        updateUrl = baseUrl + 'rs/PageModel/updateSketch?code=' + PROPERTY.code;


    /**
     * Calls a POST to update the frames, if success updates the xml
     */
    function postUpdatedFrames(frames) {
        $.ajax(updateUrl, {
            method: 'POST',
            contentType : 'application/json',
            data: JSON.stringify(frames),
            success: function(data) {
                $('#xmlConfiguration').val(data);
            }
        });
    }


    function showGridWarning(alertText) {
        var alert = '<div class="alert alert-warning">' +
            '<span class="pficon pficon-warning-triangle-o"></span>' +
            '<strong>' + alertText + '</strong>' +
            '</div>';
        $('.grid-container').html(alert);
    }

    function getMessageText(key, args) {
        var msg = TEXT[key] || '';
        if (_.isArray(args)) {
            for (var i=0; i<args.length; ++i) {
                msg = msg.replace('{' + i + '}', args[i]);
            }
        }
        return msg;
    }

    function updateGridPreview(data, skipPost) {

        try {
            var gen = new GridGenerator({
                frames: data,
                rowHeight: 80
            });

            if (!skipPost) {
                postUpdatedFrames(gen.getUpdatedFrames());
            }

            var gridHtml = '<div class="grid-preview">' + gen.getHtml() + '</div>';
            $('.grid-container').html(gridHtml);
        } catch (e) {
            console.error(e, e.data);
            var alertText;
            switch (e.type) {
                case GridGenerator.ERROR.OVERLAPPING_FRAMES:
                    alertText = getMessageText('error.grid.overlappingFrames',
                        [e.data[0].a.description, e.data[0].b.description]);
                    break;
                case GridGenerator.ERROR.MALFORMED_FRAMES:
                    alertText = getMessageText('error.grid.malformedFrames', [e.data[0].description]);
                    break;
                default:
                    alertText = getMessageText('error.grid.gridError');
                    break;

            }

            showGridWarning(alertText);
        }

    }

    function xmlToJson() {
        var xmlJson = $.xml2json($('#xmlConfiguration').val()),
            xmlFrames = _.isArray(xmlJson.frames.frame) ? xmlJson.frames.frame : [xmlJson.frames.frame];
        return _.map(xmlFrames, function(frame) {
            var res = _.clone(frame.$) || {};
            res.description = frame.descr;
            res.sketch = _.get(frame, 'sketch.$', null);
            _.forEach(res.sketch, function(v, k) {
                res.sketch[k] = parseInt(v, 10);
            });
            return res;
        });
    }


    $('#xmlConfiguration').on('input propertychange change keyup paste', function() {
        try {
            var data = xmlToJson();
            updateGridPreview(data, true);
        } catch (e) {
            // malformed XML, fail silently
        }
    });


    // Initializes the
    $.ajax(serviceUrl, {
        method: 'GET',
        success: function(data) {
            updateGridPreview(data, false);
        },
        error: function() {
            showGridWarning('Error getting template data.');
        }
    });

	// ACE EDITOR
    registerAceTextarea('xmlConfiguration', 'ace/theme/chrome', 'ace/mode/xml');
    registerAceTextarea('template', 'ace/theme/chrome', 'ace/mode/jsp');

});//domready