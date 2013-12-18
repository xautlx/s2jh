<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<ul class="breadcrumb">
			<li class="active">欢迎访问<s:property value="%{systemTitle}" /></li>
		</ul>
		<div class="row-fluid">
			<div class="column" id="column1" style="width: 60%" data-sortable-column="true">
				<div class="dragbox" data-dragbox="true">
					<h2>待办任务</h2>
					<div class="dragbox-content" style="margin: 0px">
						<div id="userBpmTasksDiv"></div>
					</div>
				</div>
			</div>
			<div class="column" id="column2" style="width: 40%" data-sortable-column="true">
				<div class="dragbox" data-dragbox="true">
					<h2>最近公告</h2>
					<div class="dragbox-content" style="margin: 0px">
						<div id="pubPostListShow"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script>
        $(function() {

            $("#pubPostListShow").ajaxGetUrl("${base}/profile/pub-post!list");

            $("#userBpmTasksDiv").ajaxGetUrl("${base}/bpm/bpm-task!userTasks");
        });
    </script>
</body>
</html>