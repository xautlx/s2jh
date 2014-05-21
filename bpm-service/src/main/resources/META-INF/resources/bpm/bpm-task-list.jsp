<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="portlet box green tasks-widget portlet-tasks">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-tasks"></i>最近待办任务<span class="badge badge-warning"><s:property value="#request.tasks.size()" /></span>
		</div>
		<div class="tools">
			<a href="" class="reload"></a>
		</div>
		<div class="actions">
			<div class="btn-group">
				<a data-close-others="true" data-hover="dropdown" data-toggle="dropdown" href="javascript:;"
					class="btn btn-xs green"> 过滤 <i class="fa fa-angle-down"></i>
				</a>
				<div class="dropdown-menu hold-on-click dropdown-checkboxes pull-right">
					<label><input type="checkbox" checked="true" id="chk-task-user" /> 个人任务</label> <label><input
						type="checkbox" checked="true" id="chk-task-candidate" /> 候选任务</label>
				</div>
			</div>
		</div>
	</div>
	<div class="portlet-body">
		<div class="scroller" style="height: 300px;" data-always-visible="1" data-rail-visible="0">
			<ul class="feeds" id="dashboard-task-list">
				<s:iterator value="#request.tasks" status="s" var="item">
					<li need-claim="<s:property value='#item.candidate' />" id="<s:property value='#item.id' />"><a
						class="ajaxify" href="javascript:;" id="list-task-<s:property value='#item.id' />"
						rel="address:/bpm/bpm-task!show?taskId=<s:property value='#item.id' />&candidate=<s:property value='#item.candidate' />"
						title='<s:property value="#item.name" />'>
							<div class="col1">
								<div class="cont">
									<div class="cont-col1">
										<s:if test="%{#item.candidate}">
											<div class="label label-sm label-info">
												<i class="fa fa-group"></i>
											</div>
										</s:if>
										<s:else>
											<div class="label label-sm label-primary">
												<i class="fa fa-user"></i>
											</div>
										</s:else>
									</div>
									<div class="cont-col2">
										<div class="desc">
											<s:property value="#item.pdname" />
											-
											<s:property value="#item.name" />
										</div>
										<div class="biz-key" style="padding-left: 35px">
											<s:property value="#item.bizKey" />
										</div>
									</div>
								</div>
							</div>
							<div class="col2">
								<div class="date">
									<s:date name="#item.createTime" format="MM-dd HH:mm" />
								</div>
							</div>
					</a></li>
				</s:iterator>
			</ul>
		</div>
		<div class="scroller-footer">
			<div class="pull-right">
				<a href="javascript:;">查看全部... <i class="m-icon-swapright m-icon-gray"></i></a> &nbsp;
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
    $(function() {

        //console.profile('Profile Sttart');

        $("#chk-task-user").click(function() {
            var $userTasks = $("#dashboard-task-list").find("li[need-claim='false']");
            if (this.checked) {
                $userTasks.show();
            } else {
                $userTasks.hide();
            }
        });

        $("#chk-task-candidate").click(function() {
            var $candidateTasks = $("#dashboard-task-list").find("li[need-claim='true']");
            if (this.checked) {
                $candidateTasks.show();
            } else {
                $candidateTasks.hide();
            }
        });

        var dropdownTaskHTML = [];
        var $headerTaskBar = $("#header_task_bar");
        var $dropdownTaskList = $headerTaskBar.find(".dropdown-menu");
        var $dropdownTaskListUL = $dropdownTaskList.find(".dropdown-menu-list");
        var $taskTemplate = $dropdownTaskListUL.clone(false);
        $taskTemplate.children("li.template").removeClass("display-hide");
        var newDropdownTaskListCount = 0;
        $("#dashboard-task-list > li").each(function() {
            newDropdownTaskListCount = newDropdownTaskListCount + 1;
            var $item = $(this);
            var taskId = $item.attr("id");
            $taskTemplate.find(".task-biz-key").html($item.find(".biz-key").html());
            $taskTemplate.find(".task-desc").html($item.find(".desc").html());
            $taskTemplate.find(".task-date").html($item.find(".date").html());
            var $itemlink = $item.find("a");
            var $templatelink = $taskTemplate.find("a");

            $templatelink.attr("for-task-id", taskId);
            $templatelink.attr("href", $itemlink.attr('href'));
            $templatelink.attr("title", $itemlink.attr('title'));
            if ($item.attr("need-claim") == 'true') {
                $taskTemplate.find(".label-primary").removeClass("label-primary").addClass("label-info");
                $taskTemplate.find(".fa-user").removeClass("fa-user").addClass("fa-group");
            }
            dropdownTaskHTML.push($taskTemplate.html());
        });
        var $badgeTasksCount = $headerTaskBar.find(".badge-tasks-count");
        var curTasksCount = $badgeTasksCount.html();
        if (curTasksCount == '') {
            curTasksCount = '0';
        }
        $dropdownTaskListUL.empty();
        $dropdownTaskListUL.html(dropdownTaskHTML.join(""));
        if (newDropdownTaskListCount == 0) {
            $badgeTasksCount.hide();
        } else {
            $badgeTasksCount.html(newDropdownTaskListCount).show();
        }
        $dropdownTaskList.find(".tasks-count").html(newDropdownTaskListCount);
        curTasksCount = Number(curTasksCount);
        if (newDropdownTaskListCount > curTasksCount) {
            $headerTaskBar.pulsate({
                color : "#bf1c56",
                repeat : 5
            });
        }

        $("a", $dropdownTaskListUL).on("click", function() {
            var forTaskId = $(this).attr("for-task-id");
            $("#list-task-" + forTaskId).click();
        });

        //console.profileEnd();
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>