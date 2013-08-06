<#--
/*
 * 基于KindEditor封装标签
 */
-->
<#include "/${parameters.templateDir}/simple/textarea.ftl" />
<script type="text/javascript">
$(function() {
    KindEditor.create('#${parameters.id?html}', $.extend({
        uploadJson : '${request.contextPath}/components/kindeditor/4.1.7/jsp/upload_json.jsp',
        fileManagerJson : '${request.contextPath}/components/kindeditor/4.1.7/jsp/file_manager_json.jsp',
        allowFileManager : false
    },${parameters.options?default("{}")?string}));
})
</script>