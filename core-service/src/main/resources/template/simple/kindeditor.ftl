<#--
/*
 * 基于KindEditor封装标签
 */
-->
<#include "/${parameters.templateDir}/simple/textarea.ftl" />
<script type="text/javascript">
$(function() {
    $('#${parameters.id?html}').attr("kindeditor","KindEditor_${parameters.id?html}");
    var KindEditor_${parameters.id?html} = KindEditor.create('#${parameters.id?html}', $.extend({
<#if parameters.dynamicAttributes["uploadJson"]??>    
        uploadJson : '${request.contextPath}${parameters.dynamicAttributes["uploadJson"]?string}',
</#if>         
        allowFileManager : false,
        imageSizeLimit: '${parameters.dynamicAttributes["imageSizeLimit"]?string}',
        width: '100%',
        minWidth: '200px',
        minHeight: '60px',
<#if parameters.dynamicAttributes["items"]??>
        items : ${parameters.dynamicAttributes["items"]?string},
</#if>   
<#if parameters.dynamicAttributes["height"]??>
        height : '${parameters.dynamicAttributes["height"]?string}',
<#else>
        height : $('#${parameters.id?html}').parents("div.ui-tabs-panel").first().height()-95,
</#if>       
        afterBlur : function() {
            this.sync();
        }
    },${parameters.options?default("{}")?string}));
})
</script>
