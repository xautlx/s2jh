<#include "/${parameters.templateDir}/bootstrap/controlheader.ftl" />
        <#include "/${parameters.templateDir}/simple/text.ftl" />
        <span id="aclCode_text_${parameters.id?html}"></span>
        <script type="text/javascript">
            $.getJSON("${request.contextPath}/pub/acl-code!label?value=${parameters.nameValue?html}", function(data) {
                $("#aclCode_text_${parameters.id?html}").html(data);
            });

            $("#${parameters.id?html}").autocomplete({
                minLength : 2,
                source : "${request.contextPath}/auth/user!aclCodes.json",
                select : function(event, ui) {
                    $("#aclCode_text_${parameters.id?html}").html(ui.item.label);
                    $("#${parameters.id?html}").val(ui.item.value);
                    return false;
                }
            }).data("ui-autocomplete")._renderItem = function(ul, item) {
                return $("<li>").append("<a><b>" + item.value + "</b> " + item.label + "</a>").appendTo(ul);
            };

        </script>
<#include "/${parameters.templateDir}/bootstrap/controlfooter.ftl" />