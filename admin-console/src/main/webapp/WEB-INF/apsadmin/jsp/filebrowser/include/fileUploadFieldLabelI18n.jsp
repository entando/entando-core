<script type="text/javascript">
    $(document).ready(function () {
        $('#fields-container').on("change", "input[type=file]", function (e) {
            var name = $(this)[0].files[0].name;
            var id = $(this).attr("id");
            $("#" + id + "_selected").html(name);
        });
        $('input[type=file]').change(function (e) {
            var name = $(this)[0].files[0].name;
            var id = $(this).attr("id");
            $("#" + id + "_selected").html(name);
        });
    });
</script>