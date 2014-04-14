<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation"
	action='${base}/sys/util!loggerLevelUpdate' method="post">
	<div class="form-actions">
		<button class="btn blue" type="submit">
			<i class="fa fa-check"></i> 动态更新Logger日志级别
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
		<div class="alert alert-info">
			<p>
				此功能主要用于在应用运行过程中动态修改Logger日志级别从而实现在线Debug调试系统日志信息以便实时进行一些线上问题分析排查. <br>在调低日志级别问题排查完毕后，最好把日志级别调整会预设较高级别以避免大量日志信息影响系统运行效率
			</p>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">Logger Name</label>
					<div class="controls">
						<s:select name="loggerName" cssClass="form-control" list="loggerList" size="10" multiple="true" value=""
							emptyOption="false" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">Logger Level</label>
					<div class="controls">
						<s:select name="loggerLevel"
							list="#{'OFF':'OFF','ERROR':'ERROR','WARN':'WARN','INFO':'INFO','DEBUG':'DEBUG','TRACE':'TRACE','ALL':'ALL'}"
							value="'DEBUG'" emptyOption="false" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<button class="btn blue" type="submit">
			<i class="fa fa-check"></i> 动态更新Logger日志级别
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<%@ include file="/common/ajax-footer.jsp"%>