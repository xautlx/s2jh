$(function() {
    $(".grid-schedule-job-bean-cfg-index").data("gridOptions", {
        url : WEB_ROOT + '/schedule/job-bean-cfg!findByPage',
        colModel : [ {
            label : '任务类全名',
            name : 'jobClass',
            width : 200,
            editable : true,
            editoptions : {
                title : '新添加计划任务不会立刻安排作业,需要重启应用服务器才能生效'
            }
        }, {
            label : 'CRON表达式',
            name : 'cronExpression',
            width : 100,
            editable : true,
            align : 'right'
        }, {
            label : '自动初始运行',
            name : 'autoStartup',
            formatter : 'checkbox',
            editable : true,
            editoptions : {
                title : '是否随应用启动之后自动部署运行任务，禁用后需要手工启动任务运行'
            }
        }, {
            label : '启用历史记录',
            name : 'logRunHist',
            formatter : 'checkbox',
            editable : true,
            editoptions : {
                title : '关键任务建议启用历史记录，对非关键任务且运行频率较高考虑性能因素建议关闭'
            }
        }, {
            label : '集群运行模式',
            name : 'runWithinCluster',
            formatter : 'checkbox',
            editable : true,
            editoptions : {
                title : '变更运行模式会移除当前计划任务,并需要重启应用服务器才能生效'
            }
        }, {
            label : '名称描述',
            name : 'description',
            editable : true,
            edittype : 'textarea',
            width : 100,
            align : 'left'
        } ],
        editcol : 'code',
        editurl : WEB_ROOT + "/schedule/job-bean-cfg!doSave",
        delurl : WEB_ROOT + "/schedule/job-bean-cfg!doDelete"
    });
});