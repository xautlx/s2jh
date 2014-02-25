<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li class="active"><a class="tab-default" data-toggle="tab" href="#tab-auto">列表查询</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane fade active in">
			<div class="row search-form-default">
				<div class="col-md-12">
					<form action="#" method="get" class="form-inline form-validation form-search form-search-init"
						data-grid-search=".grid-${full_entity_name_field}">
						<div class="input-group">
							<div class="input-cont">
								<input type="text" name="search['CN_code']" class="form-control" placeholder="代码...">
							</div>
							<span class="input-group-btn">
								<button class="btn green" type="submmit">
									<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
								</button>
								<button class="btn default hidden-inline-xs" type="reset">
									<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
								</button>
							</span>
						</div>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="grid-${full_entity_name_field}"></table>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
    $(function() {
        $(".grid-${full_entity_name_field}").data("gridOptions", {
            url : '${base}${model_path}/${entity_name_field}!findByPage',
            colNames : [ '流水号'<#list entityFields as entityField><#if entityField.list>,'${entityField.title}'</#if></#list>],
	        colModel : [ {
	            name : 'displayId',
	            index : 'id'                            
	        <#list entityFields as entityField> 
	        <#if entityField.list>    
	        <#if entityField.enumField>
	        }, {
                stype : 'select',
                searchoptions : {
                    value : $.parseJSON('<s:property value="%{convertToJson(#application.enums.${entityField.fieldName}Enum)}" escape="false"/>')
                }
	        <#elseif entityField.fieldType=='Entity'>
	        }, {
	            name : '${entityField.fieldName}.display',
	            index : '${entityField.fieldName}',
	        <#else>    
	        }, {
	            name : '${entityField.fieldName}',
	        </#if>              
	        <#if entityField.listWidth!=0>  
	            width : ${entityField.listWidth},
	        </#if>               
	        <#if entityField.listHidden>    
	            hidden : true,
	        </#if>  
	        <#if entityField.fieldType=='Boolean'>          
	            formatter : booleanFormatter,
	        </#if>  
	        <#if entityField.fieldType=='Date'>          
	            sorttype: 'date',
	        </#if>
	        <#if entityField.fieldType=='BigDecimal'>          
	            sorttype: 'number',
	        </#if>                                                                       
	            align : '${entityField.listAlign}'
	        </#if>
	        </#list>
	        } ],
	        <#if fetchJoinFields?exists>
	        postData: {
	           <#list fetchJoinFields?keys as key> 
	           "search['FETCH_${key}']" : "${fetchJoinFields[key]}"<#if (key_has_next)>,</#if>
	           </#list>
	        },
	        </#if>  
            editurl : "${base}${model_path}/${entity_name_field}!doSave",
            delurl : "${base}${model_path}/${entity_name_field}!doDelete",
            fullediturl : "${base}${model_path}/${entity_name_field}!inputTabs"
        });
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>
