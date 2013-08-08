<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{persistentedModel?'/sys/data-dict!doUpdate':'/sys/data-dict!doCreate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="dataDictIndexTabs"
						callback-grid="dataDictListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close" callback-tab="dataDictIndexTabs"
						callback-grid="dataDictListDiv">
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
					<s2:combotext list="categoryMap" name="category" label="分类" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="key1Value" label="Key1定义" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="key2Value" label="Key2定义" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="data1Value" label="数据1设定" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="data2Value" label="数据2设定" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textarea name="data3Value" label="数据2设定" rows="3" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="code" label="代码" disabled="%{persistentedModel}" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="parentCode" label="父代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="disabled" list="#application.enums.booleanLabel" label="禁用标识" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="orderRank" label="排序号" />
				</div>
			</div>
		</div>
	</s2:form>
</div>