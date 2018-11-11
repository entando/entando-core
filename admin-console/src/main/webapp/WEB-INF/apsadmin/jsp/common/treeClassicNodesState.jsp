<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- We need to keep track of the tree state also using classic node settings.
  -- This requires some additional client side logic: it is necessary to add
  -- an hidden input field binded to the treeNodesToOpen variable to all opened
  -- nodes and remove it from all closed nodes.
  -- IMPORTANT: Before including this file set the treeIdVar --%>

<script>
    $(document).ready(function () {

        var treeId = '<s:property value="%{treeIdVar}" />';

        function fillTreeNodesToLoadHiddenInputs() {
            $('#' + treeId + ' .treeRow').each(function (index, element) {
                var $tr = $(element);
                var $td = $tr.find('td:first-child');
                if ($tr.hasClass('hidden') || $tr.hasClass('collapsed')) {
                    $td.find('input[name="treeNodesToOpen"]').remove();
                } else if ($td.find('input[name="treeNodesToOpen"]').length === 0) {
                    $td.append('<input type="hidden" name="treeNodesToOpen" value="' + $tr.attr('id') + '" />');
                }
            });
        }

        $(document).on('click', '#' + treeId + ' tr', fillTreeNodesToLoadHiddenInputs);
        $(document).on('#expandAll', fillTreeNodesToLoadHiddenInputs);
        $(document).on('#collapseAll', fillTreeNodesToLoadHiddenInputs);

        fillTreeNodesToLoadHiddenInputs();
    });
</script>