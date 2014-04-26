$(function() {
    $(".grid-${full_entity_name_field}-index").data("gridOptions", {
        url : WEB_ROOT + '${model_path}/${entity_name_field}!findByPage',
        colModel : [ {
            label : '流水号',
            name : 'id'                            
        <#list entityFields as entityField> 
        <#if entityField.list>    
        <#if entityField.enumField>
        }, {
            label : '${entityField.title}',
            name : '${entityField.fieldName}',
            formatter : 'select',
            searchoptions : {
                value : Util.getCacheEnumsByType('${entityField.fieldName}Enum')
            },
        <#elseif entityField.fieldType=='Entity'>
        }, {
            label : '${entityField.title}',
            name : '${entityField.fieldName}.display',
            index : '${entityField.fieldName}',
            width : 200,
        <#else>    
        }, {
            label : '${entityField.title}',
            name : '${entityField.fieldName}',
        </#if>              
        <#if entityField.listWidth!=0>  
            width : ${entityField.listWidth},
        </#if>               
        <#if entityField.listHidden>    
            hidden : true,
        </#if>  
        <#if entityField.fieldType=='Boolean'>          
            formatter : 'checkbox',
        </#if>  
        <#if entityField.fieldType=='Date'>          
            formatter: 'date',
        </#if>
        <#if entityField.fieldType=='BigDecimal'>          
            formatter: 'number',
        </#if> 
        <#if entityField.edit>
            editable: true,
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
        editurl : WEB_ROOT + '${model_path}/${entity_name_field}!doSave',
        editrulesurl : WEB_ROOT + '${model_path}/${entity_name_field}!buildValidateRules',
        delurl : WEB_ROOT + '${model_path}/${entity_name_field}!doDelete',
        fullediturl : WEB_ROOT + '${model_path}/${entity_name_field}!inputTabs'
    });
});
