package lab.s2jh.core.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lab.s2jh.core.exception.DuplicateTokenException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 1,默认标准Token拦截器如果启用，则会判断所有表单是否包含token参数，如果没有则直接抛出异常
 * 扩展标准的TokenInterceptor，如果表单参数包含token才进行后续校验，否则直接跳过Token校验
 * 2,在最后再次生成Token,用于JSON类型Form请求异常处理后,以JS无刷新方式更新form表单中的token值以便用户可以再次无刷新页面提交表单
 * 
 */
public class ExtTokenInterceptor extends MethodFilterInterceptor {

    private static final long serialVersionUID = 5336675352033972132L;

    private static final Logger logger = LoggerFactory.getLogger(ExtTokenInterceptor.class);

    public static final String TOKEN_COUNTER = "struts.form.submit.counter.token";

    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        Map<String, Object> params = ActionContext.getContext().getParameters();
        if (params.get(TokenHelper.TOKEN_NAME_FIELD) != null) {
            logger.debug("TokenHelper.TOKEN_NAME_FIELD found.");
            HttpServletRequest request = ServletActionContext.getRequest();
            String token = request.getParameter(request.getParameter(TokenHelper.TOKEN_NAME_FIELD));
            HttpSession session = ServletActionContext.getRequest().getSession(true);
            synchronized (session) {
                String counterToken = (String) session.getAttribute(TOKEN_COUNTER);
                if (counterToken != null && token.equals(counterToken)) {
                    throw new DuplicateTokenException("The form has already been processed");
                }
                session.setAttribute(TOKEN_COUNTER, token);
            }
        }
        return invocation.invoke();
    }

}
