<#--
/*
 * Extension combobox for textfield
 */
-->
<#include "/${parameters.templateDir}/simple/select.ftl" />
<script type="text/javascript">
$(function() {
    $("#${parameters.id?html}").combotext();
})
</script>
