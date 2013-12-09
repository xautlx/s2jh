<#--
/*
 * Autocomplete
 */
-->
<#include "/${parameters.templateDir}/simple/text.ftl" />
<script type="text/javascript">
$("#${parameters.id?html}").autocomplete({
<#if parameters.dynamicAttributes["minLength"]??>
        minLength : ${parameters.dynamicAttributes["minLength"]?html},
</#if>
<#if parameters.dynamicAttributes["delay"]??>
        delay : ${parameters.dynamicAttributes["delay"]?html},
</#if>
<#if parameters.dynamicAttributes["hiddenName"]??>
        hiddenName : "${parameters.dynamicAttributes["hiddenName"]?html}",
</#if>
    source: "${request.contextPath}${parameters.dynamicAttributes["source"]}?string",
    select: function( event, ui ) {
        $("#text_for_${parameters.id?html}").html(ui.item.label);
<#if parameters.dynamicAttributes["select"]??>
      select:  ${parameters.dynamicAttributes["select"]?string},
</#if>
        return false;
    }
});
</script>