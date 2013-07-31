<#--
/*
 * Autocomplete
 */
-->
<#include "/${parameters.templateDir}/bootstrap/controlheader.ftl" />
<div class="input-append">
<#include "/${parameters.templateDir}/simple/autocompletetext.ftl" />
<span id="text_for_${parameters.id?html}" class="add-on">
<#if parameters.dynamicAttributes["labelValue"]??>${parameters.dynamicAttributes["labelValue"]?html}</#if>
</span>
</div>
<#include "/${parameters.templateDir}/bootstrap/controlfooter.ftl" />