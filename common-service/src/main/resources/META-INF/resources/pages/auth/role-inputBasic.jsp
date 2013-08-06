<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{isBlank(#parameters.id)?'/auth/role!doCreate':'/auth/role!doUpdate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="roleIndexTabs"
						callback-grid="roleListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close" callback-tab="roleIndexTabs"
						callback-grid="roleListDiv">
						<i class="icon-check"></i> 保存并关闭
					</button>
					<button type="reset" class="btn">
						<i class="icon-repeat"></i> 重置
					</button>
				</div>
			</div>
		</div>
		<div class="well">
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="code" label="代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="title" label="名称" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="disabled" list="#application.enums.booleanLabel" label="禁用标识" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="locked" list="#application.enums.booleanLabel" label="锁定标识" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textarea name="description" label="描述" rows="2"/>
				</div>
			</div>
		</div>
	</s2:form>
</div>