<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="form-horizontal form-bordered form-label-stripped control-label-xl">
	<s:iterator value="#request.variables" var="item">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label"><s:property value='%{#item.key}' /></label>
					<div class="controls">
						<p class="form-control-static">
							<s:property value='%{#item.value}' />
						</p>
					</div>
				</div>
			</div>
		</div>
	</s:iterator>
</div>
<%@ include file="/common/ajax-footer.jsp"%>