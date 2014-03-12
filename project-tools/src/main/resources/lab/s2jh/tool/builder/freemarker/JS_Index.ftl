$(function() {
    $(".grid-${full_entity_name_field}").data("gridOptions", {
        url : WEB_ROOT + '${model_path}/${entity_name_field}!findByPage',
        colModel : [ {
            label : '流水号',
            name : 'displayId',
            index : 'id'                            
        <#list entityFields as entityField> 
        <#if entityField.list>    
        <#if entityField.enumField>
        }, {
            stype : 'select',
            searchoptions : {
                value : Util.getCacheEnumsByType('${entityField.fieldName}Enum')
            }
        <#elseif entityField.fieldType=='Entity'>
        }, {
            label : '${entityField.title}',
            name : '${entityField.fieldName}.display',
            index : '${entityField.fieldName}',
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
        editurl : WEB_ROOT + '${model_path}/${entity_name_field}!doSave',
        delurl : WEB_ROOT + '${model_path}/${entity_name_field}!doDelete',
        fullediturl : WEB_ROOT + '${model_path}/${entity_name_field}!inputTabs'
    });
});
