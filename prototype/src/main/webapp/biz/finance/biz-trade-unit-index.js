$(function() {
    $(".grid-biz-finance-biz-trade-unit-index").data("gridOptions", {
        url : WEB_ROOT + '/biz/finance/biz-trade-unit!findByPage',
        colModel : [ {
            label : '名称',
            name : 'name',
            width : 120,
            editable : true,
            editoptions : {
                spellto : 'code'
            },
            align : 'left'
        }, {
            label : '代码',
            name : 'code',
            width : 60,
            editable : true,
            align : 'left'
        }, {
            label : '单位类型',
            name : 'type',
            formatter : 'select',
            searchoptions : {
                value : Util.getCacheEnumsByType('bizTradeUnitTypeEnum')
            },
            width : 80,
            editable : true,
            align : 'center'
        }, {
            label : '办公电话',
            name : 'officePhone',
            width : 80,
            editable : true,
            align : 'left'
        }, {
            label : '移动电话',
            name : 'mobilePhone',
            width : 80,
            editable : true,
            align : 'left'
        }, {
            label : '所在地址',
            name : 'addr',
            width : 200,
            editable : true,
            align : 'left'
        } ],
        editurl : WEB_ROOT + '/biz/finance/biz-trade-unit!doSave',
        editrulesurl : WEB_ROOT + '/biz/finance/biz-trade-unit!buildValidateRules',
        delurl : WEB_ROOT + '/biz/finance/biz-trade-unit!doDelete',
        fullediturl : WEB_ROOT + '/biz/finance/biz-trade-unit!inputTabs'
    });
});
