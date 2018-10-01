<script type="text/javascript">
    $(document).ready(function () {

        $('#add-fields').click(function (e) {
            e.preventDefault();
            var numItems = $('.file').length;

            var numItems = $('.file').length;
            var template = $('#hidden-fields-template').html();

            $('#fields-container').append(template);

            var newId = parseInt(numItems);

            $('#newFileUpload_label').attr("for", "fileUpload_" + newId);
            $('#newFileUpload_label').attr("id", "fileUpload_label_" + newId);

            $('#newFileUpload_selected').attr("id", "fileUpload_" + newId + "_selected");
            $('#newFileUpload').attr("id", "fileUpload_" + newId);

        });
        $('.delete-fields').click(function (e) {
            e.preventDefault();
            $(this).parent('div').remove();
        });

        $('#fields-container').on("click", ".delete-fields", function (e) {
            e.preventDefault();
            $(this).parent('div').remove();
        })
    });
</script>