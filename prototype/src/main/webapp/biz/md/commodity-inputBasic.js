$(function() {
    $(".form-biz-md-commodity-inputBasic").data("formOptions", {
        bindEvents : function() {
            var $form = $(this);

            //条码扫描输入支持
            $form.find("input[name='barcode']").barcodeScanSupport();

            var $el = $form.find("input[name='defaultStorageLocation.id']");
            var $ajaxCacheContainer = $el.closest(".panel-content");
            var url = $el.attr("data-optionsurl");
            $el.select2({
                minimumInputLength : 0,
                ajax : {
                    cache : $ajaxCacheContainer.data(url) ? true : false,
                    url : url,
                    dataType : 'json',
                    results : function(data, page) {
                        $ajaxCacheContainer.data(url, "cached");
                        var results = $.map(data, function(n) {
                            return {
                                id : n.id,
                                text : n.display
                            };
                        })
                        return {
                            results : results
                        };
                    }
                },
                initSelection : function(element, callback) {
                    callback({
                        id : $(element).val(),
                        text : $(element).attr("data-display")
                    })
                }
            });
        }
    });
});