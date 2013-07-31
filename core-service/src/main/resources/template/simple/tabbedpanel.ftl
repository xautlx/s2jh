<div<#rt/>
<#if parameters.id??> id="${parameters.id?html}"<#rt/></#if>
<#if parameters.name??> name="${parameters.name?html}"<#rt/></#if>
<#if parameters.cssClass??> class="${parameters.cssClass?html}"<#rt/></#if>
<#if parameters.cssStyle??> style="${parameters.cssStyle?html}"<#rt/></#if>
<#if parameters.title??> title="${parameters.title?html}"<#rt/></#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />
>