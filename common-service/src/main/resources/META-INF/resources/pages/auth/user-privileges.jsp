<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    $(function() {

        $("#userRelatedPrivilegeListDiv<s:property value='#parameters.id'/>").grid({
            url : "${base}/auth/user!privileges?id=<s:property value='#parameters.id'/>",
            colNames : [ '代码', '名称', '类别', 'URL', '排序号' ],
            colModel : [ {
                name : 'code',
                align : 'left',
                fixed : true,
                width : 80,
                formatter : function(cellValue, options, rowdata, action) {
                    return $.jgrid.buildLink({
                        text : cellValue,
                        onclick : "$.popupViewDialog('${base}/auth/privilege!viewTabs?id=" + options.rowId + "')"
                    });
                }
            }, {
                name : 'title',
                align : 'left'
            }, {
                name : 'category',
                align : 'left'
            }, {
                name : 'url',
                align : 'left'
            }, {
                name : 'orderRank',
                width : 60,
                sorttype : 'number'
            } ],
            cmTemplate: {
              sortable: false  
            },
            multiselect: false,
            loadonce: true,
            grouping : true,
            groupingView : {
                groupField : [ 'category' ],
                groupOrder : [ 'asc' ],
                groupCollapse : false
            }
        });

    });
</script>
<div class="container-fluid">
	<div class="row-fluid">
		<table id="userRelatedPrivilegeListDiv<s:property value='#parameters.id'/>"></table>
		<div id="userRelatedPrivilegeListDiv<s:property value='#parameters.id'/>Pager"></div>
	</div>
</div>