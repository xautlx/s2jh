$(function() {
    $(".grid-sys-config-property-index").data("gridOptions", {
        url : WEB_ROOT + '/sys/config-property!findByPage',
        colModel : [{
            label : '代码',
            name : 'propKey',
            width : 120,
            editable: true,
            align : 'left'
        }, {
            label : '名称',
            name : 'propName',
            width : 100,
            editable: true,
            align : 'left'
        }, {
            label : '简单属性值',
            name : 'simpleValue',
            width : 80,
            editable: true,
            align : 'left'
        }, {
            label : 'HTML属性值',
            name : 'htmlValue',
            width : 200,
            editable: true,
            align : 'left'
        }, {
            label : '参数属性用法说明',
            name : 'propDescn',
            width : 100,
            editable: true,
            align : 'left'
        } ],
        editurl : WEB_ROOT + '/sys/config-property!doSave',
        delurl : WEB_ROOT + '/sys/config-property!doDelete'
    });
});
