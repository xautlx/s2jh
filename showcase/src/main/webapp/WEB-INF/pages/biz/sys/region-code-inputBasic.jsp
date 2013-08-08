<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{isBlank(#parameters.id)?'/biz/sys/region-code!doCreate':'/biz/sys/region-code!doUpdate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="regionCodeIndexTabs"
						callback-grid="regionCodeListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close"
						callback-tab="regionCodeIndexTabs" callback-grid="regionCodeListDiv">
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
					<s2:textfield name="regionCode" label="行政区划代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="regionDesc" label="行政区划名称" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="regionShort" label="行政区划简称" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="regionEdu" label="教育行政部门名称" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="parentcode" label="父行政区划代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="dqlx" label="地区类型" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="enabled" list="#application.enums.booleanLabel" label="状态" />
				</div>
			</div>
		</div>
	</s2:form>
</div>