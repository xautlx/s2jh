<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{persistentedModel?'/sys/config-property!doUpdate':'/sys/config-property!doCreate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="configPropertyIndexTabs"
						callback-grid="configPropertyListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close"
						callback-tab="configPropertyIndexTabs" callback-grid="configPropertyListDiv">
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
				<div class="span12">
					<s2:textfield name="propKey" label="代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textfield name="propName" label="名称" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textfield name="simpleValue" label="简单属性值" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:property value="staticConfigValue" label="静态属性值" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:kindeditor name="htmlValue" label="HTML属性值" rows="5" items="simple"/>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textarea name="propDescn" label="参数属性用法说明" rows="2" />
				</div>
			</div>
		</div>
	</s2:form>
</div>