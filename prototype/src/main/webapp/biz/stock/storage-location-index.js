$(function() {
    $(".grid-biz-stock-storage-location-index").data("gridOptions", {
        url : WEB_ROOT + '/biz/stock/storage-location!findByPage',
        colModel : [ {
            label : '编码',
            name : 'code',
            width : 100,
            editable : true,
            align : 'center'
        }, {
            label : '名称',
            name : 'title',
            width : 128,
            editable : true,
            align : 'left'
        }, {
            label : '地址',
            name : 'addr',
            width : 200,
            editable : true,
            align : 'left'
        } ],
        editurl : WEB_ROOT + '/biz/stock/storage-location!doSave',
        delurl : WEB_ROOT + '/biz/stock/storage-location!doDelete'
    });
});
