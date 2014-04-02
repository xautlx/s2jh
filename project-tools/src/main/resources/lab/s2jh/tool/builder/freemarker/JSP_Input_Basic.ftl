<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation form-edit-${full_entity_name_field}"
	action="${base}${model_path}/${entity_name_field}!doSave" method="post">
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<button class="btn blue" type="submit" data-grid-reload=".grid-${full_entity_name_field}">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
        <#list entityFields as entityField>
        <#if entityField.edit>        
        <div class="row">
            <div class="col-md-6">
				<div class="form-group">
					<label class="control-label">${entityField.title}</label>
					<div class="controls">
		                <#if entityField.fieldType=='Boolean'>
		                <s3:radio name="${entityField.fieldName}" list="#application.enums.booleanLabel"/>
		                <#elseif entityField.enumField>
		                <s3:select name="${entityField.fieldName}" list="#application.enums.${entityField.uncapitalizeFieldType}"/>
		                <#elseif entityField.fieldType=='Date'>
		                <s3:datetextfield name="${entityField.fieldName}" format="date"/>                  
		                <#elseif entityField.fieldType=='LocalDate'>
		                <s3:datetextfield name="${entityField.fieldName}" format="date"/> 
		                <#elseif entityField.fieldType=='LocalDateTime'>
		                <s3:datetextfield name="${entityField.fieldName}" format="timestamp"/>                                             
		                <#elseif (entityField.fieldType=='String' && entityField.listWidth gt 255)>
		                <s3:textarea name="${entityField.fieldName}" rows="3"/>                                             
		                <#else>
		                <s3:textfield name="${entityField.fieldName}" />
		                </#if>
					</div>
				</div>
            </div>
        </div>
        </#if>
        </#list>  
	</div>
	<div class="form-actions right">
		<button class="btn blue" type="submit" data-grid-reload=".grid-${full_entity_name_field}">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<script type="text/javascript">
    $(function() {
        $(".form-edit-${full_entity_name_field}").data("formOptions", {
            bindEvents : function() {
                var $form = $(this);
            }
        });
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>