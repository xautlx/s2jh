<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="form-horizontal form-bordered form-label-stripped">
	<div class="form-group">
		<label class="control-label">异常标识</label>
		<div class="controls">
			<p class="form-control-static">
				<s3:property value="exceptionFlag" />
			</p>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label">执行结果</label>
		<div class="controls">
			<s3:property value="result" pre="true" />
		</div>
	</div>
	<div class="form-group">
		<label class="control-label">异常日志</label>
		<div class="controls">
			<s3:property value="exceptionStack" pre="true" />
		</div>
	</div>
</div>
<%@ include file="/common/ajax-footer.jsp"%>
