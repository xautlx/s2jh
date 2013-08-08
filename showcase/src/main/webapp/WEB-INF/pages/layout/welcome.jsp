<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<div class="hero-unit">A Java/J2EE development framework for enterprise system based on
			Struts/Spring/JPA/Hibernate and jquery/bootstrap</div>
		<div class="row-fluid">
			<div class="well">
				<fieldset>
					<legend>最新公告</legend>
					<div id="pubPostListShow"></div>
				</fieldset>
			</div>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script>
        $(function() {
            $("#pubPostListShow").ajaxGetUrl("${base}/profile/pub-post!list");
        });
    </script>
</body>
</html>