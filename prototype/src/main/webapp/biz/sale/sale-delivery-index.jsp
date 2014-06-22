<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li class="active"><a class="tab-default" data-toggle="tab" href="#tab-auto">列表查询</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane fade active in">
			<div class="row search-form-default">
				<div class="col-md-12">
					<form method="get" class="form-inline form-validation form-search-init"
						data-grid-search=".grid-biz-sale-sale-delivery" action="#">
						<div class="form-group">
							<input type="text" name="search['CN_voucher_OR_receivePerson']" class="form-control input-large"
								placeholder="销售单号、收货人...">
						</div>
						<div class="form-group">
							<input type="text" name="search['BT_voucherDate']" class="form-control input-medium input-daterangepicker"
								placeholder="记账日期">
						</div>
						<div class="form-group">
							<label> <s:checkbox name="search['EQ_voucherUser.signinid']" fieldValue="%{signinUsername}" />只显示我的
							</label>
						</div>
						<button class="btn default hidden-inline-xs" type="reset">
							<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
						</button>
						<button class="btn green" type="submmit">
							<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
						</button>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="grid-biz-sale-sale-delivery"></table>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="${base}/biz/sale/sale-delivery-index.js" />
<%@ include file="/common/ajax-footer.jsp"%>
