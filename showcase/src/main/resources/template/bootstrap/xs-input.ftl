<#include "/${parameters.templateDir}/bootstrap/controlheader.ftl" />
        <div class="input-append">
            <#include "/${parameters.templateDir}/simple/text.ftl" />
            <span class="add-on" style="cursor: pointer" id="btnSelect_${parameters.id?html}">
            <i class="icon-list"></i></span><span id="text_for_${parameters.id?html}" class="add-on"></span>
        </div>
        <script type="text/javascript">
            function callback${parameters.id?html}(json) {
                $("#${parameters.id?html}").val(json.xh);
                var link = '<a href="javascript:void(0)" title="查看学生信息" onclick="$.popupViewDialog(\'${request.contextPath}/biz/xs/xs-jbxx!viewTabs?id=' + json.id + '\')">' + json.xm + '</a>';
                $("#text_for_${parameters.id?html}").html(link);
            }
            $().ready(function() {
                $("#btnSelect_${parameters.id?html}").click(function() {
                    $.popupDialog({
                        dialogId : 'xsSelectionDialog',
                        url : "${request.contextPath}/biz/xs/xs-jbxx!forward?_to_=selection&callback=callback${parameters.id?html}",
                        title : '选择...',
                        width : 850,
                        height : 400
                    })
                });

                $("#${parameters.id?html}").autocomplete({
                    minLength : 2,
                    source : "${request.contextPath}/biz/xs/xs-jbxx!autocomplete",
                    select : function(event, ui) {
                        callback${parameters.id?html}(ui.item);
                        return false;
                    }
                }).data("ui-autocomplete")._renderItem = function(ul, item) {
                    return $("<li>").append("<a><b>" + item.xh + "</b> " + item.xm + "</a>").appendTo(ul);
                };

                $("#${parameters.id?html}").rules("add", {
                    remote : {
                        url : "${request.contextPath}/biz/xs/xs-jbxx!validXh",
                        data : {
                            xh : function() {
                                return $("#${parameters.id?html}").val();
                            }
                        }
                    }
                });
                
                var term=$("#${parameters.id?html}").val();
                if(term!=''){
                    $.getJSON("${request.contextPath}/biz/xs/xs-jbxx!autocomplete?term="+term, function(data){
                      callback${parameters.id?html}(data[0]);
                    });
                }
            });
        </script>
<#include "/${parameters.templateDir}/bootstrap/controlfooter.ftl" />