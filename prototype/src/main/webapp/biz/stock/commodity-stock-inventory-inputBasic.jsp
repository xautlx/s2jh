<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation form-stock-commodity-stock-inventory"
	action="${base}/biz/stock/commodity-stock-inventory!doCreate" method="post">
	<s:hidden name="id" />
	<div class="form-actions form-inline">
		<s3:button type="submit" cssClass="btn blue" disabled="true">
			<i class="fa fa-check"></i> 保存
		</s3:button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body control-label-sm">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">盘存库存地</label>
					<div class="controls">
						<s:select list="#{'SA':'库存地A','SB':'库存地B'}" name="storageLocation.id" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">商品条码</label>
					<div class="controls">
						<div class="input-group ">
							<s:textfield name="commodityBarcode" placeholder="条码枪扫描输入..." />
							<s:hidden name="commodity.id" />
							<span class="input-group-addon"><i class="fa fa-barcode"></i></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="commodity-info hide">
			<div class="form-group">
				<label class="control-label">盘存商品</label>
				<div class="controls">
					<p class="form-control-static commodity-display"></p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label">盘存数量</label>
				<div class="controls">
					<div class="input-group ">
						<s:textfield name="curStockQuantity" placeholder="输入初始化盘存数量" />
						<span class="input-group-addon commodity-quantity"></span>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<s3:button type="submit" cssClass="btn blue" disabled="true">
			<i class="fa fa-check"></i> 保存
		</s3:button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="portlet hide portlet-stock-inventory-logs">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i>本次盘存记录
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<ul class="list-group list-stock-inventory-logs">
			</ul>
		</div>
	</div>
</form>

<script src="${base}/biz/stock/commodity-stock-inventory-inputBasic.js" />
<%@ include file="/common/ajax-footer.jsp"%>
