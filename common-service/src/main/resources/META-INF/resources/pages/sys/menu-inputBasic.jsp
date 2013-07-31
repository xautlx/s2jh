<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{persistentedModel?'/sys/menu!doUpdate':'/sys/menu!doCreate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="menuIndexTabs"
						callback-grid="menuListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close" callback-tab="menuIndexTabs"
						callback-grid="menuListDiv">
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
					<s2:treeinput name="parentName" label="父节点" hiddenName="parentId" treeDataUrl="/sys/menu!list"
						readonly="true" value="%{parent.title}" hiddenValue="%{parent.id}" />
				</div>
			</div>
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
					<s2:textfield name="url" label="菜单URL" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:select name="type" list="#application.enums.menuTypeEnum" label="类型" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="initOpen" list="#application.enums.booleanLabel" label="展开标识" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="disabled" list="#application.enums.booleanLabel" label="禁用标识" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="style" label="图标样式" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="orderRank" label="排序号" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="description" label="描述" />
				</div>
			</div>
		</div>
	</s2:form>
</div>
