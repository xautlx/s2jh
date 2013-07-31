package org.apache.struts2.views.jsp;

import java.util.Collection;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import lab.s2jh.core.security.AuthContextHolder;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

/**
 * 基于当前登录用户的权限代码集合，按照code属性和权限比较type类型，控制页面元素的显示
 * 用法示例： <s2:privilege code="P001">受控HML内容，如按钮、文本等</s2:privilege>
 */
public class S2PrivilegeTag extends TagSupport {

    /** 权限代码，如果是多个可逗号分隔 */
    private String code;

    /** 多权限代码处理类型: hasAny=任何权限符合则通过,hasAll=要求全部匹配才通过,notAll=所有都必须不匹配, notAny=任何一个都不能匹配 */
    private String type = "hasAny";

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int doStartTag() throws JspException {
        Collection<String> authCodes = AuthContextHolder.getAuthUserDetails().getPrivilegeCodes();
        Set<String> codes = Sets.newHashSet(StringUtils.split(code, ","));
        if("hasAll".equalsIgnoreCase(type)){
            for (String c : codes) {
                if(!authCodes.contains(c.trim())){
                    return SKIP_BODY;
                }
            }
            return EVAL_BODY_INCLUDE;
        }else if("hasAny".equalsIgnoreCase(type)){
            for (String c : codes) {
                if(authCodes.contains(c.trim())){
                    return EVAL_BODY_INCLUDE;
                }
            }
            return SKIP_BODY;
        }else if("notAll".equalsIgnoreCase(type)){
            for (String c : codes) {
                if(authCodes.contains(c.trim())){
                    return SKIP_BODY;
                }
            }
            return EVAL_BODY_INCLUDE;
        }else if("notAny".equalsIgnoreCase(type)){
            for (String c : codes) {
                if(!authCodes.contains(c.trim())){
                    return EVAL_BODY_INCLUDE;
                }
            }
            return SKIP_BODY;
        }else{
            throw new IllegalArgumentException("Undefined type parameter: "+type);
        }
    }
}
