<#--
/*
 * 基于text扩展日期选取组件
 */
-->
<#include "/${parameters.templateDir}/simple/text.ftl" />
<script type="text/javascript">
$(function() {
    $("#${parameters.id?html}").treeinput({
        url: '${request.contextPath}${parameters.dynamicAttributes["treeDataUrl"]?default("false")?html}',
        hiddenName: '${parameters.dynamicAttributes["hiddenName"]?default("")?html}',
        hiddenValue: '${parameters.dynamicAttributes["hiddenValue"]?default("")?html}'
    });
})
</script>