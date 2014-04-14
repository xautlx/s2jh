$(function() {
    $(".form-${full_entity_name_field}-inputBasic").data("formOptions", {
        bindEvents : function() {
            var $form = $(this);
        }
    });
});