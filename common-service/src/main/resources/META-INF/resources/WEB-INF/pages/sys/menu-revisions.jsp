<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    $().ready(function() {
        $("#revisionsSearchForm").formvalidate({
            initSubmit : true,
            submitHandler : function(form) {
                $("#revsListDiv").grid({
                    queryForm : $(form),
                    colNames : [ '操作', '版本号', '操作时间', '操作类型', '操作人员' ],
                    colModel : [ {
                        name : 'operation',
                        align : 'center',
                        fixed : true,
                        sortable : false,
                        hidedlg : true,
                        width : 25,
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildButtons([ {
                                title : "编辑",
                                icon : "icon-pencil",
                                onclick : "$('#" + $(this).attr("id") + "').jqGrid('editRow','" + rowdata.id + "')"
                            } ]);
                        }
                    }, {
                        name : 'revisionEntity.rev',
                        align : 'left',
                        width : 80,
                        fixed : true,
                        align : 'center',
                        formatter : function(cellValue, options, rowdata, action) {
                            link = '<a href="javascript:void(0)" title="查看" onclick="$.popupViewDialog(\'${base}/auth/role!viewTabs?id=' + options.rowId + '\')">' + cellValue + '</a>';
                            return link;
                        }
                    }, {
                        name : 'revisionEntity.revstmp',
                        width : 140,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'revisionType',
                        width : 100,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'revisionEntity.userPin'
                    } ],
                    ondblClickEnabledRow : function(rowid, iRow, iCol, e, rowdata) {
                        $("#roleIndexTabs").tabs("add", '${base}/auth/role!inputTabs?id=' + rowid, "编辑-" + eraseCellValueLink(rowdata.code));
                    },
                    caption : "角色列表"
                });
            }
        });
    });
</script>
<div class="container-fluid">
    <s2:form id="revisionsSearchForm" action="/sys/menu!revisions" method="get"
        cssClass="form-inline">
        <s:hidden name="id" value="%{#parameters.id}" />
        <div class="row-fluid">
            <div class="toolbar">
                <div class="toolbar-inner">
                    <div class="input-prepend">
                        <s:textfield name="search['CN_code_OR_title']" cssClass="input-large" title="代码/名称" />
                    </div>
                    <button type="submit" class="btn btn_submit">
                        <i class="icon-search"></i> 查询
                    </button>
                    <button type="reset" class="btn">
                        <i class="icon-repeat"></i> 重置
                    </button>
                </div>
            </div>
    </s2:form>
    <table id="revsListDiv"></table>
    <div id="revsListDivPager"></div>
</div>