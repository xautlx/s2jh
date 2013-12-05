<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s2:form cssClass="form-horizontal" method="post" 
      action="%{persistentedModel?'${entity_name_field}!doUpdate':'${entity_name_field}!doCreate'}">
	<s:hidden name="id" />
	<s:hidden name="version" />
    <s:token/>
    <div class="toolbar">
        <div class="toolbar-inner">
            <button type="button" class="btn btn-submit" callback-tab="${entity_name_uncapitalize}IndexTabs"
                callback-grid="${entity_name_uncapitalize}ListDiv">
                <i class="icon-ok"></i> 保存
            </button>
            <button type="button" class="btn btn-submit submit-post-close" callback-tab="${entity_name_uncapitalize}IndexTabs"
                callback-grid="${entity_name_uncapitalize}ListDiv">
                <i class="icon-check"></i> 保存并关闭
            </button>
            <button type="reset" class="btn">
                <i class="icon-repeat"></i> 重置
            </button>
        </div>
    </div>
    <s2:div cssClass="well" stretch="true">
        <#list entityFields as entityField>
        <#if entityField.edit>        
        <div class="row-fluid">
            <div class="span6">
                <#if entityField.fieldType=='Boolean'>
                <s2:radio name="${entityField.fieldName}" list="#application.enums.booleanLabel" label="${entityField.title}"/>
                <#elseif entityField.enumField>
                <s:select name="${entityField.fieldName}" list="#application.enums.${entityField.uncapitalizeFieldType}" label="${entityField.title}"/>
                <#elseif entityField.fieldType=='Date'>
                <s2:datetextfield name="${entityField.fieldName}" cssClass="input-small" label="${entityField.title}" format="date"/>                  
                <#elseif entityField.fieldType=='LocalDate'>
                <s2:datetextfield name="${entityField.fieldName}" cssClass="input-small" label="${entityField.title}" format="date"/> 
                <#elseif entityField.fieldType=='LocalDateTime'>
                <s2:datetextfield name="${entityField.fieldName}" cssClass="input-medium" label="${entityField.title}" format="timestamp"/>                                             
                <#elseif (entityField.fieldType=='String' && entityField.listWidth gt 255)>
                <s2:textarea name="${entityField.fieldName}" label="${entityField.title}" rows="3"/>                                             
                <#else>
                <s2:textfield name="${entityField.fieldName}" label="${entityField.title}" />
                </#if>
            </div>
        </div>
        </#if>
        </#list>            
    </s2:div>   
</s2:form>
