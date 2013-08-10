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
        uploadJson : '${request.contextPath}/components/kindeditor/4.1.7/jsp/upload_json.jsp',
        fileManagerJson : '${request.contextPath}/components/kindeditor/4.1.7/jsp/file_manager_json.jsp',
        allowFileManager : false,
        width: '100%',
        minWidth: '200px',
        minHeight: '60px',
<#if parameters.dynamicAttributes["items"]??>
        items : ${parameters.dynamicAttributes["items"]?string},
</#if>        
        afterBlur : function() {
            this.sync();
        }
    },${parameters.options?default("{}")?string}));
})
</script>
