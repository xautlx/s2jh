<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation" action='${base}/sys/util!dataEvictCache'
	method="post">
	<div class="form-actions">
		<button class="btn blue" type="submit">
			<i class="fa fa-check"></i> 刷新缓存数据
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
		<div class="alert alert-info">
			<p>为了系统运行效率，系统会基于Hibernate和Spring的Cache支持尽可能缓存数据</p>
			<p>此功能主要用于直接修改数据库数据后，通知缓存框架移除选取范围的缓存数据从而加载最新数据库数据.</p>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">缓存刷新范围</label>
					<div class="controls">
						<s3:select name="cacheNames" list="cacheNames" size="20" multiple="true" value="" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<button class="btn blue" type="submit">
			<i class="fa fa-check"></i> 刷新缓存数据
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<%@ include file="/common/ajax-footer.jsp"%>