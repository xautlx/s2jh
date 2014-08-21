<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation"
	action="${base}/auth/role!doUpdateRelatedPrivilegeR2s" method="post">
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
					<th><input type="checkbox"
						onclick="$(this).closest('table').find('tbody :checkbox').attr('checked',this.checked)" /> 权限分类</th>
					<th>角色代码</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator var="item" value="#request.privileges" status="s">
					<tr>
						<td class="active" style="width: 150px"><input type="checkbox"
							onclick="$(this).closest('tr').find('.checkbox-privileges :checkbox').attr('checked',this.checked)" /> <s:property
								value="%{#item.key}" /></td>
						<td class="checkbox-privileges"><s:iterator var="child" value="#item.value">
								<div class="col-md-3 <s:property value="%{#child.extraAttributes.related?'text-success':''}" />">
									<s:checkbox name="r2ids" fieldValue="%{#child.id}" value="%{#child.extraAttributes.related}"
										label="%{#child.title}" />
								</div>
							</s:iterator></td>
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