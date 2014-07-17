$(function() {

    $(".form-biz-purchase-purchase-order-bpmPay").data("formOptions", {
        bindEvents : function() {
            var $form = $(this);

            $form.find("input[name='accountSubject.display']").treeselect({
                url : WEB_ROOT + "/biz/finance/account-subject!findPaymentAccountSubjects",
                callback : {
                    onSingleClick : function(event, treeId, treeNode) {
                        $form.setFormDatas({
                            'accountSubject.id' : treeNode.id,
                            'accountSubject.display' : treeNode.display
                        }, true);
                    },
                    onClear : function(event) {
                        $form.setFormDatas({
                            'accountSubject.id' : '',
                            'accountSubject.display' : ''
                        }, true);
                    }
                }
            });            
        }
    });
});
