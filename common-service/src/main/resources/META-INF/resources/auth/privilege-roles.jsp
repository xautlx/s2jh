<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation"
	action="${base}/auth/privilege!doUpdateRelatedRoleR2s" method="post">
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<button class="btn blue" type="submit">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
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
				<s:iterator var="item" value="#request.roles" status="s">
					<tr class='select-table-checkbox <s:property value="%{#item.extraAttributes.related?'text-primary':''}" />'>
						<td class="table-checkbox"><s:checkbox name="r2ids" fieldValue="%{#item.id}"
								value="%{#item.extraAttributes.related}" /></td>
						<td><s:property value="%{#item.title}" /></td>
						<td><s:property value="%{#item.code}" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
	<div class="form-actions right">
		<button class="btn blue" type="submit">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<%@ include file="/common/ajax-footer.jsp"%>