<#include "/${parameters.templateDir}/bootstrap/controlheader.ftl" />
        <div class="input-append">
            <#include "/${parameters.templateDir}/simple/text.ftl" />
            <span class="add-on" style="cursor: pointer" id="btnSelect_${parameters.id?html}">
            <i class="icon-list"></i></span><span id="text_for_${parameters.id?html}" class="add-on"></span>
        </div>
        <script type="text/javascript">
            function callback${parameters.id?html}(json) {
                $("#${parameters.id?html}").val(json.xxdm);
                var link = '<a href="javascript:void(0)" title="查看学校信息" onclick="$.popupViewDialog(\'${request.contextPath}/biz/xx/xx-jcxx!viewTabs?xxdm=' + json.xxdm + '\')">' + json.xxmc + '</a>';
                $("#text_for_${parameters.id?html}").html(link);
            }
            $().ready(function() {
                $("#btnSelect_${parameters.id?html}").click(function() {
                    $.popupDialog({
                        dialogId : 'xxSelectionDialog',
                        url : "${request.contextPath}/biz/xx/xx-jcxx!forward?_to_=selection&callback=callback${parameters.id?html}",
                        title : '选择...',
                        width : 850,
                        height : 400
                    })
                });

                $("#${parameters.id?html}").autocomplete({
                    minLength : 4,
                    source : "${request.contextPath}/biz/xx/xx-jcxx!autocomplete",
                    select : function(event, ui) {
                        callback${parameters.id?html}(ui.item);
                        return false;
                    }
                }).data("ui-autocomplete")._renderItem = function(ul, item) {
                    return $("<li>").append("<a><b>" + item.xxdm + "</b> " + item.xxmc + "</a>").appendTo(ul);
                };

                $("#${parameters.id?html}").rules("add", {
                    remote : {
                        url : "${request.contextPath}/biz/xx/xx-jcxx!validXxdm",
                        data : {
                            xxdm : function() {
                                return $("#${parameters.id?html}").val();
                            }
                        }
                    }
                });
                
                var term=$("#${parameters.id?html}").val();
                if(term!=''){
                    $.getJSON("${request.contextPath}/biz/xx/xx-jcxx!autocomplete?term="+term, function(data){
                      callback${parameters.id?html}(data[0]);
                    });
                }
            });
        </script>
<#include "/${parameters.templateDir}/bootstrap/controlfooter.ftl" />