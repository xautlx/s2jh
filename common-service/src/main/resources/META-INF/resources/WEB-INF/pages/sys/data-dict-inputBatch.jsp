<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post" action="/sys/data-dict!doCreateBatch">
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
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
			<s2:dynamictable afterAdd="row.find(\"select[name$='category']\").combotext();">
				<thead>
					<tr>
						<th><span class="required">*</span>分类</th>
						<th><span class="required">*</span>Key1定义</th>
						<th>Key2定义</th>
						<th>数据1设定</th>
						<th>数据2设定</th>
					</tr>
				</thead>
				<tbody>
					<tr class="dynamicRowTemplate">
						<s:hidden name="batchDataDicts[x].extraAttributes.uiOperation" value="create" />
						<td><s:select list="categoryMap" name="batchDataDicts[x].category" requiredLabel="true" /></td>
						<td><s:textfield name="batchDataDicts[x].key1Value" requiredLabel="true"
								cssClass="input-fluid" /></td>
						<td><s:textfield name="batchDataDicts[x].key2Value" cssClass="input-fluid" /></td>
						<td><s:textfield name="batchDataDicts[x].data1Value" cssClass="input-fluid" /></td>
						<td><s:textfield name="batchDataDicts[x].data2Value" cssClass="input-fluid" /></td>
					</tr>
				</tbody>
			</s2:dynamictable>
		</div>
	</s2:form>
</div>
