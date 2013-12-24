<#--
/*
 * 可以展开收缩的下拉框
 */
-->
<div class="input-append">
<#include "/${parameters.templateDir}/simple/select.ftl" />
<span class="add-on" onclick="toggle_multi_select('#${parameters.id?html}',this)"><i class="icon-plus-sign"></i></span>
</div>