<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<s2:tabbedpanel>
				<ul>
					<li><a href="#cacheTab"> <span>缓存控制</span>
					</a></li>
					<li><a href="${base}/sys/util!logger"> <span>日志控制</span>
					</a></li>
				</ul>
				<div id="cacheTab">
					<form action='${base}/sys/util!dataEvictCache' method="post" id="hibernateEvictCacheEditForm"
						class="form-horizontal">
						<div class="row-fluid">
							<div class="toolbar">
								<div class="toolbar-inner">
									<button type="button" class="btn btn-submit">
										<i class="icon-ok"></i> 刷新缓存数据
									</button>
									<button type="reset" class="btn">
										<i class="icon-repeat"></i> 重置
									</button>
								</div>
							</div>
						</div>
						<div class="alert alert-info">
							<h4>功能说明</h4>
							为了系统运行效率，系统会基于Hibernate和Spring的Cache支持尽可能缓存数据<br>此功能主要用于直接修改数据库数据后，通知缓存框架移除选取范围的缓存数据从而加载最新数据库数据.
						</div>
						<div class="well">
							<div class="row-fluid">
								<div class="span8">
									<s2:select name="cacheNames" list="cacheNames" size="20" multiple="true" label="缓存刷新范围"
										value="" />
								</div>
								<div class="span4">留空或选择空白表示刷新整个缓存</div>
							</div>
						</div>
					</form>
				</div>
			</s2:tabbedpanel>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#hibernateEvictCacheEditForm").formvalidate({
                successCallback : function(response, submitButton) {
                    alert(response.message);
                }
            });
        });
    </script>
</body>
</html>