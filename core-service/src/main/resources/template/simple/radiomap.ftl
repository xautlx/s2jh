<#--
/*
 * Change according to Bootstrap
 */
-->
<@s.iterator value="parameters.list">
    <#if parameters.listKey??>
        <#assign itemKey = stack.findValue(parameters.listKey)/>
    <#else>
        <#assign itemKey = stack.findValue('top')/>
    </#if>
    <#assign itemKeyStr = itemKey.toString() />
    <#if parameters.listValue??>
        <#assign itemValue = stack.findString(parameters.listValue)/>
    <#else>
        <#assign itemValue = stack.findString('top')/>
    </#if>
    <#if parameters.listCssClass??>
        <#if stack.findString(parameters.listCssClass)??>
          <#assign itemCssClass= stack.findString(parameters.listCssClass)/>
        <#else>
          <#assign itemCssClass = ''/>
        </#if>
    </#if>
    <#if parameters.listCssStyle??>
        <#if stack.findString(parameters.listCssStyle)??>
          <#assign itemCssStyle= stack.findString(parameters.listCssStyle)/>
        <#else>
          <#assign itemCssStyle = ''/>
        </#if>
    </#if>
    <#if parameters.listTitle??>
        <#if stack.findString(parameters.listTitle)??>
          <#assign itemTitle= stack.findString(parameters.listTitle)/>
        <#else>
          <#assign itemTitle = ''/>
        </#if>
    </#if>
<label class="radio inline"><#rt/>
<input type="radio"<#rt/>
<#if parameters.name??>
 name="${parameters.name?html}"<#rt/>
</#if>
 id="${parameters.id?html}${itemKeyStr?html}"<#rt/>
<#if tag.contains(parameters.nameValue?default(''), itemKeyStr)>
 checked="checked"<#rt/>
</#if>
<#if itemKey??>
 value="${itemKeyStr?html}"<#rt/>
</#if>
<#if parameters.disabled?default(false)>
 disabled="disabled"<#rt/>
</#if>
<#if parameters.required?default(false)>
 required="true"<#rt/>
</#if>
<#if parameters.tabindex??>
 tabindex="${parameters.tabindex?html}"<#rt/>
</#if>
<#if itemCssClass?if_exists != "">
 class="${itemCssClass?html}"<#rt/>
<#else>
    <#if parameters.cssClass??>
 class="${parameters.cssClass?html}"<#rt/>
    </#if>
</#if>
<#if itemCssStyle?if_exists != "">
 style="${itemCssStyle?html}"<#rt/>
<#else>
    <#if parameters.cssStyle??>
 style="${parameters.cssStyle?html}"<#rt/>
    </#if>
</#if>
<#if itemTitle?if_exists != "">
 title="${itemTitle?html}"<#rt/>
<#else>
    <#if parameters.title??>
 title="${parameters.title?html}"<#rt/>
    </#if>
</#if>
<#include "/${parameters.templateDir}/simple/css.ftl" />
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />
/><#rt/>
    ${itemValue}<#t/>
</label>
</@s.iterator>