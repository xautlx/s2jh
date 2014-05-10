package lab.s2jh.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.exception.WebException;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.RestActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ModelDriven;

public abstract class SimpleController extends RestActionSupport implements ModelDriven<Object> {

    private final Logger logger = LoggerFactory.getLogger(SimpleController.class);

    /** 请求URL可提供此参数指定转向特定JSP页面，如有相同处理方法返回相同数据，但是不同业务功能需要按照不同页面显示则可以指定此参数转向特定显示JSP页面*/
    protected static final String PARAM_NAME_FOR_FORWARD_TO = "_to_";

    /** ModelDriven对象 */
    protected Object model = null;

    /** 前端表单一些不需要处理的表单元素的name设置为ignore，以抑制Struts参数绑定OGNL异常 */
    protected String ignore = null;

    /**
     * ModelDriven接口回调实现方法
     */
    @Override
    public Object getModel() {
        return model;
    }

    /**
     * 用于子类方法修改设置返回的ModelDriven模型对象
     * @param model
     */
    protected void setModel(Object model) {
        this.model = model;
    }

    /**
     * 构造默认的REST返回响应，一般用于直接JSON数据输出
     * @return
     */
    protected DefaultHttpHeaders buildDefaultHttpHeaders() {
        return new DefaultHttpHeaders().disableCaching();
    }

    /**
     * 基于code参数构造默认的REST返回响应，一般用于JSP页面转向显示数据
     * @return
     */
    protected DefaultHttpHeaders buildDefaultHttpHeaders(String code) {
        String to = this.getParameter(PARAM_NAME_FOR_FORWARD_TO);
        if (StringUtils.isNotBlank(to)) {
            code = to;
        }
        return new DefaultHttpHeaders(code).disableCaching();
    }

    /**
     * 帮助类方法，方便获取HttpServletRequest
     * 
     * @return
     */
    protected HttpServletRequest getRequest() {
        HttpServletRequest request = ServletActionContext.getRequest();
        return request;
    }

    /**
     * 帮助类方法，方便获取HttpServletResponse
     * 
     * @return
     */
    protected HttpServletResponse getResponse() {
        HttpServletResponse response = ServletActionContext.getResponse();
        return response;
    }

    // ----------------------------------  
    // -----------请求参数处理帮助类方法--------
    // ----------------------------------

    /**
     * 获取必须参数值,如果参数为空则抛出异常
     * 
     * @param name 参数名称
     * @return 参数值
     */
    protected String getRequiredParameter(String name) {
        String value = getRequest().getParameter(name);
        if (StringUtils.isBlank(value)) {
            throw new WebException("web.param.disallow.empty: " + name);
        }
        return value;
    }

    /**
     * 获取参数值,如果未空白则返回缺省值
     * 
     * @param name 参数名称
     * @param name 如果参数为空返回的默认值
     * @return 参数值
     */
    protected String getParameter(String name, String defaultValue) {
        String value = getRequest().getParameter(name);
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 常规方式获取请求参数值
     * 
     * @param name  参数名称
     * @return 参数值
     */
    protected String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    // ----------------------------------  
    // -----------OGNL处理帮助类方法---------
    // ----------------------------------
    /**
     * 用于OGNL判断字符串不为Blank
     * @param str 判断字符串
     * @return
     */
    public boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    /**
     * 用于OGNL判断字符串为Blank
     * @param str 判断字符串
     * @return
     */
    public boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    /** 
     * 前端表单一些不需要处理的表单元素的name设置为ignore，以抑制Struts参数绑定OGNL异常 
     */
    public void setIgnore(String ignore) {
        //do nothing
    }

    // -------------------------------------
    // -----------通用的页面转向处理方法------------
    // -------------------------------------
    /**
     * 显示默认Index主界面
     * @return
     */
    public HttpHeaders index() {
        return buildDefaultHttpHeaders("index");
    }

    /**
     * 通用forwar转向方法，根据to转向到对应的JSP页面 如果to参数为空，则抛出必要参数缺失异常
     * 
     * @return
     */
    public String forward() {
        String to = this.getRequiredParameter(PARAM_NAME_FOR_FORWARD_TO);
        logger.debug("Direct forward to: {}", to);
        return to;
    }

    /**
     * 占位方法定义，无实际处理逻辑。细节逻辑在子类定义
     */
    @MetaData(value = "表格数据编辑校验规则")
    public HttpHeaders buildValidateRules() {
        setModel(Maps.newHashMap());
        return buildDefaultHttpHeaders();
    }
}
