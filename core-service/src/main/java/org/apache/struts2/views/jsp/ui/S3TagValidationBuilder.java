package org.apache.struts2.views.jsp.ui;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lab.s2jh.core.web.PersistableController;
import lab.s2jh.core.web.json.DateJsonSerializer;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.struts2.components.UIBean;
import org.hibernate.validator.constraints.Email;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.opensymphony.xwork2.util.CompoundRoot;
import com.opensymphony.xwork2.util.ValueStack;

public class S3TagValidationBuilder {

    @SuppressWarnings("unchecked")
    public static void build(AbstractUITag tag, ValueStack stack, HttpServletRequest req, UIBean uiBean) {

        Map<String, Object> dynamicAttributes;
        try {
            dynamicAttributes = (Map<String, Object>) FieldUtils.readField(uiBean, "dynamicAttributes", true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        CompoundRoot rootList = stack.getRoot();
        Object controller = null;
        for (Object obj : rootList) {
            if (obj instanceof PersistableController) {
                controller = obj;
                break;
            }
        }
        if (controller == null) {
            return;
        }

        Class entityClass = null;
        PersistableController pc = (PersistableController) controller;
        Object genericClz = pc.getClass().getGenericSuperclass();
        if (genericClz instanceof ParameterizedType) {
            entityClass = (Class) ((ParameterizedType) genericClz).getActualTypeArguments()[0];
        }

        try {
            String tagName = tag.name;
            Assert.notNull(tagName, "'name' attribute for tag is required.");
            Method method = OgnlRuntime.getGetMethod((OgnlContext) stack.getContext(), entityClass, tagName);
            if (method == null) {
                String[] tagNameSplits = StringUtils.split(tagName, ".");
                if (tagNameSplits.length >= 2) {
                    Class<?> retClass = entityClass;
                    for (String tagNameSplit : tagNameSplits) {
                        if (tagNameSplit.equals("id")) {
                            break;
                        }
                        method = OgnlRuntime.getGetMethod((OgnlContext) stack.getContext(), retClass, tagNameSplit);
                        if (method != null) {
                            retClass = method.getReturnType();
                        }
                    }
                    if (method == null) {
                        retClass = controller.getClass();
                        for (String tagNameSplit : tagNameSplits) {
                            method = OgnlRuntime.getGetMethod((OgnlContext) stack.getContext(), retClass, tagNameSplit);
                            if (method != null) {
                                retClass = method.getReturnType();
                            }
                        }
                    }
                }
            }

            if (method != null) {
                Class<?> retType = method.getReturnType();
                Column column = method.getAnnotation(Column.class);

                if (column != null) {
                    if (column.nullable() == false) {
                        if (tag.requiredLabel == null) {
                            uiBean.setRequiredLabel("true");
                        }
                    }
                    if (column.unique() == true) {
                        setupDynamicAttribute(dynamicAttributes, "data-rule-unique", "true");
                    }
                    if (column.length() > 0 && retType == String.class && method.getAnnotation(Lob.class) == null) {
                        setupDynamicAttribute(dynamicAttributes, "maxlength", Integer.toString(column.length()));
                        setupDynamicAttribute(dynamicAttributes, "data-rule-maxlength",
                                Integer.toString(column.length()));
                    }
                }

                JoinColumn joinColumn = method.getAnnotation(JoinColumn.class);
                if (joinColumn != null) {
                    if (joinColumn.nullable() == false) {
                        if (tag.requiredLabel == null) {
                            uiBean.setRequiredLabel("true");
                        }
                    }
                }

                if (retType == Date.class) {
                    JsonSerialize jsonSerialize = method.getAnnotation(JsonSerialize.class);
                    if (jsonSerialize != null) {
                        if (DateJsonSerializer.class == jsonSerialize.using()) {
                            setupDynamicAttribute(dynamicAttributes, "data-rule-date", "true");
                            setupDynamicAttribute(dynamicAttributes, "data-rule-dateISO", "true");
                        } else if (DateTimeJsonSerializer.class == jsonSerialize.using()) {
                            setupDynamicAttribute(dynamicAttributes, "data-rule-timestamp", "true");
                        }
                    }
                } else if (retType == BigDecimal.class) {
                    setupDynamicAttribute(dynamicAttributes, "data-rule-number", "true");
                } else if (retType == Integer.class || retType == Long.class) {
                    setupDynamicAttribute(dynamicAttributes, "data-rule-number", "true");
                    setupDynamicAttribute(dynamicAttributes, "data-rule-digits", "true");
                }

                Size size = method.getAnnotation(Size.class);
                if (size != null) {
                    if (size.min() > 0) {
                        setupDynamicAttribute(dynamicAttributes, "data-rule-minlength", size.min());
                    }
                    if (size.max() < Integer.MAX_VALUE) {
                        setupDynamicAttribute(dynamicAttributes, "maxlength", Integer.toString(size.max()));
                        setupDynamicAttribute(dynamicAttributes, "data-rule-maxlength", Integer.toString(size.max()));
                    }
                }

                Email email = method.getAnnotation(Email.class);
                if (email != null) {
                    setupDynamicAttribute(dynamicAttributes, "data-rule-email", "true");
                }

                Pattern pattern = method.getAnnotation(Pattern.class);
                if (pattern != null) {
                    setupDynamicAttribute(dynamicAttributes, "data-rule-regex", pattern.regexp());
                    String message = pattern.message();
                    if (!"{javax.validation.constraints.Pattern.message}".equals(message)) {
                        setupDynamicAttribute(dynamicAttributes, "data-msg-regex", message);
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (OgnlException e) {
            e.printStackTrace();
        }
    }

    private static void setupDynamicAttribute(Map<String, Object> dynamicAttributes, String key, Object value) {
        if (dynamicAttributes.get(key) == null) {
            dynamicAttributes.put(key, value);
        }
    }
}
