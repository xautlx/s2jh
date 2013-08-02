<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    $(function() {
        $("#reportParamListDiv<s:property value='#parameters.id'/>").grid({
            url : "${base}/rpt/report-param!findByPage?search['EQ_reportDef.id']=<s:property value='%{#parameters.id}'/>",
            colNames : [ '操作', '代码', '名称', '必填项', '隐藏标识', '禁用标识', '参数类型', '前端UI校验规则', '缺省参数值', '是否允许多选 ', '集合数据源 ', '排序号', '创建时间', '版本号' ],
            colModel : [ {
                name : 'operation',
                align : 'center',
                fixed : true,
                sortable : false,
                hidedlg : true,
                search : false,
                width : 25,
                formatter : function(cellValue, options, rowdata, action) {
                    return $.jgrid.buildButtons([ {
                        title : "编辑",
                        icon : "icon-pencil",
                        onclick : "$('#" + $(this).attr("id") + "').jqGrid('editRow','" + rowdata.id + "')"
                    } ]);
                }
            }, {
                name : 'code',
                align : 'left',
                hidedlg : true,
                width : 100
            }, {
                name : 'title',
                align : 'left'
            }, {
                name : 'required',
                formatter : booleanFormatter
            }, {
                name : 'hidden',
                formatter : booleanFormatter                
            }, {
                name : 'disabled',
                formatter : booleanFormatter
            }, {
                name : 'type.title',
                index : 'type',
                width : 80,
                align : 'left',
                stype : 'select',
                searchoptions : {
                    value : enumsContainer['dynamicParameterTypeEnum'],
                }
            }, {
                name : 'validateRules',
                align : 'left'
            }, {
                name : 'defaultValue',
                align : 'left'
            }, {
                name : 'multiSelectFlag',
                formatter : booleanFormatter
            }, {
                name : 'listDataSource',
                align : 'left'
            }, {
                name : 'orderRank',
                width : 60,
                fixed : true,
                align : 'right'
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
                url : "${base}/rpt/report-param!doDelete"
            },
            addRow : {
                url : "${base}/rpt/report-param!create?reportDefId=<s:property value='%{#parameters.id}'/>"
            },
            editRow : {
                url : "${base}/rpt/report-param!update?reportDefId=<s:property value='%{#parameters.id}'/>",
                labelCol : 'code'
            },
            caption : "报表参数列表"
        });

        $("#reportParamAddBtn<s:property value='#parameters.id'/>").click(function() {
            $("#reportParamListDiv<s:property value='#parameters.id'/>").jqGrid('addRow');
        });

        $("#reportParamDeleteBtn<s:property value='#parameters.id'/>").click(function() {
            $("#reportParamListDiv<s:property value='#parameters.id'/>").jqGrid('delRow');
        });
    });
</script>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="toolbar">
			<div class="toolbar-inner">
				<button type="button" class="btn" id="reportParamAddBtn<s:property value='#parameters.id'/>">
					<i class="icon-plus-sign"></i> 添加
				</button>
				<button type="button" class="btn" id="reportParamDeleteBtn<s:property value='#parameters.id'/>">
					<i class="icon-trash"></i> 删除
				</button>
			</div>
		</div>
	</div>
	<div class="row-fluid">
		<table id="reportParamListDiv<s:property value='#parameters.id'/>"></table>
		<div id="reportParamListDiv<s:property value='#parameters.id'/>Pager"></div>
	</div>
</div>

