$(function() {
    $(".grid-sys-pub-post-index").data("gridOptions", {
        url : WEB_ROOT + "/sys/pub-post!findByPage",
        colNames : [ '阅读人数', '标题', '外部链接', '前端显示', '后端显示', '排序号', '生效时间', '到期时间' ],
        colModel : [ {
            name : 'readUserCount',
            width : 60,
            fixed : true,
            align : 'center'
        }, {
            name : 'htmlTitle',
            editable : true,
            align : 'left'
        }, {
            name : 'externalLink',
            editable : true,
            align : 'left'
        }, {
            name : 'frontendShow',
            width : 60,
            editable : true,
            edittype : 'checkbox'
        }, {
            name : 'backendShow',
            width : 60,
            editable : true,
            edittype : 'checkbox'
        }, {
            name : 'orderRank',
            width : 60,
            sorttype : 'number',
            editable : true
        }, {
            name : 'publishTime',
            fixed : true,
            sorttype : 'date',
            editable : true,
            editoptions : {
                time : true
            },
            align : 'center'
        }, {
            name : 'expireTime',
            fixed : true,
            sorttype : 'date',
            editable : true,
            editoptions : {
                time : true
            },
            align : 'center'
        } ],
        editcol : 'htmlTitle',
        editurl : WEB_ROOT + "/sys/pub-post!doSave",
        delurl : WEB_ROOT + "/sys/pub-post!doDelete",
        fullediturl : WEB_ROOT + "/sys/pub-post!edit",
        subGrid : true,
        subGridRowExpanded : function(subgrid_id, row_id) {
            Grid.initSubGrid(subgrid_id, row_id, {
                url : WEB_ROOT + "/sys/pub-post-read!findByPage?search['EQ_pubPost.id']=" + row_id,
                colNames : [ '阅读用户', '首次阅读时间', '最后阅读时间', '总计阅读次数' ],
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
                } ]
            });
        }
    });
});