<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li class="active"><a class="tab-default" data-toggle="tab" href="#tab-privilege">权限列表</a></li>
		<li><a class="tab-default" data-toggle="tab" href="${base}/auth/privilege?_to_=urls">权限统计</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
	<div class="tab-content">
		<div id="tab-privilege" class="tab-pane fade active in">
			<div class="row search-form-default">
				<div class="col-md-12">
					<form action="#" method="get" class="form-inline form-validation form-search-init"
						data-grid-search=".grid-auth-privilege-index">
						<div class="input-group">
							<div class="input-cont">
								<input type="text" name="search['CN_code_OR_title_OR_url']" class="form-control" placeholder="权限代码、名称、URL...">
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
					<table class="grid-auth-privilege-index" data-grid="table"></table>
				</div>
			</div>

		</div>
	</div>
</div>
<script src="${base}/auth/privilege-index.js" />
<%@ include file="/common/ajax-footer.jsp"%>