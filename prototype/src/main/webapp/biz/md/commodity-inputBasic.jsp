<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation form-biz-md-commodity-inputBasic"
	action="${base}/biz/md/commodity!doSave" method="post" data-editrulesurl="${base}/biz/md/commodity!buildValidateRules">
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<button class="btn blue" type="submit" data-grid-reload=".grid-biz-md-commodity-index">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">SKU编码</label>
					<div class="controls">
						<s:textfield name="sku" disabled="notNew" placeholder="创建之后不可修改，请仔细填写" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">实物条码</label>
					<div class="controls">
						<div class="input-group ">
							<s:textfield name="barcode" />
							<span class="input-group-addon"><i class="fa fa-barcode"></i></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">商品名称</label>
					<div class="controls">
						<s:textfield name="title" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">成本价</label>
					<div class="controls">
						<s:textfield name="costPrice" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">销售价</label>
					<div class="controls">
						<s:textfield name="salePrice" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">默认库存地</label>
					<div class="controls">
						<s:textfield name="defaultStorageLocation.id" data-display="%{defaultStorageLocation.display}"
							data-optionsurl="%{#attr.base+'/biz/stock/storage-location!selectOptions'}" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">不可买</label>
					<div class="controls">
						<s:radio name="soldOut" list="#application.enums.booleanLabel" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">已下架</label>
					<div class="controls">
						<s:radio name="removed" list="#application.enums.booleanLabel" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<button class="btn blue" type="submit" data-grid-reload=".grid-biz-md-commodity-index">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<script src="${base}/biz/md/commodity-inputBasic.js" />
<%@ include file="/common/ajax-footer.jsp"%>