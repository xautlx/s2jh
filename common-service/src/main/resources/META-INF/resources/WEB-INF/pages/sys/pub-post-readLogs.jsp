<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid">
	<div class="row-fluid">
		<table id="pubPostReadLogsListDiv<s:property value='#parameters.id'/>"></table>
		<div id="pubPostReadLogsListDiv<s:property value='#parameters.id'/>Pager"></div>
	</div>
</div>
<%@ include file="/common/index-footer.jsp"%>
<script type="text/javascript">
    $(function() {
        $("#pubPostReadLogsListDiv<s:property value='#parameters.id'/>").grid({
            url : "${base}/sys/pub-post-read!findByPage?search['EQ_pubPost.id']=<s:property value='#parameters.id'/>",
            colNames : [ '阅读用户', '首次阅读时间', '最后阅读时间', '总计阅读次数', '创建时间', '版本号' ],
            colModel : [ {
                name : 'readUserLabel'
            }, {
                name : 'firstReadTime',
                sorttype : 'date'
            }, {
                name : 'lastReadTime',
                sorttype : 'date'
            }, {
                name : 'readTotalCount',
                width : 100,
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
            caption : "公告阅读记录列表"
        });
    });
</script>