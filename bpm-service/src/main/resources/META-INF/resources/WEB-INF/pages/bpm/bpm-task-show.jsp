<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<div id="taskFormDiv"></div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#taskFormDiv").ajaxGetUrl("${base}<s:property value='#request.formKey' />");
        });
    </script>
</body>
</html>