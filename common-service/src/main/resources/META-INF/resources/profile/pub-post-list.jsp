<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="portlet box green " id="portlet-pubpostlist">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-bell-o"></i> 最近公告消息<span class="badge badge-warning"><s:property value="#request.tasks.size()" /></span>
		</div>
		<div class="tools">
			<a href="" class="reload"></a>
		</div>
		<div class="actions">
			<div class="btn-group">
				<a data-close-others="true" data-hover="dropdown" data-toggle="dropdown" href="javascript:;"
					class="btn btn-sm green"> 过滤 <i class="fa fa-angle-down"></i>
				</a>
				<div class="dropdown-menu hold-on-click dropdown-checkboxes pull-right">
					<label><input type="checkbox" checked="true" id="chk-pub-post-read" /> 未读</label> <label><input
						type="checkbox" checked="true" id="chk-pub-post-unread" /> 已读</label>
				</div>
			</div>
		</div>
	</div>
	<div class="portlet-body">
		<div class="scroller" style="height: 300px;" data-always-visible="1" data-rail-visible="0">
			<ul class="feeds" id="dashboard-pub-post-list">
				<s:iterator value="%{model}" status="s" var="item">
					<li data-read="<s:property value="%{#item.extraAttributes['readed']}"/>"><a
						href='${base}/profile/pub-post!view?id=<s:property value="%{#item.id}"/>'
						data-toggle="<s:property value="%{#item.internal?'modal-ajaxify':''}"/>" target="_blank" title="查看公告">
							<div class="col1">
								<div class="cont">
									<div class="cont-col1">
										<s:if test="#item.extraAttributes['readed']">
											<div class="label label-sm label-default">
												<i class="fa fa-star"></i>
											</div>
										</s:if>
										<s:else>
											<div class="label label-sm label-success">
												<i class="fa fa-star-o"></i>
											</div>
										</s:else>
									</div>
									<div class="cont-col2">
										<div class="desc">
											<s:property value="#item.htmlTitle" escapeHtml="false" />
										</div>
									</div>
								</div>
							</div>
							<div class="col2">
								<div class="date">
									<s:date name="#item.publishTime" format="MM-dd HH:mm" />
								</div>
							</div>
					</a></li>
				</s:iterator>
			</ul>
		</div>
		<div class="scroller-footer">
			<div class="pull-right">
				<a href="javascript:;">查看全部... <i class="m-icon-swapright m-icon-gray"></i></a> &nbsp;
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
    $(function() {

        var $headerTaskBar = $("#header_message_bar");
        var newDropdownListCount = $("#dashboard-pub-post-list > li[data-read='false']").size();
        var $badgeCount = $headerTaskBar.find(".badge-count");
        var curTasksCount = $badgeCount.html();
        if (curTasksCount == '') {
            curTasksCount = '0';
        }
        if (newDropdownListCount == 0) {
            $badgeCount.hide();
        } else {
            $badgeCount.html(newDropdownListCount).show();
        }
        curTasksCount = Number(curTasksCount);
        if (newDropdownListCount > curTasksCount) {
            $headerTaskBar.pulsate({
                color : "#bf1c56",
                repeat : 5
            });
        }

        if (window.refreshMessagesTimer == null) {
            window.refreshMessagesTimer = window.setInterval(function() {
                $("#portlet-pubpostlist").find("> .portlet-title > .tools > a.reload ").click();
            }, 1000 * 60 * 50);
        }

        $("#chk-pub-post-read").click(function() {
            var $userTasks = $("#dashboard-pub-post-list").find("li[data-read='false']");
            if (this.checked) {
                $userTasks.show();
            } else {
                $userTasks.hide();
            }
        });

        $("#chk-pub-post-unread").click(function() {
            var $candidateTasks = $("#dashboard-pub-post-list").find("li[data-read='true']");
            if (this.checked) {
                $candidateTasks.show();
            } else {
                $candidateTasks.hide();
            }
        });
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>