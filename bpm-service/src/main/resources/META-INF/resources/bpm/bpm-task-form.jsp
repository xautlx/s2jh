<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation" action="${base}/bpm/bpm-task!complete"
	method="post">
	<input type="hidden" name="id" value="<s:property value='%{#parameters.id}'/>" />
	<s:iterator value="#request.taskFormData.formProperties" var="item">
		<s:if test="#item.writable">
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label col-md-3"><s:property value='%{#item.name}' /></label>
						<div class="col-md-9">
							<s:if test="#item.type.name=='string'">
								<s:textarea ame="%{'fp_'+#item.id}" rows="3" requiredLabel="%{#item.required}" cssClass="form-control" />
							</s:if>
							<s:elseif test="#item.type.name=='boolean'">
								<s:radio name="%{'fp_'+#item.id}" list="#application.enums.booleanLabel" requiredLabel="%{#item.required}" />
							</s:elseif>
						</div>
					</div>
				</div>
			</div>
		</s:if>
		<s:else>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label col-md-3"><s:property value='%{#item.name}' /></label>
						<div class="col-md-9">
							<s:property value="%{#item.value}" />
						</div>
					</div>
				</div>
			</div>
		</s:else>
	</s:iterator>
	<div class="form-actions right">
		<button class="btn blue" type="submit">
			<i class="fa fa-check"></i> 完成任务
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<%@ include file="/common/ajax-footer.jsp"%>