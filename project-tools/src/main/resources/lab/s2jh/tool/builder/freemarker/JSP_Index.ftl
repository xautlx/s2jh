<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
    <div class="container-fluid">
        <s2:tabbedpanel id="${entity_name_uncapitalize}IndexTabs">
            <ul>
                <li><a href="#${entity_name_uncapitalize}IndexListTab"><span>列表查询</span></a></li>
            </ul>
            <div id="${entity_name_uncapitalize}IndexListTab">
                <div class="row-fluid">
                    <div class="toolbar">
                        <div class="toolbar-inner">
                            <button type="button" class="btn" id="${entity_name_uncapitalize}AddBtn">
                                <i class="icon-plus-sign"></i> 添加
                            </button>
                            <button type="button" class="btn" id="${entity_name_uncapitalize}DeleteBtn">
                                <i class="icon-trash"></i> 删除
                            </button>
                            <div class="btn-group pull-right">
                                <button type="button" class="btn" title="高级查询"
                                    onclick="$('#${entity_name_uncapitalize}ListDiv').jqGrid('advSearch');">
                                    <i class="icon-search"></i>
                                </button>
                            </div>                            
                        </div>
                    </div>
                </div>
                <div class="row-fluid">
                    <table id="${entity_name_uncapitalize}ListDiv"></table>
                    <div id="${entity_name_uncapitalize}ListDivPager"></div>
                </div>
            </div>
        </s2:tabbedpanel>
    </div>                    
	<%@ include file="/common/index-footer.jsp"%>
    <script type="text/javascript">
        $(function() {
            $("#${entity_name_uncapitalize}ListDiv").grid({
                url: '${base}${model_path}/${entity_name_field}!findByPage',
                colNames : [ '操作',<#list entityFields as entityField><#if entityField.list><#if entityField_index != 0>,</#if>'${entityField.title}'</#if></#list>,'创建时间','版本号'],
                colModel : [ {
                    name : 'operation',
                    align : 'center',
                    fixed : true,
                    sortable : false,
                    hidedlg : true,
                    search : false,
                    width : 40,
                    formatter : function(cellValue, options, rowdata, action) {
                        return $.jgrid.buildButtons([ {
                            title : "编辑",
                            icon : "icon-pencil",
                            onclick : "$('#" + $(this).attr("id") + "').jqGrid('editRow','" + rowdata.id + "')"
                        }, {
                            title : "查看",
                            icon : "icon-book",
                            onclick : "$.popupViewDialog('${base}${model_path}/${entity_name_field}!viewTabs?id=" + options.rowId + "')"
                        } ]);
                    }                 
                <#list entityFields as entityField> 
                <#if entityField.list>                      
                }, {
                    name : '${entityField.fieldName}',
                <#if entityField.listWidth!=0>  
                    width : ${entityField.listWidth},
                </#if>
                <#if entityField.listFixed>
                    fixed : true,
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
                }, {
                   name : 'createdDate',
                   width : 120,
                   fixed : true,
                   hidden : true,
                   align : 'center'
                }, {
                   name : 'version',
                   hidden : true,
                   hidedlg : true
                } ],
                delRow : {
                    url : "${base}${model_path}/${entity_name_field}!doDelete"
                },
                addRow : {
                    url : "${base}${model_path}/${entity_name_field}!inputTabs"
                },
                editRow : {
                    url : "${base}${model_path}/${entity_name_field}!inputTabs",
                    labelCol : 'TODO'
                },                
                caption:"${model_title}列表"
            }); 
            
            $("#${entity_name_uncapitalize}AddBtn").click(function() {
                $("#${entity_name_uncapitalize}ListDiv").jqGrid('addRow');
            });
            
            $("#${entity_name_uncapitalize}DeleteBtn").click(function() {
                $("#${entity_name_uncapitalize}ListDiv").jqGrid('delRow');
            });                         
         });
    </script>	
</body>
</html>
