<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="form-horizontal form-bordered form-label-stripped">
	<div class="row">
		<s:if test="#attr.taskVariablesVar['auditLevel2Time']!=null">
			<div class="col-md-6">
				<div class="portlet gren">
					<div class="portlet-title">
						<div class="caption">二线审核</div>
					</div>
					<div class="portlet-body">
						<div class="form-group">
							<label class="control-label">通过:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="#application.enums.booleanLabel[#attr.taskVariablesVar['auditLevel2Pass']]" />
								</p>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label">审核人员:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="#attr.taskVariablesVar['auditLevel2User']" />
								</p>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label">审核时间:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:date name="#attr.taskVariablesVar['auditLevel2Time']" format="yyyy-MM-dd HH:mm:ss" />
								</p>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label">审核意见:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="#attr.taskVariablesVar['auditLevel2Explain']" />
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:if>
		<s:if test="#attr.taskVariablesVar['auditLevel1Time']!=null">
			<div class="col-md-6">
				<div class="portlet gren">
					<div class="portlet-title">
						<div class="caption">一线审核</div>
					</div>
					<div class="portlet-body">
						<div class="form-group">
							<label class="control-label">通过:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="#application.enums.booleanLabel[#attr.taskVariablesVar['auditLevel1Pass']]" />
								</p>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label">审核人员:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="#attr.taskVariablesVar['auditLevel1User']" />
								</p>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label">审核时间:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:date name="#attr.taskVariablesVar['auditLevel1Time']" format="yyyy-MM-dd HH:mm:ss" />
								</p>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label">审核意见:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="#attr.taskVariablesVar['auditLevel1Explain']" />
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:if>
	</div>
	<div class="portlet">
		<div class="portlet-title">
			<div class="caption">订单信息</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">经办人</label>
						<div class="controls">
							<p class="form-control-static">
								<s:property value="voucherUser.display" />
							</p>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">部门</label>
						<div class="controls">
							<p class="form-control-static">
								<s:property value="voucherDepartment.display" />
							</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">采购单号:</label>
						<div class="controls">
							<p class="form-control-static">
								<s:property value="voucher" />
							</p>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">供货商:</label>
						<div class="controls">
							<p class="form-control-static">
								<s:property value="supplier.display" />
							</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">采购说明:</label>
						<div class="controls">
							<p class="form-control-static">
								<s:property value="adminMemo" />
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="portlet">
		<div class="portlet-title">
			<div class="caption">采购明细</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<div class="row">
				<div class="col-md-12">
					<div class="table-responsive">
						<table class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									<th align="center" style="width: 20px">#</th>
									<th align="center" style="width: 400px">商品</th>
									<th align="center" style="width: 100px">采购数量</th>
									<th align="center" style="width: 100px">采购价格</th>
									<th align="center" style="width: 100px">金额小计</th>
									<th align="center" style="width: 100px">折扣率(%)</th>
									<th align="center" style="width: 100px">折扣额</th>
									<th align="center" style="width: 100px">折后金额</th>
									<th align="center" style="width: 100px">分摊运费</th>
									<th align="center" style="width: 100px">成本单价</th>
									<th align="center" style="width: 100px">成本金额</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="purchaseOrderDetails" var="item" status="s">
									<tr>
										<td><s:property value="#s.count" /></td>
										<td><s:property value="#item.commodity.display" /></td>
										<td align="center"><s:property value="#item.quantity" /></td>
										<td align="right"><s:property value="#item.price" /></td>
										<td align="right"><s:property value="#item.originalAmount" /></td>
										<td align="right"><s:property value="#item.discountRate" /></td>
										<td align="right"><s:property value="#item.discountAmount" /></td>
										<td align="right"><s:property value="#item.amount" /></td>
										<td align="right"><s:property value="#item.deliveryAmount" /></td>
										<td align="right"><s:property value="#item.costPrice" /></td>
										<td align="right"><s:property value="#item.costAmount" /></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<div class="note note-success">
				<h4 class="block">
					订单总金额：
					<s:property value="totalAmount" />
					(含运费：
					<s:property value="totalDeliveryAmount" />
					) ，已预付金额：
					<s:property value="actualPayedAmount" />
				</h4>
			</div>
		</div>
	</div>
</div>
<%@ include file="/common/ajax-footer.jsp"%>
