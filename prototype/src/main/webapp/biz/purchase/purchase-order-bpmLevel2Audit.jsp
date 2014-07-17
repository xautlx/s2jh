<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation"
	action="${base}/biz/purchase/purchase-order!bpmLevel2Audit" method="post">
	<s:hidden name="taskId" value="%{#parameters.taskId}" />
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<button class="btn blue" type="submit" data-ajaxify-reload=".ajaxify-tasks">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
		<s:set var="taskVariablesVar" value="taskVariables" />
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">二线审核通过</label>
					<div class="controls">
						<s:radio name="auditLevel2Pass" list="#application.enums.booleanLabel" value="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">二线审核意见</label>
					<div class="controls">
						<s:textarea name="auditLevel2Explain" rows="3" />
					</div>
				</div>
			</div>
		</div>
	<div class="portlet">
				<div class="portlet-title">
					<div class="caption">
						<span >一线审核</span>
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-12">
						<div class="form-group">
							<label class="control-label">通过:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="#application.enums.booleanLabel[#attr.taskVariablesVar['auditLevel1Pass']]" />
								</p>
							</div>
						</div>
					</div>
					</div>
					<div class="row">
						<div class="col-md-12">
						<div class="form-group">
							<label class="control-label">审核人员:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="#attr.taskVariablesVar['auditLevel1User']" />
								</p>
							</div>
						</div>
					</div>
					</div>
					<div class="row">
						<div class="col-md-12">
						<div class="form-group">
							<label class="control-label">审核时间:</label>
							<div class="controls">
								<p class="form-control-static">
									<s:date name="#attr.taskVariablesVar['auditLevel1Time']" format="yyyy-MM-dd HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
					</div>
					<div class="row">
						<div class="col-md-12">
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
		</div>
			
	</div>
	
	<div class="form-actions right">
		<button class="btn blue" type="submit" data-ajaxify-reload=".ajaxify-tasks">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<div class="purchase-order-content ajaxify"
	data-url="${base}/biz/purchase/purchase-order!view?id=<s:property value='#parameters.id'/>"></div>

<%@ include file="/common/ajax-footer.jsp"%>
