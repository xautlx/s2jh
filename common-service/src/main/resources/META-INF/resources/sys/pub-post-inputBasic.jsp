<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation" action="${base}/sys/pub-post!doSave"
	method="post">
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<button class="btn blue" type="submit" data-grid-reload=".grid-sys-pub-post-index">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body control-label-sm">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">公告标题</label>
					<div class="controls">
						<s:textfield name="htmlTitle" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">发布时间</label>
					<div class="controls">
						<s3:datetextfield name="publishTime" format="yyyy-MM-dd HH:mm:00" current="true" data-timepicker="true" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">到期时间</label>
					<div class="controls">
						<s3:datetextfield name="expireTime" format="yyyy-MM-dd HH:mm:00" data-timepicker="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">排序号</label>
					<div class="controls">
						<s:textfield name="orderRank" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">显示范围</label>
					<div class="controls">
						<s:checkbox name="frontendShow" label="前端" />
						<s:checkbox name="backendShow" label="后端" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">外部链接</label>
					<div class="controls">
						<s:textfield name="externalLink" />
						<div class="help-block">如果定义了外部链接，显示公告时忽略公告内容直接新开转向链接定义的页面</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">公告内容</label>
					<div class="controls">
						<s:textarea name="htmlContent" data-htmleditor="kindeditor" data-height="500px" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">关联附件</label>
					<div class="controls">
					
						<a class="btn green btn-fileinput-trigger" href="#fileupload-dialog" data-toggle="modal"
							data-url="${base}/sys/pub-post!attachmentList" data-pk='<s:property value="model.id"/>' data-name="attachments">添加附件...</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<button class="btn blue" type="submit" data-grid-reload=".grid-sys-pub-post-index">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<%@ include file="/common/ajax-footer.jsp"%>