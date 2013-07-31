<#--
/*
 * Extension for select
 */
-->
<#include "/${parameters.templateDir}/simple/select.ftl" />
<script type="text/javascript">
$(function() {
    $("#${parameters.id?html}").comboselect();
})
</script>
