<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row search-form-default">
	<div class="col-md-12">
		<form action="#" method="get" class="form-inline form-validation form-search-init"
			data-grid-search=".grid-biz-finance-biz-trade-unit-selection">
			<div class="form-group">
				<input type="text" name="search['CN_code_OR_name']" class="form-control input-large" placeholder="代码/名称...">
			</div>
			<div class="form-group">
				<label> <s:checkbox name="search['IN_type']" fieldValue="CUSTOMER" value="true" />客户
				</label> <label> <s:checkbox name="search['IN_type']" fieldValue="SUPPLIER" value="true" />供应商
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
		<table class="grid-biz-finance-biz-trade-unit-selection"></table>
	</div>
</div>
<script src="${base}/biz/finance/biz-trade-unit-selection.js" />
<%@ include file="/common/ajax-footer.jsp"%>
