<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li class="active"><a class="tab-default" data-toggle="tab" href="#tab-auto">运行流程实例列表</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane fade active in">
			<div class="row search-form-default">
				<div class="col-md-12">
					<form action="#" method="get" class="form-inline form-validation form-search form-search-init"
						data-grid-search=".grid-bpm-process-instance-index">
						<div class="form-group">
							<input type="text" name="businessKey" class="form-control input-medium" placeholder="业务编码" />
						</div>
						<div class="form-group">
							<s:select list="processDefinitions" cssClass="form-control input-medium" name="processDefinitionKey"
								placeholder="请选择流程..." />
						</div>
						<button class="btn green" type="submmit">
							<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
						</button>
						<button class="btn default" type="reset">
							<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
						</button>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="grid-bpm-process-instance-index"></table>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="${base}/bpm/process-instance-index.js" />
<%@ include file="/common/ajax-footer.jsp"%>