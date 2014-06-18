$(function() {
    $(".grid-bpm-process-instance-index").data("gridOptions", {
        url : WEB_ROOT + '/bpm/process-instance!findByPageRunning',
        colModel : [ {
            label : '业务编码',
            name : 'businessKey',
            width : 150
        }, {
            label : '流程名称',
            name : 'processDefinitionName',
            width : 150
        }, {
            label : '当前活动',
            name : 'activityNames',
            width : 150
        } ],
        filterToolbar : false,
        cmTemplate : {
            sortable : false
        },
        operations : function(items) {
            var $grid = $(this);
            var $select = $('<li data-position="multi" data-toolbar="show"><a href="javascript:;"><i class="fa fa-trash-o"></i> 强制结束流程实例</a></li>');
            $select.children("a").bind("click", function(e) {
                var ids = $grid.getAtLeastOneSelectedItem();
                if (ids) {
                    $grid.ajaxPostURL(WEB_ROOT + "/bpm/process-instance!forceTerminal", function(response) {
                        $grid.refresh();
                    }, "确认强制结束流程实例吗？", {
                        data : {
                            ids : ids.join(",")
                        }
                    })
                }
            });
            items.push($select);
        },
        delurl : WEB_ROOT + '/bpm/process-instance!doDelete'
    });
});