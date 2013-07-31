package lab.s2jh.core.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.apache.struts2.util.TokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * 默认标准Token拦截器如果启用，则会判断所有表单是否包含token参数，如果没有则直接抛出异常
 * 扩展标准的TokenInterceptor，如果表单参数包含token才进行后续校验，否则直接跳过Token校验
 * 将当前Token搬迁到备份属性中,在前端Exception处理逻辑中,从备份属性恢复Token,从而使用户可以再次提交同一个token表单
 */
public class SmartTokenInterceptor extends TokenInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SmartTokenInterceptor.class);

    public static final String BACKUP_TOKEN_VALUE = "backup.token.value";

    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        Map<String, Object> params = ActionContext.getContext().getParameters();
        if (!params.containsKey(TokenHelper.TOKEN_NAME_FIELD)) {
            //logger.debug("TokenHelper.TOKEN_NAME_FIELD not found. Skip token interceptor.");
            return invocation.invoke();
        } else {
            logger.debug("TokenHelper.TOKEN_NAME_FIELD found.");

            //将Token保存到BACKUP_TOKEN_VALUE属性中，以备在出现异常时恢复token，使得用户可以再次提交
            String tokenName = TokenHelper.getTokenName();
            Map<String, Object> session = ActionContext.getContext().getSession();
            String tokenSessionName = TokenHelper.buildTokenSessionAttributeName(tokenName);
            String sessionToken = (String) session.get(tokenSessionName);
            HttpSession httpSession= ServletActionContext.getRequest().getSession();
            httpSession.setAttribute(BACKUP_TOKEN_VALUE, sessionToken);

            logger.debug("Invoke token interceptor.");
            return super.doIntercept(invocation);

        }
    }

}
