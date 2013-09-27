<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="toolbar">
			<div class="toolbar-inner">
				<button type="button" class="btn" id="resumeTriggerBtn">
					<i class="icon-play"></i> 启动
				</button>
				<button type="button" class="btn" id="pauseTriggerBtn">
					<i class="icon-pause"></i> 暂停
				</button>
				<div class="divider-vertical"></div>
                <button type="button" class="btn" id="runTriggerBtn">
                    <i class="icon-screenshot"></i> 立刻运行
                </button>
			</div>
		</div>
	</div>
	<div class="row-fluid">
		<table id="quartzTriggerListDiv"></table>
	</div>
</div>
<script type="text/javascript">
    $(function() {
        $("#quartzTriggerListDiv").grid({
            url : '${base}/schedule/job-bean-cfg!triggers',
            colNames : [ '任务名称','CRON表达式', '当前状态', '上次触发时间','下次触发时间', '集群运行模式'],
            colModel : [ {
                name : 'jobName',
                align : 'left'
            }, {
                name : 'cronExpression',
                width : 120,
                align : 'right'  
            }, {
                name : 'stateLabel',
                width : 60,
                align : 'center'                  
            }, {
                name : 'previousFireTime',
                fixed : true,
                sorttype: 'date',
                align : 'center'
            }, {
                name : 'nextFireTime',
                fixed : true,
                sorttype: 'date',
                align : 'center'                                
            }, {
                name : 'runWithinCluster',
                fixed : true,
                formatter : booleanFormatter,
                align : 'center'                   
            }],
            rowNum: -1,
            pager: false,
            loadonce: true,
            caption : "定时任务配置列表"
        });

        $("#resumeTriggerBtn").click(function() {
            if (rowids = $("#quartzTriggerListDiv").jqGrid("getAtLeastOneSelectedItem")) {
                $.ajaxPostURL({
                    url : '${base}/schedule/job-bean-cfg!doStateTrigger',
                    data : {
                        ids : rowids,
                        state: 'resume'
                    },
                    confirm : '确认 启动 所选计划任务？',
                    successCallback : function(response) {
                        $("#quartzTriggerListDiv").jqGrid("refresh");
                    }
                });
            }
        });

        $("#pauseTriggerBtn").click(function() {
            if (rowids = $("#quartzTriggerListDiv").jqGrid("getAtLeastOneSelectedItem")) {
                $.ajaxPostURL({
                    url : '${base}/schedule/job-bean-cfg!doStateTrigger',
                    data : {
                        ids : rowids,
                        state: 'pause'
                    },
                    confirm : '确认  暂停  所选计划任务？',
                    successCallback : function(response) {
                        $("#quartzTriggerListDiv").jqGrid("refresh");
                    }
                });
            }
        });
        

        $("#runTriggerBtn").click(function() {
            if (rowids = $("#quartzTriggerListDiv").jqGrid("getAtLeastOneSelectedItem")) {
                $.ajaxPostURL({
                    url : '${base}/schedule/job-bean-cfg!doRunTrigger',
                    data : {
                        ids : rowids
                    },
                    confirm : '确认  立即运行  所选计划任务？',
                    successCallback : function(response) {
                        $("#quartzTriggerListDiv").jqGrid("refresh");
                    }
                });
            }
        });
    });
</script>
