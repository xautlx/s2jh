var refreshTasksTimer;

$(function() {

    //console.profile('Profile Sttart');

    var $headerTaskBar = $("#header_task_bar");
    var newDropdownTaskListCount = $("#dashboard-task-list > li").size();
    var $badgeTasksCount = $headerTaskBar.find(".badge-count");
    var curTasksCount = $badgeTasksCount.html();
    if (curTasksCount == '') {
        curTasksCount = '0';
    }
    if (newDropdownTaskListCount == 0) {
        $badgeTasksCount.hide();
    } else {
        $badgeTasksCount.html(newDropdownTaskListCount).show();
    }
    curTasksCount = Number(curTasksCount);
    if (newDropdownTaskListCount > curTasksCount) {
        $headerTaskBar.pulsate({
            color : "#bf1c56",
            repeat : 5
        });
    }

    if (window.refreshTasksTimer == null) {
        window.refreshTasksTimer = window.setInterval(function() {
            $("#portlet-tasks").find("> .portlet-title > .tools > a.reload ").click();
        }, 1000 * 60 * 10);
    }

    $(".chk-task").click(function() {
        var claim = $(this).hasClass("chk-task-candidate");
        var $tasks = $("#dashboard-task-list").find("li[need-claim='" + claim + "']");
        if (this.checked) {
            $tasks.show();
        } else {
            $tasks.hide();
        }
    });

    $("#txtQuickFilterTasks").keyup(function() {
        var $el = $(this);
        var val = $el.val();
        var $tasks = $("#dashboard-task-list").find("li");
        $tasks.each(function() {
            if (val == "") {
                $(this).show();
            } else {
                val = val.toLowerCase();
                var desc = $(this).find(".desc").text() + $(this).find(".biz-key").text();
                desc = desc.toLowerCase();
                if (desc.indexOf(val) > -1) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            }
        })
    });

    //console.profileEnd();
});