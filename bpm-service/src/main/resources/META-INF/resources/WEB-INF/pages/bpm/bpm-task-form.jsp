<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="s2" uri="/struts2-tags"%>
<%
    pageContext.setAttribute("base", request.getContextPath());
%>

<s2:form cssClass="form-horizontal" method="post" action="/bpm/bpm-task!complete">
	<input type="hidden" name="id" value="<s:property value='%{#parameters.id}'/>" />
	<div class="row-fluid">
		<div class="toolbar">
			<div class="toolbar-inner">
				<button type="button" class="btn btn-submit">
					<i class="icon-ok"></i> 完成任务
				</button>
				<button type="reset" class="btn">
					<i class="icon-repeat"></i> 重置表单
				</button>
			</div>
		</div>
	</div>
	<s:iterator value="#request.taskFormData.formProperties" var="item">
		<s:if test="#item.writable">
			<div class="row-fluid">
				<div class="span12">
					<s:if test="#item.type.name=='string'">
						<s2:textarea ame="%{'fp_'+#item.id}" label="%{#item.name}" rows="3" requiredLabel="%{#item.required}"></s2:textarea>
					</s:if>
					<s:elseif test="#item.type.name=='boolean'">
						<s2:radio name="%{'fp_'+#item.id}" list="#application.enums.booleanLabel" label="%{#item.name}"
							requiredLabel="%{#item.required}" />
					</s:elseif>
				</div>
			</div>
		</s:if>
		<s:else>
			<div class="row-fluid">
				<div class="span12">
					<s2:property label="%{#item.name}" value="%{#item.value}" />
				</div>
			</div>
		</s:else>
	</s:iterator>

</s2:form>