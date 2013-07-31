<#--
/*
 * 基于CKEditor扩展Rich Editor
 */
-->
<#include "/${parameters.templateDir}/simple/textarea.ftl" />
<script type="text/javascript">
$("#${parameters.id?html}").ckeditor(function(element) {
      $(element).val( this.getData());
 });
</script>