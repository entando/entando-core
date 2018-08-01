<script type="text/javascript">
    $(document).ready(function () {

        $('#add-fields').click(function (e) {
            e.preventDefault();
            var numItems = $('.file-description').length;

            var numItems = $('.file-description').length;
            var template = $('#hidden-fields-template').html();

            $('#fields-container').append(template);

            var newId = parseInt(numItems);

            $('#newDescr').attr("name", "descr_" + newId);
            $('#newDescr').attr("id", "descr_" + newId);
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