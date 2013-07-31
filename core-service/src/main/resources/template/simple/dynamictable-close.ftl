</table>
<script type="text/javascript">
$(function() {
    $("#${parameters.id?html}").dynamictable({
<#if parameters.dynamicAttributes["minRows"]??>
        minRows : ${parameters.dynamicAttributes["minRows"]?html},
</#if>        
<#if parameters.dynamicAttributes["maxRows"]??>
        maxRows : ${parameters.dynamicAttributes["maxRows"]?html},
</#if> 
<#if parameters.dynamicAttributes["afterAdd"]??>
        afterAdd : function(row) {
            ${parameters.dynamicAttributes["afterAdd"]?string}
        },
</#if> 
        placeHolder: true 
    });
})
</script>