<#--
/*
 * 单附件选取输入组件
 */
-->
<div class="input-append">
   <#include "/${parameters.templateDir}/simple/text.ftl" />
   <input type="hidden" name="${parameters.dynamicAttributes["hiddenName"]?html}" id="${parameters.id?html}_hidden" value="${parameters.dynamicAttributes["hiddenValue"]?default("")?html}" />
   <span class="add-on" style="cursor: pointer;"
        onclick="$('#${parameters.id?html}').val('');$('#${parameters.id?html}_hidden').val('');"><i
        class="icon-ban-circle"></i>清除</span> <span class="add-on" style="cursor: pointer;"
        onclick="fileDownload($('#${parameters.id?html}_hidden').val())"><i class="icon-download"></i>下载</span> <span
        class="add-on fileinput-button"> <i class="icon-upload"></i> <span>上传...</span> <input
        id="${parameters.id?html}_file" type="file" name="attachments" multiple>
    </span>
    <script type="text/javascript">
        $(function() {
            $('#${parameters.id?html}_file').fileupload({
                url : "${request.contextPath}/sys/attachment-file!uploadSingle.json",
                dataType : 'json',
                formData : function(form) {
                    return "";
                }
            }).bind('fileuploaddone', function(e, data) {
                var json = data.result;
                $("#${parameters.id?html}").val(json.userdata.fileRealName)
                $("#${parameters.id?html}_hidden").val(json.userdata.id);
            });
        });
    </script>    
</div>