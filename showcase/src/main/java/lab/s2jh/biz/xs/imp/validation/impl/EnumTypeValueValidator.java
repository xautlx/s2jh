package lab.s2jh.biz.xs.imp.validation.impl;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lab.s2jh.biz.sys.entity.EnumValue;
import lab.s2jh.biz.sys.entity.EnumType.EnumTypes;
import lab.s2jh.biz.sys.service.EnumValueService;
import lab.s2jh.biz.xs.imp.validation.EnumTypeValue;
import lab.s2jh.core.context.SpringContextHolder;

import org.apache.commons.lang3.StringUtils;


public class EnumTypeValueValidator implements ConstraintValidator<EnumTypeValue, Object> {

    private EnumTypes enumType;

    @Override
    public void initialize(EnumTypeValue constraintAnnotation) {
        this.enumType = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String data = String.valueOf(value);
        if (StringUtils.isBlank(data)) {
            return true;
        }
        EnumValueService enumValueService = SpringContextHolder.getBean(EnumValueService.class);
        List<EnumValue> enumValues = enumValueService.findByEnumType(enumType.name());
        for (EnumValue enumValue : enumValues) {
            if (data.equals(enumValue.getCode()) || data.equals(enumValue.getLabel())) {
                return true;
            }
        }
        return false;
    }
}
