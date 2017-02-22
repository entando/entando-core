    <%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<style>
    .grid-preview {
        padding: 0 20px;
        border: 1px solid #bbb;
        border-radius: 1px;
    }
    .empty-slot  {
        background: lightgray;
    }
    .grid-slot {
        border: 1px dashed gray;
        background: #fff;
    }
</style>

<script src="<wp:resourceURL />administration/js/jquery.xml2json.js"></script>
<script src="<wp:resourceURL />administration/js/lodash.js"></script>
<script src="<wp:resourceURL />administration/js/entando.grid-generator.js"></script>

<script>
$(function() {

    var match = window.location.href.match(/(^.+\/do\/)/ ),
    baseUrl = match ? match[0] : window.location.protocol + '//' + window.location.host,
    serviceUrl = baseUrl + 'rs/PageModel/frames?code=service',
    updateUrl = baseUrl + 'rs/PageModel/updateSketch?code=service';


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


    function updateGridPreview(data, skipPost) {

        try {
            var gen = new GridGenerator(data);

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
                    alertText = 'Some frames are overlapping (' +
                    e.data[0].a.description + ', ' +
                    e.data[0].b.description +
                    ')';
                    break;
                case GridGenerator.ERROR.MALFORMED_FRAMES:
                    alertText = 'Malformed frames (' + e.data[0].description + ')';
                    break;
                default:
                    alertText = 'An error occurred while rendering the grid';
                    break;

            }

            showGridWarning(alertText);
        }

    }

        function xmlToJson() {
            var xmlJson = $.xml2json($('#xmlConfiguration').val()),
                xmlFrames = _.isArray(xmlJson.frames.frame) ? xmlJson.frames.frame : [xmlJson.frames.frame];
            var data = _.map(xmlFrames, function(frame) {
                var res = _.clone(frame.$) || {};
                res.description = frame.descr;
                res.sketch = _.get(frame, 'sketch.$', null);
                _.forEach(res.sketch, function(v, k) {
                    res.sketch[k] = parseInt(v, 10);
                });
                return res;
            });
            return data;
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



});//domready
</script>