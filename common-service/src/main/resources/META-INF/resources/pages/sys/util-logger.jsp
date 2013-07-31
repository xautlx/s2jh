<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid">
	<div class="row-fluid">
		<form action='${base}/sys/util!loggerLevelUpdate' method="post" id="loggerLevelUpdateEditForm"
			class="form-horizontal">
			<div class="row-fluid">
				<div class="toolbar">
					<div class="toolbar-inner">
						<button type="button" class="btn btn-submit">
							<i class="icon-ok"></i> 动态更新Logger日志级别
						</button>
						<button type="reset" class="btn">
							<i class="icon-repeat"></i> 重置
						</button>
					</div>
				</div>
			</div>
			<div class="alert alert-info">
				<h4>功能说明</h4>
				此功能主要用于在应用运行过程中动态修改Logger日志级别从而实现在线Debug调试系统日志信息以便实时进行一些线上问题分析排查.
				<br>在调低日志级别问题排查完毕后，最好把日志级别调整会预设较高级别以避免大量日志信息影响系统运行效率
			</div>
			<div class="well">
				<div class="row-fluid">
					<div class="span8">
						<s2:select name="loggerName" list="loggerList" size="20" multiple="true" label="Logger Name"
							value="" emptyOption="false" />
					</div>
				</div>
				<div class="row-fluid">
					<div class="span4">
						<s2:textfield name="loggerLevel" label="Logger Level" />
					</div>
					<div class="span4">OFF/ERROR/WARN/INFO/DEBUG/TRACE/ALL</div>
				</div>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
    $(function() {
        $("#loggerLevelUpdateEditForm").formvalidate({
            successCallback : function(response, submitButton) {
                alert(response.message);
            }
        });
    });
</script>