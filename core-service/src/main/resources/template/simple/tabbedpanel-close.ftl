</div>
<script type="text/javascript">
$(function() {
    $("#${parameters.id?html}").tabs({
        disableItemsExcludeFirst: "${parameters.dynamicAttributes["disableItemsExcludeFirst"]?default("false")?html}" 
    });
});
</script>