$(function() {
    $(".grid-schedule-job-run-hist-index").data("gridOptions", {
        url : WEB_ROOT + '/schedule/job-run-hist!findByPage',
        colNames : [ '流水号', '主机节点', '任务名称', '触发时间', '下次触发时间', '触发次数', '异常标识', '执行结果', '创建时间', '版本号' ],
        colModel : [ {
            name : 'id',
            align : 'center',
            width : 50
        }, {
            name : 'nodeId',
            width : 150,
            align : 'center'
        }, {
            name : 'jobName',
            width : 200,
            align : 'left'
        }, {
            name : 'previousFireTime',
            fixed : true,
            sorttype : 'date',
            align : 'center'
        }, {
            name : 'nextFireTime',
            fixed : true,
            sorttype : 'date',
            align : 'center'
        }, {
            name : 'refireCount',
            width : 60,
            fixed : true,
            hidden : true,
            align : 'right'
        }, {
            name : 'exceptionFlag',
            fixed : true,
            formatter : 'checkbox',
            align : 'center'
        }, {
            name : 'result',
            width : 100,
            align : 'left'
        }, {
            name : 'createdDate',
            width : 120,
            fixed : true,
            hidden : true,
            align : 'center'
        }, {
            name : 'version',
            hidden : true,
            hidedlg : true
        } ],
        multiselect : false,
        viewurl : WEB_ROOT + '/schedule/job-run-hist!view',
        addable : false
    });
});