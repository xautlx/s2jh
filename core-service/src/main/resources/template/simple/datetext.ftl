<#--
/*
 * 基于text扩展日期选取组件
 */
-->
<div class="input-append">
<#include "/${parameters.templateDir}/simple/text.ftl" />
<span class="add-on" style="cursor:pointer" id="${parameters.id?html}_datepicker" onclick="$.datepickerClick(this)"><i class="icon-calendar"></i></span>
</div>