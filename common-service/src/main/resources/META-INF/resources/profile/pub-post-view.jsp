<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="%{!model.internal}">
	<script type="text/javascript">
        window.location.href = '<s:property value="externalLink" />';
    </script>
</s:if>
<s:else>
	<div class="form-horizontal form-bordered form-label-stripped">
		<div class="form-body">
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">标题</label>
						<div class="controls">
							<p class="form-control-static">
								<s3:property value="htmlTitle" escapeHtml="false" />
							</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">内容</label>
						<div class="controls">
							<p class="form-control-static">
								<s3:property value="htmlContent" escapeHtml="false" />
							</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">发布时间</label>
						<div class="controls">
							<p class="form-control-static">
								<s3:date name="publishTime" format="timestamp" />
							</p>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">到期时间</label>
						<div class="controls">
							<p class="form-control-static">
								<s3:date name="expireTime" format="timestamp" />
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</s:else>