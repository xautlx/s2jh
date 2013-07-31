<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{persistentedModel?'/rpt/report-def!doUpdate':'/rpt/report-def!doCreate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:hidden name="prepare" value="true" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="reportDefIndexTabs"
						callback-grid="reportDefListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close"
						callback-tab="reportDefIndexTabs" callback-grid="reportDefListDiv">
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
					<s2:textfield name="description" label="描述" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:comboselect name="type" list="#application.enums.reportTypeEnum" label="类型" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:combotext name="category" list="categories" label="分类" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="orderRank" label="排序号" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="disabled" list="#application.enums.booleanLabel" label="禁用标识" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:singlefile name="templateFileId" labelValue="%{templateFile.fileRealName}" label="附件" value="%{templateFile.id}" />
				</div>
			</div>
		</div>
	</s2:form>
	<div class="row-fluid"></div>
</div>