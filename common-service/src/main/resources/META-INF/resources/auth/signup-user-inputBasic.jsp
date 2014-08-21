<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation"
	action="${base}/auth/signup-user!doAudit" method="post">
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<s3:button cssClass="btn blue" disabled="%{auditTime!=null}" type="submit">
			<i class="fa fa-check"></i> 审核
		</s3:button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">登录帐号</label>
					<div class="controls">
						<s:textfield name="signinid"/>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">控制代码</label>
					<div class="controls">
						<s:textfield name="aclCode" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">授权角色</label>
					<div class="controls">
						<table class="table table-striped table-advance table-bordered table-hover">
							<thead>
								<tr>
									<th class="table-checkbox"><input type="checkbox"
										onclick="$(this).closest('table').find('tbody .table-checkbox :checkbox').attr('checked',this.checked)" /></th>
									<th>角色名称</th>
									<th>角色代码</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator var="item" value="roles" status="s">
									<tr class='select-table-checkbox'>
										<td class="table-checkbox"><s:checkbox name="r2ids" fieldValue="%{#item.id}" /></td>
										<td><s:property value="%{#item.title}" /></td>
										<td><s:property value="%{#item.code}" /></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<s3:button cssClass="btn blue" disabled="%{auditTime!=null}" type="submit">
			<i class="fa fa-check"></i> 审核
        </s3:button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<%@ include file="/common/ajax-footer.jsp"%>
