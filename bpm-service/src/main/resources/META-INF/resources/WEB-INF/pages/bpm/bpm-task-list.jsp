<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul id="userTasks" class="nav nav-tabs nav-stacked" style="margin-bottom: 0px;">
	<s:iterator value="#request.tasks" status="s" var="item">
		<li><a href="${base}/bpm/bpm-task!show?id=<s:property value='#item.id' />"
			tname="<s:property value='#item.name' />" tid="<s:property value='#item.id' />">
				<div class="row-fluid">
					<div class="span1" align="center">
						<s:property value="#s.count" />
					</div>
					<div class="span7">
						<s:property value="#item.pdname" />
						-
						<s:property value="#item.name" />
					</div>
					<div class="span3" title="任务创建时间">
						<s:property value="#item.createTime" />
					</div>
					<div class="span1" title="任务类型">
						<s:if test="%{#item.needClaim}">
						参与
					</s:if>
						<s:else>
						个人
					</s:else>
					</div>
				</div>
		</a></li>
	</s:iterator>
</ul>
<script>
    $(function() {
        $("#userTasks").delegate("li a", "click", function() {
            var a = $(this);
            parent.addPanelTab({
                id : a.attr("tid"),
                title : a.attr("tname"),
                url : a.attr("href")
            });
            return false;
        });
    });
</script>