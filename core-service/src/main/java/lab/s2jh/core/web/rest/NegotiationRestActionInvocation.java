package lab.s2jh.core.web.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.RestActionInvocation;

public class NegotiationRestActionInvocation extends RestActionInvocation {

    protected NegotiationRestActionInvocation(Map<String, Object> extraContext, boolean pushAction) {
        super(extraContext, pushAction);
    }

    @Override
    protected void selectTarget() {
        super.selectTarget();
        //把值栈的异常对象放到request属性中便于errors.jsp页面获取使用
        Throwable e = (Throwable) stack.findValue("exception");
        if (e != null) {
            HttpServletRequest request = ServletActionContext.getRequest();
            request.setAttribute("struts.rest.error.exception", e);
        }
    }
}
