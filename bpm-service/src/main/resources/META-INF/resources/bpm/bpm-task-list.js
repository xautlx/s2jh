function clkChkTask(claim) {
    var $tasks = $("#dashboard-task-list").find("li[need-claim='" + claim + "']");
    if (this.checked) {
        $tasks.show();
    } else {
        $tasks.hide();
    }
}
$(function() {

    //console.profile('Profile Sttart');

    var $headerTaskBar = $("#header_task_bar");
    var newDropdownTaskListCount = $("#dashboard-task-list > li").size();
    var $badgeTasksCount = $headerTaskBar.find(".badge-tasks-count");
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

    //console.profileEnd();
});