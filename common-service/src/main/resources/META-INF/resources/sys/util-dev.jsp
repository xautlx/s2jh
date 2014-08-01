<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li class="active"><a class="tab-default" data-toggle="tab" href="#tab-auto">开发功能列表</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active">
			<ul class="list-group">
				<li class="list-group-item">
					<div class="note note-info" style="margin-bottom: 0">
						<h4 class="block">Struts配置浏览</h4>
						<p>
							基于Struts2的config-browser插件浏览动态配置信息以辅助开发调试，详见官网说明： <a
								href="http://struts.apache.org/release/2.3.x/docs/config-browser-plugin.html" target="_blank">http://struts.apache.org/release/2.3.x/docs/config-browser-plugin.html</a>
						</p>
						<p>
							<a href="${base}/config-browser/index" class="btn blue" target="_blank">点击新开页面浏览Struts配置信息</a>
						</p>
					</div>
				</li>
				<li class="list-group-item">
					<div class="note note-info" style="margin-bottom: 0">
						<h4 class="block">H2 Server管理服务</h4>
						<p>如果应用是以H2数据库模式运行，则可启动H2 Server管理服务，访问H2 Console控制台进行数据查询管理。</p>
						<p>1. 首先点击:</p>
						<p>
							<a href="javascript:;" class="btn blue btn-h2-start">加载H2 Server管理服务</a>
						</p>
						<div class="h2-info hide">
							<p>2. 点击新开页面:</p>
							<p>
								<a href="javascript:;" class="btn blue btn-h2-open" target="_blank">访问H2 Console控制台</a>
							</p>
							<p>3. 拷贝如下相关信息填写到新开的H2 Console控制台页面:</p>
							<div class="form-group">
								<label class="control-label">Driver Class：</label>
								<div class="controls">
									<p class="form-control-static h2-driver-class">org.h2.Driver</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">JDBC URL：</label>
								<div class="controls">
									<p class="form-control-static h2-jdbc-url"></p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">User Name：</label>
								<div class="controls">
									<p class="form-control-static h2-user-name">sa</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">Password：</label>
								<div class="controls">
									<p class="form-control-static h2-user-password">[留空]</p>
								</div>
							</div>
							<p>4. 为了释放资源，建议使用完H2 Console管理端后，点击如下：</p>
							<p>
								<a href="javascript:;" class="btn blue btn-h2-stop">关闭H2 Server管理服务</a>
							</p>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
</div>
<script type="text/javascript">
    $(function() {
        $(".btn-h2-start").click(function() {
            $(this).ajaxPostURL({
                url : WEB_ROOT + "/sys/util!startH2",
                success : function(data) {
                    $(".h2-jdbc-url").html(data.userdata.h2JdbcUrl);
                    $(".btn-h2-open").attr("href", data.userdata.h2LoginUrl);
                    $(".h2-info").removeClass("hide");
                },
                confirmMsg : false
            })
        });

        $(".btn-h2-stop").click(function() {
            $(this).ajaxPostURL({
                url : WEB_ROOT + "/sys/util!stopH2",
                success : function(data) {
                    $(".h2-info").addClass("hide");
                },
                confirmMsg : false
            })
        })
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>