<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid data-view">
		<div class="well form-horizontal">
			<div class="row-fluid">
				<div class="toolbar">
					<div class="toolbar-inner">
						<button type="button" class="btn" id="openH2Btn">
							<i class="icon-ok"></i> 打开H2登录界面
						</button>
					</div>
				</div>
			</div>
			<div class="alert alert-info">
				<h4>功能说明</h4>
				启动H2 Server服务，可以访问H2 Console控制台进行数据查询管理。点击“打开H2登录界面”按钮在新开H2登录页面对应拷贝粘贴填入以下信息即可：
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:property value="'org.h2.Driver'" label="Driver Class" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:property value="%{'jdbc:h2:file:'+#request.h2DatabaseName}" label="JDBC URL" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:property value="'sa'" label="User Name" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:property value="" label="Password" />
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#openH2Btn").click(function() {
                window.open('<s:property value="#request.h2LoginUrl"/>', "_blank");
            });
        });
    </script>
</body>
</html>