<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{persistentedModel?'/sys/pub-post!doUpdate':'/sys/pub-post!doCreate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<s2:button type="button" cssClass="btn btn-submit" callback-tab="pubPostIndexTabs"
						callback-grid="pubPostListDiv" disabled="disallowUpdate">
						<i class="icon-ok"></i> 保存
					</s2:button>
					<s2:button type="button" cssClass="btn btn-submit submit-post-close"
						callback-tab="pubPostIndexTabs" callback-grid="pubPostListDiv" disabled="disallowUpdate">
						<i class="icon-check"></i> 保存并关闭
					</s2:button>
					<s2:button type="reset" cssClass="btn" disabled="disallowUpdate">
						<i class="icon-repeat"></i> 重置
					</s2:button>
				</div>
			</div>
		</div>
		<div class="alert alert-info">
			<strong>提示:</strong>公告一旦被阅读后(已阅读人数大于零)，公告将冻结不允许被修改。
		</div>
		<div class="well">
			<div class="row-fluid">
				<div class="span12">
					<s2:property value="readUserCount" label="已阅读人数" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:kindeditor name="htmlTitle" label="标题" rows="2" items="simple"/>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:datetextfield name="publishTime" label="发布时间" format="yyyy-MM-dd HH:mm:00" current="true" />
				</div>
				<div class="span6">
					<s2:datetextfield name="expireTime" label="到期时间" format="yyyy-MM-dd HH:mm:00" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:kindeditor name="htmlContent" label="公告内容" rows="10" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:singlefile name="r2FileId" labelValue="%{r2File.fileRealName}" label="关联附件"
						value="%{r2File.id}" tooltip="多文件请首先压缩打包为<strong>单个文件</strong>上传" />
				</div>
			</div>
		</div>
	</s2:form>
</div>