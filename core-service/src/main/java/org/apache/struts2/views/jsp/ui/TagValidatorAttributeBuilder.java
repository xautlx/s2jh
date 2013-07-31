package org.apache.struts2.views.jsp.ui;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lab.s2jh.core.web.json.DateJsonSerializer;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;
import lab.s2jh.core.web.json.JodaDateJsonSerializer;
import lab.s2jh.core.web.json.JodaDateTimeJsonSerializer;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.UIBean;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.CompoundRoot;
import com.opensymphony.xwork2.util.ValueStack;

public class TagValidatorAttributeBuilder {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    public static void buildValidatorAttribute(String validatorAttrValue, AbstractUITag tag, ValueStack stack,
            HttpServletRequest req, UIBean uiBean) {
        Map<String, Object> dynamicAttributes = new HashMap<String, Object>();
        if (validatorAttrValue == null) {
            Map<String, Object> validator = new HashMap<String, Object>();
            Map<String, String> messages = new HashMap<String, String>();
            CompoundRoot rootList = stack.getRoot();
            Object entity = null;
            Object controller = null;
            for (Object obj : rootList) {
                if (obj instanceof Persistable) {
                    entity = obj;
                } else if (obj instanceof ActionSupport) {
                    controller = obj;
                }
            }

            if (entity != null) {
                try {
                    String tagName = tag.name;
                    Method method = OgnlRuntime.getGetMethod((OgnlContext) stack.getContext(), entity.getClass(),
                            tagName);
                    if (method == null) {
                        String[] tagNameSplits = StringUtils.split(tagName, ".");
                        if (tagNameSplits.length >= 2) {
                            Class<?> retClass = entity.getClass();
                            for (String tagNameSplit : tagNameSplits) {
                                method = OgnlRuntime.getGetMethod((OgnlContext) stack.getContext(), retClass,
                                        tagNameSplit);
                                if (method != null) {
                                    retClass = method.getReturnType();
                                }
                            }
                            if (method == null) {
                                retClass = controller.getClass();
                                for (String tagNameSplit : tagNameSplits) {
                                    method = OgnlRuntime.getGetMethod((OgnlContext) stack.getContext(), retClass,
                                            tagNameSplit);
                                    if (method != null) {
                                        retClass = method.getReturnType();
                                    }
                                }
                            }
                        }
                    }

                    if (method != null) {
                        Column column = method.getAnnotation(Column.class);
                        if (column != null) {
                            if (column.nullable() == false) {
                                if (tag.requiredLabel == null) {
                                    uiBean.setRequiredLabel("true");
                                }
                            }
                            if (column.unique() == true) {
                                validator.put("unique", "true");
                            }
                        }

                        Class<?> retType = method.getReturnType();
                        if (retType == LocalDate.class) {
                            validator.put("date", true);
                            validator.put("dateISO", true);
                        } else if (retType == LocalDateTime.class) {
                            validator.put("timestamp", true);
                        } else if (retType == DateTime.class || retType == Date.class) {
                            JsonSerialize jsonSerialize = method.getAnnotation(JsonSerialize.class);
                            if (jsonSerialize != null) {
                                if (JodaDateJsonSerializer.class == jsonSerialize.using()
                                        || DateJsonSerializer.class == jsonSerialize.using()) {
                                    validator.put("date", true);
                                    validator.put("dateISO", true);
                                } else if (JodaDateTimeJsonSerializer.class == jsonSerialize.using()
                                        || DateTimeJsonSerializer.class == jsonSerialize.using()) {
                                    validator.put("timestamp", true);
                                }
                            }
                        } else if (retType == BigDecimal.class) {
                            validator.put("number", true);
                        } else if (retType == Integer.class) {
                            validator.put("number", true);
                            validator.put("digits", true);
                        } else if (retType == Long.class) {
                            validator.put("number", true);
                            validator.put("digits", true);
                        }

                        Size size = method.getAnnotation(Size.class);
                        if (size != null) {
                            if (size.min() > 0) {
                                validator.put("minlength", size.min());
                            }
                            if (size.max() < Integer.MAX_VALUE) {
                                validator.put("maxlength", size.max());
                            }
                        }

                        Email email = method.getAnnotation(Email.class);
                        if (email != null) {
                            validator.put("email", true);
                        }

                        Pattern pattern = method.getAnnotation(Pattern.class);
                        if (pattern != null) {
                            validator.put("regex", pattern.regexp());
                            String message = pattern.message();
                            if (!"{javax.validation.constraints.Pattern.message}".equals(message)) {
                                messages.put("regex", message);
                            }
                        }
                    }
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                } catch (OgnlException e) {
                    e.printStackTrace();
                }
            }

            if (validator.size() > 0) {
                try {
                    if (messages.size() > 0) {
                        validator.put("messages", messages);
                    }
                    String json = mapper.writeValueAsString(validator);
                    json = json.replaceAll("\\\"", "'");
                    dynamicAttributes.put("validator", json);
                    uiBean.setDynamicAttributes(dynamicAttributes);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            dynamicAttributes.put("validator", validatorAttrValue);
            uiBean.setDynamicAttributes(dynamicAttributes);
        }
    }
}
