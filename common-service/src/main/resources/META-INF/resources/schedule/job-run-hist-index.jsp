<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row search-form-default">
	<div class="col-md-12">
		<form action="#" method="get" class="form-inline form-validation form-search-init"
			data-grid-search=".grid-schedule-job-run-hist-index">
			<div class="input-group">
				<div class="input-cont">
					<input type="text" name="search['CN_jobName_OR_nodeId']" class="form-control" placeholder="任务名称、主机...">
				</div>
				<span class="input-group-btn">
					<button class="btn green" type="submmit">
						<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
					</button>
					<button class="btn default" type="reset">
						<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
					</button>
				</span>
			</div>
		</form>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<table class="grid-schedule-job-run-hist-index"></table>
	</div>
</div>
<script src="${base}/schedule/job-run-hist-index.js" />
<%@ include file="/common/ajax-footer.jsp"%>